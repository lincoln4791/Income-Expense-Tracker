package com.example.dailyexpensemanager.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dailyexpensemanager.model.MC_Posts;

import java.util.ArrayList;
import java.util.List;

public class VM_AddExpenses extends ViewModel {
    public String category="",amount="";


    public MutableLiveData<String > mutable_category = new MutableLiveData<>();
    public MutableLiveData<String> mutable_amount = new MutableLiveData<>();

}
