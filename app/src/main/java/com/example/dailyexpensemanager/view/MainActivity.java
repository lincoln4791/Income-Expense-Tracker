package com.example.dailyexpensemanager.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dailyexpensemanager.R;

public class MainActivity extends AppCompatActivity {
    private CardView cv_addIncome,cv_addExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cv_addIncome = findViewById(R.id.cv_addIncome_MainActivity);
        cv_addExpense = findViewById(R.id.cv_addExpenses_MainActivity);

        cv_addIncome.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,AddIncome.class)));


        cv_addExpense.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,AddExpense.class)));

    }
}