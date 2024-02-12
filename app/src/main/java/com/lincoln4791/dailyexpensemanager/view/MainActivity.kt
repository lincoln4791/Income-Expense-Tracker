package com.lincoln4791.dailyexpensemanager.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity() : AppCompatActivity() {
   /* private lateinit var viewModel : VM_MainActivity*/
    private lateinit var binding : ActivityMainBinding
    private lateinit var navController : NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(binding.root)

        //supportActionBar!!.hide()
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.navigationBarColor = resources.getColor(R.color.white)


        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
          navController = navHostFragment.navController

        setUpChipNavigationBar()

    }

    private fun setUpChipNavigationBar() {
        markChipNavigationInHomeFragment()
        binding.chipNavigationBar.setOnItemSelectedListener {
            when(it){
                R.id.home-> {
                    navController.navigateUp()
                    navController.navigate(R.id.homeFragment)
                }
                R.id.feed-> {
                    navController.navigateUp()
                    navController.navigate(R.id.feedFragment)
                }
                R.id.helpDesk-> {
                    navController.navigateUp()
                    navController.navigate(R.id.helpdeskFragment)
                }
                R.id.tools-> {
                    navController.navigateUp()
                    navController.navigate(R.id.toolsFragment)
                }
            }
        }
    }

    fun markChipNavigationInHomeFragment() {
        binding.chipNavigationBar.setItemSelected(R.id.home,true)
    }


    @SuppressLint("InflateParams")
    private fun confirmLogout() {
        val dialog = Dialog(this@MainActivity)
        val view = LayoutInflater.from(this@MainActivity).inflate(R.layout.dialog_exit, null)
        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.show()
        view.findViewById<View>(R.id.btn_yes_alertImage_dialog_delete).setOnClickListener {
            dialog.dismiss()
            finish()
            finishAffinity()
        }
        view.findViewById<View>(R.id.btn_no_alertImage_dialog_delete)
            .setOnClickListener { dialog.dismiss() }
    }

    @SuppressLint("InflateParams")
    private fun openAbout() {
        val dialog = Dialog(this@MainActivity)
        val view = LayoutInflater.from(this@MainActivity).inflate(R.layout.dialog_about, null)
        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.show()
        view.findViewById<View>(R.id.btn_ok_dilogue_about)
            .setOnClickListener { v: View? -> dialog.dismiss() }

        view.findViewById<View>(R.id.btn_rateApp_dilogue_about).setOnClickListener { v: View? ->
            dialog.dismiss()
            val goToPlayStoreAppLnk: Intent = Intent(Intent.ACTION_VIEW)
            val appLink: Uri = Uri.parse(Constants.PLAY_STORE_APP_LINK)
            goToPlayStoreAppLnk.data = appLink
            startActivity(goToPlayStoreAppLnk)
        }
    }

    fun hideBottomNavigation(){
        binding.chipNavigationBar.visibility = View.GONE
    }

    fun showBottomNavigation(){
        binding.chipNavigationBar.visibility = View.VISIBLE
    }

    override fun onSupportNavigateUp(): Boolean {

        Log.d("tag","Back Pressed")

        return navController.navigateUp() || super.onSupportNavigateUp()
    }


/*    private fun loadFragment(fragment: Fragment) { // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }*/

}
