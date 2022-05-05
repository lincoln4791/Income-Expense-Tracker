package com.lincoln4791.dailyexpensemanager.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.mvvm_bilalkhan.ui.base.ViewModelFactory
import com.lincoln4791.dailyexpensemanager.network.RemoteDataSource

abstract class BaseFragment <VM:ViewModel, B : ViewBinding, R: BaseRepository> : Fragment() {

    protected lateinit var binding : B
    protected lateinit var viewModel : VM
    protected val remoteDataSource = RemoteDataSource

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val factory = ViewModelFactory(getFragmentRepository())
        viewModel = ViewModelProvider(this,factory)[getViewModel()]

        binding = getFragmentBinding(inflater,container)
        //binding = FragmentLoginBinding.inflate(layoutInflater,container,null)
        return binding.root
    }

    abstract fun getViewModel() : Class<VM>

    abstract fun getFragmentBinding(inflater : LayoutInflater, container: ViewGroup?) :B

    abstract fun getFragmentRepository() : R

}