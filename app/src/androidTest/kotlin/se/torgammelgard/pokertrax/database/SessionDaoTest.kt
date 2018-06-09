package se.torgammelgard.pokertrax.database

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import se.torgammelgard.pokertrax.dao.SessionDao
import se.torgammelgard.pokertrax.entity.Session
import java.util.*

@RunWith(AndroidJUnit4::class)
class SessionDaoTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var sessionDao: SessionDao

    @Before
    fun setup() {
        appDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDatabase::class.java).build()
        sessionDao = appDatabase.sessionDao()
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    fun testSessionDao() {
        val session = Session(1, 2, "Earth", 3, 4, Date(1), 6, "Game notes")
        sessionDao.add(session)
        val allSessions = sessionDao.getAll()
        Assert.assertEquals(1, allSessions.size)
    }
}