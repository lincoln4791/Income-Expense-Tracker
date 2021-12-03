package com.lincoln4791.dailyexpensemanager.view

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import com.lincoln4791.dailyexpensemanager.Adapters.Adapter_MonthlyCategoryWiseReport
import androidx.recyclerview.widget.LinearLayoutManager
import android.os.Bundle
import com.lincoln4791.dailyexpensemanager.R
import android.content.Intent
import com.lincoln4791.dailyexpensemanager.view.MainActivity
import com.lincoln4791.dailyexpensemanager.common.UtilDB
import com.lincoln4791.dailyexpensemanager.common.NodeName
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.common.SQLiteHelper
import com.lincoln4791.dailyexpensemanager.databinding.ActivityMonthlyCategoryWiseDetailsBinding
import java.util.ArrayList

class MonthlyCategoryWiseDetails : AppCompatActivity() {
    private lateinit var postList: MutableList<MC_Posts>
    private lateinit var adapter_monthlyCategoryWiseReport: Adapter_MonthlyCategoryWiseReport
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var year: String
    private lateinit var month: String
    private lateinit var type: String
    private lateinit var category: String

    private lateinit var binding : ActivityMonthlyCategoryWiseDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonthlyCategoryWiseDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //**************************************************** Initializations *********************************
        supportActionBar!!.hide()
        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager!!.reverseLayout = true
        linearLayoutManager!!.stackFromEnd = true
        postList = ArrayList()
        adapter_monthlyCategoryWiseReport = Adapter_MonthlyCategoryWiseReport(postList as ArrayList<MC_Posts>, this)
        binding.rvCategoryWiseReport.setLayoutManager(linearLayoutManager)
        binding.rvCategoryWiseReport.setAdapter(adapter_monthlyCategoryWiseReport)


        //*****************************************Click Listener********************************************
        binding.ivHomeToolbarMonthlyCategoryWiseReport.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(this@MonthlyCategoryWiseDetails,
                MainActivity::class.java))
        })


        //******************************************** Starting Methods **********************************
        binding.tvCurrentBalanceValueToolBarMonthlyCategoryWiseReport.text = UtilDB.currentBalance.toString()
        intentData
        setCategoryAndType()
        YearMonthTypeCategoryTask().execute()
    }

    private fun setCategoryAndType() {
        binding.tvCategoryMonthlyCategoryWise.text = category
        if (type == Constants.TYPE_INCOME) {
            binding.tvTypeMonthlyCategoryWise.text = getString(R.string.Incomes)
        } else if (type == Constants.TYPE_EXPENSE) {
            binding.tvTypeMonthlyCategoryWise.text = getString(R.string.Expenses)
        }
    }

    private val intentData: Unit
        get() {
            year = intent.getStringExtra(NodeName.POST_YEAR)!!
            month = intent.getStringExtra(NodeName.POST_MONTH)!!
            type = intent.getStringExtra(NodeName.POST_TYPE)!!
            category = intent.getStringExtra(NodeName.POST_CATEGORY)!!
        }

    internal inner class YearMonthTypeCategoryTask : AsyncTask<Any?, Any?, Any?>() {
        override fun doInBackground(objects: Array<Any?>): Any? {
            fetchYearMonthTypeCategoryWise()
            return null
        }

        override fun onPostExecute(o: Any?) {
            super.onPostExecute(o)
            Log.d("tag", "listSIze " + postList!!.size)
            //Collections.reverse(vm_fullReport.postsList);
            binding.rvCategoryWiseReport.adapter = adapter_monthlyCategoryWiseReport
            adapter_monthlyCategoryWiseReport!!.notifyDataSetChanged()
        }
    }

    private fun fetchYearMonthTypeCategoryWise() {
        postList!!.clear()
        //tv_typeTitle.setText(getString(R.string.Category));
        val sqLiteHelper = SQLiteHelper(this)
        val cursor = sqLiteHelper.loadYearMonthTypeCategoryWise(year, month, type, category)
        while (cursor.moveToNext()) {
            val postDescription = cursor.getString(1)
            val postCategory = cursor.getString(2)
            val postType = cursor.getString(3)
            val postAmount = cursor.getString(4)
            val postTime = cursor.getString(5)
            val postDay = cursor.getString(6)
            val postMonth = cursor.getString(7)
            val postYear = cursor.getString(8)
            val postDateTime = cursor.getString(9)
            val timeStamp = cursor.getString(10)
            val post = MC_Posts(postDescription,
                postCategory,
                postType,
                postAmount,
                postYear,
                postMonth,
                postDay,
                postTime,
                timeStamp,
                postDateTime)
            postList!!.add(post)
        }
        cursor.close()
    }
}