/*
package com.lincoln4791.dailyexpensemanager.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.lincoln4791.dailyexpensemanager.Adapters.Adapter_Daily
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.common.util.DbAdapter
import com.lincoln4791.dailyexpensemanager.common.util.GlobalVariabls
import com.lincoln4791.dailyexpensemanager.common.util.Util
import com.lincoln4791.dailyexpensemanager.databinding.ActivityDailyBinding
import com.lincoln4791.dailyexpensemanager.databinding.FragmentDailyBinding
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import com.lincoln4791.dailyexpensemanager.roomDB.AppDatabase
import com.lincoln4791.dailyexpensemanager.viewModels.VM_Daily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class DailyActivity : AppCompatActivity() {
    private var adapterDaily: Adapter_Daily? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var day: String? = null
    private var month: String? = null
    private var year: String? = null
    private var date: String? = null
    private var totalIncome = 0
    private var totalExpense = 0
    private var tempMonth = 0
    private var tempYear = 0
    private var tempDay = 0
    private lateinit var viewModel: VM_Daily
    private lateinit var binding : ActivityDailyBinding
    private lateinit var navCon : NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Util.recordScreenEvent("daily_fragment","MainActivity")
        viewModel = ViewModelProvider(this)[VM_Daily::class.java]
        changeNavBarColors()

        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager!!.reverseLayout = true
        linearLayoutManager!!.stackFromEnd = true
        //adapterDaily = Adapter_Daily(viewModel.postsList!!.value, requireContext())
        binding.rvDailyReport.layoutManager = linearLayoutManager
        binding.rvDailyReport.adapter = adapterDaily
        val calendar = Calendar.getInstance()
        tempMonth = calendar[Calendar.MONTH]
        tempYear = calendar[Calendar.YEAR]
        tempDay = calendar[Calendar.DAY_OF_MONTH]

        viewModel.postsList.observe(this@DailyActivity, androidx.lifecycle.Observer {
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                is Resource.Success ->  calculateAll(it.data)
                is Resource.Error -> Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
        })

        binding.cvDateDailyReport.setOnClickListener { changeDateAndFetchNewData() }
        binding.ibNextDateDailyReport.setOnClickListener { loadNextDateData() }
        binding.ibPreviousDateDailyReport.setOnClickListener { loadPreviousDateData() }
        binding.cvFullReportDailyReport.setOnClickListener {
            */
/* startActivity(Intent(this@Daily,
                 FullReport::class.java))*//*

        }
        binding.cvMonthlyTransactionsDailyReport.setOnClickListener(View.OnClickListener { v: View? ->
            */
/*  startActivity(Intent(this@Daily,
                  MonthlyReport::class.java))*//*

        })
        binding.cvTransactionsDailyReport.setOnClickListener(View.OnClickListener { v: View? ->
            */
