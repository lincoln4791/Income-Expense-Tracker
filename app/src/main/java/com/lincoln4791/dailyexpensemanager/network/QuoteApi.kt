package com.lincoln4791.dailyexpensemanager.network

import androidx.paging.PagingData
import com.lincoln4791.dailyexpensemanager.modelClass.QuoteModelClass.QuotesResponseModel
import com.lincoln4791.dailyexpensemanager.modelClass.QuoteModelClass.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.*

interface QuoteApi {

    @GET("quotes")
    suspend fun getListData(
        @Query("page") pageNumber: Int,
        @Query("limit") limit: Int
    ): Response<QuotesResponseModel>
}