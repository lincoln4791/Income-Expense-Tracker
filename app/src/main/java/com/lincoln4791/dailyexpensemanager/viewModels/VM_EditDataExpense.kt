package com.lincoln4791.dailyexpensemanager.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.AndroidEntryPoint


class VM_EditDataExpense : ViewModel() {
    var category = ""
    var amount = ""
    var dateTime = ""
    var time: String? = null
    var day: String? = null
    var month: String? = null
    var year: String? = null
    var mutable_category = MutableLiveData<String>()
    var mutable_amount = MutableLiveData<String>()
}