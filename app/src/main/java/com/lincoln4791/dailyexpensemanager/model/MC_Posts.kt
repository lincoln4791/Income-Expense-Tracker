package com.lincoln4791.dailyexpensemanager.model

class MC_Posts {
    var id = 0
    var postDescription: String
    var postCategory: String
    var postType: String
    var postAmount: String
    var postYear: String
    var postMonth: String
    var postDay: String
    var postTime: String
    var timeStamp: String
    var postDateTime: String

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