package com.lincoln4791.dailyexpensemanager

import android.content.Context
import androidx.room.Dao
import com.lincoln4791.dailyexpensemanager.base.BaseRepository
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.model.MC_Cards
import com.lincoln4791.dailyexpensemanager.model.MC_MonthlyReport
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import com.lincoln4791.dailyexpensemanager.roomDB.AppDatabase
import com.lincoln4791.dailyexpensemanager.roomDB.DatabaseDao
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class Repository@Inject constructor(
    private val dao: DatabaseDao
    ) : BaseRepository() {

    suspend fun insertPost(posts : MC_Posts) = safeApiCall { dao.insertAll(posts) }

    suspend fun getExpenses() = safeApiCall {dao.loadTypeWise(Constants.TYPE_EXPENSE) }

    suspend fun getIncomes() = safeApiCall {dao.loadTypeWise(Constants.TYPE_INCOME) }

    suspend fun getTransactions() = safeApiCall { dao.loadAllTransactions() }


    suspend fun loadTypeWise(type:String)=safeApiCall { dao.loadTypeWise(type) }

    suspend fun loadYearWise(year:String) = safeApiCall { dao.loadYearWise(year) }

    suspend fun loadYearTypeWise(year:String,type:String) = safeApiCall { dao.loadYearTypeWise(year,type) }

    suspend fun loadYearCategoryWise(year:String,category:String) = safeApiCall { dao.loadYearCategoryWise(year,category) }

    suspend fun loadYearTypeCategoryWise(year:String,type:String,category:String)=safeApiCall { dao.loadYearTypeCategoryWise(year,type,category) }

    suspend fun loadYearMonthWise(year:String,month:String)=safeApiCall { dao.loadYearMonthWise(year,month) }

    suspend fun loadYearMonthTypeWise(year:String,month:String,type:String)=safeApiCall { dao.loadYearMonthTypeWise(year,month,type) }

    suspend fun loadYearMonthCategoryWise(year:String,month:String,category:String)=safeApiCall { dao.loadYearMonthCategoryWise(year,month,category) }

    suspend fun loadYearMonthTypeCategoryWise(year:String,month:String,type:String,category:String)=safeApiCall { dao.loadYearMonthTypeCategoryWise(year,month,type,category) }

    suspend fun loadYearMonthDayWise(year:String,month:String,day:String)=safeApiCall { dao.loadYearMonthDayWise(year,month,day) }

    suspend fun loadYearMonthDayTypeWise(year:String,month:String,day:String,type:String)=safeApiCall { dao.loadYearMonthDayTypeWise(year,month,day,type) }

    suspend fun loadYearMonthDayCategoryWise(year:String,month:String,day:String,category:String)=safeApiCall { dao.loadYearMonthDayCategoryWise(year,month,day,category) }

    suspend fun loadYearMonthDayTypeCategoryWise(year:String,month:String,day:String,type:String,category:String)=safeApiCall { dao.loadYearMonthDayTypeCategoryWise(year,month,day,type,category) }

    suspend fun loadYearMonthTypeWiseByGroup(year:String, month:String,type:String)=safeApiCall { dao.loadYearMonthTypeWiseByGroup(year,month,type) }

    suspend fun loadYearTypeWiseByGroup(year:String,type:String)=safeApiCall { dao.loadYearTypeWiseByGroup(year,type) }




    // Cards
    suspend fun getAllExpenseCards()=safeApiCall { dao.loadAllExpenseCards(Constants.TYPE_EXPENSE) }

    suspend fun getAllCardsByTypeArrayString(type:String)=safeApiCall { dao.getAllCardsByTypeAsArrayString(type) }

    suspend fun getAllIncomeCards()=safeApiCall { dao.loadAllIncomeCards(Constants.TYPE_INCOME) }

    suspend fun loadYearMonthTypeTotal(year: String,month: String,type: String)=safeApiCall { dao.loadYearMonthTypeTotal(year,month,type) }

    suspend fun loadYearTypeTotal(year: String,type: String)=safeApiCall { dao.loadYearTypeTotal(year,type) }

    suspend fun loadYearMonthBalance(year: String,month: String)=safeApiCall { dao.loadYearMonthBalance(year,month,Constants.TYPE_INCOME,Constants.TYPE_EXPENSE) }

    suspend fun loadYeaBalance(year: String,month: String)=safeApiCall { dao.loadYearBalance(year,Constants.TYPE_INCOME,Constants.TYPE_EXPENSE) }


}