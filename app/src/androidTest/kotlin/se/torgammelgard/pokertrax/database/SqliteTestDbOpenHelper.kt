package se.torgammelgard.pokertrax.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Helper class for creating the test database version 1 with SQLite.
 */
class SqliteTestDbOpenHelper(context: Context, databaseName: String) : SQLiteOpenHelper(context, databaseName, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Not required as at version 1
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Not required as at version 1
    }

    companion object {

        const val DATABASE_VERSION = 1
    }
}
