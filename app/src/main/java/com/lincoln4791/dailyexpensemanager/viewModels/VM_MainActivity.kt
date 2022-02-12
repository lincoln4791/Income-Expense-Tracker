package com.lincoln4791.dailyexpensemanager.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.common.util.GlobalVariabls
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import com.lincoln4791.dailyexpensemanager.roomDB.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class VM_MainActivity(application: Application) : AndroidViewModel(application) {
    val dao = AppDatabase.getInstance(application.applicationContext).dbDao()
    var totalIncome: MutableLiveData<Int> = MutableLiveData()
    var totalExpense: MutableLiveData<Int> = MutableLiveData()
    var currentBalance: MutableLiveData<Int> = MutableLiveData()


    fun getIncomeExpenseData() {
        var income = 0
        var expense = 0
        CoroutineScope(Dispatchers.IO).launch {
            var transactions: List<MC_Posts>? = mutableListOf()
            val job1 = launch {
                 transactions = getAllTransactions()
            }

            job1.join()

            if(transactions!=null){
                for(transaction in transactions!!) {
                    val postType = transaction.postType
                    val amount = transaction.postAmount.toInt()
                    if ((postType == Constants.TYPE_INCOME)) income += amount
                    else if ((postType == Constants.TYPE_EXPENSE)) expense += amount
                }

               CoroutineScope(Dispatchers.Main).launch {
                   totalIncome.value = income
                   totalExpense.value = expense
                   currentBalance.value = income-expense
                   GlobalVariabls.currentBalance = income-expense
               }
            }
            else{
                Log.d("tag","Something Went Wring")
            }




        }
    }


    private suspend fun getAllTransactions(): List<MC_Posts>? {
        try {
            return dao.loadAllTransactions()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

}