package se.torgammelgard.pokertrax.Database

import android.database.sqlite.SQLiteDatabase

/**
 * TODO: Class header comment and change blinds to REAL or FLOAT
 */
object Game_StructureTable {

    val TABLE_GAME_STRUCTURE = "game_structure"
    val COLUMN_ID = "_id"
    val COLUMN_SMALLBLIND = "small_blind"
    val COLUMN_BIGBLIND = "big_blind"
    val COLUMN_ANTE = "ante"
    private val TABLE_CREATE = "CREATE TABLE " + TABLE_GAME_STRUCTURE +
            "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_SMALLBLIND + " small_blind INT, " +
            COLUMN_BIGBLIND + " big_blind INT, " +
            COLUMN_ANTE + " ante INT" +
            ");"

    fun onCreate(database: SQLiteDatabase) {
        database.execSQL(TABLE_CREATE)
    }

    fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_GAME_STRUCTURE)
        onCreate(database)
    }
}
