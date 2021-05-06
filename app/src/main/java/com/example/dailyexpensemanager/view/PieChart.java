package com.example.dailyexpensemanager.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.dailyexpensemanager.R;

public class PieChart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}