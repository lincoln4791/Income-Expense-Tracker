package com.example.dailyexpensemanager.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dailyexpensemanager.Adapters.Adapter_FullReport;
import com.example.dailyexpensemanager.R;
import com.example.dailyexpensemanager.common.Constants;
import com.example.dailyexpensemanager.common.Extras;
import com.example.dailyexpensemanager.common.SQLiteHelper;
import com.example.dailyexpensemanager.common.UtilDB;
import com.example.dailyexpensemanager.model.MC_Posts;
import com.example.dailyexpensemanager.viewModels.VM_FullReport;

public class FullReport extends AppCompatActivity {
    private Spinner spinnerYear, spinnerMonth, spinnerDay, spinnerCategory, spinnerType;
    private CardView cv_monthlyTransactions, cv_allTransactions, cv_home, cv_totalIncomes, cv_totalExpenses, cv_search;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

    private Adapter_FullReport adapterFullReport;
    private TextView tv_typeTitle,tv_incomeValue_salary,tv_incomeValue_business,tv_incomeValue_houseRent,tv_incomeValue_other,tv_totalIncomeValue;
    private TextView tv_expenseValue_food,tv_expenseValue_transport,tv_expenseValue_bills,tv_expenseValue_houseRent
                        ,tv_expenseValue_business,tv_expenseValue_medicine,tv_expenseValue_cloths,tv_expenseValue_education,
                        tv_expenseValue_lifestyle,tv_expenseValue_other,tv_totalExpenseValue;
    private TextView tv_currentBalance_toolbar;
    private ImageView iv_daily;

