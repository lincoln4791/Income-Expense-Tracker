package com.lincoln4791.dailyexpensemanager.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.lincoln4791.dailyexpensemanager.common.util.Util
import com.lincoln4791.dailyexpensemanager.common.util.GlobalVariabls
import com.lincoln4791.dailyexpensemanager.databinding.FragmentProfileBinding
import com.lincoln4791.dailyexpensemanager.viewModels.VM_Profile

class ProfileFragment : Fragment() {

    private lateinit var viewModel: VM_Profile
    private lateinit var binding : FragmentProfileBinding
    private lateinit var navCon : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    navCon.navigateUp()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Util.recordScreenEvent("profile_fragment","MainActivity")

        navCon = Navigation.findNavController(view)
        viewModel = ViewModelProvider(this)[VM_Profile::class.java]

        binding.cvImg.setOnClickListener {
            navCon.navigateUp()
        }

        binding.tvCurrentBalanceValueToolBarProfile.text = GlobalVariabls.currentBalance.toString()

    }

}