package com.lincoln4791.dailyexpensemanager.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
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
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.material.tabs.TabLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.itmedicus.patientaid.ads.admobAdsUpdated.AdMobUtil
import com.itmedicus.patientaid.ads.admobAdsUpdated.BannerAddHelper
import com.lincoln4791.dailyexpensemanager.common.util.CurrentDate
import com.lincoln4791.dailyexpensemanager.Adapters.Adapter_Transactions
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.common.PrefManager
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
import java.util.*

class TransactionsFragment : Fragment() {
    val args: TransactionsFragmentArgs by navArgs()
    private val linearLayoutManager = LinearLayoutManager(context)
    private var toolbar: Toolbar? = null
    private var adapter_transactions: Adapter_Transactions? = null
    private lateinit var transactionType :String

    private lateinit var binding : FragmentTransactionsBinding
    private lateinit var prefManager : PrefManager
    private var vm_transactions: VM_Transactions? = null
    private lateinit var navCon : NavController
    private lateinit var firebaseAnalytics: FirebaseAnalytics


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("LifeCycle","Transactions Fragment Create")

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
    ): View {
        Log.d("LifeCycle","Transactions Fragment Create View")
        // Inflate the layout for this fragment
        prefManager = PrefManager(requireContext())
        binding = FragmentTransactionsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("LifeCycle","Transactions Fragment ViewCreated")
        Util.recordScreenEvent("transactions_fragment","MainActivity")
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME,"home_fragment")
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "Main Activity")
        firebaseAnalytics = Firebase.analytics
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)

        navCon = Navigation.findNavController(view)

        initAdMob()

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

        if(posts.isEmpty()){
            binding.cvNoResultFound.visibility = View.VISIBLE
        }
        else{
            binding.cvNoResultFound.visibility = View.GONE
        }

        vm_transactions!!.fetchAllTransactions(posts)
        adapter_transactions = Adapter_Transactions(posts, requireContext(),this)
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
        loadTransactions()
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
        this.onDestroy()
        this.onDetach()
       // navCon.navigateUp()
    }

    fun navigateToDetails(year:String,month:String,type:String,category:String){
        val action = TransactionsFragmentDirections.actionTransactionsFragmentToMonthlyCategoryWiseFragment(year,month,type,category,Constants.FRAGMENT_TRANSACTION,transactionType)
        navCon.navigate(action)
        this.onDestroy()
        this.onDetach()
    }

    private fun initAdMob() {
        val lastAdShowDate = prefManager.lastBannerAdShownTransactionsF
        if (AdMobUtil.canAdShow(requireContext(), lastAdShowDate)) {
            binding.adView.visibility = View.VISIBLE

            MobileAds.initialize(requireContext()) {
              /*  val testDeviceIds = Arrays.asList("CECB82F928EFB5B4CA0B77EBD0375477")
                val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
                MobileAds.setRequestConfiguration(configuration)*/
                val bannerAdHelper = BannerAddHelper(requireContext())
                bannerAdHelper.loadBannerAd(binding.adView) {
                    if (it) {
                        prefManager.lastBannerAdShownTransactionsF = CurrentDate.currentTime24H
                    }
                }
            }
        } else {
            binding.adView.visibility = View.GONE
        }
    }

/*    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }*/


    override fun onDestroy() {
        Log.d("LifeCycle", "Transactions Fragment Destroyed")
        super.onDestroy()
    }

    override fun onDestroyView() {
        Log.d("LifeCycle", "Transactions Fragment DestroyedView")
        super.onDestroyView()
    }

    override fun onStop() {
        Log.d("LifeCycle", "Transactions Fragment Stop")
        super.onStop()
    }

    override fun onPause() {
        Log.d("LifeCycle", "Transactions Fragment Paused")
        super.onPause()
    }

    override fun onAttach(context: Context) {
        Log.d("LifeCycle", "Transactions Fragment Attached")
        super.onAttach(context)
    }

    override fun onDetach() {
        Log.d("LifeCycle", "Transactions Fragment Detached")
        super.onDetach()
    }

    override fun onStart() {
        Log.d("LifeCycle", "Transactions Fragment Started")
        super.onStart()
    }

    override fun onResume() {
        Log.d("LifeCycle", "Transactions Fragment resumed")
        super.onResume()
    }



}