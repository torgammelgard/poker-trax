package se.torgammelgard.pokertrax.model.repositories

import se.torgammelgard.pokertrax.model.dao.SessionDao
import se.torgammelgard.pokertrax.model.entities.Session
import javax.inject.Inject

class SessionRepository @Inject constructor(private val sessionDao: SessionDao){

    fun getAllSessions(): List<Session> = sessionDao.getAll()

    fun get(id: Long): Session = sessionDao.get(id)

    fun addSession(session: Session): Long = sessionDao.add(session)

    fun numberOfSessions(): Int = sessionDao.numberOfSessions()

    fun locations(): List<String> = sessionDao.locations()
}