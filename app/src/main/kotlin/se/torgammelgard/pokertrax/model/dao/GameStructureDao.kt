package se.torgammelgard.pokertrax.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import se.torgammelgard.pokertrax.model.entities.GameStructureImpl

@Dao
interface GameStructureDao {

    @Query("SELECT * FROM game_structure")
    fun getAll(): List<GameStructureImpl>

    @Insert(onConflict = REPLACE)
    fun add(gameStructureImpl: GameStructureImpl)
}