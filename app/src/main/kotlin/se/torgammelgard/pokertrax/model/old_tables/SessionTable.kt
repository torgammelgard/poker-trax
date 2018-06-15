package se.torgammelgard.pokertrax.model.old_tables

import android.database.sqlite.SQLiteDatabase

/**
 * TODO: Class header comment.
 */
object SessionTable {
    val TABLE_SESSION = "session"
    val COLUMN_ID = "_id"
    val COLUMN_GAMETYPE = "game_type"
    val COLUMN_LOCATION = "location"
    val COLUMN_GAMESTRUCTURE = "game_structure"
    val COLUMN_DURATION = "duration"
    val COLUMN_DATE = "date"
    val COLUMN_RESULT = "result"
    val COLUMN_GAMENOTES = "game_info"

    private val TABLE_CREATE = "CREATE TABLE " + TABLE_SESSION + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_GAMETYPE + " INTEGER, " +
            COLUMN_LOCATION + " TEXT, " +
            COLUMN_GAMESTRUCTURE + " INTEGER, " +
            COLUMN_DURATION + " INTEGER, " +
            COLUMN_DATE + " DATE, " +
            COLUMN_RESULT + " INTEGER, " +
            COLUMN_GAMENOTES + " TEXT, " +
            "FOREIGN KEY(game_type) REFERENCES game_type, " +
            "FOREIGN KEY(game_structure) REFERENCES game_structure);"

    fun onCreate(database: SQLiteDatabase) {
        database.execSQL(TABLE_CREATE)
    }

    fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSION)
        onCreate(database)
    }
}
