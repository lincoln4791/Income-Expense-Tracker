package com.example.dailyexpensemanager.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.dailyexpensemanager.R;

public class AllExpenses extends AppCompatActivity {
    private CardView cv_topLogo,cv_today,cv_transactions,cv_fullReport,cv_pieChart;
    private TextView tv_incomeValue_topBar,tv_expenseValue_topBar,tv_balanceValue_topBar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_expenses);


        //*************************************************View Bindings*************************************************
        cv_topLogo = findViewById(R.id.cv_topLogo_AllExpenses);
        cv_today = findViewById(R.id.cv_todaysTransactions_AllExpenses);
        cv_transactions = findViewById(R.id.cv_dailyTransactions_AllExpenses);
        cv_fullReport = findViewById(R.id.cv_fullReport_AllExpenses);
        tv_incomeValue_topBar = findViewById(R.id.tv_incomeValue_topBar_AllExpenses);
        tv_expenseValue_topBar = findViewById(R.id.tv_expenseValue_topBar_AllExpenses);
        tv_balanceValue_topBar = findViewById(R.id.tv_balanceValue_topBar_AllExpenses);
        cv_pieChart = findViewById(R.id.cv_pieChart_AllExpenses);
        recyclerView = findViewById(R.id.recyclerView_AllExpenses);






        //*************************************************Initializations*********************************************
        recyclerView.setLayoutManager(new LinearLayoutManager(this));





        //**********************************************Click Listeners***************************************************
        cv_topLogo.setOnClickListener(v -> {
            startActivity(new Intent(AllExpenses.this,MainActivity.class));
        });

        cv_today.setOnClickListener(v -> {
            startActivity(new Intent(AllExpenses.this,TodaysTransactions.class));
        });

        cv_transactions.setOnClickListener(v -> {
            startActivity(new Intent(AllExpenses.this,Transactions.class));
        });

        cv_fullReport.setOnClickListener(v -> {
            startActivity(new Intent(AllExpenses.this,Monthly.class));
        });


        cv_pieChart.setOnClickListener(v -> {
           startActivity(new Intent(AllExpenses.this,PieChart.class));
        });



        //*********************************************Initial Methods***************************************************
        observe();

    }

    private void observe() {

    }


}