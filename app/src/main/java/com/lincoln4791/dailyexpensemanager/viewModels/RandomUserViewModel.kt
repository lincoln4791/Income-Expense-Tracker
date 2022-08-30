package com.lincoln4791.dailyexpensemanager.viewModels

import androidx.lifecycle.ViewModel
import com.lincoln4791.dailyexpensemanager.common.PrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RandomUserViewModel @Inject constructor (prefManager: PrefManager) : ViewModel() {

}