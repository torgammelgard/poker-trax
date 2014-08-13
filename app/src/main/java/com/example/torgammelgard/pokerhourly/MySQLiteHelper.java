package com.example.torgammelgard.pokerhourly;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_SESSIONS = "sessions";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_GAMEINFO = "gameinfo";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_RESULT = "result";

    private static final String DATABASE_NAME = "sessions.db";
    private static final int DATABASE_VERSION = 1;

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE sessions(_id integer primary key autoincrement, " +
                "gameinfo text not null, time INT, result INT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSIONS);
        onCreate(db);
    }
}
