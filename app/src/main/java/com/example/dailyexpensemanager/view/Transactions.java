package com.example.dailyexpensemanager.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.dailyexpensemanager.R;
import com.example.dailyexpensemanager.common.Constants;
import com.example.dailyexpensemanager.common.Extras;

public class Transactions extends AppCompatActivity {
    private CardView cv_today,cv_home,cv_fullReport,cv_pieChart;
    private TextView tv_incomeValue_topBar,tv_expenseValue_topBar,tv_balanceValue_topBar,tv_typeTitle;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);


        //**********************************************View Bindings*******************************************
        cv_today = findViewById(R.id.cv_today_Transactions);
        cv_home = findViewById(R.id.cv_home_Transactions);
        cv_fullReport = findViewById(R.id.cv_fullReport_Transactions);
        tv_incomeValue_topBar = findViewById(R.id.tv_totalIncomeValue_topBar_Transactions);
        tv_expenseValue_topBar = findViewById(R.id.tv_totalExpenseValue_topBar_Transactions);
        tv_balanceValue_topBar = findViewById(R.id.tv_totalBalanceValue_topBar_Transactions);
        tv_typeTitle = findViewById(R.id.tv_typeTitle_Transactions);
        cv_pieChart = findViewById(R.id.cv_pieChart_Transactions);
        recyclerView = findViewById(R.id.rv_Transactions);


        //***********************************************Initializations*****************************************
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        //************************************************Click Listeners****************************************

        cv_today.setOnClickListener(v -> {

        });

        cv_home.setOnClickListener(v -> {
            startActivity(new Intent(Transactions.this,MainActivity.class));
        });

        cv_fullReport.setOnClickListener(v -> {
            startActivity(new Intent(Transactions.this,Monthly.class));
        });


        cv_pieChart.setOnClickListener(v -> {
            startActivity(new Intent(Transactions.this,PieChart.class));
        });





        //************************************************Starting Methods**************************************

        getIntentData();


    }

    private void getIntentData() {
        if(getIntent().getStringExtra(Extras.TYPE).equals(Constants.TYPE_INCOME)){
            showIncomes();
        }

        else if(getIntent().getStringExtra(Extras.TYPE).equals(Constants.TYPE_EXPENSE)){
            showExpenses();
        }

        else if(getIntent().getStringExtra(Extras.TYPE).equals(Constants.TYPE_ALL)){
            showAllTransactions();
        }

    }

    private void showIncomes(){
        tv_typeTitle.setText(getString(R.string.Incomes));
    }

    private void showExpenses(){
        tv_typeTitle.setText(getString(R.string.Expenses));
    }

    private void showAllTransactions(){
        tv_typeTitle.setText(getString(R.string.Transactions));
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}