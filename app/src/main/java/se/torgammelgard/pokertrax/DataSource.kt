package se.torgammelgard.pokertrax

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.util.Log

import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.ArrayList

import se.torgammelgard.pokertrax.Database.Game_StructureTable
import se.torgammelgard.pokertrax.Database.Game_TypeTable
import se.torgammelgard.pokertrax.Database.MySQLiteHelper
import se.torgammelgard.pokertrax.Database.SessionTable
import se.torgammelgard.pokertrax.model.Session

class DataSource(context: Context) {
    private var database: SQLiteDatabase? = null
    private val dbHelper: MySQLiteHelper = MySQLiteHelper.getInstance(context)
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
                val cursor = database!!.rawQuery("SELECT count(*) FROM " + SessionTable.TABLE_SESSION,
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
                val cursor = database!!.query(SessionTable.TABLE_SESSION,
                        arrayOf("TOTAL(" + SessionTable.COLUMN_DURATION + ")"), null, null, null, null, null)
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
                val sessionCursor = database!!.rawQuery(
                        "SELECT total(CAST (" + SessionTable.COLUMN_RESULT + " AS float)/(SELECT " +
                                Game_StructureTable.COLUMN_BIGBLIND + " FROM " +
                                Game_StructureTable.TABLE_GAME_STRUCTURE +
                                " WHERE " + Game_StructureTable.COLUMN_ID + " = " +
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
                val cursor = database!!.query(SessionTable.TABLE_SESSION,
                        arrayOf("TOTAL(" + SessionTable.COLUMN_RESULT + ")"), null, null, null, null, null)
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
                val cursor = database!!.query(Game_TypeTable.TABLE_GAMETYPE, null, null, null, null, null, null)
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val game_type = cursorToGameType(cursor)
                    game_types.add(game_type.toString())
                    cursor.moveToNext()
                }
                cursor.close()
                database!!.close()
            } catch (e: SQLiteException) {
                Log.e(LOG, "Failed to connect to database", e)
            }

            return game_types
        }

    val allGameStructures: ArrayList<Game_Structure>?
        get() {
            var game_structures: ArrayList<Game_Structure>? = null
            try {
                database = dbHelper.readableDatabase
                game_structures = ArrayList<Game_Structure>()
                val cursor = database!!.query(Game_StructureTable.TABLE_GAME_STRUCTURE, null, null, null, null, null, null)

                while (cursor.moveToNext()) {
                    val game_structure = cursorToGameStructure(cursor)
                    game_structures.add(game_structure)
                }
                cursor.close()
                database!!.close()
            } catch (e: SQLiteException) {
                Log.e(LOG, "Failed to connect to database", e)
            }

            return game_structures
        }

    fun addGameStructure(gs: Game_Structure) {
        try {
            database = dbHelper.writableDatabase
            val cv = ContentValues()
            cv.put(Game_StructureTable.COLUMN_SMALLBLIND, gs.small_blind)
            cv.put(Game_StructureTable.COLUMN_BIGBLIND, gs.big_blind)
            cv.put(Game_StructureTable.COLUMN_ANTE, gs.ante)
            database!!.insert(Game_StructureTable.TABLE_GAME_STRUCTURE, null, cv)
            database!!.close()
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
            val cursor = database!!.rawQuery("SELECT * FROM (SELECT * FROM " +
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
            database!!.close()
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
                val game_types = ArrayList<Game_type>()
                val cursor = database!!.query(Game_TypeTable.TABLE_GAMETYPE, null, null, null, null, null, null)
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val game_type = cursorToGameType(cursor)
                    game_types.add(game_type)
                    cursor.moveToNext()
                }
                cursor.close()

                for (game_type in game_types) {

                    val resultCursor = database!!.query(
                            SessionTable.TABLE_SESSION,
                            arrayOf(SessionTable.COLUMN_RESULT),
                            SessionTable.COLUMN_GAMETYPE + " = " + game_type.id.toString(), null, null, null, null)
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

    // won't work because we closed the database already (I think)
    val someSessionTables: Cursor?
        get() {
            var cursor: Cursor? = null
            try {
                database = dbHelper.readableDatabase
                cursor = database!!.query(SessionTable.TABLE_SESSION,
                        arrayOf(SessionTable.COLUMN_ID, SessionTable.COLUMN_DURATION, SessionTable.COLUMN_RESULT), null, null, null, null, null)
                database!!.close()
            } catch (e: SQLiteException) {
                Log.e(LOG, "Failed to connect to database", e)
            }

            return cursor
        }

    val locations: ArrayList<String>
        get() {
            val locations = ArrayList<String>()
            try {
                database = dbHelper.readableDatabase
                val cursor = database!!.query(
                        true,
                        SessionTable.TABLE_SESSION,
                        arrayOf(SessionTable.COLUMN_LOCATION), null, null, null, null, null, null)
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
            val id = database!!.insertWithOnConflict(SessionTable.TABLE_SESSION, null, values,
                    SQLiteDatabase.CONFLICT_IGNORE)
            val cursor = database!!.query(SessionTable.TABLE_SESSION,
                    allColumns,
                    SessionTable.COLUMN_ID + " = " + id, null, null, null, null)
            cursor.moveToFirst()
            newSession = cursorToSession(cursor)
            cursor.close()
            database!!.close()
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
            database!!.close()
        } catch (e: SQLiteException) {
            Log.e(LOG, "Failed to connect to database", e)
        }

    }

    private fun cursorToGameType(cursor: Cursor): Game_type {
        val game_type = Game_type()
        game_type.id = cursor.getLong(0)
        game_type.type = cursor.getString(1)
        return game_type
    }

    private fun cursorToGameStructure(cursor: Cursor): Game_Structure {
        val game_structure = Game_Structure()
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
