package com.example.dailyexpensemanager.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dailyexpensemanager.R;

public class AddExpense extends AppCompatActivity implements View.OnClickListener {
    CardView cv_back,cv_save,cv_business,cv_houseRent,cv_food,cv_transport,cv_bills,cv_medicine,cv_cloths
            ,cv_education,cv_lifeStyle,cv_other,cv_calculator;
    CardView cv_amount500,cv_amount1000,cv_amount1500,cv_amount2000,cv_amount2500, cv_amount3000,cv_amount3500,cv_amount4000,cv_amount5000,
            cv_amount10000,cv_amount20000,cv_amount30000,cv_amount40000,cv_amount50000,cv_amount100000,cv_amount200000,cv_amount300000,
            cv_amount400000, cv_amount500000;
    TextView tv_changeDate;
    EditText et_amount,et_expenseDescription;


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
        tv_changeDate = findViewById(R.id.tc_changeDate_AddExpense);
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




        //***************************************************Click Listeners**************************************
        cv_back.setOnClickListener(v -> startActivity(new Intent(AddExpense.this,MainActivity.class)));





        //************************************************Starting Methods*****************************************
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





    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.cv_amount500_AddExpense){
            et_amount.setText(R.string.a500);
        }
        else if(v.getId() == R.id.cv_amount1000_AddExpense){
            et_amount.setText(R.string.a1000);
        }

        else if(v.getId() == R.id.cv_amount1500_AddExpense){
            et_amount.setText(R.string.a1500);
        }

        else if(v.getId() == R.id.cv_amount2000_AddExpense){
            et_amount.setText(R.string.a2000);
        }

        else if(v.getId() == R.id.cv_amount2500_AddExpense){
            et_amount.setText(R.string.a2500);
        }

        else if(v.getId() == R.id.cv_amount3000_AddExpense){
            et_amount.setText(R.string.a3000);
        }

        else if(v.getId() == R.id.cv_amount3500_AddExpense){
            et_amount.setText(R.string.a3500);
        }

        else if(v.getId() == R.id.cv_amount4000_AddExpense){
            et_amount.setText(R.string.a4000);
        }

        else if(v.getId() == R.id.cv_amount5000_AddExpense){
            et_amount.setText(R.string.a5000);
        }

        else if(v.getId() == R.id.cv_amount10000_AddExpense){
            et_amount.setText(R.string.a10000);
        }

        else if(v.getId() == R.id.cv_amount20000_AddExpense){
            et_amount.setText(R.string.a20000);
        }

        else if(v.getId() == R.id.cv_amount30000_AddExpense){
            et_amount.setText(R.string.a30000);
        }

        else if(v.getId() == R.id.cv_amount40000_AddExpense){
            et_amount.setText(R.string.a40000);
        }

        else if(v.getId() == R.id.cv_amount50000_AddExpense){
            et_amount.setText(R.string.a50000);
        }

        else if(v.getId() == R.id.cv_amount100000_AddExpense){
            et_amount.setText(R.string.a100000);
        }

        else if(v.getId() == R.id.cv_amount200000_AddExpense){
            et_amount.setText(R.string.a200000);
        }

        else if(v.getId() == R.id.cv_amount300000_AddExpense){
            et_amount.setText(R.string.a300000);
        }

        else if(v.getId() == R.id.cv_amount400000_AddExpense){
            et_amount.setText(R.string.a400000);
        }

        else if(v.getId() == R.id.cv_amount500000_AddExpense){
            et_amount.setText(R.string.a500000);
        }


        //*************************************************category Selections*********************************



        else if(v.getId() == R.id.cv_food_AddExpense){
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

        else if(v.getId() == R.id.cv_business_AddExpense){
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

        else if(v.getId() == R.id.cv_houseRent_AddExpense){
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

        else if(v.getId() == R.id.cv_transport_AddExpense){
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

        else if(v.getId() == R.id.cv_cloths_AddExpense){
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

        else if(v.getId() == R.id.cv_bills_AddExpense){
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


        else if(v.getId() == R.id.cv_education_AddExpense){
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

        else if(v.getId() == R.id.cv_lifeStyle_AddExpense){
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

        else if(v.getId() == R.id.cv_medicine_AddExpense){
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

        else if(v.getId() == R.id.cv_other_AddExpense){
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

    }
}