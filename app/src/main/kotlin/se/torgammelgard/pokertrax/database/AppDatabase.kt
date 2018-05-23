package se.torgammelgard.pokertrax.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import se.torgammelgard.pokertrax.dao.GameStructureDao
import se.torgammelgard.pokertrax.entity.GameStructure

@Database(entities = [(GameStructure::class)], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gameStructureDao(): GameStructureDao

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