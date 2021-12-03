package com.lincoln4791.dailyexpensemanager.viewModels

import androidx.lifecycle.ViewModel
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import androidx.lifecycle.MutableLiveData
import com.lincoln4791.dailyexpensemanager.common.Constants
import java.util.ArrayList

class VM_FullReport : ViewModel() {
    var postsList: MutableList<MC_Posts> = mutableListOf()
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
}