package se.torgammelgard.pokertrax.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "game_structure")
class GameStructure {

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