package se.torgammelgard.pokertrax.database

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

import java.text.SimpleDateFormat
import java.util.Date

object SqliteDatabaseTestHelper {

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

    internal fun insertGameType(id: Int, name: String, helper: SqliteTestDbOpenHelper) {
        val db = helper.writableDatabase

        val values = ContentValues()
        values.put("_id", id)
        values.put("name", name)

        db.insertWithOnConflict("game_type", null, values,
                SQLiteDatabase.CONFLICT_REPLACE)

        db.close()
    }

    internal fun insertSession(id: Int, game_type: Int, location: String, gameStructureReference: Int, duration: Int, date: Date, result: Int, game_notes: String, helper: SqliteTestDbOpenHelper) {
        val db = helper.writableDatabase

        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val values = ContentValues()
        values.put("_id", id)
        values.put("game_type", game_type)
        values.put("location", location)
        values.put("game_structure", gameStructureReference)
        values.put("duration", duration)
        values.put("date", formatter.format(date))
        values.put("result", result)
        values.put("game_info", game_notes)

        db.insertWithOnConflict("session", null, values,
                SQLiteDatabase.CONFLICT_REPLACE)

        db.close()
    }

    internal fun createTable(helper: SqliteTestDbOpenHelper) {
        val db = helper.writableDatabase
        db.execSQL("CREATE TABLE IF NOT EXISTS game_structure (_id INTEGER PRIMARY KEY, " + "small_blind INT, big_blind INT, ante INT)")

        db.execSQL("CREATE TABLE IF NOT EXISTS game_type (_id INTEGER PRIMARY KEY, name TEXT)")

        db.execSQL("CREATE TABLE IF NOT EXISTS session (_id INTEGER PRIMARY KEY, "
                + "game_type INTEGER, location TEXT, game_structure INTEGER, "
                + "duration INTEGER, date DATE, result INTEGER, game_info TEXT, "
                + "FOREIGN KEY(game_type) REFERENCES game_type, "
                + "FOREIGN KEY(game_structure) REFERENCES game_structure)")
        db.close()
    }

    internal fun clearDatabase(helper: SqliteTestDbOpenHelper) {
        val db = helper.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS game_structure")
        db.execSQL("DROP TABLE IF EXISTS game_type")
        db.execSQL("DROP TABLE IF EXISTS session")
        db.close()
    }
}
