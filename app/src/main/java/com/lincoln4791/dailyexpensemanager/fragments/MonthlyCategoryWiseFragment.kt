package com.lincoln4791.dailyexpensemanager.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.lincoln4791.dailyexpensemanager.Adapters.Adapter_MonthlyCategoryWiseReport
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.common.SQLiteHelper
import com.lincoln4791.dailyexpensemanager.common.UtilDB
import com.lincoln4791.dailyexpensemanager.databinding.FragmentMonthlyCategoryWiseBinding
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import com.lincoln4791.dailyexpensemanager.viewModels.VM_MonthlyCategoryWise
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback


class MonthlyCategoryWiseFragment : Fragment() {
    private lateinit var adapter_monthlyCategoryWiseReport: Adapter_MonthlyCategoryWiseReport
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var year: String
    private lateinit var month: String
    private lateinit var type: String
    private lateinit var category: String

    val args: MonthlyCategoryWiseFragmentArgs by navArgs()

    private lateinit var viewModel: VM_MonthlyCategoryWise
    private lateinit var binding : FragmentMonthlyCategoryWiseBinding
    private lateinit var navCon : NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMonthlyCategoryWiseBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    Log.d("tag","OnBackPressCalled -> MonthlyCategoryWise")
                    //navCon.navigateUp()
                    val action = MonthlyCategoryWiseFragmentDirections.actionMonthlyCategoryWiseFragmentToMonthlyFragment()
                    navCon.navigate(action)

                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

      /*  val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            // Handle the back button event
            Log.d("tag","Backpress")
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)*/


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        navCon = Navigation.findNavController(view)
        viewModel = ViewModelProvider(this)[VM_MonthlyCategoryWise::class.java]

        year = args.year
        month = args.month
        type = args.type
        category = args.category

        linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        binding.rvCategoryWiseReport.layoutManager = linearLayoutManager


        binding.cvImg.setOnClickListener {
            navCon.navigateUp()
        }


        binding.tvCurrentBalanceValueToolBarMonthlyCategoryWiseReport.text = UtilDB.currentBalance.toString()
        setCategoryAndType()

        viewModel.postsList.observe(viewLifecycleOwner, Observer {
            when(it){
                //is Resource.Success -> adapter_transactions = Adapter_Transactions(it.data, this)
                is Resource.Success ->  updateUI(it.data)
                is Resource.Error -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        })

        viewModel.loadYearMonthTypeCategoryWise(year,month,type,category)

    }


    private fun setCategoryAndType() {
        binding.tvCategoryMonthlyCategoryWise.text = category
        if (type == Constants.TYPE_INCOME) {
            binding.tvTypeMonthlyCategoryWise.text = getString(R.string.Incomes)
        } else if (type == Constants.TYPE_EXPENSE) {
            binding.tvTypeMonthlyCategoryWise.text = getString(R.string.Expenses)
        }
    }




    private fun updateUI(postList:List<MC_Posts>){
        Log.d("tag", "listSIze " + postList!!.size)
        //Collections.reverse(vm_fullReport.postsList);
        adapter_monthlyCategoryWiseReport = Adapter_MonthlyCategoryWiseReport(postList,requireContext())
        binding.rvCategoryWiseReport.adapter = adapter_monthlyCategoryWiseReport
        adapter_monthlyCategoryWiseReport!!.notifyDataSetChanged()
    }

}