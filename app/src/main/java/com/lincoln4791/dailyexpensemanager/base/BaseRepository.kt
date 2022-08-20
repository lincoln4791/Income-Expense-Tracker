package com.lincoln4791.dailyexpensemanager.base
import com.lincoln4791.dailyexpensemanager.network.SafeApiCall

abstract class BaseRepository(private val api: BaseApi) : SafeApiCall {

    suspend fun logout() = safeApiCall {
        api.logout()
    }
}