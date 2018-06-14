package se.torgammelgard.pokertrax.database

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

import java.text.SimpleDateFormat
import java.util.Date

object SqliteDatabaseTestHelper {

    /** Used in androidTests to insert a (old) GameStructure in SQLite3 */
    internal fun insertGameStructure(id: Int, smallBlind: Int, bigBlind: Int, ante: Int, helper: SqliteTestDbOpenHelper) {
        val db = helper.writableDatabase
        val values = ContentValues()
        values.put("_id", id)
        values.put("small_blind", smallBlind)
        values.put("big_blind", bigBlind)
        values.put("ante", ante)

        db.insertWithOnConflict("game_structure", null, values,
                SQLiteDatabase.CONFLICT_REPLACE)

        db.close()
    }

    /** Used in androidTests to insert a (old) GameType in SQLite3 */
    internal fun insertGameType(id: Int, name: String, helper: SqliteTestDbOpenHelper) {
        val db = helper.writableDatabase

        val values = ContentValues()
        values.put("_id", id)
        values.put("name", name)

        db.insertWithOnConflict("game_type", null, values,
                SQLiteDatabase.CONFLICT_REPLACE)

        db.close()
    }

    /** Used in androidTests to insert a (old) Session in SQLite3 */
    internal fun insertSession(id: Int, game_type: Int, location: String, gameStructureReference: Int, duration: Int, date: Date, result: Int, game_notes: String, helper: SqliteTestDbOpenHelper) {
        val db = helper.writableDatabase
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        db.execSQL("INSERT INTO 'session' (" +
                "game_type, location, game_structure, duration, date, result, game_info) " +
                "VALUES (" +
                "$game_type, '$location', $gameStructureReference, $duration, '${formatter.format(date)}', $result, '$game_notes')")
        db.close()
    }

    /** This will get run before all the database androidTests */
    internal fun createTable(helper: SqliteTestDbOpenHelper) {
        // this will run the onCreate in SqliteTestDbOpenHelper
        val db = helper.writableDatabase

        db.execSQL("CREATE TABLE IF NOT EXISTS game_structure (_id INTEGER PRIMARY KEY, small_blind INT, big_blind INT, ante INT)")

        db.execSQL("CREATE TABLE IF NOT EXISTS game_type (_id INTEGER PRIMARY KEY, name TEXT)")

        db.execSQL("CREATE TABLE IF NOT EXISTS session (_id INTEGER PRIMARY KEY, "
                + "game_type INTEGER, location TEXT, game_structure INTEGER, "
                + "duration INTEGER, date DATE, result INTEGER, game_info TEXT, "
                + "FOREIGN KEY(game_type) REFERENCES game_type, "
                + "FOREIGN KEY(game_structure) REFERENCES game_structure)")
        db.close()
    }

    /** This will get run after all the database androidTests */
    internal fun clearDatabase(helper: SqliteTestDbOpenHelper) {
        val db = helper.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS game_structure")
        db.execSQL("DROP TABLE IF EXISTS game_type")
        db.execSQL("DROP TABLE IF EXISTS session")
        db.close()
    }
}
