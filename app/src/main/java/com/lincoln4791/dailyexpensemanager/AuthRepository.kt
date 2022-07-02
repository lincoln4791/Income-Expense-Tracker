package com.lincoln4791.dailyexpensemanager

import com.lincoln4791.dailyexpensemanager.base.BaseRepository
import com.lincoln4791.dailyexpensemanager.network.AuthApi

class AuthRepository(private val api : AuthApi) : BaseRepository() {

/*    suspend fun login(
        userName:String,
        password:String,
    ) = safeApiCAll {
        Log.d("tag","Login CAlled in UTH rePOSITORY u-> $userName :: Pass -> $password ")
        val loginBody = LoginBodyModel()
        loginBody.phone = userName
        loginBody.password = password
        api.login(loginBody)
    }*/

}