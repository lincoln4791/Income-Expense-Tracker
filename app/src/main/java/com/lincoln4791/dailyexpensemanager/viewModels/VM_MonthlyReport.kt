package com.lincoln4791.dailyexpensemanager.viewModels

import android.app.Application
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.model.MC_MonthlyReport
import java.lang.Exception

class VM_MonthlyReport(application: Application) : AndroidViewModel(application) {

    private var repository : Repository = Repository(application.applicationContext)
    var expenseList: MutableLiveData<Resource<List<MC_MonthlyReport>>> = MutableLiveData<Resource<List<MC_MonthlyReport>>>()
    var incomeList: MutableLiveData<Resource<List<MC_MonthlyReport>>> = MutableLiveData<Resource<List<MC_MonthlyReport>>>()
    var totalIncome: MutableLiveData<Resource<Int>> = MutableLiveData<Resource<Int>>()
    var totalExpense: MutableLiveData<Resource<Int>> = MutableLiveData<Resource<Int>>()
    var monthlyBalance: MutableLiveData<Resource<Int>> = MutableLiveData<Resource<Int>>()

/*    fun loadYearMonth(year:String,month:String){
        *//*   CoroutineScope(Dispatchers.IO).launch {*//*
        //postsList.value = repository.loadAllTransactions()
        postsList.value = Resource.Loading()
        try {
            repository.loadYearMonthWise(year,month) {
                android.os.Handler(Looper.getMainLooper()).post{
                    postsList.value = it
                }
            }
        }
        catch (e: Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
        // }
    }*/


    fun loadYearMonthExpeneWiseByGroup(year:String, month:String, type : String){
        /*   CoroutineScope(Dispatchers.IO).launch {*/
        //postsList.value = repository.loadAllTransactions()
        expenseList.value = Resource.Loading()
        try {
            repository.loadYearMonthTypeWiseByGroup(year,month,type) {
                android.os.Handler(Looper.getMainLooper()).post{
                    expenseList.value = it
                }
            }
        }
        catch (e: Exception){
            expenseList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
        // }
    }

    fun loadYearMonthIncomeWiseByGroup(year:String, month:String,type : String){
        /*   CoroutineScope(Dispatchers.IO).launch {*/
        //postsList.value = repository.loadAllTransactions()
        incomeList.value = Resource.Loading()
        try {
            repository.loadYearMonthTypeWiseByGroup(year,month,type) {
                android.os.Handler(Looper.getMainLooper()).post{
                    incomeList.value = it
                }
            }
        }
        catch (e: Exception){
            incomeList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
        // }
    }

    fun loadYearMonthTypeTotalIncome(year:String, month:String,type : String){
        totalIncome.value = Resource.Loading()
        try {
            repository.loadYearMonthTypeTotal(year,month,type) {
                android.os.Handler(Looper.getMainLooper()).post{
                    totalIncome.value = it
                }
            }
        }
        catch (e: Exception){
            totalIncome.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }


    fun loadYearMonthTypeTotalExpense(year:String, month:String,type : String){
        totalExpense.value = Resource.Loading()
        try {
            repository.loadYearMonthTypeTotal(year,month,type) {
                android.os.Handler(Looper.getMainLooper()).post{
                    totalExpense.value = it
                }
            }
        }
        catch (e: Exception){
            totalExpense.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }

}