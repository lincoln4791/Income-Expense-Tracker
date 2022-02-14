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
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.lincoln4791.dailyexpensemanager.Adapters.Adapter_MonthlyReportExpense
import com.lincoln4791.dailyexpensemanager.Adapters.Adapter_MonthlyReportIncome
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.calll
import com.lincoln4791.dailyexpensemanager.common.*
import com.lincoln4791.dailyexpensemanager.common.util.Util
import com.lincoln4791.dailyexpensemanager.common.util.GlobalVariabls
import com.lincoln4791.dailyexpensemanager.databinding.FragmentMonthlyBinding
import com.lincoln4791.dailyexpensemanager.model.MC_MonthlyReport
import com.lincoln4791.dailyexpensemanager.viewModels.VM_MonthlyReport
import java.text.DecimalFormat
import java.util.*

class MonthlyFragment : Fragment(),View.OnClickListener,calll {
    private val args: MonthlyFragmentArgs by navArgs()
    private var tIncome = 0.0
    private var tExpense = 0.0
    private var balance = 0.0
    private lateinit var month: String
    private lateinit var year: String
    private var currentMonthPosition = 0
    private var currentMonth = 0
    private var currentYear = 0


    private lateinit var viewModel: VM_MonthlyReport
    private lateinit var binding : FragmentMonthlyBinding
    private lateinit var navCon : NavController
    private lateinit var linearLayoutManager : LinearLayoutManager
    private lateinit var linearLayoutManager2 : LinearLayoutManager
    private lateinit var expenseAdapterExpense : Adapter_MonthlyReportExpense
    private lateinit var incomeAdapterIncome : Adapter_MonthlyReportIncome

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

        Util.recordScreenEvent("monthly_fragment","MainActivity")

        navCon = Navigation.findNavController(view)
        viewModel = ViewModelProvider(this)[VM_MonthlyReport::class.java]

        linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true

        linearLayoutManager2 = LinearLayoutManager(context)
        linearLayoutManager2.reverseLayout = true
        linearLayoutManager2.stackFromEnd = true

        binding.rvExpense.layoutManager = linearLayoutManager
        binding.rvIncome.layoutManager = linearLayoutManager2




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

        binding.tvCurrentBalanceValueToolBarMonthlyReport.setText(GlobalVariabls.currentBalance.toString())
        initMonthSpinner()
        initYearSpinner()

        observer()

        //viewModel.loadYearMonth(year,month)
        loadDatas()

