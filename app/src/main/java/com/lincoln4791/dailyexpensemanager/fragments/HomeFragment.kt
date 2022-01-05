package com.lincoln4791.dailyexpensemanager.fragments

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.databinding.FragmentHomeBinding
import com.lincoln4791.dailyexpensemanager.viewModels.VM_MainActivity

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var viewModel: VM_MainActivity
    private lateinit var navCon : NavController


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
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            val action = HomeFragmentDirections.actionHomeFragmentToMonthlyFragment()
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