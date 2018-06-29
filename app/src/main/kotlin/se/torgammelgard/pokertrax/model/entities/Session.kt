package se.torgammelgard.pokertrax.model.entities

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import se.torgammelgard.pokertrax.model.converters.Converters
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
    var gameTypeReference: Long? = 0

    var location: String? = null

    @ColumnInfo(name = "game_structure")
    var gameStructureReference: Long? = 0

    var duration: Int? = 0

    @TypeConverters(Converters::class)
    var date: Date? = null

    var result: Int = 0

    @ColumnInfo(name = "game_info")
    var gameNotes: String? = ""

    constructor()

    /** TODO should this be removed? Id shouldn't be used when inserting in database anyways */
    @Ignore
    constructor(id: Long, gameTypeReference: Long, location: String, gameStructureReference: Long, duration: Int, date: Date?, result: Int, gameNotes: String?) {
        this.id = id
        this.gameTypeReference = gameTypeReference
        this.location = location
        this.gameStructureReference = gameStructureReference
        this.duration = duration
        this.date = date
        this.result = result
        this.gameNotes = gameNotes
    }

    /** Use this for insert without id */
    @Ignore
    constructor(gameTypeReference: Long, location: String, gameStructureReference: Long, duration: Int, date: Date?, result: Int, gameNotes: String?) {
        this.gameTypeReference = gameTypeReference
        this.location = location
        this.gameStructureReference = gameStructureReference
        this.duration = duration
        this.date = date
        this.result = result
        this.gameNotes = gameNotes
    }
}