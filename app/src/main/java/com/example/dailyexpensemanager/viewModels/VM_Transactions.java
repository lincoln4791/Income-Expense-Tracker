package com.example.dailyexpensemanager.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dailyexpensemanager.model.MC_Posts;

import java.util.ArrayList;
import java.util.List;

public class VM_Transactions extends ViewModel {
    public String vm_category;
    public List<MC_Posts> postsList = new ArrayList<>();

    public MutableLiveData<List<MC_Posts>> mutable_postsList = new MutableLiveData<>();
}
