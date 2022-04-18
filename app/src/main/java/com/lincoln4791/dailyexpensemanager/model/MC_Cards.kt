package com.lincoln4791.dailyexpensemanager.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mc_cards")
class MC_Cards{
    @PrimaryKey(autoGenerate = true) var id: Int?= null
    @ColumnInfo var cardName: String? = null
    @ColumnInfo var cardType: String? = null

    constructor(cardName : String,cardType:String){
        this.cardName = cardName
        this.cardType = cardType
    }

}