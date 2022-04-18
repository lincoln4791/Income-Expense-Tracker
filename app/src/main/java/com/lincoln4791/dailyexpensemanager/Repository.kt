package com.lincoln4791.dailyexpensemanager

import android.content.Context
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.model.MC_Cards
import com.lincoln4791.dailyexpensemanager.model.MC_MonthlyReport
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import com.lincoln4791.dailyexpensemanager.roomDB.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Repository(private val context: Context) {
    private val dao = AppDatabase.getInstance(context.applicationContext).dbDao()

     fun getExpenses(callback:(Resource<List<MC_Posts>>)->Unit){
        var value : Resource<List<MC_Posts>>? = null
        CoroutineScope(Dispatchers.IO).launch {
            value = Resource.Success(dao.loadTypeWise(
                Constants.TYPE_EXPENSE))
            callback(value as Resource.Success<List<MC_Posts>>)
        }
    }

    suspend fun getIncomes(callback:(Resource<List<MC_Posts>>)->Unit){
        var value : Resource<List<MC_Posts>>? = null
        CoroutineScope(Dispatchers.IO).launch {
            value = Resource.Success(dao.loadTypeWise(
                Constants.TYPE_INCOME))
            callback(value as Resource.Success<List<MC_Posts>>)
        }
    }


    suspend fun getTransactions(callback:(Resource<List<MC_Posts>>)->Unit){
        var value : Resource<List<MC_Posts>>? = null
        CoroutineScope(Dispatchers.IO).launch {
            value = Resource.Success(dao.loadAllTransactions())
            callback(value as Resource.Success<List<MC_Posts>>)
        }
    }

    suspend fun loadTypeWise(type:String,callback:(Resource<List<MC_Posts>>)->Unit){
        var value : Resource<List<MC_Posts>>? = null
        CoroutineScope(Dispatchers.IO).launch {
            value = Resource.Success(dao.loadTypeWise(type))
            callback(value as Resource.Success<List<MC_Posts>>)
        }
    }

    suspend fun loadYearWise(year:String,callback:(Resource<List<MC_Posts>>)->Unit){
        var value : Resource<List<MC_Posts>>? = null
        CoroutineScope(Dispatchers.IO).launch {
            value = Resource.Success(dao.loadYearWise(year))
            callback(value as Resource.Success<List<MC_Posts>>)
        }
    }

    suspend fun loadYearTypeWise(year:String,type:String,callback:(Resource<List<MC_Posts>>)->Unit){
        var value : Resource<List<MC_Posts>>? = null
        CoroutineScope(Dispatchers.IO).launch {
            value = Resource.Success(dao.loadYearTypeWise(year,type))
            callback(value as Resource.Success<List<MC_Posts>>)
        }
    }

    suspend fun loadYearCategoryWise(year:String,category:String,callback:(Resource<List<MC_Posts>>)->Unit){
        var value : Resource<List<MC_Posts>>? = null
        CoroutineScope(Dispatchers.IO).launch {
            value = Resource.Success(dao.loadYearCategoryWise(year,category))
            callback(value as Resource.Success<List<MC_Posts>>)
        }
    }

    suspend fun loadYearTypeCategoryWise(year:String,type:String,category:String,callback:(Resource<List<MC_Posts>>)->Unit){
        var value : Resource<List<MC_Posts>>? = null
        CoroutineScope(Dispatchers.IO).launch {
            value = Resource.Success(dao.loadYearTypeCategoryWise(year,type,category))
            callback(value as Resource.Success<List<MC_Posts>>)
        }
    }

    suspend fun loadYearMonthWise(year:String,month:String,callback:(Resource<List<MC_Posts>>)->Unit){
        var value : Resource<List<MC_Posts>>? = null
        CoroutineScope(Dispatchers.IO).launch {
            value = Resource.Success(dao.loadYearMonthWise(year,month))
            callback(value as Resource.Success<List<MC_Posts>>)
        }
    }

    suspend fun loadYearMonthTypeWise(year:String,month:String,type:String,callback:(Resource<List<MC_Posts>>)->Unit){
        var value : Resource<List<MC_Posts>>? = null
        CoroutineScope(Dispatchers.IO).launch {
            value = Resource.Success(dao.loadYearMonthTypeWise(year,month,type))
            callback(value as Resource.Success<List<MC_Posts>>)
        }
    }

    suspend fun loadYearMonthCategoryWise(year:String,month:String,category:String,callback:(Resource<List<MC_Posts>>)->Unit){
        var value : Resource<List<MC_Posts>>? = null
        CoroutineScope(Dispatchers.IO).launch {
            value = Resource.Success(dao.loadYearMonthCategoryWise(year,month,category))
            callback(value as Resource.Success<List<MC_Posts>>)
        }
    }

    suspend fun loadYearMonthTypeCategoryWise(year:String,month:String,type:String,category:String,callback:(Resource<List<MC_Posts>>)->Unit){
        var value : Resource<List<MC_Posts>>? = null
        CoroutineScope(Dispatchers.IO).launch {
            value = Resource.Success(dao.loadYearMonthTypeCategoryWise(year,month,type,category))
            callback(value as Resource.Success<List<MC_Posts>>)
        }
    }

    suspend fun loadYearMonthDayWise(year:String,month:String,day:String,callback:(Resource<List<MC_Posts>>)->Unit){
        var value : Resource<List<MC_Posts>>? = null
        CoroutineScope(Dispatchers.IO).launch {
            value = Resource.Success(dao.loadYearMonthDayWise(year,month,day))
            callback(value as Resource.Success<List<MC_Posts>>)
        }
    }

    suspend fun loadYearMonthDayTypeWise(year:String,month:String,day:String,type:String,callback:(Resource<List<MC_Posts>>)->Unit){
        var value : Resource<List<MC_Posts>>? = null
        CoroutineScope(Dispatchers.IO).launch {
            value = Resource.Success(dao.loadYearMonthDayTypeWise(year,month,day,type))
            callback(value as Resource.Success<List<MC_Posts>>)
        }
    }


    suspend fun loadYearMonthDayCategoryWise(year:String,month:String,day:String,category:String,callback:(Resource<List<MC_Posts>>)->Unit){
        var value : Resource<List<MC_Posts>>? = null
        CoroutineScope(Dispatchers.IO).launch {
            value = Resource.Success(dao.loadYearMonthDayCategoryWise(year,month,day,category))
            callback(value as Resource.Success<List<MC_Posts>>)
        }
    }


    suspend fun loadYearMonthDayTypeCategoryWise(year:String,month:String,day:String,type:String,category:String,callback:(Resource<List<MC_Posts>>)->Unit){
        var value : Resource<List<MC_Posts>>? = null
        CoroutineScope(Dispatchers.IO).launch {
            value = Resource.Success(dao.loadYearMonthDayTypeCategoryWise(year,month,day,type,category))
            callback(value as Resource.Success<List<MC_Posts>>)
        }
    }


    suspend fun loadYearMonthTypeWiseByGroup(year:String, month:String,type:String, callback:(Resource<List<MC_MonthlyReport>>)->Unit){
        var value : Resource<List<MC_MonthlyReport>>? = null
        CoroutineScope(Dispatchers.IO).launch {
            value = Resource.Success(dao.loadYearMonthTypeWiseByGroup(year,month,type))
            callback(value as Resource.Success<List<MC_MonthlyReport>>)
        }
    }


    suspend fun loadYearTypeWiseByGroup(year:String,type:String, callback:(Resource<List<MC_MonthlyReport>>)->Unit){
        var value : Resource<List<MC_MonthlyReport>>? = null
        CoroutineScope(Dispatchers.IO).launch {
            value = Resource.Success(dao.loadYearTypeWiseByGroup(year,type))
            callback(value as Resource.Success<List<MC_MonthlyReport>>)
        }
    }




    // Cards
    suspend fun getAllExpenseCards(callback:(Resource<List<MC_Cards>>)->Unit){
        var value : Resource<List<MC_Cards>>? = null
        CoroutineScope(Dispatchers.IO).launch {
            value = Resource.Success(dao.loadAllExpenseCards(Constants.TYPE_EXPENSE))
            callback(value as Resource.Success<List<MC_Cards>>)
        }
    }

    suspend fun getAllCardsByTypeArrayString(type:String,callback:(Resource<Array<String>>)->Unit){
        var value : Resource<Array<String>>? = null
        CoroutineScope(Dispatchers.IO).launch {
            value = Resource.Success(dao.getAllCardsByTypeAsArrayString(type))
            callback(value as Resource.Success<Array<String>>)
        }
    }

    suspend fun getAllIncomeCards(callback:(Resource<List<MC_Cards>>)->Unit){
        var value : Resource<List<MC_Cards>>? = null
        CoroutineScope(Dispatchers.IO).launch {
            value = Resource.Success(dao.loadAllIncomeCards(Constants.TYPE_INCOME))
            callback(value as Resource.Success<List<MC_Cards>>)
        }
    }


    suspend fun loadYearMonthTypeTotal(year: String,month: String,type: String,callback:(Resource<Int>)->Unit){
        var value : Resource<Int>? = null
        CoroutineScope(Dispatchers.IO).launch {
            value = Resource.Success(dao.loadYearMonthTypeTotal(year,month,type))
            callback(value as Resource.Success<Int>)
        }
    }

    suspend fun loadYearTypeTotal(year: String,type: String,callback:(Resource<Int>)->Unit){
        var value : Resource<Int>? = null
        CoroutineScope(Dispatchers.IO).launch {
            value = Resource.Success(dao.loadYearTypeTotal(year,type))
            callback(value as Resource.Success<Int>)
        }
    }


    suspend fun loadYearMonthBalance(year: String,month: String,callback:(Resource<Int>)->Unit){
        var value : Resource<Int>? = null
        CoroutineScope(Dispatchers.IO).launch {
            value = Resource.Success(dao.loadYearMonthBalance(year,month,Constants.TYPE_INCOME,Constants.TYPE_EXPENSE))
            callback(value as Resource.Success<Int>)
        }
    }

    suspend fun loadYeaBalance(year: String,month: String,callback:(Resource<Int>)->Unit){
        var value : Resource<Int>? = null
        CoroutineScope(Dispatchers.IO).launch {
            value = Resource.Success(dao.loadYearBalance(year,Constants.TYPE_INCOME,Constants.TYPE_EXPENSE))
            callback(value as Resource.Success<Int>)
        }
    }


}