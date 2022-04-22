package com.lincoln4791.dailyexpensemanager.common.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.work.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.lincoln4791.dailyexpensemanager.background.worker.PeriodicSyncWorker
import com.lincoln4791.dailyexpensemanager.background.worker.SyncWorker
import java.util.concurrent.TimeUnit

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


    }
}