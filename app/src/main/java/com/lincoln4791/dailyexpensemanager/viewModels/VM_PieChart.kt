package com.lincoln4791.dailyexpensemanager.viewModels

import android.app.Application
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import com.lincoln4791.dailyexpensemanager.roomDB.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class VM_PieChart(application: Application) : AndroidViewModel(application) {

    val repository : Repository = Repository(AppDatabase.getInstance(application.applicationContext).dbDao())
    var postsList: MutableLiveData<Resource<List<MC_Posts>>> = MutableLiveData<Resource<List<MC_Posts>>>()

    fun loadYearMonth(year:String,month:String){
        /*   CoroutineScope(Dispatchers.IO).launch {*/
        //postsList.value = repository.loadAllTransactions()
        postsList.value = Resource.Loading
        try {

            CoroutineScope(Dispatchers.IO).launch {
      /*          repository.loadYearMonthWise(year,month) {
                    android.os.Handler(Looper.getMainLooper()).post{
                        postsList.value = it
                    }
                }*/
            }
        }
        catch (e: Exception){
            //postsList.value = Resource.Failure("Failed to retrive data -> ${e.message}")
        }
        // }
    }


}