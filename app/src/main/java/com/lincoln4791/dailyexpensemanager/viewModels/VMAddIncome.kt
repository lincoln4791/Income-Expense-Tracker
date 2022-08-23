package com.lincoln4791.dailyexpensemanager.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.common.util.GlobalVariabls
import com.lincoln4791.dailyexpensemanager.model.MC_Cards
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class VMAddIncome @Inject constructor (val repository: Repository) : ViewModel() {

    private var _postsList: MutableLiveData<Resource<List<MC_Cards>>> = MutableLiveData<Resource<List<MC_Cards>>>()
    val postsList: LiveData<Resource<List<MC_Cards>>>
    get() = _postsList

    private var _flagInsertIncome : MutableLiveData<Boolean> = MutableLiveData()
    val flagInsertIncome : LiveData<Boolean>
        get()=_flagInsertIncome

    var category = ""

    var amount = ""

    var dateTime : MutableLiveData<String> = MutableLiveData()


    var time: String? = null
    var day: String? = null
    var month: String? = null
    var year: String? = null
    var mutableCategory = MutableLiveData<String>()
    var mutableAmount = MutableLiveData<String>()

    fun loadAllCards(){
        _postsList.value = Resource.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _postsList.postValue(repository.getAllIncomeCards())
        }
    }

    fun setDateTime() {
        val simpleDateTimeFormat = SimpleDateFormat("dd-MM-yyyy  hh:mm a", Locale.getDefault())
        val simpleDayFormat = SimpleDateFormat("dd", Locale.getDefault())
        val simpleMonthFormat = SimpleDateFormat("MM", Locale.getDefault())
        val simpleYearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        val simpleTimeFormat = SimpleDateFormat("hh-mm a", Locale.getDefault())
        day = simpleDayFormat.format(System.currentTimeMillis())
        month = simpleMonthFormat.format(System.currentTimeMillis())
        year = simpleYearFormat.format(System.currentTimeMillis())
        time = simpleTimeFormat.format(System.currentTimeMillis())
        dateTime.value = simpleDateTimeFormat.format(System.currentTimeMillis())
        Log.d("time","time is -> ${simpleDateTimeFormat.format(System.currentTimeMillis())}")
    }


    fun addIncome(post : MC_Posts){
        viewModelScope.launch {
            try {
                repository.insertPost(post)
                _flagInsertIncome.value=true
            } catch (e:Exception){
                _flagInsertIncome.value=false
                e.printStackTrace()
            }


        }
    }

    fun adjustCurrentBalance(amount:Int){
        GlobalVariabls.currentBalance = GlobalVariabls.currentBalance + amount.toInt()
    }

}