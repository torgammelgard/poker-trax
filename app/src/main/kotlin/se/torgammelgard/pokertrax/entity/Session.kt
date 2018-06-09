package se.torgammelgard.pokertrax.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "session")
class Session {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Long = 0

    @ColumnInfo(name = "game_ref")
    var gameTypeReference: Int = 0

    var location: String? = ""

    @ColumnInfo(name = "game_structure_ref")
    var gameStructureReference: Int = 0

    var duration: Int = 0

    var date: Long = 0

    var result: Int = 0

    @ColumnInfo(name = "game_notes")
    var gameNotes: String? = ""

    constructor()

    constructor(id: Long, gameTypeReference: Int, location: String?, gameStructureReference: Int, duration: Int, date: Long, result: Int, gameNotes: String?) {
        this.id = id
        this.gameTypeReference = gameTypeReference
        this.location = location
        this.gameStructureReference = gameStructureReference
        this.duration = duration
        this.date = date
        this.result = result
        this.gameNotes = gameNotes
    }
}