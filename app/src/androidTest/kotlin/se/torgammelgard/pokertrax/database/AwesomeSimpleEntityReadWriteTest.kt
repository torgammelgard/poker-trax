package se.torgammelgard.pokertrax.database

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import se.torgammelgard.pokertrax.dao.GameStructureDao
import se.torgammelgard.pokertrax.entity.GameStructure

@RunWith(AndroidJUnit4::class)
class AwesomeSimpleEntityReadWriteTest {

    private var gameStructureDao: GameStructureDao? = null

    @Before
    fun setup() {
        gameStructureDao = AppDatabase.getInstance(InstrumentationRegistry.getTargetContext())?.gameStructureDao()
    }

    @After
    fun tearDown() {
        AppDatabase.destroyInstance()
    }

    @Test
    fun testInsertGameStructure() {
        val gameStructure = GameStructure(1, 50, 100, 25)
        gameStructureDao?.insertGameStructure(gameStructure)
        val allGameStructures = gameStructureDao?.getAll()
        assertEquals(1, allGameStructures?.size)
        assert(gameStructureDao?.getAll().orEmpty()[0].id == 1L)
        assert(gameStructureDao?.getAll().orEmpty()[0].bigBlind == 100)
        assert(gameStructureDao?.getAll().orEmpty()[0].smallBlind == 50)
        assert(gameStructureDao?.getAll().orEmpty()[0].ante == 25)
    }
}