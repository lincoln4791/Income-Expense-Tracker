package com.lincoln4791.dailyexpensemanager.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.lincoln4791.dailyexpensemanager.R;
import com.lincoln4791.dailyexpensemanager.common.Extras;
import com.lincoln4791.dailyexpensemanager.common.UtilDB;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class PieChartActivity extends AppCompatActivity {
    PieChart pieChart_income,pieChart_expense;
    private TextView tv_salaryIncomePercent,tv_businessIncomePercent,tv_houseRentIncomePercent,tv_otherIncomePercent;
    private TextView tv_foodExpensePercent,tv_businessExpensePercent,tv_transportExpensePercent,tv_houseRentExpensePercent,tv_clothsExpensePercent,
            tv_billsExpensePercent,tv_educationExpensePercent,tv_medicineExpensePercent,tv_lifeStylePercent,tv_otherExpensePercent;
    private TextView tv_currentBalance_toolbar;
    private ImageView iv_home;

    private double totalIncome = 0, totalExpense = 0, balance, transportExpense = 0, foodExpense = 0, billsExpense = 0, houseRentExpense = 0, businessExpense = 0,
            medicineExpense = 0, clothsExpense = 0, educationExpense = 0, lifeStyleExpense = 0, otherExpense = 0, salaryIncome = 0, businessIncome = 0, houseRentIncome,
            otherIncome = 0;
    private double transportExpensePercent = 0, foodExpensePercent = 0, billsExpensePercent = 0, houseRentExpensePercent = 0, businessExpensePercent = 0,
            medicineExpensePercent = 0, clothsExpensePercent = 0, educationExpensePercent = 0, lifeStyleExpensePercent = 0, otherExpensePercent = 0, salaryIncomePercent = 0, businessIncomePercent = 0, houseRentIncomePercent,
            otherIncomePercent = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);



        //**********************************************************View Bindings********************************************
        pieChart_income = findViewById(R.id.pieChart_Income);
        pieChart_expense = findViewById(R.id.pieChart_Expense);
        tv_salaryIncomePercent = findViewById(R.id.tv_salaryIncomePercentValue_PieChart);
        tv_businessIncomePercent = findViewById(R.id.tv_businessIncomePercentValue_PieChart);
        tv_houseRentIncomePercent = findViewById(R.id.tv_houseRentIncomePercentValue_PieChart);
        tv_otherIncomePercent = findViewById(R.id.tv_otherIncomePercentValue_PieChart);

        tv_foodExpensePercent = findViewById(R.id.tv_foodExpensePercentValue_PieChart);
        tv_businessExpensePercent = findViewById(R.id.tv_businessExpensePercentValue_PieChart);
        tv_transportExpensePercent = findViewById(R.id.tv_transportExpensePercentValue_PieChart);
        tv_billsExpensePercent = findViewById(R.id.tv_billsExpensePercentValue_PieChart);
        tv_houseRentExpensePercent = findViewById(R.id.tv_houseRentExpensePercentValue_PieChart);
        tv_clothsExpensePercent = findViewById(R.id.tv_clothsExpensePercentValue_PieChart);
        tv_educationExpensePercent = findViewById(R.id.tv_educationExpensePercentValue_PieChart);
        tv_medicineExpensePercent = findViewById(R.id.tv_medicineExpensePercentValue_PieChart);
        tv_lifeStylePercent = findViewById(R.id.tv_lifeStyleExpensePercentValue_PieChart);
        tv_otherExpensePercent = findViewById(R.id.tv_otherExpensePercentValue_PieChart);
        tv_currentBalance_toolbar = findViewById(R.id.tv_currentBalanceValue_toolBar_PieChart);
        iv_home = findViewById(R.id.iv_home_toolbar_PieChart);





        //**********************************************************Initializations*****************************************
        getSupportActionBar().hide();
        getIntentData();
        setPieChart();





        //****************************************************** Click Listeners ******************************************
        iv_home.setOnClickListener(v -> {
            startActivity(new Intent(PieChartActivity.this, MainActivity.class));
        });





        //********************************************************Starting Methods**********************************************
        tv_currentBalance_toolbar.setText(String.valueOf(UtilDB.currentBalance));

    }






    private void getIntentData() {
          totalIncome = getIntent().getDoubleExtra(Extras.TOTAL_INCOME,0);
          salaryIncome = getIntent().getDoubleExtra(Extras.SALARY_INCOME,0);
          businessIncome = getIntent().getDoubleExtra(Extras.BUSINESS_INCOME,0);
          houseRentIncome = getIntent().getDoubleExtra(Extras.HOUSE_RENT_INCOME,0);
          otherIncome = getIntent().getDoubleExtra(Extras.OTHER_INCOME,0);

          totalExpense = getIntent().getDoubleExtra(Extras.TOTAL_EXPENSE,0);
          foodExpense = getIntent().getDoubleExtra(Extras.FOOD_EXPENSE,0);
          businessExpense = getIntent().getDoubleExtra(Extras.BUSINESS_EXPENSE,0);
          houseRentExpense = getIntent().getDoubleExtra(Extras.HOUSE_RENT_EXPENSE,0);
          billsExpense = getIntent().getDoubleExtra(Extras.BILLS_EXPENSE,0);
          transportExpense = getIntent().getDoubleExtra(Extras.TRANSPORT_EXPENSE,0);
          clothsExpense = getIntent().getDoubleExtra(Extras.CLOTHS_EXPENSE,0);
          medicineExpense = getIntent().getDoubleExtra(Extras.MEDICINE_EXPENSE,0);
          educationExpense = getIntent().getDoubleExtra(Extras.EDUCATION_EXPENSE,0);
          lifeStyleExpense = getIntent().getDoubleExtra(Extras.LIFESTYLE_EXPENSE,0);
          otherExpense = getIntent().getDoubleExtra(Extras.OTHER_EXPENSE,0);


        salaryIncomePercent = getIntent().getDoubleExtra(Extras.SALARY_INCOME_PERCENT,0);
        businessIncomePercent = getIntent().getDoubleExtra(Extras.BUSINESS_INCOME_PERCENT,0);
        houseRentIncomePercent = getIntent().getDoubleExtra(Extras.HOUSE_RENT_INCOME_PERCENT,0);
        otherIncomePercent = getIntent().getDoubleExtra(Extras.OTHER_INCOME_PERCENT,0);

        foodExpensePercent = getIntent().getDoubleExtra(Extras.FOOD_EXPENSE_PERCENT,0);
        businessExpensePercent = getIntent().getDoubleExtra(Extras.BUSINESS_EXPENSE_PERCENT,0);
        houseRentExpensePercent = getIntent().getDoubleExtra(Extras.HOUSE_RENT_EXPENSE_PERCENT,0);
        billsExpensePercent = getIntent().getDoubleExtra(Extras.BILLS_EXPENSE_PERCENT,0);
        transportExpensePercent = getIntent().getDoubleExtra(Extras.TRANSPORT_EXPENSE_PERCENT,0);
        clothsExpensePercent = getIntent().getDoubleExtra(Extras.CLOTHS_EXPENSE_PERCENT,0);
        medicineExpensePercent = getIntent().getDoubleExtra(Extras.MEDICINE_EXPENSE_PERCENT,0);
        educationExpensePercent = getIntent().getDoubleExtra(Extras.EDUCATION_EXPENSE_PERCENT,0);
        lifeStyleExpensePercent = getIntent().getDoubleExtra(Extras.LIFESTYLE_EXPENSE_PERCENT,0);
        otherExpensePercent = getIntent().getDoubleExtra(Extras.OTHER_EXPENSE_PERCENT,0);
    }








    private void setPieChart() {
        pieChart_income.addPieSlice(new PieModel(getString(R.string.Salary), (float) salaryIncome,getColor(R.color.salary)));
        pieChart_income.addPieSlice(new PieModel(getString(R.string.Business), (float) businessIncome,getColor(R.color.business)));
        pieChart_income.addPieSlice(new PieModel(getString(R.string.HouseRent), (float) houseRentIncome,getColor(R.color.houseRent)));
        pieChart_income.addPieSlice(new PieModel(getString(R.string.Other), (float) otherIncome,getColor(R.color.other)));

        pieChart_expense.addPieSlice(new PieModel(getString(R.string.Food), (float) foodExpense,getColor(R.color.food)));
        pieChart_expense.addPieSlice(new PieModel(getString(R.string.Business), (float) businessExpense,getColor(R.color.business)));
        pieChart_expense.addPieSlice(new PieModel(getString(R.string.Transport), (float) transportExpense,getColor(R.color.transport)));
        pieChart_expense.addPieSlice(new PieModel(getString(R.string.HouseRent), (float) houseRentExpense,getColor(R.color.houseRent)));
        pieChart_expense.addPieSlice(new PieModel(getString(R.string.Bills), (float) billsExpense,getColor(R.color.bills)));
        pieChart_expense.addPieSlice(new PieModel(getString(R.string.Cloths), (float) clothsExpense,getColor(R.color.cloths)));
        pieChart_expense.addPieSlice(new PieModel(getString(R.string.Education), (float) educationExpense,getColor(R.color.education)));
        pieChart_expense.addPieSlice(new PieModel(getString(R.string.Medicine), (float) medicineExpense,getColor(R.color.medicine)));
        pieChart_expense.addPieSlice(new PieModel(getString(R.string.LifeStyle), (float) lifeStyleExpense,getColor(R.color.lifestyle)));
        pieChart_expense.addPieSlice(new PieModel(getString(R.string.Other), (float) otherExpense,getColor(R.color.other)));

        tv_salaryIncomePercent.setText(" Salary "+salaryIncomePercent+"%");
        tv_businessIncomePercent.setText(" Business "+businessIncomePercent+"%");
        tv_houseRentIncomePercent.setText(" House Rent "+houseRentIncomePercent+"%");
        tv_otherIncomePercent.setText(" Other "+otherIncomePercent+"%");

        tv_foodExpensePercent.setText(" Food "+foodExpensePercent+"%");
        tv_businessExpensePercent.setText(" Business "+businessExpensePercent+"%");
        tv_houseRentExpensePercent.setText(" House Rent` "+houseRentExpensePercent+"%");
        tv_billsExpensePercent.setText(" Bills "+billsExpensePercent+"%");
        tv_transportExpensePercent.setText(" Transport "+transportExpensePercent+"%");
        tv_clothsExpensePercent.setText(" Cloths "+clothsExpensePercent+"%");
        tv_medicineExpensePercent.setText(" Medicine "+medicineExpensePercent+"%");
        tv_educationExpensePercent.setText(" Education "+educationExpensePercent+"%");
        tv_lifeStylePercent.setText(" Life Style "+lifeStyleExpensePercent+"%");
        tv_otherExpensePercent.setText(" Other "+otherExpensePercent+"%");

        pieChart_income.startAnimation();
        pieChart_expense.startAnimation();



    }





    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}