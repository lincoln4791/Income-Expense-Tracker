package com.lincoln4791.dailyexpensemanager.viewModels

import androidx.lifecycle.ViewModel
import com.lincoln4791.dailyexpensemanager.AuthRepository

class AuthViewModel(private val repository: AuthRepository):ViewModel() {

/*    private val _loginResponse : MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val loginResponse : LiveData<Resource<LoginResponse>>
    get() = _loginResponse

    fun login(
        username:String,
        password:String
    ) = viewModelScope.launch {
        _loginResponse.value = repository.login(username,password)
    }*/

}