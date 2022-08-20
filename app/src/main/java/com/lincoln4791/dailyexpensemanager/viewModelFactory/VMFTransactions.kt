package com.lincoln4791.dailyexpensemanager.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.viewModels.VM_AddExpenses
import com.lincoln4791.dailyexpensemanager.viewModels.VM_Transactions

@Suppress("Unchecked Cast")
class VMFTransactions(val repository: Repository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VM_Transactions(repository) as T
    }
}