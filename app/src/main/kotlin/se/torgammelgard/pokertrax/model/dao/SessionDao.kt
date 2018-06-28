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
}