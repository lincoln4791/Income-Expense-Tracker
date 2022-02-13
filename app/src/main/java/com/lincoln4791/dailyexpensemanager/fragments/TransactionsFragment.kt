package com.lincoln4791.dailyexpensemanager.fragments

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.lincoln4791.dailyexpensemanager.Adapters.Adapter_Transactions
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.common.util.DbAdapter
import com.lincoln4791.dailyexpensemanager.common.util.Util
import com.lincoln4791.dailyexpensemanager.common.util.GlobalVariabls
import com.lincoln4791.dailyexpensemanager.databinding.FragmentTransactionsBinding
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import com.lincoln4791.dailyexpensemanager.roomDB.AppDatabase
import com.lincoln4791.dailyexpensemanager.viewModels.VM_Transactions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class TransactionsFragment : Fragment() {
    val args: TransactionsFragmentArgs by navArgs()
    private val linearLayoutManager = LinearLayoutManager(context)
    private var toolbar: Toolbar? = null
    private var adapter_transactions: Adapter_Transactions? = null
    private lateinit var transactionType :String

    private lateinit var binding : FragmentTransactionsBinding
    private var vm_transactions: VM_Transactions? = null
    private lateinit var navCon : NavController
    private lateinit var firebaseAnalytics: FirebaseAnalytics


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

        Util.recordScreenEvent("transactions_fragment","MainActivity")
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME,"home_fragment")
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "Main Activity")
        firebaseAnalytics = Firebase.analytics
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)

        navCon = Navigation.findNavController(view)

        toolbar = view.findViewById(R.id.toolbar_Transactions)
        transactionType = args.type

        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        binding.rvTransactions.layoutManager = linearLayoutManager
        vm_transactions = ViewModelProvider(this)[VM_Transactions::class.java]

        setUpTabLayout()


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
/*        binding.cvTotalIncomesTopBarTransactions.setOnClickListener {
            transactionType=Constants.TYPE_INCOME
            vm_transactions!!.loadAllIncomes()
        }
        binding.ivReloadTransactionsTransactions.setOnClickListener {
            transactionType=Constants.TYPE_ALL
            vm_transactions!!.loadAllTransactions()
        }
        binding.cvTotalExpensesTopBarTransactions.setOnClickListener {
            transactionType=Constants.TYPE_EXPENSE
            vm_transactions!!.loadAllExpenses()
        }*/

        binding.tvCurrentBalanceValueToolBarTransactions.text = GlobalVariabls.currentBalance.toString()
        Log.d("tag","Current Balance is ${GlobalVariabls.currentBalance}")

        loadTransactions()



    }

    private fun setUpTabLayout() {
        binding.selectTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab == binding.selectTab.getTabAt(0)){
                    transactionType=Constants.TYPE_ALL
                    vm_transactions!!.loadAllTransactions()
                }

                else if(tab == binding.selectTab.getTabAt(1)){
                    transactionType=Constants.TYPE_INCOME
                    vm_transactions!!.loadAllIncomes()
                }

                else if(tab == binding.selectTab.getTabAt(2)){
                    transactionType=Constants.TYPE_EXPENSE
                    vm_transactions!!.loadAllExpenses()
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselect
            }
        })
    }

    private fun loadTransactions() {
        when(transactionType){
            Constants.TYPE_ALL -> vm_transactions!!.loadAllTransactions()
            Constants.TYPE_EXPENSE -> vm_transactions!!.loadAllExpenses()
            Constants.TYPE_INCOME -> vm_transactions!!.loadAllIncomes()
        }
    }

    private fun update(posts: List<MC_Posts> ){
        vm_transactions!!.fetchAllTransactions(posts)
        adapter_transactions = Adapter_Transactions(posts, this)
        binding.tvTypeTitleTransactions.text = getString(R.string.Transactions)
        toolbar!!.title = getString(R.string.Transactions)
        binding.rvTransactions.adapter = adapter_transactions
        adapter_transactions!!.notifyDataSetChanged()

        when (transactionType) {
            Constants.TYPE_ALL -> {
                binding.tvTypeTitleTransactions.text = getString(R.string.Transactions)
                binding.selectTab.selectTab(binding.selectTab.getTabAt(0))
            }
            Constants.TYPE_INCOME -> {
                binding.tvTypeTitleTransactions.text = "Incomes"
                binding.selectTab.selectTab(binding.selectTab.getTabAt(1))
            }
            Constants.TYPE_EXPENSE -> {
                binding.tvTypeTitleTransactions.text = "Expenses"
                binding.selectTab.selectTab(binding.selectTab.getTabAt(2))
            }
        }

    }


    private fun deleteDataAll() {
        CoroutineScope(Dispatchers.IO).launch {
            AppDatabase.getInstance(requireContext().applicationContext).dbDao().deleteAll()
        }
    }

    fun confirmDelete(id: Int, amount: Int, typeOfTheFile: String){
        DbAdapter.confirmDelete(requireContext(),id,amount,typeOfTheFile){
            if(it !=null){
                if(it){
                    loadTransactions()
                }
                else{
                    Toast.makeText(requireContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show()
                }
            }

        }
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
        navCon.navigate(homeAction)
    }

    fun navigateToDetails(year:String,month:String,type:String,category:String){
        val action = TransactionsFragmentDirections.actionTransactionsFragmentToMonthlyCategoryWiseFragment(year,month,type,category,Constants.FRAGMENT_TRANSACTION,transactionType)
        navCon.navigate(action)
    }

/*    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }*/



}