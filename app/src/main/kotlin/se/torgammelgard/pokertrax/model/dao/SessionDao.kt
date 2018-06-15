package se.torgammelgard.pokertrax.model.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import se.torgammelgard.pokertrax.model.entities.Session

@Dao
interface SessionDao {

    @Query("SELECT * FROM session")
    fun getAll(): List<Session>

    @Insert(onConflict = REPLACE)
    fun add(session: Session)

    @Query("SELECT count(*) FROM session")
    fun entriesCount(): Int
}