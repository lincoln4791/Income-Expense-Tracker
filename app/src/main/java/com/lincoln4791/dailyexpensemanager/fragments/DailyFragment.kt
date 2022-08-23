package com.lincoln4791.dailyexpensemanager.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybaseproject2.base.BaseFragment
import com.google.android.gms.ads.MobileAds
import com.itmedicus.patientaid.ads.admobAdsUpdated.AdMobUtil
import com.itmedicus.patientaid.ads.admobAdsUpdated.BannerAddHelper
import com.lincoln4791.dailyexpensemanager.common.util.CurrentDate
import com.lincoln4791.dailyexpensemanager.Adapters.Adapter_Daily
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.common.*
import com.lincoln4791.dailyexpensemanager.common.util.DbAdapter
import com.lincoln4791.dailyexpensemanager.common.util.Util
import com.lincoln4791.dailyexpensemanager.common.util.GlobalVariabls
import com.lincoln4791.dailyexpensemanager.databinding.FragmentDailyBinding
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import com.lincoln4791.dailyexpensemanager.viewModels.VMDaily
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class DailyFragment : BaseFragment<FragmentDailyBinding>(FragmentDailyBinding::inflate) {
    @Inject lateinit var prefManager : PrefManager
    @Inject lateinit var repository : Repository

    private val viewModel by viewModels<VMDaily>()
    private lateinit var navCon : NavController

    private var adapterDaily: Adapter_Daily? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var tempMonth = 0
    private var tempYear = 0
    private var tempDay = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdMob()
        Util.recordScreenEvent("daily_fragment","MainActivity")

        navCon = Navigation.findNavController(view)


        linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager!!.reverseLayout = true
        linearLayoutManager!!.stackFromEnd = true
        binding.rvDailyReport.layoutManager = linearLayoutManager
        binding.rvDailyReport.adapter = adapterDaily
        val calendar = Calendar.getInstance()
        tempMonth = calendar[Calendar.MONTH]
        tempYear = calendar[Calendar.YEAR]
        tempDay = calendar[Calendar.DAY_OF_MONTH]



        binding.cvDateDailyReport.setOnClickListener { changeDateAndFetchNewData() }
        binding.ibNextDateDailyReport.setOnClickListener { loadNextDateData() }
        binding.ibPreviousDateDailyReport.setOnClickListener { loadPreviousDateData() }
        binding.cvImg.setOnClickListener(View.OnClickListener { v: View? ->
        navCon.navigateUp()
        })


        viewModel.setDate()
        binding.tvCurrentBalanceValueToolBarMonthlyReport.text = GlobalVariabls.currentBalance.toString()
        viewModel.loadDailyTransactions(viewModel.year.toString(),viewModel.month.toString(), viewModel.day.toString())

        observe()

    }

    private fun observe() {
        viewModel.postsList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                //is Resource.Success<*> ->  calculateAll(it.value as List<MC_Posts>)
                is Resource.Success<*> -> {
                    viewModel.calculateAll(it.value as List<MC_Posts>)
                    updateUI(it.value)
                }
                else -> {}
            }
        }


        viewModel.date.observe(viewLifecycleOwner){
            binding.tvDateDailyReport.text=it
        }
    }


    private fun loadPreviousDateData() {
        var monthValue = viewModel.month!!.toInt()
        var dayValue = viewModel.day!!.toInt()
        when (monthValue) {
            2, 4, 6, 8, 9, 11 -> {
                if (dayValue == 1) {
                    monthValue -= 1
                    dayValue = 31
                    setMonthPlain(monthValue)
                    setDay(dayValue)
                } else {
                    viewModel.day = (viewModel.day!!.toInt() - 1).toString()
                }
                viewModel.date.value = "${viewModel.day}-${viewModel.month}-${viewModel.year}"
                binding.tvDateDailyReport.text = viewModel.date.value
                viewModel.loadDailyTransactions(viewModel.year!!, viewModel.month!!,viewModel.day!!)
            }
            5, 7, 10, 12 -> {
                if (dayValue == 1) {
                    monthValue -= 1
                    dayValue = 30
                    setMonthPlain(monthValue)
                    setDay(dayValue)
                } else {
                    viewModel.day = (viewModel.day!!.toInt() - 1).toString()
                }
                viewModel.date.value = "${viewModel.day}-${viewModel.month}-${viewModel.year}"
                binding.tvDateDailyReport.text = viewModel.date.value
                viewModel.loadDailyTransactions(viewModel.year!!, viewModel.month!!, viewModel.day!!)
            }
            3 -> {
                if (dayValue == 1) {
                    monthValue -= 1
                    dayValue = 28
                    setMonthPlain(monthValue)
                    setDay(dayValue)
                } else {
                    viewModel.date.value = (viewModel.day!!.toInt() - 1).toString()
                }
                viewModel.date.value = "${viewModel.day}-${viewModel.month}-${viewModel.year}"
                binding.tvDateDailyReport.text = viewModel.date.value
                viewModel.loadDailyTransactions(viewModel.year!!, viewModel.month!!, viewModel.day!!)
            }
            1 -> {
                if (dayValue == 1) {
                    viewModel.year = (viewModel.year!!.toInt() - 1).toString()
                    monthValue = 12
                    dayValue = 31
                    setMonthPlain(monthValue)
                    setDay(dayValue)
                } else {
                    viewModel.day = (viewModel.day!!.toInt() - 1).toString()
                }
                viewModel.date.value = "${viewModel.day}-${viewModel.month}-${viewModel.year}"
                binding.tvDateDailyReport.text = viewModel.date.value
                viewModel.loadDailyTransactions(viewModel.year!!, viewModel.month!!, viewModel.day!!)
            }
        }
    }

    private fun loadNextDateData() {
        var monthValue = viewModel.month!!.toInt()
        var dayValue = viewModel.day!!.toInt()
        when (monthValue) {
            1, 3, 5, 7, 8, 10 -> {
                if (dayValue == 31) {
                    monthValue += 1
                    dayValue = 1
                    setMonthPlain(monthValue)
                    setDay(dayValue)
                } else {
                    viewModel.day = (viewModel.day!!.toInt() + 1).toString()
                }
                viewModel.date.value = "${viewModel.day}-${viewModel.month}-${viewModel.year}"
                binding.tvDateDailyReport.text = viewModel.date.value
                viewModel.loadDailyTransactions(viewModel.year!!, viewModel.month!!, viewModel.day!!)
            }
            4, 6, 9, 11 -> {
                if (dayValue == 30) {
                    monthValue += 1
                    dayValue = 1
                    setMonthPlain(monthValue)
                    setDay(dayValue)
                } else {
                    viewModel.day = (viewModel.day!!.toInt() + 1).toString()
                }
                viewModel.date.value = "${viewModel.day}-${viewModel.month}-${viewModel.year}"
                binding.tvDateDailyReport.text = viewModel.date.value
                viewModel.loadDailyTransactions(viewModel.year!!, viewModel.month!!, viewModel.day!!)
            }
            2 -> {
                if (dayValue == 28) {
                    monthValue += 1
                    dayValue = 1
                    setMonthPlain(monthValue)
                    setDay(dayValue)
                } else {
                    viewModel.day = (viewModel.day!!.toInt() + 1).toString()
                }
                viewModel.date.value = "${viewModel.day}-${viewModel.month}-${viewModel.year}"
                binding.tvDateDailyReport.text = viewModel.date.value
                viewModel.loadDailyTransactions(viewModel.year!!, viewModel.month!!, viewModel.day!!)
            }
            12 -> {
                if (dayValue == 31) {
                    viewModel.year = (viewModel.year!!.toInt() + 1).toString()
                    monthValue = 1
                    dayValue = 1
                    setMonthPlain(monthValue)
                    setDay(dayValue)
                } else {
                    viewModel.day = (viewModel.day!!.toInt() + 1).toString()
                }
                viewModel.date.value = "${viewModel.day}-${viewModel.month}-${viewModel.year}"
                binding.tvDateDailyReport.text = viewModel.date.value
                viewModel.loadDailyTransactions(viewModel.year!!, viewModel.month!!, viewModel.day!!)
            }
        }
    }

    private fun setMonthPlain(monthh: Int) {
        viewModel.month = when (monthh) {
            1 -> {
                getString(R.string.digit01)
            }
            2 -> {
                getString(R.string.digit02)
            }
            3 -> {
                getString(R.string.digit03)
            }
            4 -> {
                getString(R.string.digit04)
            }
            5 -> {
                getString(R.string.digit05)
            }
            6 -> {
                getString(R.string.digit06)
            }
            7 -> {
                getString(R.string.digit07)
            }
            8 -> {
                getString(R.string.digit08)
            }
            9 -> {
                getString(R.string.digit09)
            }
            else -> {
                monthh.toString()
            }
        }
    }

    private fun changeDateAndFetchNewData() {
        tempDay = viewModel.day!!.toInt()
        tempMonth = viewModel.month!!.toInt() - 1
        tempYear = viewModel.year!!.toInt()
        val datePickerDialog = DatePickerDialog(requireContext(),
            { _: DatePicker?, yearr: Int, monthh: Int, dayOfMonth: Int ->
                viewModel.year = yearr.toString()
                setMonth(monthh)
                setDay(dayOfMonth)
                viewModel.date.value = "${viewModel.day}-${viewModel.month}-${viewModel.year}"
                binding.tvDateDailyReport.text = viewModel.date.value
                viewModel.loadDailyTransactions(viewModel.year!!,viewModel.month!!,viewModel.year!!)
            },
            tempYear,
            tempMonth,
            tempDay)
        datePickerDialog.show()
    }

    private fun setDay(dayOfMonth: Int) {
        viewModel.day = when (dayOfMonth) {
            1 -> {
                getString(R.string.digit01)
            }
            2 -> {
                getString(R.string.digit02)
            }
            3 -> {
                getString(R.string.digit03)
            }
            4 -> {
                getString(R.string.digit04)
            }
            5 -> {
                getString(R.string.digit05)
            }
            6 -> {
                getString(R.string.digit06)
            }
            7 -> {
                getString(R.string.digit07)
            }
            8 -> {
                getString(R.string.digit08)
            }
            9 -> {
                getString(R.string.digit09)
            }
            else -> {
                dayOfMonth.toString()
            }
        }
    }

    private fun setMonth(monthh: Int) {
        viewModel.month = when (monthh) {
            0 -> {
                getString(R.string.digit01)
            }
            1 -> {
                getString(R.string.digit02)
            }
            2 -> {
                getString(R.string.digit03)
            }
            3 -> {
                getString(R.string.digit04)
            }
            4 -> {
                getString(R.string.digit05)
            }
            5 -> {
                getString(R.string.digit06)
            }
            6 -> {
                getString(R.string.digit07)
            }
            7 -> {
                getString(R.string.digit08)
            }
            8 -> {
                getString(R.string.digit09)
            }
            else -> {
                (monthh + 1).toString()
            }
        }
    }



    @SuppressLint("NotifyDataSetChanged")
    private fun updateUI(postList: List<MC_Posts>){
        if(postList.isEmpty()){
            binding.cvNoResultFound.visibility = View.VISIBLE
        }
        else{
            binding.cvNoResultFound.visibility = View.GONE
        }
        adapterDaily = Adapter_Daily(postList,requireContext(),this)
        binding.tvTotalIncomeValueDaily.text = viewModel.totalIncome.toString()
        binding.tvTotalExpenseValueDaily.text = viewModel.totalExpense.toString()
        binding.rvDailyReport.adapter = adapterDaily
        adapterDaily!!.notifyDataSetChanged()
    }




    fun confirmDelete(id: Int, amount: Int, typeOfTheFile: String){
        DbAdapter.confirmDelete(requireContext(),id,amount,typeOfTheFile){
            if(it !=null){
                if(it){
                    viewModel.loadDailyTransactions(viewModel.year!!,viewModel.month!!,viewModel.day!!)
                }
                else{
                    Toast.makeText(requireContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show()
                }
            }

        }
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

}