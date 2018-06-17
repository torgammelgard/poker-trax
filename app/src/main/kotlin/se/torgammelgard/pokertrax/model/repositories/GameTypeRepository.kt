package se.torgammelgard.pokertrax.model.repositories

import se.torgammelgard.pokertrax.model.dao.GameTypeDao
import se.torgammelgard.pokertrax.model.entities.GameType
import javax.inject.Inject

class GameTypeRepository @Inject constructor(val gameTypeDao: GameTypeDao){

    fun getAllGameTypes(): List<GameType> = gameTypeDao.getAll()

    fun add(gameType: GameType) = gameTypeDao.add(gameType)
}