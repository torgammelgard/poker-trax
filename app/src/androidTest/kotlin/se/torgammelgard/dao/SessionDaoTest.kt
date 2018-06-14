package se.torgammelgard.dao

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import se.torgammelgard.pokertrax.dao.GameStructureDao
import se.torgammelgard.pokertrax.dao.GameTypeDao
import se.torgammelgard.pokertrax.dao.SessionDao
import se.torgammelgard.pokertrax.database.AppDatabase
import se.torgammelgard.pokertrax.entity.GameStructure
import se.torgammelgard.pokertrax.entity.GameType
import se.torgammelgard.pokertrax.entity.Session
import java.util.*

@RunWith(AndroidJUnit4::class)
class SessionDaoTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var sessionDao: SessionDao
    private lateinit var gameTypeDao: GameTypeDao
    private lateinit var gameStructureDao: GameStructureDao

    @Before
    fun setup() {
        appDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDatabase::class.java).build()
        sessionDao = appDatabase.sessionDao()
        gameTypeDao = appDatabase.gameTypeDao()
        gameStructureDao = appDatabase.gameStructureDao()
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    fun testSessionDao() {
        val gameType = GameType(1, "No limit")
        gameTypeDao.add(gameType)

        val gameStructure = GameStructure(1, 100, 200, 50)
        gameStructureDao.add(gameStructure)

        // TODO how to test this without having game_reference, should it be nullable?
        val session = Session(1, 1, "Earth", 1, 4, Date(1), 6, "Game notes")
        sessionDao.add(session)
        val sessions = sessionDao.getAll()

        assert(sessions[0].location == "Earth")
    }
}