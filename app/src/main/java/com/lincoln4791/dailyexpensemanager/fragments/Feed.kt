package com.lincoln4791.dailyexpensemanager.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.lincoln4791.dailyexpensemanager.Adapters.Adapter_Quotes
import com.lincoln4791.dailyexpensemanager.Adapters.Adapter_Transactions
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.base.BaseFragment
import com.lincoln4791.dailyexpensemanager.databinding.FragmentFeedBinding
import com.lincoln4791.dailyexpensemanager.modelClass.QuoteModelClass.QuotesResponseModel
import com.lincoln4791.dailyexpensemanager.network.RetrofitClient
import com.lincoln4791.dailyexpensemanager.view.MainActivity
import com.lincoln4791.dailyexpensemanager.viewModels.VMFeed
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class Feed : BaseFragment<FragmentFeedBinding>(FragmentFeedBinding::inflate) {
    @Inject lateinit var layoutManager : LinearLayoutManager
    val viewModel by viewModels<VMFeed>()
    private lateinit var navCon : NavController
    lateinit var mainListAdapter: Adapter_Quotes


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
        navCon = Navigation.findNavController(view)
        showBottomNavigation()

        binding.cvImg.setOnClickListener { goBack() }

        setupList()
        setupView()

    }
    private fun setupList() {
        mainListAdapter = Adapter_Quotes()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = mainListAdapter
        }
    }

    private fun setupView() {
        lifecycleScope.launch {
            viewModel.listData.collect {
                Log.d("tag","data -> pagging data size ${it}")
                mainListAdapter.submitData(it)
            }
        }
    }

    private fun goBack(){
        navCon.navigateUp()
         (requireActivity() as MainActivity).markChipNavigationInHomeFragment()
        this.onDestroy()
        this.onDetach()
    }

    private fun showBottomNavigation(){
        (requireActivity() as MainActivity).showBottomNavigation()
    }

}