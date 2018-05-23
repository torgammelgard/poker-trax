package se.torgammelgard.pokertrax.database

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.After
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
        if (allGameStructures is List<GameStructure>) {
            print("HELLO : ${allGameStructures.size}")
        }
    }
}