package com.lincoln4791.dailyexpensemanager.view

import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.lincoln4791.dailyexpensemanager.Adapters.Adapter_FullReport
import com.lincoln4791.dailyexpensemanager.viewModels.VM_FullReport
import android.os.Bundle
import com.lincoln4791.dailyexpensemanager.R
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import com.lincoln4791.dailyexpensemanager.view.Daily
import com.lincoln4791.dailyexpensemanager.view.MonthlyReport
import com.lincoln4791.dailyexpensemanager.view.Transactions
import com.lincoln4791.dailyexpensemanager.common.Extras
import com.lincoln4791.dailyexpensemanager.view.MainActivity
import com.lincoln4791.dailyexpensemanager.common.UtilDB
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.*
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.common.SQLiteHelper
import com.lincoln4791.dailyexpensemanager.databinding.ActivityFullReportBinding
import com.lincoln4791.dailyexpensemanager.model.MC_Posts

class FullReport : AppCompatActivity() {

    private val linearLayoutManager = LinearLayoutManager(this)
    private var adapterFullReport: Adapter_FullReport? = null

/*    private val totalIncome = 0
    private val totalExpense = 0
    private val balance = 0
    private val transportExpense = 0
    private val foodExpense = 0
    private val billsExpense = 0
    private val houseRentExpense = 0
    private val businessExpense = 0
    private val medicineExpense = 0
    private val clothsExpense = 0
    private val educationExpense = 0
    private val lifeStyleExpense = 0
    private val otherExpense = 0
    private val salaryIncome = 0
    private val businessIncome = 0
    private val houseRentIncome = 0
    private val otherIncome = 0*/
    private var vm_fullReport: VM_FullReport? = null

    private lateinit var binding : ActivityFullReportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullReportBinding.inflate(layoutInflater)
        setContentView(binding.root)



