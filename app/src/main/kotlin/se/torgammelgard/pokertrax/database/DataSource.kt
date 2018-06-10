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

    /* Returns total game play in minutes*/
    val totalTimePlayed: Int
        get() {
            var total = 0
            try {
                database = dbHelper.readableDatabase
                //val cursor = database!!.query(SessionTable.TABLE_SESSION,
                //        arrayOf("TOTAL(" + SessionTable.COLUMN_DURATION + ")"), null, null, null, null, null)
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

    val avgbbPH: Double
        get() {
            var avg = 0.0
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
                avg = sessionCursor.getDouble(0)
                sessionCursor.close()
            } catch (e: SQLiteException) {
                Log.d(LOG, "Failed to connect to database", e)
            }

            return avg
        }


    val totalProfit: Int
        get() {
            var total = 0
            try {
                database = dbHelper.readableDatabase
                //val cursor = database!!.query(SessionTable.TABLE_SESSION,
                //        arrayOf("TOTAL(" + SessionTable.COLUMN_RESULT + ")"), null, null, null, null, null)
                val query = SupportSQLiteQueryBuilder.builder(SessionTable.TABLE_SESSION)
                        .columns(arrayOf("TOTAL(" + SessionTable.COLUMN_RESULT + ")"))
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

    /** Returns a list of all the GameTypes  */
    val allGameTypes: ArrayList<String>?
        get() {
            var game_types: ArrayList<String>? = null
            try {
                database = dbHelper.readableDatabase
                game_types = ArrayList<String>()
                val query = SupportSQLiteQueryBuilder.builder(GameTypeTable.TABLE_GAMETYPE)
                        .create()
                //val cursor = database!!.query(GameTypeTable.TABLE_GAMETYPE, null, null, null, null, null, null)
                val cursor = database!!.query(query)
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val game_type = cursorToGameType(cursor)
                    game_types.add(game_type.getOutputString())
                    cursor.moveToNext()
                }
                cursor.close()
                dbHelper.close()
            } catch (e: SQLiteException) {
                Log.e(LOG, "Failed to connect to database", e)
            }

            return game_types
        }

    val allGameStructures: ArrayList<GameStructure>?
        get() {
            var game_structures: ArrayList<GameStructure>? = null
            try {
                database = dbHelper.readableDatabase
                game_structures = ArrayList<GameStructure>()
                val query = SupportSQLiteQueryBuilder.builder(GameStructureTable.TABLE_GAME_STRUCTURE).create()
                //val cursor = database!!.query(GameStructureTable.TABLE_GAME_STRUCTURE, null, null, null, null, null, null)
                val cursor = database!!.query(query)
                while (cursor.moveToNext()) {
                    val game_structure = cursorToGameStructure(cursor)
                    game_structures.add(game_structure)
                }
                cursor.close()
                dbHelper.close()
            } catch (e: SQLiteException) {
                Log.e(LOG, "Failed to connect to database", e)
            }

            return game_structures
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

    /* returns the last sessions, max number needs to be positive */
    fun getLastSessions(maxNumberOfSessions: Int): ArrayList<Session>? {
        var sessions: ArrayList<Session>? = null
        try {
            database = dbHelper.readableDatabase
            sessions = ArrayList<Session>()
            //Cursor cursor = database.query(SessionTable.TABLE_SESSION, null, null, null,
            //        null, null, SessionTable.COLUMN_ID + " ASC", "3");
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


    /** returns a list over the result from all different game types  */
    val resultFromGametypes: ArrayList<String>
        get() {
            val resultList = ArrayList<String>()
            try {
                database = dbHelper.readableDatabase
                val game_types = ArrayList<GameType>()
                //val cursor = database!!.query(GameTypeTable.TABLE_GAMETYPE, null, null, null, null, null, null)
                val query = SupportSQLiteQueryBuilder.builder(GameTypeTable.TABLE_GAMETYPE)
                        .create()
                val cursor = database!!.query(query)
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val game_type = cursorToGameType(cursor)
                    game_types.add(game_type)
                    cursor.moveToNext()
                }
                cursor.close()

                for (game_type in game_types) {
                    val query2 = SupportSQLiteQueryBuilder.builder(SessionTable.TABLE_SESSION)
                            .columns(arrayOf(SessionTable.COLUMN_RESULT))
                            .having(SessionTable.COLUMN_GAMETYPE + " = " + game_type.id.toString())
                            .create()

                    /*val resultCursor = database!!.query(
                            SessionTable.TABLE_SESSION,
                            arrayOf(SessionTable.COLUMN_RESULT),
                            SessionTable.COLUMN_GAMETYPE + " = " + game_type.id.toString(), null, null, null, null)
                            */
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

    val locations: ArrayList<String>
        get() {
            val locations = ArrayList<String>()
            try {
                database = dbHelper.readableDatabase
                /*
                val cursor = database!!.query(

                        true,
                        SessionTable.TABLE_SESSION,
                        arrayOf(SessionTable.COLUMN_LOCATION), null, null, null, null, null, null)
                */
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
            //val cursor = database!!.query(SessionTable.TABLE_SESSION,
            //        allColumns,
            //        SessionTable.COLUMN_ID + " = " + id, null, null, null, null)
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

    fun deleteSession(sessionID: Long) {
        //long id = session.getId();
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
        val game_type = GameType()
        game_type.id = cursor.getLong(0)
        game_type.type = cursor.getString(1)
        return game_type
    }

    private fun cursorToGameStructure(cursor: Cursor): GameStructure {
        val game_structure = GameStructure()
        game_structure.id = cursor.getLong(0)
        game_structure.small_blind = cursor.getInt(1)
        game_structure.big_blind = cursor.getInt(2)
        game_structure.ante = cursor.getInt(3)
        return game_structure
    }

    private fun cursorToSession(cursor: Cursor): Session {
        val session = Session()
        session.id = cursor.getLong(0)
        session.game_type_ref = cursor.getInt(1)
        session.location = cursor.getString(2)
        session.game_structure_ref = cursor.getInt(3)
        session.duration = cursor.getInt(4)
        session.date = formatter.parse(cursor.getString(5), ParsePosition(0)) //TODO checkDate
        session.result = cursor.getInt(6)
        session.game_notes = cursor.getString(7)
        return session
    }

    companion object {
        private val LOG = "DataSource"
    }
}
