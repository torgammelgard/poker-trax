package com.example.torgammelgard.pokerhourly;

import android.database.sqlite.SQLiteDatabase;

/**
 * TODO: Class header comment and change blinds to REAL or FLOAT
 */
public class Game_StructureTable {

    public static final String TABLE_STRUCTURETABLE = "game_structure";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SMALLBLIND = "small_blind";
    public static final String COLUMN_BIGBLIND = "big_blind";
    public static final String COLUMN_ANTE = "ante";
    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_STRUCTURETABLE +
            "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_SMALLBLIND + " small_blind INT, " +
            COLUMN_BIGBLIND + " big_blind INT, " +
            COLUMN_ANTE + " ante INT" +
            ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(TABLE_CREATE);
    }

    public static void onUpdate(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_STRUCTURETABLE);
        onCreate(database);
    }
}
