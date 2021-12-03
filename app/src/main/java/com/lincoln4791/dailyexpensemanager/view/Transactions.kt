package com.lincoln4791.dailyexpensemanager.view

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.lincoln4791.dailyexpensemanager.viewModels.VM_Transactions
import com.lincoln4791.dailyexpensemanager.Adapters.Adapter_Transactions
import android.os.Bundle
import com.lincoln4791.dailyexpensemanager.R
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import com.lincoln4791.dailyexpensemanager.view.MonthlyReport
import com.lincoln4791.dailyexpensemanager.view.Daily
import com.lincoln4791.dailyexpensemanager.view.FullReport
import com.lincoln4791.dailyexpensemanager.view.MainActivity
import com.lincoln4791.dailyexpensemanager.common.UtilDB
import com.lincoln4791.dailyexpensemanager.common.Extras
import android.os.AsyncTask
import com.lincoln4791.dailyexpensemanager.common.SQLiteHelper
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import com.lincoln4791.dailyexpensemanager.view.Transactions
import android.widget.Toast
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.databinding.ActivityTransactionsBinding

class Transactions() : AppCompatActivity() {
    private val linearLayoutManager = LinearLayoutManager(this)
    private var toolbar: Toolbar? = null
    private var totalIncome = 0
    private var totalExpense = 0
    private val balance = 0
    private var vm_transactions: VM_Transactions? = null
    private var adapter_transactions: Adapter_Transactions? = null

    private lateinit var binding : ActivityTransactionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = findViewById(R.id.toolbar_Transactions)



        //***********************************************Initializations*****************************************
        supportActionBar!!.hide()
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        binding.rvTransactions.setLayoutManager(linearLayoutManager)
        vm_transactions = ViewModelProviders.of(this).get(VM_Transactions::class.java)
        adapter_transactions = Adapter_Transactions(vm_transactions!!.postsList, this)