/*val intent = Intent(this@Daily, Transactions::class.java)
            intent.putExtra(Extras.TYPE, Constants.TYPE_ALL)
            startActivity(intent)*//*

        })
        binding.cvImg.setOnClickListener(View.OnClickListener { v: View? ->
            onBackPressed()
        })


        setDate()
        binding.tvCurrentBalanceValueToolBarMonthlyReport.text = GlobalVariabls.currentBalance.toString()
        viewModel.loadDailyTransactions(year!!,month!!,day!!)

    }

    private fun loadPreviousDateData() {
        var monthValue = month!!.toInt()
        var dayValue = day!!.toInt()
        if (monthValue == 2 || monthValue == 4 || monthValue == 6 || monthValue == 8 || monthValue == 9 || monthValue == 11) {
            if (dayValue == 1) {
                monthValue = monthValue - 1
                dayValue = 31
                setMonthPlain(monthValue)
                setDay(dayValue)
            } else {
                day = (day!!.toInt() - 1).toString()
            }
            date = "$day-$month-$year"
            binding.tvDateDailyReport.text = date
            //YearMonthDayTask().execute()
            viewModel.loadDailyTransactions(year!!, month!!, day!!)
        } else if (monthValue == 5 || monthValue == 7 || monthValue == 10 || monthValue == 12) {
            if (dayValue == 1) {
                monthValue = monthValue - 1
                dayValue = 30
                setMonthPlain(monthValue)
                setDay(dayValue)
            } else {
                day = (day!!.toInt() - 1).toString()
            }
            date = "$day-$month-$year"
            binding.tvDateDailyReport.text = date
            //YearMonthDayTask().execute()
            viewModel.loadDailyTransactions(year!!, month!!, day!!)
        } else if (monthValue == 3) {
            if (dayValue == 1) {
                monthValue = monthValue - 1
                dayValue = 28
                setMonthPlain(monthValue)
                setDay(dayValue)
            } else {
                day = (day!!.toInt() - 1).toString()
            }
            date = "$day-$month-$year"
            binding.tvDateDailyReport.text = date
            //YearMonthDayTask().execute()
            viewModel.loadDailyTransactions(year!!, month!!, day!!)
        } else if (monthValue == 1) {
            if (dayValue == 1) {
                year = (year!!.toInt() - 1).toString()
                monthValue = 12
                dayValue = 31
                setMonthPlain(monthValue)
                setDay(dayValue)
            } else {
                day = (day!!.toInt() - 1).toString()
            }
            date = "$day-$month-$year"
            binding.tvDateDailyReport.text = date
            //YearMonthDayTask().execute()
            viewModel.loadDailyTransactions(year!!, month!!, day!!)
        }
    }

    private fun loadNextDateData() {
        var monthValue = month!!.toInt()
        var dayValue = day!!.toInt()
        if (monthValue == 1 || monthValue == 3 || monthValue == 5 || monthValue == 7 || monthValue == 8 || monthValue == 10) {
            if (dayValue == 31) {
                monthValue = monthValue + 1
                dayValue = 1
                setMonthPlain(monthValue)
                setDay(dayValue)
            } else {
                day = (day!!.toInt() + 1).toString()
            }
            date = "$day-$month-$year"
            binding.tvDateDailyReport.text = date
            //YearMonthDayTask().execute()
            viewModel.loadDailyTransactions(year!!, month!!, day!!)
        } else if (monthValue == 4 || monthValue == 6 || monthValue == 9 || monthValue == 11) {
            if (dayValue == 30) {
                monthValue = monthValue + 1
                dayValue = 1
                setMonthPlain(monthValue)
                setDay(dayValue)
            } else {
                day = (day!!.toInt() + 1).toString()
            }
            date = "$day-$month-$year"
            binding.tvDateDailyReport.text = date
            //YearMonthDayTask().execute()
            viewModel.loadDailyTransactions(year!!, month!!, day!!)
        } else if (monthValue == 2) {
            if (dayValue == 28) {
                monthValue = monthValue + 1
                dayValue = 1
                setMonthPlain(monthValue)
                setDay(dayValue)
            } else {
                day = (day!!.toInt() + 1).toString()
            }
            date = "$day-$month-$year"
            binding.tvDateDailyReport.text = date
            //YearMonthDayTask().execute()
            viewModel.loadDailyTransactions(year!!, month!!, day!!)
        } else if (monthValue == 12) {
            if (dayValue == 31) {
                year = (year!!.toInt() + 1).toString()
                monthValue = 1
                dayValue = 1
                setMonthPlain(monthValue)
                setDay(dayValue)
            } else {
                day = (day!!.toInt() + 1).toString()
            }
            date = "$day-$month-$year"
            binding.tvDateDailyReport.text = date
            //YearMonthDayTask().execute()
            viewModel.loadDailyTransactions(year!!, month!!, day!!)
        }
    }

    private fun setMonthPlain(monthh: Int) {
        month = if (monthh == 1) {
            getString(R.string.digit01)
        } else if (monthh == 2) {
            getString(R.string.digit02)
        } else if (monthh == 3) {
            getString(R.string.digit03)
        } else if (monthh == 4) {
            getString(R.string.digit04)
        } else if (monthh == 5) {
            getString(R.string.digit05)
        } else if (monthh == 6) {
            getString(R.string.digit06)
        } else if (monthh == 7) {
            getString(R.string.digit07)
        } else if (monthh == 8) {
            getString(R.string.digit08)
        } else if (monthh == 9) {
            getString(R.string.digit09)
        } else {
            monthh.toString()
        }
    }

    private fun changeDateAndFetchNewData() {
        tempDay = day!!.toInt()
        tempMonth = month!!.toInt() - 1
        tempYear = year!!.toInt()
        val datePickerDialog = DatePickerDialog(this,
            { view: DatePicker?, yearr: Int, monthh: Int, dayOfMonth: Int ->
                year = yearr.toString()
                setMonth(monthh)
                setDay(dayOfMonth)
                date = "$day-$month-$year"
                binding.tvDateDailyReport.text = date
                //YearMonthDayTask().execute()
            },
            tempYear,
            tempMonth,
            tempDay)
        datePickerDialog.show()
    }

    private fun changeNavBarColors() {
        supportActionBar!!.hide()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.primary)
        }
        window.navigationBarColor = resources.getColor(R.color.primary)
    }

    private fun setDay(dayOfMonth: Int) {
        day = if (dayOfMonth == 1) {
            getString(R.string.digit01)
        } else if (dayOfMonth == 2) {
            getString(R.string.digit02)
        } else if (dayOfMonth == 3) {
            getString(R.string.digit03)
        } else if (dayOfMonth == 4) {
            getString(R.string.digit04)
        } else if (dayOfMonth == 5) {
            getString(R.string.digit05)
        } else if (dayOfMonth == 6) {
            getString(R.string.digit06)
        } else if (dayOfMonth == 7) {
            getString(R.string.digit07)
        } else if (dayOfMonth == 8) {
            getString(R.string.digit08)
        } else if (dayOfMonth == 9) {
            getString(R.string.digit09)
        } else {
            dayOfMonth.toString()
        }
    }

    private fun setMonth(monthh: Int) {
        month = if (monthh == 0) {
            getString(R.string.digit01)
        } else if (monthh == 1) {
            getString(R.string.digit02)
        } else if (monthh == 2) {
            getString(R.string.digit03)
        } else if (monthh == 3) {
            getString(R.string.digit04)
        } else if (monthh == 4) {
            getString(R.string.digit05)
        } else if (monthh == 5) {
            getString(R.string.digit06)
        } else if (monthh == 6) {
            getString(R.string.digit07)
        } else if (monthh == 7) {
            getString(R.string.digit08)
        } else if (monthh == 8) {
            getString(R.string.digit09)
        } else {
            (monthh + 1).toString()
        }
    }

    private fun setDate() {
        val simpleDayFormat = SimpleDateFormat("dd", Locale.getDefault())
        val simpleMonthFormat = SimpleDateFormat("MM", Locale.getDefault())
        val simpleYearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        day = simpleDayFormat.format(System.currentTimeMillis())
        month = simpleMonthFormat.format(System.currentTimeMillis())
        year = simpleYearFormat.format(System.currentTimeMillis())
        date = "$day-$month-$year"
        binding.tvDateDailyReport.text = date
    }

    private fun updateUI(postList: List<MC_Posts>){
        if(postList.isEmpty()){
            binding.cvNoResultFound.visibility = View.VISIBLE
        }
        else{
            binding.cvNoResultFound.visibility = View.GONE
        }
        binding.tvTotalIncomeValueDaily.text = totalIncome.toString()
        binding.tvTotalExpenseValueDaily.text = totalExpense.toString()
        binding.rvDailyReport.adapter = adapterDaily
        adapterDaily!!.notifyDataSetChanged()
    }

    private fun calculateAll(postList:List<MC_Posts>) {
        adapterDaily = Adapter_Daily(postList, this)
        totalIncome = 0
        totalExpense = 0
        Log.d("tag", "list size : " + postList.size)
        for (i in postList.indices) {
            if (postList[i].postType == Constants.TYPE_INCOME) {
                totalIncome = totalIncome + postList[i].postAmount.toInt()
            } else if (postList[i].postType == Constants.TYPE_EXPENSE) {
                totalExpense = totalExpense + postList[i].postAmount.toInt()
            }
        }
        updateUI(postList)
    }


    fun navigateToDetails(type:String,category:String){

 val action = TransactionsFragmentDirections.actionTransactionsFragmentToMonthlyCategoryWiseFragment(year,month,type,category,
             Constants.FRAGMENT_TRANSACTION,transactionType)
         navCon.navigate(action)*//*


        val intent = Intent(this@DailyActivity, MonthlyCategoryWiseActivity::class.java).let {
            it.putExtra("year",year)
            it.putExtra("month",month)
            it.putExtra("type",type)
            it.putExtra("category",category)
            it.putExtra(Constants.ACTIVITY_FROM, Constants.ACTIVITY_DAILY)
            it
        }
        startActivity(intent)

    }

    fun deleteData(id: Int,callback : (isDeleted : Boolean,error:String?)->Unit) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                AppDatabase.getInstance(this@DailyActivity).dbDao().delete(id.toString())
                Handler(Looper.getMainLooper()).post {
                    callback(true, null)
                }

            }

        }
        catch (e: Exception){
            callback(false,e.message)
        }

    }

    fun confirmDelete(id: Int, amount: Int, typeOfTheFile: String){
        DbAdapter.confirmDelete(this@DailyActivity,id,amount,typeOfTheFile){
            if(it !=null){
                if(it){
                    viewModel.loadDailyTransactions(year!!,month!!,day!!)
                }
                else{
                    Toast.makeText(this@DailyActivity,"Something Went Wrong",Toast.LENGTH_SHORT).show()
                }
            }

        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@DailyActivity,MainActivity::class.java))
        finish()
    }

}*/
