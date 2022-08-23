package com.lincoln4791.dailyexpensemanager.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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

    private val vmFullReport by viewModels<VMFullReport>()

    private val linearLayoutManager = LinearLayoutManager(context)
    private var adapterFullReport: Adapter_FullReport? = null
    private var adapterExpenses: Adapter_FullReportExpense? = null
    private var adapterIncomes: Adapter_FullReportIncome? = null
    private var totalIncome = 0
    private var totalExpense = 0
    private var isAdLoaded = false

    private lateinit var navCon : NavController
    private lateinit var dateTimedialogView : View
    private lateinit var dateTimeDialog : BottomSheetDialog



    private lateinit var interAd: InterstistialAdHelper
    var mInterstitialAd: InterstitialAd? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Util.recordScreenEvent("fullReport_fragment","MainActivity")
        initInterstitialAd()

        navCon = Navigation.findNavController(view)

        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true

        dateTimedialogView = layoutInflater.inflate(R.layout.dialog_select_date_time,null,false)
        dateTimeDialog = BottomSheetDialog(requireContext())

        binding.rvReportDetailsFullReport.layoutManager = linearLayoutManager
        binding.rvReportDetailsFullReport.adapter=adapterFullReport


        binding.cvDailyFullReport.setOnClickListener(View.OnClickListener {
            /*startActivity(Intent(this@FullReport,
                Daily::class.java))*/
        })
        binding.cvMonthlyTransactionsFullReport.setOnClickListener(View.OnClickListener { v: View? ->
           /* startActivity(Intent(this@FullReport, MonthlyReport::class.java))*/
        })
        binding.cvTransactionsFullReport.setOnClickListener(View.OnClickListener { v: View? ->
          /*  val transactionsIntent = Intent(this@FullReport, Transactions::class.java)
            transactionsIntent.putExtra(Extras.TYPE, Constants.TYPE_ALL)
            startActivity(transactionsIntent)*/
        })
        binding.cvTotalIncomesFullReport.setOnClickListener(View.OnClickListener { v: View? ->
           /* val incomeIntent = Intent(this@FullReport, Transactions::class.java)
            incomeIntent.putExtra(Extras.TYPE, Constants.TYPE_INCOME)
            startActivity(incomeIntent)*/
        })
        binding.cvTotalExpensesFullReport.setOnClickListener(View.OnClickListener { v: View? ->
            /*val expenseIntent = Intent(this@FullReport, Transactions::class.java)
            expenseIntent.putExtra(Extras.TYPE, Constants.TYPE_EXPENSE)
            startActivity(expenseIntent)*/
        })

        binding.cvSearch.setOnClickListener {
            selectSearchCriteria()
        }

        binding.cvImg.setOnClickListener(View.OnClickListener { v: View? ->
          navCon.navigateUp()
        })

        binding.tvCurrentBalanceValueToolBarFullReport.text = GlobalVariabls.currentBalance.toString()



        dateTimedialogView.findViewById<Button>(R.id.btnSubmit).setOnClickListener {
            dateTimeDialog.dismiss()
            showInterAd()
            //selectQuery()
        }

        observers()

        //vm_fullReport.getAllCardsByTypeArrayString()
        initMonthSpinner()
        initYearSpinner()
        initTypeSpinner(dateTimedialogView)
        CoroutineScope(Dispatchers.Main).launch {
            selectSearchCriteria()
        }
    }

    private fun observers() {
        vmFullReport.postsList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                is Resource.Success<*> ->  updateAllTransactionsUI(it.value as List<MC_Posts>)
                //is Resource.Failure -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        })

        vmFullReport.expenseList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                is Resource.Success<*> ->  updateExpensesUI(it.value as List<MC_MonthlyReport>)
                //is Resource.Failure -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        })

        vmFullReport.incomeList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                is Resource.Success<*> ->  updateIncomesUI(it.value as List<MC_MonthlyReport>)
                //is Resource.Failure -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        })

        vmFullReport.categoryCards.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                is Resource.Success<*> ->  initExpenseCategorySpinner(it.value as Array<String>)
                //is Resource.Failure -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        })

        vmFullReport.totalIncome.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                is Resource.Success<*> ->  updateTotalIncome(it.value as Int)
                //is Resource.Failure -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        })

        vmFullReport.totalExpense.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                is Resource.Success<*> ->  updateTotalExpense(it.value as Int)
                //is Resource.Failure -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun selectSearchCriteria() {
        dateTimeDialog.setContentView(dateTimedialogView)
        dateTimeDialog.show()

    }

    private fun selectQuery() {

        if (vmFullReport.month != Constants.MONTH_All &&
            vmFullReport.day != Constants.DAY_ALL && vmFullReport.type != Constants.TYPE_ALL &&
            vmFullReport.category != Constants.CATEGORY_All
        ) {
            Log.d("Tag",
                "1)  year" + vmFullReport.year + "month" + vmFullReport.month + "day" + vmFullReport.day + " type " + vmFullReport!!.type + " category " + vmFullReport!!.category)
            vmFullReport.loadYearMonthDayTypeCategoryWise(vmFullReport.year,vmFullReport.month
                ,vmFullReport.day,vmFullReport.type,vmFullReport.category)
            vmFullReport.loadYearMonthIncomeWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_INCOME)
            vmFullReport.loadYearMonthExpeneWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_EXPENSE)
        } else if (vmFullReport.month != Constants.MONTH_All &&
            vmFullReport.type != Constants.TYPE_ALL &&
            vmFullReport.category != Constants.CATEGORY_All && vmFullReport.day == Constants.DAY_ALL
        ) {
            Log.d("Tag",
                "2)  year" + vmFullReport.year + "month" + vmFullReport.month + "day" + vmFullReport.day + " type " + vmFullReport!!.type + " category " + vmFullReport!!.category)
            vmFullReport.loadYearMonthTypeCategoryWise(
                vmFullReport.year,vmFullReport.month,vmFullReport.type,vmFullReport.category
            )
            vmFullReport.loadYearMonthIncomeWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_INCOME)
            vmFullReport.loadYearMonthExpeneWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_EXPENSE)
        } else if (vmFullReport.month != Constants.MONTH_All &&
            vmFullReport.day != Constants.DAY_ALL &&
            vmFullReport.category != Constants.CATEGORY_All && vmFullReport.type == Constants.TYPE_ALL
        ) {
            val d = Log.d("Tag",
                "3)  year" + vmFullReport.year + "month" + vmFullReport.month + "day" + vmFullReport.day + " type " + vmFullReport!!.type + " category " + vmFullReport!!.category)
            vmFullReport.loadYearMonthDayCategoryWise(
                vmFullReport.year,vmFullReport.month,vmFullReport.day,vmFullReport.category
            )
            vmFullReport.loadYearMonthIncomeWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_INCOME)
            vmFullReport.loadYearMonthExpeneWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_EXPENSE)
        } else if (vmFullReport.month != Constants.MONTH_All &&
            vmFullReport.day != Constants.DAY_ALL &&
            vmFullReport.type != Constants.TYPE_ALL && vmFullReport.category == Constants.CATEGORY_All
        ) {
            Log.d("Tag",
                "4)  year" + vmFullReport.year + "month" + vmFullReport.month + "day" + vmFullReport.day + " type " + vmFullReport!!.type + " category " + vmFullReport!!.category)
            vmFullReport.loadYearMonthDayTypeWise(
                vmFullReport.year,vmFullReport.month,vmFullReport.day,vmFullReport.type
            )
            vmFullReport.loadYearMonthIncomeWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_INCOME)
            vmFullReport.loadYearMonthExpeneWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_EXPENSE)
        } else if (vmFullReport.month != Constants.MONTH_All &&
            vmFullReport.day != Constants.DAY_ALL && vmFullReport.type == Constants.TYPE_ALL && vmFullReport.category == Constants.CATEGORY_All
        ) {
            Log.d("Tag",
                "5)  year" + vmFullReport.year + "month" + vmFullReport.month + "day" + vmFullReport.day + " type " + vmFullReport!!.type + " category " + vmFullReport!!.category)
            vmFullReport.loadYearMonthDayWise(
                vmFullReport.year,vmFullReport.month,vmFullReport.day
            )
            vmFullReport.loadYearMonthIncomeWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_INCOME)
            vmFullReport.loadYearMonthExpeneWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_EXPENSE)
            vmFullReport.loadYearMonthBalance(vmFullReport.year,vmFullReport.month)
        } else if (vmFullReport.month != Constants.MONTH_All &&
            vmFullReport.type != Constants.TYPE_ALL && vmFullReport.day == Constants.DAY_ALL && vmFullReport.category == Constants.CATEGORY_All
        ) {
            Log.d("Tag",
                "6)  year" + vmFullReport.year + "month" + vmFullReport.month + "day" + vmFullReport.day + " type " + vmFullReport!!.type + " category " + vmFullReport!!.category)
            vmFullReport.loadYearMonthTypeWise(
                vmFullReport.year,vmFullReport.month,vmFullReport.type
            )
            vmFullReport.loadYearMonthIncomeWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_INCOME)
            vmFullReport.loadYearMonthExpeneWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_EXPENSE)
        } else if (vmFullReport.month != Constants.MONTH_All &&
            vmFullReport.category != Constants.CATEGORY_All && vmFullReport.day == Constants.DAY_ALL && vmFullReport!!.type == Constants.TYPE_ALL
        ) {
            Log.d("Tag",
                "7)  year" + vmFullReport.year + "month" + vmFullReport.month + "day" + vmFullReport.day + " type " + vmFullReport!!.type + " category " + vmFullReport!!.category)
            vmFullReport.loadYearMonthCategoryWise(
                vmFullReport.year,vmFullReport.month,vmFullReport.category
            )
            vmFullReport.loadYearMonthIncomeWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_INCOME)
            vmFullReport.loadYearMonthExpeneWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_EXPENSE)
        } else if (vmFullReport.month != Constants.MONTH_All && vmFullReport.day == Constants.DAY_ALL && vmFullReport!!.type == Constants.TYPE_ALL && vmFullReport!!.category == Constants.CATEGORY_All) {
            Log.d("Tag",
                "8)  year" + vmFullReport.year + "month" + vmFullReport.month + "day" + vmFullReport.day + " type " + vmFullReport!!.type + " category " + vmFullReport!!.category)
            vmFullReport.loadYearMonthWise(
                vmFullReport.year,vmFullReport.month
            )
            vmFullReport.loadYearMonthIncomeWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_INCOME)
            vmFullReport.loadYearMonthExpeneWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_EXPENSE)
            vmFullReport.loadYearMonthIncomeTotal(vmFullReport.year,vmFullReport.month,Constants.TYPE_INCOME)
            vmFullReport.loadYearMonthExpenseTotal(vmFullReport.year,vmFullReport.month,Constants.TYPE_EXPENSE)
        } else if (vmFullReport.type != Constants.TYPE_ALL && vmFullReport.month == Constants.MONTH_All && vmFullReport!!.day == Constants.DAY_ALL && vmFullReport!!.category == Constants.CATEGORY_All) {
            Log.d("Tag",
                "9)  year" + vmFullReport.year + "month" + vmFullReport.month + "day" + vmFullReport.day + " type " + vmFullReport!!.type + " category " + vmFullReport!!.category)
            vmFullReport.loadYearTypeWise(vmFullReport.year,vmFullReport.type)
            vmFullReport.loadYearIncomeWiseByGroup(vmFullReport.year,Constants.TYPE_INCOME)
            vmFullReport.loadYearExpeneWiseByGroup(vmFullReport.year,Constants.TYPE_EXPENSE)
        } else if (vmFullReport.type != Constants.TYPE_ALL && vmFullReport.category != Constants.CATEGORY_All && vmFullReport!!.day == Constants.DAY_ALL && vmFullReport!!.month == Constants.MONTH_All) {
            Log.d("Tag",
                "12)  year" + vmFullReport.year + "month" + vmFullReport.month + "day" + vmFullReport.day + " type " + vmFullReport!!.type + " category " + vmFullReport!!.category)
            vmFullReport.loadYearTypeCategoryWise(
                vmFullReport.year,vmFullReport.type,vmFullReport.category
            )
            vmFullReport.loadYearMonthIncomeWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_INCOME)
            vmFullReport.loadYearMonthExpeneWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_EXPENSE)
        } else if (vmFullReport.category != Constants.CATEGORY_All && vmFullReport.month == Constants.MONTH_All && vmFullReport!!.day == Constants.DAY_ALL && vmFullReport!!.type == Constants.TYPE_ALL) {
            Log.d("Tag",
                "10)  year" + vmFullReport.year + "month" + vmFullReport.month + "day" + vmFullReport.day + " type " + vmFullReport!!.type + " category " + vmFullReport!!.category)
            vmFullReport.loadYearMonthWise(
                vmFullReport.year,vmFullReport.month
            )
            vmFullReport.loadYearMonthIncomeWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_INCOME)
            vmFullReport.loadYearMonthExpeneWiseByGroup(vmFullReport.year, vmFullReport.month,Constants.TYPE_EXPENSE)
        } else {
            Log.d("Tag",
                "11)  year" + vmFullReport.year + "month" + vmFullReport.month + "day" + vmFullReport.day + " type " + vmFullReport!!.type + " category " + vmFullReport!!.category)
            vmFullReport.loadYearWise(vmFullReport.year)
            vmFullReport.loadYearIncomeWiseByGroup(vmFullReport.year,Constants.TYPE_INCOME)
            vmFullReport.loadYearExpeneWiseByGroup(vmFullReport.year,Constants.TYPE_EXPENSE)
        }
    }

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
                if (position == 0) {
                    vmFullReport.type = Constants.TYPE_ALL
                    vmFullReport.category = Constants.CATEGORY_All
                    initNullCategorySpinner()
                } else if (position == 1) {
                    vmFullReport.type = Constants.TYPE_INCOME
                    vmFullReport.getAllCardsByTypeArrayString(Constants.TYPE_INCOME)
                } else if (position == 2) {
                    vmFullReport.type = Constants.TYPE_EXPENSE
                    vmFullReport.getAllCardsByTypeArrayString(Constants.TYPE_EXPENSE)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                vmFullReport.type = Constants.TYPE_ALL
                vmFullReport.category = Constants.CATEGORY_All
                initNullCategorySpinner()
            }
        }
    }

    private fun initMonthSpinner() {
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.monthNames))
        dateTimedialogView.findViewById<Spinner>(R.id.spinnerMonth).adapter = spinnerAdapter
        dateTimedialogView.findViewById<Spinner>(R.id.spinnerMonth).onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    vmFullReport.month = Constants.MONTH_All
                    vmFullReport.day = Constants.DAY_ALL
                    initDay0Spinner()
                } else if (position == 1 || position == 3 || position == 5 || position == 7 || position == 8 || position == 10 || position == 12) {
                    if (position == 1) {
                        vmFullReport.month = getString(R.string.digit01)
                    } else if (position == 3) {
                        vmFullReport.month = getString(R.string.digit03)
                    } else if (position == 5) {
                        vmFullReport.month = getString(R.string.digit05)
                    } else if (position == 7) {
                        vmFullReport.month = getString(R.string.digit07)
                    } else if (position == 8) {
                        vmFullReport.month = getString(R.string.digit08)
                    } else if (position == 10) {
                        vmFullReport.month = getString(R.string.digit10)
                    } else if (position == 12) {
                        vmFullReport.month = getString(R.string.digit12)
                    }
                    initDay31Spinner()
                } else if (position == 4 || position == 6 || position == 9 || position == 11) {
                    if (position == 4) {
                        vmFullReport.month = getString(R.string.digit04)
                    } else if (position == 6) {
                        vmFullReport.month = getString(R.string.digit06)
                    } else if (position == 9) {
                        vmFullReport.month = getString(R.string.digit09)
                    }
                    if (position == 11) {
                        vmFullReport.month = getString(R.string.digit11)
                    }
                    initDay30Spinner()
                } else if (position == 2) {
                    vmFullReport.month = getString(R.string.digit02)
                    initDay28Spinner()
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
        dateTimedialogView.findViewById<Spinner>(R.id.spinnerCategory).adapter = spinnerAdapter
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
        val expenseSpinner = dateTimedialogView.findViewById<Spinner>(R.id.spinnerCategory)
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
        dateTimedialogView.findViewById<Spinner>(R.id.spinnerDay).adapter = spinnerAdapter
    }

    private fun initDay31Spinner() {
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.day31))
        dateTimedialogView.findViewById<Spinner>(R.id.spinnerDay).adapter = spinnerAdapter
        dateTimedialogView.findViewById<Spinner>(R.id.spinnerDay).onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    vmFullReport.day = Constants.DAY_ALL
                } else if (position == 1) {
                    vmFullReport.day = getString(R.string.digit01)
                } else if (position == 2) {
                    vmFullReport.day = getString(R.string.digit02)
                } else if (position == 3) {
                    vmFullReport.day = getString(R.string.digit03)
                } else if (position == 4) {
                    vmFullReport.day = getString(R.string.digit04)
                } else if (position == 5) {
                    vmFullReport.day = getString(R.string.digit05)
                } else if (position == 6) {
                    vmFullReport.day = getString(R.string.digit06)
                } else if (position == 7) {
                    vmFullReport.day = getString(R.string.digit07)
                } else if (position == 8) {
                    vmFullReport.day = getString(R.string.digit08)
                } else if (position == 9) {
                    vmFullReport.day = getString(R.string.digit09)
                } else if (position == 10) {
                    vmFullReport.day = getString(R.string.digit10)
                } else if (position == 11) {
                    vmFullReport.day = getString(R.string.digit11)
                } else if (position == 12) {
                    vmFullReport.day = getString(R.string.digit12)
                } else if (position == 13) {
                    vmFullReport.day = getString(R.string.digit13)
                } else if (position == 14) {
                    vmFullReport.day = getString(R.string.digit14)
                } else if (position == 15) {
                    vmFullReport.day = getString(R.string.digit15)
                } else if (position == 16) {
                    vmFullReport.day = getString(R.string.digit16)
                } else if (position == 17) {
                    vmFullReport.day = getString(R.string.digit17)
                } else if (position == 18) {
                    vmFullReport.day = getString(R.string.digit18)
                } else if (position == 19) {
                    vmFullReport.day = getString(R.string.digit19)
                } else if (position == 20) {
                    vmFullReport.day = getString(R.string.digit20)
                } else if (position == 21) {
                    vmFullReport.day = getString(R.string.digit21)
                } else if (position == 22) {
                    vmFullReport.day = getString(R.string.digit22)
                } else if (position == 23) {
                    vmFullReport.day = getString(R.string.digit23)
                } else if (position == 24) {
                    vmFullReport.day = getString(R.string.digit24)
                } else if (position == 25) {
                    vmFullReport.day = getString(R.string.digit25)
                } else if (position == 26) {
                    vmFullReport.day = getString(R.string.digit26)
                } else if (position == 27) {
                    vmFullReport.day = getString(R.string.digit27)
                } else if (position == 28) {
                    vmFullReport.day = getString(R.string.digit28)
                } else if (position == 29) {
                    vmFullReport.day = getString(R.string.digit29)
                } else if (position == 30) {
                    vmFullReport.day = getString(R.string.digit30)
                } else if (position == 31) {
                    vmFullReport.day = getString(R.string.digit31)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                vmFullReport.day = Constants.DAY_ALL
            }
        }
    }

    private fun initDay30Spinner() {
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.day30))
        dateTimedialogView.findViewById<Spinner>(R.id.spinnerDay).adapter = spinnerAdapter
        dateTimedialogView.findViewById<Spinner>(R.id.spinnerDay).onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    vmFullReport.day = Constants.DAY_ALL
                } else if (position == 1) {
                    vmFullReport.day = getString(R.string.digit01)
                } else if (position == 2) {
                    vmFullReport.day = getString(R.string.digit02)
                } else if (position == 3) {
                    vmFullReport.day = getString(R.string.digit03)
                } else if (position == 4) {
                    vmFullReport.day = getString(R.string.digit04)
                } else if (position == 5) {
                    vmFullReport.day = getString(R.string.digit05)
                } else if (position == 6) {
                    vmFullReport.day = getString(R.string.digit06)
                } else if (position == 7) {
                    vmFullReport.day = getString(R.string.digit07)
                } else if (position == 8) {
                    vmFullReport.day = getString(R.string.digit08)
                } else if (position == 9) {
                    vmFullReport.day = getString(R.string.digit09)
                } else if (position == 10) {
                    vmFullReport.day = getString(R.string.digit10)
                } else if (position == 11) {
                    vmFullReport.day = getString(R.string.digit11)
                } else if (position == 12) {
                    vmFullReport.day = getString(R.string.digit12)
                } else if (position == 13) {
                    vmFullReport.day = getString(R.string.digit13)
                } else if (position == 14) {
                    vmFullReport.day = getString(R.string.digit14)
                } else if (position == 15) {
                    vmFullReport.day = getString(R.string.digit15)
                } else if (position == 16) {
                    vmFullReport.day = getString(R.string.digit16)
                } else if (position == 17) {
                    vmFullReport.day = getString(R.string.digit17)
                } else if (position == 18) {
                    vmFullReport.day = getString(R.string.digit18)
                } else if (position == 19) {
                    vmFullReport.day = getString(R.string.digit19)
                } else if (position == 20) {
                    vmFullReport.day = getString(R.string.digit20)
                } else if (position == 21) {
                    vmFullReport.day = getString(R.string.digit21)
                } else if (position == 22) {
                    vmFullReport.day = getString(R.string.digit22)
                } else if (position == 23) {
                    vmFullReport.day = getString(R.string.digit23)
                } else if (position == 24) {
                    vmFullReport.day = getString(R.string.digit24)
                } else if (position == 25) {
                    vmFullReport.day = getString(R.string.digit25)
                } else if (position == 26) {
                    vmFullReport.day = getString(R.string.digit26)
                } else if (position == 27) {
                    vmFullReport.day = getString(R.string.digit27)
                } else if (position == 28) {
                    vmFullReport.day = getString(R.string.digit28)
                } else if (position == 29) {
                    vmFullReport.day = getString(R.string.digit29)
                } else if (position == 30) {
                    vmFullReport.day = getString(R.string.digit30)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                vmFullReport.day = Constants.DAY_ALL
            }
        }
    }

    private fun initDay28Spinner() {
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.day28))
        dateTimedialogView.findViewById<Spinner>(R.id.spinnerDay).adapter = spinnerAdapter
        dateTimedialogView.findViewById<Spinner>(R.id.spinnerDay).onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    vmFullReport.day = Constants.TYPE_ALL
                } else if (position == 1) {
                    vmFullReport.day = getString(R.string.digit01)
                } else if (position == 2) {
                    vmFullReport.day = getString(R.string.digit02)
                } else if (position == 3) {
                    vmFullReport.day = getString(R.string.digit03)
                } else if (position == 4) {
                    vmFullReport.day = getString(R.string.digit04)
                } else if (position == 5) {
                    vmFullReport.day = getString(R.string.digit05)
                } else if (position == 6) {
                    vmFullReport.day = getString(R.string.digit06)
                } else if (position == 7) {
                    vmFullReport.day = getString(R.string.digit07)
                } else if (position == 8) {
                    vmFullReport.day = getString(R.string.digit08)
                } else if (position == 9) {
                    vmFullReport.day = getString(R.string.digit09)
                } else if (position == 10) {
                    vmFullReport.day = getString(R.string.digit10)
                } else if (position == 11) {
                    vmFullReport.day = getString(R.string.digit11)
                } else if (position == 12) {
                    vmFullReport.day = getString(R.string.digit12)
                } else if (position == 13) {
                    vmFullReport.day = getString(R.string.digit13)
                } else if (position == 14) {
                    vmFullReport.day = getString(R.string.digit14)
                } else if (position == 15) {
                    vmFullReport.day = getString(R.string.digit15)
                } else if (position == 16) {
                    vmFullReport.day = getString(R.string.digit16)
                } else if (position == 17) {
                    vmFullReport.day = getString(R.string.digit17)
                } else if (position == 18) {
                    vmFullReport.day = getString(R.string.digit18)
                } else if (position == 19) {
                    vmFullReport.day = getString(R.string.digit19)
                } else if (position == 20) {
                    vmFullReport.day = getString(R.string.digit20)
                } else if (position == 21) {
                    vmFullReport.day = getString(R.string.digit21)
                } else if (position == 22) {
                    vmFullReport.day = getString(R.string.digit22)
                } else if (position == 23) {
                    vmFullReport.day = getString(R.string.digit23)
                } else if (position == 24) {
                    vmFullReport.day = getString(R.string.digit24)
                } else if (position == 25) {
                    vmFullReport.day = getString(R.string.digit25)
                } else if (position == 26) {
                    vmFullReport.day = getString(R.string.digit26)
                } else if (position == 27) {
                    vmFullReport.day = getString(R.string.digit27)
                } else if (position == 28) {
                    vmFullReport.day = getString(R.string.digit28)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                vmFullReport.day = Constants.DAY_ALL
            }
        }
    }

    private fun initYearSpinner() {
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.year))
        dateTimedialogView.findViewById<Spinner>(R.id.spinnerYear).adapter = spinnerAdapter
        dateTimedialogView.findViewById<Spinner>(R.id.spinnerYear).onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    vmFullReport.year = Constants.YEAR_DEFAULT
                } else if (position == 1) {
                    vmFullReport.year = "2022"
                } else if (position == 2) {
                    vmFullReport.year = "2023"
                } else if (position == 3) {
                    vmFullReport.year = "2024"
                } else if (position == 4) {
                    vmFullReport.year = "2025"
                } else if (position == 5) {
                    vmFullReport.year = "2026"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                vmFullReport.year = Constants.YEAR_DEFAULT
            }
        }
    }

    fun updateAllTransactionsUI(list : List<MC_Posts>){
        if(list.isEmpty()){
            binding.cvNoResultFound.visibility = View.VISIBLE
        }
        else{
            binding.cvNoResultFound.visibility = View.GONE
        }
        // setting transaction searchs title
        val mMonth = if(vmFullReport.month!=Constants.MONTH_All){ "${vmFullReport.month}" } else{ "" }
        val mDay = if(vmFullReport.day!=Constants.DAY_ALL){ "${vmFullReport.day}" } else{ "" }
        val mType = if(vmFullReport.type!=Constants.TYPE_ALL){ "${vmFullReport.type}" } else{ "" }
        val mCategory = if(vmFullReport.category!=Constants.CATEGORY_All){ "${vmFullReport.category}" } else{ "" }

        binding.tvTitleTransactionsFullReport.text = "Transactions : ${vmFullReport.year}-${Util.getMonthNameFromMonthNumber(mMonth)}-$mDay-$mType-$mCategory "
        binding.tvTitleTop.text = "Report : ${vmFullReport.year}-${Util.getMonthNameFromMonthNumber(mMonth)}"

        binding.rvReportDetailsFullReport.layoutManager = LinearLayoutManager(requireContext())
        adapterFullReport = Adapter_FullReport(requireContext(),list)
        binding.rvReportDetailsFullReport.adapter = adapterFullReport
        adapterFullReport!!.notifyDataSetChanged()
    }

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

    private fun  updateTotalIncome(amount : Int?){
        binding.tvTotalIncomeValueTopBarFullReport.text = "$amount tk"
        if (amount != null) {
            totalIncome = amount
        }
    }

    private fun  updateTotalExpense(amount : Int?){
        binding.tvTotalExpenseValueTopBarFullReport.text = "$amount tk"
        if (amount != null) {
            totalExpense = amount
        }
        updateBalance()
    }

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