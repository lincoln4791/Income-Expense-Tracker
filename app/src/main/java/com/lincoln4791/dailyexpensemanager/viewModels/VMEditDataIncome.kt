package com.lincoln4791.dailyexpensemanager.viewModels

import androidx.lifecycle.ViewModel


class VMEditDataIncome : ViewModel() {
    var category = ""
    var amount = ""
    var dateTime = ""
    lateinit var time: String
    lateinit var day: String
    lateinit var month: String
    lateinit var year: String
}