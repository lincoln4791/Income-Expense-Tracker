package com.lincoln4791.dailyexpensemanager.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VMMonthlyCategoryWise @Inject constructor (val repository:Repository) : ViewModel() {
    private var _postsList: MutableLiveData<Resource<List<MC_Posts>>> = MutableLiveData<Resource<List<MC_Posts>>>()
    val postsList: LiveData<Resource<List<MC_Posts>>>
        get()=_postsList


    lateinit var year: String
    lateinit var month: String
    lateinit var type: String
    lateinit var category: String
    lateinit var fragmentFrom: String
    lateinit var selectedTransactionType: String

    fun loadYearMonthTypeCategoryWise(year:String,month:String,type:String,category:String){
        _postsList.value = Resource.Loading
            viewModelScope.launch {
                _postsList.postValue(repository.loadYearMonthTypeCategoryWise(year,month,type,category))
            }

    }


}