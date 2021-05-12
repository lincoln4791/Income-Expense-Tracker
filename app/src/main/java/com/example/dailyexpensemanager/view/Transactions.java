package com.example.dailyexpensemanager.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dailyexpensemanager.Adapters.Adapter_Transactions;
import com.example.dailyexpensemanager.R;
import com.example.dailyexpensemanager.common.Constants;
import com.example.dailyexpensemanager.common.Extras;
import com.example.dailyexpensemanager.common.SQLiteHelper;
import com.example.dailyexpensemanager.model.MC_Posts;
import com.example.dailyexpensemanager.viewModels.VM_Transactions;

public class Transactions extends AppCompatActivity {
    private CardView cv_today,cv_home,cv_fullReport,cv_pieChart;
    private TextView tv_incomeValue_topBar,tv_expenseValue_topBar,tv_balanceValue_topBar,tv_typeTitle;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

    private VM_Transactions vm_transactions;
    private Adapter_Transactions adapter_transactions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);


        //**********************************************View Bindings*******************************************
        cv_today = findViewById(R.id.cv_today_Transactions);
        cv_home = findViewById(R.id.cv_home_Transactions);
        cv_fullReport = findViewById(R.id.cv_fullReport_Transactions);
        tv_incomeValue_topBar = findViewById(R.id.tv_totalIncomeValue_topBar_Transactions);
        tv_expenseValue_topBar = findViewById(R.id.tv_totalExpenseValue_topBar_Transactions);
        tv_balanceValue_topBar = findViewById(R.id.tv_totalBalanceValue_topBar_Transactions);
        tv_typeTitle = findViewById(R.id.tv_typeTitle_Transactions);
        cv_pieChart = findViewById(R.id.cv_pieChart_Transactions);
        recyclerView = findViewById(R.id.rv_Transactions);


        //***********************************************Initializations*****************************************
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        vm_transactions = ViewModelProviders.of(this).get(VM_Transactions.class);
        adapter_transactions = new Adapter_Transactions(vm_transactions.postsList,this);




        //************************************************Click Listeners****************************************

        cv_today.setOnClickListener(v -> {

        });

        cv_home.setOnClickListener(v -> {
            startActivity(new Intent(Transactions.this,MainActivity.class));
        });

        cv_fullReport.setOnClickListener(v -> {
            startActivity(new Intent(Transactions.this, FullReport.class));
        });


        cv_pieChart.setOnClickListener(v -> {
            startActivity(new Intent(Transactions.this,PieChart.class));
        });





        //************************************************Starting Methods**************************************

        getIntentData();


    }

    private void getIntentData() {
        if(getIntent().getStringExtra(Extras.TYPE).equals(Constants.TYPE_INCOME)){
            new AllIncomeTask().execute();
        }

        else if(getIntent().getStringExtra(Extras.TYPE).equals(Constants.TYPE_EXPENSE)){
           new AllExpensesTask().execute();
        }

        else {
            new AllTransactionsTask().execute();
        }


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
            recyclerView.setAdapter(adapter_transactions);
            adapter_transactions.notifyDataSetChanged();

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
            recyclerView.setAdapter(adapter_transactions);
            adapter_transactions.notifyDataSetChanged();

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
            recyclerView.setAdapter(adapter_transactions);
            adapter_transactions.notifyDataSetChanged();

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
            recyclerView.setAdapter(adapter_transactions);
            adapter_transactions.notifyDataSetChanged();

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
            recyclerView.setAdapter(adapter_transactions);
            adapter_transactions.notifyDataSetChanged();

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
            recyclerView.setAdapter(adapter_transactions);
            adapter_transactions.notifyDataSetChanged();

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
            recyclerView.setAdapter(adapter_transactions);
            adapter_transactions.notifyDataSetChanged();

        }
    }










    class YearMonthTypeTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            fetchYearMonthTypeWise();
            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            recyclerView.setAdapter(adapter_transactions);
            adapter_transactions.notifyDataSetChanged();

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
            recyclerView.setAdapter(adapter_transactions);
            adapter_transactions.notifyDataSetChanged();

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
            recyclerView.setAdapter(adapter_transactions);
            adapter_transactions.notifyDataSetChanged();

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
            recyclerView.setAdapter(adapter_transactions);
            adapter_transactions.notifyDataSetChanged();

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
            recyclerView.setAdapter(adapter_transactions);
            adapter_transactions.notifyDataSetChanged();

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
            recyclerView.setAdapter(adapter_transactions);
            adapter_transactions.notifyDataSetChanged();

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
            recyclerView.setAdapter(adapter_transactions);
            adapter_transactions.notifyDataSetChanged();

        }
    }












    private void fetchAllTransactions(){
        vm_transactions.postsList.clear();
        tv_typeTitle.setText(getString(R.string.Transactions));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadAllTransactions();

        while(cursor.moveToNext()){
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

            MC_Posts post = new MC_Posts(ID,postDescription,postCategory,postType,postAmount,postYear,postMonth,postDay,postTime,timeStamp,postDateTime);
            vm_transactions.postsList.add(post);
        }

        cursor.close();

    }







    private void fetchAllExpenses(){
        vm_transactions.postsList.clear();
        tv_typeTitle.setText(getString(R.string.Expenses));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadTypeWise(Constants.TYPE_EXPENSE);

        while(cursor.moveToNext()){
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

            MC_Posts post = new MC_Posts(postDescription,postCategory,postType,postAmount,postYear,postMonth,postDay,postTime,timeStamp,postDateTime);
            vm_transactions.postsList.add(post);
        }

        cursor.close();

    }








    private void fetchAllIncomes(){
        vm_transactions.postsList.clear();
        tv_typeTitle.setText(getString(R.string.Incomes));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadTypeWise(Constants.TYPE_INCOME);

        while(cursor.moveToNext()){
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
            MC_Posts post = new MC_Posts(postDescription,postCategory,postType,postAmount,postYear,postMonth,postDay,postTime,timeStamp,postDateTime);
            vm_transactions.postsList.add(post);
        }

        cursor.close();

    }








    private void fetchYearWise(){
        vm_transactions.postsList.clear();
        tv_typeTitle.setText(getString(R.string.YearWise));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadYearWise(getString(R.string.year2021));

        while(cursor.moveToNext()){
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

            MC_Posts post = new MC_Posts(postDescription,postCategory,postType,postAmount,postYear,postMonth,postDay,postTime,timeStamp,postDateTime);
            vm_transactions.postsList.add(post);
        }

        cursor.close();

    }





    private void fetchYearTypeWise(){
        vm_transactions.postsList.clear();
        tv_typeTitle.setText(getString(R.string.Category));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadYearTypeWise("2021",Constants.TYPE_EXPENSE);

        while(cursor.moveToNext()){
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

            MC_Posts post = new MC_Posts(postDescription,postCategory,postType,postAmount,postYear,postMonth,postDay,postTime,timeStamp,postDateTime);
            vm_transactions.postsList.add(post);
        }

        cursor.close();

    }






    private void fetchYearCategoryWise(){
        vm_transactions.postsList.clear();
        tv_typeTitle.setText(getString(R.string.Category));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadYearCategoryWise("2021",Constants.CATEGORY_HOUSE_RENT);

        while(cursor.moveToNext()){
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

            MC_Posts post = new MC_Posts(postDescription,postCategory,postType,postAmount,postYear,postMonth,postDay,postTime,timeStamp,postDateTime);
            vm_transactions.postsList.add(post);
        }

        cursor.close();

    }




    private void fetchYearTypeCategoryWise(){
        vm_transactions.postsList.clear();
        tv_typeTitle.setText(getString(R.string.Category));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadYearTypeCategoryWise("2021",Constants.TYPE_EXPENSE,Constants.CATEGORY_HOUSE_RENT);

        while(cursor.moveToNext()){
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

            MC_Posts post = new MC_Posts(postDescription,postCategory,postType,postAmount,postYear,postMonth,postDay,postTime,timeStamp,postDateTime);
            vm_transactions.postsList.add(post);
        }

        cursor.close();

    }








    private void fetchYearMonthTypeWise(){
        vm_transactions.postsList.clear();
        tv_typeTitle.setText(getString(R.string.Category));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadYearMonthTypeWise("2021","05",Constants.TYPE_INCOME);

        while(cursor.moveToNext()){
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

            MC_Posts post = new MC_Posts(postDescription,postCategory,postType,postAmount,postYear,postMonth,postDay,postTime,timeStamp,postDateTime);
            vm_transactions.postsList.add(post);
        }

        cursor.close();

    }








    private void fetchYearMonthCategoryWise(){
        vm_transactions.postsList.clear();
        tv_typeTitle.setText(getString(R.string.Category));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadYearMonthCategoryWise("2021","05",Constants.CATEGORY_OTHER);

        while(cursor.moveToNext()){
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

            MC_Posts post = new MC_Posts(postDescription,postCategory,postType,postAmount,postYear,postMonth,postDay,postTime,timeStamp,postDateTime);
            vm_transactions.postsList.add(post);
        }

        cursor.close();

    }







    private void fetchYearMonthTypeCategoryWise(){
        vm_transactions.postsList.clear();
        tv_typeTitle.setText(getString(R.string.Category));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadYearMonthTypeCategoryWise("2021","05",Constants.TYPE_INCOME,Constants.CATEGORY_OTHER);

        while(cursor.moveToNext()){
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

            MC_Posts post = new MC_Posts(postDescription,postCategory,postType,postAmount,postYear,postMonth,postDay,postTime,timeStamp,postDateTime);
            vm_transactions.postsList.add(post);
        }

        cursor.close();

    }






    private void fetchYearMonthDayWise(){
        vm_transactions.postsList.clear();
        tv_typeTitle.setText(getString(R.string.Category));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadYearMonthDayWise("2021","05","09");
        while(cursor.moveToNext()){
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

            MC_Posts post = new MC_Posts(postDescription,postCategory,postType,postAmount,postYear,postMonth,postDay,postTime,timeStamp,postDateTime);
            vm_transactions.postsList.add(post);
        }

        cursor.close();

    }






    private void fetchYearMonthDayTypeWise(){
        vm_transactions.postsList.clear();
        tv_typeTitle.setText(getString(R.string.Category));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadYearMonthDayTypeWise("2021","05","09",Constants.TYPE_INCOME);
        while(cursor.moveToNext()){
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

            MC_Posts post = new MC_Posts(postDescription,postCategory,postType,postAmount,postYear,postMonth,postDay,postTime,timeStamp,postDateTime);
            vm_transactions.postsList.add(post);
        }

        cursor.close();

    }






    private void fetchYearMonthDayCategoryWise(){
        vm_transactions.postsList.clear();
        tv_typeTitle.setText(getString(R.string.Category));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadYearMonthDayTypeWise("2021","05","09",Constants.CATEGORY_OTHER);
        while(cursor.moveToNext()){
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

            MC_Posts post = new MC_Posts(postDescription,postCategory,postType,postAmount,postYear,postMonth,postDay,postTime,timeStamp,postDateTime);
            vm_transactions.postsList.add(post);
        }

        cursor.close();

    }





    private void fetchYearMonthDayTypeCategoryWise(){
        vm_transactions.postsList.clear();
        tv_typeTitle.setText(getString(R.string.YearWise));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadYearMonthDayTypeCategoryWise("2021","05","09",Constants.TYPE_INCOME,Constants.CATEGORY_SALARY);

        while(cursor.moveToNext()){
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

            MC_Posts post = new MC_Posts(ID,postDescription,postCategory,postType,postAmount,postYear,postMonth,postDay,postTime,timeStamp,postDateTime);
            vm_transactions.postsList.add(post);
        }

        cursor.close();

    }





    public void deleteData(int id){
        SQLiteHelper sqLiteHelper = new SQLiteHelper(Transactions.this);
        Toast.makeText(this, "ID"+id, Toast.LENGTH_SHORT).show();
       sqLiteHelper.deleteData(id);
    }












    public void confirmDelete(int id) {
        Dialog dialog = new Dialog(Transactions.this);
        View view = LayoutInflater.from(Transactions.this).inflate(R.layout.dialog_delete,null);
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
        finish();
        super.onBackPressed();
    }



}