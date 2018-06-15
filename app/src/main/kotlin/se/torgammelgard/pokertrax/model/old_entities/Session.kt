package se.torgammelgard.pokertrax.model.old_entities

import java.io.Serializable
import java.util.Date

data class Session(
        var id: Long = 0,
        var game_type_ref: Int = 0,
        var location: String? = "",
        var game_structure_ref: Int = 0,
        var duration: Int = 0,
        var date: Date? = Date(),
        var result: Int = 0,
        var game_notes: String? = "") : Serializable {

    override fun toString(): String {
        return "Session(id=$id, game_type_ref=$game_type_ref, location=$location, game_structure_ref=$game_structure_ref, duration=$duration, date=$date, result=$result, game_notes=$game_notes)"
    }

}
