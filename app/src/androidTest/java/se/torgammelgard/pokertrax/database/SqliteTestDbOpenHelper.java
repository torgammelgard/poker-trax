package se.torgammelgard.pokertrax.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper class for creating the test database version 1 with SQLite.
 */
public class SqliteTestDbOpenHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public SqliteTestDbOpenHelper(Context context, String databaseName) {
        super(context, databaseName, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE game_structure ( _id INTEGER PRIMARY KEY, small_blind INT, big_blind INT, ante INT )");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }
}
