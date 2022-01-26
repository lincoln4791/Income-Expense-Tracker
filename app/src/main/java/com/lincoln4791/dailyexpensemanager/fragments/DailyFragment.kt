package com.lincoln4791.dailyexpensemanager.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.lincoln4791.dailyexpensemanager.Adapters.Adapter_Daily
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.common.Extras
import com.lincoln4791.dailyexpensemanager.common.SQLiteHelper
import com.lincoln4791.dailyexpensemanager.common.UtilDB
import com.lincoln4791.dailyexpensemanager.databinding.AddExpenseFragmentBinding
import com.lincoln4791.dailyexpensemanager.databinding.FragmentDailyBinding
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import com.lincoln4791.dailyexpensemanager.roomDB.AppDatabase
import com.lincoln4791.dailyexpensemanager.view.MainActivity
import com.lincoln4791.dailyexpensemanager.viewModels.VM_Daily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class DailyFragment : Fragment() {

    private var adapterDaily: Adapter_Daily? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var day: String? = null
    private var month: String? = null
    private var year: String? = null
    private var date: String? = null
    private var totalIncome = 0
    private var totalExpense = 0
    private var tempMonth = 0
    private var tempYear = 0
    private var tempDay = 0
    private lateinit var viewModel: VM_Daily
    private lateinit var binding : FragmentDailyBinding
    private lateinit var navCon : NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDailyBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[VM_Daily::class.java]
        navCon = Navigation.findNavController(view)


        linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager!!.reverseLayout = true
        linearLayoutManager!!.stackFromEnd = true
        //adapterDaily = Adapter_Daily(viewModel.postsList!!.value, requireContext())
        binding.rvDailyReport.layoutManager = linearLayoutManager
        binding.rvDailyReport.adapter = adapterDaily
        val calendar = Calendar.getInstance()
        tempMonth = calendar[Calendar.MONTH]
        tempYear = calendar[Calendar.YEAR]
        tempDay = calendar[Calendar.DAY_OF_MONTH]

        viewModel.postsList.observe(viewLifecycleOwner,Observer{
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                is Resource.Success ->  calculateAll(it.data)
                is Resource.Error -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        })

        binding.cvDateDailyReport.setOnClickListener { changeDateAndFetchNewData() }
        binding.ibNextDateDailyReport.setOnClickListener { loadNextDateData() }
        binding.ibPreviousDateDailyReport.setOnClickListener { loadPreviousDateData() }
        binding.cvFullReportDailyReport.setOnClickListener {
            /* startActivity(Intent(this@Daily,
                 FullReport::class.java))*/
        }
        binding.cvMonthlyTransactionsDailyReport.setOnClickListener(View.OnClickListener { v: View? ->
          /*  startActivity(Intent(this@Daily,
                MonthlyReport::class.java))*/
        })
        binding.cvTransactionsDailyReport.setOnClickListener(View.OnClickListener { v: View? ->
            /*val intent = Intent(this@Daily, Transactions::class.java)
            intent.putExtra(Extras.TYPE, Constants.TYPE_ALL)
            startActivity(intent)*/
        })
        binding.cvImg.setOnClickListener(View.OnClickListener { v: View? ->
        navCon.navigateUp()
        })


        setDate()
        binding.tvCurrentBalanceValueToolBarMonthlyReport.text = UtilDB.currentBalance.toString()
       viewModel.loadDailyTransactions(year!!,month!!,day!!)

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
            binding.tvCurrentBalanceValueToolBarMonthlyReport.text = date
            //YearMonthDayTask().execute()
            viewModel.loadDailyTransactions(year!!, month!!, day!!)
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
            //YearMonthDayTask().execute()
            viewModel.loadDailyTransactions(year!!, month!!, day!!)
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
            //YearMonthDayTask().execute()
            viewModel.loadDailyTransactions(year!!, month!!, day!!)
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
            binding.tvDateDailyReport.text = date
            //YearMonthDayTask().execute()
            viewModel.loadDailyTransactions(year!!, month!!, day!!)
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
            //YearMonthDayTask().execute()
            viewModel.loadDailyTransactions(year!!, month!!, day!!)
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
            //YearMonthDayTask().execute()
            viewModel.loadDailyTransactions(year!!, month!!, day!!)
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
            //YearMonthDayTask().execute()
            viewModel.loadDailyTransactions(year!!, month!!, day!!)
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
            //YearMonthDayTask().execute()
            viewModel.loadDailyTransactions(year!!, month!!, day!!)
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
        val datePickerDialog = DatePickerDialog(requireContext(),
            { view: DatePicker?, yearr: Int, monthh: Int, dayOfMonth: Int ->
                year = yearr.toString()
                setMonth(monthh)
                setDay(dayOfMonth)
                date = "$day-$month-$year"
                binding.tvDateDailyReport.text = date
                //YearMonthDayTask().execute()
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


    private fun updateUI(){
        binding.tvTotalIncomeValueDaily.text = totalIncome.toString()
        binding.tvTotalExpenseValueDaily.text = totalExpense.toString()
        binding.rvDailyReport.adapter = adapterDaily
        adapterDaily!!.notifyDataSetChanged()
    }

    private fun calculateAll(postList:List<MC_Posts>) {
        adapterDaily = Adapter_Daily(postList, requireContext(),this@DailyFragment)
        totalIncome = 0
        totalExpense = 0
        Log.d("tag", "list size : " + postList.size)
        for (i in postList.indices) {
            if (postList[i].postType == Constants.TYPE_INCOME) {
                totalIncome = totalIncome + postList[i].postAmount.toInt()
            } else if (postList[i].postType == Constants.TYPE_EXPENSE) {
                totalExpense = totalExpense + postList[i].postAmount.toInt()
            }
        }
        updateUI()
    }

    fun deleteData(id: Int,callback : (isDeleted : Boolean,error:String?)->Unit) {
        try {
            AppDatabase.getInstance(requireContext()).dbDao().delete(id.toString())
            callback(true,null)
        }
        catch (e:Exception){
            callback(false,e.message)
        }

    }

    fun confirmDelete(id: Int) {
        val dialog = Dialog(requireContext())
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_delete, null)
        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.show()
        view.findViewById<View>(R.id.btn_yes_alertImage_dialog_delete).setOnClickListener {
            dialog.dismiss()
            CoroutineScope(Dispatchers.IO).launch {
                deleteData(id){ isDeleted: Boolean, error: String? ->
                    Handler(Looper.getMainLooper()).post {
                        if(isDeleted){
                            viewModel.loadDailyTransactions(year!!, month!!, day!!)
                        }
                        else{
                            Toast.makeText(context,"Something Went Wrong -> $error",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }
        view.findViewById<View>(R.id.btn_no_alertImage_dialog_delete)
            .setOnClickListener { dialog.dismiss() }
    }
}