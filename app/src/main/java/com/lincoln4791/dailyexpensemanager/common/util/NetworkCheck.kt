package com.lincoln4791.dailyexpensemanager.common.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

object NetworkCheck {

    fun isConnect(context: Context): Boolean {
        try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return if (activeNetworkInfo != null) {
                activeNetworkInfo.isConnected || activeNetworkInfo.isConnectedOrConnecting
            } else {
                false
            }
        } catch (e: Exception) {
            return false
        }

    }

}
