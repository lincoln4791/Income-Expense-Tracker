package com.example.dailyexpensemanager.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dailyexpensemanager.Adapters.Adapter_Daily;
import com.example.dailyexpensemanager.R;
import com.example.dailyexpensemanager.common.Constants;
import com.example.dailyexpensemanager.common.Extras;
import com.example.dailyexpensemanager.common.SQLiteHelper;
import com.example.dailyexpensemanager.common.UtilDB;
import com.example.dailyexpensemanager.model.MC_Posts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Daily extends AppCompatActivity {

    private CardView cv_fullReport,cv_monthly,cv_transactions,cv_date;
    private ImageButton ib_previous,ib_next;
    private TextView tv_totalIncome,tv_totalExpense,tv_date,tv_currentBalance_toolbar;
    private ImageView iv_home;

    private Adapter_Daily adapterDaily;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private String day,month,year,date;
    private List<MC_Posts> postList;
    private int totalIncome,totalExpense,tempMonth,tempYear,tempDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);



        //************************************************** View Bindings **********************************************
        cv_fullReport =findViewById(R.id.cv_FullReport_DailyReport);
        cv_monthly=findViewById(R.id.cv_monthlyTransactions_DailyReport);
        cv_transactions=findViewById(R.id.cv_transactions_DailyReport);
        cv_date=findViewById(R.id.cv_date_DailyReport);

        ib_next = findViewById(R.id.ib_nextDate_DailyReport);
        ib_previous = findViewById(R.id.ib_previousDate_DailyReport);

        tv_totalIncome = findViewById(R.id.tv_totalIncomeValue_Daily);
        tv_totalExpense = findViewById(R.id.tv_totalExpenseValue_Daily);
        tv_date = findViewById(R.id.tv_date_DailyReport);

        recyclerView = findViewById(R.id.rv_DailyReport);





        //*************************************************Initializations*********************************************
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList = new ArrayList<>();
        adapterDaily = new Adapter_Daily(postList,this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterDaily);

        Calendar calendar = Calendar.getInstance();
        tempMonth = calendar.get(Calendar.MONTH);
        tempYear = calendar.get(Calendar.YEAR);
        tempDay = calendar.get(Calendar.DAY_OF_MONTH);

        tv_currentBalance_toolbar = findViewById(R.id.tv_currentBalanceValue_toolBar_DailyActivity);
        iv_home = findViewById(R.id.iv_home_toolbar_DailyActivity);



        //***************************************************Initializations****************************************
        getSupportActionBar().hide();




        //************************************************ Click Listeners *******************************************
        cv_date.setOnClickListener(v -> changeDateAndFetchNewData());

        ib_next.setOnClickListener(v -> {
            loadNextDateData();
        });

        ib_previous.setOnClickListener(v -> loadPreviousDateData());

        cv_fullReport.setOnClickListener(v -> startActivity(new Intent(Daily.this,MainActivity.class)));

        cv_monthly.setOnClickListener(v -> startActivity(new Intent(Daily.this,MonthlyReport.class)));

        cv_transactions.setOnClickListener(v -> {
            Intent intent = new Intent(Daily.this,Transactions.class);
            intent.putExtra(Extras.TYPE, Constants.TYPE_ALL);
            startActivity(intent);
        });

        iv_home.setOnClickListener(v -> {
            startActivity(new Intent(Daily.this,FullReport.class));
        });






        //************************************************* Starting Methods *****************************************
        setDate();
        tv_currentBalance_toolbar.setText(String.valueOf(UtilDB.currentBalance));
        new YearMonthDayTask().execute();




    }




    private void loadPreviousDateData() {
        int monthValue = Integer.parseInt(month);
        int dayValue = Integer.parseInt(day);

        if (monthValue == 2 || monthValue == 4 || monthValue == 6 || monthValue == 8 || monthValue == 9 || monthValue == 11) {
            if (dayValue == 1) {
                monthValue = monthValue-1;
                dayValue=31;
                setMonthPlain(monthValue);
                setDay(dayValue);
            }
            else{
                day = String.valueOf(Integer.parseInt(day)-1);
            }
            date = day+"-"+month+"-"+year;
            tv_date.setText(date);
            new YearMonthDayTask().execute();

        }
        else if (monthValue == 5 || monthValue == 7 || monthValue == 10 || monthValue == 12) {
            if (dayValue == 1){
                monthValue = monthValue-1;
                dayValue = 30;
                setMonthPlain(monthValue);
                setDay(dayValue);
            }
            else{
                day = String.valueOf(Integer.parseInt(day)-1);
            }
            date = day+"-"+month+"-"+year;
            tv_date.setText(date);
            new YearMonthDayTask().execute();

        }
        else if (monthValue == 3) {
            if (dayValue == 1){
                monthValue = monthValue-1;
                dayValue = 28;
                setMonthPlain(monthValue);
                setDay(dayValue);
            }
            else{
                day = String.valueOf(Integer.parseInt(day)-1);
            }
            date = day+"-"+month+"-"+year;
            tv_date.setText(date);
            new YearMonthDayTask().execute();
        }



        else if (monthValue == 1) {
            if (dayValue == 1){
                year = String.valueOf(Integer.parseInt(year)-1);
                monthValue = 12;
                dayValue = 31;
                setMonthPlain(monthValue);
                setDay(dayValue);
            }
            else{
                day = String.valueOf(Integer.parseInt(day)-1);
            }
            date = day+"-"+month+"-"+year;
            tv_date.setText(date);
            new YearMonthDayTask().execute();
        }

    }




    private void loadNextDateData() {
        int monthValue = Integer.parseInt(month);
        int dayValue = Integer.parseInt(day);

        if (monthValue == 1 || monthValue == 3 || monthValue == 5 || monthValue == 7 || monthValue == 8 || monthValue == 10) {
            if (dayValue == 31) {
                monthValue = monthValue+1;
                dayValue=1;
                setMonthPlain(monthValue);
                setDay(dayValue);
            }
            else{
                day = String.valueOf(Integer.parseInt(day)+1);
            }
            date = day+"-"+month+"-"+year;
            tv_date.setText(date);
            new YearMonthDayTask().execute();


        }
        else if (monthValue == 4 || monthValue == 6 || monthValue == 9 || monthValue == 11) {
            if (dayValue == 30){
                monthValue = monthValue+1;
                dayValue = 1;
                setMonthPlain(monthValue);
                setDay(dayValue);
            }
            else{
                day = String.valueOf(Integer.parseInt(day)+1);
            }
            date = day+"-"+month+"-"+year;
            tv_date.setText(date);
            new YearMonthDayTask().execute();

        }
        else if (monthValue == 2) {
            if (dayValue == 28){
                monthValue = monthValue+1;
                dayValue = 1;
                setMonthPlain(monthValue);
                setDay(dayValue);
            }
            else{
                day = String.valueOf(Integer.parseInt(day)+1);
            }
            date = day+"-"+month+"-"+year;
            tv_date.setText(date);
            new YearMonthDayTask().execute();
        }

        else if (monthValue == 12) {
            if (dayValue == 31){
                year = String.valueOf(Integer.parseInt(year)+1);
                monthValue = 1;
                dayValue = 1;
                setMonthPlain(monthValue);
                setDay(dayValue);
            }
            else{
                day = String.valueOf(Integer.parseInt(day)+1);
            }
            date = day+"-"+month+"-"+year;
            tv_date.setText(date);
            new YearMonthDayTask().execute();
        }





    }

    private void setMonthPlain(int monthh) {
        if(monthh==1){
            month=getString(R.string.digit01);
        }
        else if(monthh==2){
            month=getString(R.string.digit02);
        }
        else if(monthh==3){
            month=getString(R.string.digit03);
        }
        else if(monthh==4){
            month=getString(R.string.digit04);
        }
        else if(monthh==5){
            month=getString(R.string.digit05);
        }
        else if(monthh==6){
            month=getString(R.string.digit06);
        }
        else if(monthh==7){
            month=getString(R.string.digit07);
        }
        else if(monthh==8){
            month=getString(R.string.digit08);
        }
        else if(monthh==9){
            month=getString(R.string.digit09);
        }
        else{
            month = String.valueOf(monthh);
        }
    }


    private void changeDateAndFetchNewData() {
        tempDay=Integer.parseInt(day);
        tempMonth=Integer.parseInt(month)-1;
        tempYear=Integer.parseInt(year);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Daily.this, (view, yearr, monthh, dayOfMonth) -> {
            year = String.valueOf(yearr);
            setMonth(monthh);
            setDay(dayOfMonth);

            date = day+"-"+month+"-"+year;
            tv_date.setText(date);
            new YearMonthDayTask().execute();

        },tempYear,tempMonth,tempDay);
        datePickerDialog.show();
    }


    private void setDay(int dayOfMonth) {
        if(dayOfMonth==1){
            day = getString(R.string.digit01);
        }

        else if(dayOfMonth==2){
            day = getString(R.string.digit02);
        }

        else if(dayOfMonth==3){
            day = getString(R.string.digit03);
        }
        else if(dayOfMonth==4){
            day = getString(R.string.digit04);
        }
        else if(dayOfMonth==5){
            day = getString(R.string.digit05);
        }
        else if(dayOfMonth==6){
            day = getString(R.string.digit06);
        }
        else if(dayOfMonth==7){
            day = getString(R.string.digit07);
        }else if(dayOfMonth==8){
            day = getString(R.string.digit08);
        }else if(dayOfMonth==9){
            day = getString(R.string.digit09);
        }
        else{
            day = String.valueOf(dayOfMonth);
        }




    }







    private void setMonth(int monthh) {
        if(monthh==0){
            month=getString(R.string.digit01);
        }
        else if(monthh==1){
            month=getString(R.string.digit02);
        }
        else if(monthh==2){
            month=getString(R.string.digit03);
        }
        else if(monthh==3){
            month=getString(R.string.digit04);
        }
        else if(monthh==4){
            month=getString(R.string.digit05);
        }
        else if(monthh==5){
            month=getString(R.string.digit06);
        }
        else if(monthh==6){
            month=getString(R.string.digit07);
        }
        else if(monthh==7){
            month=getString(R.string.digit08);
        }
        else if(monthh==8){
            month=getString(R.string.digit09);
        }
        else{
            month = String.valueOf(monthh+1);
        }

    }


    private void setDate() {
        SimpleDateFormat simpleDayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat simpleMonthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat simpleYearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());


        day = simpleDayFormat.format(System.currentTimeMillis());
        month = simpleMonthFormat.format(System.currentTimeMillis());
        year = simpleYearFormat.format(System.currentTimeMillis());
        date = day+"-"+month+"-"+year;
        tv_date.setText(date);


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
            tv_totalIncome.setText(String.valueOf(totalIncome));
            tv_totalExpense.setText(String.valueOf(totalExpense));
            recyclerView.setAdapter(adapterDaily);
            adapterDaily.notifyDataSetChanged();

        }
    }











    private void fetchYearMonthDayWise() {
        postList.clear();
        //tv_typeTitle.setText(getString(R.string.Category));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadYearMonthDayWise(year, month,day);
        while (cursor.moveToNext()) {
            int ID = cursor.getInt(0);
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

            MC_Posts post = new MC_Posts(ID,postDescription, postCategory, postType, postAmount, postYear, postMonth, postDay, postTime, timeStamp, postDateTime);
            postList.add(post);
        }

        cursor.close();
        calculateAll();

    }









    private void calculateAll(){
        totalIncome=0;
        totalExpense=0;
        Log.d("tag","list size : "+postList.size());

        for(int i = 0; i< postList.size() ; i++){
            if(postList.get(i).getPostType().equals(Constants.TYPE_INCOME)){

                totalIncome=totalIncome+Integer.parseInt(postList.get(i).getPostAmount());

            }
            else if (postList.get(i).getPostType().equals(Constants.TYPE_EXPENSE)){

                totalExpense = totalExpense+Integer.parseInt(postList.get(i).getPostAmount());
            }
        }

    }





    public void deleteData(int id){
        SQLiteHelper sqLiteHelper = new SQLiteHelper(Daily.this);
        Toast.makeText(this, "ID"+id, Toast.LENGTH_SHORT).show();
        sqLiteHelper.deleteData(id);
    }






    public void confirmDelete(int id) {
        Dialog dialog = new Dialog(Daily.this);
        View view = LayoutInflater.from(Daily.this).inflate(R.layout.dialog_delete,null);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();

        view.findViewById(R.id.btn_yes_alertImage_dialog_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                deleteData(id);

            }
        });

        view.findViewById(R.id.btn_no_alertImage_dialog_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }





}