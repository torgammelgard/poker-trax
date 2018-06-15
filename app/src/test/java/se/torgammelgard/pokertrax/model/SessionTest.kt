package se.torgammelgard.pokertrax.model

import org.junit.Assert.*
import org.junit.Test
import se.torgammelgard.pokertrax.model.old_entities.Session
import java.util.*

/**
 * Created by torgammelgard on 2017-08-06.
 */
class SessionTest {

    @Test
    fun testEmptyConstructor() {
        val session = Session()
        assertEquals(0, session.id)
        assertEquals(0, session.game_type_ref)
        assertEquals("", session.location)
        assertEquals(0, session.duration)
        assertEquals(Date(), session.date)
        assertEquals(0, session.result)
        assertEquals("", session.game_notes)
    }
}
