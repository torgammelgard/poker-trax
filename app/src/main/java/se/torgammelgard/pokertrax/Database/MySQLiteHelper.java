package se.torgammelgard.pokertrax.Database;

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
                "INSERT INTO game_type(name) VALUES('No Limit'), ('Limit'), ('Pot Limit');"
        );
        database.execSQL(
                "INSERT INTO game_structure(small_blind, big_blind) VALUES" +
                        "(100, 200), (200, 400), (300, 600), (500, 1000), (1000, 2000);"
        );
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Game_StructureTable.onUpgrade(db, oldVersion, newVersion);
        Game_TypeTable.onUpgrade(db, oldVersion, newVersion);
        SessionTable.onUpgrade(db, oldVersion, newVersion);
    }
}
