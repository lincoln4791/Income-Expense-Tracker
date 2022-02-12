/*
package com.itmedicus.patientaid.ads.admobAdsUpdated
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.AdError
import androidx.lifecycle.Lifecycle.Event.ON_START
import com.google.android.gms.ads.FullScreenContentCallback
import androidx.lifecycle.OnLifecycleEvent
import com.lincoln4791.dailyexpensemanager.MyApplication

import java.util.*


class AppOpenManager
*/
/** Constructor  *//*
(private var myApplication: MyApplication?) : LifecycleObserver, Application.ActivityLifecycleCallbacks {
    private var currentActivity: Activity? = null
    //private val AD_UNIT_ID = "ca-app-pub-3940256099942544/3419835294"
    //private val AD_UNIT_ID = "ca-app-pub-7974966348786771/9555310918"
    private val AD_UNIT_ID = "ca-app-pub-7974966348786771/4022053257"
    private var appOpenAd: AppOpenAd? = null
    private var loadCallback: AppOpenAdLoadCallback? = null
    private var isShowingAd = false
    private var loadTime: Long = 0

    init {
        //myApplication!!.registerActivityLifecycleCallbacks(this)
        //ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    */
/** Request an ad  *//*

    fun fetchAd() {
        if (isAdAvailable()) {
            Log.d("AppOpenAd","inside isAvailable Called")
            return
        }
        Log.d("AppOpenAd","after isAvailable Called")
        loadCallback = object : AppOpenAdLoadCallback() {
            override fun onAdLoaded(ad: AppOpenAd) {
                super.onAdLoaded(ad)
                appOpenAd = ad
                loadTime = Date().time
                Log.d("AppOpenAd","onAdLoaded() called")
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                Log.d("AppOpenAd","onAdFailedToLoad() called -> ${p0!!.message}")
            }

        */
/*    override fun on(ad: AppOpenAd?) {


            }
*//*
*/
/*
            override fun onAppOpenAdFailedToLoad(p0: LoadAdError?) {
                super.onAppOpenAdFailedToLoad(p0)
                Log.d("tag","onAdFailedToLoad() called -> ${p0!!.message}")
            }*//*

           */
/* override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                Log.d("tag","onAdFailedToLoad() called")
            }*//*

        }
        val request = getAdRequest()
        AppOpenAd.load(
            myApplication, AD_UNIT_ID, request,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback)
    }

    */
/** Creates and returns ad request.  *//*

    private fun getAdRequest(): AdRequest? {
        return AdRequest.Builder().build()
    }

    */
/** Utility method that checks if ad exists and can be shown.  *//*

    fun isAdAvailable(): Boolean {
        //return appOpenAd != null
        //Log.d("tag","isAdAvailable -> $appOpenAd::: ${wasLoadTimeLessThanNHoursAgo(4)} :::${appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)} ")
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {

    }

    override fun onActivityStarted(p0: Activity) {
        currentActivity = p0
    }

    override fun onActivityResumed(p0: Activity) {
        currentActivity = p0
    }

    override fun onActivityPaused(p0: Activity) {

    }

    override fun onActivityStopped(p0: Activity) {

    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {

    }

    override fun onActivityDestroyed(p0: Activity) {
        currentActivity = null
    }

    */
/** Shows the ad if one isn't already showing.  *//*

    fun showAdIfAvailable() {
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.
        if (!isShowingAd && isAdAvailable()) {
            Log.d("AppOpenAd", "Will show ad.")
            val fullScreenContentCallback: FullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        // Set the reference to null so isAdAvailable() returns false.
                        appOpenAd = null
                        isShowingAd = false
                        Log.d("AppOpenAd"," Add Dismissed")
                        fetchAd()
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        Log.d("AppOpenAd"," Can not show ad -> ${adError.message} ")
                    }
                    override fun onAdShowedFullScreenContent() {
                        isShowingAd = true
                        Log.d("AppOpenAd"," AdShowedFullScreenContent ")
                    }
                }
            appOpenAd!!.fullScreenContentCallback = fullScreenContentCallback
            appOpenAd!!.show(currentActivity)
            //appOpenAd!!.show(currentActivity,fullScreenContentCallback)
        } else {
            Log.d("AppOpenAd", "Can not show ad.")
            fetchAd()
        }
    }

    @OnLifecycleEvent(ON_START)
    fun onStart() {
        Log.d("AppOpenAd", "onStart")
        showAdIfAvailable()

    }

    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
        val dateDifference = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        Log.d("AppOpenAd","loadTime -> $loadTime:::currentTime->${Date().time} ::: Diff-> $dateDifference ::: ConstantHourDiff -> ${numMilliSecondsPerHour*numHours}")
        return dateDifference < numMilliSecondsPerHour * numHours
        //return dateDifference < 30000
    }

}*/
