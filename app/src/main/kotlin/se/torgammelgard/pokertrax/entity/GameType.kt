package se.torgammelgard.pokertrax.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "game_type")
class GameType {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Long = 0

    @ColumnInfo(name = "name")
    var type: String? = "" //TODO should this be nullable or not

    constructor(id: Long, type: String?) {
        this.id = id
        this.type = type
    }

    constructor()
}