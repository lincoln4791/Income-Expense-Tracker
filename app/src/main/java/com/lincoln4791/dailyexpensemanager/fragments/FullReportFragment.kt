package com.lincoln4791.dailyexpensemanager.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.lincoln4791.dailyexpensemanager.Adapters.Adapter_FullReport
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.common.*
import com.lincoln4791.dailyexpensemanager.common.util.Util
import com.lincoln4791.dailyexpensemanager.common.util.GlobalVariabls
import com.lincoln4791.dailyexpensemanager.databinding.FragmentFullReportBinding
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import com.lincoln4791.dailyexpensemanager.viewModels.VM_FullReport

class FullReportFragment : Fragment() {
    private val linearLayoutManager = LinearLayoutManager(context)
    private var adapterFullReport: Adapter_FullReport? = null

    private lateinit var vm_fullReport: VM_FullReport
    private lateinit var binding : FragmentFullReportBinding
    private lateinit var navCon : NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFullReportBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Util.recordScreenEvent("fullReport_fragment","MainActivity")

        navCon = Navigation.findNavController(view)
        vm_fullReport = ViewModelProvider(this)[VM_FullReport::class.java]

        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true

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
        binding.cvSearchFullReport.setOnClickListener({ selectQuery() })

        binding.cvImg.setOnClickListener(View.OnClickListener { v: View? ->
          navCon.navigateUp()
        })

        binding.tvCurrentBalanceValueToolBarFullReport.setText(GlobalVariabls.currentBalance.toString())


