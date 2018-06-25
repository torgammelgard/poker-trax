package se.torgammelgard.pokertrax.model.repositories

import se.torgammelgard.pokertrax.model.dao.GameTypeDao
import se.torgammelgard.pokertrax.model.entities.GameType
import javax.inject.Inject

class GameTypeRepository @Inject constructor(private val gameTypeDao: GameTypeDao){

    fun getAll(): List<GameType> = gameTypeDao.getAll()

    fun add(gameType: GameType) = gameTypeDao.add(gameType)
}