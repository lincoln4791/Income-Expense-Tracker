package com.example.dailyexpensemanager.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dailyexpensemanager.R;
import com.example.dailyexpensemanager.common.Constants;
import com.example.dailyexpensemanager.common.SQLiteHelper;
import com.example.dailyexpensemanager.model.MC_Posts;
import com.example.dailyexpensemanager.viewModels.VM_AddExpenses;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AddExpense extends AppCompatActivity implements View.OnClickListener {
    CardView cv_back,cv_save,cv_business,cv_houseRent,cv_food,cv_transport,cv_bills,cv_medicine,cv_cloths
            ,cv_education,cv_lifeStyle,cv_other,cv_calculator;
    CardView cv_amount500,cv_amount1000,cv_amount1500,cv_amount2000,cv_amount2500, cv_amount3000,cv_amount3500,cv_amount4000,cv_amount5000,
            cv_amount10000,cv_amount20000,cv_amount30000,cv_amount40000,cv_amount50000,cv_amount100000,cv_amount200000,cv_amount300000,
            cv_amount400000, cv_amount500000;
    TextView tv_changeDate,tv_dateTime;
    EditText et_amount,et_expenseDescription;

    VM_AddExpenses vm_addExpenses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);


        //***************************************************View Bindings*********************************************
        cv_back = findViewById(R.id.cv_back_AddExpense);
        cv_save = findViewById(R.id.cv_save_AddExpense);
        cv_business = findViewById(R.id.cv_business_AddExpense);
        cv_houseRent = findViewById(R.id.cv_houseRent_AddExpense);
        cv_food = findViewById(R.id.cv_food_AddExpense);
        cv_transport = findViewById(R.id.cv_transport_AddExpense);
        cv_bills = findViewById(R.id.cv_bills_AddExpense);
        cv_medicine = findViewById(R.id.cv_medicine_AddExpense);
        cv_cloths = findViewById(R.id.cv_cloths_AddExpense);
        cv_education = findViewById(R.id.cv_education_AddExpense);
        cv_lifeStyle = findViewById(R.id.cv_lifeStyle_AddExpense);
        cv_other = findViewById(R.id.cv_other_AddExpense);
        cv_calculator = findViewById(R.id.cv_calculator_AddExpense);
        tv_changeDate = findViewById(R.id.tv_changeDate_AddExpense);
        tv_dateTime = findViewById(R.id.tv_dateTime_AddExpense);
        et_amount = findViewById(R.id.et_expenseAmount_AddExpense);
        et_expenseDescription = findViewById(R.id.et_expenseDescription_AddExpense);

        cv_amount500 = findViewById(R.id.cv_amount500_AddExpense);
        cv_amount1000 = findViewById(R.id.cv_amount1000_AddExpense);
        cv_amount1500 = findViewById(R.id.cv_amount1500_AddExpense);
        cv_amount2000 = findViewById(R.id.cv_amount2000_AddExpense);
        cv_amount2500 = findViewById(R.id.cv_amount2500_AddExpense);
        cv_amount3000 = findViewById(R.id.cv_amount3000_AddExpense);
        cv_amount3500 = findViewById(R.id.cv_amount3500_AddExpense);
        cv_amount4000 = findViewById(R.id.cv_amount4000_AddExpense);
        cv_amount5000 = findViewById(R.id.cv_amount5000_AddExpense);
        cv_amount10000 = findViewById(R.id.cv_amount10000_AddExpense);
        cv_amount20000 = findViewById(R.id.cv_amount20000_AddExpense);
        cv_amount30000 = findViewById(R.id.cv_amount30000_AddExpense);
        cv_amount40000 = findViewById(R.id.cv_amount40000_AddExpense);
        cv_amount50000 = findViewById(R.id.cv_amount50000_AddExpense);
        cv_amount100000 = findViewById(R.id.cv_amount100000_AddExpense);
        cv_amount200000 = findViewById(R.id.cv_amount200000_AddExpense);
        cv_amount300000 = findViewById(R.id.cv_amount300000_AddExpense);
        cv_amount400000 = findViewById(R.id.cv_amount400000_AddExpense);
        cv_amount500000 = findViewById(R.id.cv_amount500000_AddExpense);




        //*************************************************Initializations*******************************************
        vm_addExpenses = ViewModelProviders.of(this).get(VM_AddExpenses.class);




        //***************************************************Click Listeners**************************************
        cv_back.setOnClickListener(v -> startActivity(new Intent(AddExpense.this,MainActivity.class)));
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

        cv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
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


    private void saveData() {

        if(TextUtils.isEmpty(et_amount.getText())){
            et_amount.setError(getString(R.string.AmontNeeded));
        }

        else if(TextUtils.isEmpty(vm_addExpenses.category)) {
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

            if(!TextUtils.isEmpty(et_expenseDescription.getText())){
                expenseDescription = et_expenseDescription.getText().toString();
            }

            MC_Posts posts = new MC_Posts(expenseDescription,vm_addExpenses.category,Constants.TYPE_EXPENSE,amount,year,
                    month,day,time,String.valueOf(System.currentTimeMillis()),dateTime);
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();



            SQLiteHelper helper = new SQLiteHelper(AddExpense.this);
            long flag =  helper.saveData(posts);
            Toast.makeText(this, "Flag "+flag, Toast.LENGTH_SHORT).show();

        }
    }





    private void observe() {
        vm_addExpenses.mutable_category.observe(this, s -> {
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


        vm_addExpenses.mutable_amount.observe(this, new Observer<String>() {
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
        if(v.getId() == R.id.cv_amount500_AddExpense){
            vm_addExpenses.amount = Constants.AMOUNT_500;
            vm_addExpenses.mutable_amount.setValue(vm_addExpenses.amount);
        }
        else if(v.getId() == R.id.cv_amount1000_AddExpense){
            vm_addExpenses.amount = Constants.AMOUNT_1000;
            vm_addExpenses.mutable_amount.setValue(vm_addExpenses.amount);
        }

        else if(v.getId() == R.id.cv_amount1500_AddExpense){
            vm_addExpenses.amount = Constants.AMOUNT_1500;
            vm_addExpenses.mutable_amount.setValue(vm_addExpenses.amount);
        }

        else if(v.getId() == R.id.cv_amount2000_AddExpense){
            vm_addExpenses.amount = Constants.AMOUNT_2000;
            vm_addExpenses.mutable_amount.setValue(vm_addExpenses.amount);
        }

        else if(v.getId() == R.id.cv_amount2500_AddExpense){
            vm_addExpenses.amount = Constants.AMOUNT_2500;
            vm_addExpenses.mutable_amount.setValue(vm_addExpenses.amount);
        }

        else if(v.getId() == R.id.cv_amount3000_AddExpense){
            vm_addExpenses.amount = Constants.AMOUNT_3000;
            vm_addExpenses.mutable_amount.setValue(vm_addExpenses.amount);
        }

        else if(v.getId() == R.id.cv_amount3500_AddExpense){
            vm_addExpenses.amount = Constants.AMOUNT_3500;
            vm_addExpenses.mutable_amount.setValue(vm_addExpenses.amount);
        }

        else if(v.getId() == R.id.cv_amount4000_AddExpense){
            vm_addExpenses.amount = Constants.AMOUNT_4000;
            vm_addExpenses.mutable_amount.setValue(vm_addExpenses.amount);
        }

        else if(v.getId() == R.id.cv_amount5000_AddExpense){
            vm_addExpenses.amount = Constants.AMOUNT_5000;
            vm_addExpenses.mutable_amount.setValue(vm_addExpenses.amount);
        }

        else if(v.getId() == R.id.cv_amount10000_AddExpense){
            vm_addExpenses.amount = Constants.AMOUNT_10000;
            vm_addExpenses.mutable_amount.setValue(vm_addExpenses.amount);
        }

        else if(v.getId() == R.id.cv_amount20000_AddExpense){
            vm_addExpenses.amount = Constants.AMOUNT_20000;
            vm_addExpenses.mutable_amount.setValue(vm_addExpenses.amount);
        }

        else if(v.getId() == R.id.cv_amount30000_AddExpense){
            vm_addExpenses.amount = Constants.AMOUNT_30000;
            vm_addExpenses.mutable_amount.setValue(vm_addExpenses.amount);
        }

        else if(v.getId() == R.id.cv_amount40000_AddExpense){
            vm_addExpenses.amount = Constants.AMOUNT_40000;
            vm_addExpenses.mutable_amount.setValue(vm_addExpenses.amount);
        }

        else if(v.getId() == R.id.cv_amount50000_AddExpense){
            vm_addExpenses.amount = Constants.AMOUNT_50000;
            vm_addExpenses.mutable_amount.setValue(vm_addExpenses.amount);
        }

        else if(v.getId() == R.id.cv_amount100000_AddExpense){
            vm_addExpenses.amount = Constants.AMOUNT_100000;
            vm_addExpenses.mutable_amount.setValue(vm_addExpenses.amount);
        }

        else if(v.getId() == R.id.cv_amount200000_AddExpense){
            vm_addExpenses.amount = Constants.AMOUNT_200000;
            vm_addExpenses.mutable_amount.setValue(vm_addExpenses.amount);
        }

        else if(v.getId() == R.id.cv_amount300000_AddExpense){
            vm_addExpenses.amount = Constants.AMOUNT_300000;
            vm_addExpenses.mutable_amount.setValue(vm_addExpenses.amount);
        }

        else if(v.getId() == R.id.cv_amount400000_AddExpense){
            vm_addExpenses.amount = Constants.AMOUNT_400000;
            vm_addExpenses.mutable_amount.setValue(vm_addExpenses.amount);
        }

        else if(v.getId() == R.id.cv_amount500000_AddExpense){
            vm_addExpenses.amount = Constants.AMOUNT_500000;
            vm_addExpenses.mutable_amount.setValue(vm_addExpenses.amount);
        }


        //*************************************************category Selections*********************************



        else if(v.getId() == R.id.cv_food_AddExpense){
            vm_addExpenses.category = Constants.CATEGORY_FOOD;
            vm_addExpenses.mutable_category.setValue(vm_addExpenses.category);
        }

        else if(v.getId() == R.id.cv_business_AddExpense){
            vm_addExpenses.category = Constants.CATEGORY_BUSINESS;
            vm_addExpenses.mutable_category.setValue(vm_addExpenses.category);

        }

        else if(v.getId() == R.id.cv_houseRent_AddExpense){
            vm_addExpenses.category = Constants.CATEGORY_HOUSE_RENT;
            vm_addExpenses.mutable_category.setValue(vm_addExpenses.category);

        }

        else if(v.getId() == R.id.cv_transport_AddExpense){
            vm_addExpenses.category = Constants.CATEGORY_TRANSPORT;
            vm_addExpenses.mutable_category.setValue(vm_addExpenses.category);

        }

        else if(v.getId() == R.id.cv_cloths_AddExpense){
            vm_addExpenses.category = Constants.CATEGORY_CLOTHS;
            vm_addExpenses.mutable_category.setValue(vm_addExpenses.category);

        }

        else if(v.getId() == R.id.cv_bills_AddExpense){
            vm_addExpenses.category = Constants.CATEGORY_BILLS;
            vm_addExpenses.mutable_category.setValue(vm_addExpenses.category);

        }


        else if(v.getId() == R.id.cv_education_AddExpense){
            vm_addExpenses.category = Constants.CATEGORY_EDUCATION;
            vm_addExpenses.mutable_category.setValue(vm_addExpenses.category);

        }

        else if(v.getId() == R.id.cv_lifeStyle_AddExpense){
            vm_addExpenses.category = Constants.CATEGORY_LIFESTYLE;
            vm_addExpenses.mutable_category.setValue(vm_addExpenses.category);
        }

        else if(v.getId() == R.id.cv_medicine_AddExpense){
            vm_addExpenses.category = Constants.CATEGORY_MEDICINE;
            vm_addExpenses.mutable_category.setValue(vm_addExpenses.category);
        }

        else if(v.getId() == R.id.cv_other_AddExpense){
            vm_addExpenses.category = Constants.CATEGORY_OTHER;
            vm_addExpenses.mutable_category.setValue(vm_addExpenses.category);

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
}