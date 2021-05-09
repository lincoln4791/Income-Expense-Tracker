package com.example.dailyexpensemanager.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dailyexpensemanager.R;
import com.example.dailyexpensemanager.common.Constants;
import com.example.dailyexpensemanager.common.Extras;

public class MainActivity extends AppCompatActivity {
    private CardView cv_addIncome,cv_addExpense,cv_transactions,cv_fullReport,cv_incomes,cv_expenses,cv_incomesTopBar, cv_expensesTopBar;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //************************************************* View Bindings***************************************
        cv_addIncome = findViewById(R.id.cv_addIncome_MainActivity);
        cv_addExpense = findViewById(R.id.cv_addExpenses_MainActivity);
        cv_fullReport = findViewById(R.id.cv_fullReport_MainActivity);
        cv_transactions = findViewById(R.id.cv_transactions_MainActivity);
        cv_incomes = findViewById(R.id.cv_income_MainActivity);
        cv_expenses = findViewById(R.id.cv_expenses_MainActivity);
        cv_incomesTopBar = findViewById(R.id.cv_totalIncomes_topBar_MainActivity);
        cv_expensesTopBar = findViewById(R.id.cv_totalExpenses_topBar_MainActivity);



        cv_addIncome.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,AddIncome.class)));

        cv_addExpense.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,AddExpense.class)));

        cv_fullReport.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,Monthly.class)));

        cv_transactions.setOnClickListener(v -> {
            Intent transactionsIntent = new Intent(MainActivity.this,Transactions.class);
            transactionsIntent.putExtra(Extras.TYPE, Constants.TYPE_ALL);
            startActivity(transactionsIntent);
        });

        cv_incomes.setOnClickListener(v -> {
            Intent incomeIntent = new Intent(MainActivity.this,Transactions.class);
            incomeIntent.putExtra(Extras.TYPE, Constants.TYPE_INCOME);
            startActivity(incomeIntent);
        });

        cv_expenses.setOnClickListener(v -> {
            Intent incomeIntent = new Intent(MainActivity.this,Transactions.class);
            incomeIntent.putExtra(Extras.TYPE, Constants.TYPE_EXPENSE);
            startActivity(incomeIntent);
        });

        cv_incomesTopBar.setOnClickListener(v -> {
            Intent incomeIntent = new Intent(MainActivity.this,Transactions.class);
            incomeIntent.putExtra(Extras.TYPE, Constants.TYPE_INCOME);
            startActivity(incomeIntent);
        });

        cv_expensesTopBar.setOnClickListener(v -> {
            Intent expenseIntent = new Intent(MainActivity.this,Transactions.class);
            expenseIntent.putExtra(Extras.TYPE, Constants.TYPE_EXPENSE);
            startActivity(expenseIntent);
        });



    }


    @Override
    public void onBackPressed() {
        confirmLogout();
    }



    private void confirmLogout() {
        Dialog dialog = new Dialog(MainActivity.this);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_logout,null);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();

        view.findViewById(R.id.btn_yes_alertImage_dialog_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
                finishAffinity();

            }
        });

        view.findViewById(R.id.btn_no_alertImage_dialog_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }




}