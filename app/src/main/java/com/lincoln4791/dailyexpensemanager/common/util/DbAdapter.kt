package com.lincoln4791.dailyexpensemanager.common.util

import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.roomDB.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

object DbAdapter {


    fun confirmDelete(context: Context,id: Int, amount: Int, typeOfTheFile: String,callback: (isDeleted: Boolean?) -> Unit) {
        val dialog = Dialog(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_delete, null)
        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.show()
        view.findViewById<View>(R.id.btn_yes_alertImage_dialog_delete).setOnClickListener {
            if ((typeOfTheFile == Constants.TYPE_INCOME)) {
                GlobalVariabls.currentBalance = GlobalVariabls.currentBalance - amount
            } else if ((typeOfTheFile == Constants.TYPE_EXPENSE)) {
                GlobalVariabls.currentBalance = GlobalVariabls.currentBalance + amount
            }
            dialog.dismiss()
            deleteData(context,id){
                if(it){
                    callback(true)
                    //loadTransactions()
                }
                else{
                    callback(false)
                    //Toast.makeText(requireContext(),"Something Went Wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
        view.findViewById<View>(R.id.btn_no_alertImage_dialog_delete)
            .setOnClickListener { dialog.dismiss() }
    }

    fun deleteData(context:Context,id: Int,callback : (isDeleted:Boolean)->Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                AppDatabase.getInstance(context.applicationContext).dbDao().delete(id.toString())
                Handler(Looper.getMainLooper()).post {
                    callback(true)
                }
            }
            catch (e: Exception){
                e.printStackTrace()
                callback(false)
            }
        }
    }

}