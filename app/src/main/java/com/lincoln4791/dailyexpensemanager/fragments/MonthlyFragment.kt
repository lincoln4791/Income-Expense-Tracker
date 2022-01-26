package com.lincoln4791.dailyexpensemanager.fragments

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.calll
import com.lincoln4791.dailyexpensemanager.common.*
import com.lincoln4791.dailyexpensemanager.databinding.AddExpenseFragmentBinding
import com.lincoln4791.dailyexpensemanager.databinding.FragmentMonthlyBinding
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import com.lincoln4791.dailyexpensemanager.view.*
import com.lincoln4791.dailyexpensemanager.viewModels.VM_AddExpenses
import com.lincoln4791.dailyexpensemanager.viewModels.VM_MainActivity
import com.lincoln4791.dailyexpensemanager.viewModels.VM_MonthlyReport
import java.text.DecimalFormat
import java.util.*

class MonthlyFragment : Fragment(),View.OnClickListener,calll {
    private val args: MonthlyFragmentArgs by navArgs()
    private var totalIncome = 0.0
    private var totalExpense = 0.0
    private var balance = 0.0
    private var transportExpense = 0.0
    private var foodExpense = 0.0
    private var billsExpense = 0.0
    private var houseRentExpense = 0.0
    private var businessExpense = 0.0
    private var medicineExpense = 0.0
    private var clothsExpense = 0.0
    private var educationExpense = 0.0
    private var lifeStyleExpense = 0.0
    private var otherExpense = 0.0
    private var salaryIncome = 0.0
    private var businessIncome = 0.0
    private var houseRentIncome = 0.0
    private var otherIncome = 0.0
    private lateinit var foodExpensePercent: String
    private lateinit var transportExpensePercent: String
    private lateinit var billsExpensePercent: String
    private lateinit var businessExpensePercent: String
    private lateinit var houseRentExpensePercent: String
    private lateinit var medicineExpensePercent: String
    private lateinit var clothsExpensePercent: String
    private lateinit var educationExpensePercent: String
    private lateinit var lifeStyleExpensePercent: String
    private lateinit var otherExpensePercent: String
    private lateinit var salaryIncomePercent: String
    private lateinit var businessIncomePercent: String
    private lateinit var houseRentIncomePercent: String
    private lateinit var otherIncomePercent: String
    private lateinit var month: String
    private lateinit var year: String
    private var currentMonthPosition = 0
    private var currentMonth = 0
    private var currentYear = 0


    private lateinit var viewModel: VM_MonthlyReport
    private lateinit var binding : FragmentMonthlyBinding
    private lateinit var navCon : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    Log.d("tag","OnBackPressCalled -> Monthly")
                    //navCon.navigateUp()
                    goBack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMonthlyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navCon = Navigation.findNavController(view)
        viewModel = ViewModelProvider(this)[VM_MonthlyReport::class.java]



        val calendar = Calendar.getInstance()
        currentMonthPosition = calendar[Calendar.MONTH]
        currentMonth = currentMonthPosition + 1
        currentYear = calendar[Calendar.YEAR]
       // year = currentYear.toString()

        year=args.year?:currentYear.toString()
        currentMonth= args.month?.toInt() ?:currentMonth
        currentMonthPosition=currentMonth-1
        setMonth()
        Log.d("tag","year -> $year :argyear -> ${args.year}::: month -> $month :argmonth->${args.month}")

        binding.cvSearchMonthlyReport.setOnClickListener {
            Log.d("tag", "Year -> $year ::: Month -> $month")
            viewModel.loadYearMonth(year, month)
        }
        binding.cvDailyMonthlyReport.setOnClickListener(View.OnClickListener { v: View? ->
           /* startActivity(Intent(this@MonthlyReport,
                Daily::class.java))*/
        })
        binding.cvFullReportMonthlyReport.setOnClickListener(View.OnClickListener { v: View? ->
            /*startActivity(Intent(this@MonthlyReport,
                FullReport::class.java))*/
        })
        binding.cvTransactionsMonthlyReport.setOnClickListener(View.OnClickListener { v: View? ->
           /* val intent = Intent(this@MonthlyReport, Transactions::class.java)
            intent.putExtra(Extras.TYPE, Constants.TYPE_ALL)
            startActivity(intent)*/
        })


        binding.cvImg.setOnClickListener {
            goBack()
        }

