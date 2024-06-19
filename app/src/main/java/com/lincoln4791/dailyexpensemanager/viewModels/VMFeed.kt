package com.lincoln4791.dailyexpensemanager.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.RepositoryNetwork
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.common.PrefManager
import com.lincoln4791.dailyexpensemanager.model.MC_Cards
import com.lincoln4791.dailyexpensemanager.modelClass.QuoteModelClass.QuotesResponseModel
import com.lincoln4791.dailyexpensemanager.network.QuoteApi
import com.lincoln4791.dailyexpensemanager.network.RetrofitClient
import com.lincoln4791.dailyexpensemanager.paging.PostDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class VMFeed @Inject constructor (private val repository: Repository, private val repositoryNetwork: RepositoryNetwork): ViewModel() {

    val listData = Pager(PagingConfig(pageSize = 50)) {
        PostDataSource(RetrofitClient.getRetrofitQuoteClient())
    }.flow.cachedIn(viewModelScope)

}