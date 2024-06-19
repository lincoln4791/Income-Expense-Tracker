package com.lincoln4791.dailyexpensemanager

import okhttp3.ResponseBody

sealed class Resource2<out T> {
    data class Success<out T>(val value : T):Resource2<T>()
    data class Failure(
        val isNetworkError : Boolean,
        val errorCode : Int?,
        val errorBody : ResponseBody?
    ) : Resource2<Nothing>()
}