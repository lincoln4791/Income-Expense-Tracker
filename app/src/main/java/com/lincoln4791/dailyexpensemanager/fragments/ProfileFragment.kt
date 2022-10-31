package com.lincoln4791.dailyexpensemanager.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.lincoln4791.dailyexpensemanager.base.BaseFragment
import com.lincoln4791.dailyexpensemanager.common.PrefManager
import com.lincoln4791.dailyexpensemanager.common.util.Util
import com.lincoln4791.dailyexpensemanager.common.util.GlobalVariabls
import com.lincoln4791.dailyexpensemanager.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    @Inject lateinit var prefManager : PrefManager
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


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Util.recordScreenEvent("profile_fragment","MainActivity")
        navCon = Navigation.findNavController(view)

        binding.cvImg.setOnClickListener {
            navCon.navigateUp()
        }

        binding.tvCurrentBalanceValueToolBarProfile.text = GlobalVariabls.currentBalance.toString()

        binding.name.text = "Name : ${prefManager.name}"
        binding.phone.text = "Phone : ${prefManager.phone}"
        binding.email.text = "Email : ${prefManager.email}"

    }

}