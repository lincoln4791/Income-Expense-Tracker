package com.lincoln4791.dailyexpensemanager

import android.content.Context
import androidx.room.Dao
import com.lincoln4791.dailyexpensemanager.base.BaseRepository
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.model.MC_Cards
import com.lincoln4791.dailyexpensemanager.model.MC_MonthlyReport
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import com.lincoln4791.dailyexpensemanager.network.QuoteApi
import com.lincoln4791.dailyexpensemanager.roomDB.AppDatabase
import com.lincoln4791.dailyexpensemanager.roomDB.DatabaseDao
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class RepositoryNetwork@Inject constructor(
    private val quoteApi: QuoteApi
    ) : BaseRepository() {

    //Quotes
    //suspend fun getAllQuotes()=safeApiCall { quoteApi.getListData(50) }

}