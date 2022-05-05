package com.lincoln4791.dailyexpensemanager.network

import retrofit2.Call
import retrofit2.http.*

interface AuthApi {

    @Headers("P-Auth-Token:P4t13nt4idu53r")
    @POST("api/v1/login")
    suspend fun login(
        /*@Field("phone") username : String,
        @Field("password") password : String*/
        @Body loginBodyModel: Any
    ) : Any

}