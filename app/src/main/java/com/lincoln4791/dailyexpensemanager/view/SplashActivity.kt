package com.lincoln4791.dailyexpensemanager.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.view.animation.Animation
import android.widget.TextView
import android.os.Bundle
import com.lincoln4791.dailyexpensemanager.R
import android.content.Intent
import android.os.Build
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.lincoln4791.dailyexpensemanager.databinding.ActivitySplashBinding
import com.lincoln4791.dailyexpensemanager.view.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private var animation_iv: Animation? = null
    private val animation_tv: Animation? = null

    private lateinit var binding : ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.primary)
        }
        window.navigationBarColor = resources.getColor(R.color.primary)
        supportActionBar!!.hide()
        setContentView(binding.root)





        animation_iv = AnimationUtils.loadAnimation(this, R.anim.splash_animation)
        animation_iv!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        binding.ivSplash.startAnimation(animation_iv)
    }

    override fun onStart() {
        super.onStart()
    }
}