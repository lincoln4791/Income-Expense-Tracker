package com.lincoln4791.dailyexpensemanager.viewModels

import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class VM_MonthlyCategoryWise(val repository:Repository) : ViewModel() {

    var postsList: MutableLiveData<Resource<List<MC_Posts>>> = MutableLiveData<Resource<List<MC_Posts>>>()

    fun loadYearMonthTypeCategoryWise(year:String,month:String,type:String,category:String){
        /*   CoroutineScope(Dispatchers.IO).launch {*/
        //postsList.value = repository.loadAllTransactions()
        postsList.value = Resource.Loading
        try {
            CoroutineScope(Dispatchers.IO).launch {
                repository.loadYearMonthTypeCategoryWise(year,month,type,category) {
                    android.os.Handler(Looper.getMainLooper()).post{
                        postsList.value = it
                    }
                }
            }

        }
        catch (e: Exception){
            //postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
        // }
    }


}