package se.torgammelgard.pokertrax.model.repositories

import se.torgammelgard.pokertrax.model.dao.GameTypeDao
import se.torgammelgard.pokertrax.model.entities.GameType
import javax.inject.Inject

class GameTypeRepository @Inject constructor(private val gameTypeDao: GameTypeDao){

    fun getAll(): List<GameType> = gameTypeDao.getAll()

    fun get(id: Long): GameType = gameTypeDao.get(id)

    fun add(gameType: GameType) : Long = gameTypeDao.add(gameType)
}