package com.example.dailyexpensemanager.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dailyexpensemanager.R;
import com.example.dailyexpensemanager.common.Constants;
import com.example.dailyexpensemanager.common.Extras;
import com.example.dailyexpensemanager.common.SQLiteHelper;
import com.example.dailyexpensemanager.common.UtilDB;

public class MainActivity extends AppCompatActivity {
    private CardView cv_addIncome,cv_addExpense,cv_transactions,cv_fullReport,cv_incomes,cv_expenses,cv_incomesTopBar, cv_expensesTopBar, cv_daily,cv_monthly;
    private TextView tv_totalIncome,tv_totalExpense,tv_balance,tv_currentBalance_toolbar;
    private int totalIncome=0,totalExpenses=0;
    private Spinner spinner;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //************************************************* View Bindings***************************************
        cv_addIncome = findViewById(R.id.cv_addIncome_MainActivity);
        cv_addExpense = findViewById(R.id.cv_addExpenses_MainActivity);
        cv_fullReport = findViewById(R.id.cv_fullReport_MainActivity);
        cv_daily = findViewById(R.id.cv_daily_MainActivity);
        cv_monthly = findViewById(R.id.cv_monthly_MainActivity);
        cv_transactions = findViewById(R.id.cv_transactions_MainActivity);
        cv_incomes = findViewById(R.id.cv_income_MainActivity);
        cv_expenses = findViewById(R.id.cv_expenses_MainActivity);
        cv_incomesTopBar = findViewById(R.id.cv_totalIncomes_topBar_MainActivity);
        cv_expensesTopBar = findViewById(R.id.cv_totalExpenses_topBar_MainActivity);
        tv_totalIncome = findViewById(R.id.tv_totalIncomeValue_topBar_MainActivity);
        tv_totalExpense = findViewById(R.id.tv_totalExpenseValue_topBar_MainActivity);
        tv_balance = findViewById(R.id.tv_balanceValue_topBar_MainActivity);
        tv_currentBalance_toolbar = findViewById(R.id.tv_currentBalanceValue_toolBar_MainActivity);






        //**********************************************Initializations****************************************
        getSupportActionBar().hide();









    //*************************************************** Click Listeners****************************************
        cv_addIncome.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,AddIncome.class)));

        cv_addExpense.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,AddExpense.class)));

        cv_fullReport.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, FullReport.class)));

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


        cv_daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Daily.class));
            }
        });


        cv_monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MonthlyReport.class));
            }
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








        //**********************************************Starting Methods***************************************
        setIncomeExpenseValues();


    }

    private void setIncomeExpenseValues() {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(MainActivity.this);
        Cursor cursor = sqLiteHelper.loadAllTransactions();
        while (cursor.moveToNext()) {
            String postType = cursor.getString(3);
            int amount = Integer.parseInt(cursor.getString(4));

            if(postType.equals(Constants.TYPE_INCOME)){
                totalIncome = totalIncome+amount;
            }
            else if (postType.equals(Constants.TYPE_EXPENSE)){
                totalExpenses = totalExpenses+amount;
            }
        }

        UtilDB.currentBalance = totalIncome-totalExpenses;

        tv_totalIncome.setText(String.valueOf(totalIncome));
        tv_totalExpense.setText(String.valueOf(totalExpenses));
        tv_balance.setText(String.valueOf(UtilDB.currentBalance));
        tv_currentBalance_toolbar.setText(String.valueOf(UtilDB.currentBalance));
        cursor.close();
    }


    @Override
    public void onBackPressed() {
        confirmLogout();
    }



    private void confirmLogout() {
        Dialog dialog = new Dialog(MainActivity.this);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_exit,null);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();

        view.findViewById(R.id.btn_yes_alertImage_dialog_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
                finishAffinity();

            }
        });

        view.findViewById(R.id.btn_no_alertImage_dialog_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }













}