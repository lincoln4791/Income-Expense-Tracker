package com.lincoln4791.dailyexpensemanager.background.worker

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.room.Dao
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.database.BuildConfig
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.itmedicus.patientaid.utils.*
import com.itmedicus.patientaid.utils.CurrentDate.Companion.currentTime
import com.itmedicus.patientaid.utils.DayDifference.Companion.getDaysDifference
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.common.PrefManager
import com.lincoln4791.dailyexpensemanager.common.util.*
import com.lincoln4791.dailyexpensemanager.fragments.HomeFragment
import com.lincoln4791.dailyexpensemanager.modelClass.Banner
import com.lincoln4791.dailyexpensemanager.roomDB.AppDatabase
import com.lincoln4791.dailyexpensemanager.roomDB.DatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class PeriodicSyncWorker(var context: Context, workerParameter: WorkerParameters) :
    CoroutineWorker(context, workerParameter) {


    private val sdf = SimpleDateFormat("yyyy-MM-dd")
    private val currentDate = sdf.parse(CurrentDate.currentDate)

    private lateinit var prefManager: PrefManager
    private lateinit var dao: DatabaseDao


    override suspend fun doWork(): Result {

        try {
            init()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "doWork: Exception: ${e.localizedMessage}")

            return Result.retry()
        }

        return Result.success()
    }


    private fun init() {
        prefManager = PrefManager(context)
        dao = AppDatabase.getInstance(applicationContext).dbDao()
        CoroutineScope(Dispatchers.Default).launch {
            startSync()
        }

    }


    private suspend fun startSync() {

        if(System.currentTimeMillis()-prefManager.lastBannerSyncTime >= Constants.INTERVAL_DAILY){
            CoroutineScope(Dispatchers.Default).launch { banner() }
        }
        else{
            Log.d(TAG,"Sync for today is completed")
        }

    }

    private suspend fun banner() {

        withContext(Dispatchers.Default) {
            if (NetworkCheck.isConnect(applicationContext)) {
                Log.d("bannerSync","Called")
                val postListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        CoroutineScope(Dispatchers.Default).launch {
                            // Get Post object and use the values to update the UI
                            val data = dataSnapshot.value
                            val bannerList : ArrayList<Banner> = ArrayList<Banner>()

                            for (ds in dataSnapshot.children) {
                                val created_date = ds.child("created_date").getValue(String::class.java)
                                val deleted_date = ds.child("deleted_date").getValue(String::class.java)
                                val isActive = ds.child("isActive").getValue(String::class.java)
                                val link = ds.child("link").getValue(String::class.java)
                                val media_url = ds.child("media_url").getValue(String::class.java)
                                val name = ds.child("name").getValue(String::class.java)

                                val bannerModel = Banner(created_date!!,deleted_date!!,isActive!!,link!!,media_url!!,name!!)
                                bannerList.add(bannerModel)
                            }

                            Log.d(TAG,"banner list is -> $bannerList")

                            for(element in bannerList){
                                if(element.created_date.isNotEmpty()){
                                    insertBanner(element)
                                    FileUtils.saveImageToAppSpecificStorageFromUrl(applicationContext,element.name,element.media_url)
                                }

                                if(element.deleted_date.isNotEmpty()){
                                    deleteBanner(element)
                                }

                            }

                            prefManager.lastBannerSyncTime = System.currentTimeMillis()

                         /*   if(existingBannerList.isEmpty()){
                                Log.d(TAG,"banner list is empty")
                                for(element in bannerList){
                                    insertBanner(element)
                                    FileUtils.saveImageToAppSpecificStorageFromUrl(applicationContext,element.name,element.media_url)
                                }
                            }
                            else{
                                Log.d(TAG,"banner list Exists")
                                for(element in bannerList){
                                    insertBanner(element)
                                    FileUtils.saveImageToAppSpecificStorageFromUrl(applicationContext,element.name,element.media_url)
                                }

                                Log.d(TAG,"banner list Exists")
                            }*/


                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Getting Post failed, log a message
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                    }
                }
                val ref= Firebase.database.reference.child(NodeNames.Common).child(NodeNames.Banner).child(NodeNames.banners)

                ref.addListenerForSingleValueEvent(postListener)
            }
            else{
                Log.d(TAG,"No Internet")
            }

        }

    }

    private suspend fun insertBanner(banner: Banner){
        dao.insertBanner(banner)
    }

    private suspend fun deleteBanner(banner: Banner){
        dao.deleteBanner(banner)
    }


    companion object{
        const val TAG = "periodicSyncWorker"
    }

}
