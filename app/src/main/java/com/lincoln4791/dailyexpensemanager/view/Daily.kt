package com.lincoln4791.dailyexpensemanager.view

import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.lincoln4791.dailyexpensemanager.Adapters.Adapter_Daily
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import android.os.Bundle
import com.lincoln4791.dailyexpensemanager.R
import android.content.Intent
import com.lincoln4791.dailyexpensemanager.view.FullReport
import com.lincoln4791.dailyexpensemanager.view.MonthlyReport
import com.lincoln4791.dailyexpensemanager.view.Transactions
import com.lincoln4791.dailyexpensemanager.common.Extras
import com.lincoln4791.dailyexpensemanager.view.MainActivity
import com.lincoln4791.dailyexpensemanager.common.UtilDB
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.AsyncTask
import android.util.Log
import com.lincoln4791.dailyexpensemanager.common.SQLiteHelper
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.databinding.ActivityDailyBinding
import java.text.SimpleDateFormat
import java.util.*

class Daily : AppCompatActivity() {
    private var adapterDaily: Adapter_Daily? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var day: String? = null
    private var month: String? = null
    private var year: String? = null
    private var date: String? = null
    private var postList: MutableList<MC_Posts>? = null
    private var totalIncome = 0
    private var totalExpense = 0
    private var tempMonth = 0
    private var tempYear = 0
    private var tempDay = 0

    private lateinit var binding : ActivityDailyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailyBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //*************************************************Initializations*********************************************
        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager!!.reverseLayout = true
        linearLayoutManager!!.stackFromEnd = true
        postList = ArrayList()
        adapterDaily = Adapter_Daily(postList!!, this)
        binding.rvDailyReport.setLayoutManager(linearLayoutManager)
        binding.rvDailyReport.setAdapter(adapterDaily)
        val calendar = Calendar.getInstance()
        tempMonth = calendar[Calendar.MONTH]
        tempYear = calendar[Calendar.YEAR]
        tempDay = calendar[Calendar.DAY_OF_MONTH]



        //***************************************************Initializations****************************************
        supportActionBar!!.hide()


        //************************************************ Click Listeners *******************************************
        binding.cvDateDailyReport.setOnClickListener(View.OnClickListener { v: View? -> changeDateAndFetchNewData() })
        binding.ibNextDateDailyReport.setOnClickListener(View.OnClickListener { v: View? -> loadNextDateData() })
        binding.ibPreviousDateDailyReport.setOnClickListener(View.OnClickListener { v: View? -> loadPreviousDateData() })
        binding.cvFullReportDailyReport.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(this@Daily,
                FullReport::class.java))
        })
        binding.cvMonthlyTransactionsDailyReport.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(this@Daily,
                MonthlyReport::class.java))
        })
        binding.cvTransactionsDailyReport.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(this@Daily, Transactions::class.java)
            intent.putExtra(Extras.TYPE, Constants.TYPE_ALL)
            startActivity(intent)
        })
        binding.ivHomeToolbarDailyActivity.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(this@Daily,
                MainActivity::class.java))
        })


        //************************************************* Starting Methods *****************************************
        setDate()
        binding.tvCurrentBalanceTextToolBarDailyActivity.text = UtilDB.currentBalance.toString()
        YearMonthDayTask().execute()
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
            binding.tvCurrentBalanceValueToolBarDailyActivity.text = date
            YearMonthDayTask().execute()
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
            YearMonthDayTask().execute()
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
            YearMonthDayTask().execute()
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
            binding.tvDateDailyReport!!.text = date
            YearMonthDayTask().execute()
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
            YearMonthDayTask().execute()
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
            YearMonthDayTask().execute()
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
            YearMonthDayTask().execute()
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
            YearMonthDayTask().execute()
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
        val datePickerDialog = DatePickerDialog(this@Daily,
            { view: DatePicker?, yearr: Int, monthh: Int, dayOfMonth: Int ->
                year = yearr.toString()
                setMonth(monthh)
                setDay(dayOfMonth)
                date = "$day-$month-$year"
                binding.tvDateDailyReport.text = date
                YearMonthDayTask().execute()
            },
            tempYear,
            tempMonth,
            tempDay)
        datePickerDialog.show()
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

    internal inner class YearMonthDayTask : AsyncTask<Any?, Any?, Any?>() {
        override fun doInBackground(objects: Array<Any?>): Any? {
            fetchYearMonthDayWise()
            return null
        }

        override fun onPostExecute(o: Any?) {
            super.onPostExecute(o)
            //Collections.reverse(vm_fullReport.postsList);
            binding.tvTotalIncomeValueDaily.text = totalIncome.toString()
            binding.tvTotalExpenseValueDaily.text = totalExpense.toString()
            binding.rvDailyReport!!.adapter = adapterDaily
            adapterDaily!!.notifyDataSetChanged()
        }
    }

    private fun fetchYearMonthDayWise() {
        postList!!.clear()
        //tv_typeTitle.setText(getString(R.string.Category));
        val sqLiteHelper = SQLiteHelper(this)
        val cursor = sqLiteHelper.loadYearMonthDayWise(year!!, month!!, day!!)
        while (cursor.moveToNext()) {
            val ID = cursor.getInt(0)
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
            val post = MC_Posts(ID,
                postDescription,
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
        calculateAll()
    }

    private fun calculateAll() {
        totalIncome = 0
        totalExpense = 0
        Log.d("tag", "list size : " + postList!!.size)
        for (i in postList!!.indices) {
            if (postList!![i].postType == Constants.TYPE_INCOME) {
                totalIncome = totalIncome + postList!![i].postAmount.toInt()
            } else if (postList!![i].postType == Constants.TYPE_EXPENSE) {
                totalExpense = totalExpense + postList!![i].postAmount.toInt()
            }
        }
    }

    fun deleteData(id: Int) {
        val sqLiteHelper = SQLiteHelper(this@Daily)
        Toast.makeText(this, "ID$id", Toast.LENGTH_SHORT).show()
        sqLiteHelper.deleteData(id)
    }

    fun confirmDelete(id: Int) {
        val dialog = Dialog(this@Daily)
        val view = LayoutInflater.from(this@Daily).inflate(R.layout.dialog_delete, null)
        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.show()
        view.findViewById<View>(R.id.btn_yes_alertImage_dialog_delete).setOnClickListener {
            dialog.dismiss()
            deleteData(id)
        }
        view.findViewById<View>(R.id.btn_no_alertImage_dialog_delete)
            .setOnClickListener { dialog.dismiss() }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}