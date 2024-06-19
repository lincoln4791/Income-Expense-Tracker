package com.lincoln4791.dailyexpensemanager.fragments

import androidx.fragment.app.viewModels
import com.lincoln4791.dailyexpensemanager.base.BaseFragment
import com.lincoln4791.dailyexpensemanager.databinding.FragmentRandomUserBinding
import com.lincoln4791.dailyexpensemanager.viewModels.RandomUserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RandomUserFragment : BaseFragment<FragmentRandomUserBinding>(FragmentRandomUserBinding::inflate) {
    private val viewModel by viewModels<RandomUserViewModel>()



}