        binding.cvPieChartMonthlyReport.setOnClickListener(View.OnClickListener { v: View? -> goToPieChartActivity() })
        binding.cvTypeSalaryIncomeMonthlyReport.setOnClickListener(this)
        binding.cvTypeBusinessIncomeMonthlyReport.setOnClickListener(this)
        binding.cvTypeHouseRentIncomeMonthlyReport.setOnClickListener(this)
        binding.cvTypeOtherIncomeMonthlyReport.setOnClickListener(this)
        binding.cvTypeFoodExpenseMonthlyReport.setOnClickListener(this)
        binding.cvTypeTransportExpenseMonthlyReport.setOnClickListener(this)
        binding.cvTypeBusinessExpenseMonthlyReport.setOnClickListener(this)
        binding.cvTypeHouseRentExpenseMonthlyReport.setOnClickListener(this)
        binding.cvTypeBillsExpenseMonthlyReport.setOnClickListener(this)
        binding.cvTypeClothsExpenseMonthlyReport.setOnClickListener(this)
        binding.cvTypeMedicineExpenseMonthlyReport.setOnClickListener(this)
        binding.cvTypeEducationExpenseMonthlyReport.setOnClickListener(this)
        binding.cvTypeLifeStyleExpenseMonthlyReport.setOnClickListener(this)
        binding.cvTypeOtherExpenseMonthlyReport.setOnClickListener(this)

        /*binding.ivHomeToolbarMonthlyReport.setOnClickListener {
           *//* startActivity(Intent(this@MonthlyReport,
                MainActivity::class.java))*//*
        }*/


        binding.tvCurrentBalanceValueToolBarMonthlyReport.setText(UtilDB.currentBalance.toString())
        initMonthSpinner()
        initYearSpinner()

