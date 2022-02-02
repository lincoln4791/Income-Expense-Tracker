package com.lincoln4791.dailyexpensemanager.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class MC_Cards{

    @PrimaryKey(autoGenerate = true) var id: Int?= null
    @ColumnInfo var cardName: String? = null

    constructor(cardName : String){
        this.cardName = cardName

    }

}