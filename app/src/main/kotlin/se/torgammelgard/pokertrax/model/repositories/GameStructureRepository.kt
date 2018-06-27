package se.torgammelgard.pokertrax.model.repositories

import se.torgammelgard.pokertrax.model.dao.GameStructureDao
import se.torgammelgard.pokertrax.model.entities.GameStructure
import javax.inject.Inject

class GameStructureRepository @Inject constructor(private val gameStructureDao: GameStructureDao){

    fun getAllGameStructures(): List<GameStructure> = gameStructureDao.getAll()

    fun get(id: Long): GameStructure = gameStructureDao.get(id)

    fun add(gameStructure: GameStructure): Long = gameStructureDao.add(gameStructure)
}