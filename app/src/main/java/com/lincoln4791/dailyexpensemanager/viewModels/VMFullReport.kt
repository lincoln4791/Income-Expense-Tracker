package com.lincoln4791.dailyexpensemanager.viewModels

import androidx.lifecycle.LiveData
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.model.MC_MonthlyReport
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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
        viewModelScope.launch {
            _postsList.postValue(repository.loadYearMonthDayTypeCategoryWise(year,month,day,type,category))
        }
    }


    fun loadYearMonthTypeCategoryWise(year:String,month:String,type:String,category:String){
        _postsList.value = Resource.Loading
        viewModelScope.launch {
            _postsList.postValue(repository.loadYearMonthTypeCategoryWise(year, month, type, category))
        }
    }


    fun loadYearMonthDayTypeWise(year: String, month: String, day: String, type: String) {
        _postsList.value = Resource.Loading
        viewModelScope.launch {
            _postsList.postValue(repository.loadYearMonthDayTypeWise(year, month, day, type))
        }

    }


    fun loadYearMonthDayCategoryWise(year: String, month: String, day: String, category: String) {
        _postsList.value = Resource.Loading
        viewModelScope.launch {
            _postsList.postValue(repository.loadYearMonthDayCategoryWise(year, month, day, category))
        }

    }

    fun loadYearMonthDayWise(year: String, month: String, day: String) {
        _postsList.value = Resource.Loading
        viewModelScope.launch {
            _postsList.postValue(repository.loadYearMonthDayWise(year, month, day))
        }
    }


    fun loadYearMonthTypeWise(year: String, month: String, type: String) {
        _postsList.value = Resource.Loading
        viewModelScope.launch {
            _postsList.postValue(repository.loadYearMonthTypeWise(year, month, type))
        }
    }


    fun loadYearMonthCategoryWise(year: String, month: String, category: String) {
        _postsList.value = Resource.Loading
        viewModelScope.launch {
            _postsList.postValue(repository.loadYearMonthCategoryWise(year, month, category))
        }
    }


    fun loadYearTypeWise(year: String, type: String) {
        _postsList.value = Resource.Loading
        viewModelScope.launch {
            _postsList.postValue(repository.loadYearTypeWise(year, type))
        }
    }

    fun loadYearTypeCategoryWise(year: String, type: String, category: String) {
        _postsList.value = Resource.Loading
        viewModelScope.launch {
            _postsList.postValue(repository.loadYearTypeCategoryWise(year, type, category))
        }
    }


    fun loadYearMonthWise(year: String, month: String) {
        _postsList.value = Resource.Loading
        viewModelScope.launch {
            _postsList.postValue(repository.loadYearMonthWise(year, month))
        }
    }

    fun loadYearWise(year:String){
        _postsList.value = Resource.Loading
            viewModelScope.launch {
                _postsList.postValue(repository.loadYearWise(year))
            }
    }


    fun getAllCardsByTypeArrayString(type: String) {
        _categoryCards.value = Resource.Loading
        viewModelScope.launch {
            _categoryCards.postValue(repository.getAllCardsByTypeArrayString(type))
        }
    }


    //Group Wise
    fun loadYearMonthExpenseWiseByGroup(year: String, month: String, type: String) {
        _expenseList.value = Resource.Loading
        viewModelScope.launch {
            _expenseList.postValue(repository.loadYearMonthTypeWiseByGroup(year, month, type))
        }
    }

    fun loadYearMonthIncomeWiseByGroup(year:String, month:String, type : String){
        _incomeList.value = Resource.Loading
            viewModelScope.launch {
                _incomeList.postValue(repository.loadYearMonthTypeWiseByGroup(year,month,type))
            }

    }

    fun loadYearExpenseWiseByGroup(year: String, type: String) {
        _expenseList.value = Resource.Loading
        viewModelScope.launch {
            _expenseList.postValue(repository.loadYearTypeWiseByGroup(year, type))
        }
    }

    fun loadYearIncomeWiseByGroup(year:String,type : String){
        _incomeList.value = Resource.Loading
            viewModelScope.launch {
                _incomeList.postValue(repository.loadYearTypeWiseByGroup(year,type))
            }
    }


    fun loadYearMonthIncomeTotal(year: String, month: String, type: String) {
        _totalIncome.value = Resource.Loading
        viewModelScope.launch {
            _totalIncome.postValue(repository.loadYearMonthTypeTotal(year, month, type))
        }
    }


    fun loadYearMonthExpenseTotal(year: String, month: String, type: String) {
        _totalExpense.value = Resource.Loading
        viewModelScope.launch {
            _totalExpense.postValue(repository.loadYearMonthTypeTotal(year, month, type))
        }
    }

    fun loadYearMonthBalance(year: String, month: String) {
        balance.value = Resource.Loading
        viewModelScope.launch {
            balance.postValue(repository.loadYearMonthBalance(year, month))
        }
    }



}