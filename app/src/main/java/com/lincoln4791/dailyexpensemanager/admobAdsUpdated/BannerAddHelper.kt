package com.itmedicus.patientaid.ads.admobAdsUpdated

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.ads.*
import com.lincoln4791.dailyexpensemanager.common.PrefManager

class BannerAddHelper(private val context: Context) {
    val prefManager = PrefManager(context)


    fun loadBannerAd(mAdView : AdView, callBack:(isShown:Boolean)->Unit){
        //MobileAds.initialize(context)

        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)


        mAdView.adListener = object: AdListener(){
            override fun onAdLoaded() {
                //Toast.makeText(this@MainActivity, "Add clicked",Toast.LENGTH_SHORT).show()
                Log.d("Banner","Banner Loaded")
                callBack(true)

            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                // Code to be executed when an ad request fails.
                Log.d("Banner","Banner Failed")
                callBack(false)
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.d("Banner","Banner Opened")
            }

            override fun onAdClicked() {
                //Toast.makeText(context, "Add clicked", Toast.LENGTH_SHORT).show()
                Log.d("Banner","banner Clicked")
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                //Toast.makeText(context, "Add clicked", Toast.LENGTH_SHORT).show()
                Log.d("Banner","banner Closed")

            }
        }
    }

}
