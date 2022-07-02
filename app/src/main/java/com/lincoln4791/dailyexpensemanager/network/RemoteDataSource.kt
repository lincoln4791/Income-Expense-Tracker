package com.lincoln4791.dailyexpensemanager.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RemoteDataSource {
    companion object{
        const val BASE_URL = "http://patientaid.itmapi.com/"


        fun<Api> buildApi(
            api: Class<Api>,
        ):Api{

            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
            val client: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                /*.client(OkHttpClient.Builder().also {client->
                    if(BuildConfig.DEBUG){
                        val logging = HttpLoggingInterceptor();
                        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                        client.addInterceptor(logging)
                    }
                }.build())*/
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(api)
        }

    }

}