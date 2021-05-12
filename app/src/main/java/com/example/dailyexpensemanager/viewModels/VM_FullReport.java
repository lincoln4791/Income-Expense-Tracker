package com.example.dailyexpensemanager.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dailyexpensemanager.common.Constants;
import com.example.dailyexpensemanager.model.MC_Posts;

import java.util.ArrayList;
import java.util.List;

public class VM_FullReport extends ViewModel {
    public List<MC_Posts> postsList = new ArrayList<>();
    public String year = Constants.YEAR_DEFAULT;
    public String month = Constants.MONTH_NULL;
    public String day = Constants.DAY_NULL;
    public String type = Constants.TYPE_ALL;
    public String category = Constants.CATEGORY_NULL;
    public int income_salary,income=0,income_business=0,income_house_rent=0,income_other=0,total_income=0;
    public int expense_food=0,expense_transport=0,expense_bills=0,expense_houseRent=0,expense_business=0,
            expense_medicine=0,expense_cloths=0,expense_education=0,expense_lifestyle=0,expense_other=0,total_expense=0;


    public MutableLiveData<List<MC_Posts>> mutablePostsList = new MutableLiveData<>();
    public MutableLiveData<String > mutableYear = new MutableLiveData<>();
    public MutableLiveData<String > mutableMonth = new MutableLiveData<>();
    public MutableLiveData<String > mutableDay = new MutableLiveData<>();
    public MutableLiveData<String > mutableType = new MutableLiveData<>();
    public MutableLiveData<String > mutableCategory = new MutableLiveData<>();



}
