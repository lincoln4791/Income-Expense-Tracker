package com.example.dailyexpensemanager.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dailyexpensemanager.R;

public class AddIncome extends AppCompatActivity implements View.OnClickListener {

    CardView cv_back,cv_save,cv_salary,cv_business,cv_houseRent,cv_other,cv_calculator;
    CardView cv_amount500,cv_amount1000,cv_amount1500,cv_amount2000,cv_amount2500, cv_amount3000,cv_amount3500,cv_amount4000,cv_amount5000,
            cv_amount10000,cv_amount20000,cv_amount30000,cv_amount40000,cv_amount50000,cv_amount100000,cv_amount200000,cv_amount300000,
            cv_amount400000, cv_amount500000;
    TextView tv_changeDate;
    EditText et_amount,et_incomeDescription;

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




        //***************************************************Click Listeners**************************************
        cv_back.setOnClickListener(v -> startActivity(new Intent(AddIncome.this,MainActivity.class)));





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
        cv_salary.setOnClickListener(this);
        cv_business.setOnClickListener(this);
        cv_houseRent.setOnClickListener(this);
        cv_other.setOnClickListener(this);



    }



    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.cv_amount500_AddIncome){
            et_amount.setText(R.string.a500);
        }
        else if(v.getId() == R.id.cv_amount1000_AddIncome){
            et_amount.setText(R.string.a1000);
        }

        else if(v.getId() == R.id.cv_amount1500_AddIncome){
            et_amount.setText(R.string.a1500);
        }

        else if(v.getId() == R.id.cv_amount2000_AddIncome){
            et_amount.setText(R.string.a2000);
        }

        else if(v.getId() == R.id.cv_amount2500_AddIncome){
            et_amount.setText(R.string.a2500);
        }

        else if(v.getId() == R.id.cv_amount3000_AddIncome){
            et_amount.setText(R.string.a3000);
        }

        else if(v.getId() == R.id.cv_amount3500_AddIncome){
            et_amount.setText(R.string.a3500);
        }

        else if(v.getId() == R.id.cv_amount4000_AddIncome){
            et_amount.setText(R.string.a4000);
        }

        else if(v.getId() == R.id.cv_amount5000_AddIncome){
            et_amount.setText(R.string.a5000);
        }

        else if(v.getId() == R.id.cv_amount10000_AddIncome){
            et_amount.setText(R.string.a10000);
        }

        else if(v.getId() == R.id.cv_amount20000_AddIncome){
            et_amount.setText(R.string.a20000);
        }

        else if(v.getId() == R.id.cv_amount30000_AddIncome){
            et_amount.setText(R.string.a30000);
        }

        else if(v.getId() == R.id.cv_amount40000_AddIncome){
            et_amount.setText(R.string.a40000);
        }

        else if(v.getId() == R.id.cv_amount50000_AddIncome){
            et_amount.setText(R.string.a50000);
        }

        else if(v.getId() == R.id.cv_amount100000_AddIncome){
            et_amount.setText(R.string.a100000);
        }

        else if(v.getId() == R.id.cv_amount200000_AddIncome){
            et_amount.setText(R.string.a200000);
        }

        else if(v.getId() == R.id.cv_amount300000_AddIncome){
            et_amount.setText(R.string.a300000);
        }

        else if(v.getId() == R.id.cv_amount400000_AddIncome){
            et_amount.setText(R.string.a400000);
        }

        else if(v.getId() == R.id.cv_amount500000_AddIncome){
            et_amount.setText(R.string.a500000);
        }


        //*************************************************category Selections*********************************



        else if(v.getId() == R.id.cv_salary_AddIncome){
            cv_salary.setCardBackgroundColor(getColor(R.color.green));
            cv_business.setCardBackgroundColor(getColor(R.color.white));
            cv_houseRent.setCardBackgroundColor(getColor(R.color.white));
            cv_other.setCardBackgroundColor(getColor(R.color.white));
        }

        else if(v.getId() == R.id.cv_business_AddIncome){
            cv_salary.setCardBackgroundColor(getColor(R.color.white));
            cv_business.setCardBackgroundColor(getColor(R.color.green));
            cv_houseRent.setCardBackgroundColor(getColor(R.color.white));
            cv_other.setCardBackgroundColor(getColor(R.color.white));
        }

        else if(v.getId() == R.id.cv_houseRent_AddIncome){
            cv_salary.setCardBackgroundColor(getColor(R.color.white));
            cv_business.setCardBackgroundColor(getColor(R.color.white));
            cv_houseRent.setCardBackgroundColor(getColor(R.color.green));
            cv_other.setCardBackgroundColor(getColor(R.color.white));
        }

        else if(v.getId() == R.id.cv_other_AddIncome){
            cv_salary.setCardBackgroundColor(getColor(R.color.white));
            cv_business.setCardBackgroundColor(getColor(R.color.white));
            cv_houseRent.setCardBackgroundColor(getColor(R.color.white));
            cv_other.setCardBackgroundColor(getColor(R.color.green));
        }
    }
}