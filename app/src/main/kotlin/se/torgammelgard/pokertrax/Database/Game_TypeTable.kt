package se.torgammelgard.pokertrax.Database

import android.database.sqlite.SQLiteDatabase

/**
 * TODO: Class header comment.
 */
object Game_TypeTable {

    val TABLE_GAMETYPE = "game_type"
    val COLUMN_ID = "_id"
    val COLUMN_NAME = "name"
    private val TABLE_CREATE = "CREATE TABLE " + TABLE_GAMETYPE +
            "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_NAME + " TEXT" +
            ");"

    fun onCreate(database: SQLiteDatabase) {
        database.execSQL(TABLE_CREATE)
    }

    fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMETYPE)
        onCreate(database)
    }
}
