package com.lincoln4791.dailyexpensemanager.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.lincoln4791.dailyexpensemanager.Adapters.Adapter_Transactions
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.common.Extras
import com.lincoln4791.dailyexpensemanager.common.UtilDB
import com.lincoln4791.dailyexpensemanager.databinding.FragmentTransactionsBinding
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import com.lincoln4791.dailyexpensemanager.roomDB.AppDatabase
import com.lincoln4791.dailyexpensemanager.view.*
import com.lincoln4791.dailyexpensemanager.viewModels.VM_Transactions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionsFragment : Fragment() {
    val args: TransactionsFragmentArgs by navArgs()
    private val linearLayoutManager = LinearLayoutManager(context)
    private var toolbar: Toolbar? = null
    private var adapter_transactions: Adapter_Transactions? = null
    private lateinit var transactionType :String

    private lateinit var binding : FragmentTransactionsBinding
    private var vm_transactions: VM_Transactions? = null
    private lateinit var navCon : NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    Log.d("tag","OnBackPressCalled -> MonthlyCategoryWise")
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
        binding = FragmentTransactionsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navCon = Navigation.findNavController(view)

        toolbar = view.findViewById(R.id.toolbar_Transactions)
        transactionType = args.type

        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        binding.rvTransactions.layoutManager = linearLayoutManager
        vm_transactions = ViewModelProvider(this)[VM_Transactions::class.java]


        vm_transactions!!.postsList.observe(viewLifecycleOwner, Observer {
            Log.d("Transaction", "observed")
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                //is Resource.Success -> adapter_transactions = Adapter_Transactions(it.data, this)
                is Resource.Success ->  update(it.data)
                is Resource.Error -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        })

        vm_transactions!!.totalIncome.observe(viewLifecycleOwner, Observer {
            binding.tvTotalIncomeValueTopBarTransactions.text = it.toString()
        })

        vm_transactions!!.totalExpense.observe(viewLifecycleOwner, Observer {
            binding.tvTotalExpenseValueTopBarTransactions.text = it.toString()
        })



        binding.cvMonthlyTransactions.setOnClickListener(View.OnClickListener { v: View? ->
          /*  startActivity(Intent(this@Transactions,
                MonthlyReport::class.java))*/
        })
        binding.cvDailyTransactions.setOnClickListener(View.OnClickListener { v: View? ->
           /* startActivity(Intent(this@Transactions,
                Daily::class.java))*/
        })
        binding.cvFullReportTransactions.setOnClickListener(View.OnClickListener { v: View? ->
          /*  startActivity(Intent(this@Transactions,
                FullReport::class.java))*/
        })
        binding.ivDeleteAllTransactions.setOnClickListener(View.OnClickListener { v: View? -> confirmDeleteAll() })
        binding.cvImg.setOnClickListener(View.OnClickListener { v: View? ->
            goBack()
        })
        binding.cvTotalIncomesTopBarTransactions.setOnClickListener {
            vm_transactions!!.loadAllIncomes()
        }
        binding.ivReloadTransactionsTransactions.setOnClickListener {
            vm_transactions!!.loadAllTransactions()
        }
        binding.cvTotalExpensesTopBarTransactions.setOnClickListener {
            vm_transactions!!.loadAllExpenses()
        }

        binding.tvCurrentBalanceValueToolBarTransactions.text = UtilDB.currentBalance.toString()
        Log.d("tag","Current Balance is ${UtilDB.currentBalance}")

        when(transactionType){
            Constants.TYPE_ALL -> vm_transactions!!.loadAllTransactions()
            Constants.TYPE_EXPENSE -> vm_transactions!!.loadAllExpenses()
            Constants.TYPE_INCOME -> vm_transactions!!.loadAllIncomes()
        }

        //vm_transactions!!.loadAllTransactions()

    }

    private fun update(posts: List<MC_Posts> ){
        vm_transactions!!.fetchAllTransactions(posts)
        adapter_transactions = Adapter_Transactions(posts, requireContext())
        binding.tvTypeTitleTransactions.text = getString(R.string.Transactions)
        toolbar!!.title = getString(R.string.Transactions)
        binding.rvTransactions.adapter = adapter_transactions
        adapter_transactions!!.notifyDataSetChanged()
    }


    private fun deleteDataAll() {
        CoroutineScope(Dispatchers.IO).launch {
            AppDatabase.getInstance(requireContext().applicationContext).dbDao().deleteAll()
        }
        /*val intent = Intent(context, Transactions::class.java)
        intent.putExtra(Extras.TYPE, Constants.TYPE_ALL)
        UtilDB.currentBalance = 0
        startActivity(intent)*/
    }

    fun deleteData(id: Int) {
        AppDatabase.getInstance(requireContext().applicationContext).dbDao().delete(id.toString())
    }

    fun confirmDelete(id: Int, amount: Int, typeOfTheFile: String) {
        val dialog = Dialog(requireContext())
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_delete, null)
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
        val dialog = Dialog(requireContext())
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_delete_all, null)
        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.show()
        view.findViewById<View>(R.id.btn_yes_alertImage_dialog_deleteAll).setOnClickListener(
            View.OnClickListener {
                dialog.dismiss()
                deleteDataAll()
            })
        view.findViewById<View>(R.id.btn_no_alertImage_dialog_deleteAll)
            .setOnClickListener { dialog.dismiss() }
    }


    private fun goBack(){
        val homeAction = TransactionsFragmentDirections.actionTransactionsFragmentToHomeFragment()
        navCon.navigateUp()
    }

/*    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }*/



}