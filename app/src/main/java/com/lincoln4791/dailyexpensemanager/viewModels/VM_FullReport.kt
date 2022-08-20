package com.lincoln4791.dailyexpensemanager.viewModels

import android.app.Application
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.crashlytics.internal.common.CrashlyticsReportWithSessionId
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.model.MC_MonthlyReport
import com.lincoln4791.dailyexpensemanager.roomDB.AppDatabase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class VM_FullReport(val repository: Repository) : ViewModel() {

    var expenseList: MutableLiveData<Resource<List<MC_MonthlyReport>>> = MutableLiveData<Resource<List<MC_MonthlyReport>>>()
    var incomeList: MutableLiveData<Resource<List<MC_MonthlyReport>>> = MutableLiveData<Resource<List<MC_MonthlyReport>>>()
    var postsList: MutableLiveData<Resource<List<MC_Posts>>> = MutableLiveData<Resource<List<MC_Posts>>>()
    var categoryCards: MutableLiveData<Resource<Array<String>>> = MutableLiveData<Resource<Array<String>>>()
    var totalIncome: MutableLiveData<Resource<Int>> = MutableLiveData<Resource<Int>>()
    var totalExpense: MutableLiveData<Resource<Int>> = MutableLiveData<Resource<Int>>()
    var balance: MutableLiveData<Resource<Int>> = MutableLiveData<Resource<Int>>()
    var year = Constants.YEAR_DEFAULT
    var month = Constants.MONTH_All
    var day = Constants.DAY_ALL
    var type = Constants.TYPE_ALL
    var category = Constants.CATEGORY_All
    var income = 0

    fun loadYearMonthDayTypeCategoryWise(year:String,month:String,day:String,type:String,category:String){
        postsList.value = Resource.Loading()
        try {
            CoroutineScope(Dispatchers.IO).launch {
                repository.loadYearMonthDayTypeCategoryWise(year,month,day,type,category) {
                    android.os.Handler(Looper.getMainLooper()).post{
                        postsList.value = it
                    }
                }
            }

        }
        catch (e: Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }


    fun loadYearMonthTypeCategoryWise(year:String,month:String,type:String,category:String){
        postsList.value = Resource.Loading()
        try {
            CoroutineScope(Dispatchers.IO).launch {
                repository.loadYearMonthTypeCategoryWise(year,month,type,category) {
                    android.os.Handler(Looper.getMainLooper()).post{
                        postsList.value = it
                    }
                }
            }

        }
        catch (e: Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }


    fun loadYearMonthDayTypeWise(year:String,month:String,day:String,type:String){
        postsList.value = Resource.Loading()
        try {
            CoroutineScope(Dispatchers.IO).launch {
                repository.loadYearMonthDayTypeWise(year,month,day,type) {
                    android.os.Handler(Looper.getMainLooper()).post{
                        postsList.value = it
                    }
                }
            }

        }
        catch (e: Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }


    fun loadYearMonthDayCategoryWise(year:String,month:String,day:String,category:String){
        postsList.value = Resource.Loading()
        try {
            CoroutineScope(Dispatchers.IO).launch {
                repository.loadYearMonthDayCategoryWise(year,month,day,category) {
                    android.os.Handler(Looper.getMainLooper()).post{
                        postsList.value = it
                    }
                }
            }

        }
        catch (e: Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }

    fun loadYearMonthDayWise(year:String,month:String,day:String){
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
    }


    fun loadYearMonthTypeWise(year:String,month:String,type:String){
        postsList.value = Resource.Loading()
        try {
            CoroutineScope(Dispatchers.IO).launch {
                repository.loadYearMonthTypeWise(year,month,type) {
                    android.os.Handler(Looper.getMainLooper()).post{
                        postsList.value = it
                    }
                }
            }

        }
        catch (e: Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }


    fun loadYearMonthCategoryWise(year:String,month:String,category:String){
        postsList.value = Resource.Loading()
        try {
            CoroutineScope(Dispatchers.IO).launch {
                repository.loadYearMonthCategoryWise(year,month,category) {
                    android.os.Handler(Looper.getMainLooper()).post{
                        postsList.value = it
                    }
                }
            }

        }
        catch (e: Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }


    fun loadYearMonthCategoryWise(year:String,month:String){
        postsList.value = Resource.Loading()
        try {
            CoroutineScope(Dispatchers.IO).launch {
                repository.loadYearMonthWise(year,month) {
                    android.os.Handler(Looper.getMainLooper()).post{
                        postsList.value = it
                    }
                }
            }

        }
        catch (e: Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }

    fun loadYearTypeWise(year:String,type:String){
        postsList.value = Resource.Loading()
        try {
            CoroutineScope(Dispatchers.IO).launch {
                repository.loadYearTypeWise(year,type) {
                    android.os.Handler(Looper.getMainLooper()).post{
                        postsList.value = it
                    }
                }
            }

        }
        catch (e: Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }

    fun loadYearTypeCategoryWise(year:String,type:String,category:String){
        postsList.value = Resource.Loading()
        try {
            CoroutineScope(Dispatchers.IO).launch {
                repository.loadYearTypeCategoryWise(year,type, category) {
                    android.os.Handler(Looper.getMainLooper()).post{
                        postsList.value = it
                    }
                }
            }

        }
        catch (e: Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }


    fun loadYearMonthWise(year:String,month:String){
        postsList.value = Resource.Loading()
        try {
            CoroutineScope(Dispatchers.IO).launch {
                repository.loadYearMonthWise(year,month) {
                    android.os.Handler(Looper.getMainLooper()).post{
                        postsList.value = it
                    }
                }
            }

        }
        catch (e: Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }

    fun loadYearWise(year:String){
        postsList.value = Resource.Loading()
        try {
            CoroutineScope(Dispatchers.IO).launch {
                repository.loadYearWise(year) {
                    android.os.Handler(Looper.getMainLooper()).post{
                        postsList.value = it
                    }
                }
            }

        }
        catch (e: Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }


    fun getAllCardsByTypeArrayString(type : String){
        categoryCards.value = Resource.Loading()
        try {
            CoroutineScope(Dispatchers.IO).launch {
                repository.getAllCardsByTypeArrayString(type) {
                    android.os.Handler(Looper.getMainLooper()).post{
                        categoryCards.value = it
                    }
                }
            }

        }
        catch (e: Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }


    //Group Wise
    fun loadYearMonthExpeneWiseByGroup(year:String, month:String, type : String){
        /*   CoroutineScope(Dispatchers.IO).launch {*/
        //postsList.value = repository.loadAllTransactions()
        expenseList.value = Resource.Loading()
        try {
            CoroutineScope(Dispatchers.IO).launch {
                repository.loadYearMonthTypeWiseByGroup(year,month,type) {
                    android.os.Handler(Looper.getMainLooper()).post{
                        expenseList.value = it
                    }
                }
            }

        }
        catch (e: Exception){
            expenseList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
        // }
    }

    fun loadYearMonthIncomeWiseByGroup(year:String, month:String, type : String){
        /*   CoroutineScope(Dispatchers.IO).launch {*/
        //postsList.value = repository.loadAllTransactions()
        incomeList.value = Resource.Loading()
        try {
            CoroutineScope(Dispatchers.IO).launch {
                repository.loadYearMonthTypeWiseByGroup(year,month,type) {
                    android.os.Handler(Looper.getMainLooper()).post{
                        incomeList.value = it
                    }
                }
            }

        }
        catch (e: Exception){
            incomeList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
        // }
    }

    fun loadYearExpeneWiseByGroup(year:String, type : String){
        /*   CoroutineScope(Dispatchers.IO).launch {*/
        //postsList.value = repository.loadAllTransactions()
        expenseList.value = Resource.Loading()
        try {
            CoroutineScope(Dispatchers.IO).launch {
                repository.loadYearTypeWiseByGroup(year,type) {
                    android.os.Handler(Looper.getMainLooper()).post{
                        expenseList.value = it
                    }
                }
            }

        }
        catch (e: Exception){
            expenseList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
        // }
    }

    fun loadYearIncomeWiseByGroup(year:String,type : String){
        /*   CoroutineScope(Dispatchers.IO).launch {*/
        //postsList.value = repository.loadAllTransactions()
        incomeList.value = Resource.Loading()
        try {
            CoroutineScope(Dispatchers.IO).launch {
                repository.loadYearTypeWiseByGroup(year,type) {
                    android.os.Handler(Looper.getMainLooper()).post{
                        incomeList.value = it
                    }
                }
            }

        }
        catch (e: Exception){
            incomeList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
        // }
    }


    fun loadYearMonthIncomeTotal(year:String,month: String,type : String){
        totalIncome.value = Resource.Loading()
        try {
            CoroutineScope(Dispatchers.IO).launch {
                repository.loadYearMonthTypeTotal(year,month,type) {
                    android.os.Handler(Looper.getMainLooper()).post{
                        totalIncome.value = it
                    }
                }
            }

        }
        catch (e: Exception){
            totalIncome.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }


    fun loadYearMonthExpenseTotal(year:String,month: String,type : String){
        totalExpense.value = Resource.Loading()
        try {
            CoroutineScope(Dispatchers.IO).launch {
                repository.loadYearMonthTypeTotal(year,month,type) {
                    android.os.Handler(Looper.getMainLooper()).post{
                        totalExpense.value = it
                    }
                }
            }

        }
        catch (e: Exception){
            totalExpense.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }

    fun loadYearMonthBalance(year:String,month: String){
        balance.value = Resource.Loading()
        try {
            CoroutineScope(Dispatchers.IO).launch {
                repository.loadYearMonthBalance(year,month) {
                    android.os.Handler(Looper.getMainLooper()).post{
                        balance.value = it
                    }
                }
            }

        }
        catch (e: Exception){
            balance.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }



}