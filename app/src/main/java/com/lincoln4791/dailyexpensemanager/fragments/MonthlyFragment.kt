package com.lincoln4791.dailyexpensemanager.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.lincoln4791.dailyexpensemanager.base.BaseFragment
import com.google.android.gms.ads.MobileAds
import com.itmedicus.patientaid.ads.admobAdsUpdated.AdMobUtil
import com.itmedicus.patientaid.ads.admobAdsUpdated.BannerAddHelper
import com.lincoln4791.dailyexpensemanager.common.util.CurrentDate
import com.lincoln4791.dailyexpensemanager.Adapters.Adapter_MonthlyReportExpense
import com.lincoln4791.dailyexpensemanager.Adapters.Adapter_MonthlyReportIncome
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.calll
import com.lincoln4791.dailyexpensemanager.common.*
import com.lincoln4791.dailyexpensemanager.common.util.Util
import com.lincoln4791.dailyexpensemanager.common.util.GlobalVariabls
import com.lincoln4791.dailyexpensemanager.databinding.FragmentMonthlyBinding
import com.lincoln4791.dailyexpensemanager.model.MC_MonthlyReport
import com.lincoln4791.dailyexpensemanager.viewModels.VMMonthlyReport
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MonthlyFragment : BaseFragment<FragmentMonthlyBinding>(FragmentMonthlyBinding::inflate),View.OnClickListener,calll {
    @Inject lateinit var repository : Repository
    @Inject lateinit var prefManager : PrefManager
    @Inject lateinit var linearLayoutManager : LinearLayoutManager
    @Inject lateinit var linearLayoutManager2 : LinearLayoutManager
    private var isFirstTimeMonthSelectedCompleted = false
    private var isFirstTimeYearSelectedCompleted = false
    private val viewModel by viewModels<VMMonthlyReport>()
    private val args: MonthlyFragmentArgs by navArgs()
    private lateinit var month: String
    private lateinit var year: String

    private lateinit var navCon : NavController
    private lateinit var expenseAdapterExpense : Adapter_MonthlyReportExpense
    private lateinit var incomeAdapterIncome : Adapter_MonthlyReportIncome

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    Log.d("tag","OnBackPressCalled -> Monthly")
                    goBack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        unloadProgressBar()
        initAdMob()
        Util.recordScreenEvent("monthly_fragment","MainActivity")
        binding.shimmerViewContainer.startShimmer()
        navCon = Navigation.findNavController(view)
        initialization()

        binding.cvImg.setOnClickListener {
            goBack()
        }

        binding.tvCurrentBalanceValueToolBarMonthlyReport.text = GlobalVariabls.currentBalance.toString()
        initMonthSpinner()
        initYearSpinner()

        observer()
        loadData()

        binding.tvCurrentBalanceValueToolBarMonthlyReport.text = GlobalVariabls.currentBalance.toString()


    }

    private fun initialization() {
        binding.rvExpense.layoutManager = linearLayoutManager
        binding.rvIncome.layoutManager = linearLayoutManager2
        val calendar = Calendar.getInstance()
        viewModel.currentMonthPosition = calendar[Calendar.MONTH]
        viewModel.currentMonth = viewModel.currentMonthPosition + 1
        viewModel.currentYear = calendar[Calendar.YEAR]
        // year = currentYear.toString()

        year=args.year?:viewModel.currentYear.toString()
        viewModel.currentMonth= args.month?.toInt() ?:viewModel.currentMonth
        viewModel.currentMonthPosition=viewModel.currentMonth-1
        setMonth()
        Log.d("tag","year -> $year :arg year -> ${args.year}::: month -> $month :arg month->${args.month}")
    }

    private fun loadData() {
        Log.d("date","$year/$month")
        CoroutineScope(Dispatchers.IO).launch {
            delay(350)
            launch {
                viewModel.loadYearMonthIncomeWiseByGroup(year,month,Constants.TYPE_INCOME)
            }

            launch {
                viewModel.loadYearMonthExpeneWiseByGroup(year,month,Constants.TYPE_EXPENSE)
            }

            launch {
                viewModel.loadYearMonthTypeTotalIncome(year,month,Constants.TYPE_INCOME)
            }

            launch {
                viewModel.loadYearMonthTypeTotalExpense(year,month,Constants.TYPE_EXPENSE)
            }

        }

    }

    private fun observer() {
        viewModel.expenseList.observe(viewLifecycleOwner) {
            @Suppress("UNCHECKED_CAST")
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                //is Resource.Success -> calculateAll(it.data)
                is Resource.Success<*> -> updateExpenseUI(it.value as List<MC_MonthlyReport>)
                //is Resource.Failure -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                else -> {}
            }
        }

        viewModel.incomeList.observe(viewLifecycleOwner) {
            @Suppress("UNCHECKED_CAST")
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                is Resource.Success<*> -> updateIncomeUI(it.value as List<MC_MonthlyReport>)
                else -> {}
            }
        }

        viewModel.totalExpense.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                is Resource.Success<*> -> updateTotalExpenseUI(it.value as Int?)
                else -> {}
            }
        }

        viewModel.totalIncome.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                is Resource.Success<*> -> { updateTotalIncomeUI(it.value as Int?) }
                else -> {}
            }
        }

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
        month = when (viewModel.currentMonth) {
            1 -> {
                "01"
            }
            2 -> {
                "02"
            }
            3 -> {
                "03"
            }
            4 -> {
                "04"
            }
            5 -> {
                "05"
            }
            6 -> {
                "06"
            }
            7 -> {
                "07"
            }
            8 -> {
                "08"
            }
            9 -> {
                "09"
            }
            else -> {
                viewModel.currentMonth.toString()
            }
        }
    }

    private fun initMonthSpinner() {
        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.monthFullNames_MonthlyReport))
        binding.spinnerMonthMonthlyReport.adapter = spinnerAdapter
        binding.spinnerMonthMonthlyReport.setSelection(viewModel.currentMonthPosition)
        binding.spinnerMonthMonthlyReport.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long,
            ) {
                Log.d("tag","monthly spinner onItemSelectedCalled")
                when (position) {
                    0 -> {
                        month = getString(R.string.digit01)
                    }
                    1 -> {
                        month = getString(R.string.digit02)
                    }
                    2 -> {
                        month = getString(R.string.digit03)
                    }
                    3 -> {
                        month = getString(R.string.digit04)
                    }
                    4 -> {
                        month = getString(R.string.digit05)
                    }
                    5 -> {
                        month = getString(R.string.digit06)
                    }
                    6 -> {
                        month = getString(R.string.digit07)
                    }
                    7 -> {
                        month = getString(R.string.digit08)
                    }
                    8 -> {
                        month = getString(R.string.digit09)
                    }
                    9 -> {
                        month = getString(R.string.digit10)
                    }
                    10 -> {
                        month = getString(R.string.digit11)
                    }
                    11 -> {
                        month = getString(R.string.digit12)
                    }
                }
                if(isFirstTimeMonthSelectedCompleted){
                    loadData()
                }
                else{
                    isFirstTimeMonthSelectedCompleted=true
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                val calendar = Calendar.getInstance()
                month = calendar[Calendar.MONTH].toString()
            }
        }
    }

    private fun initYearSpinner() {
        val spinnerArray = arrayListOf("2021","2022","2023","2024","2025")
        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            spinnerArray)
        val spinnerIndex: Int
        when (year) {
            "2021" -> {
                spinnerIndex = 0
            }
            "2022" -> {
                spinnerIndex = 1
            }
            "2023" -> {
                spinnerIndex = 2
            }
            "2024" -> {
                spinnerIndex=4
            }
            else -> {
                spinnerIndex=5
            }
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
                year = viewModel.currentYear.toString()
                Log.d("tag","year spinner onItemSelectedCalled")
                when (position) {
                    0 -> {
                        year = "2021"
                    }
                    1 -> {
                        //year = currentYear.toString()
                        year = "2022"
                    }
                    2 -> {
                        //year = (currentYear.toLong()+position).toString()
                        year = "2023"
                    }
                    3 -> {
                        //year = (currentYear.toLong()+position).toString()
                        year = "2024"
                    }
                    4 -> {
                        //year = (currentYear.toLong()+position).toString()
                        year = "2025"
                    }

                }
                if (isFirstTimeYearSelectedCompleted){
                    loadData()
                }
                else{
                    isFirstTimeYearSelectedCompleted=true
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                val calendar = Calendar.getInstance()
                year = calendar[Calendar.YEAR].toString()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateExpenseUI(list : List<MC_MonthlyReport>){
        if(list.isEmpty()){
            binding.cvNoResultFoundExpenses.visibility = View.VISIBLE
        }
        else{
            binding.cvNoResultFoundExpenses.visibility = View.GONE
        }
        expenseAdapterExpense = Adapter_MonthlyReportExpense(list,requireContext(),this)
        binding.rvExpense.adapter = expenseAdapterExpense
        expenseAdapterExpense.notifyDataSetChanged()


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateIncomeUI(list : List<MC_MonthlyReport>){

        if(list.isEmpty()){
            binding.cvNoResultFoundIncomes.visibility = View.VISIBLE
        }
        else{
            binding.cvNoResultFoundIncomes.visibility = View.GONE
        }

        incomeAdapterIncome = Adapter_MonthlyReportIncome(list,requireContext(),this)
        binding.rvIncome.adapter = incomeAdapterIncome
        incomeAdapterIncome.notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    private fun updateTotalIncomeUI(totalIncome:Int?){
        binding.tvTitleIncomeMonthlyReport.text = "Incomes :        ${Util.getMonthNameFromMonthNumber(month)}-$year"
        viewModel.tIncome = totalIncome?.toDouble() ?: 0.0
        binding.tvTotalIncomeBotMonthlyReport.text="${totalIncome?.toString()?:"0.0"} tk"
        updateMonthlyBalanceUI()
    }

    @SuppressLint("SetTextI18n")
    private fun updateTotalExpenseUI(totalExpense:Int?){
        binding.tvTitleExpensesMonthlyReport.text = "Expenses :      ${Util.getMonthNameFromMonthNumber(month)}-$year"
        viewModel.tExpense = totalExpense?.toDouble() ?: 0.0
        binding.tvTotalExpenseBotMonthlyReport.text= "${totalExpense?.toString() ?: "0.0"} tk"
        updateMonthlyBalanceUI()
    }

    @SuppressLint("SetTextI18n")
    private fun updateMonthlyBalanceUI(){
        binding.tvTitleBalanceMonthlyReport.text = "Balance :      ${Util.getMonthNameFromMonthNumber(month)}-$year"
        binding.tvBalanceBotMonthlyReport.text="${(viewModel.tIncome-viewModel.tExpense)} tk"
        binding.shimmerViewContainer.stopShimmer()
        binding.shimmerViewContainer.visibility = View.GONE
        binding.clContentMain.visibility = View.VISIBLE
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


    private fun initAdMob() {
        val lastAdShowDate = prefManager.lastBannerAdShownMonthlyF
        if (AdMobUtil.canAdShow(requireContext(), lastAdShowDate)) {
            binding.adView.visibility = View.VISIBLE
            MobileAds.initialize(requireContext()) {
                val bannerAdHelper = BannerAddHelper(requireContext())
                bannerAdHelper.loadBannerAd(binding.adView) {
                    if (it) {
                        prefManager.lastBannerAdShownMonthlyF = CurrentDate.currentTime24H
                    }
                }
            }
        } else {
            binding.adView.visibility = View.GONE
        }
    }


    private fun loadProgressBar() {
        binding.mainLoadingBar.visibility = View.VISIBLE
        binding.clContainer.visibility=View.GONE
    }

    private fun unloadProgressBar(){
        binding.mainLoadingBar.visibility = View.GONE
        binding.clContainer.visibility=View.VISIBLE
    }


}