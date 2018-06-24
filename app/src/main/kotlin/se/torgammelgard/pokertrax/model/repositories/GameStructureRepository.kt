package se.torgammelgard.pokertrax.model.repositories

import se.torgammelgard.pokertrax.model.dao.GameStructureDao
import se.torgammelgard.pokertrax.model.entities.GameStructure
import javax.inject.Inject

class GameStructureRepository @Inject constructor(private val gameStructureDao: GameStructureDao){

    fun getAllGameStructures(): List<GameStructure> {
        return gameStructureDao.getAll()
    }

    fun add(gameStructure: GameStructure) {
        gameStructureDao.add(gameStructure)
    }
}