package com.lincoln4791.dailyexpensemanager.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.itmedicus.patientaid.ads.admobAdsUpdated.AdMobUtil
import com.lincoln4791.dailyexpensemanager.Adapters.*
import com.lincoln4791.dailyexpensemanager.R
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
import com.lincoln4791.dailyexpensemanager.viewModels.VM_FullReport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FullReportFragment : Fragment() {
    private val linearLayoutManager = LinearLayoutManager(context)
    private var adapterFullReport: Adapter_FullReport? = null
    private var adapterExpenses: Adapter_FullReportExpense? = null
    private var adapterIncomes: Adapter_FullReportIncome? = null
    private var totalIncome = 0
    private var totalExpense = 0
    private var isAdLoaded = false

    private lateinit var vm_fullReport: VM_FullReport
    private lateinit var binding : FragmentFullReportBinding
    private lateinit var navCon : NavController
    private lateinit var dateTimedialogView : View
    private lateinit var dateTimeDialog : BottomSheetDialog
    private lateinit var prefManager : PrefManager

    private lateinit var interAd: InterstistialAdHelper
    var mInterstitialAd: InterstitialAd? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        prefManager = PrefManager(requireContext())
        // Inflate the layout for this fragment
        binding = FragmentFullReportBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Util.recordScreenEvent("fullReport_fragment","MainActivity")
        initInterstitialAd()

        navCon = Navigation.findNavController(view)
        vm_fullReport = ViewModelProvider(this)[VM_FullReport::class.java]

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
            delay(300)
            selectSearchCriteria()
        }
    }

    private fun observers() {
        vm_fullReport.postsList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                is Resource.Success ->  updateAllTransactionsUI(it.data)
                is Resource.Error -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        })

        vm_fullReport.expenseList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                is Resource.Success ->  updateExpensesUI(it.data)
                is Resource.Error -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        })

        vm_fullReport.incomeList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                is Resource.Success ->  updateIncomesUI(it.data)
                is Resource.Error -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        })

        vm_fullReport.categoryCards.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                is Resource.Success ->  initExpenseCategorySpinner(it.data)
                is Resource.Error -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        })

        vm_fullReport.totalIncome.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                is Resource.Success ->  updateTotalIncome(it.data)
                is Resource.Error -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        })

        vm_fullReport.totalExpense.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                is Resource.Success ->  updateTotalExpense(it.data)
                is Resource.Error -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun selectSearchCriteria() {
        dateTimeDialog.setContentView(dateTimedialogView)
        dateTimeDialog.show()

    }

    private fun selectQuery() {

        if (vm_fullReport.month != Constants.MONTH_All &&
            vm_fullReport.day != Constants.DAY_ALL && vm_fullReport.type != Constants.TYPE_ALL &&
            vm_fullReport.category != Constants.CATEGORY_All
        ) {
            Log.d("Tag",
                "1)  year" + vm_fullReport.year + "month" + vm_fullReport.month + "day" + vm_fullReport.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            vm_fullReport.loadYearMonthDayTypeCategoryWise(vm_fullReport.year,vm_fullReport.month
                ,vm_fullReport.day,vm_fullReport.type,vm_fullReport.category)
            vm_fullReport.loadYearMonthIncomeWiseByGroup(vm_fullReport.year, vm_fullReport.month,Constants.TYPE_INCOME)
            vm_fullReport.loadYearMonthExpeneWiseByGroup(vm_fullReport.year, vm_fullReport.month,Constants.TYPE_EXPENSE)
        } else if (vm_fullReport.month != Constants.MONTH_All &&
            vm_fullReport.type != Constants.TYPE_ALL &&
            vm_fullReport.category != Constants.CATEGORY_All && vm_fullReport.day == Constants.DAY_ALL
        ) {
            Log.d("Tag",
                "2)  year" + vm_fullReport.year + "month" + vm_fullReport.month + "day" + vm_fullReport.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            vm_fullReport.loadYearMonthTypeCategoryWise(
                vm_fullReport.year,vm_fullReport.month,vm_fullReport.type,vm_fullReport.category
            )
            vm_fullReport.loadYearMonthIncomeWiseByGroup(vm_fullReport.year, vm_fullReport.month,Constants.TYPE_INCOME)
            vm_fullReport.loadYearMonthExpeneWiseByGroup(vm_fullReport.year, vm_fullReport.month,Constants.TYPE_EXPENSE)
        } else if (vm_fullReport.month != Constants.MONTH_All &&
            vm_fullReport.day != Constants.DAY_ALL &&
            vm_fullReport.category != Constants.CATEGORY_All && vm_fullReport.type == Constants.TYPE_ALL
        ) {
            val d = Log.d("Tag",
                "3)  year" + vm_fullReport.year + "month" + vm_fullReport.month + "day" + vm_fullReport.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            vm_fullReport.loadYearMonthDayCategoryWise(
                vm_fullReport.year,vm_fullReport.month,vm_fullReport.day,vm_fullReport.category
            )
            vm_fullReport.loadYearMonthIncomeWiseByGroup(vm_fullReport.year, vm_fullReport.month,Constants.TYPE_INCOME)
            vm_fullReport.loadYearMonthExpeneWiseByGroup(vm_fullReport.year, vm_fullReport.month,Constants.TYPE_EXPENSE)
        } else if (vm_fullReport.month != Constants.MONTH_All &&
            vm_fullReport.day != Constants.DAY_ALL &&
            vm_fullReport.type != Constants.TYPE_ALL && vm_fullReport.category == Constants.CATEGORY_All
        ) {
            Log.d("Tag",
                "4)  year" + vm_fullReport.year + "month" + vm_fullReport.month + "day" + vm_fullReport.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            vm_fullReport.loadYearMonthDayTypeWise(
                vm_fullReport.year,vm_fullReport.month,vm_fullReport.day,vm_fullReport.type
            )
            vm_fullReport.loadYearMonthIncomeWiseByGroup(vm_fullReport.year, vm_fullReport.month,Constants.TYPE_INCOME)
            vm_fullReport.loadYearMonthExpeneWiseByGroup(vm_fullReport.year, vm_fullReport.month,Constants.TYPE_EXPENSE)
        } else if (vm_fullReport.month != Constants.MONTH_All &&
            vm_fullReport.day != Constants.DAY_ALL && vm_fullReport.type == Constants.TYPE_ALL && vm_fullReport.category == Constants.CATEGORY_All
        ) {
            Log.d("Tag",
                "5)  year" + vm_fullReport.year + "month" + vm_fullReport.month + "day" + vm_fullReport.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            vm_fullReport.loadYearMonthDayWise(
                vm_fullReport.year,vm_fullReport.month,vm_fullReport.day
            )
            vm_fullReport.loadYearMonthIncomeWiseByGroup(vm_fullReport.year, vm_fullReport.month,Constants.TYPE_INCOME)
            vm_fullReport.loadYearMonthExpeneWiseByGroup(vm_fullReport.year, vm_fullReport.month,Constants.TYPE_EXPENSE)
            vm_fullReport.loadYearMonthBalance(vm_fullReport.year,vm_fullReport.month)
        } else if (vm_fullReport.month != Constants.MONTH_All &&
            vm_fullReport.type != Constants.TYPE_ALL && vm_fullReport.day == Constants.DAY_ALL && vm_fullReport.category == Constants.CATEGORY_All
        ) {
            Log.d("Tag",
                "6)  year" + vm_fullReport.year + "month" + vm_fullReport.month + "day" + vm_fullReport.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            vm_fullReport.loadYearMonthTypeWise(
                vm_fullReport.year,vm_fullReport.month,vm_fullReport.type
            )
            vm_fullReport.loadYearMonthIncomeWiseByGroup(vm_fullReport.year, vm_fullReport.month,Constants.TYPE_INCOME)
            vm_fullReport.loadYearMonthExpeneWiseByGroup(vm_fullReport.year, vm_fullReport.month,Constants.TYPE_EXPENSE)
        } else if (vm_fullReport.month != Constants.MONTH_All &&
            vm_fullReport.category != Constants.CATEGORY_All && vm_fullReport.day == Constants.DAY_ALL && vm_fullReport!!.type == Constants.TYPE_ALL
        ) {
            Log.d("Tag",
                "7)  year" + vm_fullReport.year + "month" + vm_fullReport.month + "day" + vm_fullReport.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            vm_fullReport.loadYearMonthCategoryWise(
                vm_fullReport.year,vm_fullReport.month,vm_fullReport.category
            )
            vm_fullReport.loadYearMonthIncomeWiseByGroup(vm_fullReport.year, vm_fullReport.month,Constants.TYPE_INCOME)
            vm_fullReport.loadYearMonthExpeneWiseByGroup(vm_fullReport.year, vm_fullReport.month,Constants.TYPE_EXPENSE)
        } else if (vm_fullReport.month != Constants.MONTH_All && vm_fullReport.day == Constants.DAY_ALL && vm_fullReport!!.type == Constants.TYPE_ALL && vm_fullReport!!.category == Constants.CATEGORY_All) {
            Log.d("Tag",
                "8)  year" + vm_fullReport.year + "month" + vm_fullReport.month + "day" + vm_fullReport.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            vm_fullReport.loadYearMonthWise(
                vm_fullReport.year,vm_fullReport.month
            )
            vm_fullReport.loadYearMonthIncomeWiseByGroup(vm_fullReport.year, vm_fullReport.month,Constants.TYPE_INCOME)
            vm_fullReport.loadYearMonthExpeneWiseByGroup(vm_fullReport.year, vm_fullReport.month,Constants.TYPE_EXPENSE)
            vm_fullReport.loadYearMonthIncomeTotal(vm_fullReport.year,vm_fullReport.month,Constants.TYPE_INCOME)
            vm_fullReport.loadYearMonthExpenseTotal(vm_fullReport.year,vm_fullReport.month,Constants.TYPE_EXPENSE)
        } else if (vm_fullReport.type != Constants.TYPE_ALL && vm_fullReport.month == Constants.MONTH_All && vm_fullReport!!.day == Constants.DAY_ALL && vm_fullReport!!.category == Constants.CATEGORY_All) {
            Log.d("Tag",
                "9)  year" + vm_fullReport.year + "month" + vm_fullReport.month + "day" + vm_fullReport.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            vm_fullReport.loadYearTypeWise(vm_fullReport.year,vm_fullReport.type)
            vm_fullReport.loadYearIncomeWiseByGroup(vm_fullReport.year,Constants.TYPE_INCOME)
            vm_fullReport.loadYearExpeneWiseByGroup(vm_fullReport.year,Constants.TYPE_EXPENSE)
        } else if (vm_fullReport.type != Constants.TYPE_ALL && vm_fullReport.category != Constants.CATEGORY_All && vm_fullReport!!.day == Constants.DAY_ALL && vm_fullReport!!.month == Constants.MONTH_All) {
            Log.d("Tag",
                "12)  year" + vm_fullReport.year + "month" + vm_fullReport.month + "day" + vm_fullReport.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            vm_fullReport.loadYearTypeCategoryWise(
                vm_fullReport.year,vm_fullReport.type,vm_fullReport.category
            )
            vm_fullReport.loadYearMonthIncomeWiseByGroup(vm_fullReport.year, vm_fullReport.month,Constants.TYPE_INCOME)
            vm_fullReport.loadYearMonthExpeneWiseByGroup(vm_fullReport.year, vm_fullReport.month,Constants.TYPE_EXPENSE)
        } else if (vm_fullReport.category != Constants.CATEGORY_All && vm_fullReport.month == Constants.MONTH_All && vm_fullReport!!.day == Constants.DAY_ALL && vm_fullReport!!.type == Constants.TYPE_ALL) {
            Log.d("Tag",
                "10)  year" + vm_fullReport.year + "month" + vm_fullReport.month + "day" + vm_fullReport.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            vm_fullReport.loadYearMonthWise(
                vm_fullReport.year,vm_fullReport.month
            )
            vm_fullReport.loadYearMonthIncomeWiseByGroup(vm_fullReport.year, vm_fullReport.month,Constants.TYPE_INCOME)
            vm_fullReport.loadYearMonthExpeneWiseByGroup(vm_fullReport.year, vm_fullReport.month,Constants.TYPE_EXPENSE)
        } else {
            Log.d("Tag",
                "11)  year" + vm_fullReport.year + "month" + vm_fullReport.month + "day" + vm_fullReport.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            vm_fullReport.loadYearWise(vm_fullReport.year)
            vm_fullReport.loadYearIncomeWiseByGroup(vm_fullReport.year,Constants.TYPE_INCOME)
            vm_fullReport.loadYearExpeneWiseByGroup(vm_fullReport.year,Constants.TYPE_EXPENSE)
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
                    vm_fullReport.type = Constants.TYPE_ALL
                    vm_fullReport.category = Constants.CATEGORY_All
                    initNullCategorySpinner()
                } else if (position == 1) {
                    vm_fullReport.type = Constants.TYPE_INCOME
                    vm_fullReport.getAllCardsByTypeArrayString(Constants.TYPE_INCOME)
                } else if (position == 2) {
                    vm_fullReport.type = Constants.TYPE_EXPENSE
                    vm_fullReport.getAllCardsByTypeArrayString(Constants.TYPE_EXPENSE)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                vm_fullReport.type = Constants.TYPE_ALL
                vm_fullReport.category = Constants.CATEGORY_All
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
                    vm_fullReport.month = Constants.MONTH_All
                    vm_fullReport.day = Constants.DAY_ALL
                    initDay0Spinner()
                } else if (position == 1 || position == 3 || position == 5 || position == 7 || position == 8 || position == 10 || position == 12) {
                    if (position == 1) {
                        vm_fullReport.month = getString(R.string.digit01)
                    } else if (position == 3) {
                        vm_fullReport.month = getString(R.string.digit03)
                    } else if (position == 5) {
                        vm_fullReport.month = getString(R.string.digit05)
                    } else if (position == 7) {
                        vm_fullReport.month = getString(R.string.digit07)
                    } else if (position == 8) {
                        vm_fullReport.month = getString(R.string.digit08)
                    } else if (position == 10) {
                        vm_fullReport.month = getString(R.string.digit10)
                    } else if (position == 12) {
                        vm_fullReport.month = getString(R.string.digit12)
                    }
                    initDay31Spinner()
                } else if (position == 4 || position == 6 || position == 9 || position == 11) {
                    if (position == 4) {
                        vm_fullReport.month = getString(R.string.digit04)
                    } else if (position == 6) {
                        vm_fullReport.month = getString(R.string.digit06)
                    } else if (position == 9) {
                        vm_fullReport.month = getString(R.string.digit09)
                    }
                    if (position == 11) {
                        vm_fullReport.month = getString(R.string.digit11)
                    }
                    initDay30Spinner()
                } else if (position == 2) {
                    vm_fullReport.month = getString(R.string.digit02)
                    initDay28Spinner()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                vm_fullReport.month = Constants.MONTH_All
                vm_fullReport.day = Constants.DAY_ALL
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

                vm_fullReport.category = if(position==0){
                    Log.d("FullReport","position -> $position:: item -> ${expenseCards[position]} :: size -> ${expenseCards.size} ::list is -> $expenseCards")
                    Constants.CATEGORY_All

                }
                else{
                    Log.d("FullReport","position -> $position:: item -> ${expenseCards[position-1]} :: size -> ${expenseCards.size} ::list is -> $expenseCards")
                    expenseCards[(position-1)]
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                vm_fullReport.category = Constants.CATEGORY_All
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
                    vm_fullReport.day = Constants.DAY_ALL
                } else if (position == 1) {
                    vm_fullReport.day = getString(R.string.digit01)
                } else if (position == 2) {
                    vm_fullReport.day = getString(R.string.digit02)
                } else if (position == 3) {
                    vm_fullReport.day = getString(R.string.digit03)
                } else if (position == 4) {
                    vm_fullReport.day = getString(R.string.digit04)
                } else if (position == 5) {
                    vm_fullReport.day = getString(R.string.digit05)
                } else if (position == 6) {
                    vm_fullReport.day = getString(R.string.digit06)
                } else if (position == 7) {
                    vm_fullReport.day = getString(R.string.digit07)
                } else if (position == 8) {
                    vm_fullReport.day = getString(R.string.digit08)
                } else if (position == 9) {
                    vm_fullReport.day = getString(R.string.digit09)
                } else if (position == 10) {
                    vm_fullReport.day = getString(R.string.digit10)
                } else if (position == 11) {
                    vm_fullReport.day = getString(R.string.digit11)
                } else if (position == 12) {
                    vm_fullReport.day = getString(R.string.digit12)
                } else if (position == 13) {
                    vm_fullReport.day = getString(R.string.digit13)
                } else if (position == 14) {
                    vm_fullReport.day = getString(R.string.digit14)
                } else if (position == 15) {
                    vm_fullReport.day = getString(R.string.digit15)
                } else if (position == 16) {
                    vm_fullReport.day = getString(R.string.digit16)
                } else if (position == 17) {
                    vm_fullReport.day = getString(R.string.digit17)
                } else if (position == 18) {
                    vm_fullReport.day = getString(R.string.digit18)
                } else if (position == 19) {
                    vm_fullReport.day = getString(R.string.digit19)
                } else if (position == 20) {
                    vm_fullReport.day = getString(R.string.digit20)
                } else if (position == 21) {
                    vm_fullReport.day = getString(R.string.digit21)
                } else if (position == 22) {
                    vm_fullReport.day = getString(R.string.digit22)
                } else if (position == 23) {
                    vm_fullReport.day = getString(R.string.digit23)
                } else if (position == 24) {
                    vm_fullReport.day = getString(R.string.digit24)
                } else if (position == 25) {
                    vm_fullReport.day = getString(R.string.digit25)
                } else if (position == 26) {
                    vm_fullReport.day = getString(R.string.digit26)
                } else if (position == 27) {
                    vm_fullReport.day = getString(R.string.digit27)
                } else if (position == 28) {
                    vm_fullReport.day = getString(R.string.digit28)
                } else if (position == 29) {
                    vm_fullReport.day = getString(R.string.digit29)
                } else if (position == 30) {
                    vm_fullReport.day = getString(R.string.digit30)
                } else if (position == 31) {
                    vm_fullReport.day = getString(R.string.digit31)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                vm_fullReport.day = Constants.DAY_ALL
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
                    vm_fullReport.day = Constants.DAY_ALL
                } else if (position == 1) {
                    vm_fullReport.day = getString(R.string.digit01)
                } else if (position == 2) {
                    vm_fullReport.day = getString(R.string.digit02)
                } else if (position == 3) {
                    vm_fullReport.day = getString(R.string.digit03)
                } else if (position == 4) {
                    vm_fullReport.day = getString(R.string.digit04)
                } else if (position == 5) {
                    vm_fullReport.day = getString(R.string.digit05)
                } else if (position == 6) {
                    vm_fullReport.day = getString(R.string.digit06)
                } else if (position == 7) {
                    vm_fullReport.day = getString(R.string.digit07)
                } else if (position == 8) {
                    vm_fullReport.day = getString(R.string.digit08)
                } else if (position == 9) {
                    vm_fullReport.day = getString(R.string.digit09)
                } else if (position == 10) {
                    vm_fullReport.day = getString(R.string.digit10)
                } else if (position == 11) {
                    vm_fullReport.day = getString(R.string.digit11)
                } else if (position == 12) {
                    vm_fullReport.day = getString(R.string.digit12)
                } else if (position == 13) {
                    vm_fullReport.day = getString(R.string.digit13)
                } else if (position == 14) {
                    vm_fullReport.day = getString(R.string.digit14)
                } else if (position == 15) {
                    vm_fullReport.day = getString(R.string.digit15)
                } else if (position == 16) {
                    vm_fullReport.day = getString(R.string.digit16)
                } else if (position == 17) {
                    vm_fullReport.day = getString(R.string.digit17)
                } else if (position == 18) {
                    vm_fullReport.day = getString(R.string.digit18)
                } else if (position == 19) {
                    vm_fullReport.day = getString(R.string.digit19)
                } else if (position == 20) {
                    vm_fullReport.day = getString(R.string.digit20)
                } else if (position == 21) {
                    vm_fullReport.day = getString(R.string.digit21)
                } else if (position == 22) {
                    vm_fullReport.day = getString(R.string.digit22)
                } else if (position == 23) {
                    vm_fullReport.day = getString(R.string.digit23)
                } else if (position == 24) {
                    vm_fullReport.day = getString(R.string.digit24)
                } else if (position == 25) {
                    vm_fullReport.day = getString(R.string.digit25)
                } else if (position == 26) {
                    vm_fullReport.day = getString(R.string.digit26)
                } else if (position == 27) {
                    vm_fullReport.day = getString(R.string.digit27)
                } else if (position == 28) {
                    vm_fullReport.day = getString(R.string.digit28)
                } else if (position == 29) {
                    vm_fullReport.day = getString(R.string.digit29)
                } else if (position == 30) {
                    vm_fullReport.day = getString(R.string.digit30)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                vm_fullReport.day = Constants.DAY_ALL
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
                    vm_fullReport.day = Constants.TYPE_ALL
                } else if (position == 1) {
                    vm_fullReport.day = getString(R.string.digit01)
                } else if (position == 2) {
                    vm_fullReport.day = getString(R.string.digit02)
                } else if (position == 3) {
                    vm_fullReport.day = getString(R.string.digit03)
                } else if (position == 4) {
                    vm_fullReport.day = getString(R.string.digit04)
                } else if (position == 5) {
                    vm_fullReport.day = getString(R.string.digit05)
                } else if (position == 6) {
                    vm_fullReport.day = getString(R.string.digit06)
                } else if (position == 7) {
                    vm_fullReport.day = getString(R.string.digit07)
                } else if (position == 8) {
                    vm_fullReport.day = getString(R.string.digit08)
                } else if (position == 9) {
                    vm_fullReport.day = getString(R.string.digit09)
                } else if (position == 10) {
                    vm_fullReport.day = getString(R.string.digit10)
                } else if (position == 11) {
                    vm_fullReport.day = getString(R.string.digit11)
                } else if (position == 12) {
                    vm_fullReport.day = getString(R.string.digit12)
                } else if (position == 13) {
                    vm_fullReport.day = getString(R.string.digit13)
                } else if (position == 14) {
                    vm_fullReport.day = getString(R.string.digit14)
                } else if (position == 15) {
                    vm_fullReport.day = getString(R.string.digit15)
                } else if (position == 16) {
                    vm_fullReport.day = getString(R.string.digit16)
                } else if (position == 17) {
                    vm_fullReport.day = getString(R.string.digit17)
                } else if (position == 18) {
                    vm_fullReport.day = getString(R.string.digit18)
                } else if (position == 19) {
                    vm_fullReport.day = getString(R.string.digit19)
                } else if (position == 20) {
                    vm_fullReport.day = getString(R.string.digit20)
                } else if (position == 21) {
                    vm_fullReport.day = getString(R.string.digit21)
                } else if (position == 22) {
                    vm_fullReport.day = getString(R.string.digit22)
                } else if (position == 23) {
                    vm_fullReport.day = getString(R.string.digit23)
                } else if (position == 24) {
                    vm_fullReport.day = getString(R.string.digit24)
                } else if (position == 25) {
                    vm_fullReport.day = getString(R.string.digit25)
                } else if (position == 26) {
                    vm_fullReport.day = getString(R.string.digit26)
                } else if (position == 27) {
                    vm_fullReport.day = getString(R.string.digit27)
                } else if (position == 28) {
                    vm_fullReport.day = getString(R.string.digit28)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                vm_fullReport.day = Constants.DAY_ALL
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
                    vm_fullReport.year = Constants.YEAR_DEFAULT
                } else if (position == 1) {
                    vm_fullReport.year = "2022"
                } else if (position == 2) {
                    vm_fullReport.year = "2023"
                } else if (position == 3) {
                    vm_fullReport.year = "2024"
                } else if (position == 4) {
                    vm_fullReport.year = "2025"
                } else if (position == 5) {
                    vm_fullReport.year = "2026"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                vm_fullReport.year = Constants.YEAR_DEFAULT
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
        val mMonth = if(vm_fullReport.month!=Constants.MONTH_All){ "${vm_fullReport.month}" } else{ "" }
        val mDay = if(vm_fullReport.day!=Constants.DAY_ALL){ "${vm_fullReport.day}" } else{ "" }
        val mType = if(vm_fullReport.type!=Constants.TYPE_ALL){ "${vm_fullReport.type}" } else{ "" }
        val mCategory = if(vm_fullReport.category!=Constants.CATEGORY_All){ "${vm_fullReport.category}" } else{ "" }

        binding.tvTitleTransactionsFullReport.text = "Transactions : ${vm_fullReport.year}-${Util.getMonthNameFromMonthNumber(mMonth)}-$mDay-$mType-$mCategory "
        binding.tvTitleTop.text = "Report : ${vm_fullReport.year}-${Util.getMonthNameFromMonthNumber(mMonth)}"

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