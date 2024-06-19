package com.lincoln4791.dailyexpensemanager.admobAdsUpdated

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class InterstistialAdHelper(private val context: Context,private val activity:Activity,private var mInterstitialAd: InterstitialAd?) {

    //var mInterstitialAd: InterstitialAd? = null

    fun loadinterAd(aui:String,callback:(isAdLoaded : Boolean)-> Unit){
        InterstitialAd.load(context,aui, AdRequest.Builder().build(), object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d("InterAD", adError.message)
                mInterstitialAd = null
                callback(false)

            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d("InterAD", "InterAd was loaded.")
                mInterstitialAd = interstitialAd
                callback(true)
            }
        })

    }


    fun showInterAd(callback : (isShown : Boolean,error:String?)->Unit){

        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d("InterAD", "InterAd was dismissed.")
                callback(true,null)
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                Log.d("InterAD", "InterAd failed to show -> ${adError.message}")
                callback(false,adError.message)
            }

            override fun onAdShowedFullScreenContent() {
                Log.d("InterAD", "InterAd showed fullscreen content.")
                mInterstitialAd = null
            }
        }

        if (mInterstitialAd != null) {
            mInterstitialAd?.show(activity)
        } else {
            Log.d("InterAD", "The interstitial ad wasn't ready yet.")
            callback(false,"Inter Ad is not ready yet!")
        }

    }




}