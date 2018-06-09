package se.torgammelgard.pokertrax.database

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import se.torgammelgard.pokertrax.dao.GameStructureDao
import se.torgammelgard.pokertrax.dao.GameTypeDao
import se.torgammelgard.pokertrax.dao.SessionDao
import se.torgammelgard.pokertrax.entity.GameStructure
import se.torgammelgard.pokertrax.entity.GameType
import se.torgammelgard.pokertrax.entity.Session

@RunWith(AndroidJUnit4::class)
class AwesomeSimpleEntityReadWriteTest {

    private var gameStructureDao: GameStructureDao? = null
    private var sessionDao: SessionDao? = null
    private var gameTypeDao: GameTypeDao? = null

    @Before
    fun setup() {
        gameStructureDao = AppDatabase.getInstance(InstrumentationRegistry.getTargetContext())?.gameStructureDao()
        sessionDao = AppDatabase.getInstance(InstrumentationRegistry.getTargetContext())?.sessionDao()
        gameTypeDao = AppDatabase.getInstance(InstrumentationRegistry.getTargetContext())?.gameTypeDao()
    }

    @After
    fun tearDown() {
        AppDatabase.destroyInstance()
    }

    @Test
    fun testInsertGameStructure() {
        val gameStructure = GameStructure(1, 50, 100, 25)
        gameStructureDao?.add(gameStructure)
        val allGameStructures = gameStructureDao?.getAll()
        assertEquals(1, allGameStructures.orEmpty().size)
        assert(gameStructureDao?.getAll().orEmpty()[0].id == 1L)
        assert(gameStructureDao?.getAll().orEmpty()[0].bigBlind == 100)
        assert(gameStructureDao?.getAll().orEmpty()[0].smallBlind == 50)
        assert(gameStructureDao?.getAll().orEmpty()[0].ante == 25)
    }

    @Test
    fun testSessionDao() {
        val session = Session(1, 2, "Earth", 3, 4, 5, 6, gameNotes = "Game notes")
        sessionDao?.add(session)
        val allSessions = sessionDao?.getAll()
        assertEquals(1, allSessions.orEmpty().size)
    }

    @Test
    fun testGameTypeDao() {
        val gameType =  GameType(1, "No limit")
        gameTypeDao?.add(gameType)
        val allGameTypes = gameTypeDao?.getAll()
        assertEquals(1, allGameTypes.orEmpty().size)
    }
}