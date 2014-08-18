package com.example.torgammelgard.pokerhourly;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static MySQLiteHelper sInstance;

    private static final String DATABASE_NAME = "sessions.db";
    private static final int DATABASE_VERSION = 1;

    public static MySQLiteHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MySQLiteHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        /* creation of tables and initial population */
        Game_TypeTable.onCreate(database);
        Game_StructureTable.onCreate(database);
        SessionTable.onCreate(database);
        database.execSQL(
                "INSERT INTO game_type(name) VALUES('NL'), ('PL'), ('Limit');"
        );
        database.execSQL(
                "INSERT INTO game_structure(small_blind, big_blind) VALUES" +
                "(1, 2), (2, 4), (3, 6), (5, 10), (10, 20);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Game_StructureTable.onUpdate(db, oldVersion, newVersion);
        Game_StructureTable.onUpdate(db, oldVersion, newVersion);
        SessionTable.onUpgrade(db, oldVersion, newVersion);
    }
}
