/*
package com.lincoln4791.dailyexpensemanager.view

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lincoln4791.dailyexpensemanager.Adapters.Adapter_MonthlyCategoryWiseReport
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.common.util.DbAdapter
import com.lincoln4791.dailyexpensemanager.common.util.GlobalVariabls
import com.lincoln4791.dailyexpensemanager.common.util.Util
import com.lincoln4791.dailyexpensemanager.databinding.ActivityMonthlyCategoryWiseBinding
import com.lincoln4791.dailyexpensemanager.databinding.FragmentMonthlyCategoryWiseBinding
import com.lincoln4791.dailyexpensemanager.fragments.MonthlyCategoryWiseFragmentDirections
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import com.lincoln4791.dailyexpensemanager.viewModels.VM_MonthlyCategoryWise

class MonthlyCategoryWiseActivity : AppCompatActivity() {
    private lateinit var adapter_monthlyCategoryWiseReport: Adapter_MonthlyCategoryWiseReport
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var year: String
    private lateinit var month: String
    private lateinit var type: String
    private lateinit var category: String
    private lateinit var activityFrom: String
    private lateinit var selectedTransactionType: String
    private lateinit var viewModel: VM_MonthlyCategoryWise
    private lateinit var binding : ActivityMonthlyCategoryWiseBinding
    private lateinit var navCon : NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonthlyCategoryWiseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeNavBarColors()
        Util.recordScreenEvent("monthly_category_wise_fragment","MainActivity")

        viewModel = ViewModelProvider(this)[VM_MonthlyCategoryWise::class.java]

        year = intent.getStringExtra("year")!!
        month = intent.getStringExtra("month")!!
        type = intent.getStringExtra("type")!!
        category = intent.getStringExtra("category")!!
        activityFrom = intent.getStringExtra(Constants.ACTIVITY_FROM)?:""
        selectedTransactionType = intent.getStringExtra(Constants.SELECTED_TRANSACTION_TYPE)?:Constants.TYPE_ALL

        Log.d("tag","year -> $year")
        Log.d("tag","year -> $month")
        Log.d("tag","year -> $type")
        Log.d("tag","year -> $category")

        binding.tvTitle.text= "$category $type of ${Util.getMonthNameFromMonthNumber(month)}-$year"

        binding.cvImg.setOnClickListener {
            goBack()
        }


        binding.tvCurrentBalanceValueToolBarMonthlyCategoryWiseReport.text = GlobalVariabls.currentBalance.toString()
        setCategoryAndType()

        viewModel.postsList.observe(this, Observer {
            when(it){
                //is Resource.Success -> adapter_transactions = Adapter_Transactions(it.data, this)
                is Resource.Success ->  updateUI(it.data)
                is Resource.Error -> Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
        })

        viewModel.loadYearMonthTypeCategoryWise(year,month,type,category)

    }

    private fun changeNavBarColors() {
        supportActionBar!!.hide()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.primary)
        }
        window.navigationBarColor = resources.getColor(R.color.primary)
    }

    private fun setCategoryAndType() {
        binding.tvCategoryMonthlyCategoryWise.text = category
        if (type == Constants.TYPE_INCOME) {
            binding.tvTypeMonthlyCategoryWise.text = getString(R.string.Incomes)
        } else if (type == Constants.TYPE_EXPENSE) {
            binding.tvTypeMonthlyCategoryWise.text = getString(R.string.Expenses)
        }
    }




    private fun updateUI(postList:List<MC_Posts>){
        Log.d("tag", "listSIze " + postList!!.size)
        //Collections.reverse(vm_fullReport.postsList);
        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        binding.rvCategoryWiseReport.layoutManager = linearLayoutManager
        adapter_monthlyCategoryWiseReport = Adapter_MonthlyCategoryWiseReport(postList,this@MonthlyCategoryWiseActivity)
        binding.rvCategoryWiseReport.adapter = adapter_monthlyCategoryWiseReport
        adapter_monthlyCategoryWiseReport!!.notifyDataSetChanged()
    }


    fun confirmDelete(id: Int, amount: Int, typeOfTheFile: String){
        DbAdapter.confirmDelete(this,id,amount,typeOfTheFile){
            if(it !=null){
                if(it){
                    viewModel.loadYearMonthTypeCategoryWise(year,month,type,category)
                }
                else{
                    Toast.makeText(this,"Something Went Wrong",Toast.LENGTH_SHORT).show()
                }
            }

        }
    }


    private fun goBack() {

        if(activityFrom==Constants.ACTIVITY_TRANSACTION){
            val transactionsAction = MonthlyCategoryWiseFragmentDirections.actionMonthlyCategoryWiseFragmentToTransactionsFragment(selectedTransactionType)
            navCon.navigate(transactionsAction)
            */
/*val intent = Intent(this,TransactionsActivity::class.java).let {
                it.putExtra("type",selectedTransactionType)
            }*//*

            startActivity(intent)
            finish()
        }

        else if(activityFrom == Constants.ACTIVITY_DAILY){
            startActivity(Intent(this@MonthlyCategoryWiseActivity,DailyActivity::class.java))
            finish()
        }

        else{
            val intent = Intent(this@MonthlyCategoryWiseActivity,MonthlyActivity::class.java).let {
                it.putExtra("year",year)
                it.putExtra("month",month)
                it
            }
            startActivity(intent)
            finish()
        }

    }

}*/
