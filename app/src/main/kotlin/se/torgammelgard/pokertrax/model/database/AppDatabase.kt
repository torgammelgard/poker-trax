package se.torgammelgard.pokertrax.model.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.migration.Migration
import android.content.Context
import se.torgammelgard.pokertrax.model.dao.GameStructureDao
import se.torgammelgard.pokertrax.model.dao.GameTypeDao
import se.torgammelgard.pokertrax.model.dao.SessionDao
import se.torgammelgard.pokertrax.model.converters.Converters
import se.torgammelgard.pokertrax.model.entities.GameStructure
import se.torgammelgard.pokertrax.model.entities.GameType
import se.torgammelgard.pokertrax.model.entities.Session
import android.arch.persistence.db.SupportSQLiteDatabase
import android.support.annotation.VisibleForTesting


@Database(entities = [GameStructure::class, Session::class, GameType::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gameStructureDao(): GameStructureDao
    abstract fun sessionDao(): SessionDao
    abstract fun gameTypeDao(): GameTypeDao


    companion object {
        private var INSTANCE: AppDatabase? = null

        /**
         * Migrate from:
         * version 1: using the SQLiteDatabase API
         * to
         * version 2: using Room
         */
        @VisibleForTesting
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Table session
                // Backup (rename) the table
                database.execSQL("DROP TABLE IF EXISTS 'session_old'")
                database.execSQL("ALTER TABLE 'session' RENAME TO 'session_old'")

                // Create the new table
                database.execSQL("CREATE TABLE IF NOT EXISTS 'session' " +
                        "('_id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "'date' INTEGER, " +
                        "'duration' INTEGER NOT NULL, " +
                        "'result' INTEGER NOT NULL, " +
                        "'game_structure' INTEGER NOT NULL, " +
                        "'game_type' INTEGER NOT NULL, " +
                        "'location' TEXT NOT NULL, " +
                        "'game_info' TEXT, " +
                        "FOREIGN KEY(`game_structure`) REFERENCES `game_structure`(`_id`) ON UPDATE NO ACTION ON DELETE CASCADE," +
                        "FOREIGN KEY(`game_type`) REFERENCES `game_type`(`_id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
                database.execSQL("INSERT INTO 'session' (date, duration, result, game_structure, game_type, location, game_info) " +
                        "SELECT strftime('%s000', date), duration, result, game_structure, game_type, location, game_info FROM session_old")
                // Clean up
                database.execSQL("DROP TABLE IF EXISTS 'session_old'")

                // Table game_structure
                database.execSQL("DROP TABLE IF EXISTS 'game_structure_old'")
                database.execSQL("ALTER TABLE 'game_structure' RENAME TO 'game_structure_old'")
                database.execSQL("CREATE TABLE IF NOT EXISTS 'game_structure'(" +
                        "'_id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        "'small_blind' INTEGER NOT NULL," +
                        "'big_blind' INTEGER NOT NULL," +
                        "'ante' INTEGER NOT NULL)")
                // Copy data from old table
                database.execSQL("INSERT INTO 'game_structure' (small_blind, big_blind, ante) " + "SELECT small_blind, big_blind, ante FROM game_structure_old")
                // Clean up
                database.execSQL("DROP TABLE IF EXISTS 'game_structure_old'")

                // Table game_type
                database.execSQL("DROP TABLE IF EXISTS 'game_type_old'")
                database.execSQL("ALTER TABLE 'game_type' RENAME TO 'game_type_old'")
                database.execSQL("CREATE TABLE IF NOT EXISTS 'game_type'(" +
                        "'_id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        "'name' TEXT NOT NULL)")
                // Copy data from old table
                database.execSQL("INSERT INTO 'game_type' (name) " + "SELECT name FROM game_type_old")
                // Clean up
                database.execSQL("DROP TABLE IF EXISTS 'game_type_old'")
            }
        }

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "sessions.db")
                            .allowMainThreadQueries()
                            .addMigrations(MIGRATION_1_2)
                            .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }


    }
}