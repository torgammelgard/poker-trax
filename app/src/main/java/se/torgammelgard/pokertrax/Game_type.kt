package se.torgammelgard.pokertrax

/**
 * Refers to table game_type. For example NL, PL, limit.
 */
class Game_type {
    var id: Long = 0             //refers to id in table game_type
    var type: String? = null

    override fun toString(): String {
        return "Game_type(id=$id, type=$type)"
    }
}
