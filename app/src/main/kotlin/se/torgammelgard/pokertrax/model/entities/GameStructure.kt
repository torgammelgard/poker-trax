package se.torgammelgard.pokertrax.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "game_structure")
class GameStructure {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Long = 0

    /** blinds and antes are stored in cents */
    @ColumnInfo(name = "small_blind")
    var smallBlind: Int? = 0

    @ColumnInfo(name = "big_blind")
    var bigBlind: Int? = 0

    @ColumnInfo(name = "ante")
    var ante: Int? = 0

    @Ignore
    constructor()

    @Ignore
    constructor(smallBlind: Int?, bigBlind: Int?, ante: Int?) {
        this.smallBlind = smallBlind
        this.bigBlind = bigBlind
        this.ante = ante
    }

    constructor(id: Long, smallBlind: Int?, bigBlind: Int?, ante: Int?) {
        this.id = id
        this.smallBlind = smallBlind
        this.bigBlind = bigBlind
        this.ante = ante
    }

}