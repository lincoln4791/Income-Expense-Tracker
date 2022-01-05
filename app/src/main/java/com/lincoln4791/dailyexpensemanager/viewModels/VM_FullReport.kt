package com.lincoln4791.dailyexpensemanager.viewModels

import android.app.Application
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import androidx.lifecycle.MutableLiveData
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.common.Constants
import java.lang.Exception
import java.util.ArrayList

class VM_FullReport(application: Application) : AndroidViewModel(application) {
    var postsList: MutableLiveData<Resource<List<MC_Posts>>> = MutableLiveData<Resource<List<MC_Posts>>>()
    private var repository : Repository = Repository(application.applicationContext)
    var year = Constants.YEAR_DEFAULT
    var month = Constants.MONTH_NULL
    var day = Constants.DAY_NULL
    var type = Constants.TYPE_ALL
    var category = Constants.CATEGORY_NULL
    var income_salary = 0
    var income = 0
    var income_business = 0
    var income_house_rent = 0
    var income_other = 0
    var total_income = 0
    var expense_food = 0
    var expense_transport = 0
    var expense_bills = 0
    var expense_houseRent = 0
    var expense_business = 0
    var expense_medicine = 0
    var expense_cloths = 0
    var expense_education = 0
    var expense_lifestyle = 0
    var expense_other = 0
    var total_expense = 0
    var mutablePostsList = MutableLiveData<List<MC_Posts>>()
    var mutableYear = MutableLiveData<String>()
    var mutableMonth = MutableLiveData<String>()
    var mutableDay = MutableLiveData<String>()
    var mutableType = MutableLiveData<String>()
    var mutableCategory = MutableLiveData<String>()

    fun loadYearMonthDayTypeCategoryWise(year:String,month:String,day:String,type:String,category:String){
        postsList.value = Resource.Loading()
        try {
            repository.loadYearMonthDayTypeCategoryWise(year,month,day,type,category) {
                android.os.Handler(Looper.getMainLooper()).post{
                    postsList.value = it
                }
            }
        }
        catch (e: Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }


    fun loadYearMonthTypeCategoryWise(year:String,month:String,type:String,category:String){
        postsList.value = Resource.Loading()
        try {
            repository.loadYearMonthTypeCategoryWise(year,month,type,category) {
                android.os.Handler(Looper.getMainLooper()).post{
                    postsList.value = it
                }
            }
        }
        catch (e: Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }


    fun loadYearMonthDayTypeWise(year:String,month:String,day:String,type:String){
        postsList.value = Resource.Loading()
        try {
            repository.loadYearMonthDayTypeWise(year,month,day,type) {
                android.os.Handler(Looper.getMainLooper()).post{
                    postsList.value = it
                }
            }
        }
        catch (e: Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }


    fun loadYearMonthDayCategoryWise(year:String,month:String,day:String,category:String){
        postsList.value = Resource.Loading()
        try {
            repository.loadYearMonthDayCategoryWise(year,month,day,category) {
                android.os.Handler(Looper.getMainLooper()).post{
                    postsList.value = it
                }
            }
        }
        catch (e: Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }

    fun loadYearMonthDayWise(year:String,month:String,day:String){
        postsList.value = Resource.Loading()
        try {
            repository.loadYearMonthDayWise(year,month,day) {
                android.os.Handler(Looper.getMainLooper()).post{
                    postsList.value = it
                }
            }
        }
        catch (e: Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }


    fun loadYearMonthTypeWise(year:String,month:String,type:String){
        postsList.value = Resource.Loading()
        try {
            repository.loadYearMonthTypeWise(year,month,type) {
                android.os.Handler(Looper.getMainLooper()).post{
                    postsList.value = it
                }
            }
        }
        catch (e: Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }


    fun loadYearMonthCategoryWise(year:String,month:String,category:String){
        postsList.value = Resource.Loading()
        try {
            repository.loadYearMonthCategoryWise(year,month,category) {
                android.os.Handler(Looper.getMainLooper()).post{
                    postsList.value = it
                }
            }
        }
        catch (e: Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }


    fun loadYearMonthCategoryWise(year:String,month:String){
        postsList.value = Resource.Loading()
        try {
            repository.loadYearMonthWise(year,month) {
                android.os.Handler(Looper.getMainLooper()).post{
                    postsList.value = it
                }
            }
        }
        catch (e: Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }

    fun loadYearTypeWise(year:String,type:String){
        postsList.value = Resource.Loading()
        try {
            repository.loadYearTypeWise(year,type) {
                android.os.Handler(Looper.getMainLooper()).post{
                    postsList.value = it
                }
            }
        }
        catch (e: Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }

    fun loadYearTypeCategoryWise(year:String,type:String,category:String){
        postsList.value = Resource.Loading()
        try {
            repository.loadYearTypeCategoryWise(year,type, category) {
                android.os.Handler(Looper.getMainLooper()).post{
                    postsList.value = it
                }
            }
        }
        catch (e: Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }


    fun loadYearMonthWise(year:String,month:String){
        postsList.value = Resource.Loading()
        try {
            repository.loadYearMonthWise(year,month) {
                android.os.Handler(Looper.getMainLooper()).post{
                    postsList.value = it
                }
            }
        }
        catch (e: Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }

    fun loadYearWise(year:String){
        postsList.value = Resource.Loading()
        try {
            repository.loadYearWise(year) {
                android.os.Handler(Looper.getMainLooper()).post{
                    postsList.value = it
                }
            }
        }
        catch (e: Exception){
            postsList.value = Resource.Error("Failed to retrive data -> ${e.message}")
        }
    }

}