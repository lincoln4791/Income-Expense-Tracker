package com.lincoln4791.dailyexpensemanager.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lincoln4791.dailyexpensemanager.R;
import com.lincoln4791.dailyexpensemanager.common.Constants;
import com.lincoln4791.dailyexpensemanager.common.NodeName;
import com.lincoln4791.dailyexpensemanager.common.SQLiteHelper;
import com.lincoln4791.dailyexpensemanager.common.UtilDB;
import com.lincoln4791.dailyexpensemanager.model.MC_Posts;
import com.lincoln4791.dailyexpensemanager.viewModels.VM_EditDataIncome;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditDataIncome extends AppCompatActivity implements View.OnClickListener {

    CardView cv_back, cv_update,cv_salary,cv_business,cv_houseRent,cv_other,cv_calculator;
    CardView cv_amount500,cv_amount1000,cv_amount1500,cv_amount2000,cv_amount2500, cv_amount3000,cv_amount3500,cv_amount4000,cv_amount5000,
            cv_amount10000,cv_amount20000,cv_amount30000,cv_amount40000,cv_amount50000,cv_amount100000,cv_amount200000,cv_amount300000,
            cv_amount400000, cv_amount500000;
    TextView tv_dateTime,tv_currentBalance_toolbar;
    EditText et_amount,et_incomeDescription;
    private ImageView iv_home;

    String incomeDescription,amount;
    private String ID;

    private int hour, minute,year,month,day;
    String am_pm,hourInString;

    private VM_EditDataIncome vm_EditDataIncome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data_income);





        //***************************************************View Bindings*********************************************
        cv_back = findViewById(R.id.cv_back_EditDataIncome);
        cv_update = findViewById(R.id.cv_save_EditDataIncome);
        cv_salary = findViewById(R.id.cv_salary_EditDataIncome);
        cv_business = findViewById(R.id.cv_business_EditDataIncome);
        cv_houseRent = findViewById(R.id.cv_houseRent_EditDataIncome);
        cv_other = findViewById(R.id.cv_other_EditDataIncome);
        cv_calculator = findViewById(R.id.cv_calculator_EditDataIncome);
        tv_dateTime = findViewById(R.id.tv_dateTime_EditDataIncome);
        et_amount = findViewById(R.id.et_amount_EditDataIncome);
        et_incomeDescription = findViewById(R.id.et_incomeDescription_EditDataIncome);

        cv_amount500 = findViewById(R.id.cv_amount500_EditDataIncome);
        cv_amount1000 = findViewById(R.id.cv_amount1000_EditDataIncome);
        cv_amount1500 = findViewById(R.id.cv_amount1500_EditDataIncome);
        cv_amount2000 = findViewById(R.id.cv_amount2000_EditDataIncome);
        cv_amount2500 = findViewById(R.id.cv_amount2500_EditDataIncome);
        cv_amount3000 = findViewById(R.id.cv_amount3000_EditDataIncome);
        cv_amount3500 = findViewById(R.id.cv_amount3500_EditDataIncome);
        cv_amount4000 = findViewById(R.id.cv_amount4000_EditDataIncome);
        cv_amount5000 = findViewById(R.id.cv_amount5000_EditDataIncome);
        cv_amount10000 = findViewById(R.id.cv_amount10000_EditDataIncome);
        cv_amount20000 = findViewById(R.id.cv_amount20000_EditDataIncome);
        cv_amount30000 = findViewById(R.id.cv_amount30000_EditDataIncome);
        cv_amount40000 = findViewById(R.id.cv_amount40000_EditDataIncome);
        cv_amount50000 = findViewById(R.id.cv_amount50000_EditDataIncome);
        cv_amount100000 = findViewById(R.id.cv_amount100000_EditDataIncome);
        cv_amount200000 = findViewById(R.id.cv_amount200000_EditDataIncome);
        cv_amount300000 = findViewById(R.id.cv_amount300000_EditDataIncome);
        cv_amount400000 = findViewById(R.id.cv_amount400000_EditDataIncome);
        cv_amount500000 = findViewById(R.id.cv_amount500000_EditDataIncome);

        tv_currentBalance_toolbar = findViewById(R.id.tv_currentBalanceValue_toolBar_EditDataIncome);
        iv_home = findViewById(R.id.iv_home_toolbar_EditDataIncome);




        //*************************************************Initializations*******************************************
        getSupportActionBar().hide();
        vm_EditDataIncome = ViewModelProviders.of(this).get(VM_EditDataIncome.class);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat simpleHourFormat = new SimpleDateFormat("hh");
        SimpleDateFormat simpleMinuteFormat = new SimpleDateFormat("mm");

        hour = Integer.parseInt(simpleHourFormat.format(System.currentTimeMillis()));
        minute = Integer.parseInt(simpleMinuteFormat.format(System.currentTimeMillis()));






        //***************************************************Click Listeners**************************************
        cv_back.setOnClickListener(v -> startActivity(new Intent(EditDataIncome.this,MainActivity.class)));

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

        cv_update.setOnClickListener(v -> updateIncome());

        tv_dateTime.setOnClickListener(v -> changeDate());

        iv_home.setOnClickListener(v -> {
            startActivity(new Intent(EditDataIncome.this,MainActivity.class));
        });





        //************************************************Starting Methods*****************************************
        tv_currentBalance_toolbar.setText(String.valueOf(UtilDB.currentBalance));
        observe();
        getIntentData();
        setViewWIthIntentData();


    }

    private void setViewWIthIntentData() {
        tv_dateTime.setText(vm_EditDataIncome.dateTime);
        et_amount.setText(amount);
        et_incomeDescription.setText(incomeDescription);

        if(vm_EditDataIncome.category.equals(Constants.CATEGORY_SALARY)){
            markSalary();
        }
        else if(vm_EditDataIncome.category.equals(Constants.CATEGORY_BUSINESS)){
            markBusiness();
        }
        else if(vm_EditDataIncome.category.equals(Constants.CATEGORY_HOUSE_RENT)){
            markHouseRent();
        }
        else if(vm_EditDataIncome.category.equals(Constants.CATEGORY_OTHER)){
            markOther();
        }



    }

    private void getIntentData() {
        ID = getIntent().getStringExtra(NodeName.ID);
        incomeDescription = getIntent().getStringExtra(NodeName.POST_DESCRIPTION);
        amount= getIntent().getStringExtra(NodeName.POST_AMOUNT);
        vm_EditDataIncome.dateTime = getIntent().getStringExtra(NodeName.POST_DATE_TIME);
        vm_EditDataIncome.category=getIntent().getStringExtra(NodeName.POST_CATEGORY);
        vm_EditDataIncome.year=getIntent().getStringExtra(NodeName.POST_YEAR);
        vm_EditDataIncome.month=getIntent().getStringExtra(NodeName.POST_MONTH);
        vm_EditDataIncome.day=getIntent().getStringExtra(NodeName.POST_DAY);
        vm_EditDataIncome.time=getIntent().getStringExtra(NodeName.POST_TIME);

    }


    private void updateIncome() {

        if(TextUtils.isEmpty(et_amount.getText())){
            et_amount.setError(getString(R.string.AmontNeeded));
        }

        else if(TextUtils.isEmpty(vm_EditDataIncome.category)) {
            Toast.makeText(this, getString(R.string.Pleaseselectacatagory), Toast.LENGTH_SHORT).show();
        }

        else{
             amount = et_amount.getText().toString();
             incomeDescription="";

            if(!TextUtils.isEmpty(et_incomeDescription.getText())){
                incomeDescription = et_incomeDescription.getText().toString();
            }

            MC_Posts posts = new MC_Posts(incomeDescription, vm_EditDataIncome.category, Constants.TYPE_INCOME,amount, vm_EditDataIncome.year,
                    vm_EditDataIncome.month, vm_EditDataIncome.day, vm_EditDataIncome.time,String.valueOf(System.currentTimeMillis()), vm_EditDataIncome.dateTime);


            SQLiteHelper helper = new SQLiteHelper(EditDataIncome.this);
            helper.updateData(ID,posts);
            Toast.makeText(this, "Success ", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(EditDataIncome.this,MainActivity.class));

        }

    }







    private void observe() {
        vm_EditDataIncome.mutable_category.observe(this, s -> {
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



        vm_EditDataIncome.mutable_amount.observe(this, new Observer<String>() {
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
        if(v.getId() == R.id.cv_amount500_EditDataIncome){
            vm_EditDataIncome.amount = Constants.AMOUNT_500;
            vm_EditDataIncome.mutable_amount.setValue(vm_EditDataIncome.amount);
        }
        else if(v.getId() == R.id.cv_amount1000_EditDataIncome){
            vm_EditDataIncome.amount = Constants.AMOUNT_1000;
            vm_EditDataIncome.mutable_amount.setValue(vm_EditDataIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount1500_EditDataIncome){
            vm_EditDataIncome.amount = Constants.AMOUNT_1500;
            vm_EditDataIncome.mutable_amount.setValue(vm_EditDataIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount2000_EditDataIncome){
            vm_EditDataIncome.amount = Constants.AMOUNT_2000;
            vm_EditDataIncome.mutable_amount.setValue(vm_EditDataIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount2500_EditDataIncome){
            vm_EditDataIncome.amount = Constants.AMOUNT_2500;
            vm_EditDataIncome.mutable_amount.setValue(vm_EditDataIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount3000_EditDataIncome){
            vm_EditDataIncome.amount = Constants.AMOUNT_3000;
            vm_EditDataIncome.mutable_amount.setValue(vm_EditDataIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount3500_EditDataIncome){
            vm_EditDataIncome.amount = Constants.AMOUNT_3500;
            vm_EditDataIncome.mutable_amount.setValue(vm_EditDataIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount4000_EditDataIncome){
            vm_EditDataIncome.amount = Constants.AMOUNT_4000;
            vm_EditDataIncome.mutable_amount.setValue(vm_EditDataIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount5000_EditDataIncome){
            vm_EditDataIncome.amount = Constants.AMOUNT_5000;
            vm_EditDataIncome.mutable_amount.setValue(vm_EditDataIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount10000_EditDataIncome){
            vm_EditDataIncome.amount = Constants.AMOUNT_10000;
            vm_EditDataIncome.mutable_amount.setValue(vm_EditDataIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount20000_EditDataIncome){
            vm_EditDataIncome.amount = Constants.AMOUNT_20000;
            vm_EditDataIncome.mutable_amount.setValue(vm_EditDataIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount30000_EditDataIncome){
            vm_EditDataIncome.amount = Constants.AMOUNT_30000;
            vm_EditDataIncome.mutable_amount.setValue(vm_EditDataIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount40000_EditDataIncome){
            vm_EditDataIncome.amount = Constants.AMOUNT_40000;
            vm_EditDataIncome.mutable_amount.setValue(vm_EditDataIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount50000_EditDataIncome){
            vm_EditDataIncome.amount = Constants.AMOUNT_50000;
            vm_EditDataIncome.mutable_amount.setValue(vm_EditDataIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount100000_EditDataIncome){
            vm_EditDataIncome.amount = Constants.AMOUNT_100000;
            vm_EditDataIncome.mutable_amount.setValue(vm_EditDataIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount200000_EditDataIncome){
            vm_EditDataIncome.amount = Constants.AMOUNT_200000;
            vm_EditDataIncome.mutable_amount.setValue(vm_EditDataIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount300000_EditDataIncome){
            vm_EditDataIncome.amount = Constants.AMOUNT_300000;
            vm_EditDataIncome.mutable_amount.setValue(vm_EditDataIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount400000_EditDataIncome){
            vm_EditDataIncome.amount = Constants.AMOUNT_400000;
            vm_EditDataIncome.mutable_amount.setValue(vm_EditDataIncome.amount);
        }

        else if(v.getId() == R.id.cv_amount500000_EditDataIncome){
            vm_EditDataIncome.amount = Constants.AMOUNT_500000;
            vm_EditDataIncome.mutable_amount.setValue(vm_EditDataIncome.amount);
        }


        //*************************************************category Selections*********************************



        else if(v.getId() == R.id.cv_salary_EditDataIncome){
            vm_EditDataIncome.category= Constants.CATEGORY_SALARY;
            vm_EditDataIncome.mutable_category.setValue(vm_EditDataIncome.category);
        }

        else if(v.getId() == R.id.cv_business_EditDataIncome){
            vm_EditDataIncome.category= Constants.CATEGORY_BUSINESS;
            vm_EditDataIncome.mutable_category.setValue(vm_EditDataIncome.category);
        }

        else if(v.getId() == R.id.cv_houseRent_EditDataIncome){
            vm_EditDataIncome.category= Constants.CATEGORY_HOUSE_RENT;
            vm_EditDataIncome.mutable_category.setValue(vm_EditDataIncome.category);
        }

        else if(v.getId() == R.id.cv_other_EditDataIncome){
            vm_EditDataIncome.category= Constants.CATEGORY_OTHER;
            vm_EditDataIncome.mutable_category.setValue(vm_EditDataIncome.category);
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







    private void changeDate() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(EditDataIncome.this, (view, hourOfDay, minute) -> {
            vm_EditDataIncome.time = String.valueOf(hourOfDay)+" : "+String.valueOf(minute);
            Log.d("tag","hour"+String.valueOf(hourOfDay));
            if(hourOfDay>=12){
                am_pm="pm";
                hourInString = String.valueOf(hourOfDay-12);
            }
            else{
                am_pm="am";
                hourInString = String.valueOf(hourOfDay);
            }

            //Log.d("tag","year"+vm_addIncome.year+" month "+vm_addIncome.month+" day "+vm_addIncome.day+" hour "+hourOfDay+"min "+minute+" "+am_pm);
            tv_dateTime.setText(vm_EditDataIncome.day+"-"+ vm_EditDataIncome.month+"-"+ vm_EditDataIncome.year+"  "+hourInString+":"+String.valueOf(minute)+" "+am_pm);
        }, hour, minute, true);

        DatePickerDialog datePickerDialog = new DatePickerDialog(EditDataIncome.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                setDay(day);
                setMonth(month);
                if(day==1){
                    vm_EditDataIncome.day= getString(R.string.digit01);
                }
                vm_EditDataIncome.year = String.valueOf(year);
                vm_EditDataIncome.month = String.valueOf(month+1);
                vm_EditDataIncome.day = String.valueOf(dayOfMonth);
                timePickerDialog.show();

            }
        }, year, month, day);
        datePickerDialog.show();


    }

    private void setDay(int day) {
        if(day==1){
            vm_EditDataIncome.day=getString(R.string.digit01);
        }
        else if(day==2){
            vm_EditDataIncome.day=getString(R.string.digit02);
        }
        else if(day==3){
            vm_EditDataIncome.day=getString(R.string.digit03);
        }
        else if(day==4){
            vm_EditDataIncome.day=getString(R.string.digit04);
        }
        else if(day==5){
            vm_EditDataIncome.day=getString(R.string.digit05);
        }
        else if(day==6){
            vm_EditDataIncome.day=getString(R.string.digit06);
        }
        else if(day==7){
            vm_EditDataIncome.day=getString(R.string.digit07);
        }
        else if(day==8){
            vm_EditDataIncome.day=getString(R.string.digit08);
        }
        else if(day==9){
            vm_EditDataIncome.day=getString(R.string.digit09);
        }
        else {
            vm_EditDataIncome.day=String.valueOf(day);
        }

    }

    private void setMonth(int month) {
        if (month == 1) {
            vm_EditDataIncome.month = getString(R.string.digit01);
        } else if (month == 2) {
            vm_EditDataIncome.month = getString(R.string.digit02);
        } else if (month == 3) {
            vm_EditDataIncome.month = getString(R.string.digit03);
        } else if (month == 4) {
            vm_EditDataIncome.month = getString(R.string.digit04);
        } else if (month == 5) {
            vm_EditDataIncome.month = getString(R.string.digit05);
        } else if (month == 6) {
            vm_EditDataIncome.month = getString(R.string.digit06);
        } else if (month == 7) {
            vm_EditDataIncome.month = getString(R.string.digit07);
        } else if (month == 8) {
            vm_EditDataIncome.month = getString(R.string.digit08);
        } else if (month == 9) {
            vm_EditDataIncome.month = getString(R.string.digit09);
        } else {
            vm_EditDataIncome.month = String.valueOf(month);
        }
    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }




}