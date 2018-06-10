package se.torgammelgard.pokertrax.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.migration.Migration
import android.content.Context
import se.torgammelgard.pokertrax.dao.GameStructureDao
import se.torgammelgard.pokertrax.dao.GameTypeDao
import se.torgammelgard.pokertrax.dao.SessionDao
import se.torgammelgard.pokertrax.entity.Converters
import se.torgammelgard.pokertrax.entity.GameStructure
import se.torgammelgard.pokertrax.entity.GameType
import se.torgammelgard.pokertrax.entity.Session
import android.arch.persistence.db.SupportSQLiteDatabase



@Database(entities = [GameStructure::class, Session::class, GameType::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gameStructureDao(): GameStructureDao
    abstract fun sessionDao(): SessionDao
    abstract fun gameTypeDao(): GameTypeDao


    companion object {
        private var INSTANCE: AppDatabase? = null
        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
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