package com.lincoln4791.dailyexpensemanager.viewModels

import android.app.Application
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import androidx.lifecycle.MutableLiveData
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.common.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class VM_Transactions( application: Application) : AndroidViewModel(application) {
    private var repository : Repository = Repository(application.applicationContext)
    var postsList: MutableLiveData<Resource<List<MC_Posts>>> = MutableLiveData<Resource<List<MC_Posts>>>()
    var totalIncome : MutableLiveData<Int> = MutableLiveData(0)
    var totalExpense : MutableLiveData<Int> = MutableLiveData(0)
    var mutable_postsList = MutableLiveData<List<MC_Posts>>()

     fun  loadAllTransactions(){
     /*   CoroutineScope(Dispatchers.IO).launch {*/
            //postsList.value = repository.loadAllTransactions()
            postsList.value = Resource.Loading()
        try {
            CoroutineScope(Dispatchers.IO).launch {
                repository.getTransactions {
                    android.os.Handler(Looper.getMainLooper()).post{
                        postsList.value = it
                    }
                }
            }

        }
            catch (e:Exception){
                postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
            }
       // }
    }

    fun loadAllIncomes(){
        /*   CoroutineScope(Dispatchers.IO).launch {*/
        //postsList.value = repository.loadAllTransactions()
        postsList.value = Resource.Loading()
        try {
           CoroutineScope(Dispatchers.IO).launch {
               repository.getIncomes {
                   android.os.Handler(Looper.getMainLooper()).post{
                       postsList.value = it
                   }
               }
           }
        }
        catch (e:Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
        // }
    }


    fun loadAllExpenses(){
        /*   CoroutineScope(Dispatchers.IO).launch {*/
        //postsList.value = repository.loadAllTransactions()
        try {
            repository.getExpenses {
                android.os.Handler(Looper.getMainLooper()).post{
                    postsList.value = it
                }
            }
        }
        catch (e:Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
        // }
    }


     fun fetchAllTransactions(posts: List<MC_Posts>) {
        totalIncome.value = 0
        totalExpense.value =0

        for (i in posts.indices) {
            val ID = posts[i].id
            val postDescription = posts[i].postDescription
            val postCategory = posts[i].postCategory
            val postType = posts[i].postType
            val postAmount = posts[i].postAmount
            val postTime = posts[i].postTime
            val postDay = posts[i].postDay
            val postMonth = posts[i].postMonth
            val postYear = posts[i].postYear
            val postDateTime = posts[i].postDateTime
            val timeStamp = posts[i].timeStamp
            val post = MC_Posts(ID,
                postDescription,
                postCategory,
                postType,
                postAmount,
                postYear,
                postMonth,
                postDay,
                postTime,
                timeStamp,
                postDateTime)
            //vm_transactions!!.postsList.add(post)
            if ((postType == Constants.TYPE_INCOME)) {
                totalIncome.value = totalIncome.value!! + postAmount.toInt()
            } else if ((postType == Constants.TYPE_EXPENSE)) {
                totalExpense.value = totalExpense.value!! + postAmount.toInt()
            }
        }
    }



}