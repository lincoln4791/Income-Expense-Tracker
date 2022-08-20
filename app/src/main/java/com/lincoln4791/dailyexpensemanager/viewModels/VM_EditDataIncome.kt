package com.lincoln4791.dailyexpensemanager.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData


class VM_EditDataIncome : ViewModel() {
    var category = ""
    var amount = ""
    var dateTime = ""
    lateinit var time: String
    lateinit var day: String
    lateinit var month: String
    lateinit var year: String
    var mutable_category = MutableLiveData<String>()
    var mutable_amount = MutableLiveData<String>()
}