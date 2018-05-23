package se.torgammelgard.pokertrax.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "game_structure")
class GameStructure {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    // blinds and ante stored in cents
    @ColumnInfo(name = "small_blind")
    var small_blind: Int = 0

    @ColumnInfo(name = "big_blind")
    var big_blind: Int = 0

    @ColumnInfo(name = "ante")
    var ante: Int = 0

    constructor(id: Long, small_blind: Int, big_blind: Int, ante: Int) {
        this.id = id
        this.small_blind = small_blind
        this.big_blind = big_blind
        this.ante = ante
    }

    constructor()
}