        viewModel.postsList.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                is Resource.Success ->  calculateAll(it.data)
                is Resource.Error -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        })

        viewModel.loadYearMonth(year,month)

    }

    private fun goToPieChartActivity() {
        val action = MonthlyFragmentDirections.actionMonthlyFragmentToPieChartFragment(year,month)
        navCon.navigate(action)

    }

    override fun onClick(v: View) {
        if (v.id == R.id.cv_type_salaryIncome_MonthlyReport) {
            val action = MonthlyFragmentDirections.actionMonthlyFragmentToMonthlyCategoryWiseFragment(year,month,Constants.TYPE_INCOME,Constants.CATEGORY_SALARY)
            navCon.navigate(action)
        } else if (v.id == R.id.cv_type_businessIncome_MonthlyReport) {
            val action = MonthlyFragmentDirections.actionMonthlyFragmentToMonthlyCategoryWiseFragment(year,month,Constants.TYPE_INCOME,Constants.CATEGORY_BUSINESS)
            navCon.navigate(action)
        } else if (v.id == R.id.cv_type_houseRentIncome_MonthlyReport) {
            val action = MonthlyFragmentDirections.actionMonthlyFragmentToMonthlyCategoryWiseFragment(year,month,Constants.TYPE_INCOME,Constants.CATEGORY_HOUSE_RENT)
            navCon.navigate(action)
        } else if (v.id == R.id.cv_type_otherIncome_MonthlyReport) {
            val action = MonthlyFragmentDirections.actionMonthlyFragmentToMonthlyCategoryWiseFragment(year,month,Constants.TYPE_INCOME,Constants.CATEGORY_OTHER)
            navCon.navigate(action)
        } else if (v.id == R.id.cv_type_foodExpense_MonthlyReport) {
            val action = MonthlyFragmentDirections.actionMonthlyFragmentToMonthlyCategoryWiseFragment(year,month,Constants.TYPE_EXPENSE,Constants.CATEGORY_FOOD)
            navCon.navigate(action)
        } else if (v.id == R.id.cv_type_transportExpense_MonthlyReport) {
            val action = MonthlyFragmentDirections.actionMonthlyFragmentToMonthlyCategoryWiseFragment(year,month,Constants.TYPE_EXPENSE,Constants.CATEGORY_TRANSPORT)
            navCon.navigate(action)
        } else if (v.id == R.id.cv_type_businessExpense_MonthlyReport) {
            val action = MonthlyFragmentDirections.actionMonthlyFragmentToMonthlyCategoryWiseFragment(year,month,Constants.TYPE_EXPENSE,Constants.CATEGORY_BUSINESS)
            navCon.navigate(action)
        } else if (v.id == R.id.cv_type_houseRentExpense_MonthlyReport) {
            val action = MonthlyFragmentDirections.actionMonthlyFragmentToMonthlyCategoryWiseFragment(year,month,Constants.TYPE_EXPENSE,Constants.CATEGORY_HOUSE_RENT)
            navCon.navigate(action)
        } else if (v.id == R.id.cv_type_billsExpense_MonthlyReport) {
            val action = MonthlyFragmentDirections.actionMonthlyFragmentToMonthlyCategoryWiseFragment(year,month,Constants.TYPE_EXPENSE,Constants.CATEGORY_BILLS)
            navCon.navigate(action)
        } else if (v.id == R.id.cv_type_medicineExpense_MonthlyReport) {
            val action = MonthlyFragmentDirections.actionMonthlyFragmentToMonthlyCategoryWiseFragment(year,month,Constants.TYPE_EXPENSE,Constants.CATEGORY_MEDICINE)
            navCon.navigate(action)
        } else if (v.id == R.id.cv_type_clothsExpense_MonthlyReport) {
            val action = MonthlyFragmentDirections.actionMonthlyFragmentToMonthlyCategoryWiseFragment(year,month,Constants.TYPE_EXPENSE,Constants.CATEGORY_CLOTHS)
            navCon.navigate(action)
        } else if (v.id == R.id.cv_type_educationExpense_MonthlyReport) {
            val action = MonthlyFragmentDirections.actionMonthlyFragmentToMonthlyCategoryWiseFragment(year,month,Constants.TYPE_EXPENSE,Constants.CATEGORY_EDUCATION)
            navCon.navigate(action)
        } else if (v.id == R.id.cv_type_lifeStyleExpense_MonthlyReport) {
            val action = MonthlyFragmentDirections.actionMonthlyFragmentToMonthlyCategoryWiseFragment(year,month,Constants.TYPE_EXPENSE,Constants.CATEGORY_LIFESTYLE)
            navCon.navigate(action)
        } else if (v.id == R.id.cv_type_otherExpense_MonthlyReport) {
            val action = MonthlyFragmentDirections.actionMonthlyFragmentToMonthlyCategoryWiseFragment(year,month,Constants.TYPE_EXPENSE,Constants.CATEGORY_OTHER)
            navCon.navigate(action)
        }
    }

    private fun setMonth() {
        month = if (currentMonth == 1) {
            "01"
        } else if (currentMonth == 2) {
            "02"
        } else if (currentMonth == 3) {
            "03"
        } else if (currentMonth == 4) {
            "04"
        } else if (currentMonth == 5) {
            "05"
        } else if (currentMonth == 6) {
            "06"
        } else if (currentMonth == 7) {
            "07"
        } else if (currentMonth == 8) {
            "08"
        } else if (currentMonth == 9) {
            "09"
        } else {
            currentMonth.toString()
        }
    }

    private fun initMonthSpinner() {
        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.monthNames_MonthlyReport))
        binding.spinnerMonthMonthlyReport.adapter = spinnerAdapter
        binding.spinnerMonthMonthlyReport.setSelection(currentMonthPosition)
        binding.spinnerMonthMonthlyReport.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long,
            ) {
                if (position == 0) {
                    month = getString(R.string.digit01)
                } else if (position == 1) {
                    month = getString(R.string.digit02)
                } else if (position == 2) {
                    month = getString(R.string.digit03)
                } else if (position == 3) {
                    month = getString(R.string.digit04)
                } else if (position == 4) {
                    month = getString(R.string.digit05)
                } else if (position == 5) {
                    month = getString(R.string.digit06)
                } else if (position == 6) {
                    month = getString(R.string.digit07)
                } else if (position == 7) {
                    month = getString(R.string.digit08)
                } else if (position == 8) {
                    month = getString(R.string.digit09)
                } else if (position == 9) {
                    month = getString(R.string.digit10)
                } else if (position == 10) {
                    month = getString(R.string.digit11)
                } else if (position == 11) {
                    month = getString(R.string.digit12)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                val calendar = Calendar.getInstance()
                month = calendar[Calendar.MONTH].toString()
            }
        }
    }

    private fun initYearSpinner() {
        val spinnerArray = arrayListOf<String>("2021","2022","2023","2024","2025")
        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            spinnerArray)
        var spinnerIndex = 0
        if(year =="2021"){
            spinnerIndex = 0
        }
        else if(year =="2022"){
            spinnerIndex = 1
        }
        else if(year =="2023"){
            spinnerIndex = 2
        }
        else if(year == "2024"){
            spinnerIndex=4
        }
        else {
            spinnerIndex=5
        }
        binding.spinnerYearMonthlyReport.adapter = spinnerAdapter
        binding.spinnerYearMonthlyReport.setSelection(spinnerIndex)
        binding.spinnerYearMonthlyReport.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long,
            ) {
                year = currentYear.toString()
                if(position==0){
                    year = "2021"
                }
                else if(position == 1){
                    year = currentYear.toString()
                }
                else{
                    year = (currentYear.toLong()+position).toString()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                val calendar = Calendar.getInstance()
                year = calendar[Calendar.YEAR].toString()
            }
        }
    }


    private fun calculateAll(postsList:List<MC_Posts>) {
        resetPreviousCalculatedValues()
        Log.d("tag", "list size : " + postsList!!.size)
        for (i in postsList!!.indices) {
            if (postsList!![i].postType == Constants.TYPE_INCOME) {
                if (postsList!![i].postCategory == Constants.CATEGORY_SALARY) {
                    salaryIncome = salaryIncome + postsList!![i].postAmount.toInt()
                } else if (postsList!![i].postCategory == Constants.CATEGORY_BUSINESS) {
                    businessIncome = businessIncome + postsList!![i].postAmount.toInt()
                } else if (postsList!![i].postCategory == Constants.CATEGORY_HOUSE_RENT) {
                    houseRentIncome = houseRentIncome + postsList!![i].postAmount.toInt()
                } else if (postsList!![i].postCategory == Constants.CATEGORY_OTHER) {
                    otherIncome = otherIncome + postsList!![i].postAmount.toInt()
                }
                totalIncome = totalIncome + postsList!![i].postAmount.toInt()
            } else {
                if (postsList!![i].postCategory == Constants.CATEGORY_FOOD) {
                    foodExpense = foodExpense + postsList!![i].postAmount.toInt()
                } else if (postsList!![i].postCategory == Constants.CATEGORY_TRANSPORT) {
                    transportExpense = transportExpense + postsList!![i].postAmount.toInt()
                } else if (postsList!![i].postCategory == Constants.CATEGORY_BILLS) {
                    billsExpense = billsExpense + postsList!![i].postAmount.toInt()
                } else if (postsList!![i].postCategory == Constants.CATEGORY_HOUSE_RENT) {
                    houseRentExpense = houseRentExpense + postsList!![i].postAmount.toInt()
                } else if (postsList!![i].postCategory == Constants.CATEGORY_BUSINESS) {
                    businessExpense = businessExpense + postsList!![i].postAmount.toInt()
                } else if (postsList!![i].postCategory == Constants.CATEGORY_MEDICINE) {
                    medicineExpense = medicineExpense + postsList!![i].postAmount.toInt()
                } else if (postsList!![i].postCategory == Constants.CATEGORY_CLOTHS) {
                    clothsExpense = clothsExpense + postsList!![i].postAmount.toInt()
                } else if (postsList!![i].postCategory == Constants.CATEGORY_EDUCATION) {
                    educationExpense = educationExpense + postsList!![i].postAmount.toInt()
                } else if (postsList!![i].postCategory == Constants.CATEGORY_LIFESTYLE) {
                    lifeStyleExpense = lifeStyleExpense + postsList!![i].postAmount.toInt()
                } else if (postsList!![i].postCategory == Constants.CATEGORY_OTHER) {
                    otherExpense = otherExpense + postsList!![i].postAmount.toInt()
                }
                totalExpense = totalExpense + postsList!![i].postAmount.toInt()
            }
        }
        balance = totalIncome - totalExpense
        calculatePercent()
    }

    private fun resetPreviousCalculatedValues() {
        salaryIncome = 0.0
        businessIncome = 0.0
        houseRentIncome = 0.0
        otherIncome = 0.0
        totalIncome = 0.0
        foodExpense = 0.0
        transportExpense = 0.0
        billsExpense = 0.0
        houseRentExpense = 0.0
        businessExpense = 0.0
        medicineExpense = 0.0
        clothsExpense = 0.0
        educationExpense = 0.0
        lifeStyleExpense = 0.0
        otherExpense = 0.0
        totalExpense = 0.0
    }

    fun setCalculatedValue() {
        Log.d("tag", "setting value")
        binding.tvAmountSalaryIncomeMonthlyReport.text = salaryIncome.toString()
        binding.tvAmountBusinessIncomeMonthlyReport!!.text = businessIncome.toString()
        binding.tvAmountHouseRentIncomeMonthlyReport!!.text = houseRentIncome.toString()
        binding.tvAmountOtherIncomeMonthlyReport!!.text = otherIncome.toString()
        binding.tvTotalIncomeBotMonthlyReport!!.text = totalIncome.toString()
        binding.tvTotalIncomeValueTopBarMonthlyReport.text = totalIncome.toString()
        binding.tvAmountFoodMonthlyReport!!.text = foodExpense.toString()
        binding.tvAmountTransportMonthlyReport!!.text = transportExpense.toString()
        binding.tvAmountBusinessMonthlyReport!!.text = businessExpense.toString()
        binding.tvAmountBillsMonthlyReport!!.text = billsExpense.toString()
        binding.tvAmountHouseRentMonthlyReport!!.text = houseRentExpense.toString()
        binding.tvAmountMedicineMonthlyReport!!.text = medicineExpense.toString()
        binding.tvAmountClothsMonthlyReport!!.text = clothsExpense.toString()
        binding.tvAmountEducationMonthlyReport!!.text = educationExpense.toString()
        binding.tvAmountLifeStyleMonthlyReport!!.text = lifeStyleExpense.toString()
        binding.tvAmountOtherExpenseMonthlyReport!!.text = otherExpense.toString()
        binding.tvTotalExpenseBotMonthlyReport!!.text = totalExpense.toString()
        binding.tvTotalExpenseValueTopBarMonthlyReport!!.text = totalExpense.toString()
        binding.tvCategoryFoodPercentValueMonthlyReport!!.text = foodExpensePercent.toString()
        binding.tvCategoryTransportPercentValueMonthlyReport!!.text = transportExpensePercent.toString()
        binding.tvCategoryBusinessPercentValueMonthlyReport!!.text = businessExpensePercent.toString()
        binding.tvCategoryBillsPercentValueMonthlyReport!!.text = billsExpensePercent.toString()
        binding.tvCategoryHouseRentPercentValueMonthlyReport!!.text = houseRentExpensePercent.toString()
        binding.tvCategoryMedicinePercentValueMonthlyReport!!.text = medicineExpensePercent.toString()
        binding.tvCategoryClothsPercentValueMonthlyReport!!.text = clothsExpensePercent.toString()
        binding.tvCategoryEducationPercentValueMonthlyReport!!.text = educationExpensePercent.toString()
        binding.tvCategoryLifeStylePercentValueMonthlyReport!!.text = lifeStyleExpensePercent.toString()
        binding.tvCategoryOtherExpensePercentValueMonthlyReport!!.text = otherExpensePercent.toString()
        binding.tvCategorySalaryIncomePercentValueMonthlyReport!!.text = salaryIncomePercent.toString()
        binding.tvCategoryBusinessIncomePercentValueMonthlyReport!!.text = businessIncomePercent.toString()
        binding.tvCategoryHouseRentIncomePercentValueMonthlyReport!!.text = houseRentIncomePercent.toString()
        binding.tvCategoryOtherIncomePercentValueMonthlyReport!!.text = otherIncomePercent.toString()
        binding.tvBalanceBotMonthlyReport!!.text = balance.toString()
        binding.tvCurrentBalanceValueToolBarMonthlyReport!!.text = balance.toString()
    }

    private fun calculatePercent() {
        foodExpensePercent = df2.format(foodExpense / totalExpense * 100.0)
        businessExpensePercent = df2.format(businessExpense / totalExpense * 100)
        transportExpensePercent = df2.format(transportExpense / totalExpense * 100)
        billsExpensePercent = df2.format(billsExpense / totalExpense * 100)
        houseRentExpensePercent = df2.format(houseRentExpense / totalExpense * 100)
        medicineExpensePercent = df2.format(medicineExpense / totalExpense * 100)
        clothsExpensePercent = df2.format(clothsExpense / totalExpense * 100)
        educationExpensePercent = df2.format(educationExpense / totalExpense * 100)
        lifeStyleExpensePercent = df2.format(lifeStyleExpense / totalExpense * 100)
        otherExpensePercent = df2.format(otherExpense / totalExpense * 100)
        salaryIncomePercent = df2.format(salaryIncome / totalIncome * 100)
        businessIncomePercent = df2.format(businessIncome / totalIncome * 100)
        houseRentIncomePercent = df2.format(houseRentIncome / totalIncome * 100)
        otherIncomePercent = df2.format(otherIncome / totalIncome * 100)
        Log.d("tag",
            "food : " + foodExpense + " totat : " + totalExpense + "percent: " + foodExpensePercent)

        setCalculatedValue()
    }


    companion object{
         val df2 = DecimalFormat("#.##")
    }

    override fun callMe() {
        Log.d("tag","I have Called")
    }

    private fun goBack(){
        val action = MonthlyFragmentDirections.actionMonthlyFragmentToHomeFragment()
        navCon.navigate(action)
    }

}