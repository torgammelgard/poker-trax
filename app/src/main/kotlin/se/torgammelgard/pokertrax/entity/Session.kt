package se.torgammelgard.pokertrax.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "session", foreignKeys = arrayOf(ForeignKey(entity = GameStructure::class,
        parentColumns = arrayOf("_id"), childColumns = arrayOf("game_structure"), onDelete = CASCADE),
        ForeignKey(entity = GameType::class,
                parentColumns = arrayOf("_id"), childColumns = arrayOf("game_type"), onDelete = CASCADE)))
class Session {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Long = 0

    @ColumnInfo(name = "game_type")
    var gameTypeReference: Int = 0

    var location: String? = ""

    @ColumnInfo(name = "game_structure")
    var gameStructureReference: Int = 0

    var duration: Int = 0

    var date: Date? = null

    var result: Int = 0

    @ColumnInfo(name = "game_info")
    var gameNotes: String? = ""

    constructor()

    constructor(id: Long, gameTypeReference: Int, location: String?, gameStructureReference: Int, duration: Int, date: Date?, result: Int, gameNotes: String?) {
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