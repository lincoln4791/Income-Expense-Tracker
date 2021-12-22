package com.lincoln4791.dailyexpensemanager.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
 class MC_Posts{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    @ColumnInfo
    var postDescription: String
    @ColumnInfo
    var postCategory: String
    @ColumnInfo
    var postType: String
    @ColumnInfo
    var postAmount: String
    @ColumnInfo
    var postYear: String
    @ColumnInfo
    var postMonth: String
    @ColumnInfo
    var postDay: String
    @ColumnInfo
    var postTime: String
    @ColumnInfo
    var timeStamp: String
    @ColumnInfo
    var postDateTime: String

    /*@ColumnInfo var postDescription: String
    @ColumnInfo var postCategory: String
    @ColumnInfo var postType: String
    @ColumnInfo var postAmount: String
    @ColumnInfo var postYear: String
    @ColumnInfo var postMonth: String
    @ColumnInfo var postDay: String
    @ColumnInfo var postTime: String
    @ColumnInfo var timeStamp: String
    @ColumnInfo var postDateTime: String*/

    @Ignore
    constructor(
        postDescription: String,
        postCategory: String,
        postType: String,
        postAmount: String,
        postYear: String,
        postMonth: String,
        postDay: String,
        postTime: String,
        timeStamp: String,
        postDateTime: String
    ) {
        this.postDescription = postDescription
        this.postCategory = postCategory
        this.postType = postType
        this.postAmount = postAmount
        this.postYear = postYear
        this.postMonth = postMonth
        this.postDay = postDay
        this.postTime = postTime
        this.timeStamp = timeStamp
        this.postDateTime = postDateTime
    }

    constructor(
        id: Int,
        postDescription: String,
        postCategory: String,
        postType: String,
        postAmount: String,
        postYear: String,
        postMonth: String,
        postDay: String,
        postTime: String,
        timeStamp: String,
        postDateTime: String
    ) {
        this.id = id
        this.postDescription = postDescription
        this.postCategory = postCategory
        this.postType = postType
        this.postAmount = postAmount
        this.postYear = postYear
        this.postMonth = postMonth
        this.postDay = postDay
        this.postTime = postTime
        this.timeStamp = timeStamp
        this.postDateTime = postDateTime
    }
}