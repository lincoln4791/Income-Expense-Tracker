package com.example.mvvm_bilalkhan.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lincoln4791.dailyexpensemanager.AuthRepository
import com.lincoln4791.dailyexpensemanager.base.BaseRepository
import com.lincoln4791.dailyexpensemanager.viewModels.AuthViewModel
import java.lang.IllegalStateException

@Suppress("Unchecked Cast")
class ViewModelFactory(
    private val repository : BaseRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       return when{
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repository as AuthRepository) as T
           else -> throw IllegalStateException("ViewModel Class Not Found")
        }
    }

}