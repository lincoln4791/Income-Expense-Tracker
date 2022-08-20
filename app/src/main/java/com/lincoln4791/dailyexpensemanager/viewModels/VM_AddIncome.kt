package com.lincoln4791.dailyexpensemanager.viewModels

import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.model.MC_Cards
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception


class VM_AddIncome(val repository: Repository) : ViewModel() {

    var postsList: MutableLiveData<Resource<List<MC_Cards>>> = MutableLiveData<Resource<List<MC_Cards>>>()

    var category = ""
    var amount = ""
    var dateTime = ""
    var time: String? = null
    var day: String? = null
    var month: String? = null
    var year: String? = null
    var mutable_category = MutableLiveData<String>()
    var mutable_amount = MutableLiveData<String>()

    fun loadAllCards(callback : (isLoaded : Boolean)-> Unit){
        postsList.value = Resource.Loading
        try {
            CoroutineScope(Dispatchers.IO).launch {
                repository.getAllIncomeCards {
                    android.os.Handler(Looper.getMainLooper()).post{
                        postsList.value = it
                        callback(true)
                    }
                }
            }
        }
        catch (e: Exception){
            //postsList.value = Resource.Failure("Failed to retrive data -> ${e.message}")
        }
    }

}