package com.lincoln4791.dailyexpensemanager.viewModels

import android.os.Looper
import androidx.lifecycle.LiveData
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.common.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class VMTransactions @Inject constructor (
    val repository: Repository
    ) : ViewModel() {


    private var _postsList: MutableLiveData<Resource<List<MC_Posts>>> = MutableLiveData<Resource<List<MC_Posts>>>()
    val postsList: LiveData<Resource<List<MC_Posts>>>
    get() = _postsList

    private var _totalIncome : MutableLiveData<Int> = MutableLiveData(0)
    val totalIncome : LiveData<Int>
    get()=_totalIncome


    var _totalExpense : MutableLiveData<Int> = MutableLiveData(0)
    val totalExpense : LiveData<Int>
    get() = _totalExpense



     fun  loadAllTransactions() = viewModelScope.launch {
         _postsList.value = Resource.Loading
         _postsList.value = repository.getTransactions()
     }


    fun loadAllIncomes()=viewModelScope.launch {
        _postsList.value = Resource.Loading
        _postsList.value = repository.getIncomes()
    }


    fun loadAllExpenses() = viewModelScope.launch {
        _postsList.value = Resource.Loading
        _postsList.value = repository.getExpenses()
    }


     fun fetchAllTransactions(posts: List<MC_Posts>) {
        _totalIncome.value = 0
        _totalExpense.value =0

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
                _totalIncome.value = _totalIncome.value!! + postAmount.toInt()
            } else if ((postType == Constants.TYPE_EXPENSE)) {
                _totalExpense.value = _totalExpense.value!! + postAmount.toInt()
            }
        }
    }



}