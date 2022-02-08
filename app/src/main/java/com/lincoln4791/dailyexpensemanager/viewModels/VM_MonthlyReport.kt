package com.lincoln4791.dailyexpensemanager.viewModels

import android.app.Application
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import java.lang.Exception

class VM_MonthlyReport(application: Application) : AndroidViewModel(application) {

    private var repository : Repository = Repository(application.applicationContext)
    var postsList: MutableLiveData<Resource<List<MC_Posts>>> = MutableLiveData<Resource<List<MC_Posts>>>()

    fun loadYearMonth(year:String,month:String){
        /*   CoroutineScope(Dispatchers.IO).launch {*/
        //postsList.value = repository.loadAllTransactions()
        postsList.value = Resource.Loading()
        try {
            repository.loadYearMonthWise(year,month) {
                android.os.Handler(Looper.getMainLooper()).post{
                    postsList.value = it
                }
            }
        }
        catch (e: Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
        // }
    }


    fun loadYearMonthTypeWiseByGroup(year:String, month:String,type : String){
        /*   CoroutineScope(Dispatchers.IO).launch {*/
        //postsList.value = repository.loadAllTransactions()
        postsList.value = Resource.Loading()
        try {
            repository.loadYearMonthTypeWiseByGroup(year,month,type) {
                android.os.Handler(Looper.getMainLooper()).post{
                    postsList.value = it
                }
            }
        }
        catch (e: Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
        // }
    }


}