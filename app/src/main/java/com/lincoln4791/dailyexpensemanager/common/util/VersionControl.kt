package com.lincoln4791.dailyexpensemanager.common.util

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.itmedicus.patientaid.utils.CurrentDate
import com.lincoln4791.dailyexpensemanager.BuildConfig
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.common.PrefManager
import com.lincoln4791.dailyexpensemanager.fragments.HomeFragment
import java.lang.Exception

object VersionControl {
    fun checkVersion(context : Context){
        Log.d("appVersion","Called")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val appVersion = dataSnapshot.value
                Log.d("appVersion","app -> $appVersion")

                if(BuildConfig.VERSION_NAME!=appVersion){
                    showNewVersionAvailableDialog(context)
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("appVersion", "loadPost:onCancelled", databaseError.toException())
            }
        }
        val ref= Firebase.database.reference.child(NodeNames.Common).child(NodeNames.AppVersionControl)
            .child(NodeNames.appVersion)
        ref.addListenerForSingleValueEvent(postListener)
    }

    fun showNewVersionAvailableDialog(context: Context){
        try {
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_new_version_available,null,false)
            val dialog = Dialog(context)
            dialog.setContentView(view)
            dialog.show()
            val prefManager = PrefManager(context)
            prefManager.versionControlCheckLastDate = CurrentDate.currentDate

            view.findViewById<ImageView>(R.id.ivClose).setOnClickListener { dialog.dismiss() }
            view.findViewById<Button>(R.id.btnUpdate).setOnClickListener {
                dialog.dismiss()
                HomeFragment.goToPlayStore(context)
            }
        }
        catch (e:Exception){
            e.printStackTrace()
        }

    }
}