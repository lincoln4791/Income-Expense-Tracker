package com.lincoln4791.dailyexpensemanager.network;


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val hostAddress: String = "http://sandbox.emdexapi.com/"
    private const val HOST_ADDRESS_QUOTE: String = "http://api.quotable.io/"
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .readTimeout(90, TimeUnit.SECONDS)
        .writeTimeout(90, TimeUnit.SECONDS)
        .connectTimeout(90, TimeUnit.SECONDS)
        .build()

    private val gson: Gson = GsonBuilder()
        .setLenient()
        .create()


    fun getRetrofitAuthClient(): Api {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(hostAddress)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
        return retrofit.create(Api::class.java)
    }

    fun getRetrofitQuoteClient(): QuoteApi {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(HOST_ADDRESS_QUOTE)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
        return retrofit.create(QuoteApi::class.java)
    }


}