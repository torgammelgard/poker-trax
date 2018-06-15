package se.torgammelgard.pokertrax.model.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import se.torgammelgard.pokertrax.model.entities.GameType

@Dao
interface GameTypeDao {

    @Query("SELECT * FROM game_type")
    fun getAll(): List<GameType>

    @Insert(onConflict = REPLACE)
    fun add(gameType: GameType)
}