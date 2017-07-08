package com.example.torgammelgard.pokerhourly.Database;

import android.database.sqlite.SQLiteDatabase;

/**
 * TODO: Class header comment.
 */
public class SessionTable {
    public static final String TABLE_SESSION = "session";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_GAMETYPE = "game_type";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_GAMESTRUCTURE = "game_structure";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_RESULT = "result";
    public static final String COLUMN_GAMENOTES = "game_info";

    private final static String TABLE_CREATE = "CREATE TABLE " + TABLE_SESSION + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_GAMETYPE + " INTEGER, " +
            COLUMN_LOCATION + " TEXT, " +
            COLUMN_GAMESTRUCTURE + " INTEGER, " +
            COLUMN_DURATION + " INTEGER, " +
            COLUMN_DATE + " DATE, " +
            COLUMN_RESULT + " INTEGER, " +
            COLUMN_GAMENOTES + " TEXT, " +
            "FOREIGN KEY(game_type) REFERENCES game_type, " +
            "FOREIGN KEY(game_structure) REFERENCES game_structure);";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(TABLE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSION);
        onCreate(database);
    }
}
