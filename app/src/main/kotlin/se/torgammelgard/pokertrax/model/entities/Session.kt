package se.torgammelgard.pokertrax.model.entities

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import se.torgammelgard.pokertrax.model.converters.Converters
import se.torgammelgard.pokertrax.model.entities.impl.GameStructureImpl
import java.util.*

@Entity(tableName = "session", foreignKeys = arrayOf(ForeignKey(entity = GameStructureImpl::class,
        parentColumns = arrayOf("_id"), childColumns = arrayOf("game_structure"), onDelete = CASCADE),
        ForeignKey(entity = GameType::class,
                parentColumns = arrayOf("_id"), childColumns = arrayOf("game_type"), onDelete = CASCADE)))
class Session {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Long = 0

    @ColumnInfo(name = "game_type")
    var gameTypeReference: Int = 0

    var location: String = ""

    @ColumnInfo(name = "game_structure")
    var gameStructureReference: Int = 0

    var duration: Int = 0

    @TypeConverters(Converters::class)
    var date: Date? = null

    var result: Int = 0

    @ColumnInfo(name = "game_info")
    var gameNotes: String? = ""

    constructor()

    /** TODO should this be removed? Id shouldn't be used when inserting in database anyways */
    @Ignore
    constructor(id: Long, gameTypeReference: Int, location: String, gameStructureReference: Int, duration: Int, date: Date?, result: Int, gameNotes: String?) {
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
    constructor(gameTypeReference: Int, location: String, gameStructureReference: Int, duration: Int, date: Date?, result: Int, gameNotes: String?) {
        this.gameTypeReference = gameTypeReference
        this.location = location
        this.gameStructureReference = gameStructureReference
        this.duration = duration
        this.date = date
        this.result = result
        this.gameNotes = gameNotes
    }
}