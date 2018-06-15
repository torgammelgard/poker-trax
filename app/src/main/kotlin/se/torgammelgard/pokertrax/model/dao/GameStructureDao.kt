package se.torgammelgard.pokertrax.model.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import se.torgammelgard.pokertrax.model.entities.GameStructure

@Dao
interface GameStructureDao {

    @Query("SELECT * FROM game_structure")
    fun getAll(): List<GameStructure>

    @Insert(onConflict = REPLACE)
    fun add(gameStructure: GameStructure)
}