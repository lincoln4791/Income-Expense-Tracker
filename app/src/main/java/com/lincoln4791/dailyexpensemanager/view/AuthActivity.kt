package com.lincoln4791.dailyexpensemanager.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lincoln4791.dailyexpensemanager.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)


    }
}