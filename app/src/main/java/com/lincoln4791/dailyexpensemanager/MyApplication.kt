package com.lincoln4791.dailyexpensemanager

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class MyApplication : Application(), Configuration.Provider {
    @Inject lateinit var workerFactory: HiltWorkerFactory
   // private var appOpenManager: AppOpenManager? = null
    override fun onCreate() {
        super.onCreate()
        /*MobileAds.initialize(
            this
        ) { }*/
       instance=this
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    companion object {
        lateinit var instance: MyApplication
            private set
    }
}