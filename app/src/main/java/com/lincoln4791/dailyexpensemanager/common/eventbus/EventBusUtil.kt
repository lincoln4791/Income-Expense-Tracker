package com.lincoln4791.dailyexpensemanager.common.eventbus

import org.greenrobot.eventbus.EventBus


object EventBusUtil {

    fun postEvent(message : String){
        EventBus.getDefault().post(MessageEvent(message))
    }

    fun postStickyEvent(message : String){
        EventBus.getDefault().postSticky(MessageEvent(message))
    }
}