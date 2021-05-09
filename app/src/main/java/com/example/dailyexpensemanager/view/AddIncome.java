package com.example.dailyexpensemanager.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dailyexpensemanager.R;
import com.example.dailyexpensemanager.common.Constants;
import com.example.dailyexpensemanager.common.SQLiteHelper;
import com.example.dailyexpensemanager.model.MC_Posts;
import com.example.dailyexpensemanager.viewModels.VM_AddIncome;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AddIncome extends AppCompatActivity implements View.OnClickListener {

    CardView cv_back,cv_save,cv_salary,cv_business,cv_houseRent,cv_other,cv_calculator;
    CardView cv_amount500,cv_amount1000,cv_amount1500,cv_amount2000,cv_amount2500, cv_amount3000,cv_amount3500,cv_amount4000,cv_amount5000,
            cv_amount10000,cv_amount20000,cv_amount30000,cv_amount40000,cv_amount50000,cv_amount100000,cv_amount200000,cv_amount300000,
            cv_amount400000, cv_amount500000;
    TextView tv_changeDate,tv_dateTime;
    EditText et_amount,et_incomeDescription;

    private VM_AddIncome vm_addIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);


        //***************************************************View Bindings*********************************************
        cv_back = findViewById(R.id.cv_back_AddIncome);
        cv_save = findViewById(R.id.cv_save_AddIncome);
        cv_salary = findViewById(R.id.cv_salary_AddIncome);
        cv_business = findViewById(R.id.cv_business_AddIncome);
        cv_houseRent = findViewById(R.id.cv_houseRent_AddIncome);
        cv_other = findViewById(R.id.cv_other_AddIncome);
        cv_calculator = findViewById(R.id.cv_calculator_AddIncome);
        tv_changeDate = findViewById(R.id.tc_changeDate_AddIncome);
        tv_dateTime = findViewById(R.id.tv_dateTime_AddIncome);
        et_amount = findViewById(R.id.et_amount_AddIncome);
        et_incomeDescription = findViewById(R.id.et_incomeDescription_AddIncome);

        cv_amount500 = findViewById(R.id.cv_amount500_AddIncome);
        cv_amount1000 = findViewById(R.id.cv_amount1000_AddIncome);
        cv_amount1500 = findViewById(R.id.cv_amount1500_AddIncome);
        cv_amount2000 = findViewById(R.id.cv_amount2000_AddIncome);
        cv_amount2500 = findViewById(R.id.cv_amount2500_AddIncome);
        cv_amount3000 = findViewById(R.id.cv_amount3000_AddIncome);
        cv_amount3500 = findViewById(R.id.cv_amount3500_AddIncome);
        cv_amount4000 = findViewById(R.id.cv_amount4000_AddIncome);
        cv_amount5000 = findViewById(R.id.cv_amount5000_AddIncome);
        cv_amount10000 = findViewById(R.id.cv_amount10000_AddIncome);
        cv_amount20000 = findViewById(R.id.cv_amount20000_AddIncome);
        cv_amount30000 = findViewById(R.id.cv_amount30000_AddIncome);
        cv_amount40000 = findViewById(R.id.cv_amount40000_AddIncome);
        cv_amount50000 = findViewById(R.id.cv_amount50000_AddIncome);
        cv_amount100000 = findViewById(R.id.cv_amount100000_AddIncome);
        cv_amount200000 = findViewById(R.id.cv_amount200000_AddIncome);
        cv_amount300000 = findViewById(R.id.cv_amount300000_AddIncome);
        cv_amount400000 = findViewById(R.id.cv_amount400000_AddIncome);
        cv_amount500000 = findViewById(R.id.cv_amount500000_AddIncome);




        //*************************************************Initializations*******************************************
        vm_addIncome = ViewModelProviders.of(this).get(VM_AddIncome.class);




        //***************************************************Click Listeners**************************************
        cv_back.setOnClickListener(v -> startActivity(new Intent(AddIncome.this,MainActivity.class)));

        cv_amount500.setOnClickListener(this);
        cv_amount1000.setOnClickListener(this);
        cv_amount1500.setOnClickListener(this);
        cv_amount2000.setOnClickListener(this);
        cv_amount2500.setOnClickListener(this);
        cv_amount3000.setOnClickListener(this);
        cv_amount3500.setOnClickListener(this);
        cv_amount4000.setOnClickListener(this);
        cv_amount5000.setOnClickListener(this);
        cv_amount10000.setOnClickListener(this);
        cv_amount20000.setOnClickListener(this);
        cv_amount30000.setOnClickListener(this);
        cv_amount40000.setOnClickListener(this);
        cv_amount50000.setOnClickListener(this);
        cv_amount100000.setOnClickListener(this);
        cv_amount200000.setOnClickListener(this);
        cv_amount300000.setOnClickListener(this);
        cv_amount400000.setOnClickListener(this);
        cv_amount500000.setOnClickListener(this);
        cv_salary.setOnClickListener(this);
        cv_business.setOnClickListener(this);
        cv_houseRent.setOnClickListener(this);
        cv_other.setOnClickListener(this);

        cv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIncome();
            }
        });


        tv_dateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getDateTime();
            }
        });





        //************************************************Starting Methods*****************************************

        observe();
        setDateTime();



    }

    private void setDateTime() {
        SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("dd-MM-yyyy  hh:mm a", Locale.getDefault());
        tv_dateTime.setText(simpleDateTimeFormat.format(System.currentTimeMillis()));
    }

    private void addIncome() {

            if(TextUtils.isEmpty(et_amount.getText())){
                et_amount.setError(getString(R.string.AmontNeeded));
            }

            else if(TextUtils.isEmpty(vm_addIncome.category)) {
                Toast.makeText(this, getString(R.string.Pleaseselectacatagory), Toast.LENGTH_SHORT).show();
            }

            else{

                //dd-MM-yyyy
                SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("dd-MM-yyyy  hh:mm a", Locale.getDefault());
                SimpleDateFormat simpleDayFormat = new SimpleDateFormat("dd", Locale.getDefault());
                SimpleDateFormat simpleMonthFormat = new SimpleDateFormat("MM", Locale.getDefault());
                SimpleDateFormat simpleYearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
                SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh-mm a", Locale.getDefault());

                String day = simpleDayFormat.format(System.currentTimeMillis());
                String month = simpleMonthFormat.format(System.currentTimeMillis());
                String year = simpleYearFormat.format(System.currentTimeMillis());
                String time = simpleTimeFormat.format(System.currentTimeMillis());
                String dateTime = simpleDateTimeFormat.format(System.currentTimeMillis());

                String amount = et_amount.getText().toString();
                String expenseDescription="";

                if(!TextUtils.isEmpty(et_incomeDescription.getText())){
                    expenseDescription = et_incomeDescription.getText().toString();
                }

                MC_Posts posts = new MC_Posts(expenseDescription,vm_addIncome.category,Constants.TYPE_INCOME,amount,year,
                        month,day,time,String.valueOf(System.currentTimeMillis()),dateTime);
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();



                SQLiteHelper helper = new SQLiteHelper(AddIncome.this);
                long flag =  helper.saveData(posts);
                Toast.makeText(this, "Flag "+flag, Toast.LENGTH_SHORT).show();

            }

    }







    private void observe() {
        vm_addIncome.mutable_category.observe(this, s -> {
                  if(s.equals(Constants.CATEGORY_SALARY)){
                      markSalary();
                  }

                  else if(s.equals(Constants.CATEGORY_BUSINESS)){
                      markBusiness();
                  }

                  else if(s.equals(Constants.CATEGORY_HOUSE_RENT)){
                      markHouseRent();
                  }

                  else if(s.equals(Constants.CATEGORY_OTHER)){
                      markOther();
                  }
        });



        vm_addIncome.mutable_amount.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals(Constants.AMOUNT_500)){
                    et_amount.setText(Constants.AMOUNT_500);
                }

                else if(s.equals(Constants.AMOUNT_1000)){
                    et_amount.setText(Constants.AMOUNT_1000);
                }

                else if(s.equals(Constants.AMOUNT_1500)){
                    et_amount.setText(Constants.AMOUNT_1500);
                }

                else if(s.equals(Constants.AMOUNT_2000)){
                    et_amount.setText(Constants.AMOUNT_2000);
                }

                else if(s.equals(Constants.AMOUNT_2500)){
                    et_amount.setText(Constants.AMOUNT_2500);
                }

                else if(s.equals(Constants.AMOUNT_3000)){
                    et_amount.setText(Constants.AMOUNT_3000);
                }

                else if(s.equals(Constants.AMOUNT_4000)){
                    et_amount.setText(Constants.AMOUNT_4000);
                }

                else if(s.equals(Constants.AMOUNT_5000)){
                    et_amount.setText(Constants.AMOUNT_5000);
                }

                else if(s.equals(Constants.AMOUNT_10000)){
                    et_amount.setText(Constants.AMOUNT_10000);
                }

                else if(s.equals(Constants.AMOUNT_20000)){
                    et_amount.setText(Constants.AMOUNT_20000);
                }

                else if(s.equals(Constants.AMOUNT_30000)){
                    et_amount.setText(Constants.AMOUNT_30000);
                }

                else if(s.equals(Constants.AMOUNT_40000)){
                    et_amount.setText(Constants.AMOUNT_40000);
                }

                else if(s.equals(Constants.AMOUNT_50000)){
                    et_amount.setText(Constants.AMOUNT_50000);
                }

                else if(s.equals(Constants.AMOUNT_100000)){
                    et_amount.setText(Constants.AMOUNT_100000);
                }

                else if(s.equals(Constants.AMOUNT_200000)){
                    et_amount.setText(Constants.AMOUNT_200000);
                }

                else if(s.equals(Constants.AMOUNT_300000)){
                    et_amount.setText(Constants.AMOUNT_300000);
                }

                else if(s.equals(Constants.AMOUNT_400000)){
                    et_amount.setText(Constants.AMOUNT_400000);
                }

                else if(s.equals(Constants.AMOUNT_500000)){
                    et_amount.setText(Constants.AMOUNT_500000);
                }
            }
        });
    }










    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.cv_amount500_AddIncome){
            vm_addIncome.amount = Constants.AMOUNT_500;
            vm_addIncome.mutable_amount.setValue(vm_addIncome.amount);
        }
        else if(v.getId() == R.id.cv_amount1000_AddIncome){
            vm_addIncome.amount = Constants.AMOUNT_1000;
            vm_addIncome.mutable_amount.setValue(vm_addIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount1500_AddIncome){
            vm_addIncome.amount = Constants.AMOUNT_1500;
            vm_addIncome.mutable_amount.setValue(vm_addIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount2000_AddIncome){
            vm_addIncome.amount = Constants.AMOUNT_2000;
            vm_addIncome.mutable_amount.setValue(vm_addIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount2500_AddIncome){
            vm_addIncome.amount = Constants.AMOUNT_2500;
            vm_addIncome.mutable_amount.setValue(vm_addIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount3000_AddIncome){
            vm_addIncome.amount = Constants.AMOUNT_3000;
            vm_addIncome.mutable_amount.setValue(vm_addIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount3500_AddIncome){
            vm_addIncome.amount = Constants.AMOUNT_3500;
            vm_addIncome.mutable_amount.setValue(vm_addIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount4000_AddIncome){
            vm_addIncome.amount = Constants.AMOUNT_4000;
            vm_addIncome.mutable_amount.setValue(vm_addIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount5000_AddIncome){
            vm_addIncome.amount = Constants.AMOUNT_5000;
            vm_addIncome.mutable_amount.setValue(vm_addIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount10000_AddIncome){
            vm_addIncome.amount = Constants.AMOUNT_10000;
            vm_addIncome.mutable_amount.setValue(vm_addIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount20000_AddIncome){
            vm_addIncome.amount = Constants.AMOUNT_20000;
            vm_addIncome.mutable_amount.setValue(vm_addIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount30000_AddIncome){
            vm_addIncome.amount = Constants.AMOUNT_30000;
            vm_addIncome.mutable_amount.setValue(vm_addIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount40000_AddIncome){
            vm_addIncome.amount = Constants.AMOUNT_40000;
            vm_addIncome.mutable_amount.setValue(vm_addIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount50000_AddIncome){
            vm_addIncome.amount = Constants.AMOUNT_50000;
            vm_addIncome.mutable_amount.setValue(vm_addIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount100000_AddIncome){
            vm_addIncome.amount = Constants.AMOUNT_100000;
            vm_addIncome.mutable_amount.setValue(vm_addIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount200000_AddIncome){
            vm_addIncome.amount = Constants.AMOUNT_200000;
            vm_addIncome.mutable_amount.setValue(vm_addIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount300000_AddIncome){
            vm_addIncome.amount = Constants.AMOUNT_300000;
            vm_addIncome.mutable_amount.setValue(vm_addIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount400000_AddIncome){
            vm_addIncome.amount = Constants.AMOUNT_400000;
            vm_addIncome.mutable_amount.setValue(vm_addIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount500000_AddIncome){
            vm_addIncome.amount = Constants.AMOUNT_500000;
            vm_addIncome.mutable_amount.setValue(vm_addIncome.amount);
        }


        //*************************************************category Selections*********************************



        else if(v.getId() == R.id.cv_salary_AddIncome){
            vm_addIncome.category= Constants.CATEGORY_SALARY;
            vm_addIncome.mutable_category.setValue(vm_addIncome.category);
        }

        else if(v.getId() == R.id.cv_business_AddIncome){
            vm_addIncome.category= Constants.CATEGORY_BUSINESS;
            vm_addIncome.mutable_category.setValue(vm_addIncome.category);
        }

        else if(v.getId() == R.id.cv_houseRent_AddIncome){
            vm_addIncome.category= Constants.CATEGORY_HOUSE_RENT;
            vm_addIncome.mutable_category.setValue(vm_addIncome.category);
        }

        else if(v.getId() == R.id.cv_other_AddIncome){
            vm_addIncome.category= Constants.CATEGORY_OTHER;
            vm_addIncome.mutable_category.setValue(vm_addIncome.category);
        }
    }

    private void markOther() {
        cv_salary.setCardBackgroundColor(getColor(R.color.white));
        cv_business.setCardBackgroundColor(getColor(R.color.white));
        cv_houseRent.setCardBackgroundColor(getColor(R.color.white));
        cv_other.setCardBackgroundColor(getColor(R.color.green));
    }

    private void markHouseRent() {
        cv_salary.setCardBackgroundColor(getColor(R.color.white));
        cv_business.setCardBackgroundColor(getColor(R.color.white));
        cv_houseRent.setCardBackgroundColor(getColor(R.color.green));
        cv_other.setCardBackgroundColor(getColor(R.color.white));
    }

    private void markBusiness() {
        cv_salary.setCardBackgroundColor(getColor(R.color.white));
        cv_business.setCardBackgroundColor(getColor(R.color.green));
        cv_houseRent.setCardBackgroundColor(getColor(R.color.white));
        cv_other.setCardBackgroundColor(getColor(R.color.white));
    }

    private void markSalary() {
        cv_salary.setCardBackgroundColor(getColor(R.color.green));
        cv_business.setCardBackgroundColor(getColor(R.color.white));
        cv_houseRent.setCardBackgroundColor(getColor(R.color.white));
        cv_other.setCardBackgroundColor(getColor(R.color.white));
    }







    private String getDate() {
        DatePicker datePicker = new DatePicker(this);
        int currentDay = datePicker.getDayOfMonth();
        int currentMonth = (datePicker.getMonth()+1);
        int currentYear = datePicker.getYear();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        vm_addIncome.dateTime = String.valueOf(currentDay)+"/"+String.valueOf(currentMonth)+"/"+currentYear;
                        tv_dateTime.setText(vm_addIncome.dateTime);
                        //Log.d("tag",requiredDate);
                    }
                },currentDay, currentMonth,currentYear
        );
        datePickerDialog.show();
        return vm_addIncome.dateTime;
    }



}