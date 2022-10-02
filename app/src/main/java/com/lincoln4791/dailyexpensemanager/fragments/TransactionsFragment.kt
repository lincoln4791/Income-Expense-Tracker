package com.lincoln4791.dailyexpensemanager.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybaseproject2.base.BaseFragment
import com.google.android.gms.ads.MobileAds
import com.google.android.material.tabs.TabLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.itmedicus.patientaid.ads.admobAdsUpdated.AdMobUtil
import com.itmedicus.patientaid.ads.admobAdsUpdated.BannerAddHelper
import com.lincoln4791.dailyexpensemanager.common.util.CurrentDate
import com.lincoln4791.dailyexpensemanager.Adapters.Adapter_Transactions
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.common.PrefManager
import com.lincoln4791.dailyexpensemanager.common.util.DbAdapter
import com.lincoln4791.dailyexpensemanager.common.util.Util
import com.lincoln4791.dailyexpensemanager.common.util.GlobalVariabls
import com.lincoln4791.dailyexpensemanager.databinding.FragmentTransactionsBinding
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import com.lincoln4791.dailyexpensemanager.roomDB.AppDatabase
import com.lincoln4791.dailyexpensemanager.viewModels.VMTransactions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TransactionsFragment : BaseFragment<FragmentTransactionsBinding>(FragmentTransactionsBinding::inflate) {
    @Inject lateinit var repository: Repository
    @Inject lateinit var prefManager : PrefManager
    @Inject lateinit var firebaseAnalytics: FirebaseAnalytics
    @Inject lateinit var linearLayoutManager : LinearLayoutManager
    private val args: TransactionsFragmentArgs by navArgs()
    private val vmTransactions by viewModels<VMTransactions>()

    private var toolbar: Toolbar? = null
    private var adapterTransactions: Adapter_Transactions? = null
    private lateinit var transactionType :String
    private lateinit var navCon : NavController




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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        Log.d("LifeCycle","Transactions Fragment ViewCreated")
        Util.recordScreenEvent("transactions_fragment","MainActivity")
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME,"home_fragment")
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "Main Activity")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)

        navCon = Navigation.findNavController(view)

        initAdMob()

        toolbar = view.findViewById(R.id.toolbar_Transactions)
        transactionType = args.type
        markTab()
        binding.rvTransactions.layoutManager=linearLayoutManager

        setUpTabLayout()
        CoroutineScope(Dispatchers.IO).launch {
            repository.loadYearWise("2022")
        }

        vmTransactions.postsList.observe(viewLifecycleOwner) {
            Log.d("Transaction", "observed")
            @Suppress("UNCHECKED_CAST")
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                is Resource.Success<*> -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        update(it.value as List<MC_Posts>)
                    }

                }
                is Resource.Failure -> {
                    if (it.isNetworkError) {
                        Toast.makeText(requireContext(),
                            "something went wrong, please try later -> ${it.errorBody}",
                            Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(),
                            "something went wrong, please try later -> ${it.errorBody}",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        @Suppress("RedundantSamConstructor")
        vmTransactions.totalIncome.observe(viewLifecycleOwner, Observer {
            binding.tvTotalIncomeValueTopBarTransactions.text = it.toString()
        })

        vmTransactions.totalExpense.observe(viewLifecycleOwner) {
            binding.tvTotalExpenseValueTopBarTransactions.text = it.toString()
        }



        binding.cvMonthlyTransactions.setOnClickListener {
            /*  startActivity(Intent(this@Transactions,
                  MonthlyReport::class.java))*/
        }
        binding.cvDailyTransactions.setOnClickListener {
            /* startActivity(Intent(this@Transactions,
                 Daily::class.java))*/
        }
        binding.cvFullReportTransactions.setOnClickListener {
            /*  startActivity(Intent(this@Transactions,
                  FullReport::class.java))*/
        }
        binding.ivDeleteAllTransactions.setOnClickListener { confirmDeleteAll() }
        binding.cvImg.setOnClickListener {
            goBack()
        }

        setCurrentBalance()
        Log.d("tag","Current Balance is ${GlobalVariabls.currentBalance}")

        loadTransactions()
    }

    private fun setCurrentBalance() {
        binding.tvCurrentBalanceValueToolBarTransactions.text = GlobalVariabls.currentBalance.toString()
    }

    private fun setUpTabLayout() {
        binding.selectTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab) {
                    binding.selectTab.getTabAt(0) -> {
                        transactionType=Constants.TYPE_ALL
                        vmTransactions.loadAllTransactions()
                    }
                    binding.selectTab.getTabAt(1) -> {
                        transactionType=Constants.TYPE_INCOME
                        vmTransactions.loadAllIncomes()
                    }
                    binding.selectTab.getTabAt(2) -> {
                        transactionType=Constants.TYPE_EXPENSE
                        vmTransactions.loadAllExpenses()
                    }
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
            Constants.TYPE_ALL -> vmTransactions.loadAllTransactions()
            Constants.TYPE_EXPENSE -> vmTransactions.loadAllExpenses()
            Constants.TYPE_INCOME -> vmTransactions.loadAllIncomes()
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun update(posts: List<MC_Posts> ){
        Log.d("Transaction","list size -> ${posts.size}")
        if(posts.isEmpty()){
            binding.cvNoResultFound.visibility = View.VISIBLE
        }
        else{
            binding.cvNoResultFound.visibility = View.GONE
        }

        vmTransactions.fetchAllTransactions(posts)
        adapterTransactions = Adapter_Transactions(posts, requireContext(),this)
        binding.tvTypeTitleTransactions.text = getString(R.string.Transactions)
        toolbar!!.title = getString(R.string.Transactions)
        binding.rvTransactions.adapter = adapterTransactions
        adapterTransactions!!.notifyDataSetChanged()

        when (transactionType) {
            Constants.TYPE_ALL -> {
                binding.tvTypeTitleTransactions.text = getString(R.string.Transactions)
                //binding.selectTab.selectTab(binding.selectTab.getTabAt(0))
            }
            Constants.TYPE_INCOME -> {
                binding.tvTypeTitleTransactions.text = "Incomes"
                //binding.selectTab.selectTab(binding.selectTab.getTabAt(1))
            }
            Constants.TYPE_EXPENSE -> {
                binding.tvTypeTitleTransactions.text = "Expenses"
                //binding.selectTab.selectTab(binding.selectTab.getTabAt(2))
            }
        }

        //unloadProgressBar()

    }

    private fun loadProgressBar() {
        binding.mainLoadingBar.visibility = View.VISIBLE
        binding.clContainer.visibility=View.GONE
    }

    private fun unloadProgressBar(){
        binding.mainLoadingBar.visibility = View.GONE
        binding.clContainer.visibility=View.VISIBLE
    }


    private fun deleteDataAll() {
        CoroutineScope(Dispatchers.IO).launch {
            AppDatabase.getInstance(requireContext().applicationContext).dbDao().deleteAll()
            loadTransactions()
            setCurrentBalance()
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

    @SuppressLint("InflateParams")
    fun confirmDeleteAll() {
        val dialog = Dialog(requireContext())
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_delete_all, null)
        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.show()
        view.findViewById<View>(R.id.btn_yes_alertImage_dialog_deleteAll).setOnClickListener {
            dialog.dismiss()
            deleteDataAll()
        }
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

    fun markTab(){
        when (transactionType) {
            Constants.TYPE_ALL -> {
                binding.selectTab.selectTab(binding.selectTab.getTabAt(0))
            }
            Constants.TYPE_INCOME -> {
                binding.selectTab.selectTab(binding.selectTab.getTabAt(1))
            }
            Constants.TYPE_EXPENSE -> {
                binding.selectTab.selectTab(binding.selectTab.getTabAt(2))
            }
        }
        unloadProgressBar()

    }



}