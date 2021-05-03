package com.example.dailyexpensemanager.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dailyexpensemanager.R;

public class AddExpense extends AppCompatActivity {

    CardView cv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        cv_back = findViewById(R.id.cv_back_AddExpense);

        cv_back.setOnClickListener(v -> startActivity(new Intent(AddExpense.this,MainActivity.class)));


    }
}