        vm_fullReport.postsList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                is Resource.Success ->  calculateAll(it.data)
                is Resource.Error -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        })

        initMonthSpinner()
        initYearSpinner()
        initTypeSpinner()

    }


    private fun selectQuery() {
        if (vm_fullReport.month != Constants.MONTH_NULL &&
            vm_fullReport.day != Constants.DAY_NULL && vm_fullReport.type != Constants.TYPE_ALL &&
            vm_fullReport.category != Constants.CATEGORY_NULL
        ) {
            Log.d("Tag",
                "1)  year" + vm_fullReport.year + "month" + vm_fullReport.month + "day" + vm_fullReport.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            vm_fullReport.loadYearMonthDayTypeCategoryWise(vm_fullReport.year,vm_fullReport.month
                ,vm_fullReport.day,vm_fullReport.type,vm_fullReport.category)
        } else if (vm_fullReport.month != Constants.MONTH_NULL &&
            vm_fullReport.type != Constants.TYPE_ALL &&
            vm_fullReport.category != Constants.CATEGORY_NULL && vm_fullReport.day == Constants.DAY_NULL
        ) {
            Log.d("Tag",
                "2)  year" + vm_fullReport.year + "month" + vm_fullReport.month + "day" + vm_fullReport.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            vm_fullReport.loadYearMonthTypeCategoryWise(
                vm_fullReport.year,vm_fullReport.month,vm_fullReport.type,vm_fullReport.category
            )
        } else if (vm_fullReport.month != Constants.MONTH_NULL &&
            vm_fullReport.day != Constants.DAY_NULL &&
            vm_fullReport.category != Constants.CATEGORY_NULL && vm_fullReport.type == Constants.TYPE_ALL
        ) {
            val d = Log.d("Tag",
                "3)  year" + vm_fullReport.year + "month" + vm_fullReport.month + "day" + vm_fullReport.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            vm_fullReport.loadYearMonthDayCategoryWise(
                vm_fullReport.year,vm_fullReport.month,vm_fullReport.day,vm_fullReport.category
            )
        } else if (vm_fullReport.month != Constants.MONTH_NULL &&
            vm_fullReport.day != Constants.DAY_NULL &&
            vm_fullReport.type != Constants.TYPE_ALL && vm_fullReport.category == Constants.CATEGORY_NULL
        ) {
            Log.d("Tag",
                "4)  year" + vm_fullReport.year + "month" + vm_fullReport.month + "day" + vm_fullReport.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            vm_fullReport.loadYearMonthDayTypeWise(
                vm_fullReport.year,vm_fullReport.month,vm_fullReport.day,vm_fullReport.type
            )
        } else if (vm_fullReport.month != Constants.MONTH_NULL &&
            vm_fullReport.day != Constants.DAY_NULL && vm_fullReport.type == Constants.TYPE_ALL && vm_fullReport.category == Constants.CATEGORY_NULL
        ) {
            Log.d("Tag",
                "5)  year" + vm_fullReport.year + "month" + vm_fullReport.month + "day" + vm_fullReport.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            vm_fullReport.loadYearMonthDayWise(
                vm_fullReport.year,vm_fullReport.month,vm_fullReport.day
            )
        } else if (vm_fullReport.month != Constants.MONTH_NULL &&
            vm_fullReport.type != Constants.TYPE_ALL && vm_fullReport.day == Constants.DAY_NULL && vm_fullReport.category == Constants.CATEGORY_NULL
        ) {
            Log.d("Tag",
                "6)  year" + vm_fullReport.year + "month" + vm_fullReport.month + "day" + vm_fullReport.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            vm_fullReport.loadYearMonthTypeWise(
                vm_fullReport.year,vm_fullReport.month,vm_fullReport.type
            )
        } else if (vm_fullReport.month != Constants.MONTH_NULL &&
            vm_fullReport.category != Constants.CATEGORY_NULL && vm_fullReport.day == Constants.DAY_NULL && vm_fullReport!!.type == Constants.TYPE_ALL
        ) {
            Log.d("Tag",
                "7)  year" + vm_fullReport.year + "month" + vm_fullReport.month + "day" + vm_fullReport.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            vm_fullReport.loadYearMonthCategoryWise(
                vm_fullReport.year,vm_fullReport.month,vm_fullReport.category
            )
        } else if (vm_fullReport.month != Constants.MONTH_NULL && vm_fullReport.day == Constants.DAY_NULL && vm_fullReport!!.type == Constants.TYPE_ALL && vm_fullReport!!.category == Constants.CATEGORY_NULL) {
            Log.d("Tag",
                "8)  year" + vm_fullReport.year + "month" + vm_fullReport.month + "day" + vm_fullReport.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            vm_fullReport.loadYearMonthWise(
                vm_fullReport.year,vm_fullReport.month
            )
        } else if (vm_fullReport.type != Constants.TYPE_ALL && vm_fullReport.month == Constants.MONTH_NULL && vm_fullReport!!.day == Constants.DAY_NULL && vm_fullReport!!.category == Constants.CATEGORY_NULL) {
            Log.d("Tag",
                "9)  year" + vm_fullReport.year + "month" + vm_fullReport.month + "day" + vm_fullReport.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            vm_fullReport.loadYearTypeWise(vm_fullReport.year,vm_fullReport.type)
        } else if (vm_fullReport.type != Constants.TYPE_ALL && vm_fullReport.category != Constants.CATEGORY_NULL && vm_fullReport!!.day == Constants.DAY_NULL && vm_fullReport!!.month == Constants.MONTH_NULL) {
            Log.d("Tag",
                "12)  year" + vm_fullReport.year + "month" + vm_fullReport.month + "day" + vm_fullReport.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            vm_fullReport.loadYearTypeCategoryWise(
                vm_fullReport.year,vm_fullReport.type,vm_fullReport.category
            )
        } else if (vm_fullReport.category != Constants.CATEGORY_NULL && vm_fullReport.month == Constants.MONTH_NULL && vm_fullReport!!.day == Constants.DAY_NULL && vm_fullReport!!.type == Constants.TYPE_ALL) {
            Log.d("Tag",
                "10)  year" + vm_fullReport.year + "month" + vm_fullReport.month + "day" + vm_fullReport.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            vm_fullReport.loadYearMonthWise(
                vm_fullReport.year,vm_fullReport.month
            )
        } else {
            Log.d("Tag",
                "11)  year" + vm_fullReport.year + "month" + vm_fullReport.month + "day" + vm_fullReport.day + " type " + vm_fullReport!!.type + " category " + vm_fullReport!!.category)
            vm_fullReport.loadYearWise(vm_fullReport.year)
        }
    }

    private fun initTypeSpinner() {
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(),
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
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(),
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
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.nullCategory))
        binding.spinnerCatagoryFullReport.setAdapter(spinnerAdapter)
    }

    private fun initExpenseCategorySpinner() {
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(),
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
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(),
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
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.dayNull))
        binding.spinnerDayFullReport.adapter = spinnerAdapter
    }

    private fun initDay31Spinner() {
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(),
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
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(),
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
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(),
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
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(),
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


    private fun calculateAll(postsList:List<MC_Posts>) {
        resetPreviousCalculatedValues()
        adapterFullReport = Adapter_FullReport(requireContext(), postsList)
        binding.rvReportDetailsFullReport.adapter=adapterFullReport
        adapterFullReport!!.notifyDataSetChanged()
        for (i in postsList.indices) {
            if (postsList[i].postType == Constants.TYPE_INCOME) {
                if (postsList[i].postCategory == Constants.CATEGORY_SALARY) {
                    vm_fullReport.income_salary =
                        vm_fullReport.income_salary + postsList[i].postAmount
                            .toInt()
                } else if (postsList[i].postCategory == Constants.CATEGORY_BUSINESS) {
                    vm_fullReport.income_business =
                        vm_fullReport.income_business + postsList[i].postAmount
                            .toInt()
                } else if (postsList[i].postCategory == Constants.CATEGORY_HOUSE_RENT) {
                    vm_fullReport!!.income_house_rent =
                        vm_fullReport!!.income_house_rent + postsList[i].postAmount
                            .toInt()
                } else if (postsList[i].postCategory == Constants.CATEGORY_OTHER) {
                    vm_fullReport!!.income_other =
                        vm_fullReport!!.income_other + postsList[i].postAmount
                            .toInt()
                }
                vm_fullReport!!.total_income =
                    vm_fullReport!!.total_income + postsList[i].postAmount
                        .toInt()
            } else {
                if (postsList[i].postCategory == Constants.CATEGORY_FOOD) {
                    vm_fullReport!!.expense_food =
                        vm_fullReport!!.expense_food + postsList[i].postAmount
                            .toInt()
                } else if (postsList[i].postCategory == Constants.CATEGORY_TRANSPORT) {
                    vm_fullReport!!.expense_transport =
                        vm_fullReport!!.expense_transport +postsList[i].postAmount
                            .toInt()
                } else if (postsList[i].postCategory == Constants.CATEGORY_BILLS) {
                    vm_fullReport!!.expense_bills =
                        vm_fullReport!!.expense_bills + postsList[i].postAmount
                            .toInt()
                } else if (postsList[i].postCategory == Constants.CATEGORY_HOUSE_RENT) {
                    vm_fullReport!!.expense_houseRent =
                        vm_fullReport.expense_houseRent + postsList[i].postAmount
                            .toInt()
                } else if (postsList[i].postCategory == Constants.CATEGORY_BUSINESS) {
                    vm_fullReport!!.expense_business =
                        vm_fullReport.expense_business + postsList[i].postAmount
                            .toInt()
                } else if (postsList[i].postCategory == Constants.CATEGORY_MEDICINE) {
                    vm_fullReport!!.expense_medicine =
                        vm_fullReport!!.expense_medicine + postsList[i].postAmount
                            .toInt()
                } else if (postsList[i].postCategory == Constants.CATEGORY_CLOTHS) {
                    vm_fullReport!!.expense_cloths =
                        vm_fullReport!!.expense_cloths + postsList[i].postAmount
                            .toInt()
                } else if (postsList[i].postCategory == Constants.CATEGORY_EDUCATION) {
                    vm_fullReport!!.expense_education =
                        vm_fullReport!!.expense_education + postsList[i].postAmount
                            .toInt()
                } else if (postsList[i].postCategory == Constants.CATEGORY_LIFESTYLE) {
                    vm_fullReport!!.expense_lifestyle =
                        vm_fullReport!!.expense_lifestyle + postsList[i].postAmount
                            .toInt()
                } else if (postsList[i].postCategory == Constants.CATEGORY_OTHER) {
                    vm_fullReport!!.expense_other =
                        vm_fullReport!!.expense_other + postsList[i].postAmount
                            .toInt()
                }
                vm_fullReport!!.total_expense =
                    vm_fullReport!!.total_expense + postsList[i].postAmount
                        .toInt()
            }
        }

        setCalculatedValue()

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
    }

}