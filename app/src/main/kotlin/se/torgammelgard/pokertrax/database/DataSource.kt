package se.torgammelgard.pokertrax.database

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.db.SupportSQLiteOpenHelper
import android.arch.persistence.db.SupportSQLiteQueryBuilder
import android.arch.persistence.room.OnConflictStrategy
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.util.Log
import se.torgammelgard.pokertrax.model.GameStructure
import se.torgammelgard.pokertrax.model.GameType
import se.torgammelgard.pokertrax.model.Session
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

class DataSource(context: Context) {
    private var database: SupportSQLiteDatabase? = null
    private val dbHelper: SupportSQLiteOpenHelper = AppDatabase.getInstance(context)!!.openHelper
    private val formatter = SimpleDateFormat("yyyy-MM-dd")

    private val allColumns = arrayOf(SessionTable.COLUMN_ID, SessionTable.COLUMN_GAMETYPE, SessionTable.COLUMN_LOCATION, SessionTable.COLUMN_GAMESTRUCTURE, SessionTable.COLUMN_DURATION, SessionTable.COLUMN_DATE, SessionTable.COLUMN_RESULT, SessionTable.COLUMN_GAMENOTES)

    fun close() {
        database!!.close()
    }

    /**
     * @returns the total number of sessions
     */
    val entriesCount: Int
        get() {
            var count = 0
            try {
                database = dbHelper.readableDatabase

                val cursor = database!!.query("SELECT count(*) FROM " + SessionTable.TABLE_SESSION,
                        null)
                cursor.moveToFirst()
                count = cursor.getInt(0)
                cursor.close()
            } catch (e: SQLiteException) {
                Log.d(LOG, "Failed to connect to database", e)
            }

            return count
        }

    /**
     * @return total game play in minutes
     */
    val totalTimePlayed: Int
        get() {
            var total = 0
            try {
                database = dbHelper.readableDatabase
                val query = SupportSQLiteQueryBuilder.builder(SessionTable.TABLE_SESSION)
                        .columns(arrayOf("TOTAL(" + SessionTable.COLUMN_DURATION + ")"))
                        .create()
                val cursor = database!!.query(query)
                cursor.moveToFirst()
                total = cursor.getInt(0)
                cursor.close()
            } catch (e: SQLiteException) {
                Log.e(LOG, "Failed to connect to database", e)
            }

            return total
        }

    val averageBigBetPerHour: Double
        get() {
            var average = 0.0
            try {
                database = dbHelper.readableDatabase
                val sessionCursor = database!!.query(
                        "SELECT total(CAST (" + SessionTable.COLUMN_RESULT + " AS float)/(SELECT " +
                                GameStructureTable.COLUMN_BIGBLIND + " FROM " +
                                GameStructureTable.TABLE_GAME_STRUCTURE +
                                " WHERE " + GameStructureTable.COLUMN_ID + " = " +
                                SessionTable.COLUMN_GAMESTRUCTURE + "))/total(CAST (" + SessionTable.COLUMN_DURATION +
                                " AS float)/?) FROM " + SessionTable.TABLE_SESSION,
                        arrayOf(60.toString())
                )
                sessionCursor.moveToFirst()
                average = sessionCursor.getDouble(0)
                sessionCursor.close()
            } catch (e: SQLiteException) {
                Log.d(LOG, "Failed to connect to database", e)
            }

            return average
        }

    /** Returns a list of all the GameTypes  */
    val allGameTypes: ArrayList<String>?
        get() {
            var gameTypes: ArrayList<String>? = null
            try {
                database = dbHelper.readableDatabase
                gameTypes = ArrayList()
                val query = SupportSQLiteQueryBuilder.builder(GameTypeTable.TABLE_GAMETYPE)
                        .create()
                val cursor = database!!.query(query)
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val gameType = cursorToGameType(cursor)
                    gameTypes.add(gameType.getOutputString())
                    cursor.moveToNext()
                }
                cursor.close()
                dbHelper.close()
            } catch (e: SQLiteException) {
                Log.e(LOG, "Failed to connect to database", e)
            }

