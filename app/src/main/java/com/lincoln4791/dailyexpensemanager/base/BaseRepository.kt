package com.lincoln4791.dailyexpensemanager.base
import com.lincoln4791.dailyexpensemanager.network.SafeApiCall

abstract class BaseRepository() : SafeApiCall {
    // All Common repo calls will be added here , so that extended repo can get those common method
}