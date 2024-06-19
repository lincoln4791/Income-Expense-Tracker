package com.lincoln4791.dailyexpensemanager

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.google.firebase.FirebaseApp
import com.google.firebase.inappmessaging.FirebaseInAppMessaging
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.installations.FirebaseInstallationsApi
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
       FirebaseApp.initializeApp(this);
       getFirebaseInstallationId()

       //FirebaseInAppMessaging.getInstance().isAutomaticDataCollectionEnabled = true;
       //Log.d("tag","FID - ${}")
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

    private fun getFirebaseInstallationId() {
        FirebaseInstallations.getInstance().id.addOnSuccessListener { installationId ->
            Log.d("tag", "FID ${installationId}")
            // Use the installationId as needed
        }.addOnFailureListener { e ->
            Log.e("tag", "Error getting Firebase Installation ID", e)
        }
    }
}