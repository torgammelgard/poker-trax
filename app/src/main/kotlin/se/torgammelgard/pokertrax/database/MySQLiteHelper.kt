package se.torgammelgard.pokertrax.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 *
 */
class MySQLiteHelper private constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(database: SQLiteDatabase) {
        GameTypeTable.onCreate(database)
        GameStructureTable.onCreate(database)
        SessionTable.onCreate(database)
        database.execSQL(
                "INSERT INTO game_type(name) VALUES('No Limit'), ('Limit'), ('Pot Limit');"
        )
        database.execSQL(
                "INSERT INTO game_structure(small_blind, big_blind) VALUES" + "(100, 200), (200, 400), (300, 600), (500, 1000), (1000, 2000);"
        )
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        GameStructureTable.onUpgrade(db, oldVersion, newVersion)
        GameTypeTable.onUpgrade(db, oldVersion, newVersion)
        SessionTable.onUpgrade(db, oldVersion, newVersion)
    }

    companion object {

        private var sInstance: MySQLiteHelper? = null

        private const val DATABASE_NAME = "sessions.db"
        private const val DATABASE_VERSION = 1

        fun getInstance(context: Context): MySQLiteHelper {
            return sInstance ?: MySQLiteHelper(context.applicationContext)
        }
    }
}
