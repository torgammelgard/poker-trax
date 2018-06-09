package se.torgammelgard.pokertrax.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import se.torgammelgard.pokertrax.dao.GameStructureDao
import se.torgammelgard.pokertrax.dao.GameTypeDao
import se.torgammelgard.pokertrax.dao.SessionDao
import se.torgammelgard.pokertrax.entity.GameStructure
import se.torgammelgard.pokertrax.entity.GameType
import se.torgammelgard.pokertrax.entity.Session

@Database(entities = [GameStructure::class, Session::class, GameType::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gameStructureDao(): GameStructureDao
    abstract fun sessionDao(): SessionDao
    abstract fun gameTypeDao(): GameTypeDao?

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "cool_room.db").build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}