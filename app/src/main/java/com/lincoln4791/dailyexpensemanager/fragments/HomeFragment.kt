package com.lincoln4791.dailyexpensemanager.fragments


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import androidx.work.*
import com.lincoln4791.dailyexpensemanager.base.BaseFragment
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.itmedicus.patientaid.ads.admobAdsUpdated.AdMobUtil
import com.itmedicus.patientaid.ads.admobAdsUpdated.BannerAddHelper
import com.lincoln4791.dailyexpensemanager.BuildConfig
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.admobAdsUpdated.AdUnitIds
import com.lincoln4791.dailyexpensemanager.admobAdsUpdated.InterstistialAdHelper
import com.lincoln4791.dailyexpensemanager.background.worker.PeriodicSyncWorker
import com.lincoln4791.dailyexpensemanager.background.worker.SyncWorker
import com.lincoln4791.dailyexpensemanager.common.BannerUtil
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.common.Constants.DATABASE_NAME
import com.lincoln4791.dailyexpensemanager.common.PrefManager
import com.lincoln4791.dailyexpensemanager.common.SubscriptionUtil
import com.lincoln4791.dailyexpensemanager.common.eventbus.EventBusUtil
import com.lincoln4791.dailyexpensemanager.common.eventbus.MessageEvent
import com.lincoln4791.dailyexpensemanager.common.slider.SliderAdapter
import com.lincoln4791.dailyexpensemanager.common.util.*
import com.lincoln4791.dailyexpensemanager.common.util.BackupUtil.backupDatabaseForRestore
import com.lincoln4791.dailyexpensemanager.common.util.BackupUtil.deleteRestoreBackupFile
import com.lincoln4791.dailyexpensemanager.databinding.FragmentHomeBinding
import com.lincoln4791.dailyexpensemanager.modelClass.Banner
import com.lincoln4791.dailyexpensemanager.roomDB.AppDatabase
import com.lincoln4791.dailyexpensemanager.view.AuthActivity
import com.lincoln4791.dailyexpensemanager.view.MainActivity
import com.lincoln4791.dailyexpensemanager.viewModels.VMHomeFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.abs


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate), OnUserEarnedRewardListener {
    @Inject lateinit var sdf : SimpleDateFormat
    @Inject lateinit var prefManager: PrefManager
    @Inject lateinit var appDatabase: AppDatabase
    private var user : FirebaseUser?=null
    private val viewModel by viewModels<VMHomeFragment>()

    private lateinit var navCon: NavController
    private lateinit var rewardedAd: RewardedAd
    private lateinit var adLoadingBar: Dialog
    @Suppress("DEPRECATION", "DEPRECATION")
    private val sliderHandler: Handler = Handler()
    private lateinit var interAd: InterstistialAdHelper
    private var mInterstitialAd: InterstitialAd? = null

    //google drive
    private val dbPath = "/data/data/com.example.drivedbsync/databases/studentdb"
    private val dbPathShm = "/data/data/com.example.drivedbsync/databases/studentdb-shm"
    private val dbPathWal = "/data/data/com.example.drivedbsync/databases/studentdb-wal"




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



    @Suppress("DEPRECATION")
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("LifeCycle", "Home Fragment ViewCreated")
        unloadProgressBar()
        setDate()
        setLoginAndLogoutData()
        getAndSaveFCM()
        //Util.recordScreenEvent("home_fragment", "MainActivity")
        initAdMob()
        FirebaseUtil.fetchCommonDataFromRemoteConfig(requireContext())
        initCheckSubscription()
        InitCheckAppVersion()
        scheduleSyncTask()
        imageSlider()
        initPremiumBadge()
        Util.initAdRemoveByRewardAd(requireContext())
        initAdLoadingBar()
        initRewardAdView()
        //getDriveService()
        showBottomNavigation()
        initInAppReview(requireContext(),requireActivity())

        navCon = Navigation.findNavController(view)

        binding.cvAddIncomeMainActivity.setOnClickListener {
            hideBottomNavigation()
            val action = HomeFragmentDirections.actionHomeFragmentToAddIncomeFragment()
            navigateToFragment(action)
        }

        binding.cvAddExpensesMainActivity.setOnClickListener {
            hideBottomNavigation()
            val action = HomeFragmentDirections.actionHomeFragmentToAddExpenseFragment()
            navigateToFragment(action)
        }

        binding.cvFullReportMainActivity.setOnClickListener {
            hideBottomNavigation()
            val action = HomeFragmentDirections.actionHomeFragmentToFullReportFragment()
            navigateToFragment(action)
        }

        binding.cvTransactionsMainActivity.setOnClickListener {
            hideBottomNavigation()
            val action = HomeFragmentDirections.actionHomeFragmentToTransactionsFragment(Constants.TYPE_ALL)
            navigateToFragment(action)
        }

        binding.cvDailyMainActivity.setOnClickListener {
            hideBottomNavigation()
            val action = HomeFragmentDirections.actionHomeFragmentToDailyFragment()
         navigateToFragment(action)
        }
        binding.cvMonthlyMainActivity.setOnClickListener {
            hideBottomNavigation()
            val action = HomeFragmentDirections.actionHomeFragmentToMonthlyFragment(viewModel.year, viewModel.month)
           navigateToFragment(action)
        }


        binding.cvAboutMainActivity.setOnClickListener {
            openAbout()
            //syncNow(requireContext())
            getBackStackCount()
        }

        binding.cvBackupDataMainActivity.setOnClickListener {
            hideBottomNavigation()
            val action = HomeFragmentDirections.actionHomeFragmentToBackupFragment()
            navigateToFragment(action)
        }


        binding.ivNavMenu.setOnClickListener {
            binding.drawerLayout.open()
        }

        binding.lotteWatchAd.setOnClickListener {
            confirmWatchAd()
        }

        binding.toolbarMainActivity.setOnClickListener {
            Log.d("tag","Event Sent FromHome")
            EventBusUtil.postEvent("Event From Home")
            //EventBusUtil.postStickyEvent("Event From Home")
        }

        binding.navigationView.setNavigationItemSelectedListener {
            it.isChecked = true
            binding.drawerLayout.close()

            when (it.itemId) {
                R.id.menu_profile -> {
                    hideBottomNavigation()
                    val goToProfile = HomeFragmentDirections.actionHomeFragmentToProfileFragment()
                    navigateToFragment(goToProfile)
                }
                R.id.menu_NoAds -> {
                    hideBottomNavigation()
                    val action = HomeFragmentDirections.actionHomeFragmentToSubscription()
                    navigateToFragment(action)
                }
                R.id.menu_facebookPage -> {
                    Util.goToFacebookPage(requireContext())
                }
                R.id.menu_checkUpdate -> {
                    goToPlayStore(requireContext())
                }
                R.id.menu_rateUs -> {
                    goToPlayStore(requireContext())
                }
                R.id.menu_aboutUs -> {
                    openAbout()
                }
                R.id.menu_privacyPolicy -> {
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
                R.id.menu_shareApp -> {
                    val sharingIntent = Intent(Intent.ACTION_SEND)
                    sharingIntent.type = "text/plain"
                    val shareBodyText =
                        "Income Expense Manager - Your Daily Financial Calculator.\n\n" + "Download Income Expense Manager from google play:\n\n ${Constants.PLAY_STORE_APP_LINK}"
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT,
                        "Keep Track Of Your Daily Transactions.")
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText)
                    startActivity(Intent.createChooser(sharingIntent, "Share Income Expense Manager"))
                }
                R.id.menu_loginLogout -> {
                    if(prefManager.isLoggedIn){
                        if(NetworkCheck.isConnect(requireContext())){
                            showConfirmLogoutDialog()
                        }
                        else{
                            Util.showNoInternetDialog(requireContext()){

                            }
                        }
                    }
                    else{
                        navigateToAuthActivity()
                    }

                }
            }

            true
        }

        binding.navigationView.itemIconTintList = null
        binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.tvAppVersion).text = "Version : ${BuildConfig.VERSION_NAME}"

        viewModel.getIncomeExpenseData()
        initDarkTheme()
        observe()

        CommonUtils.test {a,b->
            Log.d("tag","cb is -> $b")
        }



    }


    private fun initAdLoadingBar() {
        adLoadingBar = Dialog(requireContext())
        val adLoadingBarView = layoutInflater.inflate(R.layout.content_ad_loading_bar, null, false)
        adLoadingBar.setContentView(adLoadingBarView)
        adLoadingBar.setCancelable(false)
    }


    private fun showConfirmLogoutDialog() {
        val dialog = Dialog(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_exit,null,false)
        dialogView.findViewById<TextView>(R.id.tv_alertImage_dialog_logout).text="Are you sure want to logout?"
        dialog.setContentView(dialogView)
        dialog.show()

        dialog.findViewById<Button>(R.id.btn_no_alertImage_dialog_delete).setOnClickListener { dialog.dismiss() }
        dialog.findViewById<Button>(R.id.btn_yes_alertImage_dialog_delete).setOnClickListener {
            dialog.dismiss()
            performLogout()
        }
    }

    private fun performLogout() {
        if(NetworkCheck.isConnect(requireContext())){
            try {
                FirebaseAuth.getInstance().signOut()
                Util.removeCommonUserData(requireContext())
                val action = HomeFragmentDirections.actionHomeFragmentSelf()
                navigateToFragment(action)
            }
            catch (e:Exception){
                e.printStackTrace()
            }

        }
        else{
            Util.showNoInternetDialog(requireContext()){

            }
        }

    }

    private fun observe() {
        viewModel.totalIncome.observe(viewLifecycleOwner) {
            binding.tvTotalIncomeValueTopBarMainActivity.text = it!!.toString()
        }

        viewModel.totalExpense.observe(viewLifecycleOwner) {
            binding.tvTotalExpenseValueTopBarMainActivity.text = it!!.toString()
        }

        viewModel.currentBalance.observe(viewLifecycleOwner) {
            binding.tvCurrentBalanceValueToolBarMainActivity.text = it!!.toString()
            binding.tvBalanceValueTopBarMainActivity.text = it.toString()
        }
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
        if(NetworkCheck.isConnect(requireContext())){
            user = Firebase.auth.currentUser
            if(user==null){
                Util.removeCommonUserData(requireContext())
            }
            else{
                Util.addCommonUserData(requireContext(),user!!)
            }
        }


        val navigationView = view?.findViewById<View>(R.id.navigationView) as NavigationView

        val menu = navigationView.menu


        if (prefManager.isLoggedIn) {
            menu.findItem(R.id.menu_loginLogout).title = "Logout"
            menu.findItem(R.id.menu_profile).isVisible = true
        }
        else{
            menu.findItem(R.id.menu_loginLogout).title = "Login"
            menu.findItem(R.id.menu_profile).isVisible = false
        }
    }

    private fun imageSlider() {
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

        binding.viewPagerImageSlider.setPageTransformer(compositePageTransformer)
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

    @SuppressLint("InflateParams")
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

    @SuppressLint("InflateParams")
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

    @Suppress("unused")
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


    @Suppress("unused")
    private fun initAdMob() {
        val lastAdShowDate = prefManager.lastBannerAdShownHomeF
        if (AdMobUtil.canAdShow(requireContext(), lastAdShowDate)) {
            Log.d("tag","Banner Ad Home will load")

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
            Log.d("tag","Banner Ad Home Not Shown")
        }
    }

    private fun setDate() {
        val simpleDayFormat = SimpleDateFormat("dd", Locale.getDefault())
        val simpleMonthFormat = SimpleDateFormat("MM", Locale.getDefault())
        val simpleYearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        viewModel.day = simpleDayFormat.format(System.currentTimeMillis())
        viewModel.month = simpleMonthFormat.format(System.currentTimeMillis())
        viewModel.year = simpleYearFormat.format(System.currentTimeMillis())
    }

    override fun onDestroy() {
        Log.d("LifeCycle", "Home Fragment Destroyed")
        super.onDestroy()
    }

    override fun onDestroyView() {
        Log.d("LifeCycle", "Home Fragment DestroyedView")
        super.onDestroyView()
    }

    override fun onPause() {
        Log.d("LifeCycle", "Home Fragment Paused")
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
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

    override fun onStop() {
        Log.d("LifeCycle", "Home Fragment Stop")
        super.onStop()
    }

    override fun onResume() {
        Log.d("LifeCycle", "Home Fragment resumed")
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 4000)
    }

    private val sliderRunnable =
        Runnable {
            binding.viewPagerImageSlider.currentItem = binding.viewPagerImageSlider.currentItem + 1
        }


    @Suppress("unused")
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

            @Suppress("DEPRECATION")
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


    private fun loadProgressBar() {
        binding.mainLoadingBar.visibility = View.VISIBLE
        binding.clContainer.visibility=View.GONE
    }

    private fun unloadProgressBar(){
        binding.mainLoadingBar.visibility = View.GONE
        binding.clContainer.visibility=View.VISIBLE
    }




    private fun confirmWatchAd(){
        val dialog = Dialog(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_confirm_watch_ad,null,false)
        dialog.setContentView(dialogView)
        dialog.show()

        dialog.findViewById<ImageView>(R.id.ivClose).setOnClickListener { dialog.dismiss() }
        dialog.findViewById<Button>(R.id.btnWatchAd).setOnClickListener {
            dialog.dismiss()
            adLoadingBar.show()
            //loadRewardedAd()
           initInterstitialAd()
        }
    }



    private fun navigateToFragment(action : NavDirections) {
        loadProgressBar()
        navCon.navigate(action)
    }

    private fun navigateToActivity(activity : Activity){

    }

    private fun navigateToAuthActivity(){
        startActivity(Intent(requireContext(),AuthActivity::class.java))
    }


    private fun loadRewardedAd() {
        Log.d("RewardAd", "load Reward Ad Called")
        // Use the test ad unit ID to load an ad.
        RewardedAd.load(requireContext(), AdUnitIds.REWARDED_AD_REMOVE,
            AdRequest.Builder().setHttpTimeoutMillis(Constants.REWARD_AD_TIMEOUT).build(), object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    Log.d("RewardAd", "onAdLoaded -> ${ad.responseInfo}")

                    rewardedAd.fullScreenContentCallback = object :
                        FullScreenContentCallback() {
                        /** Called when the ad failed to show full screen content.  */
                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            Log.d("RewardAd", "onAdFailedToShowFullScreenContent")
                        }

                        /** Called when ad showed the full screen content.  */
                        override fun onAdShowedFullScreenContent() {
                            Log.d("RewardAd", "onAdShowedFullScreenContent")
                            prefManager.lastRewardedAdASubscriptionShownTime=System.currentTimeMillis()
                            binding.lotteWatchAd.visibility=View.GONE
                        }

                        /** Called when full screen content is dismissed.  */
                        override fun onAdDismissedFullScreenContent() {
                            Log.d("RewardAd", "onAdDismissedFullScreenContent")

                        }
                    }
                    try {
                        adLoadingBar.dismiss()
                        showRewardedAD()
                        Log.d("RewardAd", "onAdDismissedFullScreenContent")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    try {
                        Log.e("RewardAd",
                            "onAdFailedToLoad -> ${loadAdError.message} :: code-> ${loadAdError.code}:: cause ${loadAdError.cause} :: response ${loadAdError.responseInfo}")
                        adLoadingBar.dismiss()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            })
    }

    fun showRewardedAD() {
        rewardedAd.show(requireActivity(), this@HomeFragment)
    }

    override fun onUserEarnedReward(p0: RewardItem) {
        Toast.makeText(requireContext(),"Thank you so much for your support",Toast.LENGTH_SHORT).show()
    }

    private fun initRewardAdView(){
       //Log.d("tag","add time diff is -> ${System.currentTimeMillis()-prefManager.lastRewardedAdHomeShownTime} ::: ${prefManager.adInterval.toLong()}")
        if(AdMobUtil.canAdShow(requireContext(),prefManager.lastInterstitialAdShownHome)){
            binding.lotteWatchAd.visibility=View.VISIBLE
        }
        else{
            binding.lotteWatchAd.visibility=View.GONE
        }
    }


    private fun initInterstitialAd() {
        interAd = InterstistialAdHelper(requireContext(), requireActivity(), mInterstitialAd)
        interAd.loadinterAd(AdUnitIds.INTERSTITIAL_FULL_REPORT) {
            Log.d("InterAd", "Inter ad loaded -> $it")
            adLoadingBar.dismiss()
            showInterAd()
        }

    }


    private fun showInterAd() {
            interAd.showInterAd { isShown: Boolean, error: String? ->
                if (isShown) {
                    Log.d("InterAD", "InterAd has been shown")
                    prefManager.lastInterstitialAdShownHome = CurrentDate.currentTime24H
                    binding.lotteWatchAd.visibility=View.GONE
                }
            }

    }

    private fun hideBottomNavigation(){
        (requireActivity() as MainActivity).hideBottomNavigation()
    }

    private fun showBottomNavigation(){
        (requireActivity() as MainActivity).showBottomNavigation()
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
            val goToPlayStoreAppLnk = Intent(Intent.ACTION_VIEW)
            val appLink: Uri = Uri.parse(Constants.PLAY_STORE_APP_LINK)
            goToPlayStoreAppLnk.data = appLink
            context.startActivity(goToPlayStoreAppLnk)
        }
    }


    private fun initInAppReview(context: Context,activity: Activity){
/*        val manager = ReviewManagerFactory.create(context)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("tag","in app successfull")
                // In-app review is supported, continue with the flow
                val reviewInfo = task.result
                val flow = manager.launchReviewFlow(activity, reviewInfo)
                flow.addOnCompleteListener { _ ->
                    // The in-app review flow has finished, handle the result if needed
                    Log.d("tag","in app completed")
                }
            } else {
                // In-app review is not supported, handle the error
                Log.d("tag","in app Failed")
            }
        }*/
    }
}