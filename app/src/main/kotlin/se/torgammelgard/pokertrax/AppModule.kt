package se.torgammelgard.pokertrax

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import se.torgammelgard.pokertrax.model.dao.GameStructureDao
import se.torgammelgard.pokertrax.model.dao.GameTypeDao
import se.torgammelgard.pokertrax.model.dao.SessionDao
import se.torgammelgard.pokertrax.model.database.AppDatabase
import se.torgammelgard.pokertrax.model.repositories.GameStructureRepository
import se.torgammelgard.pokertrax.model.repositories.GameTypeRepository
import se.torgammelgard.pokertrax.model.repositories.SessionRepository
import javax.inject.Singleton


@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(mainApp: MainApp): Context {
        return mainApp
    }

    @Singleton
    @Provides
    fun provideMyDatabase(mainApp: MainApp) = Room.databaseBuilder(mainApp, AppDatabase::class.java, "sessions.db").build()

    @Singleton
    @Provides
    fun provideGameTypeRepository(gameTypeDao: GameTypeDao) = GameTypeRepository(gameTypeDao)

    @Singleton
    @Provides
    fun provideGameStructureRepository(gameStructureDao: GameStructureDao) = GameStructureRepository(gameStructureDao)

    @Singleton
    @Provides
    fun provideSessionRepository(sessionDao: SessionDao) = SessionRepository(sessionDao)

    @Singleton
    @Provides
    fun provideGameTypeDao(appDatabase: AppDatabase) = appDatabase.gameTypeDao()

    @Singleton
    @Provides
    fun provideGameStructureDao(appDatabase: AppDatabase) = appDatabase.gameStructureDao()

    @Singleton
    @Provides
    fun provideSession(appDatabase: AppDatabase) = appDatabase.sessionDao()
}