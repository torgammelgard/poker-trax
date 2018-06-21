package se.torgammelgard.pokertrax.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_structure")
class GameStructureImpl {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Long = 0

    /** blinds and antes are stored in cents */
    @ColumnInfo(name = "small_blind")
    var smallBlind: Int = 0

    @ColumnInfo(name = "big_blind")
    var bigBlind: Int = 0

    @ColumnInfo(name = "ante")
    var ante: Int = 0

    constructor()

    constructor(id: Long, small_blind: Int, big_blind: Int, ante: Int) {
        this.id = id
        this.smallBlind = small_blind
        this.bigBlind = big_blind
        this.ante = ante
    }

}