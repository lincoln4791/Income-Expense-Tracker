package com.lincoln4791.dailyexpensemanager.common.util

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class Util(){
    companion object{
        fun getMonthNameFromMonthNumber(monthNumber : String):String{
            var monthName = ""
            if(monthNumber=="01"){
                monthName = "January"
            }
            else if(monthNumber=="02"){
                monthName="February"
            }
            else if(monthNumber=="03"){
                monthName="March"
            }
            else if(monthNumber=="04"){
                monthName="April"
            }
            else if(monthNumber=="05"){
                monthName="May"
            }
            else if(monthNumber=="06"){
                monthName="June"
            }
            else if(monthNumber=="07"){
                monthName="July"
            }
            else if(monthNumber=="08"){
                monthName="August"
            }
            else if(monthNumber=="09"){
                monthName="September"
            }
            else if(monthNumber=="10"){
                monthName="October"
            }
            else if(monthNumber=="11"){
                monthName="November"
            }
            else if(monthNumber=="12"){
                monthName="December"
            }
            else{
                monthName="Blank"
            }


            return monthName
        }

        fun recordScreenEvent(screenName:String,screenClass : String){
            val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME,screenName)
            bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass)
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
        }
    }
}