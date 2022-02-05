package com.lincoln4791.dailyexpensemanager.view

import android.Manifest
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.Spinner
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import android.os.Bundle
import com.lincoln4791.dailyexpensemanager.R
import android.content.Intent
import android.net.Uri

import android.view.LayoutInflater

import android.view.View

import android.view.WindowManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.databinding.ActivityMainBinding
import java.io.*
import android.os.Build




class MainActivity() : AppCompatActivity() {
   /* private lateinit var viewModel : VM_MainActivity*/
    private lateinit var binding : ActivityMainBinding
    private lateinit var navController : NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
/*        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)*/
        setContentView(binding.root)

    //**********************************************Initializations****************************************

        supportActionBar!!.hide()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.primary)
        }
        window.navigationBarColor = resources.getColor(R.color.primary)


        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
          navController = navHostFragment.navController
        //setupActionBarWithNavController(navController)

        /*viewModel = ViewModelProvider(this).get(VM_MainActivity::class.java)

        viewModel.totalIncome.observe(this, Observer {
            binding.tvTotalIncomeValueTopBarMainActivity.text = it!!.toString()
        })

        viewModel.totalExpense.observe(this, Observer {
            binding.tvTotalExpenseValueTopBarMainActivity.text = it!!.toString()
        })

        viewModel.currentBalance.observe(this, Observer {
            binding.tvCurrentBalanceValueToolBarMainActivity.text = it!!.toString()
            binding.tvBalanceValueTopBarMainActivity.text = it.toString()
        })*/



        //*************************************************** Click Listeners****************************************
      /*  binding.cvAddIncomeMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(this@MainActivity,
                AddIncome::class.java))
        })
        binding.cvAddExpensesMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(this@MainActivity,
                AddExpense::class.java))
        })
        binding.cvFullReportMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(this@MainActivity,
                FullReport::class.java))
        })
        binding.cvTransactionsMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            val transactionsIntent: Intent = Intent(this@MainActivity, Transactions::class.java)
            transactionsIntent.putExtra(Extras.TYPE, Constants.TYPE_ALL)
            startActivity(transactionsIntent)
        })
        binding.cvIncomeMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            val incomeIntent: Intent = Intent(this@MainActivity, Transactions::class.java)
            incomeIntent.putExtra(Extras.TYPE, Constants.TYPE_INCOME)
            startActivity(incomeIntent)
        })
        binding.cvExpensesMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            val incomeIntent: Intent = Intent(this@MainActivity, Transactions::class.java)
            incomeIntent.putExtra(Extras.TYPE, Constants.TYPE_EXPENSE)
            startActivity(incomeIntent)
        })
        binding.cvDailyMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(this@MainActivity,
                Daily::class.java))
        })
        binding.cvMonthlyMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(this@MainActivity,
                MonthlyReport::class.java))
        })
        binding.cvTotalIncomesTopBarMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            val incomeIntent: Intent = Intent(this@MainActivity, Transactions::class.java)
            incomeIntent.putExtra(Extras.TYPE, Constants.TYPE_INCOME)
            startActivity(incomeIntent)
        })
        binding.cvTotalExpensesTopBarMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            val expenseIntent: Intent = Intent(this@MainActivity, Transactions::class.java)
            expenseIntent.putExtra(Extras.TYPE, Constants.TYPE_EXPENSE)
            startActivity(expenseIntent)
        })*/
        //binding.cvAboutMainActivity.setOnClickListener { openAbout() }
        //binding.cvBackupDataMainActivity.setOnClickListener { Toast.makeText(this,"Coming Soon",Toast.LENGTH_SHORT).show() }
        //binding.cvRestoreDataMainActivity.setOnClickListener { Toast.makeText(this,"Coming Soon",Toast.LENGTH_SHORT).show() }


        //**********************************************Starting Methods***************************************
        //viewModel.getIncomeExpenseData()


    }


/*    override fun onBackPressed() {
        confirmLogout()
    }*/

    private fun confirmLogout() {
        val dialog = Dialog(this@MainActivity)
        val view = LayoutInflater.from(this@MainActivity).inflate(R.layout.dialog_exit, null)
        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.show()
        view.findViewById<View>(R.id.btn_yes_alertImage_dialog_delete).setOnClickListener {
            dialog.dismiss()
            finish()
            finishAffinity()
        }
        view.findViewById<View>(R.id.btn_no_alertImage_dialog_delete)
            .setOnClickListener { dialog.dismiss() }
    }

    private fun openAbout() {
        val dialog = Dialog(this@MainActivity)
        val view = LayoutInflater.from(this@MainActivity).inflate(R.layout.dialog_about, null)
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

/*    override fun onSupportNavigateUp(): Boolean {

        Log.d("tag","Back Prressed")
        val cl: calll = MonthlyFragment()
        cl.callMe()

        //return navController.navigateUp() || super.onSupportNavigateUp()
        return  true
    }*/

}
