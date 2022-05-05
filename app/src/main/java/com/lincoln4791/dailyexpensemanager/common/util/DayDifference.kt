package com.lincoln4791.dailyexpensemanager.common.util

import android.util.Log
import java.util.*

class DayDifference {
    companion object{
        fun getDaysDifference(fromDate: Date?, toDate: Date?): Int {
            return if (fromDate == null || toDate == null) 0 else ((toDate.time - fromDate.time) / (1000 * 60 * 60 * 24)).toInt()
        }

        fun getBngDigitFromEngDigit(engDigit : String):String{
            Log.d("tag","Bangla DIgit is -> $engDigit")
            var bngDigit : String = ""

            for(dgt in engDigit){
                Log.d("tag","Looping, element is -> $dgt")

                if(dgt == '0'){
                    bngDigit += "০"
                }
                else if(dgt == '1'){
                    bngDigit += "১"
                }
                else if(dgt == '2'){
                    bngDigit += "২"
                }
                else if(dgt == '3'){
                    bngDigit += "৩"
                }
                else if(dgt == '4'){
                    bngDigit += "৪"
                }
                else if(dgt == '5'){
                    bngDigit += "৫"
                }
                else if(dgt == '6'){
                    bngDigit += "৬"
                }
                else if(dgt == '7'){
                    bngDigit += "৭"
                }
                else if(dgt == '8'){
                    bngDigit += "৮"
                }
                else if(dgt == '9'){
                    bngDigit += "৯"
                }
                else{
                    Log.d("tag","Something is not ok,please check input data")
                }
            }

            Log.d("tag","Bangla DIgit is -> $bngDigit")
            return bngDigit
        }

    }
}