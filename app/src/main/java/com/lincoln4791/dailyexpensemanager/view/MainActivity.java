package com.lincoln4791.dailyexpensemanager.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.lincoln4791.dailyexpensemanager.R;
import com.lincoln4791.dailyexpensemanager.common.Constants;
import com.lincoln4791.dailyexpensemanager.common.Extras;
import com.lincoln4791.dailyexpensemanager.common.SQLiteHelper;
import com.lincoln4791.dailyexpensemanager.common.UtilDB;
import com.lincoln4791.dailyexpensemanager.model.MC_Posts;
import com.opencsv.CSVReader;
import com.opencsv.enums.CSVReaderNullFieldIndicator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private CardView cv_addIncome,cv_addExpense,cv_transactions,cv_fullReport,cv_incomes,cv_expenses,cv_incomesTopBar,
            cv_expensesTopBar, cv_daily,cv_monthly,cv_about,cv_backupData,cv_restoreData;
    private TextView tv_totalIncome,tv_totalExpense,tv_balance,tv_currentBalance_toolbar;
    private int totalIncome=0,totalExpenses=0;
    private Spinner spinner;
    private ImageView iv_back;

    private List<MC_Posts> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //************************************************* View Bindings***************************************
        cv_addIncome = findViewById(R.id.cv_addIncome_MainActivity);
        cv_addExpense = findViewById(R.id.cv_addExpenses_MainActivity);
        cv_fullReport = findViewById(R.id.cv_fullReport_MainActivity);
        cv_daily = findViewById(R.id.cv_daily_MainActivity);
        cv_monthly = findViewById(R.id.cv_monthly_MainActivity);
        cv_transactions = findViewById(R.id.cv_transactions_MainActivity);
        cv_incomes = findViewById(R.id.cv_income_MainActivity);
        cv_expenses = findViewById(R.id.cv_expenses_MainActivity);
        cv_incomesTopBar = findViewById(R.id.cv_totalIncomes_topBar_MainActivity);
        cv_expensesTopBar = findViewById(R.id.cv_totalExpenses_topBar_MainActivity);
        tv_totalIncome = findViewById(R.id.tv_totalIncomeValue_topBar_MainActivity);
        tv_totalExpense = findViewById(R.id.tv_totalExpenseValue_topBar_MainActivity);
        tv_balance = findViewById(R.id.tv_balanceValue_topBar_MainActivity);
        tv_currentBalance_toolbar = findViewById(R.id.tv_currentBalanceValue_toolBar_MainActivity);
        cv_about = findViewById(R.id.cv_about_MainActivity);
        cv_backupData = findViewById(R.id.cv_backupData_MainActivity);
        cv_restoreData = findViewById(R.id.cv_restoreData_MainActivity);





        //**********************************************Initializations****************************************
        postList = new ArrayList<>();
        getSupportActionBar().hide();









    //*************************************************** Click Listeners****************************************
        cv_addIncome.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,AddIncome.class)));

        cv_addExpense.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,AddExpense.class)));

        cv_fullReport.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, FullReport.class)));

        cv_transactions.setOnClickListener(v -> {
            Intent transactionsIntent = new Intent(MainActivity.this,Transactions.class);
            transactionsIntent.putExtra(Extras.TYPE, Constants.TYPE_ALL);
            startActivity(transactionsIntent);
        });

        cv_incomes.setOnClickListener(v -> {
            Intent incomeIntent = new Intent(MainActivity.this,Transactions.class);
            incomeIntent.putExtra(Extras.TYPE, Constants.TYPE_INCOME);
            startActivity(incomeIntent);
        });

        cv_expenses.setOnClickListener(v -> {
            Intent incomeIntent = new Intent(MainActivity.this,Transactions.class);
            incomeIntent.putExtra(Extras.TYPE, Constants.TYPE_EXPENSE);
            startActivity(incomeIntent);
        });


        cv_daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Daily.class));
            }
        });


        cv_monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MonthlyReport.class));
            }
        });

        cv_incomesTopBar.setOnClickListener(v -> {
            Intent incomeIntent = new Intent(MainActivity.this,Transactions.class);
            incomeIntent.putExtra(Extras.TYPE, Constants.TYPE_INCOME);
            startActivity(incomeIntent);
        });

        cv_expensesTopBar.setOnClickListener(v -> {
            Intent expenseIntent = new Intent(MainActivity.this,Transactions.class);
            expenseIntent.putExtra(Extras.TYPE, Constants.TYPE_EXPENSE);
            startActivity(expenseIntent);
        });

        cv_about.setOnClickListener(v -> openAbout());


        cv_backupData.setOnClickListener(v -> {
                backupData();
        });


        cv_restoreData.setOnClickListener(v -> restoreData());








        //**********************************************Starting Methods***************************************
        setIncomeExpenseValues();


    }







    private void setIncomeExpenseValues() {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(MainActivity.this);
        Cursor cursor = sqLiteHelper.loadAllTransactions();
        while (cursor.moveToNext()) {
            String postType = cursor.getString(3);
            int amount = Integer.parseInt(cursor.getString(4));

            if(postType.equals(Constants.TYPE_INCOME)){
                totalIncome = totalIncome+amount;
            }
            else if (postType.equals(Constants.TYPE_EXPENSE)){
                totalExpenses = totalExpenses+amount;
            }
        }

        UtilDB.currentBalance = totalIncome-totalExpenses;

        tv_totalIncome.setText(String.valueOf(totalIncome));
        tv_totalExpense.setText(String.valueOf(totalExpenses));
        tv_balance.setText(String.valueOf(UtilDB.currentBalance));
        tv_currentBalance_toolbar.setText(String.valueOf(UtilDB.currentBalance));
        cursor.close();
    }


    @Override
    public void onBackPressed() {
        confirmLogout();
    }



    private void confirmLogout() {
        Dialog dialog = new Dialog(MainActivity.this);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_exit,null);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();

        view.findViewById(R.id.btn_yes_alertImage_dialog_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
                finishAffinity();

            }
        });

        view.findViewById(R.id.btn_no_alertImage_dialog_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



    }








    private void openAbout() {
        Dialog dialog = new Dialog(MainActivity.this);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_about,null);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();

        view.findViewById(R.id.btn_ok_dilogue_about).setOnClickListener(v -> {
            dialog.dismiss();
        });

        view.findViewById(R.id.btn_rateApp_dilogue_about).setOnClickListener(v -> {
            dialog.dismiss();

        });
    }









    private void restoreData() {
        Toast.makeText(MainActivity.this, "permission Checking", Toast.LENGTH_SHORT).show();
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Toast.makeText(MainActivity.this, "permission Granted", Toast.LENGTH_SHORT).show();
                importCSV();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).onSameThread().check();

    }




    private void backupData() {
        Toast.makeText(this, "checking ", Toast.LENGTH_SHORT).show();
            Dexter.withContext(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                    Toast.makeText(MainActivity.this, "Granted ", Toast.LENGTH_SHORT).show();
                    new AllTransactionsTask().execute();
                }

                @Override
                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                }

                @Override
                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).onSameThread().check();
    }

    private void exportCSV() {
        File folder = new File(getFilesDir(),"SQLiteBackup");
        folder.delete();
        folder.mkdir();

        String fileName = "incomeExpenseManager.csv";
        String filePathAndName = folder.toString()+fileName;
        try {
            FileWriter fw = new FileWriter(filePathAndName);
            fw.write("");
            for(int i = 0 ; i<postList.size() ; i++){
                fw.append(""+postList.get(i).getID()+",");
                fw.append(""+postList.get(i).getPostDescription()+",");
                fw.append(""+postList.get(i).getPostCategory()+",");
                fw.append(""+postList.get(i).getPostType()+",");
                fw.append(""+postList.get(i).getPostAmount()+",");
                fw.append(""+postList.get(i).getPostYear()+",");
                fw.append(""+postList.get(i).getPostMonth()+",");
                fw.append(""+postList.get(i).getPostDay()+",");
                fw.append(""+postList.get(i).getPostTime()+",");
                fw.append(""+postList.get(i).getTimeStamp()+",");
                fw.append(""+postList.get(i).getPostDateTime()+",");
                fw.append("\n");
            }

            fw.flush();
            fw.close();

            startActivity(new Intent(this,MainActivity.class));
            Toast.makeText(this, "BackUp Successful", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Toast.makeText(this, "BackUp Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("tag","fail "+e.getMessage());
            e.printStackTrace();
        }



    }




    private void importCSV() {
        File folder = new File(getFilesDir(),"SQLiteBackup");
        String fileName = "incomeExpenseManager.csv";
        String filePathAndName = folder.toString()+fileName;

        File csvFile = new File(filePathAndName);

        if(csvFile.exists()){
            try {
                CSVReader csvReader = new CSVReader(new FileReader(csvFile.getAbsolutePath()));

                String[] nextLine;
                while((nextLine = csvReader.readNext()) != null){
                    int ID = Integer.parseInt(nextLine[0]);
                    String postDescription = nextLine[1];
                    String postCategory = nextLine[2];
                    String postType = nextLine[3];
                    String postAmount = nextLine[4];
                    String postYear = nextLine[5];
                    String postMonth = nextLine[6];
                    String postDay = nextLine[7];
                    String time = nextLine[8];
                    String timeStamp = nextLine[9];
                    String dateTime = nextLine[10];

                    MC_Posts mc_posts = new MC_Posts(ID,postDescription,postCategory,postType,postAmount,postYear,postMonth,postDay,time,timeStamp,dateTime);
                    SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
                    sqLiteHelper.saveData(mc_posts);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(this, "No Backup Found", Toast.LENGTH_SHORT).show();
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
            exportCSV();
        }
    }







    private void fetchAllTransactions(){
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
            postList.add(post);

        }
        cursor.close();
    }





}