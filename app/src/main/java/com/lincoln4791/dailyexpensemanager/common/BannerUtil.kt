package com.lincoln4791.dailyexpensemanager.common

import android.content.Context
import android.os.Looper
import com.lincoln4791.dailyexpensemanager.modelClass.Banner
import com.lincoln4791.dailyexpensemanager.roomDB.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class BannerUtil() {

    companion object{
        suspend fun getAllBanners(context: Context,callback:(bannerList:MutableList<Banner>?)->Unit){
            val dao = AppDatabase.getInstance(context.applicationContext).dbDao()
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    val list = dao.getAllBanner()
                    android.os.Handler(Looper.getMainLooper()).post {
                        callback(list)
                    }

                }
            }
            catch (e:Exception){
                android.os.Handler(Looper.getMainLooper()).post {
                    callback(null)
                }
            }


        }

        suspend fun getAllActiveBanners(context: Context,callback:(bannerList:MutableList<Banner>?)->Unit){
            val dao = AppDatabase.getInstance(context.applicationContext).dbDao()
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    val list = dao.getAllActiveBanner("1")
                    android.os.Handler(Looper.getMainLooper()).post {
                        callback(list)
                    }

                }
            }
            catch (e:Exception){
                android.os.Handler(Looper.getMainLooper()).post {
                    callback(null)
                }
            }


        }
    }

}