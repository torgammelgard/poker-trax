package se.torgammelgard.pokertrax.model

import java.io.Serializable
import java.util.Date

class Session : Serializable {

    var id: Long = 0
    var game_type_ref: Int = 0
    var location: String? = null
    var game_structure_ref: Int = 0

    /**
     * In minutes
     */
    var duration: Int = 0
    var date: Date? = null
    var result: Int = 0
    var game_notes: String? = null

    init {
        this.id = 0
        date = Date()
    }

    override fun toString(): String {
        return "Session(id=$id, game_type_ref=$game_type_ref, location=$location, game_structure_ref=$game_structure_ref, duration=$duration, date=$date, result=$result, game_notes=$game_notes)"
    }

}
