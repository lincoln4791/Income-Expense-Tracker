package com.lincoln4791.dailyexpensemanager.viewModels

import android.os.Looper
import androidx.lifecycle.LiveData
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.model.MC_MonthlyReport
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class VMFullReport @Inject constructor (val repository: Repository) : ViewModel() {

    private var _expenseList: MutableLiveData<Resource<List<MC_MonthlyReport>>> = MutableLiveData<Resource<List<MC_MonthlyReport>>>()
    val expenseList: LiveData<Resource<List<MC_MonthlyReport>>>
        get() = _expenseList

    private var _incomeList: MutableLiveData<Resource<List<MC_MonthlyReport>>> = MutableLiveData<Resource<List<MC_MonthlyReport>>>()
    val incomeList: LiveData<Resource<List<MC_MonthlyReport>>>
        get() = _incomeList

    private var _postsList: MutableLiveData<Resource<List<MC_Posts>>> = MutableLiveData<Resource<List<MC_Posts>>>()
    val postsList: LiveData<Resource<List<MC_Posts>>>
        get() = _postsList

    private var _categoryCards: MutableLiveData<Resource<Array<String>>> = MutableLiveData<Resource<Array<String>>>()
    val categoryCards: LiveData<Resource<Array<String>>>
        get() = _categoryCards

    private var _totalIncome: MutableLiveData<Resource<Int>> = MutableLiveData<Resource<Int>>()
    val totalIncome: LiveData<Resource<Int>>
        get() = _totalIncome

    private var _totalExpense: MutableLiveData<Resource<Int>> = MutableLiveData<Resource<Int>>()
    val totalExpense: LiveData<Resource<Int>>
        get() = _totalIncome


    private var balance: MutableLiveData<Resource<Int>> = MutableLiveData<Resource<Int>>()
    var year = Constants.YEAR_DEFAULT
    var month = Constants.MONTH_All
    var day = Constants.DAY_ALL
    var type = Constants.TYPE_ALL
    var category = Constants.CATEGORY_All
    var income = 0

    fun loadYearMonthDayTypeCategoryWise(year:String,month:String,day:String,type:String,category:String){
        _postsList.value = Resource.Loading
        try {
            CoroutineScope(Dispatchers.IO).launch {
                _postsList.value = repository.loadYearMonthDayTypeCategoryWise(year,month,day,type,category)
            }

        }
        catch (e: Exception){
            //postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }


    fun loadYearMonthTypeCategoryWise(year:String,month:String,type:String,category:String){
        _postsList.value = Resource.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _postsList.value = repository.loadYearMonthTypeCategoryWise(year, month, type, category)
        }
    }


    fun loadYearMonthDayTypeWise(year: String, month: String, day: String, type: String) {
        _postsList.value = Resource.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _postsList.value = repository.loadYearMonthDayTypeWise(year, month, day, type)
        }

    }


    fun loadYearMonthDayCategoryWise(year: String, month: String, day: String, category: String) {
        _postsList.value = Resource.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _postsList.value = repository.loadYearMonthDayCategoryWise(year, month, day, category)
        }

    }

    fun loadYearMonthDayWise(year: String, month: String, day: String) {
        _postsList.value = Resource.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _postsList.value = repository.loadYearMonthDayWise(year, month, day)
        }
    }


    fun loadYearMonthTypeWise(year: String, month: String, type: String) {
        _postsList.value = Resource.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _postsList.value = repository.loadYearMonthTypeWise(year, month, type)
        }
    }


    fun loadYearMonthCategoryWise(year: String, month: String, category: String) {
        _postsList.value = Resource.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _postsList.value = repository.loadYearMonthCategoryWise(year, month, category)
        }
    }


    fun loadYearMonthCategoryWise(year:String,month:String){
        _postsList.value = Resource.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _postsList.value = repository.loadYearMonthWise(year, month)
        }
    }

    fun loadYearTypeWise(year: String, type: String) {
        _postsList.value = Resource.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _postsList.value = repository.loadYearTypeWise(year, type)
        }
    }

    fun loadYearTypeCategoryWise(year: String, type: String, category: String) {
        _postsList.value = Resource.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _postsList.value = repository.loadYearTypeCategoryWise(year, type, category)
        }
    }


    fun loadYearMonthWise(year: String, month: String) {
        _postsList.value = Resource.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _postsList.value = repository.loadYearMonthWise(year, month)
        }
    }

    fun loadYearWise(year:String){
        _postsList.value = Resource.Loading
            CoroutineScope(Dispatchers.IO).launch {
                _postsList.value=repository.loadYearWise(year)
            }
    }


    fun getAllCardsByTypeArrayString(type: String) {
        _categoryCards.value = Resource.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _categoryCards.value = repository.getAllCardsByTypeArrayString(type)
        }
    }


    //Group Wise
    fun loadYearMonthExpeneWiseByGroup(year: String, month: String, type: String) {
        _expenseList.value = Resource.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _expenseList.value = repository.loadYearMonthTypeWiseByGroup(year, month, type)
        }
    }

    fun loadYearMonthIncomeWiseByGroup(year:String, month:String, type : String){
        _incomeList.value = Resource.Loading
            CoroutineScope(Dispatchers.IO).launch {
                _incomeList.value= repository.loadYearMonthTypeWiseByGroup(year,month,type)
            }

    }

    fun loadYearExpeneWiseByGroup(year: String, type: String) {
        _expenseList.value = Resource.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _expenseList.value = repository.loadYearTypeWiseByGroup(year, type)
        }
    }

    fun loadYearIncomeWiseByGroup(year:String,type : String){
        _incomeList.value = Resource.Loading
            CoroutineScope(Dispatchers.IO).launch {
                _incomeList.value=repository.loadYearTypeWiseByGroup(year,type)
            }
    }


    fun loadYearMonthIncomeTotal(year: String, month: String, type: String) {
        _totalIncome.value = Resource.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _totalIncome.value = repository.loadYearMonthTypeTotal(year, month, type)
        }
    }


    fun loadYearMonthExpenseTotal(year: String, month: String, type: String) {
        _totalExpense.value = Resource.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _totalExpense.value = repository.loadYearMonthTypeTotal(year, month, type)
        }
    }

    fun loadYearMonthBalance(year:String,month: String){
        balance.value = Resource.Loading
        try {
            CoroutineScope(Dispatchers.IO).launch {
                balance.value= repository.loadYearMonthBalance(year,month)
            }

        }
        catch (e: Exception){
            //balance.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }



}