        //************************************************Click Listeners****************************************
        binding.cvMonthlyTransactions.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(this@Transactions,
                MonthlyReport::class.java))
        })
        binding.cvDailyTransactions.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(this@Transactions,
                Daily::class.java))
        })
        binding.cvFullReportTransactions.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(this@Transactions,
                FullReport::class.java))
        })
        binding.ivDeleteAllTransactions.setOnClickListener(View.OnClickListener { v: View? -> confirmDeleteAll() })
        binding.ivHomeToolbarTransactions.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(this@Transactions,
                MainActivity::class.java))
        })
        binding.cvTotalIncomesTopBarTransactions.setOnClickListener(View.OnClickListener { v: View? -> AllIncomeTask().execute() })
        binding.ivReloadTransactionsTransactions.setOnClickListener(View.OnClickListener { v: View? -> AllTransactionsTask().execute() })
        binding.cvTotalExpensesTopBarTransactions.setOnClickListener(View.OnClickListener { v: View? -> AllExpensesTask().execute() })


        //************************************************Starting Methods**************************************
        binding.tvCurrentBalanceValueToolBarTransactions.text = UtilDB.currentBalance.toString()
        intentData
    }

    private val intentData: Unit
        get() {
            if ((intent.getStringExtra(Extras.TYPE) == Constants.TYPE_INCOME)) {
                AllIncomeTask().execute()
            } else if ((intent.getStringExtra(Extras.TYPE) == Constants.TYPE_EXPENSE)) {
                AllExpensesTask().execute()
            } else {
                AllTransactionsTask().execute()
            }
        }

    internal inner class AllTransactionsTask() : AsyncTask<Any?, Any?, Any?>() {
        override fun doInBackground(objects: Array<Any?>): Any? {
            fetchAllTransactions()
            return null
        }

        override fun onPostExecute(o: Any?) {
            super.onPostExecute(o)
            binding.tvTypeTitleTransactions.text = getString(R.string.Transactions)
            toolbar!!.title = getString(R.string.Transactions)
            binding.tvTotalExpenseValueTopBarTransactions.text = totalExpense.toString()
            binding.tvTotalIncomeValueTopBarTransactions.text = totalIncome.toString()
            binding.rvTransactions.adapter = adapter_transactions
            adapter_transactions!!.notifyDataSetChanged()
        }
    }

    internal inner class AllExpensesTask() : AsyncTask<Any?, Any?, Any?>() {
        override fun doInBackground(objects: Array<Any?>): Any? {
            fetchAllExpenses()
            return null
        }

        override fun onPostExecute(o: Any?) {
            super.onPostExecute(o)
            binding.tvTypeTitleTransactions!!.text = getString(R.string.Expenses)
            toolbar!!.title = getString(R.string.Expenses)
            binding.tvTotalExpenseValueTopBarTransactions!!.text = totalExpense.toString()
            binding.tvTotalIncomeValueTopBarTransactions!!.text = getString(R.string.value)
            binding.rvTransactions.adapter = adapter_transactions
            adapter_transactions!!.notifyDataSetChanged()
        }
    }

    internal inner class AllIncomeTask() : AsyncTask<Any?, Any?, Any?>() {
        override fun doInBackground(objects: Array<Any?>): Any? {
            fetchAllIncomes()
            return null
        }

        override fun onPostExecute(o: Any?) {
            super.onPostExecute(o)
            binding.tvTypeTitleTransactions.text = getString(R.string.Incomes)
            toolbar!!.title = getString(R.string.Incomes)
            binding.tvTotalIncomeValueTopBarTransactions.text = totalIncome.toString()
            binding.tvTotalExpenseValueTopBarTransactions.text = getString(R.string.value)
            binding.rvTransactions.adapter = adapter_transactions
            adapter_transactions!!.notifyDataSetChanged()
        }
    }

    private fun fetchAllTransactions() {
        totalIncome = 0
        totalExpense = 0
        vm_transactions!!.postsList.clear()
        val sqLiteHelper = SQLiteHelper(this)
        val cursor = sqLiteHelper.loadAllTransactions()
        while (cursor.moveToNext()) {
            val ID = cursor.getInt(0)
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
            val post = MC_Posts(ID,
                postDescription,
                postCategory,
                postType,
                postAmount,
                postYear,
                postMonth,
                postDay,
                postTime,
                timeStamp,
                postDateTime)
            vm_transactions!!.postsList.add(post)
            if ((postType == Constants.TYPE_INCOME)) {
                totalIncome = totalIncome + postAmount.toInt()
            } else if ((postType == Constants.TYPE_EXPENSE)) {
                totalExpense = totalExpense + postAmount.toInt()
            }
        }
        cursor.close()
    }

    private fun fetchAllExpenses() {
        totalExpense = 0
        totalIncome = 0
        vm_transactions!!.postsList.clear()
        val sqLiteHelper = SQLiteHelper(this)
        val cursor = sqLiteHelper.loadTypeWise(Constants.TYPE_EXPENSE)
        while (cursor.moveToNext()) {
            val ID = cursor.getInt(0)
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
            val post = MC_Posts(ID,
                postDescription,
                postCategory,
                postType,
                postAmount,
                postYear,
                postMonth,
                postDay,
                postTime,
                timeStamp,
                postDateTime)
            vm_transactions!!.postsList.add(post)
            totalExpense = totalExpense + postAmount.toInt()
        }
        cursor.close()
    }

    private fun fetchAllIncomes() {
        totalExpense = 0
        totalIncome = 0
        vm_transactions!!.postsList.clear()
        val sqLiteHelper = SQLiteHelper(this)
        val cursor = sqLiteHelper.loadTypeWise(Constants.TYPE_INCOME)
        while (cursor.moveToNext()) {
            val ID = cursor.getInt(0)
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
            val post = MC_Posts(ID,
                postDescription,
                postCategory,
                postType,
                postAmount,
                postYear,
                postMonth,
                postDay,
                postTime,
                timeStamp,
                postDateTime)
            vm_transactions!!.postsList.add(post)
            totalIncome = totalIncome + postAmount.toInt()
        }
        cursor.close()
    }

    private fun deleteDataAll() {
        val sqLiteHelper = SQLiteHelper(this)
        sqLiteHelper.deleteDataAll()
        val intent = Intent(this@Transactions, Transactions::class.java)
        intent.putExtra(Extras.TYPE, Constants.TYPE_ALL)
        UtilDB.currentBalance = 0
        startActivity(intent)
        finish()
    }

    fun deleteData(id: Int) {
        val sqLiteHelper = SQLiteHelper(this@Transactions)
        Toast.makeText(this, "ID$id", Toast.LENGTH_SHORT).show()
        sqLiteHelper.deleteData(id)
    }

    fun confirmDelete(id: Int, amount: Int, typeOfTheFile: String) {
        val dialog = Dialog(this@Transactions)
        val view = LayoutInflater.from(this@Transactions).inflate(R.layout.dialog_delete, null)
        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.show()
        view.findViewById<View>(R.id.btn_yes_alertImage_dialog_delete).setOnClickListener {
            if ((typeOfTheFile == Constants.TYPE_INCOME)) {
                UtilDB.currentBalance = UtilDB.currentBalance - amount
            } else if ((typeOfTheFile == Constants.TYPE_EXPENSE)) {
                UtilDB.currentBalance = UtilDB.currentBalance + amount
            }
            dialog.dismiss()
            deleteData(id)
        }
        view.findViewById<View>(R.id.btn_no_alertImage_dialog_delete)
            .setOnClickListener { dialog.dismiss() }
    }

    fun confirmDeleteAll() {
        val dialog = Dialog(this@Transactions)
        val view = LayoutInflater.from(this@Transactions).inflate(R.layout.dialog_delete_all, null)
        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.show()
        view.findViewById<View>(R.id.btn_yes_alertImage_dialog_deleteAll).setOnClickListener(
            View.OnClickListener {
                dialog.dismiss()
                deleteDataAll()
            })
        view.findViewById<View>(R.id.btn_no_alertImage_dialog_deleteAll)
            .setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    dialog.dismiss()
                }
            })
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}