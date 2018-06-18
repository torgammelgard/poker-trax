package se.torgammelgard.pokertrax.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "game_type")
class GameType {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Long = 0

    @ColumnInfo(name = "name")
    var type: String = "" //TODO should this be nullable or not

    @Ignore
    constructor(id: Long, type: String) {
        this.id = id
        this.type = type
    }

    constructor()
}