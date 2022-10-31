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

    var adInterval:String
        get() = pref.getString("adInterval", "4")!!
        set(value) {editor.putString("adInterval",value).commit()}

    var adRemoveDurationDayByAd:String
        get() = pref.getString("adRemoveDurationDayByAd", "3")!!
        set(value) {editor.putString("adRemoveDurationDayByAd",value).commit()}







    // ads
    var lastBannerAdShownHomeF:String
        get() = pref.getString("lastBannerAdShownHomeF","")!!
        set(value) {editor.putString("lastBannerAdShownHomeF",value).commit()}

    var lastBannerAdShownTransactionsF:String
        get() = pref.getString("lastBannerAdShownTransactionsF","")!!
        set(value) {editor.putString("lastBannerAdShownTransactionsF",value).commit()}

    var lastBannerAdShownMonthlyF:String
        get() = pref.getString("lastBannerAdShownMonthlyF","")!!
        set(value) {editor.putString("lastBannerAdShownMonthlyF",value).commit()}

    var lastBannerAdShownDailyF:String
        get() = pref.getString("lastBannerAdShownDailyF","")!!
        set(value) {editor.putString("lastBannerAdShownDailyF",value).commit()}

    var lastNativeAdShownAIF:String
        get() = pref.getString("lastNativeAdShownAIF","")!!
        set(value) {editor.putString("lastNativeAdShownAIF",value).commit()}

    var lastNativeAdShownAEF:String
        get() = pref.getString("lastNativeAdShownAEF","")!!
        set(value) {editor.putString("lastNativeAdShownAEF",value).commit()}

    var lastInterstitialAdShownFRF:String
        get() = pref.getString("lastInterstitialAdShownFRF","")!!
        set(value) {editor.putString("lastInterstitialAdShownFRF",value).commit()}

    var lastInterstitialAdShownHome:String
        get() = pref.getString("lastInterstitialAdShownHome","")!!
        set(value) {editor.putString("lastInterstitialAdShownHome",value).commit()}

    var lastRewardedAdHomeShownTime:Long
        get() = pref.getLong("lastRewardedAdHomeShownTime", 1640973600000)// 1st January 2022
        set(value) {editor.putLong("lastRewardedAdHomeShownTime",value).commit()}

    var lastRewardedAdASubscriptionShownTime:Long
        get() = pref.getLong("lastRewardedAdSubscriptionShownTime", 1640973600000)// 1st January 2022
        set(value) {editor.putLong("lastRewardedAdSubscriptionShownTime",value).commit()}



    //Sync
    var lastBannerSyncTime:Long
        get() = pref.getLong("lastBannerSyncTime", 1640973600000)// 1st January 2022
        set(value) {editor.putLong("lastBannerSyncTime",value).commit()}





    var isPeriodicSyncLaunched:Boolean
        get() = pref.getBoolean("isPeriodicSyncLaunched",false)
        set(value) {editor.putBoolean("isPeriodicSyncLaunched", value).commit()}


    //user stts

    var isLoggedIn:Boolean
        get() = pref.getBoolean("isLoggedIn",false)
        set(value) {editor.putBoolean("isLoggedIn", value).commit()}

    var isPremiumUser:Boolean
        get() = pref.getBoolean("isPremiumUser",false)
        set(value) {editor.putBoolean("isPremiumUser", value).commit()}

    var isAdRemoved:Boolean
        get() = pref.getBoolean("isAdRemoved",false)
        set(value) {editor.putBoolean("isAdRemoved", value).commit()}

    var adRemoveExpireTime:Long
        get() = pref.getLong("adRemoveExpireTime", 1640973600000)// 1st January 2022
        set(value) {editor.putLong("adRemoveExpireTime",value).commit()}



    var isSubscriptionActiveInGooglePlayAccount:Boolean
        get() = pref.getBoolean("isSubscriptionActiveInGooglePlayAccount",false)
        set(value) {editor.putBoolean("isSubscriptionActiveInGooglePlayAccount", value).commit()}

    var subscriptionProductID:String
        get() = pref.getString("subscriptionProductID","")!!
        set(value) {editor.putString("subscriptionProductID",value).commit()}


    var name:String
        get() = pref.getString("name","")!!
        set(value) {editor.putString("name",value).commit()}

    var phone:String
        get() = pref.getString("phone","")!!
        set(value) {editor.putString("phone",value).commit()}

    var email:String
        get() = pref.getString("email","")!!
        set(value) {editor.putString("email",value).commit()}

    var UID:String
        get() = pref.getString("key","")!!
        set(value) {editor.putString("key",value).commit()}


    //Subscription
    var subscriptionExpiryDateByAd:Long
        get() = pref.getLong("subscriptionExpiryDateByAd", 1640973600000)// 1st January 2022
        set(value) {editor.putLong("subscriptionExpiryDateByAd",value).commit()}

    //Common
    var lastCommonRemoteConfigDataFetchTime:Long
        get() = pref.getLong("lastCommonRemoteConfigDataFetchTime", 1640973600000)// 1st January 2022
        set(value) {editor.putLong("lastCommonRemoteConfigDataFetchTime",value).commit()}

    var lastAppVersionRemoteConfigDataFetchTime:Long
        get() = pref.getLong("lastAppVersionRemoteConfigDataFetchTime", 1640973600000)// 1st January 2022
        set(value) {editor.putLong("lastAppVersionRemoteConfigDataFetchTime",value).commit()}

    var lastBackupTime:Long
        get() = pref.getLong("last_backup_time", 0)// 1st January 2022
        set(value) {editor.putLong("last_backup_time",value).commit()}


    var isDatabaseRestored:Boolean
        get() = pref.getBoolean("is_database_restored",false)
        set(value) {editor.putBoolean("is_database_restored", value).commit()}

    var isAutoLocalBackUpEnabled:Boolean
        get() = pref.getBoolean("is_auto_local_backup_enabled",false)
        set(value) {editor.putBoolean("is_auto_local_backup_enabled", value).commit()}

    var isAutoGDriveBackUpEnabled:Boolean
        get() = pref.getBoolean("is_auto_gdrive_backup_enabled",false)
        set(value) {editor.putBoolean("is_auto_gdrive_backup_enabled", value).commit()}

    var driveFileID:String
        get() = pref.getString("drive_file_id","")!!
        set(value) {editor.putString("drive_file_id",value).commit()}



}