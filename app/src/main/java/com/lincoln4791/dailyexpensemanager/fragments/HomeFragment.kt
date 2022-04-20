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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.itmedicus.patientaid.ads.admobAdsUpdated.AdMobUtil
import com.itmedicus.patientaid.ads.admobAdsUpdated.BannerAddHelper
import com.itmedicus.patientaid.utils.CurrentDate
import com.itmedicus.patientaid.utils.DayDifference.Companion.getDaysDifference
//import com.lincoln4791.dailyexpensemanager.BuildConfig
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.common.PrefManager
import com.lincoln4791.dailyexpensemanager.common.slider.SliderAdapter
import com.lincoln4791.dailyexpensemanager.common.slider.SliderItems
import com.lincoln4791.dailyexpensemanager.common.util.FirebaseUtil
import com.lincoln4791.dailyexpensemanager.common.util.NetworkCheck
import com.lincoln4791.dailyexpensemanager.common.util.Util
import com.lincoln4791.dailyexpensemanager.common.util.VersionControl
import com.lincoln4791.dailyexpensemanager.databinding.FragmentHomeBinding
import com.lincoln4791.dailyexpensemanager.viewModels.VM_MainActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs


class HomeFragment : Fragment() {
    private val sdf = SimpleDateFormat("yyyy-MM-dd")

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: VM_MainActivity
    private lateinit var navCon: NavController
    private lateinit var prefManager: PrefManager
    private var day: String? = null
    private var month: String? = null
    private var year: String? = null
    private val sliderHandler: Handler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("LifeCycle","Home Fragment Create")

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
        Log.d("LifeCycle","Home Fragment CreateView")
        binding = FragmentHomeBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prefManager = PrefManager(requireContext())
        super.onViewCreated(view, savedInstanceState)
        Log.d("LifeCycle","Home Fragment ViewCreated")
        val window = requireActivity().window
        //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(R.color.primary)

        setDate()
        getAndSaveFCM()
        Util.recordScreenEvent("home_fragment", "MainActivity")
        FirebaseUtil.fetchDataFromRemoteConfig(requireContext())
        InitCheckAppVersion()
        initAdMob()
        imageSlider()

        navCon = Navigation.findNavController(view)
        viewModel = ViewModelProvider(this)[VM_MainActivity::class.java]


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
            this.onDestroy()
            this.onDetach()

        }

        binding.cvIncomeMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            /*val transactionsIntent = Intent(requireContext(), TransactionsActivity::class.java)
            transactionsIntent.putExtra(Extras.TYPE, Constants.TYPE_INCOME)
            startActivity(transactionsIntent)*/
            val action =
                HomeFragmentDirections.actionHomeFragmentToTransactionsFragment(Constants.TYPE_INCOME)
            navCon.navigate(action)
            this.onDestroy()
            this.onDetach()
        })
        binding.cvExpensesMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            /*  val transactionsIntent = Intent(requireContext(), TransactionsActivity::class.java)
            transactionsIntent.putExtra(Extras.TYPE, Constants.TYPE_EXPENSE)
            startActivity(transactionsIntent)*/
            val action =
                HomeFragmentDirections.actionHomeFragmentToTransactionsFragment(Constants.TYPE_EXPENSE)
            navCon.navigate(action)
            this.onDestroy()
            this.onDetach()
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
        }
        binding.cvBackupDataMainActivity.setOnClickListener {
            Toast.makeText(requireContext().applicationContext, "Coming Soon", Toast.LENGTH_SHORT)
                .show()
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
            } else if (it.itemId == R.id.menu_DarkTheme) {
                Toast.makeText(requireContext().applicationContext,
                    "Coming Soon",
                    Toast.LENGTH_SHORT).show()
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
            } else if (it.itemId == R.id.menu_loginLogout) {
                Toast.makeText(requireContext().applicationContext,
                    "Coming Soon",
                    Toast.LENGTH_SHORT).show()
            }

            true
        }

        binding.navigationView.itemIconTintList = null
       // binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.tvAppVersion).text = "Version : ${BuildConfig.VERSION_NAME}"


        //**********************************************Starting Methods***************************************
        viewModel.getIncomeExpenseData()
        initDarkTheme()

    }

    private fun imageSlider() {

        val sliderItems: MutableList<SliderItems> = ArrayList()
        sliderItems.add(SliderItems(R.drawable.cover_1))
        sliderItems.add(SliderItems(R.drawable.cover_2))

        binding.viewPagerImageSlider.adapter = SliderAdapter(sliderItems, binding.viewPagerImageSlider,this@HomeFragment)

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
        if (NetworkCheck.isConnect(requireContext())) {
            //VersionControl.checkVersion(requireContext())
            val lastVersionControlCheckDate = sdf.parse(prefManager.versionControlCheckLastDate)
            if (getDaysDifference(lastVersionControlCheckDate, sdf.parse(currentTime)) >= 1) {
                VersionControl.checkVersion(requireContext())
                Log.d("appVersion", "VC checked")
            } else {
                Log.d("appVersion", "VC will be checked tomorrow")
            }
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
            if (!prefManager.isUserLoggedIn) {
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
            binding.adView.visibility = View.VISIBLE
            MobileAds.initialize(requireContext()) {
                val bannerAdHelper = BannerAddHelper(requireContext())
                bannerAdHelper.loadBannerAd(binding.adView) {
                    if (it) {
                        prefManager.lastBannerAdShownHomeF = CurrentDate.currentTime24H
                    }
                }
            }
        }
        else{
            binding.adView.visibility = View.GONE
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
        Runnable { binding.viewPagerImageSlider.currentItem = binding.viewPagerImageSlider.currentItem + 1 }



}