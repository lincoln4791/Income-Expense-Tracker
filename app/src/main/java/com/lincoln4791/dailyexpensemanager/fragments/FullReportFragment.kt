package com.lincoln4791.dailyexpensemanager.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybaseproject2.base.BaseFragment
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.itmedicus.patientaid.ads.admobAdsUpdated.AdMobUtil
import com.lincoln4791.dailyexpensemanager.Adapters.*
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.admobAdsUpdated.AdUnitIds
import com.lincoln4791.dailyexpensemanager.admobAdsUpdated.InterstistialAdHelper
import com.lincoln4791.dailyexpensemanager.common.*
import com.lincoln4791.dailyexpensemanager.common.util.CurrentDate
import com.lincoln4791.dailyexpensemanager.common.util.Util
import com.lincoln4791.dailyexpensemanager.common.util.GlobalVariabls
import com.lincoln4791.dailyexpensemanager.databinding.FragmentFullReportBinding
import com.lincoln4791.dailyexpensemanager.model.MC_MonthlyReport
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import com.lincoln4791.dailyexpensemanager.viewModels.VMFullReport
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FullReportFragment : BaseFragment<FragmentFullReportBinding>(FragmentFullReportBinding::inflate) {
    @Inject lateinit var repository: Repository
    @Inject lateinit var prefManager : PrefManager
    @Inject lateinit var linearLayoutManager : LinearLayoutManager
    private val vmFullReport by viewModels<VMFullReport>()

    private var adapterFullReport: Adapter_FullReport? = null
    private var adapterExpenses: Adapter_FullReportExpense? = null
    private var adapterIncomes: Adapter_FullReportIncome? = null
    private var totalIncome = 0
    private var totalExpense = 0
    private var isAdLoaded = false

    private lateinit var navCon : NavController
    private lateinit var dateTimeDialogView : View
    private lateinit var dateTimeDialog : BottomSheetDialog

    private lateinit var interAd: InterstistialAdHelper
    private var mInterstitialAd: InterstitialAd? = null


    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Util.recordScreenEvent("fullReport_fragment","MainActivity")
        initInterstitialAd()
        navCon = Navigation.findNavController(view)
        initialization()

        binding.cvSearch.setOnClickListener {
            selectSearchCriteria()
        }

        binding.cvImg.setOnClickListener {
            navCon.navigateUp()
        }

        binding.tvCurrentBalanceValueToolBarFullReport.text = GlobalVariabls.currentBalance.toString()


        dateTimeDialogView.findViewById<Button>(R.id.btnSubmit).setOnClickListener {
            dateTimeDialog.dismiss()
            showInterAd()
        }

        observers()
        //vm_fullReport.getAllCardsByTypeArrayString()
        initMonthSpinner()
        initYearSpinner()
        initTypeSpinner(dateTimeDialogView)
        CoroutineScope(Dispatchers.Main).launch {
            selectSearchCriteria()
        }
    }

    private fun initialization() {
        dateTimeDialogView = layoutInflater.inflate(R.layout.dialog_select_date_time,null,false)
        dateTimeDialog = BottomSheetDialog(requireContext())
        binding.rvReportDetailsFullReport.layoutManager = linearLayoutManager
        binding.rvReportDetailsFullReport.adapter=adapterFullReport
    }

    @Suppress("UNCHECKED_CAST")
    private fun observers() {
        vmFullReport.postsList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                is Resource.Success<*> -> updateAllTransactionsUI(it.value as List<MC_Posts>)
                else -> {}
            }
        }

        vmFullReport.expenseList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                is Resource.Success<*> -> updateExpensesUI(it.value as List<MC_MonthlyReport>)
                else -> {}
            }
        }

        vmFullReport.incomeList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                is Resource.Success<*> -> updateIncomesUI(it.value as List<MC_MonthlyReport>)
                else -> {}
            }
        }

        vmFullReport.categoryCards.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                is Resource.Success<*> -> initExpenseCategorySpinner(it.value as Array<String>)
                //is Resource.Failure -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                else -> {}
            }
        }

        vmFullReport.totalIncome.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                is Resource.Success<*> -> updateTotalIncome(it.value as Int)
                //is Resource.Failure -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                else -> {}
            }
        }

        vmFullReport.totalExpense.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                is Resource.Success<*> -> updateTotalExpense(it.value as Int)
                //is Resource.Failure -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                else -> {}
            }
        }

    }

    private fun selectSearchCriteria() {
        dateTimeDialog.setContentView(dateTimeDialogView)
        dateTimeDialog.show()

    }

    private fun selectQuery() {

        if (vmFullReport.month != Constants.MONTH_All &&
            vmFullReport.day != Constants.DAY_ALL && vmFullReport.type != Constants.TYPE_ALL &&
            vmFullReport.category != Constants.CATEGORY_All
        ) {
            Log.d("Tag",
                "1)  year" + vmFullReport.year + "month" + vmFullReport.month + "day" + vmFullReport.day + " type " + vmFullReport.type + " category " + vmFullReport.category)
            vmFullReport.loadYearMonthDayTypeCategoryWise(vmFullReport.year,vmFullReport.month
                ,vmFullReport.day,vmFullReport.type,vmFullReport.category)
            vmFullReport.loadYearMonthIncomeWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_INCOME)
            vmFullReport.loadYearMonthExpenseWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_EXPENSE)
        } else if (vmFullReport.month != Constants.MONTH_All &&
            vmFullReport.type != Constants.TYPE_ALL &&
            vmFullReport.category != Constants.CATEGORY_All && vmFullReport.day == Constants.DAY_ALL
        ) {
            Log.d("Tag",
                "2)  year" + vmFullReport.year + "month" + vmFullReport.month + "day" + vmFullReport.day + " type " + vmFullReport.type + " category " + vmFullReport.category)
            vmFullReport.loadYearMonthTypeCategoryWise(
                vmFullReport.year,vmFullReport.month,vmFullReport.type,vmFullReport.category
            )
            vmFullReport.loadYearMonthIncomeWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_INCOME)
            vmFullReport.loadYearMonthExpenseWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_EXPENSE)
        } else if (vmFullReport.month != Constants.MONTH_All &&
            vmFullReport.day != Constants.DAY_ALL &&
            vmFullReport.category != Constants.CATEGORY_All && vmFullReport.type == Constants.TYPE_ALL
        ) {
            Log.d("Tag",
                "3)  year" + vmFullReport.year + "month" + vmFullReport.month + "day" + vmFullReport.day + " type " + vmFullReport.type + " category " + vmFullReport.category)
            vmFullReport.loadYearMonthDayCategoryWise(
                vmFullReport.year,vmFullReport.month,vmFullReport.day,vmFullReport.category
            )
            vmFullReport.loadYearMonthIncomeWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_INCOME)
            vmFullReport.loadYearMonthExpenseWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_EXPENSE)
        } else if (vmFullReport.month != Constants.MONTH_All &&
            vmFullReport.day != Constants.DAY_ALL &&
            vmFullReport.type != Constants.TYPE_ALL && vmFullReport.category == Constants.CATEGORY_All
        ) {
            Log.d("Tag",
                "4)  year" + vmFullReport.year + "month" + vmFullReport.month + "day" + vmFullReport.day + " type " + vmFullReport.type + " category " + vmFullReport.category)
            vmFullReport.loadYearMonthDayTypeWise(
                vmFullReport.year,vmFullReport.month,vmFullReport.day,vmFullReport.type
            )
            vmFullReport.loadYearMonthIncomeWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_INCOME)
            vmFullReport.loadYearMonthExpenseWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_EXPENSE)
        } else if (vmFullReport.month != Constants.MONTH_All &&
            vmFullReport.day != Constants.DAY_ALL && vmFullReport.type == Constants.TYPE_ALL && vmFullReport.category == Constants.CATEGORY_All
        ) {
            Log.d("Tag",
                "5)  year" + vmFullReport.year + "month" + vmFullReport.month + "day" + vmFullReport.day + " type " + vmFullReport.type + " category " + vmFullReport.category)
            vmFullReport.loadYearMonthDayWise(
                vmFullReport.year,vmFullReport.month,vmFullReport.day
            )
            vmFullReport.loadYearMonthIncomeWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_INCOME)
            vmFullReport.loadYearMonthExpenseWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_EXPENSE)
            vmFullReport.loadYearMonthBalance(vmFullReport.year,vmFullReport.month)
        } else if (vmFullReport.month != Constants.MONTH_All &&
            vmFullReport.type != Constants.TYPE_ALL && vmFullReport.day == Constants.DAY_ALL && vmFullReport.category == Constants.CATEGORY_All
        ) {
            Log.d("Tag",
                "6)  year" + vmFullReport.year + "month" + vmFullReport.month + "day" + vmFullReport.day + " type " + vmFullReport.type + " category " + vmFullReport.category)
            vmFullReport.loadYearMonthTypeWise(
                vmFullReport.year,vmFullReport.month,vmFullReport.type
            )
            vmFullReport.loadYearMonthIncomeWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_INCOME)
            vmFullReport.loadYearMonthExpenseWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_EXPENSE)
        } else if (vmFullReport.month != Constants.MONTH_All &&
            vmFullReport.category != Constants.CATEGORY_All && vmFullReport.day == Constants.DAY_ALL && vmFullReport.type == Constants.TYPE_ALL
        ) {
            Log.d("Tag",
                "7)  year" + vmFullReport.year + "month" + vmFullReport.month + "day" + vmFullReport.day + " type " + vmFullReport.type + " category " + vmFullReport.category)
            vmFullReport.loadYearMonthCategoryWise(
                vmFullReport.year,vmFullReport.month,vmFullReport.category
            )
            vmFullReport.loadYearMonthIncomeWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_INCOME)
            vmFullReport.loadYearMonthExpenseWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_EXPENSE)
        } else if (vmFullReport.month != Constants.MONTH_All && vmFullReport.day == Constants.DAY_ALL && vmFullReport.type == Constants.TYPE_ALL && vmFullReport.category == Constants.CATEGORY_All) {
            Log.d("Tag",
                "8)  year" + vmFullReport.year + "month" + vmFullReport.month + "day" + vmFullReport.day + " type " + vmFullReport.type + " category " + vmFullReport.category)
            vmFullReport.loadYearMonthWise(
                vmFullReport.year,vmFullReport.month
            )
            vmFullReport.loadYearMonthIncomeWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_INCOME)
            vmFullReport.loadYearMonthExpenseWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_EXPENSE)
            vmFullReport.loadYearMonthIncomeTotal(vmFullReport.year,vmFullReport.month,Constants.TYPE_INCOME)
            vmFullReport.loadYearMonthExpenseTotal(vmFullReport.year,vmFullReport.month,Constants.TYPE_EXPENSE)
        } else if (vmFullReport.type != Constants.TYPE_ALL && vmFullReport.month == Constants.MONTH_All && vmFullReport.day == Constants.DAY_ALL && vmFullReport.category == Constants.CATEGORY_All) {
            Log.d("Tag",
                "9)  year" + vmFullReport.year + "month" + vmFullReport.month + "day" + vmFullReport.day + " type " + vmFullReport.type + " category " + vmFullReport.category)
            vmFullReport.loadYearTypeWise(vmFullReport.year,vmFullReport.type)
            vmFullReport.loadYearIncomeWiseByGroup(vmFullReport.year,Constants.TYPE_INCOME)
            vmFullReport.loadYearExpenseWiseByGroup(vmFullReport.year,Constants.TYPE_EXPENSE)
        } else if (vmFullReport.type != Constants.TYPE_ALL && vmFullReport.category != Constants.CATEGORY_All && vmFullReport.day == Constants.DAY_ALL && vmFullReport.month == Constants.MONTH_All) {
            Log.d("Tag",
                "12)  year" + vmFullReport.year + "month" + vmFullReport.month + "day" + vmFullReport.day + " type " + vmFullReport.type + " category " + vmFullReport.category)
            vmFullReport.loadYearTypeCategoryWise(
                vmFullReport.year,vmFullReport.type,vmFullReport.category
            )
            vmFullReport.loadYearMonthIncomeWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_INCOME)
            vmFullReport.loadYearMonthExpenseWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_EXPENSE)
        } else if (vmFullReport.category != Constants.CATEGORY_All && vmFullReport.month == Constants.MONTH_All && vmFullReport.day == Constants.DAY_ALL && vmFullReport.type == Constants.TYPE_ALL) {
            Log.d("Tag",
                "10)  year" + vmFullReport.year + "month" + vmFullReport.month + "day" + vmFullReport.day + " type " + vmFullReport.type + " category " + vmFullReport.category)
            vmFullReport.loadYearMonthWise(
                vmFullReport.year,vmFullReport.month
            )
            vmFullReport.loadYearMonthIncomeWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_INCOME)
            vmFullReport.loadYearMonthExpenseWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_EXPENSE)
        } else {
            Log.d("Tag",
                "11)  year" + vmFullReport.year + "month" + vmFullReport.month + "day" + vmFullReport.day + " type " + vmFullReport.type + " category " + vmFullReport.category)
            vmFullReport.loadYearWise(vmFullReport.year)
            vmFullReport.loadYearIncomeWiseByGroup(vmFullReport.year,Constants.TYPE_INCOME)
            vmFullReport.loadYearExpenseWiseByGroup(vmFullReport.year,Constants.TYPE_EXPENSE)
        }
    }

    @SuppressLint("CutPasteId")
    private fun initTypeSpinner(view:View) {
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.type))
        view.findViewById<Spinner>(R.id.spinnerType).adapter = spinnerAdapter
        view.findViewById<Spinner>(R.id.spinnerType).onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        vmFullReport.type = Constants.TYPE_ALL
                        vmFullReport.category = Constants.CATEGORY_All
                        initNullCategorySpinner()
                    }
                    1 -> {
                        vmFullReport.type = Constants.TYPE_INCOME
                        vmFullReport.getAllCardsByTypeArrayString(Constants.TYPE_INCOME)
                    }
                    2 -> {
                        vmFullReport.type = Constants.TYPE_EXPENSE
                        vmFullReport.getAllCardsByTypeArrayString(Constants.TYPE_EXPENSE)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                vmFullReport.type = Constants.TYPE_ALL
                vmFullReport.category = Constants.CATEGORY_All
                initNullCategorySpinner()
            }
        }
    }

    @SuppressLint("CutPasteId")
    private fun initMonthSpinner() {
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.monthNames))
        dateTimeDialogView.findViewById<Spinner>(R.id.spinnerMonth).adapter = spinnerAdapter
        dateTimeDialogView.findViewById<Spinner>(R.id.spinnerMonth).onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        vmFullReport.month = Constants.MONTH_All
                        vmFullReport.day = Constants.DAY_ALL
                        initDay0Spinner()
                    }
                    1, 3, 5, 7, 8, 10, 12 -> {
                        when (position) {
                            1 -> {
                                vmFullReport.month = getString(R.string.digit01)
                            }
                            3 -> {
                                vmFullReport.month = getString(R.string.digit03)
                            }
                            5 -> {
                                vmFullReport.month = getString(R.string.digit05)
                            }
                            7 -> {
                                vmFullReport.month = getString(R.string.digit07)
                            }
                            8 -> {
                                vmFullReport.month = getString(R.string.digit08)
                            }
                            10 -> {
                                vmFullReport.month = getString(R.string.digit10)
                            }
                            12 -> {
                                vmFullReport.month = getString(R.string.digit12)
                            }
                        }
                        initDay31Spinner()
                    }
                    4, 6, 9, 11 -> {
                        when (position) {
                            4 -> {
                                vmFullReport.month = getString(R.string.digit04)
                            }
                            6 -> {
                                vmFullReport.month = getString(R.string.digit06)
                            }
                            9 -> {
                                vmFullReport.month = getString(R.string.digit09)
                            }
                        }
                        if (position == 11) {
                            vmFullReport.month = getString(R.string.digit11)
                        }
                        initDay30Spinner()
                    }
                    2 -> {
                        vmFullReport.month = getString(R.string.digit02)
                        initDay28Spinner()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                vmFullReport.month = Constants.MONTH_All
                vmFullReport.day = Constants.DAY_ALL
                initDay0Spinner()
            }
        }
    }

    private fun initNullCategorySpinner() {
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.nullCategory))
        dateTimeDialogView.findViewById<Spinner>(R.id.spinnerCategory).adapter = spinnerAdapter
    }

    private fun initExpenseCategorySpinner(expenseCards:Array<String>) {
        val list: MutableList<String> = mutableListOf()
            list.add("All")

        for(item in expenseCards){
            list.add(item)
        }

        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            list.toTypedArray())
        val expenseSpinner = dateTimeDialogView.findViewById<Spinner>(R.id.spinnerCategory)
        expenseSpinner.adapter = spinnerAdapter
        expenseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {

                vmFullReport.category = if(position==0){
                    Log.d("FullReport","position -> $position:: item -> ${expenseCards[position]} :: size -> ${expenseCards.size} ::list is -> $expenseCards")
                    Constants.CATEGORY_All

                }
                else{
                    Log.d("FullReport","position -> $position:: item -> ${expenseCards[position-1]} :: size -> ${expenseCards.size} ::list is -> $expenseCards")
                    expenseCards[(position-1)]
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                vmFullReport.category = Constants.CATEGORY_All
            }
        }
    }

    private fun initDay0Spinner() {
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.dayNull))
        dateTimeDialogView.findViewById<Spinner>(R.id.spinnerDay).adapter = spinnerAdapter
    }

    @SuppressLint("CutPasteId")
    private fun initDay31Spinner() {
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.day31))
        dateTimeDialogView.findViewById<Spinner>(R.id.spinnerDay).adapter = spinnerAdapter
        dateTimeDialogView.findViewById<Spinner>(R.id.spinnerDay).onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        vmFullReport.day = Constants.DAY_ALL
                    }
                    1 -> {
                        vmFullReport.day = getString(R.string.digit01)
                    }
                    2 -> {
                        vmFullReport.day = getString(R.string.digit02)
                    }
                    3 -> {
                        vmFullReport.day = getString(R.string.digit03)
                    }
                    4 -> {
                        vmFullReport.day = getString(R.string.digit04)
                    }
                    5 -> {
                        vmFullReport.day = getString(R.string.digit05)
                    }
                    6 -> {
                        vmFullReport.day = getString(R.string.digit06)
                    }
                    7 -> {
                        vmFullReport.day = getString(R.string.digit07)
                    }
                    8 -> {
                        vmFullReport.day = getString(R.string.digit08)
                    }
                    9 -> {
                        vmFullReport.day = getString(R.string.digit09)
                    }
                    10 -> {
                        vmFullReport.day = getString(R.string.digit10)
                    }
                    11 -> {
                        vmFullReport.day = getString(R.string.digit11)
                    }
                    12 -> {
                        vmFullReport.day = getString(R.string.digit12)
                    }
                    13 -> {
                        vmFullReport.day = getString(R.string.digit13)
                    }
                    14 -> {
                        vmFullReport.day = getString(R.string.digit14)
                    }
                    15 -> {
                        vmFullReport.day = getString(R.string.digit15)
                    }
                    16 -> {
                        vmFullReport.day = getString(R.string.digit16)
                    }
                    17 -> {
                        vmFullReport.day = getString(R.string.digit17)
                    }
                    18 -> {
                        vmFullReport.day = getString(R.string.digit18)
                    }
                    19 -> {
                        vmFullReport.day = getString(R.string.digit19)
                    }
                    20 -> {
                        vmFullReport.day = getString(R.string.digit20)
                    }
                    21 -> {
                        vmFullReport.day = getString(R.string.digit21)
                    }
                    22 -> {
                        vmFullReport.day = getString(R.string.digit22)
                    }
                    23 -> {
                        vmFullReport.day = getString(R.string.digit23)
                    }
                    24 -> {
                        vmFullReport.day = getString(R.string.digit24)
                    }
                    25 -> {
                        vmFullReport.day = getString(R.string.digit25)
                    }
                    26 -> {
                        vmFullReport.day = getString(R.string.digit26)
                    }
                    27 -> {
                        vmFullReport.day = getString(R.string.digit27)
                    }
                    28 -> {
                        vmFullReport.day = getString(R.string.digit28)
                    }
                    29 -> {
                        vmFullReport.day = getString(R.string.digit29)
                    }
                    30 -> {
                        vmFullReport.day = getString(R.string.digit30)
                    }
                    31 -> {
                        vmFullReport.day = getString(R.string.digit31)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                vmFullReport.day = Constants.DAY_ALL
            }
        }
    }

    @SuppressLint("CutPasteId")
    private fun initDay30Spinner() {
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.day30))
        dateTimeDialogView.findViewById<Spinner>(R.id.spinnerDay).adapter = spinnerAdapter
        dateTimeDialogView.findViewById<Spinner>(R.id.spinnerDay).onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        vmFullReport.day = Constants.DAY_ALL
                    }
                    1 -> {
                        vmFullReport.day = getString(R.string.digit01)
                    }
                    2 -> {
                        vmFullReport.day = getString(R.string.digit02)
                    }
                    3 -> {
                        vmFullReport.day = getString(R.string.digit03)
                    }
                    4 -> {
                        vmFullReport.day = getString(R.string.digit04)
                    }
                    5 -> {
                        vmFullReport.day = getString(R.string.digit05)
                    }
                    6 -> {
                        vmFullReport.day = getString(R.string.digit06)
                    }
                    7 -> {
                        vmFullReport.day = getString(R.string.digit07)
                    }
                    8 -> {
                        vmFullReport.day = getString(R.string.digit08)
                    }
                    9 -> {
                        vmFullReport.day = getString(R.string.digit09)
                    }
                    10 -> {
                        vmFullReport.day = getString(R.string.digit10)
                    }
                    11 -> {
                        vmFullReport.day = getString(R.string.digit11)
                    }
                    12 -> {
                        vmFullReport.day = getString(R.string.digit12)
                    }
                    13 -> {
                        vmFullReport.day = getString(R.string.digit13)
                    }
                    14 -> {
                        vmFullReport.day = getString(R.string.digit14)
                    }
                    15 -> {
                        vmFullReport.day = getString(R.string.digit15)
                    }
                    16 -> {
                        vmFullReport.day = getString(R.string.digit16)
                    }
                    17 -> {
                        vmFullReport.day = getString(R.string.digit17)
                    }
                    18 -> {
                        vmFullReport.day = getString(R.string.digit18)
                    }
                    19 -> {
                        vmFullReport.day = getString(R.string.digit19)
                    }
                    20 -> {
                        vmFullReport.day = getString(R.string.digit20)
                    }
                    21 -> {
                        vmFullReport.day = getString(R.string.digit21)
                    }
                    22 -> {
                        vmFullReport.day = getString(R.string.digit22)
                    }
                    23 -> {
                        vmFullReport.day = getString(R.string.digit23)
                    }
                    24 -> {
                        vmFullReport.day = getString(R.string.digit24)
                    }
                    25 -> {
                        vmFullReport.day = getString(R.string.digit25)
                    }
                    26 -> {
                        vmFullReport.day = getString(R.string.digit26)
                    }
                    27 -> {
                        vmFullReport.day = getString(R.string.digit27)
                    }
                    28 -> {
                        vmFullReport.day = getString(R.string.digit28)
                    }
                    29 -> {
                        vmFullReport.day = getString(R.string.digit29)
                    }
                    30 -> {
                        vmFullReport.day = getString(R.string.digit30)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                vmFullReport.day = Constants.DAY_ALL
            }
        }
    }

    @SuppressLint("CutPasteId")
    private fun initDay28Spinner() {
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.day28))
        dateTimeDialogView.findViewById<Spinner>(R.id.spinnerDay).adapter = spinnerAdapter
        dateTimeDialogView.findViewById<Spinner>(R.id.spinnerDay).onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        vmFullReport.day = Constants.TYPE_ALL
                    }
                    1 -> {
                        vmFullReport.day = getString(R.string.digit01)
                    }
                    2 -> {
                        vmFullReport.day = getString(R.string.digit02)
                    }
                    3 -> {
                        vmFullReport.day = getString(R.string.digit03)
                    }
                    4 -> {
                        vmFullReport.day = getString(R.string.digit04)
                    }
                    5 -> {
                        vmFullReport.day = getString(R.string.digit05)
                    }
                    6 -> {
                        vmFullReport.day = getString(R.string.digit06)
                    }
                    7 -> {
                        vmFullReport.day = getString(R.string.digit07)
                    }
                    8 -> {
                        vmFullReport.day = getString(R.string.digit08)
                    }
                    9 -> {
                        vmFullReport.day = getString(R.string.digit09)
                    }
                    10 -> {
                        vmFullReport.day = getString(R.string.digit10)
                    }
                    11 -> {
                        vmFullReport.day = getString(R.string.digit11)
                    }
                    12 -> {
                        vmFullReport.day = getString(R.string.digit12)
                    }
                    13 -> {
                        vmFullReport.day = getString(R.string.digit13)
                    }
                    14 -> {
                        vmFullReport.day = getString(R.string.digit14)
                    }
                    15 -> {
                        vmFullReport.day = getString(R.string.digit15)
                    }
                    16 -> {
                        vmFullReport.day = getString(R.string.digit16)
                    }
                    17 -> {
                        vmFullReport.day = getString(R.string.digit17)
                    }
                    18 -> {
                        vmFullReport.day = getString(R.string.digit18)
                    }
                    19 -> {
                        vmFullReport.day = getString(R.string.digit19)
                    }
                    20 -> {
                        vmFullReport.day = getString(R.string.digit20)
                    }
                    21 -> {
                        vmFullReport.day = getString(R.string.digit21)
                    }
                    22 -> {
                        vmFullReport.day = getString(R.string.digit22)
                    }
                    23 -> {
                        vmFullReport.day = getString(R.string.digit23)
                    }
                    24 -> {
                        vmFullReport.day = getString(R.string.digit24)
                    }
                    25 -> {
                        vmFullReport.day = getString(R.string.digit25)
                    }
                    26 -> {
                        vmFullReport.day = getString(R.string.digit26)
                    }
                    27 -> {
                        vmFullReport.day = getString(R.string.digit27)
                    }
                    28 -> {
                        vmFullReport.day = getString(R.string.digit28)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                vmFullReport.day = Constants.DAY_ALL
            }
        }
    }

    @SuppressLint("CutPasteId")
    private fun initYearSpinner() {
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.year))
        dateTimeDialogView.findViewById<Spinner>(R.id.spinnerYear).adapter = spinnerAdapter
        dateTimeDialogView.findViewById<Spinner>(R.id.spinnerYear).onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        vmFullReport.year = Constants.YEAR_DEFAULT
                    }
                    1 -> {
                        vmFullReport.year = "2022"
                    }
                    2 -> {
                        vmFullReport.year = "2023"
                    }
                    3 -> {
                        vmFullReport.year = "2024"
                    }
                    4 -> {
                        vmFullReport.year = "2025"
                    }
                    5 -> {
                        vmFullReport.year = "2026"
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                vmFullReport.year = Constants.YEAR_DEFAULT
            }
        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    fun updateAllTransactionsUI(list : List<MC_Posts>){
        if(list.isEmpty()){
            binding.cvNoResultFound.visibility = View.VISIBLE
        }
        else{
            binding.cvNoResultFound.visibility = View.GONE
        }
        // setting transaction search title
        val mMonth = if(vmFullReport.month!=Constants.MONTH_All){
            vmFullReport.month
        } else{ "" }
        val mDay = if(vmFullReport.day!=Constants.DAY_ALL){
            vmFullReport.day
        } else{ "" }
        val mType = if(vmFullReport.type!=Constants.TYPE_ALL){
            vmFullReport.type
        } else{ "" }
        val mCategory = if(vmFullReport.category!=Constants.CATEGORY_All){
            vmFullReport.category
        } else{ "" }

        binding.tvTitleTransactionsFullReport.text = "Transactions : ${vmFullReport.year}-${Util.getMonthNameFromMonthNumber(mMonth)}-$mDay-$mType-$mCategory "
        binding.tvTitleTop.text = "Report : ${vmFullReport.year}-${Util.getMonthNameFromMonthNumber(mMonth)}"

        binding.rvReportDetailsFullReport.layoutManager = LinearLayoutManager(requireContext())
        adapterFullReport = Adapter_FullReport(requireContext(),list)
        binding.rvReportDetailsFullReport.adapter = adapterFullReport
        adapterFullReport!!.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateIncomesUI(list : List<MC_MonthlyReport>){
        if(list.isEmpty()){
            binding.cvNoResultFoundTop.visibility = View.VISIBLE
        }
        else{
            binding.cvNoResultFoundTop.visibility = View.GONE
        }

        binding.rvIncomes.layoutManager = LinearLayoutManager(requireContext())
        adapterIncomes = Adapter_FullReportIncome(list,this@FullReportFragment)
        binding.rvIncomes.adapter = adapterIncomes
        adapterIncomes!!.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateExpensesUI(list : List<MC_MonthlyReport>){
        if(list.isEmpty()){
            binding.cvNoResultFoundTop2.visibility = View.VISIBLE
        }
        else{
            binding.cvNoResultFoundTop2.visibility = View.GONE
        }
        binding.rvExpenses.layoutManager = LinearLayoutManager(requireContext())
        adapterExpenses = Adapter_FullReportExpense(list,this@FullReportFragment)
        binding.rvExpenses.adapter = adapterExpenses
        adapterExpenses!!.notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    private fun  updateTotalIncome(amount : Int?){
        binding.tvTotalIncomeValueTopBarFullReport.text = "$amount tk"
        if (amount != null) {
            totalIncome = amount
        }
    }

    @SuppressLint("SetTextI18n")
    private fun  updateTotalExpense(amount : Int?){
        binding.tvTotalExpenseValueTopBarFullReport.text = "$amount tk"
        if (amount != null) {
            totalExpense = amount
        }
        updateBalance()
    }

    @SuppressLint("SetTextI18n")
    private fun  updateBalance(){
        binding.tvBalanceValueTopBarFullReport.text = "${totalIncome-totalExpense} tk"
    }



    private fun initInterstitialAd() {
        interAd = InterstistialAdHelper(requireContext(), requireActivity(),mInterstitialAd)
        val lastAdShown = prefManager.lastInterstitialAdShownFRF
        if (AdMobUtil.canAdShow(requireContext(), lastAdShown)) {
            interAd.loadinterAd(AdUnitIds.INTERSTITIAL_FULL_REPORT) {
                Log.d("InterAd", "Inter ad loaded -> $it")
                isAdLoaded = it
            }
        }
    }


    private fun showInterAd() {
        if (isAdLoaded) {
            Log.d("InterAD", "InterAd Loaded")
            interAd.showInterAd { isShown: Boolean, error: String? ->
                if (isShown) {
                    Log.d("InterAD", "InterAd has been shown")
                    prefManager.lastInterstitialAdShownFRF = CurrentDate.currentTime24H
                    isAdLoaded = false
                    selectQuery()
                } else {
                    Log.d("InterAD", "InterAd Not been shown->$error")
                    selectQuery()
                }
            }
        } else {
            Log.d("InterAD", "InterAd Not Loaded yet")
            selectQuery()
        }
    }


}