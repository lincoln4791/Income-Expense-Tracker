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


}