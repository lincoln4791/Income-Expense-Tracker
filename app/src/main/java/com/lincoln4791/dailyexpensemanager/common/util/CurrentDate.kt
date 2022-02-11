package com.itmedicus.patientaid.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class CurrentDate {
    companion object{
        val currentDate: String
            get() {
                val c = Calendar.getInstance()
                val df = SimpleDateFormat("yyyy-MM-dd")
                return df.format(c.time)
            }

        val currentTime:String
            get() {
                val c = Calendar.getInstance()
                val df = SimpleDateFormat("HH:mm:ss")
                return df.format(c.time)
            }

        val currentTime24H:String
            get() {
                val c = Calendar.getInstance()
                val df = SimpleDateFormat("hh:mm aa")
                return df.format(c.time)
            }

        val currentDateTime:String
            get() {
                val c = Calendar.getInstance()
                val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                return df.format(c.time)
            }


        val timeStamp:String
            get() {
                val c = Calendar.getInstance().timeInMillis
                return c.toString()
            }


        val getCalenderDateInMill:Long
            get() {
                return Calendar.getInstance().timeInMillis
            }
    }
    }