            return gameTypes
        }

    val allGameStructures: ArrayList<GameStructure>?
        get() {
            var gameStructures: ArrayList<GameStructure>? = null
            try {
                database = dbHelper.readableDatabase
                gameStructures = ArrayList()
                val query = SupportSQLiteQueryBuilder.builder(GameStructureTable.TABLE_GAME_STRUCTURE).create()
                val cursor = database!!.query(query)
                while (cursor.moveToNext()) {
                    val gameStructure = cursorToGameStructure(cursor)
                    gameStructures.add(gameStructure)
                }
                cursor.close()
                dbHelper.close()
            } catch (e: SQLiteException) {
                Log.e(LOG, "Failed to connect to database", e)
            }

            return gameStructures
        }

    fun addGameStructure(gs: GameStructure) {
        try {
            database = dbHelper.writableDatabase
            val cv = ContentValues()
            cv.put(GameStructureTable.COLUMN_SMALLBLIND, gs.small_blind)
            cv.put(GameStructureTable.COLUMN_BIGBLIND, gs.big_blind)
            cv.put(GameStructureTable.COLUMN_ANTE, gs.ante)
            database!!.insert(GameStructureTable.TABLE_GAME_STRUCTURE, SQLiteDatabase.CONFLICT_REPLACE, cv)
            dbHelper.close()
        } catch (e: SQLiteException) {
            Log.e(LOG, "Failed to connect to database", e)
        }

    }

    /**
     * @param maxNumberOfSessions needs to be positive
     *  @returns the last sessions
     */
    fun getLastSessions(maxNumberOfSessions: Int): ArrayList<Session>? {
        var sessions: ArrayList<Session>? = null
        try {
            database = dbHelper.readableDatabase
            sessions = ArrayList()
            val cursor = database!!.query("SELECT * FROM (SELECT * FROM " +
                    SessionTable.TABLE_SESSION + " ORDER BY " +
                    SessionTable.COLUMN_ID + " DESC LIMIT ?)" +
                    " ORDER BY " + SessionTable.COLUMN_ID + " ASC;",
                    arrayOf(maxNumberOfSessions.toString()))
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val session = cursorToSession(cursor)
                sessions.add(session)
                cursor.moveToNext()
            }
            cursor.close()
            dbHelper.close()
        } catch (e: SQLiteException) {
            Log.e(LOG, "Failed to connect to database", e)
        }

        return sessions
    }


    /**
     *  @returns results from all different game types
     */
    val resultsFromGameTypes: ArrayList<String>
        get() {
            val resultList = ArrayList<String>()
            try {
                database = dbHelper.readableDatabase
                val gameTypes = ArrayList<GameType>()
                val query = SupportSQLiteQueryBuilder.builder(GameTypeTable.TABLE_GAMETYPE)
                        .create()
                val cursor = database!!.query(query)
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val gameType = cursorToGameType(cursor)
                    gameTypes.add(gameType)
                    cursor.moveToNext()
                }
                cursor.close()

                for (game_type in gameTypes) {
                    val query2 = SupportSQLiteQueryBuilder.builder(SessionTable.TABLE_SESSION)
                            .columns(arrayOf(SessionTable.COLUMN_RESULT))
                            .having(SessionTable.COLUMN_GAMETYPE + " = " + game_type.id.toString())
                            .create()
                    val resultCursor = database!!.query(query2)
                    resultCursor.moveToFirst()
                    var result = 0
                    while (!resultCursor.isAfterLast) {
                        result += resultCursor.getInt(0)
                        resultCursor.moveToNext()
                    }
                    resultList.add(result.toString())
                    resultCursor.close()
                }
            } catch (e: SQLiteException) {
                Log.e(LOG, "Failed to connect to database", e)
            }

            return resultList
        }

    /**
     * @returns all locations
     */
    val locations: ArrayList<String>
        get() {
            val locations = ArrayList<String>()
            try {
                database = dbHelper.readableDatabase
                val query = SupportSQLiteQueryBuilder.builder(SessionTable.TABLE_SESSION)
                        .columns(arrayOf(SessionTable.COLUMN_LOCATION))
                        .create()
                val cursor = database!!.query(query)
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    locations.add(cursor.getString(cursor.getColumnIndex(SessionTable.COLUMN_LOCATION)))
                    cursor.moveToNext()
                }
                cursor.close()
            } catch (e: SQLiteException) {
                Log.e(LOG, "Failed to connect to database", e)
            }

            return locations
        }

    /**
     * Adds a session to the database
     *
     * @returns the added session
     */
    fun addSession(session: Session): Session? {
        val values = ContentValues()
        values.put(SessionTable.COLUMN_GAMETYPE, session.game_type_ref)
        values.put(SessionTable.COLUMN_LOCATION, session.location)
        values.put(SessionTable.COLUMN_GAMESTRUCTURE, session.game_structure_ref)
        values.put(SessionTable.COLUMN_DURATION, session.duration)
        values.put(SessionTable.COLUMN_DATE, formatter.format(session.date))
        values.put(SessionTable.COLUMN_RESULT, session.result)
        values.put(SessionTable.COLUMN_GAMENOTES, session.game_notes)

        var newSession: Session? = null
        try {
            database = dbHelper.writableDatabase
            val id = database!!.insert(SessionTable.TABLE_SESSION, OnConflictStrategy.REPLACE, values)
            val query = SupportSQLiteQueryBuilder.builder(SessionTable.TABLE_SESSION)
                    .columns(allColumns)
                    .having(SessionTable.COLUMN_ID + " = " + id)
                    .create()
            val cursor = database!!.query(query)
            cursor.moveToFirst()
            newSession = cursorToSession(cursor)
            cursor.close()
            dbHelper.close()
        } catch (e: SQLiteException) {
            Log.e(LOG, "Failed to connect to database", e)
        }

        return newSession
    }

    /**
     * Deletes the session
     *
     * @param sessionID the id of the session to be deleted
     */
    fun deleteSession(sessionID: Long) {
        try {
            database = dbHelper.writableDatabase
            database!!.delete(SessionTable.TABLE_SESSION,
                    SessionTable.COLUMN_ID + " = " + sessionID, null)
            dbHelper.close()
        } catch (e: SQLiteException) {
            Log.e(LOG, "Failed to connect to database", e)
        }

    }

    private fun cursorToGameType(cursor: Cursor): GameType {
        val gameType = GameType()
        gameType.id = cursor.getLong(0)
        gameType.type = cursor.getString(1)
        return gameType
    }

    private fun cursorToGameStructure(cursor: Cursor): GameStructure {
        val gameStructure = GameStructure()
        gameStructure.id = cursor.getLong(0)
        gameStructure.small_blind = cursor.getInt(1)
        gameStructure.big_blind = cursor.getInt(2)
        gameStructure.ante = cursor.getInt(3)
        return gameStructure
    }

    private fun cursorToSession(cursor: Cursor): Session {
        val session = Session()
        session.id = cursor.getLong(0)
        session.game_type_ref = cursor.getInt(1)
        session.location = cursor.getString(2)
        session.game_structure_ref = cursor.getInt(3)
        session.duration = cursor.getInt(4)
        session.date = formatter.parse(cursor.getString(5), ParsePosition(0))
        session.result = cursor.getInt(6)
        session.game_notes = cursor.getString(7)
        return session
    }

    companion object {
        private const val LOG = "DataSource"
    }
}
