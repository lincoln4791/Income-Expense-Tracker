package com.lincoln4791.dailyexpensemanager.network

import retrofit2.http.*

interface UsersApi {

    @Headers("P-Auth-Token:test")
    //@GET("api/v1/login")
    @GET("api/v1/login")
    suspend fun login(
        /*@Field("phone") username : String,
        @Field("password") password : String*/
        @Query("result") result: String
    )

}