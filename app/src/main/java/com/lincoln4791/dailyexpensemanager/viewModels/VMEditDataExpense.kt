package com.lincoln4791.dailyexpensemanager.viewModels

import androidx.lifecycle.ViewModel


class VMEditDataExpense : ViewModel() {
    var category = ""
    var amount = ""
    var dateTime = ""
    var time: String? = null
    var day: String? = null
    var month: String? = null
    var year: String? = null
}