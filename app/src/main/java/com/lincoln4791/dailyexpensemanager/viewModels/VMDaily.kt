package com.lincoln4791.dailyexpensemanager.viewModels
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@HiltViewModel
class VMDaily @Inject constructor (val repository: Repository) : ViewModel() {
    private var _postsList: MutableLiveData<Resource<List<MC_Posts>>> = MutableLiveData<Resource<List<MC_Posts>>>()
    val postsList: LiveData<Resource<List<MC_Posts>>>
    get() = _postsList

    var day : String? = null
    var month : String? = null
    var year : String? = null
    var totalIncome = 0
    var totalExpense = 0

    var date : MutableLiveData<String> = MutableLiveData()

    fun loadDailyTransactions(year:String,month:String,day:String){
        _postsList.value = Resource.Loading
            viewModelScope.launch {
                _postsList.postValue(repository.loadYearMonthDayWise(year,month,day))
            }
    }

    fun setDate() {
        val simpleDayFormat = SimpleDateFormat("dd", Locale.getDefault())
        val simpleMonthFormat = SimpleDateFormat("MM", Locale.getDefault())
        val simpleYearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        day = simpleDayFormat.format(System.currentTimeMillis())
        month = simpleMonthFormat.format(System.currentTimeMillis())
        year = simpleYearFormat.format(System.currentTimeMillis())
        date.value = "$day-$month-$year"
    }

    fun calculateAll(postList:List<MC_Posts>) {
        totalIncome = 0
        totalExpense = 0
        Log.d("tag", "list size : " + postList.size)
        for (i in postList.indices) {
            if (postList[i].postType == Constants.TYPE_INCOME) {
                totalIncome += postList[i].postAmount
            } else if (postList[i].postType == Constants.TYPE_EXPENSE) {
                totalExpense += postList[i].postAmount
            }
        }
    }

}