        binding.tvCurrentBalanceValueToolBarMonthlyReport.text = GlobalVariabls.currentBalance.toString()


    }

    private fun loadDatas() {
        viewModel.loadYearMonthExpeneWiseByGroup(year,month,Constants.TYPE_EXPENSE)
        viewModel.loadYearMonthIncomeWiseByGroup(year,month,Constants.TYPE_INCOME)
        viewModel.loadYearMonthTypeTotalExpense(year,month,Constants.TYPE_EXPENSE)
        viewModel.loadYearMonthTypeTotalIncome(year,month,Constants.TYPE_INCOME)
    }

    private fun observer() {
        viewModel.expenseList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                //is Resource.Success -> calculateAll(it.data)
                is Resource.Success -> updateExpenseUI(it.data)
                is Resource.Error -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.incomeList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                //is Resource.Success -> calculateAll(it.data)
                is Resource.Success -> updateIncomeUI(it.data)
                is Resource.Error -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.totalExpense.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                //is Resource.Success -> calculateAll(it.data)
                is Resource.Success -> updateTotalExpenseUI(it.data)
                is Resource.Error -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.totalIncome.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                //is Resource.Success -> calculateAll(it.data)
                is Resource.Success -> updateTotalIncomeUI(it.data)
                is Resource.Error -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun goToPieChartActivity() {
        val action = MonthlyFragmentDirections.actionMonthlyFragmentToPieChartFragment(year,month)
        navCon.navigate(action)

    }

    override fun onClick(v: View) {
        if (v.id == R.id.cv_type_houseRentExpense_MonthlyReport) {
            val action =
                MonthlyFragmentDirections.actionMonthlyFragmentToMonthlyCategoryWiseFragment(year,
                    month,
                    Constants.TYPE_EXPENSE,
                    Constants.CATEGORY_HOUSE_RENT,Constants.FRAGMENT_MONTHLY,null)
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
            resources.getStringArray(R.array.monthFullNames_MonthlyReport))
        binding.spinnerMonthMonthlyReport.adapter = spinnerAdapter
        binding.spinnerMonthMonthlyReport.setSelection(currentMonthPosition)
        binding.spinnerMonthMonthlyReport.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long,
            ) {
                when (position) {
                    0 -> {
                        month = getString(R.string.digit01)
                        loadDatas()
                    }
                    1 -> {
                        month = getString(R.string.digit02)
                        loadDatas()
                    }
                    2 -> {
                        month = getString(R.string.digit03)
                        loadDatas()
                    }
                    3 -> {
                        month = getString(R.string.digit04)
                        loadDatas()
                    }
                    4 -> {
                        month = getString(R.string.digit05)
                        loadDatas()
                    }
                    5 -> {
                        month = getString(R.string.digit06)
                        loadDatas()
                    }
                    6 -> {
                        month = getString(R.string.digit07)
                        loadDatas()
                    }
                    7 -> {
                        month = getString(R.string.digit08)
                        loadDatas()
                    }
                    8 -> {
                        month = getString(R.string.digit09)
                        loadDatas()
                    }
                    9 -> {
                        month = getString(R.string.digit10)
                        loadDatas()
                    }
                    10 -> {
                        month = getString(R.string.digit11)
                        loadDatas()
                    }
                    11 -> {
                        month = getString(R.string.digit12)
                        loadDatas()
                    }
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
                    loadDatas()
                }
                else if(position == 1){
                    //year = currentYear.toString()
                    year = "2022"
                    loadDatas()
                }
                else if(position==2) {
                    //year = (currentYear.toLong()+position).toString()
                    year = "2023"
                    loadDatas()
                }

                else if(position==3) {
                    //year = (currentYear.toLong()+position).toString()
                    year = "2024"
                    loadDatas()
                }

                else if(position==4) {
                    //year = (currentYear.toLong()+position).toString()
                    year = "2025"
                    loadDatas()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                val calendar = Calendar.getInstance()
                year = calendar[Calendar.YEAR].toString()
            }
        }
    }



    private fun updateExpenseUI(list : List<MC_MonthlyReport>){
        expenseAdapterExpense = Adapter_MonthlyReportExpense(list,this)
        binding.rvExpense.adapter = expenseAdapterExpense
        expenseAdapterExpense.notifyDataSetChanged()
    }

    private fun updateIncomeUI(list : List<MC_MonthlyReport>){
        incomeAdapterIncome = Adapter_MonthlyReportIncome(list,this)
        binding.rvIncome.adapter = incomeAdapterIncome
        incomeAdapterIncome.notifyDataSetChanged()
    }


    private fun updateTotalIncomeUI(totalIncome:Int?){
        binding.tvTitleIncomeMonthlyReport.text = "Incomes :        ${Util.getMonthNameFromMonthNumber(month)}-$year"
        tIncome = totalIncome?.toDouble() ?: 0.0
        binding.tvTotalIncomeBotMonthlyReport.text="${totalIncome?.toString()?:"0.0"} tk"
        updateMonthlyBalanceUI()

    }

    private fun updateTotalExpenseUI(totalExpense:Int?){
        binding.tvTitleExpensesMonthlyReport.text = "Expenses :      ${Util.getMonthNameFromMonthNumber(month)}-$year"
        tExpense = totalExpense?.toDouble() ?: 0.0
        binding.tvTotalExpenseBotMonthlyReport.text= "${totalExpense?.toString() ?: "0.0"} tk"
        updateMonthlyBalanceUI()
    }

    private fun updateMonthlyBalanceUI(){
        binding.tvTitleBalanceMonthlyReport.text = "Balance :      ${Util.getMonthNameFromMonthNumber(month)}-$year"
        binding.tvBalanceBotMonthlyReport.text="${(tIncome-tExpense).toString()} tk"
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

    fun navigateToDetails(type:String,category:String){
        val action = MonthlyFragmentDirections.actionMonthlyFragmentToMonthlyCategoryWiseFragment(year,month,type,category,Constants.FRAGMENT_MONTHLY,null)
        navCon.navigate(action)
    }


    private fun selectMonthDialog(){

    }

}