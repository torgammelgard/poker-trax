package se.torgammelgard.pokertrax.model.repositories

import org.mockito.Mockito
import org.mockito.Mockito.times
import se.torgammelgard.pokertrax.model.dao.GameStructureDao
import se.torgammelgard.pokertrax.model.entities.impl.GameStructureImpl
import kotlin.test.BeforeTest
import kotlin.test.Test

class GameStructureRepositoryTest {

    private lateinit var gameStructureRepository: GameStructureRepository
    private val dao = Mockito.mock(GameStructureDao::class.java)

    @BeforeTest
    fun setup() {
        gameStructureRepository = GameStructureRepository(dao)
    }

    @Test
    fun testThing() {
        var gameStructure = Mockito.mock(GameStructureImpl::class.java)
        gameStructureRepository.add(gameStructure)
        Mockito.verify(dao, times(1)).add(gameStructure)
    }
}