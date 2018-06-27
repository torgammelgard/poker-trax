package se.torgammelgard.pokertrax.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import se.torgammelgard.pokertrax.model.entities.GameStructure

@Dao
interface GameStructureDao {

    @Query("SELECT * FROM game_structure")
    fun getAll(): List<GameStructure>

    @Query("SELECT * FROM game_structure WHERE _id = :id")
    fun get(id: Long): GameStructure

    @Insert(onConflict = REPLACE)
    fun add(gameStructure: GameStructure): Long
}