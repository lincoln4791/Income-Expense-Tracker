package com.lincoln4791.dailyexpensemanager.common

import android.content.Context
import android.content.SharedPreferences

class PrefManager(val context : Context) {

    private var pref: SharedPreferences
    private var editor: SharedPreferences.Editor
    private var PRIVATE_MODE = 0

    init {
        pref = context.getSharedPreferences("IEManager", PRIVATE_MODE)
        editor = pref.edit()
    }

    var isDarkThemeEnabled:Boolean
        get() = pref.getBoolean("isDarkThemeEnabled",false)
        set(value) {editor.putBoolean("isDarkThemeEnabled",value).commit()}


    var fcmToken:String?
        get() = pref.getString("fcmToken","")
        set(value) {editor.putString("fcmToken",value).commit()}

    var isUserLoggedIn:Boolean
        get() = pref.getBoolean("isUserLoggedIn",false)
        set(value) {editor.putBoolean("isUserLoggedIn",value).commit()}


    var isAppInstalledFirstTime:Boolean?
        get() = pref.getBoolean("isAppInstalledFirstTime",true)
        set(value) {editor.putBoolean("isAppInstalledFirstTime", value!!).commit()}

    var appVersion:String?
        get() = pref.getString("appVersion","1")
        set(value) {editor.putString("appVersion",value).commit()}

    var appInstallDate:String
        get() = pref.getString("appInstallDate","")!!
        set(value) {editor.putString("appInstallDate",value).commit()}

    var versionControlCheckLastDate:String
        get() = pref.getString("versionControlCheckLastDate", "2021-08-10")!!
        set(value) {editor.putString("versionControlCheckLastDate",value).commit()}


}