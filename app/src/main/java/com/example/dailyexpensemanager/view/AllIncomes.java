package com.example.dailyexpensemanager.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.dailyexpensemanager.R;

public class AllIncomes extends AppCompatActivity {
    private CardView cv_topLogo,cv_today,cv_transactions,cv_fullReport,cv_pieChart;
    private TextView tv_incomeValue_topBar,tv_expenseValue_topBar,tv_balanceValue_topBar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_incomes);


        //*************************************************View Bindings*************************************************
        cv_topLogo = findViewById(R.id.cv_topLogo_AllIncomes);
        cv_today = findViewById(R.id.cv_todaysTransactions_AllIncomes);
        cv_transactions = findViewById(R.id.cv_transactions_AllIncomes);
        cv_fullReport = findViewById(R.id.cv_fullReport_AllIncomes);
        tv_incomeValue_topBar = findViewById(R.id.tv_incomeValue_topBar_AllIncomes);
        tv_expenseValue_topBar = findViewById(R.id.tv_expenseValue_topBar_AllIncomes);
        tv_balanceValue_topBar = findViewById(R.id.tv_balanceValue_topBar_AllIncomes);
        cv_pieChart = findViewById(R.id.cv_pieChart_AllIncomes);
        recyclerView = findViewById(R.id.recyclerView_AllIncomes);






        //*************************************************Initializations*********************************************
            recyclerView.setLayoutManager(new LinearLayoutManager(this));





        //**********************************************Click Listeners***************************************************
        cv_topLogo.setOnClickListener(v -> {
          startActivity(new Intent(AllIncomes.this,MainActivity.class));
        });

        cv_today.setOnClickListener(v -> {
        startActivity(new Intent(AllIncomes.this,TodaysTransactions.class));
        });

        cv_transactions.setOnClickListener(v -> {
            startActivity(new Intent(AllIncomes.this,Transactions.class));
        });

        cv_fullReport.setOnClickListener(v -> {
            startActivity(new Intent(AllIncomes.this, FullReport.class));
        });

        cv_pieChart.setOnClickListener(v -> {
            startActivity(new Intent(AllIncomes.this, PieChartActivity.class));
        });



        //*********************************************Initial Methods***************************************************

    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }




}