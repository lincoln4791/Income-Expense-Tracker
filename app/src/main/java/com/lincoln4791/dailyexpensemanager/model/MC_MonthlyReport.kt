package com.lincoln4791.dailyexpensemanager.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mc_monthly_report")
data class MC_MonthlyReport(@PrimaryKey val postCategory : String,
                            @ColumnInfo
                            val postAmount : Int?,
                            @ColumnInfo
                            val amountPercent:String?) {
}