    /*    supportActionBar!!.hide()
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        vm_fullReport = ViewModelProviders.of(this).get(VM_FullReport::class.java)
        binding.rvReportDetailsFullReport.layoutManager = linearLayoutManager
        adapterFullReport = Adapter_FullReport(this@FullReport, vm_fullReport!!.postsList)

        binding.cvDailyFullReport.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(this@FullReport,
                Daily::class.java))
        })
        binding.cvMonthlyTransactionsFullReport.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(this@FullReport, MonthlyReport::class.java))
        })
        binding.cvTransactionsFullReport.setOnClickListener(View.OnClickListener { v: View? ->
            val transactionsIntent = Intent(this@FullReport, Transactions::class.java)
            transactionsIntent.putExtra(Extras.TYPE, Constants.TYPE_ALL)
            startActivity(transactionsIntent)
        })
        binding.cvTotalIncomesFullReport.setOnClickListener(View.OnClickListener { v: View? ->
            val incomeIntent = Intent(this@FullReport, Transactions::class.java)
            incomeIntent.putExtra(Extras.TYPE, Constants.TYPE_INCOME)
            startActivity(incomeIntent)
        })
        binding.cvTotalExpensesFullReport.setOnClickListener(View.OnClickListener { v: View? ->
            val expenseIntent = Intent(this@FullReport, Transactions::class.java)
            expenseIntent.putExtra(Extras.TYPE, Constants.TYPE_EXPENSE)
            startActivity(expenseIntent)
        })
        binding.cvSearchFullReport.setOnClickListener(View.OnClickListener { v: View? -> selectQuery() })

        binding.ivHomeToolbarFullReport.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(this@FullReport,
                MainActivity::class.java))
        })


        binding.tvCurrentBalanceValueToolBarFullReport.setText(UtilDB.currentBalance.toString())
        initMonthSpinner()
        initYearSpinner()
        initTypeSpinner()*/
    }

   /* private fun selectQuery() {
        if (vm_fullReport!!.month != Constants.MONTH_NULL &&
            vm_fullReport!!.day != Constants.DAY_NULL && vm_fullReport!!.type != Constants.TYPE_ALL &&
            vm_fullReport!!.category != Constants.CATEGORY_NULL
        ) {
            Log.d("Tag",
                "1)  year" + vm_fullReport!!.year + "month" + vm_fullReport!!.month + "day" + vm_fullReport!!.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            YearMonthDayTypeCategoryTask().execute()
        } else if (vm_fullReport!!.month != Constants.MONTH_NULL &&
            vm_fullReport!!.type != Constants.TYPE_ALL &&
            vm_fullReport!!.category != Constants.CATEGORY_NULL && vm_fullReport!!.day == Constants.DAY_NULL
        ) {
            Log.d("Tag",
                "2)  year" + vm_fullReport!!.year + "month" + vm_fullReport!!.month + "day" + vm_fullReport!!.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            YearMonthTypeCategoryTask().execute()
        } else if (vm_fullReport!!.month != Constants.MONTH_NULL &&
            vm_fullReport!!.day != Constants.DAY_NULL &&
            vm_fullReport!!.category != Constants.CATEGORY_NULL && vm_fullReport!!.type == Constants.TYPE_ALL
        ) {
            Log.d("Tag",
                "3)  year" + vm_fullReport!!.year + "month" + vm_fullReport!!.month + "day" + vm_fullReport!!.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            YearMonthDayCategoryTask().execute()
        } else if (vm_fullReport!!.month != Constants.MONTH_NULL &&
            vm_fullReport!!.day != Constants.DAY_NULL &&
            vm_fullReport!!.type != Constants.TYPE_ALL && vm_fullReport!!.category == Constants.CATEGORY_NULL
        ) {
            Log.d("Tag",
                "4)  year" + vm_fullReport!!.year + "month" + vm_fullReport!!.month + "day" + vm_fullReport!!.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            YearMonthDayTypeTask().execute()
        } else if (vm_fullReport!!.month != Constants.MONTH_NULL &&
            vm_fullReport!!.day != Constants.DAY_NULL && vm_fullReport!!.type == Constants.TYPE_ALL && vm_fullReport!!.category == Constants.CATEGORY_NULL
        ) {
            Log.d("Tag",
                "5)  year" + vm_fullReport!!.year + "month" + vm_fullReport!!.month + "day" + vm_fullReport!!.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            YearMonthDayTask().execute()
        } else if (vm_fullReport!!.month != Constants.MONTH_NULL &&
            vm_fullReport!!.type != Constants.TYPE_ALL && vm_fullReport!!.day == Constants.DAY_NULL && vm_fullReport!!.category == Constants.CATEGORY_NULL
        ) {
            Log.d("Tag",
                "6)  year" + vm_fullReport!!.year + "month" + vm_fullReport!!.month + "day" + vm_fullReport!!.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            YearMonthTypeTask().execute()
        } else if (vm_fullReport!!.month != Constants.MONTH_NULL &&
            vm_fullReport!!.category != Constants.CATEGORY_NULL && vm_fullReport!!.day == Constants.DAY_NULL && vm_fullReport!!.type == Constants.TYPE_ALL
        ) {
            Log.d("Tag",
                "7)  year" + vm_fullReport!!.year + "month" + vm_fullReport!!.month + "day" + vm_fullReport!!.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            YearMonthCategoryTask().execute()
        } else if (vm_fullReport!!.month != Constants.MONTH_NULL && vm_fullReport!!.day == Constants.DAY_NULL && vm_fullReport!!.type == Constants.TYPE_ALL && vm_fullReport!!.category == Constants.CATEGORY_NULL) {
            Log.d("Tag",
                "8)  year" + vm_fullReport!!.year + "month" + vm_fullReport!!.month + "day" + vm_fullReport!!.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            YearMonthTask().execute()
        } else if (vm_fullReport!!.type != Constants.TYPE_ALL && vm_fullReport!!.month == Constants.MONTH_NULL && vm_fullReport!!.day == Constants.DAY_NULL && vm_fullReport!!.category == Constants.CATEGORY_NULL) {
            Log.d("Tag",
                "9)  year" + vm_fullReport!!.year + "month" + vm_fullReport!!.month + "day" + vm_fullReport!!.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            YearTypeTask().execute()
        } else if (vm_fullReport!!.type != Constants.TYPE_ALL && vm_fullReport!!.category != Constants.CATEGORY_NULL && vm_fullReport!!.day == Constants.DAY_NULL && vm_fullReport!!.month == Constants.MONTH_NULL) {
            Log.d("Tag",
                "12)  year" + vm_fullReport!!.year + "month" + vm_fullReport!!.month + "day" + vm_fullReport!!.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            YearTypeCategoryTask().execute()
        } else if (vm_fullReport!!.category != Constants.CATEGORY_NULL && vm_fullReport!!.month == Constants.MONTH_NULL && vm_fullReport!!.day == Constants.DAY_NULL && vm_fullReport!!.type == Constants.TYPE_ALL) {
            Log.d("Tag",
                "10)  year" + vm_fullReport!!.year + "month" + vm_fullReport!!.month + "day" + vm_fullReport!!.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            YearMonthTask().execute()
        } else {
            Log.d("Tag",
                "11)  year" + vm_fullReport!!.year + "month" + vm_fullReport!!.month + "day" + vm_fullReport!!.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            YearTask().execute()
        }
    }

    private fun initTypeSpinner() {
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.type))
        binding.spinnerTypeFullReport.adapter = spinnerAdapter
        binding.spinnerTypeFullReport.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    vm_fullReport!!.type = Constants.TYPE_ALL
                    vm_fullReport!!.category = Constants.CATEGORY_NULL
                    initNullCategorySpinner()
                } else if (position == 1) {
                    vm_fullReport!!.type = Constants.TYPE_INCOME
                    initIncomeCategorySpinner()
                } else if (position == 2) {
                    vm_fullReport!!.type = Constants.TYPE_EXPENSE
                    initExpenseCategorySpinner()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                vm_fullReport!!.type = Constants.TYPE_ALL
                vm_fullReport!!.category = Constants.CATEGORY_NULL
                initNullCategorySpinner()
            }
        }
    }

    private fun initMonthSpinner() {
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.monthNames))
        binding.spinnerMonthFullReport.adapter = spinnerAdapter
        binding.spinnerMonthFullReport.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    vm_fullReport!!.month = Constants.MONTH_NULL
                    vm_fullReport!!.day = Constants.DAY_NULL
                    initDay0Spinner()
                } else if (position == 1 || position == 3 || position == 5 || position == 7 || position == 8 || position == 10 || position == 12) {
                    if (position == 1) {
                        vm_fullReport!!.month = getString(R.string.digit01)
                    } else if (position == 3) {
                        vm_fullReport!!.month = getString(R.string.digit03)
                    } else if (position == 5) {
                        vm_fullReport!!.month = getString(R.string.digit05)
                    } else if (position == 7) {
                        vm_fullReport!!.month = getString(R.string.digit07)
                    } else if (position == 8) {
                        vm_fullReport!!.month = getString(R.string.digit08)
                    } else if (position == 10) {
                        vm_fullReport!!.month = getString(R.string.digit10)
                    } else if (position == 12) {
                        vm_fullReport!!.month = getString(R.string.digit12)
                    }
                    initDay31Spinner()
                } else if (position == 4 || position == 6 || position == 9 || position == 11) {
                    if (position == 4) {
                        vm_fullReport!!.month = getString(R.string.digit04)
                    } else if (position == 6) {
                        vm_fullReport!!.month = getString(R.string.digit06)
                    } else if (position == 9) {
                        vm_fullReport!!.month = getString(R.string.digit09)
                    }
                    if (position == 11) {
                        vm_fullReport!!.month = getString(R.string.digit11)
                    }
                    initDay30Spinner()
                } else if (position == 2) {
                    vm_fullReport!!.month = getString(R.string.digit02)
                    initDay28Spinner()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                vm_fullReport!!.month = Constants.MONTH_NULL
                vm_fullReport!!.day = Constants.DAY_NULL
                initDay0Spinner()
            }
        }
    }

    private fun initNullCategorySpinner() {
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.nullCategory))
        binding.spinnerCatagoryFullReport.setAdapter(spinnerAdapter)
    }

    private fun initExpenseCategorySpinner() {
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.expenseCategory))
        binding.spinnerCatagoryFullReport.setAdapter(spinnerAdapter)
        binding.spinnerCatagoryFullReport.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    vm_fullReport!!.category = Constants.CATEGORY_NULL
                } else if (position == 1) {
                    vm_fullReport!!.category = Constants.CATEGORY_FOOD
                } else if (position == 2) {
                    vm_fullReport!!.category = Constants.CATEGORY_TRANSPORT
                } else if (position == 3) {
                    vm_fullReport!!.category = Constants.CATEGORY_HOUSE_RENT
                } else if (position == 4) {
                    vm_fullReport!!.category = Constants.CATEGORY_BUSINESS
                } else if (position == 5) {
                    vm_fullReport!!.category = Constants.CATEGORY_MEDICINE
                } else if (position == 6) {
                    vm_fullReport!!.category = Constants.CATEGORY_CLOTHS
                } else if (position == 7) {
                    vm_fullReport!!.category = Constants.CATEGORY_EDUCATION
                } else if (position == 8) {
                    vm_fullReport!!.category = Constants.CATEGORY_LIFESTYLE
                } else if (position == 9) {
                    vm_fullReport!!.category = Constants.CATEGORY_BILLS
                } else if (position == 10) {
                    vm_fullReport!!.category = Constants.CATEGORY_OTHER
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                vm_fullReport!!.category = Constants.CATEGORY_NULL
            }
        })
    }

    private fun initIncomeCategorySpinner() {
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.incomeCategory))
        binding.spinnerCatagoryFullReport.setAdapter(spinnerAdapter)
        binding.spinnerCatagoryFullReport.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    vm_fullReport!!.category = Constants.CATEGORY_NULL
                } else if (position == 1) {
                    vm_fullReport!!.category = Constants.CATEGORY_SALARY
                } else if (position == 2) {
                    vm_fullReport!!.category = Constants.CATEGORY_BUSINESS
                } else if (position == 3) {
                    vm_fullReport!!.category = Constants.CATEGORY_HOUSE_RENT
                } else if (position == 4) {
                    vm_fullReport!!.category = Constants.CATEGORY_OTHER
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
    }

    private fun initDay0Spinner() {
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.dayNull))
        binding.spinnerDayFullReport.adapter = spinnerAdapter
    }

    private fun initDay31Spinner() {
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.day31))
        binding.spinnerDayFullReport.adapter = spinnerAdapter
        binding.spinnerDayFullReport.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    vm_fullReport!!.day = Constants.DAY_NULL
                } else if (position == 1) {
                    vm_fullReport!!.day = getString(R.string.digit01)
                } else if (position == 2) {
                    vm_fullReport!!.day = getString(R.string.digit02)
                } else if (position == 3) {
                    vm_fullReport!!.day = getString(R.string.digit03)
                } else if (position == 4) {
                    vm_fullReport!!.day = getString(R.string.digit04)
                } else if (position == 5) {
                    vm_fullReport!!.day = getString(R.string.digit05)
                } else if (position == 6) {
                    vm_fullReport!!.day = getString(R.string.digit06)
                } else if (position == 7) {
                    vm_fullReport!!.day = getString(R.string.digit07)
                } else if (position == 8) {
                    vm_fullReport!!.day = getString(R.string.digit08)
                } else if (position == 9) {
                    vm_fullReport!!.day = getString(R.string.digit09)
                } else if (position == 10) {
                    vm_fullReport!!.day = getString(R.string.digit10)
                } else if (position == 11) {
                    vm_fullReport!!.day = getString(R.string.digit11)
                } else if (position == 12) {
                    vm_fullReport!!.day = getString(R.string.digit12)
                } else if (position == 13) {
                    vm_fullReport!!.day = getString(R.string.digit13)
                } else if (position == 14) {
                    vm_fullReport!!.day = getString(R.string.digit14)
                } else if (position == 15) {
                    vm_fullReport!!.day = getString(R.string.digit15)
                } else if (position == 16) {
                    vm_fullReport!!.day = getString(R.string.digit16)
                } else if (position == 17) {
                    vm_fullReport!!.day = getString(R.string.digit17)
                } else if (position == 18) {
                    vm_fullReport!!.day = getString(R.string.digit18)
                } else if (position == 19) {
                    vm_fullReport!!.day = getString(R.string.digit19)
                } else if (position == 20) {
                    vm_fullReport!!.day = getString(R.string.digit20)
                } else if (position == 21) {
                    vm_fullReport!!.day = getString(R.string.digit21)
                } else if (position == 22) {
                    vm_fullReport!!.day = getString(R.string.digit22)
                } else if (position == 23) {
                    vm_fullReport!!.day = getString(R.string.digit23)
                } else if (position == 24) {
                    vm_fullReport!!.day = getString(R.string.digit24)
                } else if (position == 25) {
                    vm_fullReport!!.day = getString(R.string.digit25)
                } else if (position == 26) {
                    vm_fullReport!!.day = getString(R.string.digit26)
                } else if (position == 27) {
                    vm_fullReport!!.day = getString(R.string.digit27)
                } else if (position == 28) {
                    vm_fullReport!!.day = getString(R.string.digit28)
                } else if (position == 29) {
                    vm_fullReport!!.day = getString(R.string.digit29)
                } else if (position == 30) {
                    vm_fullReport!!.day = getString(R.string.digit30)
                } else if (position == 31) {
                    vm_fullReport!!.day = getString(R.string.digit31)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                vm_fullReport!!.day = Constants.DAY_NULL
            }
        }
    }

    private fun initDay30Spinner() {
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.day30))
        binding.spinnerDayFullReport.adapter = spinnerAdapter
        binding.spinnerDayFullReport.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    vm_fullReport!!.day = Constants.DAY_NULL
                } else if (position == 1) {
                    vm_fullReport!!.day = getString(R.string.digit01)
                } else if (position == 2) {
                    vm_fullReport!!.day = getString(R.string.digit02)
                } else if (position == 3) {
                    vm_fullReport!!.day = getString(R.string.digit03)
                } else if (position == 4) {
                    vm_fullReport!!.day = getString(R.string.digit04)
                } else if (position == 5) {
                    vm_fullReport!!.day = getString(R.string.digit05)
                } else if (position == 6) {
                    vm_fullReport!!.day = getString(R.string.digit06)
                } else if (position == 7) {
                    vm_fullReport!!.day = getString(R.string.digit07)
                } else if (position == 8) {
                    vm_fullReport!!.day = getString(R.string.digit08)
                } else if (position == 9) {
                    vm_fullReport!!.day = getString(R.string.digit09)
                } else if (position == 10) {
                    vm_fullReport!!.day = getString(R.string.digit10)
                } else if (position == 11) {
                    vm_fullReport!!.day = getString(R.string.digit11)
                } else if (position == 12) {
                    vm_fullReport!!.day = getString(R.string.digit12)
                } else if (position == 13) {
                    vm_fullReport!!.day = getString(R.string.digit13)
                } else if (position == 14) {
                    vm_fullReport!!.day = getString(R.string.digit14)
                } else if (position == 15) {
                    vm_fullReport!!.day = getString(R.string.digit15)
                } else if (position == 16) {
                    vm_fullReport!!.day = getString(R.string.digit16)
                } else if (position == 17) {
                    vm_fullReport!!.day = getString(R.string.digit17)
                } else if (position == 18) {
                    vm_fullReport!!.day = getString(R.string.digit18)
                } else if (position == 19) {
                    vm_fullReport!!.day = getString(R.string.digit19)
                } else if (position == 20) {
                    vm_fullReport!!.day = getString(R.string.digit20)
                } else if (position == 21) {
                    vm_fullReport!!.day = getString(R.string.digit21)
                } else if (position == 22) {
                    vm_fullReport!!.day = getString(R.string.digit22)
                } else if (position == 23) {
                    vm_fullReport!!.day = getString(R.string.digit23)
                } else if (position == 24) {
                    vm_fullReport!!.day = getString(R.string.digit24)
                } else if (position == 25) {
                    vm_fullReport!!.day = getString(R.string.digit25)
                } else if (position == 26) {
                    vm_fullReport!!.day = getString(R.string.digit26)
                } else if (position == 27) {
                    vm_fullReport!!.day = getString(R.string.digit27)
                } else if (position == 28) {
                    vm_fullReport!!.day = getString(R.string.digit28)
                } else if (position == 29) {
                    vm_fullReport!!.day = getString(R.string.digit29)
                } else if (position == 30) {
                    vm_fullReport!!.day = getString(R.string.digit30)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                vm_fullReport!!.day = Constants.DAY_NULL
            }
        }
    }

    private fun initDay28Spinner() {
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.day28))
        binding.spinnerDayFullReport.adapter = spinnerAdapter
        binding.spinnerDayFullReport.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    vm_fullReport!!.day = Constants.DAY_NULL
                } else if (position == 1) {
                    vm_fullReport!!.day = getString(R.string.digit01)
                } else if (position == 2) {
                    vm_fullReport!!.day = getString(R.string.digit02)
                } else if (position == 3) {
                    vm_fullReport!!.day = getString(R.string.digit03)
                } else if (position == 4) {
                    vm_fullReport!!.day = getString(R.string.digit04)
                } else if (position == 5) {
                    vm_fullReport!!.day = getString(R.string.digit05)
                } else if (position == 6) {
                    vm_fullReport!!.day = getString(R.string.digit06)
                } else if (position == 7) {
                    vm_fullReport!!.day = getString(R.string.digit07)
                } else if (position == 8) {
                    vm_fullReport!!.day = getString(R.string.digit08)
                } else if (position == 9) {
                    vm_fullReport!!.day = getString(R.string.digit09)
                } else if (position == 10) {
                    vm_fullReport!!.day = getString(R.string.digit10)
                } else if (position == 11) {
                    vm_fullReport!!.day = getString(R.string.digit11)
                } else if (position == 12) {
                    vm_fullReport!!.day = getString(R.string.digit12)
                } else if (position == 13) {
                    vm_fullReport!!.day = getString(R.string.digit13)
                } else if (position == 14) {
                    vm_fullReport!!.day = getString(R.string.digit14)
                } else if (position == 15) {
                    vm_fullReport!!.day = getString(R.string.digit15)
                } else if (position == 16) {
                    vm_fullReport!!.day = getString(R.string.digit16)
                } else if (position == 17) {
                    vm_fullReport!!.day = getString(R.string.digit17)
                } else if (position == 18) {
                    vm_fullReport!!.day = getString(R.string.digit18)
                } else if (position == 19) {
                    vm_fullReport!!.day = getString(R.string.digit19)
                } else if (position == 20) {
                    vm_fullReport!!.day = getString(R.string.digit20)
                } else if (position == 21) {
                    vm_fullReport!!.day = getString(R.string.digit21)
                } else if (position == 22) {
                    vm_fullReport!!.day = getString(R.string.digit22)
                } else if (position == 23) {
                    vm_fullReport!!.day = getString(R.string.digit23)
                } else if (position == 24) {
                    vm_fullReport!!.day = getString(R.string.digit24)
                } else if (position == 25) {
                    vm_fullReport!!.day = getString(R.string.digit25)
                } else if (position == 26) {
                    vm_fullReport!!.day = getString(R.string.digit26)
                } else if (position == 27) {
                    vm_fullReport!!.day = getString(R.string.digit27)
                } else if (position == 28) {
                    vm_fullReport!!.day = getString(R.string.digit28)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                vm_fullReport!!.day = Constants.DAY_NULL
            }
        }
    }

    private fun initYearSpinner() {
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.year))
        binding.spinnerYearFullReport.adapter = spinnerAdapter
        binding.spinnerYearFullReport.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    vm_fullReport!!.year = Constants.YEAR_DEFAULT
                } else if (position == 1) {
                    vm_fullReport!!.year = "2022"
                } else if (position == 2) {
                    vm_fullReport!!.year = "2023"
                } else if (position == 3) {
                    vm_fullReport!!.year = "2024"
                } else if (position == 4) {
                    vm_fullReport!!.year = "2025"
                } else if (position == 5) {
                    vm_fullReport!!.year = "2026"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                vm_fullReport!!.year = Constants.YEAR_DEFAULT
            }
        }
    }

    internal inner class AllTransactionsTask : AsyncTask<Any?, Any?, Any?>() {
        override fun doInBackground(objects: Array<Any?>): Any? {
            fetchAllTransactions()
            return null
        }

        override fun onPostExecute(o: Any?) {
            super.onPostExecute(o)
            binding.rvReportDetailsFullReport!!.adapter = adapterFullReport
            adapterFullReport!!.notifyDataSetChanged()
            setCalculatedValue()
        }
    }

    internal inner class AllExpensesTask : AsyncTask<Any?, Any?, Any?>() {
        override fun doInBackground(objects: Array<Any?>): Any? {
            fetchAllExpenses()
            return null
        }

        override fun onPostExecute(o: Any?) {
            super.onPostExecute(o)
            binding.rvReportDetailsFullReport.adapter = adapterFullReport
            adapterFullReport!!.notifyDataSetChanged()
            setCalculatedValue()
        }
    }

    internal inner class AllIncomeTask : AsyncTask<Any?, Any?, Any?>() {
        override fun doInBackground(objects: Array<Any?>): Any? {
            fetchAllIncomes()
            return null
        }

        override fun onPostExecute(o: Any?) {
            super.onPostExecute(o)
            binding.rvReportDetailsFullReport!!.adapter = adapterFullReport
            adapterFullReport!!.notifyDataSetChanged()
            setCalculatedValue()
        }
    }

    internal inner class YearTask : AsyncTask<Any?, Any?, Any?>() {
        override fun doInBackground(objects: Array<Any?>): Any? {
            fetchYearWise()
            return null
        }

        override fun onPostExecute(o: Any?) {
            super.onPostExecute(o)
            binding.rvReportDetailsFullReport!!.adapter = adapterFullReport
            adapterFullReport!!.notifyDataSetChanged()
            setCalculatedValue()
        }
    }

    internal inner class YearTypeTask : AsyncTask<Any?, Any?, Any?>() {
        override fun doInBackground(objects: Array<Any?>): Any? {
            fetchYearTypeWise()
            return null
        }

        override fun onPostExecute(o: Any?) {
            super.onPostExecute(o)
            binding.rvReportDetailsFullReport!!.adapter = adapterFullReport
            adapterFullReport!!.notifyDataSetChanged()
            setCalculatedValue()
        }
    }

    internal inner class YearCategoryTask : AsyncTask<Any?, Any?, Any?>() {
        override fun doInBackground(objects: Array<Any?>): Any? {
            fetchYearCategoryWise()
            return null
        }

        override fun onPostExecute(o: Any?) {
            super.onPostExecute(o)
            //Collections.reverse(vm_fullReport.postsList);
            binding.rvReportDetailsFullReport!!.adapter = adapterFullReport
            adapterFullReport!!.notifyDataSetChanged()
            setCalculatedValue()
        }
    }

    internal inner class YearTypeCategoryTask : AsyncTask<Any?, Any?, Any?>() {
        override fun doInBackground(objects: Array<Any?>): Any? {
            fetchYearTypeCategoryWise()
            return null
        }

        override fun onPostExecute(o: Any?) {
            super.onPostExecute(o)
            //Collections.reverse(vm_fullReport.postsList);
            binding.rvReportDetailsFullReport!!.adapter = adapterFullReport
            adapterFullReport!!.notifyDataSetChanged()
            setCalculatedValue()
        }
    }

    internal inner class YearMonthTask : AsyncTask<Any?, Any?, Any?>() {
        override fun doInBackground(objects: Array<Any?>): Any? {
            fetchYearMonthWise()
            return null
        }

        override fun onPostExecute(o: Any?) {
            super.onPostExecute(o)
            //Collections.reverse(vm_fullReport.postsList);
            binding.rvReportDetailsFullReport!!.adapter = adapterFullReport
            adapterFullReport!!.notifyDataSetChanged()
            setCalculatedValue()
        }
    }

    internal inner class YearMonthTypeTask : AsyncTask<Any?, Any?, Any?>() {
        override fun doInBackground(objects: Array<Any?>): Any? {
            fetchYearMonthTypeWise()
            return objects
        }

        override fun onPostExecute(o: Any?) {
            super.onPostExecute(o)
            //Collections.reverse(vm_fullReport.postsList);
            binding.rvReportDetailsFullReport!!.adapter = adapterFullReport
            adapterFullReport!!.notifyDataSetChanged()
            setCalculatedValue()
        }
    }

    internal inner class YearMonthCategoryTask : AsyncTask<Any?, Any?, Any?>() {
        override fun doInBackground(objects: Array<Any?>): Any? {
            fetchYearMonthCategoryWise()
            return null
        }

        override fun onPostExecute(o: Any?) {
            super.onPostExecute(o)
            //Collections.reverse(vm_fullReport.postsList);
            binding.rvReportDetailsFullReport!!.adapter = adapterFullReport
            adapterFullReport!!.notifyDataSetChanged()
            setCalculatedValue()
        }
    }

    internal inner class YearMonthTypeCategoryTask : AsyncTask<Any?, Any?, Any?>() {
        override fun doInBackground(objects: Array<Any?>): Any? {
            fetchYearMonthTypeCategoryWise()
            return null
        }

        override fun onPostExecute(o: Any?) {
            super.onPostExecute(o)
            //Collections.reverse(vm_fullReport.postsList);
            binding.rvReportDetailsFullReport!!.adapter = adapterFullReport
            adapterFullReport!!.notifyDataSetChanged()
            setCalculatedValue()
        }
    }

    internal inner class YearMonthDayTask : AsyncTask<Any?, Any?, Any?>() {
        override fun doInBackground(objects: Array<Any?>): Any? {
            fetchYearMonthDayWise()
            return null
        }

        override fun onPostExecute(o: Any?) {
            super.onPostExecute(o)
            //Collections.reverse(vm_fullReport.postsList);
            binding.rvReportDetailsFullReport!!.adapter = adapterFullReport
            adapterFullReport!!.notifyDataSetChanged()
            setCalculatedValue()
        }
    }

    internal inner class YearMonthDayTypeTask : AsyncTask<Any?, Any?, Any?>() {
        override fun doInBackground(objects: Array<Any?>): Any? {
            fetchYearMonthDayTypeWise()
            return null
        }

        override fun onPostExecute(o: Any?) {
            super.onPostExecute(o)
            //Collections.reverse(vm_fullReport.postsList);
            binding.rvReportDetailsFullReport!!.adapter = adapterFullReport
            adapterFullReport!!.notifyDataSetChanged()
            setCalculatedValue()
        }
    }

    internal inner class YearMonthDayCategoryTask : AsyncTask<Any?, Any?, Any?>() {
        override fun doInBackground(objects: Array<Any?>): Any? {
            fetchYearMonthDayCategoryWise()
            return null
        }

        override fun onPostExecute(o: Any?) {
            super.onPostExecute(o)
            //Collections.reverse(vm_fullReport.postsList);
            binding.rvReportDetailsFullReport!!.adapter = adapterFullReport
            adapterFullReport!!.notifyDataSetChanged()
            setCalculatedValue()
        }
    }

    internal inner class YearMonthDayTypeCategoryTask : AsyncTask<Any?, Any?, Any?>() {
        override fun doInBackground(objects: Array<Any?>): Any? {
            fetchYearMonthDayTypeCategoryWise()
            return null
        }

        override fun onPostExecute(o: Any?) {
            super.onPostExecute(o)
            //Collections.reverse(vm_fullReport.postsList);
            binding.rvReportDetailsFullReport!!.adapter = adapterFullReport
            adapterFullReport!!.notifyDataSetChanged()
            setCalculatedValue()
        }
    }

    private fun fetchAllTransactions() {
        vm_fullReport!!.postsList.clear()
        //tv_typeTitle.setText(getString(R.string.Transactions));
        val sqLiteHelper = SQLiteHelper(this)
        val cursor = sqLiteHelper.loadAllTransactions()
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
            vm_fullReport!!.postsList.add(post)
        }
        cursor.close()
        calculateAll()
    }

    private fun fetchAllExpenses() {
        vm_fullReport!!.postsList.clear()
        //tv_typeTitle.setText(getString(R.string.Expenses));
        val sqLiteHelper = SQLiteHelper(this)
        val cursor = sqLiteHelper.loadTypeWise(Constants.TYPE_EXPENSE)
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
            vm_fullReport!!.postsList.add(post)
        }
        cursor.close()
        calculateAll()
    }

    private fun fetchAllIncomes() {
        vm_fullReport!!.postsList.clear()
        //tv_typeTitle.setText(getString(R.string.Incomes));
        val sqLiteHelper = SQLiteHelper(this)
        val cursor = sqLiteHelper.loadTypeWise(Constants.TYPE_INCOME)
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
            vm_fullReport!!.postsList.add(post)
        }
        cursor.close()
        calculateAll()
    }

    private fun fetchYearWise() {
        vm_fullReport!!.postsList.clear()
        //tv_typeTitle.setText(getString(R.string.YearWise));
        val sqLiteHelper = SQLiteHelper(this)
        val cursor = sqLiteHelper.loadYearWise(vm_fullReport!!.year)
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
            vm_fullReport!!.postsList.add(post)
        }
        cursor.close()
        calculateAll()
    }

    private fun fetchYearTypeWise() {
        vm_fullReport!!.postsList.clear()
        //tv_typeTitle.setText(getString(R.string.Category));
        val sqLiteHelper = SQLiteHelper(this)
        val cursor = sqLiteHelper.loadYearTypeWise(vm_fullReport!!.year, vm_fullReport!!.type)
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
            vm_fullReport!!.postsList.add(post)
        }
        cursor.close()
        calculateAll()
    }

    private fun fetchYearCategoryWise() {
        vm_fullReport!!.postsList.clear()
        //tv_typeTitle.setText(getString(R.string.Category));
        val sqLiteHelper = SQLiteHelper(this)
        val cursor =
            sqLiteHelper.loadYearCategoryWise(vm_fullReport!!.year, vm_fullReport!!.category)
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
            vm_fullReport!!.postsList.add(post)
        }
        cursor.close()
        calculateAll()
    }

    private fun fetchYearTypeCategoryWise() {
        vm_fullReport!!.postsList.clear()
        //tv_typeTitle.setText(getString(R.string.Category));
        val sqLiteHelper = SQLiteHelper(this)
        val cursor = sqLiteHelper.loadYearTypeCategoryWise(vm_fullReport!!.year,
            vm_fullReport!!.type,
            vm_fullReport!!.category)
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
            vm_fullReport!!.postsList.add(post)
        }
        cursor.close()
        calculateAll()
    }

    private fun fetchYearMonthWise() {
        vm_fullReport!!.postsList.clear()
        //tv_typeTitle.setText(getString(R.string.Category));
        val sqLiteHelper = SQLiteHelper(this)
        val cursor = sqLiteHelper.loadYearMonthWise(vm_fullReport!!.year, vm_fullReport!!.month)
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
            vm_fullReport!!.postsList.add(post)
        }
        cursor.close()
        calculateAll()
    }

    private fun fetchYearMonthTypeWise() {
        vm_fullReport!!.postsList.clear()
        //tv_typeTitle.setText(getString(R.string.Category));
        val sqLiteHelper = SQLiteHelper(this)
        val cursor = sqLiteHelper.loadYearMonthTypeWise(vm_fullReport!!.year,
            vm_fullReport!!.month,
            vm_fullReport!!.type)
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
            vm_fullReport!!.postsList.add(post)
        }
        cursor.close()
        calculateAll()
    }

    private fun fetchYearMonthCategoryWise() {
        vm_fullReport!!.postsList.clear()
        //tv_typeTitle.setText(getString(R.string.Category));
        val sqLiteHelper = SQLiteHelper(this)
        val cursor = sqLiteHelper.loadYearMonthCategoryWise(
            vm_fullReport!!.year, vm_fullReport!!.month, vm_fullReport!!.category)
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
            vm_fullReport!!.postsList.add(post)
        }
        cursor.close()
        calculateAll()
    }

    private fun fetchYearMonthTypeCategoryWise() {
        vm_fullReport!!.postsList.clear()
        //tv_typeTitle.setText(getString(R.string.Category));
        val sqLiteHelper = SQLiteHelper(this)
        val cursor = sqLiteHelper.loadYearMonthTypeCategoryWise(
            vm_fullReport!!.year,
            vm_fullReport!!.month,
            vm_fullReport!!.type,
            vm_fullReport!!.category)
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
            vm_fullReport!!.postsList.add(post)
        }
        cursor.close()
        calculateAll()
    }

    private fun fetchYearMonthDayWise() {
        vm_fullReport!!.postsList.clear()
        //tv_typeTitle.setText(getString(R.string.Category));
        val sqLiteHelper = SQLiteHelper(this)
        val cursor = sqLiteHelper.loadYearMonthDayWise(vm_fullReport!!.year,
            vm_fullReport!!.month,
            vm_fullReport!!.day)
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
            vm_fullReport!!.postsList.add(post)
        }
        cursor.close()
        calculateAll()
    }

    private fun fetchYearMonthDayTypeWise() {
        vm_fullReport!!.postsList.clear()
        //tv_typeTitle.setText(getString(R.string.Category));
        val sqLiteHelper = SQLiteHelper(this)
        val cursor = sqLiteHelper.loadYearMonthDayTypeWise(vm_fullReport!!.year,
            vm_fullReport!!.month,
            vm_fullReport!!.day,
            vm_fullReport!!.type)
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
            vm_fullReport!!.postsList.add(post)
        }
        cursor.close()
        calculateAll()
    }

    private fun fetchYearMonthDayCategoryWise() {
        vm_fullReport!!.postsList.clear()
        //tv_typeTitle.setText(getString(R.string.Category));
        val sqLiteHelper = SQLiteHelper(this)
        val cursor = sqLiteHelper.loadYearMonthDayTypeWise(vm_fullReport!!.year,
            vm_fullReport!!.month,
            vm_fullReport!!.day,
            vm_fullReport!!.category)
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
            vm_fullReport!!.postsList.add(post)
        }
        cursor.close()
        calculateAll()
    }

    private fun fetchYearMonthDayTypeCategoryWise() {
        vm_fullReport!!.postsList.clear()
        //tv_typeTitle.setText(getString(R.string.YearWise));
        val sqLiteHelper = SQLiteHelper(this)
        val cursor = sqLiteHelper.loadYearMonthDayTypeCategoryWise(
            vm_fullReport!!.year,
            vm_fullReport!!.month,
            vm_fullReport!!.day,
            vm_fullReport!!.type,
            vm_fullReport!!.category)
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
            vm_fullReport!!.postsList.add(post)
        }
        cursor.close()
        calculateAll()
    }

    private fun calculateAll() {
        resetPreviousCalculatedValues()
        for (i in vm_fullReport!!.postsList.indices) {
            if (vm_fullReport!!.postsList[i].postType == Constants.TYPE_INCOME) {
                if (vm_fullReport!!.postsList[i].postCategory == Constants.CATEGORY_SALARY) {
                    vm_fullReport!!.income_salary =
                        vm_fullReport!!.income_salary + vm_fullReport!!.postsList[i].postAmount
                            .toInt()
                } else if (vm_fullReport!!.postsList[i].postCategory == Constants.CATEGORY_BUSINESS) {
                    vm_fullReport!!.income_business =
                        vm_fullReport!!.income_business + vm_fullReport!!.postsList[i].postAmount
                            .toInt()
                } else if (vm_fullReport!!.postsList[i].postCategory == Constants.CATEGORY_HOUSE_RENT) {
                    vm_fullReport!!.income_house_rent =
                        vm_fullReport!!.income_house_rent + vm_fullReport!!.postsList[i].postAmount
                            .toInt()
                } else if (vm_fullReport!!.postsList[i].postCategory == Constants.CATEGORY_OTHER) {
                    vm_fullReport!!.income_other =
                        vm_fullReport!!.income_other + vm_fullReport!!.postsList[i].postAmount
                            .toInt()
                }
                vm_fullReport!!.total_income =
                    vm_fullReport!!.total_income + vm_fullReport!!.postsList[i].postAmount
                        .toInt()
            } else {
                if (vm_fullReport!!.postsList[i].postCategory == Constants.CATEGORY_FOOD) {
                    vm_fullReport!!.expense_food =
                        vm_fullReport!!.expense_food + vm_fullReport!!.postsList[i].postAmount
                            .toInt()
                } else if (vm_fullReport!!.postsList[i].postCategory == Constants.CATEGORY_TRANSPORT) {
                    vm_fullReport!!.expense_transport =
                        vm_fullReport!!.expense_transport + vm_fullReport!!.postsList[i].postAmount
                            .toInt()
                } else if (vm_fullReport!!.postsList[i].postCategory == Constants.CATEGORY_BILLS) {
                    vm_fullReport!!.expense_bills =
                        vm_fullReport!!.expense_bills + vm_fullReport!!.postsList[i].postAmount
                            .toInt()
                } else if (vm_fullReport!!.postsList[i].postCategory == Constants.CATEGORY_HOUSE_RENT) {
                    vm_fullReport!!.expense_houseRent =
                        vm_fullReport!!.expense_houseRent + vm_fullReport!!.postsList[i].postAmount
                            .toInt()
                } else if (vm_fullReport!!.postsList[i].postCategory == Constants.CATEGORY_BUSINESS) {
                    vm_fullReport!!.expense_business =
                        vm_fullReport!!.expense_business + vm_fullReport!!.postsList[i].postAmount
                            .toInt()
                } else if (vm_fullReport!!.postsList[i].postCategory == Constants.CATEGORY_MEDICINE) {
                    vm_fullReport!!.expense_medicine =
                        vm_fullReport!!.expense_medicine + vm_fullReport!!.postsList[i].postAmount
                            .toInt()
                } else if (vm_fullReport!!.postsList[i].postCategory == Constants.CATEGORY_CLOTHS) {
                    vm_fullReport!!.expense_cloths =
                        vm_fullReport!!.expense_cloths + vm_fullReport!!.postsList[i].postAmount
                            .toInt()
                } else if (vm_fullReport!!.postsList[i].postCategory == Constants.CATEGORY_EDUCATION) {
                    vm_fullReport!!.expense_education =
                        vm_fullReport!!.expense_education + vm_fullReport!!.postsList[i].postAmount
                            .toInt()
                } else if (vm_fullReport!!.postsList[i].postCategory == Constants.CATEGORY_LIFESTYLE) {
                    vm_fullReport!!.expense_lifestyle =
                        vm_fullReport!!.expense_lifestyle + vm_fullReport!!.postsList[i].postAmount
                            .toInt()
                } else if (vm_fullReport!!.postsList[i].postCategory == Constants.CATEGORY_OTHER) {
                    vm_fullReport!!.expense_other =
                        vm_fullReport!!.expense_other + vm_fullReport!!.postsList[i].postAmount
                            .toInt()
                }
                vm_fullReport!!.total_expense =
                    vm_fullReport!!.total_expense + vm_fullReport!!.postsList[i].postAmount
                        .toInt()
            }
        }
    }

    fun setCalculatedValue() {
        binding.tvSalaryIncomeValueTopBarFullReport.text = vm_fullReport!!.income_salary.toString()
        binding.tvBusinessIncomeValueTopBarFullReport.text = vm_fullReport!!.income_business.toString()
        binding.tvHouseRentIncomeValueTopBarFullReport.text = vm_fullReport!!.income_house_rent.toString()
        binding.tvOtherIncomeTopBarFullReport.text = vm_fullReport!!.income_other.toString()
        binding.tvTotalIncomeValueTopBarFullReport.text = vm_fullReport!!.total_income.toString()
        binding.tvFoodExpenseValueTopBarFullReport.text = vm_fullReport!!.expense_food.toString()
        binding.tvTransportExpenseValueTopBarFullReport.text = vm_fullReport!!.expense_transport.toString()
        binding.tvBillsExpenseValueTopBarFullReport.text = vm_fullReport!!.expense_bills.toString()
        binding.tvHouseRentExpenseValueTopBarFullReport.text = vm_fullReport!!.expense_houseRent.toString()
        binding.tvBusinessExpenseValueTopBarFullReport.text = vm_fullReport!!.expense_business.toString()
        binding.tvMedicineExpensesValueTopBarFullReport.text = vm_fullReport!!.expense_medicine.toString()
        binding.tvClothsExpensesValueTopBarFullReport.text = vm_fullReport!!.expense_cloths.toString()
        binding.tvEducationExpensesValueTopBarFullReport.text = vm_fullReport!!.expense_education.toString()
        binding.tvLifeStyleExpensesValueTopBarFullReport.text = vm_fullReport!!.expense_lifestyle.toString()
        binding.tvOtherExpensesValueTopBarFullReport.text = vm_fullReport!!.expense_other.toString()
        binding.tvTotalExpenseValueTopBarFullReport.text = vm_fullReport!!.total_expense.toString()
    }

    private fun resetPreviousCalculatedValues() {
        vm_fullReport!!.income_salary = 0
        vm_fullReport!!.income_business = 0
        vm_fullReport!!.income_house_rent = 0
        vm_fullReport!!.income_other = 0
        vm_fullReport!!.total_income = 0
        vm_fullReport!!.expense_food = 0
        vm_fullReport!!.expense_transport = 0
        vm_fullReport!!.expense_bills = 0
        vm_fullReport!!.expense_houseRent = 0
        vm_fullReport!!.expense_business = 0
        vm_fullReport!!.expense_medicine = 0
        vm_fullReport!!.expense_cloths = 0
        vm_fullReport!!.expense_education = 0
        vm_fullReport!!.expense_lifestyle = 0
        vm_fullReport!!.expense_other = 0
        vm_fullReport!!.total_expense = 0
    }*/

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}