package com.example.dailyexpensemanager.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VM_EditDataExpense extends ViewModel {
    public String category="",amount="",dateTime="";
    public String  time,day,month,year;


    public MutableLiveData<String > mutable_category = new MutableLiveData<>();
    public MutableLiveData<String> mutable_amount = new MutableLiveData<>();
}
