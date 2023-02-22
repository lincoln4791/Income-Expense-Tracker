package com.lincoln4791.dailyexpensemanager.common.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
import android.net.Uri
import android.os.Bundle
import android.util.LayoutDirection
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.common.PrefManager
import com.lincoln4791.dailyexpensemanager.view.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class Util(){
    companion object{

        fun getMonthNameFromMonthNumber(monthNumber : String):String{
            var monthName = ""
            when (monthNumber) {
                "01" -> {
                    monthName = "January"
                }
                "02" -> {
                    monthName="February"
                }
                "03" -> {
                    monthName="March"
                }
                "04" -> {
                    monthName="April"
                }
                "05" -> {
                    monthName="May"
                }
                "06" -> {
                    monthName="June"
                }
                "07" -> {
                    monthName="July"
                }
                "08" -> {
                    monthName="August"
                }
                "09" -> {
                    monthName="September"
                }
                "10" -> {
                    monthName="October"
                }
                "11" -> {
                    monthName="November"
                }
                "12" -> {
                    monthName="December"
                }
                else -> {
                    monthName="Blank"
                }
            }


            return monthName
        }

        fun recordScreenEvent(screenName:String,screenClass : String){
            val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME,screenName)
            bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass)
            //bundle.putString("TestCustomEvent", "I am test Custom Event")
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
            Log.d("Analytics","Screen Recorded")
        }


        fun recordNotificationEvent(title:String){
            val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics
            val bundle = Bundle()
            //bundle.putString("title","myTitle")
            bundle.putString("title",title)
            firebaseAnalytics.logEvent("notification_received", bundle)
            Log.d("Analytics","Screen Recorded -> $title")
        }

        fun recordNotificationEvent2(title:String){
            val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics
            val bundle = Bundle()
            //bundle.putString("title","myTitle")
            bundle.putString("notification_received_cd_demo",title)
            firebaseAnalytics.logEvent("notification_received_event_demo", bundle)
            Log.d("Analytics","Screen Recorded -> $title")
        }

        fun isEnglishWord(name: String): Boolean {
            var isEnglish = true
            if(name.isNotEmpty()){
                val chars = name.toCharArray()
                for (c in chars) {
                    if (!c.toString().matches(".*[ a-zA-Z0-9].*".toRegex())) {
                        isEnglish = false
                    }
                }
            }

            else{

            }
            return isEnglish
        }


        fun goToFacebookPage(context: Context){
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/104842935132029"))
                context.startActivity(intent)
            } catch (e: Exception) {
                val intent =
                    Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.facebook.com/IncomeExpenseManager/"))
                context.startActivity(intent)
                e.printStackTrace()
            }
        }


        fun initAdRemoveByRewardAd(context: Context){
             CoroutineScope(Dispatchers.IO).launch {
                 val prefManager = PrefManager(context.applicationContext)
                 val expireTime = prefManager.adRemoveExpireTime

                 if(expireTime>System.currentTimeMillis()){
                     val difference = expireTime-System.currentTimeMillis()
                     val expireTimeThreshold = (prefManager.adRemoveDurationDayByAd.toLong())*(Constants.INTERVAL_DAILY.toLong())
                     if(difference>expireTimeThreshold){
                         //User Showed an reward add when user's phones clock time was not correct
                         val newExpireTime = System.currentTimeMillis()+expireTimeThreshold
                         prefManager.adRemoveExpireTime = newExpireTime
                         Log.d("rewardAd","Reward Ad Expired Time reset, new time is -> $newExpireTime ")
                     }
                     Log.d("rewardAd","Reward Ad not Expired, expired time is  -> $expireTime ")
                     prefManager.isAdRemoved=true

                 }
                 else{
                     Log.d("rewardAd","Reward Ad Expired Time Expired ")
                     prefManager.isAdRemoved=false
                 }
             }

        }

        fun showLoginRequiredDialog(context: Context,callback:(action:Boolean)->Unit){
            val dialog = Dialog(context)
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_login_required,null,false)
            dialog.setCancelable(false)
            dialog.setContentView(dialogView)
            try {
                dialog.show()
            }
            catch (e:Exception){e.printStackTrace()}


            dialogView.findViewById<Button>(R.id.btn_login).setOnClickListener {
                try {
                    dialog.dismiss()
                    callback(true)
                }
                catch (e:Exception){e.printStackTrace()}

            }

            dialogView.findViewById<ImageView>(R.id.iv_close).setOnClickListener {
                try {
                    dialog.dismiss()
                    callback(false)
                }
                catch (e:Exception){e.printStackTrace()}

            }

        }


        fun showNoInternetDialog(context: Context,callback:(action:Boolean)->Unit){
            val dialog = Dialog(context)
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_login_required,null,false)
            dialogView.findViewById<TextView>(R.id.tv_title).text = "Internet Connection Required"
            dialogView.findViewById<TextView>(R.id.tv_body).text = "Please connect to the internet!!"
            dialogView.findViewById<Button>(R.id.btn_login).text = "ok!"
            dialogView.findViewById<LottieAnimationView>(R.id.lotte).setAnimation(R.raw.no_internet)
            dialog.setCancelable(false)
            dialog.setContentView(dialogView)
            try {
                dialog.show()
            }
            catch (e:Exception){e.printStackTrace()}


            dialogView.findViewById<Button>(R.id.btn_login).setOnClickListener {
                try {
                    dialog.dismiss()
                    callback(true)
                }
                catch (e:Exception){e.printStackTrace()}

            }

            dialogView.findViewById<ImageView>(R.id.iv_close).setOnClickListener {
                try {
                    dialog.dismiss()
                    callback(false)
                }
                catch (e:Exception){e.printStackTrace()}
            }

        }




        fun removeCommonUserData(context: Context){
            val prefManager = PrefManager(context)
            prefManager.name = ""
            prefManager.phone = ""
            prefManager.email = ""
            prefManager.UID = ""
            prefManager.isLoggedIn=false
        }

        fun addCommonUserData(context: Context,user : FirebaseUser){
            val prefManager = PrefManager(context)
            prefManager.name = user.displayName?:""
            prefManager.phone = user.phoneNumber?:""
            prefManager.email = user.email?:""
            prefManager.UID = user.uid
            prefManager.isLoggedIn=true
        }

        fun closeApp(activity: Activity){
            //activity.finishAffinity()
            //activity.finishAndRemoveTask()
            activity.finish();
            exitProcess(0);
        }

        fun reloadApp(activity: Activity){
            val intent = Intent(activity,MainActivity::class.java)
            intent.flags=FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
            intent.flags = FLAG_ACTIVITY_CLEAR_TOP
            activity.startActivity(intent)
        }

    }
}