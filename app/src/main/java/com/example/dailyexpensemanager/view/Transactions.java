package com.example.dailyexpensemanager.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dailyexpensemanager.Adapters.Adapter_Transactions;
import com.example.dailyexpensemanager.R;
import com.example.dailyexpensemanager.common.Constants;
import com.example.dailyexpensemanager.common.Extras;
import com.example.dailyexpensemanager.common.SQLiteHelper;
import com.example.dailyexpensemanager.common.UtilDB;
import com.example.dailyexpensemanager.model.MC_Posts;
import com.example.dailyexpensemanager.viewModels.VM_Transactions;

public class Transactions extends AppCompatActivity {
    private CardView cv_monthly, cv_daily,cv_fullReport, cv_deleteAll,cv_totalIncome,cv_totalExpense;
    private TextView tv_incomeValue_topBar,tv_expenseValue_topBar,tv_typeTitle;
    private TextView tv_currentBalance_toolbar;
    private ImageView iv_home,iv_reloadTransactions;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    private Toolbar toolbar;

    private int totalIncome=0,totalExpense=0,balance=0;
    private VM_Transactions vm_transactions;
    private Adapter_Transactions adapter_transactions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);


        //**********************************************View Bindings*******************************************
        toolbar = findViewById(R.id.toolbar_Transactions);
        cv_monthly = findViewById(R.id.cv_Monthly_Transactions);
        cv_daily = findViewById(R.id.cv_daily_Transactions);
        cv_fullReport = findViewById(R.id.cv_fullReport_Transactions);
        cv_totalIncome=findViewById(R.id.cv_totalIncomes_topBar_transactions);
        cv_totalExpense=findViewById(R.id.cv_totalExpenses_topBar_transactions);
        tv_incomeValue_topBar = findViewById(R.id.tv_totalIncomeValue_topBar_Transactions);
        tv_expenseValue_topBar = findViewById(R.id.tv_totalExpenseValue_topBar_Transactions);
        tv_typeTitle = findViewById(R.id.tv_typeTitle_Transactions);
        cv_deleteAll = findViewById(R.id.cv_pieChart_Transactions);
        recyclerView = findViewById(R.id.rv_Transactions);
        iv_reloadTransactions = findViewById(R.id.iv_reloadTransactions_Transactions);

        tv_currentBalance_toolbar = findViewById(R.id.tv_currentBalanceValue_toolBar_Transactions);
        iv_home = findViewById(R.id.iv_home_toolbar_Transactions);


        //***********************************************Initializations*****************************************
        getSupportActionBar().hide();
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        vm_transactions = ViewModelProviders.of(this).get(VM_Transactions.class);
        adapter_transactions = new Adapter_Transactions(vm_transactions.postsList,this);




        //************************************************Click Listeners****************************************

        cv_monthly.setOnClickListener(v -> {
            startActivity(new Intent(Transactions.this,MonthlyReport.class));
        });

        cv_daily.setOnClickListener(v -> {
            startActivity(new Intent(Transactions.this,Daily.class));
        });

        cv_fullReport.setOnClickListener(v -> {
            startActivity(new Intent(Transactions.this, FullReport.class));
        });


        cv_deleteAll.setOnClickListener(v -> {
            confirmDeleteAll();
        });

        iv_home.setOnClickListener((View v) -> {
            startActivity(new Intent(Transactions.this,MainActivity.class));
        });

        cv_totalIncome.setOnClickListener(v -> {
            new AllIncomeTask().execute();
        });


        iv_reloadTransactions.setOnClickListener(v -> new AllTransactionsTask().execute());


        cv_totalExpense.setOnClickListener(v -> new AllExpensesTask().execute());



        //************************************************Starting Methods**************************************
        tv_currentBalance_toolbar.setText(String.valueOf(UtilDB.currentBalance));
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
            tv_typeTitle.setText(getString(R.string.Transactions));
            toolbar.setTitle(getString(R.string.Transactions));
            tv_expenseValue_topBar.setText(String.valueOf(totalExpense));
            tv_incomeValue_topBar.setText(String.valueOf(totalIncome));
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
            tv_typeTitle.setText(getString(R.string.Expenses));
            toolbar.setTitle(getString(R.string.Expenses));
            tv_expenseValue_topBar.setText(String.valueOf(totalExpense));
            tv_incomeValue_topBar.setText(getString(R.string.value));
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
            tv_typeTitle.setText(getString(R.string.Incomes));
            toolbar.setTitle(getString(R.string.Incomes));
            tv_incomeValue_topBar.setText(String.valueOf(totalIncome));
            tv_expenseValue_topBar.setText(getString(R.string.value));
            recyclerView.setAdapter(adapter_transactions);
            adapter_transactions.notifyDataSetChanged();

        }
    }





    private void fetchAllTransactions(){
        totalIncome=0;
        totalExpense=0;
        vm_transactions.postsList.clear();
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

            if(postType.equals(Constants.TYPE_INCOME)){
                totalIncome=totalIncome+Integer.parseInt(postAmount);
            }
            else if(postType.equals(Constants.TYPE_EXPENSE)){
                totalExpense=totalExpense+Integer.parseInt(postAmount);
            }

        }

        cursor.close();
    }







    private void fetchAllExpenses(){
        totalExpense=0;
        totalIncome =0;
        vm_transactions.postsList.clear();
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadTypeWise(Constants.TYPE_EXPENSE);

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

           totalExpense=totalExpense+Integer.parseInt(postAmount);
        }

        cursor.close();
    }







    private void fetchAllIncomes(){
        totalExpense=0;
        totalIncome=0;
        vm_transactions.postsList.clear();
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadTypeWise(Constants.TYPE_INCOME);

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

            totalIncome = totalIncome+Integer.parseInt(postAmount);
        }


        cursor.close();

    }








    private void deleteDataAll() {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        sqLiteHelper.deleteDataAll();

        Intent intent = new Intent(Transactions.this,Transactions.class);
        intent.putExtra(Extras.TYPE,Constants.TYPE_ALL);
        UtilDB.currentBalance=0;
        startActivity(intent);
        finish();
    }






    public void deleteData(int id){
        SQLiteHelper sqLiteHelper = new SQLiteHelper(Transactions.this);
        Toast.makeText(this, "ID"+id, Toast.LENGTH_SHORT).show();
       sqLiteHelper.deleteData(id);
    }







    public void confirmDelete(int id,int amount,String typeOfTheFile) {
        Dialog dialog = new Dialog(Transactions.this);
        View view = LayoutInflater.from(Transactions.this).inflate(R.layout.dialog_delete,null);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();

        view.findViewById(R.id.btn_yes_alertImage_dialog_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(typeOfTheFile.equals(Constants.TYPE_INCOME)){
                    UtilDB.currentBalance=UtilDB.currentBalance-amount;
                }
                else if (typeOfTheFile.equals(Constants.TYPE_EXPENSE)){
                    UtilDB.currentBalance = UtilDB.currentBalance+amount;
                }
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






    public void confirmDeleteAll() {
        Dialog dialog = new Dialog(Transactions.this);
        View view = LayoutInflater.from(Transactions.this).inflate(R.layout.dialog_delete_all,null);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();

        view.findViewById(R.id.btn_yes_alertImage_dialog_deleteAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                deleteDataAll();

            }
        });

        view.findViewById(R.id.btn_no_alertImage_dialog_deleteAll).setOnClickListener(new View.OnClickListener() {
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