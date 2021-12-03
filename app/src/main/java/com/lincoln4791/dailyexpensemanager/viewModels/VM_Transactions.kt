package com.lincoln4791.dailyexpensemanager.viewModels

import androidx.lifecycle.ViewModel
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import androidx.lifecycle.MutableLiveData
import java.util.ArrayList

class VM_Transactions : ViewModel() {
    lateinit var vm_category: String
    var postsList: MutableList<MC_Posts> = mutableListOf()
    var mutable_postsList = MutableLiveData<List<MC_Posts>>()
}