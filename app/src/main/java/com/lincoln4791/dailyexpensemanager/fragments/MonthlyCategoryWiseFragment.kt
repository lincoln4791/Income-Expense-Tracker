package com.lincoln4791.dailyexpensemanager.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.lincoln4791.dailyexpensemanager.Adapters.Adapter_MonthlyCategoryWiseReport
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.common.util.GlobalVariabls
import com.lincoln4791.dailyexpensemanager.databinding.FragmentMonthlyCategoryWiseBinding
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import com.lincoln4791.dailyexpensemanager.viewModels.VMMonthlyCategoryWise
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.lincoln4791.dailyexpensemanager.base.BaseFragment
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.common.util.DbAdapter
import com.lincoln4791.dailyexpensemanager.common.util.Util
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
@AndroidEntryPoint
class MonthlyCategoryWiseFragment : BaseFragment<FragmentMonthlyCategoryWiseBinding>(FragmentMonthlyCategoryWiseBinding::inflate) {
    @Inject lateinit var linearLayoutManager: LinearLayoutManager
    private val args: MonthlyCategoryWiseFragmentArgs by navArgs()
    private val viewModel by viewModels<VMMonthlyCategoryWise>()
    private lateinit var adapterMonthlyCategoryWiseReport: Adapter_MonthlyCategoryWiseReport
    private lateinit var navCon : NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LifeCycle", "MonthlyCategoryWise Fragment Create")
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    goBack()

                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

    }



    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        unloadProgressBar()
        Log.d("LifeCycle", "MonthlyCategoryWise Fragment ViewCreated")
        Util.recordScreenEvent("monthly_category_wise_fragment","MainActivity")
        navCon = Navigation.findNavController(view)
        initialization()
        setCategoryAndType()

        binding.cvImg.setOnClickListener {
            goBack()
        }

        viewModel.postsList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success<*> -> updateUI(it.value as List<MC_Posts>)
                is Resource.Failure -> {
                }
                else -> {}
            }
        }
        viewModel.loadYearMonthTypeCategoryWise(viewModel.year,viewModel.month,viewModel.type,viewModel.category)
    }

    private fun initialization() {
        viewModel.year = args.year
        viewModel.month = args.month
        viewModel.type = args.type
        viewModel.category = args.category
        viewModel.fragmentFrom = args.fragmentFrom?:""
        viewModel.selectedTransactionType = args.selectedTransactionType?:Constants.TYPE_ALL

        Log.d("tag","year -> ${viewModel.year}")
        Log.d("tag","year -> ${viewModel.month}")
        Log.d("tag","year -> ${viewModel.type}")
        Log.d("tag","year -> ${viewModel.category}")

        binding.tvTitle.text= "${viewModel.category} ${viewModel.type} of ${Util.getMonthNameFromMonthNumber(viewModel.month)}-${viewModel.year}"
        binding.tvCurrentBalanceValueToolBarMonthlyCategoryWiseReport.text = GlobalVariabls.currentBalance.toString()
    }


    private fun setCategoryAndType() {
        binding.tvCategoryMonthlyCategoryWise.text = viewModel.category
        if (viewModel.type == Constants.TYPE_INCOME) {
            binding.tvTypeMonthlyCategoryWise.text = getString(R.string.Incomes)
        } else if (viewModel.type == Constants.TYPE_EXPENSE) {
            binding.tvTypeMonthlyCategoryWise.text = getString(R.string.Expenses)
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun updateUI(postList:List<MC_Posts>){
        Log.d("tag", "listSIze " + postList.size)
        binding.rvCategoryWiseReport.layoutManager = linearLayoutManager
        adapterMonthlyCategoryWiseReport = Adapter_MonthlyCategoryWiseReport(postList,requireContext(),this)
        binding.rvCategoryWiseReport.adapter = adapterMonthlyCategoryWiseReport
        adapterMonthlyCategoryWiseReport.notifyDataSetChanged()
    }


    fun confirmDelete(id: Int, amount: Int, typeOfTheFile: String){
        DbAdapter.confirmDelete(requireContext(),id,amount,typeOfTheFile){
            if(it !=null){
                if(it){
                    viewModel.loadYearMonthTypeCategoryWise(viewModel.year,viewModel.month,viewModel.type,viewModel.category)
                }
                else{
                    Toast.makeText(requireContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show()
                }
            }

        }
    }


    private fun goBack() {

        if(viewModel.fragmentFrom==Constants.FRAGMENT_TRANSACTION){
            val transactionsAction = MonthlyCategoryWiseFragmentDirections.actionMonthlyCategoryWiseFragmentToTransactionsFragment(viewModel.selectedTransactionType)
            navCon.navigate(transactionsAction)
        }
        else{
            val action = MonthlyCategoryWiseFragmentDirections.actionMonthlyCategoryWiseFragmentToMonthlyFragment(viewModel.year,viewModel.month)
            navCon.navigate(action)
        }
        this.onDestroy()
        this.onDetach()
    }

    private fun loadProgressBar() {
        binding.mainLoadingBar.visibility = View.VISIBLE
        binding.clContainer.visibility=View.GONE
    }

    private fun unloadProgressBar(){
        binding.mainLoadingBar.visibility = View.GONE
        binding.clContainer.visibility=View.VISIBLE
    }


    override fun onDestroy() {
        Log.d("LifeCycle", "MonthlyCategoryWise Fragment Destroyed")
        super.onDestroy()
    }

    override fun onDestroyView() {
        Log.d("LifeCycle", "MonthlyCategoryWise Fragment DestroyedView")
        super.onDestroyView()
    }

    override fun onStop() {
        Log.d("LifeCycle", "MonthlyCategoryWise Fragment Stop")
        super.onStop()
    }

    override fun onPause() {
        Log.d("LifeCycle", "MonthlyCategoryWise Fragment Paused")
        super.onPause()
    }

    override fun onAttach(context: Context) {
        Log.d("LifeCycle", "MonthlyCategoryWise Fragment Attached")
        super.onAttach(context)
    }

    override fun onDetach() {
        Log.d("LifeCycle", "MonthlyCategoryWise Fragment Detached")
        super.onDetach()
    }

    override fun onStart() {
        Log.d("LifeCycle", "MonthlyCategoryWise Fragment Started")
        super.onStart()
    }

    override fun onResume() {
        Log.d("LifeCycle", "MonthlyCategoryWise Fragment resumed")
        super.onResume()
    }

}