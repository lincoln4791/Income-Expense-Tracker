package com.lincoln4791.dailyexpensemanager.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lincoln4791.dailyexpensemanager.modelClass.QuoteModelClass.Result
import com.lincoln4791.dailyexpensemanager.network.QuoteApi

class PostDataSource(private val apiService: QuoteApi) : PagingSource<Int, Result>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int,Result> {
        try {
            val currentLoadingPageKey = params.key ?: 1
            val response = apiService.getListData(currentLoadingPageKey,50)
            val responseData = mutableListOf<Result>()
            Log.d("tag","data -- code -> ${response.code()} :: body -> ${response.body()}")
            val data = response.body()?.results ?: emptyList()
            responseData.addAll(data)

            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1
            Log.d("tag","data load success -> ${data.size}")

            if(currentLoadingPageKey<response.body()!!.totalCount){
                return LoadResult.Page(
                    data = responseData,
                    prevKey = prevKey,
                    nextKey = currentLoadingPageKey.plus(1)
                )
            }
            else{
                return LoadResult.Page(
                    data = responseData,
                    prevKey = null,
                    nextKey = null
                )
            }


        } catch (e: Exception) {
            Log.d("tag","data load error -> ${e.localizedMessage}")
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}