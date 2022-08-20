package com.lincoln4791.dailyexpensemanager.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import androidx.work.*
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.messaging.FirebaseMessaging
import com.itmedicus.patientaid.ads.admobAdsUpdated.AdMobUtil
import com.itmedicus.patientaid.ads.admobAdsUpdated.BannerAddHelper
import com.lincoln4791.dailyexpensemanager.BuildConfig
import com.lincoln4791.dailyexpensemanager.common.util.CurrentDate
//import com.lincoln4791.dailyexpensemanager.BuildConfig
import com.lincoln4791.dailyexpensemanager.background.worker.PeriodicSyncWorker
import com.lincoln4791.dailyexpensemanager.background.worker.SyncWorker
import com.lincoln4791.dailyexpensemanager.common.BannerUtil
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.common.PrefManager
import com.lincoln4791.dailyexpensemanager.common.SubscriptionUtil
import com.lincoln4791.dailyexpensemanager.common.slider.SliderAdapter
import com.lincoln4791.dailyexpensemanager.common.slider.SliderItems
import com.lincoln4791.dailyexpensemanager.common.util.*
import com.lincoln4791.dailyexpensemanager.databinding.FragmentHomeBinding
import com.lincoln4791.dailyexpensemanager.modelClass.Banner
import com.lincoln4791.dailyexpensemanager.viewModels.VM_MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.roomDB.DatabaseDao
import com.lincoln4791.dailyexpensemanager.viewModelFactory.MainViewModelFactory
import com.lincoln4791.dailyexpensemanager.viewModelFactory.ViewModelFactory
import com.lincoln4791.dailyexpensemanager.viewModels.VM_FullReport
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    @Inject protected lateinit var sdf : SimpleDateFormat
    private lateinit var binding: FragmentHomeBinding
    //private lateinit var viewModel: VM_MainActivity
    private lateinit var navCon: NavController
    private lateinit var prefManager: PrefManager
    private var day: String? = null
    private var month: String? = null
    private var year: String? = null
    private val sliderHandler: Handler = Handler()
    @Inject lateinit var dao : DatabaseDao
    private val viewModel by viewModels<VM_MainActivity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("LifeCycle", "Home Fragment Create")

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    Log.d("tag", "OnBackPressCalled -> MonthlyCategoryWise")
                    //navCon.navigateUp()
                    goback()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.d("LifeCycle", "Home Fragment CreateView")
        binding = FragmentHomeBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prefManager = PrefManager(requireContext())
        super.onViewCreated(view, savedInstanceState)
        Log.d("LifeCycle", "Home Fragment ViewCreated")
        val window = requireActivity().window
        //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(R.color.primary)

        setDate()
        setLoginAndLogoutData()
        //getAndSaveFCM()
        Util.recordScreenEvent("home_fragment", "MainActivity")
        FirebaseUtil.fetchCommonDataFromRemoteConfig(requireContext())
        initCheckSubscription()
        InitCheckAppVersion()
        scheduleSyncTask()
        //initAdMob()
        imageSlider()
        initPremiumBadge()
        Util.initAdRemoveByRewardAd(requireContext())


        navCon = Navigation.findNavController(view)
        //viewModel =ViewModelProvider(this@HomeFragment, MainViewModelFactory(dao))[VM_MainActivity::class.java]


        viewModel.totalIncome.observe(viewLifecycleOwner, Observer {
            binding.tvTotalIncomeValueTopBarMainActivity.text = it!!.toString()
        })

        viewModel.totalExpense.observe(viewLifecycleOwner, Observer {
            binding.tvTotalExpenseValueTopBarMainActivity.text = it!!.toString()
        })

        viewModel.currentBalance.observe(viewLifecycleOwner, Observer {
            binding.tvCurrentBalanceValueToolBarMainActivity.text = it!!.toString()
            binding.tvBalanceValueTopBarMainActivity.text = it.toString()
        })


        //*************************************************** Click Listeners****************************************
        binding.cvAddIncomeMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            val action = HomeFragmentDirections.actionHomeFragmentToAddIncomeFragment()
            navCon.navigate(action)
            this.onDestroy()
            this.onDetach()
        })

        binding.cvAddExpensesMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            val action = HomeFragmentDirections.actionHomeFragmentToAddExpenseFragment()
            navCon.navigate(action)
            this.onDestroy()
            this.onDetach()

        })

        binding.cvFullReportMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            val action = HomeFragmentDirections.actionHomeFragmentToFullReportFragment()
            navCon.navigate(action)
            this.onDestroy()
            this.onDetach()
        })

        binding.cvTransactionsMainActivity.setOnClickListener {
            /* val transactionsIntent = Intent(requireContext(), TransactionsActivity::class.java)
             transactionsIntent.putExtra(Extras.TYPE, Constants.TYPE_ALL)
             startActivity(transactionsIntent)*/

            val action =
                HomeFragmentDirections.actionHomeFragmentToTransactionsFragment(Constants.TYPE_ALL)
            navCon.navigate(action)

        }

        binding.cvIncomeMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            /*val transactionsIntent = Intent(requireContext(), TransactionsActivity::class.java)
            transactionsIntent.putExtra(Extras.TYPE, Constants.TYPE_INCOME)
            startActivity(transactionsIntent)*/
            val action =
                HomeFragmentDirections.actionHomeFragmentToTransactionsFragment(Constants.TYPE_INCOME)
            navCon.navigate(action)

        })
        binding.cvExpensesMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            /*  val transactionsIntent = Intent(requireContext(), TransactionsActivity::class.java)
            transactionsIntent.putExtra(Extras.TYPE, Constants.TYPE_EXPENSE)
            startActivity(transactionsIntent)*/
            val action =
                HomeFragmentDirections.actionHomeFragmentToTransactionsFragment(Constants.TYPE_EXPENSE)
            navCon.navigate(action)
        })
        binding.cvDailyMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            val action = HomeFragmentDirections.actionHomeFragmentToDailyFragment()
            navCon.navigate(action)
            this.onDestroy()
            this.onDetach()
            //startActivity(Intent(requireContext(),DailyActivity::class.java))
        })
        binding.cvMonthlyMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            /* startActivity(Intent(this@MainActivity,
                MonthlyReport::class.java))*/

            val action = HomeFragmentDirections.actionHomeFragmentToMonthlyFragment(year, month)
            navCon.navigate(action)
            this.onDestroy()
            this.onDetach()

            /*val monthlyIntent = Intent(requireContext(),MonthlyActivity::class.java)
            monthlyIntent.putExtra("year","2022")
            monthlyIntent.putExtra("month","02")
            startActivity(monthlyIntent)*/

        })
        binding.cvTotalIncomesTopBarMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            /*  val incomeIntent: Intent = Intent(this@MainActivity, Transactions::class.java)
            incomeIntent.putExtra(Extras.TYPE, Constants.TYPE_INCOME)
            startActivity(incomeIntent)*/
        })
        binding.cvTotalExpensesTopBarMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            /*  val expenseIntent: Intent = Intent(this@MainActivity, Transactions::class.java)
            expenseIntent.putExtra(Extras.TYPE, Constants.TYPE_EXPENSE)
            startActivity(expenseIntent)*/
        })
        binding.cvAboutMainActivity.setOnClickListener {
            openAbout()
            //syncNow(requireContext())
            getBackStackCount()
        }
        binding.cvBackupDataMainActivity.setOnClickListener {
            Toast.makeText(requireContext().applicationContext, "Coming Soon", Toast.LENGTH_SHORT)
                .show()

          /*  val d = HashMap<String,String>()
            d["name"] = Random().nextInt().toString()
            d["phone"] = Random().nextInt().toString()
            Firebase.database.reference.child(Constants.USER_DATA).child(prefManager.UID).child("test").setValue(d)*/

        }
        binding.cvRestoreDataMainActivity.setOnClickListener {
            Toast.makeText(context,
                "Coming Soon",
                Toast.LENGTH_SHORT).show()
        }

        binding.ivNavMenu.setOnClickListener {
            binding.drawerLayout.open()
        }

        binding.navigationView.setNavigationItemSelectedListener {
            it.isChecked = true
            binding.drawerLayout.close()

            if (it.itemId == R.id.menu_profile) {
                val goToProfile = HomeFragmentDirections.actionHomeFragmentToProfileFragment()
                navCon.navigate(goToProfile)
            } else if (it.itemId == R.id.menu_NoAds) {

                val action = HomeFragmentDirections.actionHomeFragmentToSubscription()
                //navCon.popBackStack()
                navCon.navigate(action)
                //navCon.popBackStack()

              /*  Toast.makeText(requireContext().applicationContext,
                    "Coming Soon",
                    Toast.LENGTH_SHORT).show()*/
                /*if(prefManager.isDarkThemeEnabled){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    prefManager.isDarkThemeEnabled = false
                    it.title = "Day Theme"
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    prefManager.isDarkThemeEnabled = true
                    it.title = "Dark Theme"
                }*/
            } else if (it.itemId == R.id.menu_facebookPage) {

                Util.goToFacebookPage(requireContext())
            } else if (it.itemId == R.id.menu_checkUpdate) {
                goToPlayStore(requireContext())
            } else if (it.itemId == R.id.menu_rateUs) {
                goToPlayStore(requireContext())
            } else if (it.itemId == R.id.menu_aboutUs) {
                openAbout()
            } else if (it.itemId == R.id.menu_privacyPolicy) {
                val intent =
                    Intent(Intent.ACTION_VIEW,
                        Uri.parse(Constants.PRIVACY_POLICY_LINK))
                startActivity(intent)
            }

/*            else if(it.itemId == R.id.menu_moreApps){
                val intent =
                    Intent(Intent.ACTION_VIEW,
                        Uri.parse(" https://play.google.com/store/apps/developer?id=Mahmudul+Karim+Lincoln&hl=en&gl=US"))
                startActivity(intent)
            }*/
            else if (it.itemId == R.id.menu_shareApp) {
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                val shareBodyText =
                    "Income Expense Manager - Your Daily Financial Calculator.\n\n" + "Download Income Expense Manager from google play:\n\n ${Constants.PLAY_STORE_APP_LINK}"
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT,
                    "Keep Track Of Your Daily Transactions.")
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText)
                startActivity(Intent.createChooser(sharingIntent, "Share Income Expense Manager"))
            }
            else if (it.itemId == R.id.menu_loginLogout) {
                    val action = HomeFragmentDirections.actionHomeFragmentToLoginFragment()
                    navCon.navigate(action)
            }

            true
        }

        binding.navigationView.itemIconTintList = null
        binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.tvAppVersion).text = "Version : ${BuildConfig.VERSION_NAME}"


        //**********************************************Starting Methods***************************************
        viewModel.getIncomeExpenseData()
        initDarkTheme()

    }

    private fun initPremiumBadge() {
        if(prefManager.isPremiumUser){
            binding.premiumBadge.visibility=View.VISIBLE
        }
        else{
            binding.premiumBadge.visibility=View.GONE
        }
    }

    private fun initCheckSubscription() {
        SubscriptionUtil.initBillingClient(requireContext().applicationContext)
    }

    private fun setLoginAndLogoutData() {

        val navigationView = view?.findViewById<View>(R.id.navigationView) as NavigationView

        val menu = navigationView.menu


        if (!prefManager.isLoggedIn) {
            menu.findItem(R.id.menu_loginLogout).title = "Login"
            menu.findItem(R.id.menu_loginLogout).isVisible = true
            menu.findItem(R.id.menu_profile).isVisible = false
        }
        else{
            menu.findItem(R.id.menu_loginLogout).isVisible = false
            menu.findItem(R.id.menu_profile).isVisible = true
        }
    }

    private fun imageSlider() {

        val sliderItems: MutableList<SliderItems> = ArrayList()
        /*    sliderItems.add(SliderItems(R.drawable.cover_1))
            sliderItems.add(SliderItems(R.drawable.cover_2))*/

        CoroutineScope(Dispatchers.IO).launch {
            BannerUtil.getAllActiveBanners(requireContext()) { bannerList: MutableList<Banner>? ->
                if (!bannerList.isNullOrEmpty()) {
                    val shuffledList = bannerList.shuffled().toMutableList()
                    binding.viewPagerImageSlider.adapter =
                        SliderAdapter(shuffledList, binding.viewPagerImageSlider, this@HomeFragment)
                } else {
                    val defaultList: MutableList<Banner> = mutableListOf()
                    val b1 = Banner("21-4-2022",
                        "",
                        "1",
                        "www.iem.com",
                        "https://firebasestorage.googleapis.com/v0/b/dailyexpensemanager-16f91.appspot.com/o/banner%2Fcover_1.png?alt=media&token=dc866b43-c46b-4ca9-8425-0e2bd62361d2",
                        "cover_1")
                    defaultList.add(b1)
                    binding.viewPagerImageSlider.adapter =
                        SliderAdapter(defaultList, binding.viewPagerImageSlider, this@HomeFragment)
                }

            }
        }


        binding.viewPagerImageSlider.clipToPadding = false
        binding.viewPagerImageSlider.clipChildren = false
        binding.viewPagerImageSlider.offscreenPageLimit = 3
        binding.viewPagerImageSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(30))

        compositePageTransformer.addTransformer { page, position ->
            val r: Float = 1 - (abs(position))
            page.scaleY = 0.85f + r * 0.15f
        }

        binding.viewPagerImageSlider.setPageTransformer(compositePageTransformer);
        binding.viewPagerImageSlider.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 4000) // slide duration 2 seconds
            }
        })

    }

    private fun InitCheckAppVersion() {
        if(System.currentTimeMillis()-prefManager.lastAppVersionRemoteConfigDataFetchTime>=Constants.INTERVAL_DAILY){
            if (NetworkCheck.isConnect(requireContext())) {
                //VersionControl.checkVersion(requireContext())
                    VersionControl.checkVersion(requireContext())
                    Log.d("appVersion", "VC checked")
            } else {
                Log.d("appVersion", "No Internet")
            }
        }
        else{
            Log.d("appVersion","App Version Will Check Tomorrow")
        }
    }

    private fun openAbout() {
        val dialog = Dialog(requireContext())
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_about, null)
        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.show()
        view.findViewById<View>(R.id.btn_ok_dilogue_about)
            .setOnClickListener { v: View? -> dialog.dismiss() }

        view.findViewById<View>(R.id.btn_rateApp_dilogue_about).setOnClickListener { v: View? ->
            dialog.dismiss()
            goToPlayStore(requireContext())
        }
    }

    private fun goback() {
        if (binding.drawerLayout.isOpen) {
            binding.drawerLayout.closeDrawers()
        } else {
            confirmQuit()
        }

    }

    private fun confirmQuit() {
        val dialog = Dialog(requireContext())
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_exit, null)
        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.show()
        view.findViewById<View>(R.id.btn_yes_alertImage_dialog_delete).setOnClickListener {
            dialog.dismiss()
            requireActivity().finishAffinity()
        }
        view.findViewById<View>(R.id.btn_no_alertImage_dialog_delete)
            .setOnClickListener { dialog.dismiss() }
    }

    private fun initDarkTheme() {
        if (prefManager.isDarkThemeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            binding.navigationView.menu.findItem(R.id.menu_DarkTheme).title = "Day Theme"
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding.navigationView.menu.findItem(R.id.menu_DarkTheme).title = "Dark Theme"
        }
    }

    private fun getAndSaveFCM() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("FCM", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d("FCM", "FCM is  -> ${task.result}")
            prefManager.fcmToken = token ?: ""
            if (!prefManager.isLoggedIn) {
                saveFCMInFirebase(token!!)
            } else {
                //FcmSave()
            }
            Log.d("FCM", "FCM is $token")

        })
    }

    companion object {
        val currentTime: String
            @SuppressLint("SimpleDateFormat")
            get() {
                val c = Calendar.getInstance()
                val df = SimpleDateFormat("yyyy-MM-dd")
                return df.format(c.time)
            }

        fun saveFCMInFirebase(fcm: String) {

        }

        fun goToPlayStore(context: Context) {
            val goToPlayStoreAppLnk: Intent = Intent(Intent.ACTION_VIEW)
            val appLink: Uri = Uri.parse(Constants.PLAY_STORE_APP_LINK)
            goToPlayStoreAppLnk.data = appLink
            context.startActivity(goToPlayStoreAppLnk)
        }
    }

    private fun initAdMob() {

        val lastAdShowDate = prefManager.lastBannerAdShownHomeF
        if (AdMobUtil.canAdShow(requireContext(), lastAdShowDate)) {
            Log.d(tag,"Banner Ad Home will load")

            binding.adView.visibility = View.VISIBLE
            MobileAds.initialize(requireContext()) {
                val bannerAdHelper = BannerAddHelper(requireContext().applicationContext)
                bannerAdHelper.loadBannerAd(binding.adView) {
                    if (it) {
                        prefManager.lastBannerAdShownHomeF = CurrentDate.currentTime24H
                    }
                }
            }
        } else {
            binding.adView.visibility = View.GONE
            Log.d(tag,"Banner Ad Home Not Shown")
        }
    }

    private fun setDate() {
        val simpleDayFormat = SimpleDateFormat("dd", Locale.getDefault())
        val simpleMonthFormat = SimpleDateFormat("MM", Locale.getDefault())
        val simpleYearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        day = simpleDayFormat.format(System.currentTimeMillis())
        month = simpleMonthFormat.format(System.currentTimeMillis())
        year = simpleYearFormat.format(System.currentTimeMillis())
        /*date = "$day-$month-$year"
        binding.tvDateDailyReport.text = date*/
    }

    override fun onDestroy() {
        Log.d("LifeCycle", "Home Fragment Destroyed")
        super.onDestroy()
    }

    override fun onDestroyView() {
        Log.d("LifeCycle", "Home Fragment DestroyedView")
        super.onDestroyView()
    }

    override fun onStop() {
        Log.d("LifeCycle", "Home Fragment Stop")
        super.onStop()
    }

    override fun onPause() {
        Log.d("LifeCycle", "Home Fragment Paused")
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    override fun onAttach(context: Context) {
        Log.d("LifeCycle", "Home Fragment Attached")
        super.onAttach(context)
    }

    override fun onDetach() {
        Log.d("LifeCycle", "Home Fragment Detached")
        super.onDetach()
    }

    override fun onStart() {
        Log.d("LifeCycle", "Home Fragment Started")
        super.onStart()
    }

    override fun onResume() {
        Log.d("LifeCycle", "Home Fragment resumed")
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 4000);
    }

    private val sliderRunnable =
        Runnable {
            binding.viewPagerImageSlider.currentItem = binding.viewPagerImageSlider.currentItem + 1
        }


    fun syncNow(context: Context) {
        //Log.d(SyncWorker.TAG, "One Time Work request for sync is scheduled")

        val constraints = Constraints.Builder().apply {
            setRequiredNetworkType(NetworkType.CONNECTED)
        }.build()

        val work = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .addTag("bannerSync")
            .build()

//        prefManager.SYNC=0

        val syncWorker = WorkManager.getInstance(context)
        syncWorker.enqueue(work)
    }


    private fun scheduleSyncTask() {

        if (!prefManager.isPeriodicSyncLaunched) {
            Log.d(PeriodicSyncWorker.TAG, "Periodic Sync Work Scheduled")

            val constraints = Constraints.Builder().apply {
                setRequiredNetworkType(NetworkType.CONNECTED)
            }.build()

            val workRequest = PeriodicWorkRequestBuilder<PeriodicSyncWorker>(60, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .addTag(PeriodicSyncWorker.TAG)
                .build()


            Log.d(PeriodicSyncWorker.TAG, "Periodic Work request for sync is scheduled")

            WorkManager.getInstance()
                .enqueueUniquePeriodicWork(
                    PeriodicSyncWorker.TAG,
                    ExistingPeriodicWorkPolicy.KEEP,
                    workRequest
                )

            prefManager.isPeriodicSyncLaunched=true

        }
    }



    fun getBackStackCount(){
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        val backStackEntryCount = navHostFragment?.childFragmentManager?.backStackEntryCount
        Log.d("backstack","count is -> $backStackEntryCount")
    }








}