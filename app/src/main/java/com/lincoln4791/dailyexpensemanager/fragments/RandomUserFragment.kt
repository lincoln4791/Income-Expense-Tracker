package com.lincoln4791.dailyexpensemanager.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.mybaseproject2.base.BaseFragment
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.databinding.FragmentRandomUserBinding
import com.lincoln4791.dailyexpensemanager.viewModels.RandomUserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RandomUserFragment : BaseFragment<FragmentRandomUserBinding>(FragmentRandomUserBinding::inflate) {
    private val viewModel by viewModels<RandomUserViewModel>()



}