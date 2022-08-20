package com.lincoln4791.dailyexpensemanager.viewModelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.roomDB.AppDatabase
import com.lincoln4791.dailyexpensemanager.roomDB.DatabaseDao
import com.lincoln4791.dailyexpensemanager.viewModels.*
import java.lang.IllegalStateException

@Suppress("Unchecked Cast")
open class ViewModelFactory(
    private val repository : Repository,
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       return when{
           modelClass.isAssignableFrom(VM_AddExpenses::class.java) -> VM_AddExpenses(repository) as T
           modelClass.isAssignableFrom(VM_AddIncome::class.java) -> VM_AddIncome(repository) as T
           modelClass.isAssignableFrom(VM_Daily::class.java) -> VM_Daily(repository) as T
           modelClass.isAssignableFrom(VM_FullReport::class.java) -> VM_FullReport(repository) as T
           modelClass.isAssignableFrom(VM_MonthlyCategoryWise::class.java) -> VM_MonthlyCategoryWise(repository) as T
           modelClass.isAssignableFrom(VM_MonthlyReport::class.java) -> VM_MonthlyReport(repository) as T
           modelClass.isAssignableFrom(VM_Transactions::class.java) -> VM_Transactions(repository) as T
           else -> throw IllegalStateException("ViewModel Class Not Found")
        }
    }
}

class MainViewModelFactory(private val dao: DatabaseDao) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  VM_MainActivity(dao) as T
    }
}