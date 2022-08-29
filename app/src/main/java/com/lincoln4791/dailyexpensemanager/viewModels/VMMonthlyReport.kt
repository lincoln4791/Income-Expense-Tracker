package com.lincoln4791.dailyexpensemanager.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.model.MC_MonthlyReport
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VMMonthlyReport @Inject constructor (val repository: Repository) : ViewModel() {
    private var _expenseList: MutableLiveData<Resource<List<MC_MonthlyReport>>> = MutableLiveData<Resource<List<MC_MonthlyReport>>>()
    val expenseList: LiveData<Resource<List<MC_MonthlyReport>>>
        get() = _expenseList

    private var _incomeList: MutableLiveData<Resource<List<MC_MonthlyReport>>> = MutableLiveData<Resource<List<MC_MonthlyReport>>>()
    val incomeList: LiveData<Resource<List<MC_MonthlyReport>>>
        get() = _incomeList

    private var _totalIncome: MutableLiveData<Resource<Int>> = MutableLiveData<Resource<Int>>()
    val totalIncome: LiveData<Resource<Int>>
        get() = _totalIncome

    private var _totalExpense: MutableLiveData<Resource<Int>> = MutableLiveData<Resource<Int>>()
    val totalExpense: LiveData<Resource<Int>>
        get() = _totalExpense


    var tIncome = 0.0
    var tExpense = 0.0
    var currentMonthPosition = 0
    var currentMonth = 0
    var currentYear = 0

    suspend fun loadYearMonthExpeneWiseByGroup(year:String, month:String, type : String){
        viewModelScope.launch {
            _expenseList.postValue(repository.loadYearMonthTypeWiseByGroup(year,month,type))
        }

    }

    suspend fun loadYearMonthIncomeWiseByGroup(year:String, month:String,type : String){
        viewModelScope.launch {
            _incomeList.postValue(repository.loadYearMonthTypeWiseByGroup(year,month,type))
        }

    }

   suspend fun loadYearMonthTypeTotalIncome(year:String, month:String,type : String){
       viewModelScope.launch {
           _totalIncome.postValue(repository.loadYearMonthTypeTotal(year,month,type))
       }
    }


   suspend fun loadYearMonthTypeTotalExpense(year:String, month:String,type : String){
       viewModelScope.launch {
           _totalExpense.postValue(repository.loadYearMonthTypeTotal(year,month,type))
       }
    }

}