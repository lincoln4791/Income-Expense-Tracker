package com.itmedicus.patientaid.ads.admobAdsUpdated

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.lincoln4791.dailyexpensemanager.common.util.CurrentDate
import com.lincoln4791.dailyexpensemanager.BuildConfig
import com.lincoln4791.dailyexpensemanager.common.PrefManager
import com.lincoln4791.dailyexpensemanager.common.util.NetworkCheck
import java.text.SimpleDateFormat
import kotlin.math.abs

class AdMobUtil {
    companion object{
        private val tag = "AdMob"

        private val TEST_AD_ID = "ca-app-pub-3940256099942544~3347511713"


        private val testDeviceIds = listOf<String>("c86b65ba-aa79-4913-8900-ad506c4ce57a","2831bbff-d301-4ff9-90a7-a41cfa7b7c5a",
        "f26dd6c3-ae53-4470-880b-ebd4147e12e9","9ce9d179-cbbf-4d5e-a340-c57ef3836adc","2b461cf6-e0f6-40ea-8283-a4f1e040d8a4",
        "9e2891e0-0a37-455a-8400-0016a9314c6d")

        @SuppressLint("SimpleDateFormat")
        fun diffTime(time: String): Long {
            var min: Long = 0
            val difference: Long
            try {
                val simpleDateFormat = SimpleDateFormat("hh:mm aa") // for 12-hour system, hh should be used instead of HH
                // There is no minute different between the two, only 8 hours difference. We are not considering Date, So minute will always remain 0
                val date1 = simpleDateFormat.parse(time)
                val date2 = simpleDateFormat.parse(CurrentDate.currentTime24H)
                difference = (date2?.time!! - date1?.time!!) / 1000
                val hours = difference % (24 * 3600) / 3600 // Calculating Hours
                val minute = difference % 3600 / 60 // Calculating minutes if there is any minutes difference
                min = minute + hours * 60 // This will be our final minutes. Multiplying by 60 as 1 hour contains 60 mins
            } catch (e: Throwable) {
                Log.d(tag,"exception in getting dff time of can ad show method -> ${e.message}")
                e.printStackTrace()
            }
            Log.d("AdMob", "$min of difference")
            return abs(min)
        }


        fun canAdShow(context:Context,lastAdShownDate:String) : Boolean{
            val prefManager = PrefManager(context)
            var canAdShow  = false
            var error = ""

            if(NetworkCheck.isConnect(context)){
                if(prefManager.isPremiumUser || prefManager.isAdRemoved){
                    Log.d("AdMob","Ad wont show because this is premium user")
                }
                else{
                    if (lastAdShownDate!=""){
                        //if (diffTime(lastAdShownDate).toInt() >= prefManager.adInterval.toInt()) {
                        if (diffTime(lastAdShownDate).toInt() >= 0) {
                            //if (diffTime(lastAdShownDate).toInt() >=0) {
                            info(tag," Ad shown because difference is greater than ${prefManager.adInterval}")
                            canAdShow = true
                        } else {
                            info(tag," AD not loaded because difference is less than ${prefManager.adInterval}")
                            error = "AD not loaded because difference is less than 1"
                            canAdShow  = false
                        }
                    }



                    else {
                        info(tag," AD loaded because difference is either empty or less then 3")
                        canAdShow  = true
                    }
                }

            }
            else{
                error = "No Internet Connection"
               canAdShow = false

            }

            Log.d("AdMob","AdCanShow -> $canAdShow ::: message -> $error ")

            return canAdShow
        }

        fun isAppInstalledMinimumThreeDaysAgo(context: Context) : Boolean{
            var isInstalled = false
            val prefManager = PrefManager(context)
            val dif = diffTime(prefManager.appInstallDate)

            Log.d("AdMob"," Inter AD diff is -> $dif ")

            if(dif>3){
                isInstalled = true
            }

            return isInstalled
        }

        fun info(tag: String, msg: String){
            if (BuildConfig.DEBUG) Log.i(tag, msg)
        }


        fun setTestDevices(){
            val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
            MobileAds.setRequestConfiguration(configuration)
        }
    }



}