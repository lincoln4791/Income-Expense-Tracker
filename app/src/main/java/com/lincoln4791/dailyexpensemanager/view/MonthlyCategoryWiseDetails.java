package com.lincoln4791.dailyexpensemanager.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.lincoln4791.dailyexpensemanager.Adapters.Adapter_MonthlyCategoryWiseReport;
import com.lincoln4791.dailyexpensemanager.R;
import com.lincoln4791.dailyexpensemanager.common.Constants;
import com.lincoln4791.dailyexpensemanager.common.NodeName;
import com.lincoln4791.dailyexpensemanager.common.SQLiteHelper;
import com.lincoln4791.dailyexpensemanager.common.UtilDB;
import com.lincoln4791.dailyexpensemanager.model.MC_Posts;

import java.util.ArrayList;
import java.util.List;

public class MonthlyCategoryWiseDetails extends AppCompatActivity {

    private TextView tv_type,tv_category,tv_currentBalance_toolbar;
    private ImageView iv_home;
    private RecyclerView recyclerView;
    private List<MC_Posts> postList;
    private Adapter_MonthlyCategoryWiseReport adapter_monthlyCategoryWiseReport;
    private LinearLayoutManager linearLayoutManager;


    private String year,month,type,category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_category_wise_details);




        //**************************************************** View Bindings ***********************************
        tv_type = findViewById(R.id.tv_type_MonthlyCategoryWise);
        tv_category = findViewById(R.id.tv_category_MonthlyCategoryWise);
        recyclerView = findViewById(R.id.rv_categoryWiseReport);

        tv_currentBalance_toolbar = findViewById(R.id.tv_currentBalanceValue_toolBar_MonthlyCategoryWiseReport);
        iv_home = findViewById(R.id.iv_home_toolbar_MonthlyCategoryWiseReport);




        //**************************************************** Initializations *********************************
        getSupportActionBar().hide();
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList = new ArrayList<>();
        adapter_monthlyCategoryWiseReport = new Adapter_MonthlyCategoryWiseReport(postList,this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter_monthlyCategoryWiseReport);







        //*****************************************Click Listener********************************************
        iv_home.setOnClickListener(v -> {
            startActivity(new Intent(MonthlyCategoryWiseDetails.this,MainActivity.class));
        });






        //******************************************** Starting Methods **********************************
        tv_currentBalance_toolbar.setText(String.valueOf(UtilDB.currentBalance));
        getIntentData();
        setCategoryAndType();
       new YearMonthTypeCategoryTask().execute();

    }




    private void setCategoryAndType() {
            tv_category.setText(category);
            if(type.equals(Constants.TYPE_INCOME)){
                tv_type.setText(getString(R.string.Incomes));
            }
            else if(type.equals(Constants.TYPE_EXPENSE)){
                tv_type.setText(getString(R.string.Expenses));
            }
    }





    private void getIntentData() {
       year = getIntent().getStringExtra(NodeName.POST_YEAR);
       month = getIntent().getStringExtra(NodeName.POST_MONTH);
       type = getIntent().getStringExtra(NodeName.POST_TYPE);
       category = getIntent().getStringExtra(NodeName.POST_CATEGORY);

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
            Log.d("tag","listSIze "+postList.size());
            //Collections.reverse(vm_fullReport.postsList);
            recyclerView.setAdapter(adapter_monthlyCategoryWiseReport);
            adapter_monthlyCategoryWiseReport.notifyDataSetChanged();
        }
    }







    private void fetchYearMonthTypeCategoryWise() {
        postList.clear();
        //tv_typeTitle.setText(getString(R.string.Category));
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.loadYearMonthTypeCategoryWise(year, month, type, category);

        while (cursor.moveToNext()) {
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

            MC_Posts post = new MC_Posts(postDescription, postCategory, postType, postAmount, postYear, postMonth, postDay, postTime, timeStamp, postDateTime);
            postList.add(post);
        }

        cursor.close();
    }




}