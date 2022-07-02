package com.lincoln4791.dailyexpensemanager.base
import com.lincoln4791.dailyexpensemanager.Resource2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseRepository {

    suspend fun <T> safeApiCAll(
        apiCall : suspend ()->T
    ): Resource2<T> {
        return withContext(Dispatchers.IO){
            try {
                Resource2.Success(apiCall.invoke())
            }
            catch (throwabel : Throwable){
                when(throwabel){
                    is HttpException -> {
                        Resource2.Failure(false,throwabel.code(), throwabel.response()?.errorBody())
                    }
                    else -> Resource2.Failure(true,null,null)

                }
            }
        }
    }

}