package com.lincoln4791.dailyexpensemanager.viewModels

import android.app.Application
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import com.lincoln4791.dailyexpensemanager.roomDB.AppDatabase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject


class VM_Daily(val repository: Repository) : ViewModel() {
    var postsList: MutableLiveData<Resource<List<MC_Posts>>> = MutableLiveData<Resource<List<MC_Posts>>>()

    fun loadDailyTransactions(year:String,month:String,day:String){
        /*   CoroutineScope(Dispatchers.IO).launch {*/
        //postsList.value = repository.loadAllTransactions()
        postsList.value = Resource.Loading()
        try {
            CoroutineScope(Dispatchers.IO).launch {
                repository.loadYearMonthDayWise(year,month,day) {
                    android.os.Handler(Looper.getMainLooper()).post{
                        postsList.value = it
                    }
                }
            }

        }
        catch (e: Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
        // }
    }

}