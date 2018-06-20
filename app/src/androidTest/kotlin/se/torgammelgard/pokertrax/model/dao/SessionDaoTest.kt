package se.torgammelgard.pokertrax.model.dao

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import se.torgammelgard.pokertrax.model.database.AppDatabase
import se.torgammelgard.pokertrax.model.entities.impl.GameStructureImpl
import se.torgammelgard.pokertrax.model.entities.GameType
import se.torgammelgard.pokertrax.model.entities.Session
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

        val gameStructure = GameStructureImpl(1, 100, 200, 50)
        gameStructureDao.add(gameStructure)

        // TODO how to test this without having game_reference, should it be nullable?
        val session = Session(1, 1, "Earth", 1, 4, Date(1), 6, "Game notes")
        sessionDao.add(session)
        val sessions = sessionDao.getAll()

        assert(sessions[0].location == "Earth")
    }
}