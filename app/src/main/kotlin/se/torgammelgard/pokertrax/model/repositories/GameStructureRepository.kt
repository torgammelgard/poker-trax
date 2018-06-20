package se.torgammelgard.pokertrax.model.repositories

import se.torgammelgard.pokertrax.model.dao.GameStructureDao
import se.torgammelgard.pokertrax.model.entities.impl.GameStructureImpl
import javax.inject.Inject

class GameStructureRepository @Inject constructor(val gameStructureDao: GameStructureDao){

    fun getAllGameStructures(): List<GameStructureImpl> {
        return gameStructureDao.getAll()
    }

    fun add(gameStructureImpl: GameStructureImpl) {
        gameStructureDao.add(gameStructureImpl)
    }
}