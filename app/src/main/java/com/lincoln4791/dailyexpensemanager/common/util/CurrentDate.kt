package com.lincoln4791.dailyexpensemanager.common.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

@SuppressLint("SimpleDateFormat")
class CurrentDate {
    companion object {
        val currentDate: String
            get() {
                val c = Calendar.getInstance()
                val df = SimpleDateFormat("yyyy-MM-dd")
                return df.format(c.time)
            }

        val currentTime: String
            get() {
                val c = Calendar.getInstance()
                val df = SimpleDateFormat("HH:mm:ss")
                return df.format(c.time)
            }

        val currentTime24H: String
            get() {
                val c = Calendar.getInstance()
                val df = SimpleDateFormat("hh:mm aa")
                return df.format(c.time)
            }

        val currentDateTime: String
            get() {
                val c = Calendar.getInstance()
                val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                return df.format(c.time)
            }


        val timeStamp: String
            get() {
                val c = Calendar.getInstance().timeInMillis
                return c.toString()
            }


        val getCalenderDateInMill: Long
            get() {
                return Calendar.getInstance().timeInMillis
            }



        fun getTimeInMIllFromOnline(context: Context, callback:(isSuccess:Boolean, timeInMil:Long?, error:String?)->Unit){
            if(NetworkCheck.isConnect(context)){
                try {
                    CoroutineScope(Dispatchers.IO).launch {
                        var time : Long? = null
                        val url = URL("https://currentmillis.com/time/minutes-since-unix-epoch.php")
                        val con: HttpURLConnection = url.openConnection() as HttpURLConnection
                        con.requestMethod = "GET"
                        val inR = BufferedReader(InputStreamReader(con.inputStream))
                        time = inR.readLine().toLong() * 60 * 1000
                        inR.close()
                        con.disconnect()
                        //time = ServerValue.TIMESTAMP
                        android.os.Handler(Looper.getMainLooper()).post {
                            callback(true,time , null)
                        }
                    }
                }
                catch (e: Exception){
                    Handler(Looper.getMainLooper()).post{
                        callback(false,null,"")
                        e.printStackTrace()
                    }
                }
            }
            else{
                Handler(Looper.getMainLooper()).post {
                    callback(false,null,"No Internet Connection")
                }
            }
        }


        fun getDayDifFromMilSecond(from : Long, to : Long) : Int{
            val diffInMill = from - to
            val dayParameter = 1000*60*60*24
            return (diffInMill/dayParameter).toInt()
        }

        /*   fun getMinDiffFromMill(context: Context,lastTime:Long) : Long{
               val prefManager = PrefManager(context)
               val currenttime = System.currentTimeMillis()
               val lastT = prefManager.lastAutoSyncRunTime
               val diff = (currenttime-lastTime)/60000
               Log.d("time","current time-> $currenttime::: lasttime-> $lastTime ::: Diff -> $diff")
               return lastT
           }*/

        fun getDayDiffValues(numberOfDay : Int) : String{
            var dayValue = ""
            if(numberOfDay==0){
                dayValue = "today"
            }

            else if(numberOfDay<0){
                if(numberOfDay==-1){
                    dayValue = "yesterday"
                }
                else{
                    dayValue  ="${abs(numberOfDay)} days ago"
                }

            }

            else if(numberOfDay>0){
                if(numberOfDay==1){
                    dayValue = "tomorrow"
                }
                else{
                    dayValue = "in $numberOfDay days"
                }
            }

            return  dayValue
        }

        fun getDayDiffValuesBng(numberOfDay : Int) : String{
            var dayValue = ""
            if(numberOfDay==0){
                dayValue = "আজকে"
            }

            else if(numberOfDay<0){
                if(numberOfDay==-1){
                    dayValue = "গতকাল"
                }
                else{
                    dayValue  ="${DayDifference.getBngDigitFromEngDigit(abs(numberOfDay).toString())} দিন আগে"
                }

            }

            else if(numberOfDay>0){
                if(numberOfDay==1){
                    dayValue = "আগামি কাল"
                }
                else{
                    dayValue = "${DayDifference.getBngDigitFromEngDigit(abs(numberOfDay).toString())} দিন পর"
                }
            }

            return  dayValue
        }


        fun getDateFromMill(timeInMIll : Long) : String{
            var finalDate = ""
            val date = Date(timeInMIll).toString()
            val dateSubStringArray = date.split(" ").toTypedArray()
            finalDate = "${dateSubStringArray[0]} ${dateSubStringArray[1]} ${dateSubStringArray[2]} ${dateSubStringArray[5]}"
            return finalDate
        }


    }
}
