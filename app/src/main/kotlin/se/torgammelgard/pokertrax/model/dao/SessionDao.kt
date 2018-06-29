package se.torgammelgard.pokertrax.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import se.torgammelgard.pokertrax.model.entities.Session

@Dao
interface SessionDao {

    @Query("SELECT * FROM session")
    fun getAll(): List<Session>

    @Query("SELECT * FROM session WHERE _id = :id")
    fun get(id: Long): Session

    @Insert(onConflict = REPLACE)
    fun add(session: Session): Long

    @Query("SELECT count(*) FROM session")
    fun numberOfSessions(): Int

    @Query("SELECT DISTINCT location FROM session WHERE location <> ''")
    fun locations(): List<String>

    @Query("SELECT total(CAST (result AS float)/(SELECT big_blind FROM game_structure WHERE game_structure._id = session.game_structure)) / total(CAST (duration AS float)/60) FROM session")
    fun getAverageBigBlindPerHour(): Double

    @Query("SELECT total(CAST (result AS float)) FROM session WHERE game_type = :gameTypeId")
    fun resultForGameType(gameTypeId: Long): Int

    @Query("SELECT total(duration) FROM session")
    fun totalTimePlayed(): Int
}
