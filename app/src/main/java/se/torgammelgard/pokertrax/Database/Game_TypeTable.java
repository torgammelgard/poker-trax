package se.torgammelgard.pokertrax.Database;

import android.database.sqlite.SQLiteDatabase;

/**
 * TODO: Class header comment.
 */
public class Game_TypeTable {

    public static final String TABLE_GAMETYPE = "game_type";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_GAMETYPE +
            "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_NAME + " TEXT" +
            ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(TABLE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMETYPE);
        onCreate(database);
    }
}
