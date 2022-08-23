package com.lincoln4791.dailyexpensemanager.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.roomDB.DatabaseDao
import com.lincoln4791.dailyexpensemanager.viewModels.*
import java.lang.IllegalStateException

@Suppress("Unchecked Cast")
open class ViewModelFactory(
    private val repository : Repository,
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       return when{
           modelClass.isAssignableFrom(VMAddExpenses::class.java) -> VMAddExpenses(repository) as T
           modelClass.isAssignableFrom(VMAddIncome::class.java) -> VMAddIncome(repository) as T
           modelClass.isAssignableFrom(VMDaily::class.java) -> VMDaily(repository) as T
           modelClass.isAssignableFrom(VMFullReport::class.java) -> VMFullReport(repository) as T
           modelClass.isAssignableFrom(VMMonthlyCategoryWise::class.java) -> VMMonthlyCategoryWise(repository) as T
           modelClass.isAssignableFrom(VMMonthlyReport::class.java) -> VMMonthlyReport(repository) as T
           modelClass.isAssignableFrom(VMTransactions::class.java) -> VMTransactions(repository) as T
           else -> throw IllegalStateException("ViewModel Class Not Found")
        }
    }
}

class MainViewModelFactory(private val dao: DatabaseDao) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  VMHomeFragment(dao) as T
    }
}