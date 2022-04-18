package com.lincoln4791.dailyexpensemanager.common.util

import android.content.Context
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.common.PrefManager

class FirebaseUtil {

    companion object{
        fun fetchDataFromRemoteConfig(context:Context){
             val prefManager = PrefManager(context)
             val firebaseRemoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

            Log.d("Remote","Remote Config Inited")
            val configSettings  = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 3600
            }
            firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
            firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_cofig_defaults)

            //val cacheExpiration: Long = 3600 // 1 hour in seconds.

            firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener {
                if (it.isSuccessful) {
                    val updated = it.result
                    prefManager.adInterval = firebaseRemoteConfig.getString(Constants.ADD_INTERVAL)
                    Log.d("Remote", "Remote : adInterval is : ${prefManager.adInterval}")

                } else {
                    Log.d("Remote", "fetch Failed")
                }
            }

        }
    }

}