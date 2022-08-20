package com.lincoln4791.dailyexpensemanager

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyApplication : Application() {
   // private var appOpenManager: AppOpenManager? = null
    override fun onCreate() {
        super.onCreate()
        /*MobileAds.initialize(
            this
        ) { }*/

     //   appOpenManager =  AppOpenManager(this
    }
}