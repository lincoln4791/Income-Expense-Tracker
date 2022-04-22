package com.lincoln4791.dailyexpensemanager.modelClass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Banner(
    @ColumnInfo val created_date: String,
    @ColumnInfo val deleted_date: String,
    @ColumnInfo val isActive: String,
    @ColumnInfo val link: String,
    @ColumnInfo val media_url: String,
    @PrimaryKey val name: String
)