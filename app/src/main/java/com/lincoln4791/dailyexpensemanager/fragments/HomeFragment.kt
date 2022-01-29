package com.lincoln4791.dailyexpensemanager.fragments

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.navigation.NavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.databinding.FragmentHomeBinding
import com.lincoln4791.dailyexpensemanager.view.MainActivity
import com.lincoln4791.dailyexpensemanager.viewModels.VM_MainActivity
import org.apache.commons.lang3.ClassUtils.getSimpleName


class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var viewModel: VM_MainActivity
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
                    goback()

                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = requireActivity().window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.primary)
        }

        firebaseAnalytics = Firebase.analytics
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME,"home_fragment")
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "Main Activity")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)


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
        })
        binding.cvAddExpensesMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            val action = HomeFragmentDirections.actionHomeFragmentToAddExpenseFragment()
            navCon.navigate(action)

        })
        binding.cvFullReportMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            val action = HomeFragmentDirections.actionHomeFragmentToFullReportFragment(  )
            navCon.navigate(action)
        })
        binding.cvTransactionsMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
       /*     val transactionsIntent: Intent = Intent(this@MainActivity, Transactions::class.java)
            transactionsIntent.putExtra(Extras.TYPE, Constants.TYPE_ALL)
            startActivity(transactionsIntent)*/
            val action = HomeFragmentDirections.actionHomeFragmentToTransactionsFragment(Constants.TYPE_ALL)
            navCon.navigate(action)
        })
        binding.cvIncomeMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
          /*  val incomeIntent: Intent = Intent(this@MainActivity, Transactions::class.java)
            incomeIntent.putExtra(Extras.TYPE, Constants.TYPE_INCOME)
            startActivity(incomeIntent)*/
            val action = HomeFragmentDirections.actionHomeFragmentToTransactionsFragment(Constants.TYPE_INCOME)
            navCon.navigate(action)
        })
        binding.cvExpensesMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
          /*  val incomeIntent: Intent = Intent(this@MainActivity, Transactions::class.java)
            incomeIntent.putExtra(Extras.TYPE, Constants.TYPE_EXPENSE)
            startActivity(incomeIntent)*/
            val action = HomeFragmentDirections.actionHomeFragmentToTransactionsFragment(Constants.TYPE_EXPENSE)
            navCon.navigate(action)
        })
        binding.cvDailyMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            val action = HomeFragmentDirections.actionHomeFragmentToDailyFragment()
            navCon.navigate(action)
        })
        binding.cvMonthlyMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
           /* startActivity(Intent(this@MainActivity,
                MonthlyReport::class.java))*/
            val action = HomeFragmentDirections.actionHomeFragmentToMonthlyFragment(null,null)
            navCon.navigate(action)
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
            Toast.makeText(context,"Coming Soon",Toast.LENGTH_SHORT).show()
        }
        binding.cvRestoreDataMainActivity.setOnClickListener { Toast.makeText(context,"Coming Soon",Toast.LENGTH_SHORT).show() }

        binding.ivNavMenu.setOnClickListener {
            binding.drawerLayout.open()
        }

        binding.navigationView.setNavigationItemSelectedListener {
            it.isChecked = true
            binding.drawerLayout.close()

            if (it.itemId == R.id.menu_profile){
                val goToProfile = HomeFragmentDirections.actionHomeFragmentToProfileFragment()
                navCon.navigate(goToProfile)
            }

            true
        }

        binding.navigationView.itemIconTintList = null
        //binding.navigationView.setNavigationItemSelectedListener(this)


        //**********************************************Starting Methods***************************************
        viewModel.getIncomeExpenseData()

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
            val goToPlayStoreAppLnk: Intent = Intent(Intent.ACTION_VIEW)
            val appLink: Uri = Uri.parse(Constants.PLAY_STORE_APP_LINK)
            goToPlayStoreAppLnk.data = appLink
            startActivity(goToPlayStoreAppLnk)
        }
    }


    private fun goback(){
        confirmQuit()
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

    companion object {

    }

}