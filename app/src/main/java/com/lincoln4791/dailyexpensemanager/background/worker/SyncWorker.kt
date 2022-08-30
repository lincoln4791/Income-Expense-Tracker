package com.lincoln4791.dailyexpensemanager.background.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.common.PrefManager
import com.lincoln4791.dailyexpensemanager.common.util.*
import com.lincoln4791.dailyexpensemanager.modelClass.Banner
import com.lincoln4791.dailyexpensemanager.modelClass.BannerResponse
import com.lincoln4791.dailyexpensemanager.roomDB.AppDatabase
import com.lincoln4791.dailyexpensemanager.roomDB.DatabaseDao
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import javax.inject.Inject



class SyncWorker(var context: Context, workerParameter: WorkerParameters) :
    CoroutineWorker(context, workerParameter) {

    @Inject lateinit var prefManager: PrefManager
    @Inject lateinit var dao: DatabaseDao
    private val sdf = SimpleDateFormat("yyyy-MM-dd")
    private val currentDate = sdf.parse(CurrentDate.currentDate)

    override suspend fun doWork(): Result {

        try {
            init()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("tag", "doWork: Exception: ${e.localizedMessage}")

            return Result.retry()
        }

        return Result.success()
    }


    private fun init() {
        CoroutineScope(Dispatchers.Default).launch {
            startSync()
        }

    }


    private suspend fun startSync() {

        CoroutineScope(Dispatchers.Default).launch { banner() }
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun banner() {

        withContext(Dispatchers.Default) {
            if (NetworkCheck.isConnect(applicationContext)) {
                Log.d("bannerSync","Called")


/*                val database = Firebase.database.reference
                val listOfBanners : MutableList<Banner> = mutableListOf()
                database.child(Constants.Common).child(Constants.BANNER).child(Constants.BANNERS).addListenerForSingleValueEvent(object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        Log.d(TAG,"Snapshot is -> $snapshot")
                        for(element in snapshot.children){
                            val data = element.getValue(Banner::class.java)
                            data!!.is_active = element.child("is_active").getValue(String::class.java)!!
                            Log.d(TAG,"element is -> $data")
                            listOfBanners.add(data)
                        }

                        Log.d(TAG,"Banner List is -> $listOfBanners")

                        for(element in listOfBanners){
                            if(element.created_date.isNotEmpty()){
                                CoroutineScope(Dispatchers.IO).launch {
                                    insertBanner(element)
                                }
                                CoroutineScope(Dispatchers.IO).launch {
                                    FileUtils.saveImageToAppSpecificStorageFromUrl(applicationContext,element.name,element.media_url)
                                }
                            }

                            if(element.deleted_date.isNotEmpty()){
                                CoroutineScope(Dispatchers.IO).launch {
                                    deleteBanner(element)
                                }
                            }
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d(TAG,"Something is wrong in sync -> ${error.message} :: code -> ${error.code}")
                    }

                })*/




                val listOfBanners : MutableList<Banner> = mutableListOf()
                val firebaseRemoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
                Log.d(TAG,"Remote Config Inited")
                val configSettings  = remoteConfigSettings {
                    minimumFetchIntervalInSeconds = 3600
                }
                firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
                firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_cofig_defaults)

                firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener {
                    if (it.isSuccessful) {

                        val dataAsJson = firebaseRemoteConfig.getString(Constants.BANNER_SYNC)
                        Log.d(TAG,"JSON DATA is -> ${dataAsJson}, size is -> ${dataAsJson.length}")
                        val gson = Gson()
                        val subscriptionData = gson.fromJson(dataAsJson,BannerResponse::class.java)

                        Log.d(TAG,"List is -> $subscriptionData")

                        if(subscriptionData.banners.isNullOrEmpty()){
                            Log.d(TAG,"banner null or empty")
                        }
                        else{

                            for(banner in subscriptionData.banners){
                                //if(banner.is_active == "1"){
                                    listOfBanners.add(banner)
                                //}
                            }


                            for(element in listOfBanners){
                                if(element.created_date.isNotEmpty()){
                                    CoroutineScope(Dispatchers.IO).launch {
                                        insertBanner(element)
                                    }
                                    CoroutineScope(Dispatchers.IO).launch {
                                        FileUtils.saveImageToAppSpecificStorageFromUrl(applicationContext,element.name,element.media_url)
                                    }
                                }

                                if(element.deleted_date.isNotEmpty()){
                                    CoroutineScope(Dispatchers.IO).launch {
                                        deleteBanner(element)
                                    }
                                }
                            }

                        }

                    }
                    else {
                        Log.d(TAG, "fetch Failed")
                    }

                }
            }
            else{
                Log.d("bannerSync","No Internet")
            }

        }

    }

    private suspend fun insertBanner(banner: Banner){
        dao.insertBanner(banner)
    }

    private suspend fun deleteBanner(banner: Banner){
        dao.deleteBanner(banner)
    }


    companion object{
        const val TAG = "bannerSync"
    }

}
