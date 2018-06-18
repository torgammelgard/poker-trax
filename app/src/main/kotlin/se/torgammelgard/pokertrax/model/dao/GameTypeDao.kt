package se.torgammelgard.pokertrax.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import se.torgammelgard.pokertrax.model.entities.GameType

@Dao
interface GameTypeDao {

    @Query("SELECT * FROM game_type")
    fun getAll(): List<GameType>

    @Insert(onConflict = REPLACE)
    fun add(gameType: GameType)
}