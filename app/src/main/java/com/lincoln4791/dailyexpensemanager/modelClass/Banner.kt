package com.lincoln4791.dailyexpensemanager.modelClass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.PropertyName
import java.io.Serializable

@Entity
 data class Banner(
    @ColumnInfo
    var created_date: String="",
    @ColumnInfo
    var deleted_date: String="",
    @ColumnInfo
    var is_active: String="",
    @ColumnInfo
    var link: String="",
    @ColumnInfo
    var media_url: String="",
    @PrimaryKey
    var name: String=""
) : Serializable