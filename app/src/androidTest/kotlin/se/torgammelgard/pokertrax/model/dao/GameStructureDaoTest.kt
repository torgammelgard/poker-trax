package se.torgammelgard.pokertrax.model.dao

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import se.torgammelgard.pokertrax.model.database.AppDatabase
import se.torgammelgard.pokertrax.model.entities.GameStructureImpl

@RunWith(AndroidJUnit4::class)
class GameStructureDaoTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var gameStructureDao: GameStructureDao

    @Before
    fun setup() {
        appDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDatabase::class.java).build()
        gameStructureDao = appDatabase.gameStructureDao()
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    fun testInsertGameStructure() {
        val gameStructure = GameStructureImpl(1, 50, 100, 25)
        gameStructureDao.add(gameStructure)
        val allGameStructures = gameStructureDao.getAll()
        Assert.assertEquals(1, allGameStructures.size)
        assert(gameStructureDao.getAll()[0].id == 1L)
        assert(gameStructureDao.getAll()[0].bigBlind == 100)
        assert(gameStructureDao.getAll()[0].smallBlind == 50)
        assert(gameStructureDao.getAll()[0].ante == 25)
    }
}