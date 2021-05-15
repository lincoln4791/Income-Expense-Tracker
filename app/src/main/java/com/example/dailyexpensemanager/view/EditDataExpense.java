package com.example.dailyexpensemanager.view;

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

import com.example.dailyexpensemanager.R;
import com.example.dailyexpensemanager.common.Constants;
import com.example.dailyexpensemanager.common.NodeName;
import com.example.dailyexpensemanager.common.SQLiteHelper;
import com.example.dailyexpensemanager.common.UtilDB;
import com.example.dailyexpensemanager.model.MC_Posts;
import com.example.dailyexpensemanager.viewModels.VM_EditDataExpense;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditDataExpense extends AppCompatActivity implements View.OnClickListener {
    CardView cv_back, cv_update,cv_business,cv_houseRent,cv_food,cv_transport,cv_bills,cv_medicine,cv_cloths
            ,cv_education,cv_lifeStyle,cv_other,cv_calculator;
    CardView cv_amount500,cv_amount1000,cv_amount1500,cv_amount2000,cv_amount2500, cv_amount3000,cv_amount3500,cv_amount4000,cv_amount5000,
            cv_amount10000,cv_amount20000,cv_amount30000,cv_amount40000,cv_amount50000,cv_amount100000,cv_amount200000,cv_amount300000,
            cv_amount400000, cv_amount500000;
    TextView tv_changeDate,tv_dateTime,tv_currentBalance_toolbar;
    EditText et_amount,et_expenseDescription;
    private ImageView iv_back;

    String expenseDescription,amount;
    private String ID;

    private int hour, minute,year,month,day;
    String am_pm,hourInString;

    private VM_EditDataExpense vm_EditDataExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data_expense);


        cv_back = findViewById(R.id.cv_back_EditDataExpense);
        cv_update = findViewById(R.id.cv_save_EditDataExpense);
        cv_business = findViewById(R.id.cv_business_EditDataExpense);
        cv_houseRent = findViewById(R.id.cv_houseRent_EditDataExpense);
        cv_food = findViewById(R.id.cv_food_EditDataExpense);
        cv_transport = findViewById(R.id.cv_transport_EditDataExpense);
        cv_bills = findViewById(R.id.cv_bills_EditDataExpense);
        cv_medicine = findViewById(R.id.cv_medicine_EditDataExpense);
        cv_cloths = findViewById(R.id.cv_cloths_EditDataExpense);
        cv_education = findViewById(R.id.cv_education_EditDataExpense);
        cv_lifeStyle = findViewById(R.id.cv_lifeStyle_EditDataExpense);
        cv_other = findViewById(R.id.cv_other_EditDataExpense);
        cv_calculator = findViewById(R.id.cv_calculator_EditDataExpense);
        tv_changeDate = findViewById(R.id.tv_changeDate_EditDataExpense);
        tv_dateTime = findViewById(R.id.tv_dateTime_EditDataExpense);
        et_amount = findViewById(R.id.et_expenseAmount_EditDataExpense);
        et_expenseDescription = findViewById(R.id.et_expenseDescription_EditDataExpense);

        cv_amount500 = findViewById(R.id.cv_amount500_EditDataExpense);
        cv_amount1000 = findViewById(R.id.cv_amount1000_EditDataExpense);
        cv_amount1500 = findViewById(R.id.cv_amount1500_EditDataExpense);
        cv_amount2000 = findViewById(R.id.cv_amount2000_EditDataExpense);
        cv_amount2500 = findViewById(R.id.cv_amount2500_EditDataExpense);
        cv_amount3000 = findViewById(R.id.cv_amount3000_EditDataExpense);
        cv_amount3500 = findViewById(R.id.cv_amount3500_EditDataExpense);
        cv_amount4000 = findViewById(R.id.cv_amount4000_EditDataExpense);
        cv_amount5000 = findViewById(R.id.cv_amount5000_EditDataExpense);
        cv_amount10000 = findViewById(R.id.cv_amount10000_EditDataExpense);
        cv_amount20000 = findViewById(R.id.cv_amount20000_EditDataExpense);
        cv_amount30000 = findViewById(R.id.cv_amount30000_EditDataExpense);
        cv_amount40000 = findViewById(R.id.cv_amount40000_EditDataExpense);
        cv_amount50000 = findViewById(R.id.cv_amount50000_EditDataExpense);
        cv_amount100000 = findViewById(R.id.cv_amount100000_EditDataExpense);
        cv_amount200000 = findViewById(R.id.cv_amount200000_EditDataExpense);
        cv_amount300000 = findViewById(R.id.cv_amount300000_EditDataExpense);
        cv_amount400000 = findViewById(R.id.cv_amount400000_EditDataExpense);
        cv_amount500000 = findViewById(R.id.cv_amount500000_EditDataExpense);

        tv_currentBalance_toolbar = findViewById(R.id.tv_currentBalanceValue_toolBar_EditDataExpense);
        iv_back = findViewById(R.id.iv_home_toolbar_EditDataExpense);



        //*************************************************Initializations*******************************************
        getSupportActionBar().hide();
        vm_EditDataExpense = ViewModelProviders.of(this).get(VM_EditDataExpense.class);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat simpleHourFormat = new SimpleDateFormat("hh");
        SimpleDateFormat simpleMinuteFormat = new SimpleDateFormat("mm");

        hour = Integer.parseInt(simpleHourFormat.format(System.currentTimeMillis()));
        minute = Integer.parseInt(simpleMinuteFormat.format(System.currentTimeMillis()));






        //***************************************************Click Listeners**************************************
        cv_back.setOnClickListener(v -> startActivity(new Intent(EditDataExpense.this,MainActivity.class)));
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
        cv_food.setOnClickListener(this);
        cv_business.setOnClickListener(this);
        cv_houseRent.setOnClickListener(this);
        cv_medicine.setOnClickListener(this);
        cv_lifeStyle.setOnClickListener(this);
        cv_education.setOnClickListener(this);
        cv_bills.setOnClickListener(this);
        cv_cloths.setOnClickListener(this);
        cv_transport.setOnClickListener(this);
        cv_other.setOnClickListener(this);

        cv_update.setOnClickListener(v -> updateExpense());


        tv_dateTime.setOnClickListener(v -> changeDate());

        iv_back.setOnClickListener(v -> {
            startActivity(new Intent(EditDataExpense.this,MainActivity.class));
        });





        //************************************************Starting Methods*****************************************
        tv_currentBalance_toolbar.setText(String.valueOf(UtilDB.currentBalance));
        observe();
        getIntentData();
        setViewWIthIntentData();


    }




    private void setViewWIthIntentData() {
        tv_dateTime.setText(vm_EditDataExpense.dateTime);
        et_amount.setText(vm_EditDataExpense.amount);
        et_expenseDescription.setText(expenseDescription);

        if(vm_EditDataExpense.category.equals(Constants.CATEGORY_FOOD)){
            markFood();
        }
        else if(vm_EditDataExpense.category.equals(Constants.CATEGORY_TRANSPORT)){
            markTransport();
        }
        else if(vm_EditDataExpense.category.equals(Constants.CATEGORY_BILLS)){
            markBills();
        }
        else if(vm_EditDataExpense.category.equals(Constants.CATEGORY_HOUSE_RENT)){
            markHouseRent();
        }
        else if(vm_EditDataExpense.category.equals(Constants.CATEGORY_BUSINESS)){
            markBusiness();
        }
        else if(vm_EditDataExpense.category.equals(Constants.CATEGORY_MEDICINE)){
            markMedicine();
        }
        else if(vm_EditDataExpense.category.equals(Constants.CATEGORY_CLOTHS)){
            markCloths();
        }
        else if(vm_EditDataExpense.category.equals(Constants.CATEGORY_EDUCATION)){
            markEducation();
        }
        else if(vm_EditDataExpense.category.equals(Constants.CATEGORY_LIFESTYLE)){
            markLifeStyle();
        }
        else if(vm_EditDataExpense.category.equals(Constants.CATEGORY_OTHER)){
            markOther();
        }



    }

    private void getIntentData() {
        ID = getIntent().getStringExtra(NodeName.ID);
        expenseDescription = getIntent().getStringExtra(NodeName.POST_DESCRIPTION);
        amount= getIntent().getStringExtra(NodeName.POST_AMOUNT);
        vm_EditDataExpense.dateTime = getIntent().getStringExtra(NodeName.POST_DATE_TIME);
        vm_EditDataExpense.category=getIntent().getStringExtra(NodeName.POST_CATEGORY);
        vm_EditDataExpense.year=getIntent().getStringExtra(NodeName.POST_YEAR);
        vm_EditDataExpense.month=getIntent().getStringExtra(NodeName.POST_MONTH);
        vm_EditDataExpense.day=getIntent().getStringExtra(NodeName.POST_DAY);
        vm_EditDataExpense.time=getIntent().getStringExtra(NodeName.POST_TIME);

    }


    private void updateExpense() {

        if(TextUtils.isEmpty(et_amount.getText())){
            et_amount.setError(getString(R.string.AmontNeeded));
        }

        else if(TextUtils.isEmpty(vm_EditDataExpense.category)) {
            Toast.makeText(this, getString(R.string.Pleaseselectacatagory), Toast.LENGTH_SHORT).show();
        }

        else{
            amount = et_amount.getText().toString();
            expenseDescription ="";

            if(!TextUtils.isEmpty(et_expenseDescription.getText())){
                expenseDescription = et_expenseDescription.getText().toString();
            }

            MC_Posts posts = new MC_Posts(expenseDescription, vm_EditDataExpense.category, Constants.TYPE_EXPENSE,amount, vm_EditDataExpense.year,
                    vm_EditDataExpense.month, vm_EditDataExpense.day, vm_EditDataExpense.time,String.valueOf(System.currentTimeMillis()), vm_EditDataExpense.dateTime);


            SQLiteHelper helper = new SQLiteHelper(EditDataExpense.this);
            helper.updateData(ID,posts);
            Toast.makeText(this, "Success "+ID, Toast.LENGTH_LONG).show();
            startActivity(new Intent(EditDataExpense.this,MainActivity.class));

        }

    }







    private void observe() {
        vm_EditDataExpense.mutable_category.observe(this, s -> {
            if(s.equals(Constants.CATEGORY_FOOD)){
                markFood();
            }

            else if(s.equals(Constants.CATEGORY_TRANSPORT)){
                markTransport();
            }

            else if(s.equals(Constants.CATEGORY_BILLS)){
                markBills();
            }

            else if(s.equals(Constants.CATEGORY_HOUSE_RENT)){
                markHouseRent();
            }

            else if(s.equals(Constants.CATEGORY_BUSINESS)){
                markBusiness();
            }

            else if(s.equals(Constants.CATEGORY_MEDICINE)){
                markMedicine();
            }

            else if(s.equals(Constants.CATEGORY_CLOTHS)){
                markCloths();
            }

            else if(s.equals(Constants.CATEGORY_EDUCATION)){
                markEducation();
            }

            else if(s.equals(Constants.CATEGORY_LIFESTYLE)){
                markLifeStyle();
            }

            else if(s.equals(Constants.CATEGORY_OTHER)){
                markOther();
            }
        });


        vm_EditDataExpense.mutable_amount.observe(this, new Observer<String>() {
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
        if(v.getId() == R.id.cv_amount500_EditDataExpense){
            vm_EditDataExpense.amount = Constants.AMOUNT_500;
            vm_EditDataExpense.mutable_amount.setValue(vm_EditDataExpense.amount);
        }
        else if(v.getId() == R.id.cv_amount1000_EditDataExpense){
            vm_EditDataExpense.amount = Constants.AMOUNT_1000;
            vm_EditDataExpense.mutable_amount.setValue(vm_EditDataExpense.amount);
        }

        else if(v.getId() == R.id.cv_amount1500_EditDataExpense){
            vm_EditDataExpense.amount = Constants.AMOUNT_1500;
            vm_EditDataExpense.mutable_amount.setValue(vm_EditDataExpense.amount);
        }

        else if(v.getId() == R.id.cv_amount2000_EditDataExpense){
            vm_EditDataExpense.amount = Constants.AMOUNT_2000;
            vm_EditDataExpense.mutable_amount.setValue(vm_EditDataExpense.amount);
        }

        else if(v.getId() == R.id.cv_amount2500_EditDataExpense){
            vm_EditDataExpense.amount = Constants.AMOUNT_2500;
            vm_EditDataExpense.mutable_amount.setValue(vm_EditDataExpense.amount);
        }

        else if(v.getId() == R.id.cv_amount3000_EditDataExpense){
            vm_EditDataExpense.amount = Constants.AMOUNT_3000;
            vm_EditDataExpense.mutable_amount.setValue(vm_EditDataExpense.amount);
        }

        else if(v.getId() == R.id.cv_amount3500_EditDataExpense){
            vm_EditDataExpense.amount = Constants.AMOUNT_3500;
            vm_EditDataExpense.mutable_amount.setValue(vm_EditDataExpense.amount);
        }

        else if(v.getId() == R.id.cv_amount4000_EditDataExpense){
            vm_EditDataExpense.amount = Constants.AMOUNT_4000;
            vm_EditDataExpense.mutable_amount.setValue(vm_EditDataExpense.amount);
        }

        else if(v.getId() == R.id.cv_amount5000_EditDataExpense){
            vm_EditDataExpense.amount = Constants.AMOUNT_5000;
            vm_EditDataExpense.mutable_amount.setValue(vm_EditDataExpense.amount);
        }

        else if(v.getId() == R.id.cv_amount10000_EditDataExpense){
            vm_EditDataExpense.amount = Constants.AMOUNT_10000;
            vm_EditDataExpense.mutable_amount.setValue(vm_EditDataExpense.amount);
        }

        else if(v.getId() == R.id.cv_amount20000_EditDataExpense){
            vm_EditDataExpense.amount = Constants.AMOUNT_20000;
            vm_EditDataExpense.mutable_amount.setValue(vm_EditDataExpense.amount);
        }

        else if(v.getId() == R.id.cv_amount30000_EditDataExpense){
            vm_EditDataExpense.amount = Constants.AMOUNT_30000;
            vm_EditDataExpense.mutable_amount.setValue(vm_EditDataExpense.amount);
        }

        else if(v.getId() == R.id.cv_amount40000_EditDataExpense){
            vm_EditDataExpense.amount = Constants.AMOUNT_40000;
            vm_EditDataExpense.mutable_amount.setValue(vm_EditDataExpense.amount);
        }

        else if(v.getId() == R.id.cv_amount50000_EditDataExpense){
            vm_EditDataExpense.amount = Constants.AMOUNT_50000;
            vm_EditDataExpense.mutable_amount.setValue(vm_EditDataExpense.amount);
        }

        else if(v.getId() == R.id.cv_amount100000_EditDataExpense){
            vm_EditDataExpense.amount = Constants.AMOUNT_100000;
            vm_EditDataExpense.mutable_amount.setValue(vm_EditDataExpense.amount);
        }

        else if(v.getId() == R.id.cv_amount200000_EditDataExpense){
            vm_EditDataExpense.amount = Constants.AMOUNT_200000;
            vm_EditDataExpense.mutable_amount.setValue(vm_EditDataExpense.amount);
        }

        else if(v.getId() == R.id.cv_amount300000_EditDataExpense){
            vm_EditDataExpense.amount = Constants.AMOUNT_300000;
            vm_EditDataExpense.mutable_amount.setValue(vm_EditDataExpense.amount);
        }

        else if(v.getId() == R.id.cv_amount400000_EditDataExpense){
            vm_EditDataExpense.amount = Constants.AMOUNT_400000;
            vm_EditDataExpense.mutable_amount.setValue(vm_EditDataExpense.amount);
        }

        else if(v.getId() == R.id.cv_amount500000_EditDataExpense){
            vm_EditDataExpense.amount = Constants.AMOUNT_500000;
            vm_EditDataExpense.mutable_amount.setValue(vm_EditDataExpense.amount);
        }


        //*************************************************category Selections*********************************



        else if(v.getId() == R.id.cv_food_EditDataExpense){
            vm_EditDataExpense.category = Constants.CATEGORY_FOOD;
            vm_EditDataExpense.mutable_category.setValue(vm_EditDataExpense.category);
        }

        else if(v.getId() == R.id.cv_business_EditDataExpense){
            vm_EditDataExpense.category = Constants.CATEGORY_BUSINESS;
            vm_EditDataExpense.mutable_category.setValue(vm_EditDataExpense.category);

        }

        else if(v.getId() == R.id.cv_houseRent_EditDataExpense){
            vm_EditDataExpense.category = Constants.CATEGORY_HOUSE_RENT;
            vm_EditDataExpense.mutable_category.setValue(vm_EditDataExpense.category);

        }

        else if(v.getId() == R.id.cv_transport_EditDataExpense){
            vm_EditDataExpense.category = Constants.CATEGORY_TRANSPORT;
            vm_EditDataExpense.mutable_category.setValue(vm_EditDataExpense.category);

        }

        else if(v.getId() == R.id.cv_cloths_EditDataExpense){
            vm_EditDataExpense.category = Constants.CATEGORY_CLOTHS;
            vm_EditDataExpense.mutable_category.setValue(vm_EditDataExpense.category);

        }

        else if(v.getId() == R.id.cv_bills_EditDataExpense){
            vm_EditDataExpense.category = Constants.CATEGORY_BILLS;
            vm_EditDataExpense.mutable_category.setValue(vm_EditDataExpense.category);

        }


        else if(v.getId() == R.id.cv_education_EditDataExpense){
            vm_EditDataExpense.category = Constants.CATEGORY_EDUCATION;
            vm_EditDataExpense.mutable_category.setValue(vm_EditDataExpense.category);

        }

        else if(v.getId() == R.id.cv_lifeStyle_EditDataExpense){
            vm_EditDataExpense.category = Constants.CATEGORY_LIFESTYLE;
            vm_EditDataExpense.mutable_category.setValue(vm_EditDataExpense.category);
        }

        else if(v.getId() == R.id.cv_medicine_EditDataExpense){
            vm_EditDataExpense.category = Constants.CATEGORY_MEDICINE;
            vm_EditDataExpense.mutable_category.setValue(vm_EditDataExpense.category);
        }

        else if(v.getId() == R.id.cv_other_EditDataExpense){
            vm_EditDataExpense.category = Constants.CATEGORY_OTHER;
            vm_EditDataExpense.mutable_category.setValue(vm_EditDataExpense.category);

        }
    }

    private void markOther() {
        cv_food.setCardBackgroundColor(getColor(R.color.white));
        cv_business.setCardBackgroundColor(getColor(R.color.white));
        cv_houseRent.setCardBackgroundColor(getColor(R.color.white));
        cv_transport.setCardBackgroundColor(getColor(R.color.white));
        cv_cloths.setCardBackgroundColor(getColor(R.color.white));
        cv_bills.setCardBackgroundColor(getColor(R.color.white));
        cv_education.setCardBackgroundColor(getColor(R.color.white));
        cv_lifeStyle.setCardBackgroundColor(getColor(R.color.white));
        cv_medicine.setCardBackgroundColor(getColor(R.color.white));
        cv_other.setCardBackgroundColor(getColor(R.color.pink));
    }

    private void markMedicine() {
        cv_food.setCardBackgroundColor(getColor(R.color.white));
        cv_business.setCardBackgroundColor(getColor(R.color.white));
        cv_houseRent.setCardBackgroundColor(getColor(R.color.white));
        cv_transport.setCardBackgroundColor(getColor(R.color.white));
        cv_cloths.setCardBackgroundColor(getColor(R.color.white));
        cv_bills.setCardBackgroundColor(getColor(R.color.white));
        cv_education.setCardBackgroundColor(getColor(R.color.white));
        cv_lifeStyle.setCardBackgroundColor(getColor(R.color.white));
        cv_medicine.setCardBackgroundColor(getColor(R.color.pink));
        cv_other.setCardBackgroundColor(getColor(R.color.white));
    }

    private void markLifeStyle() {
        cv_food.setCardBackgroundColor(getColor(R.color.white));
        cv_business.setCardBackgroundColor(getColor(R.color.white));
        cv_houseRent.setCardBackgroundColor(getColor(R.color.white));
        cv_transport.setCardBackgroundColor(getColor(R.color.white));
        cv_cloths.setCardBackgroundColor(getColor(R.color.white));
        cv_bills.setCardBackgroundColor(getColor(R.color.white));
        cv_education.setCardBackgroundColor(getColor(R.color.white));
        cv_lifeStyle.setCardBackgroundColor(getColor(R.color.pink));
        cv_medicine.setCardBackgroundColor(getColor(R.color.white));
        cv_other.setCardBackgroundColor(getColor(R.color.white));
    }

    private void markEducation() {
        cv_food.setCardBackgroundColor(getColor(R.color.white));
        cv_business.setCardBackgroundColor(getColor(R.color.white));
        cv_houseRent.setCardBackgroundColor(getColor(R.color.white));
        cv_transport.setCardBackgroundColor(getColor(R.color.white));
        cv_cloths.setCardBackgroundColor(getColor(R.color.white));
        cv_bills.setCardBackgroundColor(getColor(R.color.white));
        cv_education.setCardBackgroundColor(getColor(R.color.pink));
        cv_lifeStyle.setCardBackgroundColor(getColor(R.color.white));
        cv_medicine.setCardBackgroundColor(getColor(R.color.white));
        cv_other.setCardBackgroundColor(getColor(R.color.white));
    }

    private void markBills() {
        cv_food.setCardBackgroundColor(getColor(R.color.white));
        cv_business.setCardBackgroundColor(getColor(R.color.white));
        cv_houseRent.setCardBackgroundColor(getColor(R.color.white));
        cv_transport.setCardBackgroundColor(getColor(R.color.white));
        cv_cloths.setCardBackgroundColor(getColor(R.color.white));
        cv_bills.setCardBackgroundColor(getColor(R.color.pink));
        cv_education.setCardBackgroundColor(getColor(R.color.white));
        cv_lifeStyle.setCardBackgroundColor(getColor(R.color.white));
        cv_medicine.setCardBackgroundColor(getColor(R.color.white));
        cv_other.setCardBackgroundColor(getColor(R.color.white));
    }

    private void markCloths() {
        cv_food.setCardBackgroundColor(getColor(R.color.white));
        cv_business.setCardBackgroundColor(getColor(R.color.white));
        cv_houseRent.setCardBackgroundColor(getColor(R.color.white));
        cv_transport.setCardBackgroundColor(getColor(R.color.white));
        cv_cloths.setCardBackgroundColor(getColor(R.color.pink));
        cv_bills.setCardBackgroundColor(getColor(R.color.white));
        cv_education.setCardBackgroundColor(getColor(R.color.white));
        cv_lifeStyle.setCardBackgroundColor(getColor(R.color.white));
        cv_medicine.setCardBackgroundColor(getColor(R.color.white));
        cv_other.setCardBackgroundColor(getColor(R.color.white));
    }

    private void markTransport() {
        cv_food.setCardBackgroundColor(getColor(R.color.white));
        cv_business.setCardBackgroundColor(getColor(R.color.white));
        cv_houseRent.setCardBackgroundColor(getColor(R.color.white));
        cv_transport.setCardBackgroundColor(getColor(R.color.pink));
        cv_cloths.setCardBackgroundColor(getColor(R.color.white));
        cv_bills.setCardBackgroundColor(getColor(R.color.white));
        cv_education.setCardBackgroundColor(getColor(R.color.white));
        cv_lifeStyle.setCardBackgroundColor(getColor(R.color.white));
        cv_medicine.setCardBackgroundColor(getColor(R.color.white));
        cv_other.setCardBackgroundColor(getColor(R.color.white));
    }

    private void markHouseRent() {
        cv_food.setCardBackgroundColor(getColor(R.color.white));
        cv_business.setCardBackgroundColor(getColor(R.color.white));
        cv_houseRent.setCardBackgroundColor(getColor(R.color.pink));
        cv_transport.setCardBackgroundColor(getColor(R.color.white));
        cv_cloths.setCardBackgroundColor(getColor(R.color.white));
        cv_bills.setCardBackgroundColor(getColor(R.color.white));
        cv_education.setCardBackgroundColor(getColor(R.color.white));
        cv_lifeStyle.setCardBackgroundColor(getColor(R.color.white));
        cv_medicine.setCardBackgroundColor(getColor(R.color.white));
        cv_other.setCardBackgroundColor(getColor(R.color.white));
    }

    private void markBusiness() {
        cv_food.setCardBackgroundColor(getColor(R.color.white));
        cv_business.setCardBackgroundColor(getColor(R.color.pink));
        cv_houseRent.setCardBackgroundColor(getColor(R.color.white));
        cv_transport.setCardBackgroundColor(getColor(R.color.white));
        cv_cloths.setCardBackgroundColor(getColor(R.color.white));
        cv_bills.setCardBackgroundColor(getColor(R.color.white));
        cv_education.setCardBackgroundColor(getColor(R.color.white));
        cv_lifeStyle.setCardBackgroundColor(getColor(R.color.white));
        cv_medicine.setCardBackgroundColor(getColor(R.color.white));
        cv_other.setCardBackgroundColor(getColor(R.color.white));
    }

    private void markFood() {
        cv_food.setCardBackgroundColor(getColor(R.color.pink));
        cv_business.setCardBackgroundColor(getColor(R.color.white));
        cv_houseRent.setCardBackgroundColor(getColor(R.color.white));
        cv_transport.setCardBackgroundColor(getColor(R.color.white));
        cv_cloths.setCardBackgroundColor(getColor(R.color.white));
        cv_bills.setCardBackgroundColor(getColor(R.color.white));
        cv_education.setCardBackgroundColor(getColor(R.color.white));
        cv_lifeStyle.setCardBackgroundColor(getColor(R.color.white));
        cv_medicine.setCardBackgroundColor(getColor(R.color.white));
        cv_other.setCardBackgroundColor(getColor(R.color.white));
    }







    private void changeDate() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(EditDataExpense.this, (view, hourOfDay, minute) -> {
            vm_EditDataExpense.time = String.valueOf(hourOfDay)+" : "+String.valueOf(minute);
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
            tv_dateTime.setText(vm_EditDataExpense.day+"-"+ vm_EditDataExpense.month+"-"+ vm_EditDataExpense.year+"  "+hourInString+":"+String.valueOf(minute)+" "+am_pm);
        }, hour, minute, true);

        DatePickerDialog datePickerDialog = new DatePickerDialog(EditDataExpense.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                setDay(day);
                setMonth(month);
                if(day==1){
                    vm_EditDataExpense.day= getString(R.string.digit01);
                }
                vm_EditDataExpense.year = String.valueOf(year);
                vm_EditDataExpense.month = String.valueOf(month+1);
                vm_EditDataExpense.day = String.valueOf(dayOfMonth);
                timePickerDialog.show();

            }
        }, year, month, day);
        datePickerDialog.show();


    }

    private void setDay(int day) {
        if(day==1){
            vm_EditDataExpense.day=getString(R.string.digit01);
        }
        else if(day==2){
            vm_EditDataExpense.day=getString(R.string.digit02);
        }
        else if(day==3){
            vm_EditDataExpense.day=getString(R.string.digit03);
        }
        else if(day==4){
            vm_EditDataExpense.day=getString(R.string.digit04);
        }
        else if(day==5){
            vm_EditDataExpense.day=getString(R.string.digit05);
        }
        else if(day==6){
            vm_EditDataExpense.day=getString(R.string.digit06);
        }
        else if(day==7){
            vm_EditDataExpense.day=getString(R.string.digit07);
        }
        else if(day==8){
            vm_EditDataExpense.day=getString(R.string.digit08);
        }
        else if(day==9){
            vm_EditDataExpense.day=getString(R.string.digit09);
        }
        else {
            vm_EditDataExpense.day=String.valueOf(day);
        }

    }

    private void setMonth(int month) {
        if (month == 1) {
            vm_EditDataExpense.month = getString(R.string.digit01);
        } else if (month == 2) {
            vm_EditDataExpense.month = getString(R.string.digit02);
        } else if (month == 3) {
            vm_EditDataExpense.month = getString(R.string.digit03);
        } else if (month == 4) {
            vm_EditDataExpense.month = getString(R.string.digit04);
        } else if (month == 5) {
            vm_EditDataExpense.month = getString(R.string.digit05);
        } else if (month == 6) {
            vm_EditDataExpense.month = getString(R.string.digit06);
        } else if (month == 7) {
            vm_EditDataExpense.month = getString(R.string.digit07);
        } else if (month == 8) {
            vm_EditDataExpense.month = getString(R.string.digit08);
        } else if (month == 9) {
            vm_EditDataExpense.month = getString(R.string.digit09);
        } else {
            vm_EditDataExpense.month = String.valueOf(month);
        }
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }




}