    private int totalIncome,totalExpense,balance,transportExpense,foodExpense,billsExpense,houseRentExpense,businessExpense,
                medicineExpense,clothsExpense,educationExpense,lifeStyleExpense,otherExpense,salaryIncome,businessIncome,houseRentIncome,otherIncome;
    private VM_FullReport vm_fullReport;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_report);


        //***********************************************View Bindings**********************************************
        spinnerCategory = findViewById(R.id.spinner_catagory_FullReport);
        spinnerDay = findViewById(R.id.spinner_day_FullReport);
        spinnerYear = findViewById(R.id.spinner_year_FullReport);
        spinnerType = findViewById(R.id.spinner_type_FullReport);
        cv_home = findViewById(R.id.cv_daily_FullReport);
        cv_monthlyTransactions = findViewById(R.id.cv_monthlyTransactions_FullReport);
        cv_allTransactions = findViewById(R.id.cv_transactions_FullReport);
        cv_totalIncomes = findViewById(R.id.cv_totalIncomes_FullReport);
        cv_totalExpenses = findViewById(R.id.cv_totalExpenses_FullReport);
        cv_search = findViewById(R.id.cv_search_FullReport);
        recyclerView = findViewById(R.id.rv_reportDetails_FullReport);
        tv_typeTitle = findViewById(R.id.tv_titleTransactions_FullReport);

        tv_incomeValue_salary = findViewById(R.id.tv_salaryIncomeValue_topBar_FullReport);
        tv_incomeValue_business = findViewById(R.id.tv_businessIncomeValue_topBar_FullReport);
        tv_incomeValue_houseRent = findViewById(R.id.tv_houseRentIncomeValue_topBar_FullReport);
        tv_incomeValue_other = findViewById(R.id.tv_otherIncomeValue_topBar_FullReport);
        tv_totalIncomeValue = findViewById(R.id.tv_totalIncomeValue_topBar_FullReport);

        tv_expenseValue_food = findViewById(R.id.tv_foodExpenseValue_topBar_FullReport);
        tv_expenseValue_transport = findViewById(R.id.tv_transportExpenseValue_topBar_FullReport);
        tv_expenseValue_bills = findViewById(R.id.tv_billsExpenseValue_topBar_FullReport);
        tv_expenseValue_houseRent = findViewById(R.id.tv_houseRentExpenseValue_topBar_FullReport);
        tv_expenseValue_business = findViewById(R.id.tv_businessExpenseValue_topBar_FullReport);
        tv_expenseValue_medicine = findViewById(R.id.tv_medicineExpensesValue_topBar_FullReport);
        tv_expenseValue_cloths = findViewById(R.id.tv_clothsExpensesValue_topBar_FullReport);
        tv_expenseValue_education = findViewById(R.id.tv_educationExpensesValue_topBar_FullReport);
        tv_expenseValue_lifestyle = findViewById(R.id.tv_lifeStyleExpensesValue_topBar_FullReport);
        tv_expenseValue_other = findViewById(R.id.tv_otherExpensesValue_topBar_FullReport);
        tv_totalExpenseValue = findViewById(R.id.tv_totalExpenseValue_topBar_FullReport);

        tv_currentBalance_toolbar = findViewById(R.id.tv_currentBalanceValue_toolBar_FullReport);
        iv_daily = findViewById(R.id.iv_home_toolbar_FullReport);




        //***************************************************Initializations*****************************************
        getSupportActionBar().hide();
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        vm_fullReport = ViewModelProviders.of(this).get(VM_FullReport.class);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterFullReport = new Adapter_FullReport(FullReport.this, vm_fullReport.postsList);



        //***************************************************Click Listeners*****************************************
        cv_home.setOnClickListener(v -> {
            startActivity(new Intent(FullReport.this, MainActivity.class));
        });

        cv_monthlyTransactions.setOnClickListener(v -> startActivity(new Intent(FullReport.this, MonthlyReport.class)));

        cv_allTransactions.setOnClickListener(v -> {
            Intent transactionsIntent = new Intent(FullReport.this, Transactions.class);
            transactionsIntent.putExtra(Extras.TYPE, Constants.TYPE_ALL);
            startActivity(transactionsIntent);
        });

        cv_totalIncomes.setOnClickListener(v -> {
            Intent incomeIntent = new Intent(FullReport.this, Transactions.class);
            incomeIntent.putExtra(Extras.TYPE, Constants.TYPE_INCOME);
            startActivity(incomeIntent);
        });

        cv_totalExpenses.setOnClickListener(v -> {
            Intent expenseIntent = new Intent(FullReport.this, Transactions.class);
            expenseIntent.putExtra(Extras.TYPE, Constants.TYPE_EXPENSE);
            startActivity(expenseIntent);
        });

        cv_search.setOnClickListener(v -> {
            selectQuery();
        });

        iv_daily.setOnClickListener(v -> {
            startActivity(new Intent(FullReport.this,Daily.class));
        });


        //****************************************************Starting Methods***************************************
        tv_currentBalance_toolbar.setText(String.valueOf(UtilDB.currentBalance));
        initMonthSpinner();
        initYearSpinner();
        initTypeSpinner();

    }


    private void selectQuery() {

        if(!vm_fullReport.month.equals(Constants.MONTH_NULL) &&
                !vm_fullReport.day.equals(Constants.DAY_NULL) && !vm_fullReport.type.equals(Constants.TYPE_ALL) &&
                        !vm_fullReport.category.equals(Constants.CATEGORY_NULL)){
            Log.d("Tag","1)  year"+vm_fullReport.year+"month"+vm_fullReport.month+"day"+vm_fullReport.day+" type "+vm_fullReport.type+" category "+vm_fullReport.category);
            new YearMonthDayTypeCategoryTask().execute();
        }

        else if(!vm_fullReport.month.equals(Constants.MONTH_NULL) &&
                !vm_fullReport.type.equals(Constants.TYPE_ALL) &&
                !vm_fullReport.category.equals(Constants.CATEGORY_NULL) && vm_fullReport.day.equals(Constants.DAY_NULL)){
            Log.d("Tag","2)  year"+vm_fullReport.year+"month"+vm_fullReport.month+"day"+vm_fullReport.day+" type "+vm_fullReport.type+" category "+vm_fullReport.category);
            new YearMonthTypeCategoryTask().execute();
        }

        else if(!vm_fullReport.month.equals(Constants.MONTH_NULL) &&
                !vm_fullReport.day.equals(Constants.DAY_NULL) &&
                !vm_fullReport.category.equals(Constants.CATEGORY_NULL) && vm_fullReport.type.equals(Constants.TYPE_ALL)){
            Log.d("Tag","3)  year"+vm_fullReport.year+"month"+vm_fullReport.month+"day"+vm_fullReport.day+" type "+vm_fullReport.type+" category "+vm_fullReport.category);
            new YearMonthDayCategoryTask().execute();
        }

        else if(!vm_fullReport.month.equals(Constants.MONTH_NULL) &&
                !vm_fullReport.day.equals(Constants.DAY_NULL) &&
                !vm_fullReport.type.equals(Constants.TYPE_ALL) && vm_fullReport.category.equals(Constants.CATEGORY_NULL)){
            Log.d("Tag","4)  year"+vm_fullReport.year+"month"+vm_fullReport.month+"day"+vm_fullReport.day+" type "+vm_fullReport.type+" category "+vm_fullReport.category);
            new YearMonthDayTypeTask().execute();
        }


        else if(!vm_fullReport.month.equals(Constants.MONTH_NULL) &&
                !vm_fullReport.day.equals(Constants.DAY_NULL) && vm_fullReport.type.equals(Constants.TYPE_ALL) && vm_fullReport.category.equals(Constants.CATEGORY_NULL)){
            Log.d("Tag","5)  year"+vm_fullReport.year+"month"+vm_fullReport.month+"day"+vm_fullReport.day+" type "+vm_fullReport.type+" category "+vm_fullReport.category);
            new YearMonthDayTask().execute();
        }

        else if(!vm_fullReport.month.equals(Constants.MONTH_NULL) &&
                !vm_fullReport.type.equals(Constants.TYPE_ALL) && vm_fullReport.day.equals(Constants.DAY_NULL) && vm_fullReport.category.equals(Constants.CATEGORY_NULL)){
            Log.d("Tag","6)  year"+vm_fullReport.year+"month"+vm_fullReport.month+"day"+vm_fullReport.day+" type "+vm_fullReport.type+" category "+vm_fullReport.category);
            new YearMonthTypeTask().execute();
        }

        else if(!vm_fullReport.month.equals(Constants.MONTH_NULL) &&
                !vm_fullReport.category.equals(Constants.CATEGORY_NULL) && vm_fullReport.day.equals(Constants.DAY_NULL) && vm_fullReport.type.equals(Constants.TYPE_ALL)){
            Log.d("Tag","7)  year"+vm_fullReport.year+"month"+vm_fullReport.month+"day"+vm_fullReport.day+" type "+vm_fullReport.type+" category "+vm_fullReport.category);
            new YearMonthCategoryTask().execute();
        }

        else if(!vm_fullReport.month.equals(Constants.MONTH_NULL) && vm_fullReport.day.equals(Constants.DAY_NULL) && vm_fullReport.type.equals(Constants.TYPE_ALL) &&
                vm_fullReport.category.equals(Constants.CATEGORY_NULL)){
            Log.d("Tag","8)  year"+vm_fullReport.year+"month"+vm_fullReport.month+"day"+vm_fullReport.day+" type "+vm_fullReport.type+" category "+vm_fullReport.category);
            new YearMonthTask().execute();
        }

        else if(!vm_fullReport.type.equals(Constants.TYPE_ALL) && vm_fullReport.month.equals(Constants.MONTH_NULL) && vm_fullReport.day.equals(Constants.DAY_NULL) &&
                vm_fullReport.category.equals(Constants.CATEGORY_NULL)){
            Log.d("Tag","9)  year"+vm_fullReport.year+"month"+vm_fullReport.month+"day"+vm_fullReport.day+" type "+vm_fullReport.type+" category "+vm_fullReport.category);
            new YearTypeTask().execute();
        }

        else if(!vm_fullReport.type.equals(Constants.TYPE_ALL) && !vm_fullReport.category.equals(Constants.CATEGORY_NULL) && vm_fullReport.day.equals(Constants.DAY_NULL) &&
                vm_fullReport.month.equals(Constants.MONTH_NULL)){
            Log.d("Tag","12)  year"+vm_fullReport.year+"month"+vm_fullReport.month+"day"+vm_fullReport.day+" type "+vm_fullReport.type+" category "+vm_fullReport.category);
            new YearTypeCategoryTask().execute();
        }

        else if(!vm_fullReport.category.equals(Constants.CATEGORY_NULL) && vm_fullReport.month.equals(Constants.MONTH_NULL) && vm_fullReport.day.equals(Constants.DAY_NULL) &&
                vm_fullReport.type.equals(Constants.TYPE_ALL)){
            Log.d("Tag","10)  year"+vm_fullReport.year+"month"+vm_fullReport.month+"day"+vm_fullReport.day+" type "+vm_fullReport.type+" category "+vm_fullReport.category);
            new YearMonthTask().execute();
        }
        else{
            Log.d("Tag","11)  year"+vm_fullReport.year+"month"+vm_fullReport.month+"day"+vm_fullReport.day+" type "+vm_fullReport.type+" category "+vm_fullReport.category);
            new YearTask().execute();
        }

    }


    private void initTypeSpinner() {
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.type));
        spinnerType.setAdapter(spinnerAdapter);


        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    vm_fullReport.type = Constants.TYPE_ALL;
                    vm_fullReport.category = Constants.CATEGORY_NULL;
                    initNullCategorySpinner();
                } else if (position == 1) {
                    vm_fullReport.type = Constants.TYPE_INCOME;
                    initIncomeCategorySpinner();
                } else if (position == 2) {
                    vm_fullReport.type = Constants.TYPE_EXPENSE;
                    initExpenseCategorySpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vm_fullReport.type = Constants.TYPE_ALL;
                vm_fullReport.category = Constants.CATEGORY_NULL;
                initNullCategorySpinner();
            }
        });

    }


    private void initMonthSpinner() {
        spinnerMonth = findViewById(R.id.spinner_month_FullReport);
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.monthNames));
        spinnerMonth.setAdapter(spinnerAdapter);

        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    vm_fullReport.month = Constants.MONTH_NULL;
                    vm_fullReport.day = Constants.DAY_NULL;
                    initDay0Spinner();
                }
                else if (position == 1 || position == 3 || position == 5 || position == 7 || position == 8 || position == 10 || position == 12) {
                    if (position == 1) {
                        vm_fullReport.month = getString(R.string.digit01);
                    } else if (position == 3) {
                        vm_fullReport.month = getString(R.string.digit03);
                    } else if (position == 5) {
                        vm_fullReport.month = getString(R.string.digit05);
                    } else if (position == 7) {
                        vm_fullReport.month = getString(R.string.digit07);
                    } else if (position == 8) {
                        vm_fullReport.month = getString(R.string.digit08);
                    } else if (position == 10) {
                        vm_fullReport.month = getString(R.string.digit10);
                    } else if (position == 12) {
                        vm_fullReport.month = getString(R.string.digit12);
                    }

                    initDay31Spinner();
                } else if (position == 4 || position == 6 || position == 9 || position == 11) {
                    if (position == 4) {
                        vm_fullReport.month = getString(R.string.digit04);
                    } else if (position == 6) {
                        vm_fullReport.month = getString(R.string.digit06);
                    } else if (position == 9) {
                        vm_fullReport.month = getString(R.string.digit09);
                    }

                    if (position == 11) {
                        vm_fullReport.month = getString(R.string.digit11);
                    }
                    initDay30Spinner();
                } else if (position == 2) {
                    vm_fullReport.month = getString(R.string.digit02);
                    initDay28Spinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vm_fullReport.month = Constants.MONTH_NULL;
                vm_fullReport.day = Constants.DAY_NULL;
                initDay0Spinner();
            }
        });

    }


    private void initNullCategorySpinner() {
        spinnerCategory = findViewById(R.id.spinner_catagory_FullReport);
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.nullCategory));
        spinnerCategory.setAdapter(spinnerAdapter);

    }

    private void initExpenseCategorySpinner() {
        spinnerCategory = findViewById(R.id.spinner_catagory_FullReport);
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.expenseCategory));
        spinnerCategory.setAdapter(spinnerAdapter);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    vm_fullReport.category = Constants.CATEGORY_NULL;
                } else if (position == 1) {
                    vm_fullReport.category = Constants.CATEGORY_FOOD;
                } else if (position == 2) {
                    vm_fullReport.category = Constants.CATEGORY_TRANSPORT;
                } else if (position == 3) {
                    vm_fullReport.category = Constants.CATEGORY_HOUSE_RENT;
                } else if (position == 4) {
                    vm_fullReport.category = Constants.CATEGORY_BUSINESS;
                } else if (position == 5) {
                    vm_fullReport.category = Constants.CATEGORY_MEDICINE;
                } else if (position == 6) {
                    vm_fullReport.category = Constants.CATEGORY_CLOTHS;
                } else if (position == 7) {
                    vm_fullReport.category = Constants.CATEGORY_EDUCATION;
                } else if (position == 8) {
                    vm_fullReport.category = Constants.CATEGORY_LIFESTYLE;
                } else if (position == 9) {
                    vm_fullReport.category = Constants.CATEGORY_BILLS;
                } else if (position == 10) {
                    vm_fullReport.category = Constants.CATEGORY_OTHER;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vm_fullReport.category = Constants.CATEGORY_NULL;
            }
        });


    }

    private void initIncomeCategorySpinner() {
        spinnerCategory = findViewById(R.id.spinner_catagory_FullReport);
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.incomeCategory));
        spinnerCategory.setAdapter(spinnerAdapter);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    vm_fullReport.category = Constants.CATEGORY_NULL;
                } else if (position == 1) {
                    vm_fullReport.category = Constants.CATEGORY_SALARY;
                } else if (position == 2) {
                    vm_fullReport.category = Constants.CATEGORY_BUSINESS;
                } else if (position == 3) {
                    vm_fullReport.category = Constants.CATEGORY_HOUSE_RENT;
                } else if (position == 4) {
                    vm_fullReport.category = Constants.CATEGORY_OTHER;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void initDay0Spinner() {
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.dayNull));
        spinnerDay.setAdapter(spinnerAdapter);
    }

    private void initDay31Spinner() {
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.day31));
        spinnerDay.setAdapter(spinnerAdapter);

        spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    vm_fullReport.day = Constants.DAY_NULL;
                } else if (position == 1) {
                    vm_fullReport.day = getString(R.string.digit01);
                } else if (position == 2) {
                    vm_fullReport.day = getString(R.string.digit02);
                } else if (position == 3) {
                    vm_fullReport.day = getString(R.string.digit03);
                } else if (position == 4) {
                    vm_fullReport.day = getString(R.string.digit04);
                } else if (position == 5) {
                    vm_fullReport.day = getString(R.string.digit05);
                } else if (position == 6) {
                    vm_fullReport.day = getString(R.string.digit06);
                } else if (position == 7) {
                    vm_fullReport.day = getString(R.string.digit07);
                } else if (position == 8) {
                    vm_fullReport.day = getString(R.string.digit08);
                } else if (position == 9) {
                    vm_fullReport.day = getString(R.string.digit09);
                } else if (position == 10) {
                    vm_fullReport.day = getString(R.string.digit10);
                } else if (position == 11) {
                    vm_fullReport.day = getString(R.string.digit11);
                } else if (position == 12) {
                    vm_fullReport.day = getString(R.string.digit12);
                } else if (position == 13) {
                    vm_fullReport.day = getString(R.string.digit13);
                } else if (position == 14) {
                    vm_fullReport.day = getString(R.string.digit14);
                } else if (position == 15) {
                    vm_fullReport.day = getString(R.string.digit15);
                } else if (position == 16) {
                    vm_fullReport.day = getString(R.string.digit16);
                } else if (position == 17) {
                    vm_fullReport.day = getString(R.string.digit17);
                } else if (position == 18) {
                    vm_fullReport.day = getString(R.string.digit18);
                } else if (position == 19) {
                    vm_fullReport.day = getString(R.string.digit19);
                } else if (position == 20) {
                    vm_fullReport.day = getString(R.string.digit20);
                } else if (position == 21) {
                    vm_fullReport.day = getString(R.string.digit21);
                } else if (position == 22) {
                    vm_fullReport.day = getString(R.string.digit22);
                } else if (position == 23) {
                    vm_fullReport.day = getString(R.string.digit23);
                } else if (position == 24) {
                    vm_fullReport.day = getString(R.string.digit24);
                } else if (position == 25) {
                    vm_fullReport.day = getString(R.string.digit25);
                } else if (position == 26) {
                    vm_fullReport.day = getString(R.string.digit26);
                } else if (position == 27) {
                    vm_fullReport.day = getString(R.string.digit27);
                } else if (position == 28) {
                    vm_fullReport.day = getString(R.string.digit28);
                } else if (position == 29) {
                    vm_fullReport.day = getString(R.string.digit29);
                } else if (position == 30) {
                    vm_fullReport.day = getString(R.string.digit30);
                } else if (position == 31) {
                    vm_fullReport.day = getString(R.string.digit31);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vm_fullReport.day = Constants.DAY_NULL;
            }
        });
    }

    private void initDay30Spinner() {
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.day30));
        spinnerDay.setAdapter(spinnerAdapter);

        spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    vm_fullReport.day = Constants.DAY_NULL;
                } else if (position == 1) {
                    vm_fullReport.day = getString(R.string.digit01);
                } else if (position == 2) {
                    vm_fullReport.day = getString(R.string.digit02);
                } else if (position == 3) {
                    vm_fullReport.day = getString(R.string.digit03);
                } else if (position == 4) {
                    vm_fullReport.day = getString(R.string.digit04);
                } else if (position == 5) {
                    vm_fullReport.day = getString(R.string.digit05);
                } else if (position == 6) {
                    vm_fullReport.day = getString(R.string.digit06);
                } else if (position == 7) {
                    vm_fullReport.day = getString(R.string.digit07);
                } else if (position == 8) {
                    vm_fullReport.day = getString(R.string.digit08);
                } else if (position == 9) {
                    vm_fullReport.day = getString(R.string.digit09);
                } else if (position == 10) {
                    vm_fullReport.day = getString(R.string.digit10);
                } else if (position == 11) {
                    vm_fullReport.day = getString(R.string.digit11);
                } else if (position == 12) {
                    vm_fullReport.day = getString(R.string.digit12);
                } else if (position == 13) {
                    vm_fullReport.day = getString(R.string.digit13);
                } else if (position == 14) {
                    vm_fullReport.day = getString(R.string.digit14);
                } else if (position == 15) {
                    vm_fullReport.day = getString(R.string.digit15);
                } else if (position == 16) {
                    vm_fullReport.day = getString(R.string.digit16);
                } else if (position == 17) {
                    vm_fullReport.day = getString(R.string.digit17);
                } else if (position == 18) {
                    vm_fullReport.day = getString(R.string.digit18);
                } else if (position == 19) {
                    vm_fullReport.day = getString(R.string.digit19);
                } else if (position == 20) {
                    vm_fullReport.day = getString(R.string.digit20);
                } else if (position == 21) {
                    vm_fullReport.day = getString(R.string.digit21);
                } else if (position == 22) {
                    vm_fullReport.day = getString(R.string.digit22);
                } else if (position == 23) {
                    vm_fullReport.day = getString(R.string.digit23);
                } else if (position == 24) {
                    vm_fullReport.day = getString(R.string.digit24);
                } else if (position == 25) {
                    vm_fullReport.day = getString(R.string.digit25);
                } else if (position == 26) {
                    vm_fullReport.day = getString(R.string.digit26);
                } else if (position == 27) {
                    vm_fullReport.day = getString(R.string.digit27);
                } else if (position == 28) {
                    vm_fullReport.day = getString(R.string.digit28);
                } else if (position == 29) {
                    vm_fullReport.day = getString(R.string.digit29);
                } else if (position == 30) {
                    vm_fullReport.day = getString(R.string.digit30);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vm_fullReport.day = Constants.DAY_NULL;
            }
        });

    }

    private void initDay28Spinner() {
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.day28));
        spinnerDay.setAdapter(spinnerAdapter);

        spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    vm_fullReport.day = Constants.DAY_NULL;
                } else if (position == 1) {
                    vm_fullReport.day = getString(R.string.digit01);
                } else if (position == 2) {
                    vm_fullReport.day = getString(R.string.digit02);
                } else if (position == 3) {
                    vm_fullReport.day = getString(R.string.digit03);
                } else if (position == 4) {
                    vm_fullReport.day = getString(R.string.digit04);
                } else if (position == 5) {
                    vm_fullReport.day = getString(R.string.digit05);
                } else if (position == 6) {
                    vm_fullReport.day = getString(R.string.digit06);
                } else if (position == 7) {
                    vm_fullReport.day = getString(R.string.digit07);
                } else if (position == 8) {
                    vm_fullReport.day = getString(R.string.digit08);
                } else if (position == 9) {
                    vm_fullReport.day = getString(R.string.digit09);
                } else if (position == 10) {
                    vm_fullReport.day = getString(R.string.digit10);
                } else if (position == 11) {
                    vm_fullReport.day = getString(R.string.digit11);
                } else if (position == 12) {
                    vm_fullReport.day = getString(R.string.digit12);
                } else if (position == 13) {
                    vm_fullReport.day = getString(R.string.digit13);
                } else if (position == 14) {
                    vm_fullReport.day = getString(R.string.digit14);
                } else if (position == 15) {
                    vm_fullReport.day = getString(R.string.digit15);
                } else if (position == 16) {
                    vm_fullReport.day = getString(R.string.digit16);
                } else if (position == 17) {
                    vm_fullReport.day = getString(R.string.digit17);
                } else if (position == 18) {
                    vm_fullReport.day = getString(R.string.digit18);
                } else if (position == 19) {
                    vm_fullReport.day = getString(R.string.digit19);
                } else if (position == 20) {
                    vm_fullReport.day = getString(R.string.digit20);
                } else if (position == 21) {
                    vm_fullReport.day = getString(R.string.digit21);
                } else if (position == 22) {
                    vm_fullReport.day = getString(R.string.digit22);
                } else if (position == 23) {
                    vm_fullReport.day = getString(R.string.digit23);
                } else if (position == 24) {
                    vm_fullReport.day = getString(R.string.digit24);
                } else if (position == 25) {
                    vm_fullReport.day = getString(R.string.digit25);
                } else if (position == 26) {
                    vm_fullReport.day = getString(R.string.digit26);
                } else if (position == 27) {
                    vm_fullReport.day = getString(R.string.digit27);
                } else if (position == 28) {
                    vm_fullReport.day = getString(R.string.digit28);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vm_fullReport.day = Constants.DAY_NULL;
            }
        });

    }

    private void initYearSpinner() {

        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.year));
        spinnerYear.setAdapter(spinnerAdapter);

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    vm_fullReport.year = Constants.YEAR_DEFAULT;
                } else if (position == 1) {
                    vm_fullReport.year = "2022";
                } else if (position == 2) {
                    vm_fullReport.year = "2023";
                } else if (position == 3) {
                    vm_fullReport.year = "2024";
                } else if (position == 4) {
                    vm_fullReport.year = "2025";
                } else if (position == 5) {
                    vm_fullReport.year = "2026";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vm_fullReport.year = Constants.YEAR_DEFAULT;
            }
        });

    }


    class AllTransactionsTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            fetchAllTransactions();
            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            recyclerView.setAdapter(adapterFullReport);
            adapterFullReport.notifyDataSetChanged();
            setCalculatedValue();

        }
    }


    class AllExpensesTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            fetchAllExpenses();
            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            recyclerView.setAdapter(adapterFullReport);
            adapterFullReport.notifyDataSetChanged();
            setCalculatedValue();

        }
    }


    class AllIncomeTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            fetchAllIncomes();
            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            recyclerView.setAdapter(adapterFullReport);
            adapterFullReport.notifyDataSetChanged();
            setCalculatedValue();
        }
    }


    class YearTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            fetchYearWise();
            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            recyclerView.setAdapter(adapterFullReport);
            adapterFullReport.notifyDataSetChanged();
            setCalculatedValue();
        }
    }


    class YearTypeTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            fetchYearTypeWise();
            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            recyclerView.setAdapter(adapterFullReport);
            adapterFullReport.notifyDataSetChanged();
            setCalculatedValue();
        }
    }


    class YearCategoryTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            fetchYearCategoryWise();
            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            //Collections.reverse(vm_fullReport.postsList);
            recyclerView.setAdapter(adapterFullReport);
            adapterFullReport.notifyDataSetChanged();
            setCalculatedValue();
        }
    }


    class YearTypeCategoryTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            fetchYearTypeCategoryWise();
            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            //Collections.reverse(vm_fullReport.postsList);
            recyclerView.setAdapter(adapterFullReport);
            adapterFullReport.notifyDataSetChanged();
            setCalculatedValue();
        }
    }


    class YearMonthTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            fetchYearMonthWise();
            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            //Collections.reverse(vm_fullReport.postsList);
            recyclerView.setAdapter(adapterFullReport);
            adapterFullReport.notifyDataSetChanged();
            setCalculatedValue();
        }
    }


    class YearMonthTypeTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            fetchYearMonthTypeWise();
            return objects;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            //Collections.reverse(vm_fullReport.postsList);
            recyclerView.setAdapter(adapterFullReport);
            adapterFullReport.notifyDataSetChanged();
            setCalculatedValue();
        }
    }


    class YearMonthCategoryTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            fetchYearMonthCategoryWise();
            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            //Collections.reverse(vm_fullReport.postsList);
            recyclerView.setAdapter(adapterFullReport);
            adapterFullReport.notifyDataSetChanged();
            setCalculatedValue();
        }
    }


    class YearMonthTypeCategoryTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            fetchYearMonthTypeCategoryWise();
            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            //Collections.reverse(vm_fullReport.postsList);
            recyclerView.setAdapter(adapterFullReport);
            adapterFullReport.notifyDataSetChanged();
            setCalculatedValue();
        }
    }


    class YearMonthDayTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            fetchYearMonthDayWise();
            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            //Collections.reverse(vm_fullReport.postsList);
            recyclerView.setAdapter(adapterFullReport);
            adapterFullReport.notifyDataSetChanged();
            setCalculatedValue();
        }
    }


    class YearMonthDayTypeTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            fetchYearMonthDayTypeWise();
            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            //Collections.reverse(vm_fullReport.postsList);
            recyclerView.setAdapter(adapterFullReport);
            adapterFullReport.notifyDataSetChanged();
            setCalculatedValue();
        }
    }


    class YearMonthDayCategoryTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            fetchYearMonthDayCategoryWise();
            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            //Collections.reverse(vm_fullReport.postsList);
            recyclerView.setAdapter(adapterFullReport);
            adapterFullReport.notifyDataSetChanged();
            setCalculatedValue();
        }
    }


    class YearMonthDayTypeCategoryTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            fetchYearMonthDayTypeCategoryWise();
            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            //Collections.reverse(vm_fullReport.postsList);
            recyclerView.setAdapter(adapterFullReport);
            adapterFullReport.notifyDataSetChanged();
            setCalculatedValue();
        }
    }


    private void fetchAllTransactions() {
        vm_fullReport.postsList.clear();
        //tv_typeTitle.setText(getString(R.string.Transactions));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadAllTransactions();

        while (cursor.moveToNext()) {
            String postDescription = cursor.getString(1);
            String postCategory = cursor.getString(2);
            String postType = cursor.getString(3);
            String postAmount = cursor.getString(4);
            String postTime = cursor.getString(5);
            String postDay = cursor.getString(6);
            String postMonth = cursor.getString(7);
            String postYear = cursor.getString(8);
            String postDateTime = cursor.getString(9);
            String timeStamp = cursor.getString(10);

            MC_Posts post = new MC_Posts(postDescription, postCategory, postType, postAmount, postYear, postMonth, postDay, postTime, timeStamp, postDateTime);
            vm_fullReport.postsList.add(post);
        }
        cursor.close();
        calculateAll();

    }


    private void fetchAllExpenses() {
        vm_fullReport.postsList.clear();
        //tv_typeTitle.setText(getString(R.string.Expenses));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadTypeWise(Constants.TYPE_EXPENSE);

        while (cursor.moveToNext()) {
            String postDescription = cursor.getString(1);
            String postCategory = cursor.getString(2);
            String postType = cursor.getString(3);
            String postAmount = cursor.getString(4);
            String postTime = cursor.getString(5);
            String postDay = cursor.getString(6);
            String postMonth = cursor.getString(7);
            String postYear = cursor.getString(8);
            String postDateTime = cursor.getString(9);
            String timeStamp = cursor.getString(10);

            MC_Posts post = new MC_Posts(postDescription, postCategory, postType, postAmount, postYear, postMonth, postDay, postTime, timeStamp, postDateTime);
            vm_fullReport.postsList.add(post);
        }

        cursor.close();
        calculateAll();

    }


    private void fetchAllIncomes() {
        vm_fullReport.postsList.clear();
        //tv_typeTitle.setText(getString(R.string.Incomes));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadTypeWise(Constants.TYPE_INCOME);

        while (cursor.moveToNext()) {
            String postDescription = cursor.getString(1);
            String postCategory = cursor.getString(2);
            String postType = cursor.getString(3);
            String postAmount = cursor.getString(4);
            String postTime = cursor.getString(5);
            String postDay = cursor.getString(6);
            String postMonth = cursor.getString(7);
            String postYear = cursor.getString(8);
            String postDateTime = cursor.getString(9);
            String timeStamp = cursor.getString(10);
            MC_Posts post = new MC_Posts(postDescription, postCategory, postType, postAmount, postYear, postMonth, postDay, postTime, timeStamp, postDateTime);
            vm_fullReport.postsList.add(post);
        }

        cursor.close();
        calculateAll();
    }


    private void fetchYearWise() {
        vm_fullReport.postsList.clear();
        //tv_typeTitle.setText(getString(R.string.YearWise));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadYearWise(vm_fullReport.year);

        while (cursor.moveToNext()) {
            String postDescription = cursor.getString(1);
            String postCategory = cursor.getString(2);
            String postType = cursor.getString(3);
            String postAmount = cursor.getString(4);
            String postTime = cursor.getString(5);
            String postDay = cursor.getString(6);
            String postMonth = cursor.getString(7);
            String postYear = cursor.getString(8);
            String postDateTime = cursor.getString(9);
            String timeStamp = cursor.getString(10);

            MC_Posts post = new MC_Posts(postDescription, postCategory, postType, postAmount, postYear, postMonth, postDay, postTime, timeStamp, postDateTime);
            vm_fullReport.postsList.add(post);
        }

        cursor.close();
        calculateAll();
    }


    private void fetchYearTypeWise() {
        vm_fullReport.postsList.clear();
        //tv_typeTitle.setText(getString(R.string.Category));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadYearTypeWise(vm_fullReport.year, vm_fullReport.type);

        while (cursor.moveToNext()) {
            String postDescription = cursor.getString(1);
            String postCategory = cursor.getString(2);
            String postType = cursor.getString(3);
            String postAmount = cursor.getString(4);
            String postTime = cursor.getString(5);
            String postDay = cursor.getString(6);
            String postMonth = cursor.getString(7);
            String postYear = cursor.getString(8);
            String postDateTime = cursor.getString(9);
            String timeStamp = cursor.getString(10);

            MC_Posts post = new MC_Posts(postDescription, postCategory, postType, postAmount, postYear, postMonth, postDay, postTime, timeStamp, postDateTime);
            vm_fullReport.postsList.add(post);
        }

        cursor.close();
        calculateAll();
    }


    private void fetchYearCategoryWise() {
        vm_fullReport.postsList.clear();
        //tv_typeTitle.setText(getString(R.string.Category));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadYearCategoryWise(vm_fullReport.year, vm_fullReport.category);

        while (cursor.moveToNext()) {
            String postDescription = cursor.getString(1);
            String postCategory = cursor.getString(2);
            String postType = cursor.getString(3);
            String postAmount = cursor.getString(4);
            String postTime = cursor.getString(5);
            String postDay = cursor.getString(6);
            String postMonth = cursor.getString(7);
            String postYear = cursor.getString(8);
            String postDateTime = cursor.getString(9);
            String timeStamp = cursor.getString(10);

            MC_Posts post = new MC_Posts(postDescription, postCategory, postType, postAmount, postYear, postMonth, postDay, postTime, timeStamp, postDateTime);
            vm_fullReport.postsList.add(post);
        }

        cursor.close();
        calculateAll();
    }


    private void fetchYearTypeCategoryWise() {
        vm_fullReport.postsList.clear();
        //tv_typeTitle.setText(getString(R.string.Category));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadYearTypeCategoryWise(vm_fullReport.year, vm_fullReport.type, vm_fullReport.category);

        while (cursor.moveToNext()) {
            String postDescription = cursor.getString(1);
            String postCategory = cursor.getString(2);
            String postType = cursor.getString(3);
            String postAmount = cursor.getString(4);
            String postTime = cursor.getString(5);
            String postDay = cursor.getString(6);
            String postMonth = cursor.getString(7);
            String postYear = cursor.getString(8);
            String postDateTime = cursor.getString(9);
            String timeStamp = cursor.getString(10);

            MC_Posts post = new MC_Posts(postDescription, postCategory, postType, postAmount, postYear, postMonth, postDay, postTime, timeStamp, postDateTime);
            vm_fullReport.postsList.add(post);
        }

        cursor.close();
        calculateAll();
    }


    private void fetchYearMonthWise() {
        vm_fullReport.postsList.clear();
        //tv_typeTitle.setText(getString(R.string.Category));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadYearMonthWise(vm_fullReport.year, vm_fullReport.month);

        while (cursor.moveToNext()) {
            String postDescription = cursor.getString(1);
            String postCategory = cursor.getString(2);
            String postType = cursor.getString(3);
            String postAmount = cursor.getString(4);
            String postTime = cursor.getString(5);
            String postDay = cursor.getString(6);
            String postMonth = cursor.getString(7);
            String postYear = cursor.getString(8);
            String postDateTime = cursor.getString(9);
            String timeStamp = cursor.getString(10);

            MC_Posts post = new MC_Posts(postDescription, postCategory, postType, postAmount, postYear, postMonth, postDay, postTime, timeStamp, postDateTime);
            vm_fullReport.postsList.add(post);
        }

        cursor.close();
        calculateAll();
    }


    private void fetchYearMonthTypeWise() {
        vm_fullReport.postsList.clear();
        //tv_typeTitle.setText(getString(R.string.Category));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadYearMonthTypeWise(vm_fullReport.year, vm_fullReport.month, vm_fullReport.type);

        while (cursor.moveToNext()) {
            String postDescription = cursor.getString(1);
            String postCategory = cursor.getString(2);
            String postType = cursor.getString(3);
            String postAmount = cursor.getString(4);
            String postTime = cursor.getString(5);
            String postDay = cursor.getString(6);
            String postMonth = cursor.getString(7);
            String postYear = cursor.getString(8);
            String postDateTime = cursor.getString(9);
            String timeStamp = cursor.getString(10);

            MC_Posts post = new MC_Posts(postDescription, postCategory, postType, postAmount, postYear, postMonth, postDay, postTime, timeStamp, postDateTime);
            vm_fullReport.postsList.add(post);
        }

        cursor.close();
        calculateAll();
    }


    private void fetchYearMonthCategoryWise() {
        vm_fullReport.postsList.clear();
        //tv_typeTitle.setText(getString(R.string.Category));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadYearMonthCategoryWise(vm_fullReport.year, vm_fullReport.month, vm_fullReport.category);

        while (cursor.moveToNext()) {
            String postDescription = cursor.getString(1);
            String postCategory = cursor.getString(2);
            String postType = cursor.getString(3);
            String postAmount = cursor.getString(4);
            String postTime = cursor.getString(5);
            String postDay = cursor.getString(6);
            String postMonth = cursor.getString(7);
            String postYear = cursor.getString(8);
            String postDateTime = cursor.getString(9);
            String timeStamp = cursor.getString(10);

            MC_Posts post = new MC_Posts(postDescription, postCategory, postType, postAmount, postYear, postMonth, postDay, postTime, timeStamp, postDateTime);
            vm_fullReport.postsList.add(post);
        }

        cursor.close();
        calculateAll();
    }


    private void fetchYearMonthTypeCategoryWise() {
        vm_fullReport.postsList.clear();
        //tv_typeTitle.setText(getString(R.string.Category));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadYearMonthTypeCategoryWise(vm_fullReport.year, vm_fullReport.month, vm_fullReport.type, vm_fullReport.category);

        while (cursor.moveToNext()) {
            String postDescription = cursor.getString(1);
            String postCategory = cursor.getString(2);
            String postType = cursor.getString(3);
            String postAmount = cursor.getString(4);
            String postTime = cursor.getString(5);
            String postDay = cursor.getString(6);
            String postMonth = cursor.getString(7);
            String postYear = cursor.getString(8);
            String postDateTime = cursor.getString(9);
            String timeStamp = cursor.getString(10);

            MC_Posts post = new MC_Posts(postDescription, postCategory, postType, postAmount, postYear, postMonth, postDay, postTime, timeStamp, postDateTime);
            vm_fullReport.postsList.add(post);
        }

        cursor.close();
        calculateAll();
    }


    private void fetchYearMonthDayWise() {
        vm_fullReport.postsList.clear();
        //tv_typeTitle.setText(getString(R.string.Category));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadYearMonthDayWise(vm_fullReport.year, vm_fullReport.month, vm_fullReport.day);
        while (cursor.moveToNext()) {
            String postDescription = cursor.getString(1);
            String postCategory = cursor.getString(2);
            String postType = cursor.getString(3);
            String postAmount = cursor.getString(4);
            String postTime = cursor.getString(5);
            String postDay = cursor.getString(6);
            String postMonth = cursor.getString(7);
            String postYear = cursor.getString(8);
            String postDateTime = cursor.getString(9);
            String timeStamp = cursor.getString(10);

            MC_Posts post = new MC_Posts(postDescription, postCategory, postType, postAmount, postYear, postMonth, postDay, postTime, timeStamp, postDateTime);
            vm_fullReport.postsList.add(post);
        }

        cursor.close();
        calculateAll();

    }


    private void fetchYearMonthDayTypeWise() {
        vm_fullReport.postsList.clear();
        //tv_typeTitle.setText(getString(R.string.Category));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadYearMonthDayTypeWise(vm_fullReport.year, vm_fullReport.month, vm_fullReport.day, vm_fullReport.type);
        while (cursor.moveToNext()) {
            String postDescription = cursor.getString(1);
            String postCategory = cursor.getString(2);
            String postType = cursor.getString(3);
            String postAmount = cursor.getString(4);
            String postTime = cursor.getString(5);
            String postDay = cursor.getString(6);
            String postMonth = cursor.getString(7);
            String postYear = cursor.getString(8);
            String postDateTime = cursor.getString(9);
            String timeStamp = cursor.getString(10);

            MC_Posts post = new MC_Posts(postDescription, postCategory, postType, postAmount, postYear, postMonth, postDay, postTime, timeStamp, postDateTime);
            vm_fullReport.postsList.add(post);
        }

        cursor.close();
        calculateAll();
    }


    private void fetchYearMonthDayCategoryWise() {
        vm_fullReport.postsList.clear();
        //tv_typeTitle.setText(getString(R.string.Category));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadYearMonthDayTypeWise(vm_fullReport.year, vm_fullReport.month, vm_fullReport.day, vm_fullReport.category);
        while (cursor.moveToNext()) {
            String postDescription = cursor.getString(1);
            String postCategory = cursor.getString(2);
            String postType = cursor.getString(3);
            String postAmount = cursor.getString(4);
            String postTime = cursor.getString(5);
            String postDay = cursor.getString(6);
            String postMonth = cursor.getString(7);
            String postYear = cursor.getString(8);
            String postDateTime = cursor.getString(9);
            String timeStamp = cursor.getString(10);

            MC_Posts post = new MC_Posts(postDescription, postCategory, postType, postAmount, postYear, postMonth, postDay, postTime, timeStamp, postDateTime);
            vm_fullReport.postsList.add(post);
        }

        cursor.close();
        calculateAll();
    }


    private void fetchYearMonthDayTypeCategoryWise() {
        vm_fullReport.postsList.clear();
        //tv_typeTitle.setText(getString(R.string.YearWise));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadYearMonthDayTypeCategoryWise(vm_fullReport.year, vm_fullReport.month, vm_fullReport.day, vm_fullReport.type, vm_fullReport.category);

        while (cursor.moveToNext()) {
            String postDescription = cursor.getString(1);
            String postCategory = cursor.getString(2);
            String postType = cursor.getString(3);
            String postAmount = cursor.getString(4);
            String postTime = cursor.getString(5);
            String postDay = cursor.getString(6);
            String postMonth = cursor.getString(7);
            String postYear = cursor.getString(8);
            String postDateTime = cursor.getString(9);
            String timeStamp = cursor.getString(10);

            MC_Posts post = new MC_Posts(postDescription, postCategory, postType, postAmount, postYear, postMonth, postDay, postTime, timeStamp, postDateTime);
            vm_fullReport.postsList.add(post);
        }

        cursor.close();
        calculateAll();
    }







    private void calculateAll(){
        resetPreviousCalculatedValues();

        for(int i = 0; i< vm_fullReport.postsList.size() ; i++){
            if(vm_fullReport.postsList.get(i).getPostType().equals(Constants.TYPE_INCOME)){
                if(vm_fullReport.postsList.get(i).postCategory.equals(Constants.CATEGORY_SALARY)){
                     vm_fullReport.income_salary= vm_fullReport.income_salary+Integer.parseInt(vm_fullReport.postsList.get(i).getPostAmount());
                }

                else if(vm_fullReport.postsList.get(i).postCategory.equals(Constants.CATEGORY_BUSINESS)){
                    vm_fullReport.income_business= vm_fullReport.income_business+Integer.parseInt(vm_fullReport.postsList.get(i).getPostAmount());
                }

                else if(vm_fullReport.postsList.get(i).postCategory.equals(Constants.CATEGORY_HOUSE_RENT)){
                    vm_fullReport.income_house_rent= vm_fullReport.income_house_rent+Integer.parseInt(vm_fullReport.postsList.get(i).getPostAmount());
                }

                else if(vm_fullReport.postsList.get(i).postCategory.equals(Constants.CATEGORY_OTHER)){
                    vm_fullReport.income_other= vm_fullReport.income_other+Integer.parseInt(vm_fullReport.postsList.get(i).getPostAmount());
                }

                vm_fullReport.total_income=vm_fullReport.total_income+Integer.parseInt(vm_fullReport.postsList.get(i).getPostAmount());

            }
            else{
                if(vm_fullReport.postsList.get(i).postCategory.equals(Constants.CATEGORY_FOOD)){
                    vm_fullReport.expense_food= vm_fullReport.expense_food+ Integer.parseInt(vm_fullReport.postsList.get(i).getPostAmount());
                }
                else if(vm_fullReport.postsList.get(i).postCategory.equals(Constants.CATEGORY_TRANSPORT)){
                    vm_fullReport.expense_transport=vm_fullReport.expense_transport+ Integer.parseInt(vm_fullReport.postsList.get(i).getPostAmount());
                }
                else if(vm_fullReport.postsList.get(i).postCategory.equals(Constants.CATEGORY_BILLS)){
                    vm_fullReport.expense_bills= vm_fullReport.expense_bills+Integer.parseInt(vm_fullReport.postsList.get(i).getPostAmount());
                }
                else if(vm_fullReport.postsList.get(i).postCategory.equals(Constants.CATEGORY_HOUSE_RENT)){
                    vm_fullReport.expense_houseRent= vm_fullReport.expense_houseRent+Integer.parseInt(vm_fullReport.postsList.get(i).getPostAmount());
                }
                else if(vm_fullReport.postsList.get(i).postCategory.equals(Constants.CATEGORY_BUSINESS)){
                    vm_fullReport.expense_business= vm_fullReport.expense_business+Integer.parseInt(vm_fullReport.postsList.get(i).getPostAmount());
                }
                else if(vm_fullReport.postsList.get(i).postCategory.equals(Constants.CATEGORY_MEDICINE)){
                    vm_fullReport.expense_medicine= vm_fullReport.expense_medicine+Integer.parseInt(vm_fullReport.postsList.get(i).getPostAmount());
                }
                else if(vm_fullReport.postsList.get(i).postCategory.equals(Constants.CATEGORY_CLOTHS)){
                    vm_fullReport.expense_cloths= vm_fullReport.expense_cloths+Integer.parseInt(vm_fullReport.postsList.get(i).getPostAmount());
                }
                else if(vm_fullReport.postsList.get(i).postCategory.equals(Constants.CATEGORY_EDUCATION)){
                    vm_fullReport.expense_education=vm_fullReport.expense_education+ Integer.parseInt(vm_fullReport.postsList.get(i).getPostAmount());
                }
                else if(vm_fullReport.postsList.get(i).postCategory.equals(Constants.CATEGORY_LIFESTYLE)){
                    vm_fullReport.expense_lifestyle=vm_fullReport.expense_lifestyle+ Integer.parseInt(vm_fullReport.postsList.get(i).getPostAmount());
                }
                else if(vm_fullReport.postsList.get(i).postCategory.equals(Constants.CATEGORY_OTHER)){
                    vm_fullReport.expense_other= vm_fullReport.expense_other+Integer.parseInt(vm_fullReport.postsList.get(i).getPostAmount());
                }

                vm_fullReport.total_expense = vm_fullReport.total_expense+Integer.parseInt(vm_fullReport.postsList.get(i).getPostAmount());
            }
        }
    }




    public void setCalculatedValue(){
        tv_incomeValue_salary.setText(String.valueOf(vm_fullReport.income_salary));
        tv_incomeValue_business.setText(String.valueOf(vm_fullReport.income_business));
        tv_incomeValue_houseRent.setText(String.valueOf(vm_fullReport.income_house_rent));
        tv_incomeValue_other.setText(String.valueOf(vm_fullReport.income_other));
        tv_totalIncomeValue.setText(String.valueOf(vm_fullReport.total_income));

        tv_expenseValue_food.setText(String.valueOf(vm_fullReport.expense_food));
        tv_expenseValue_transport.setText(String.valueOf(vm_fullReport.expense_transport));
        tv_expenseValue_bills.setText(String.valueOf(vm_fullReport.expense_bills));
        tv_expenseValue_houseRent.setText(String.valueOf(vm_fullReport.expense_houseRent));
        tv_expenseValue_business.setText(String.valueOf(vm_fullReport.expense_business));
        tv_expenseValue_medicine.setText(String.valueOf(vm_fullReport.expense_medicine));
        tv_expenseValue_cloths.setText(String.valueOf(vm_fullReport.expense_cloths));
        tv_expenseValue_education.setText(String.valueOf(vm_fullReport.expense_education));
        tv_expenseValue_lifestyle.setText(String.valueOf(vm_fullReport.expense_lifestyle));
        tv_expenseValue_other.setText(String.valueOf(vm_fullReport.expense_other));
        tv_totalExpenseValue.setText(String.valueOf(vm_fullReport.total_expense));
    }





    private void resetPreviousCalculatedValues() {
        vm_fullReport.income_salary=0;
        vm_fullReport.income_business=0;
        vm_fullReport.income_house_rent=0;
        vm_fullReport.income_other=0;
        vm_fullReport.total_income=0;

        vm_fullReport.expense_food=0;
        vm_fullReport.expense_transport=0;
        vm_fullReport.expense_bills=0;
        vm_fullReport.expense_houseRent=0;
        vm_fullReport.expense_business=0;
        vm_fullReport.expense_medicine=0;
        vm_fullReport.expense_cloths=0;
        vm_fullReport.expense_education=0;
        vm_fullReport.expense_lifestyle=0;
        vm_fullReport.expense_other=0;
        vm_fullReport.total_expense=0;
    }






    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}