package com.example.dailyexpensemanager.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dailyexpensemanager.R;
import com.example.dailyexpensemanager.common.Constants;
import com.example.dailyexpensemanager.common.Extras;

public class Monthly extends AppCompatActivity {
    private Spinner spinnerYear,spinnerMonth,spinnerDay,spinnerCategory,spinnerType;
    private CardView cv_todayTransactions,cv_allTransactions,cv_home,cv_totalIncomes,cv_totalExpenses,cv_search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly);


        //***********************************************View Bindings**********************************************
        spinnerCategory = findViewById(R.id.spinner_catagory_Monthly);
        spinnerDay = findViewById(R.id.spinner_day_Monthly);
        spinnerYear = findViewById(R.id.spinner_year_Monthly);
        spinnerType = findViewById(R.id.spinner_type_Monthly);
        cv_home = findViewById(R.id.cv_Home_Monthly);
        cv_todayTransactions = findViewById(R.id.cv_todaysTransactions_Monthly);
        cv_allTransactions = findViewById(R.id.cv_transactions_Monthly);
        cv_totalIncomes = findViewById(R.id.cv_totalIncomes_Monthly);
        cv_totalExpenses = findViewById(R.id.cv_totalExpenses_Monthly);
        cv_search = findViewById(R.id.cv_search_Monthly);




        //***************************************************Initializations*****************************************






        //***************************************************Click Listeners*****************************************
        cv_home.setOnClickListener(v -> {
            startActivity(new Intent(Monthly.this,MainActivity.class));
        });

        cv_todayTransactions.setOnClickListener(v -> startActivity(new Intent(Monthly.this,MainActivity.class)));

        cv_allTransactions.setOnClickListener(v -> {
            Intent transactionsIntent = new Intent(Monthly.this,Transactions.class);
            transactionsIntent.putExtra(Extras.TYPE, Constants.TYPE_ALL);
            startActivity(transactionsIntent);
        });

        cv_totalIncomes.setOnClickListener(v -> {
            Intent incomeIntent = new Intent(Monthly.this,Transactions.class);
            incomeIntent.putExtra(Extras.TYPE, Constants.TYPE_INCOME);
            startActivity(incomeIntent);
        });

        cv_totalExpenses.setOnClickListener(v -> {
            Intent expenseIntent = new Intent(Monthly.this,Transactions.class);
            expenseIntent.putExtra(Extras.TYPE, Constants.TYPE_EXPENSE);
            startActivity(expenseIntent);
        });

        cv_search.setOnClickListener(v -> {
            Toast.makeText(this, "Searched Clicked", Toast.LENGTH_SHORT).show();
        });




        //****************************************************Starting Methods***************************************
        initMonthSpinner();
        initYearSpinner();
        initTypeSpinner();

    }





    private void initTypeSpinner() {
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.type));
        spinnerType.setAdapter(spinnerAdapter);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    initNullCategorySpinner();
                }

                else if(position==1){
                    initIncomeCategorySpinner();
                }

                else if(position==2){
                    initExpenseCategorySpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                initNullCategorySpinner();
            }
        });

    }





    private void initMonthSpinner() {
        spinnerMonth = findViewById(R.id.spinner_month_Monthly);
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.monthNames));
        spinnerMonth.setAdapter(spinnerAdapter);

        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    initDay0Spinner();
                }

                else if(position==1 ||position==3 || position==5 ||position==7 || position==8 || position==10 ||position==12){
                    initDay31Spinner();
                }

                else if(position==4 || position==6 || position==9 || position==11){
                    initDay30Spinner();
                }

                else if(position==2){
                    initDay28Spinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                initDay0Spinner();
            }
        });

    }





    private void initNullCategorySpinner() {
        spinnerCategory = findViewById(R.id.spinner_catagory_Monthly);
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.nullCategory));
        spinnerCategory.setAdapter(spinnerAdapter);
    }

    private void initExpenseCategorySpinner() {
        spinnerCategory = findViewById(R.id.spinner_catagory_Monthly);
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.expenseCategory));
        spinnerCategory.setAdapter(spinnerAdapter);

    }

    private void initIncomeCategorySpinner() {
        spinnerCategory = findViewById(R.id.spinner_catagory_Monthly);
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.incomeCategory));
        spinnerCategory.setAdapter(spinnerAdapter);
    }


    private void initDay0Spinner() {
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.dayNull));
        spinnerDay.setAdapter(spinnerAdapter);
    }

    private void initDay31Spinner() {
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.day31));
        spinnerDay.setAdapter(spinnerAdapter);
    }

    private void initDay30Spinner() {
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.day30));
        spinnerDay.setAdapter(spinnerAdapter);
    }

    private void initDay28Spinner() {
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.day28));
        spinnerDay.setAdapter(spinnerAdapter);
    }

    private void initYearSpinner() {

            ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.year));
            spinnerYear.setAdapter(spinnerAdapter);
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}