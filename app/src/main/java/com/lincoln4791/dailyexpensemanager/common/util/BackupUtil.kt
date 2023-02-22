package com.lincoln4791.dailyexpensemanager.common.util

import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.common.internal.service.Common
import com.google.firebase.crashlytics.internal.common.CommonUtils
import com.lincoln4791.dailyexpensemanager.MyApplication
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.common.Constants.BACKUP_RESTORE_ROLLBACK_FILE_NAME
import com.lincoln4791.dailyexpensemanager.common.Constants.DATABASE_NAME
import com.lincoln4791.dailyexpensemanager.common.Constants.FILE_NAME
import com.lincoln4791.dailyexpensemanager.common.Constants.MAXIMUM_DATABASE_FILE
import com.lincoln4791.dailyexpensemanager.common.Constants.SHAREDPREF
import com.lincoln4791.dailyexpensemanager.common.PrefManager
import com.lincoln4791.dailyexpensemanager.roomDB.AppDatabase
import de.hdodenhof.circleimageview.CircleImageView
import java.io.*
import java.nio.channels.FileChannel


object BackupUtil {

/*

    fun backupDatabase(context: Context) {
        val appDatabase: AppDatabase = AppDatabase.getInstance(context)
        appDatabase.close()
        val dbfile: File = context.getDatabasePath(DATABASE_NAME)
        val sdir = File(context.getExternalFilesDir("ggwp"), "backup")
        val fileName: String = FILE_NAME + System.currentTimeMillis().toString()
        val sfpath: String = sdir.getPath() + File.separator.toString() + fileName
        if (!sdir.exists()) {
            sdir.mkdirs()
        } else {
            //Directory Exists. Delete a file if count is 5 already. Because we will be creating a new.
            //This will create a conflict if the last backup file was also on the same date. In that case,
            //we will reduce it to 4 with the function call but the below code will again delete one more file.
            checkAndDeleteBackupFile(sdir, sfpath)
        }
        val savefile = File(sfpath)
        if (savefile.exists()) {
            Log.d("backup",
                "File exists. Deleting it and then creating new file.")
            savefile.delete()
        }
        try {
            if (savefile.createNewFile()) {
                val buffersize = 8 * 1024
                val buffer = ByteArray(buffersize)
                var bytes_read = buffersize
                val savedb: OutputStream = FileOutputStream(sfpath)
                val indb: InputStream = FileInputStream(dbfile)
                while (indb.read(buffer, 0, buffersize).also { bytes_read = it } > 0) {
                    savedb.write(buffer, 0, bytes_read)
                }
                savedb.flush()
                indb.close()
                savedb.close()
                val sharedPreferences: SharedPreferences =
                    context.getSharedPreferences(SHAREDPREF, MODE_PRIVATE)
                sharedPreferences.edit().putString("backupFileName", fileName).apply()
                Toast.makeText(context,"backup success",Toast.LENGTH_SHORT).show()
                //updateLastBackupTime(sharedPreferences)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("backup", "ex: $e")
            Toast.makeText(context,"backup Failed -> ${e.message}",Toast.LENGTH_SHORT).show()
        }
    }

*/

/*
    fun checkAndDeleteBackupFile(directory: File, path: String?) {
        //This is to prevent deleting extra file being deleted which is mentioned in previous comment lines.
        val currentDateFile = File(path)
        var fileIndex = -1
        var lastModifiedTime = System.currentTimeMillis()
        if (!currentDateFile.exists()) {
            val files: Array<File> = directory.listFiles()
            if (files != null && files.size >= MAXIMUM_DATABASE_FILE) {
                for (i in files.indices) {
                    val file: File = files[i]
                    val fileLastModifiedTime: Long = file.lastModified()
                    if (fileLastModifiedTime < lastModifiedTime) {
                        lastModifiedTime = fileLastModifiedTime
                        fileIndex = i
                    }
                }
                if (fileIndex != -1) {
                    val deletingFile: File = files[fileIndex]
                    if (deletingFile.exists()) {
                        deletingFile.delete()
                    }
                }
            }
        }
    }
    */

    fun backupDatabaseForRestore(context: Context) {
        val dbfile: File = context.getDatabasePath(DATABASE_NAME)
        val sdir = File(context.getExternalFilesDir("ggwp"), "backup")
        val sfpath: String = sdir.getPath() + File.separator + BACKUP_RESTORE_ROLLBACK_FILE_NAME
        if (!sdir.exists()) {
            sdir.mkdirs()
        }
        val savefile = File(sfpath)
        if (savefile.exists()) {
            Log.d("backup",
                "Backup Restore - File exists. Deleting it and then creating new file.")
            savefile.delete()
        }
        try {
            if (savefile.createNewFile()) {
                val buffersize = 8 * 1024
                val buffer = ByteArray(buffersize)
                var bytes_read = buffersize
                val savedb: OutputStream = FileOutputStream(sfpath)
                val indb: InputStream = FileInputStream(dbfile)
                while (indb.read(buffer, 0, buffersize).also { bytes_read = it } > 0) {
                    savedb.write(buffer, 0, bytes_read)
                }
                savedb.flush()
                indb.close()
                savedb.close()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Log.d("backup", "ex for restore file: $e")
        }
    }


    @Throws(IOException::class)
    fun copyFile(fromFile: FileInputStream, toFile: FileOutputStream) {
        var fromChannel: FileChannel? = null
        var toChannel: FileChannel? = null
        try {
            fromChannel = fromFile.getChannel()
            toChannel = toFile.getChannel()
            fromChannel.transferTo(0, fromChannel.size(), toChannel)
        } finally {
            try {
                if (fromChannel != null) {
                    fromChannel.close()
                }
            } finally {
                if (toChannel != null) {
                    toChannel.close()
                }
            }
        }
    }


    fun deleteRestoreBackupFile(context: Context) {
        val directory = File(context.filesDir, "backup")
        val sfpath: String =
            directory.getPath() + File.separator + BACKUP_RESTORE_ROLLBACK_FILE_NAME
        //This is to prevent deleting extra file being deleted which is mentioned in previous comment lines.
        val restoreFile = File(sfpath)
        if (restoreFile.exists()) {
            restoreFile.delete()
        }
    }



    const val PREFS_FILENAME = "com.device.myapplication.prefs"
    //const val FOLDER_URI = "folder_uri"
    const val FOLDER_URI = "Income Expense Manager"

    @JvmStatic
    fun storeString(key: String, text: String) {
        val editor = MyApplication.instance.getSharedPreferences(PREFS_FILENAME, 0)!!.edit()
        editor.putString(key, text)
        editor.commit()
    }

    @JvmStatic
    fun getString(key: String, def:String): String {
        val text = MyApplication.instance.getSharedPreferences(PREFS_FILENAME, 0).getString(key, def)?:""
        return text
    }


     fun showBackUpFailedDialog(context: Context){
        try {
            val dialog  = Dialog(context)
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialogue_something_went_wrong,null,false)
            dialog.setContentView(dialogView)
            dialogView.findViewById<TextView>(R.id.tv_title_dialogue_somethingWentWrong).text="Backup Failed"
            dialogView.findViewById<TextView>(R.id.tv_msg).text="Something is wrong in creating backup file, please try again later!"
            dialog.show()

            dialogView.findViewById<Button>(R.id.btn_ok_dialogue_somethingWentWrong).setOnClickListener { dialog.dismiss() }

        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }




     fun showBackUpSuccessDialog(context: Context){
        try {
            val prefManager = PrefManager(context)
            prefManager.lastBackupTime=System.currentTimeMillis()
            val dialog  = Dialog(context)
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_successfull,null,false)
            dialog.setContentView(dialogView)
            dialogView.findViewById<TextView>(R.id.tv_title).text="Success"
            dialogView.findViewById<TextView>(R.id.tv_msg).text="Backup Created Successfully!"
            dialog.show()
            dialogView.findViewById<Button>(R.id.btn_ok).setOnClickListener { dialog.dismiss() }

        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }


    fun showRestoreSuccessDialog(context: Context,callback : ()->Unit){
        try {
            val prefManager = PrefManager(context)
            prefManager.lastBackupTime=System.currentTimeMillis()
            val dialog  = Dialog(context)
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_successfull,null,false)
            dialog.setContentView(dialogView)
            dialogView.findViewById<TextView>(R.id.tv_title).text="Success"
            dialogView.findViewById<TextView>(R.id.tv_msg).text="Database Restored Successfully!"
            dialog.show()
            dialogView.findViewById<Button>(R.id.btn_ok).setOnClickListener {
                dialog.dismiss()
                callback()
            }

        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun showRestoreFailedDialog(context: Context){
        try {
            val dialog  = Dialog(context)
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialogue_something_went_wrong,null,false)
            dialog.setContentView(dialogView)
            dialogView.findViewById<TextView>(R.id.tv_title_dialogue_somethingWentWrong).text="Restore Failed"
            dialogView.findViewById<TextView>(R.id.tv_msg).text="Something is wrong in restoring backup file, please try again later!"
            dialog.show()

            dialogView.findViewById<Button>(R.id.btn_ok_dialogue_somethingWentWrong).setOnClickListener { dialog.dismiss() }

        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun showNoBackupFileFoundInFirebaseDialog(context: Context){
        try {
            val dialog  = Dialog(context)
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialogue_something_went_wrong,null,false)
            dialog.setContentView(dialogView)
            dialogView.findViewById<TextView>(R.id.tv_title_dialogue_somethingWentWrong).text="Restore Failed"
            dialogView.findViewById<TextView>(R.id.tv_msg).text="backup file not found in google drive.\nmake sure you have the backup file in your google drive or you are logged in in with the correct google drive!"
            dialog.show()

            dialogView.findViewById<Button>(R.id.btn_ok_dialogue_somethingWentWrong).setOnClickListener { dialog.dismiss() }

        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }

    suspend fun getLastBackupTime(context: Context) : String{
        val prefManager = PrefManager(context)
        var date = "__-__-__"
        try {
            if(prefManager.lastBackupTime!=0.toLong()){
                date =CurrentDate.getDateFromMill(prefManager.lastBackupTime)
            }
        }
        catch (e:Exception){
            e.printStackTrace()
        }

        return date
    }

    fun showConfirmDriveBackupDialog(context: Context,callback:(action:Boolean)->Unit){
        val dialog = Dialog(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_common,null,false)
        val lotteAnimationView = dialogView.findViewById<LottieAnimationView>(R.id.lotteAnimationView)
        val tvTitle = dialogView.findViewById<TextView>(R.id.tv_title)
        val tvDescription = dialogView.findViewById<TextView>(R.id.tv_description)
        val btnClose = dialogView.findViewById<CircleImageView>(R.id.iv_close)
        val btnNo = dialogView.findViewById<Button>(R.id.btn_no)
        val btnYes = dialogView.findViewById<Button>(R.id.btn_yes)

        lotteAnimationView.setAnimation(R.raw.lotte_cloud_backup)
        tvTitle.text = "Google Drive Backup"
        tvDescription.text = "Are you sure want to Backup Your Data to Google Drive?\nYour current backup file will replace your previous backup file and previous backup file be permanently deleted."

        btnYes.setOnClickListener {
            dialog.dismiss()
            callback(true)
        }

        btnNo.setOnClickListener {
            dialog.dismiss()
            callback(false)
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
            callback(false)
        }



        dialog.setCancelable(false)
        dialog.setContentView(dialogView)
        try {
            dialog.show()
        }
        catch (e:Exception){e.printStackTrace()}

    }


    fun showConfirmLocalBackupDialog(context: Context,callback:(action:Boolean)->Unit){
        val dialog = Dialog(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_common,null,false)
        val lotteAnimationView = dialogView.findViewById<LottieAnimationView>(R.id.lotteAnimationView)
        val tvTitle = dialogView.findViewById<TextView>(R.id.tv_title)
        val tvDescription = dialogView.findViewById<TextView>(R.id.tv_description)
        val btnClose = dialogView.findViewById<CircleImageView>(R.id.iv_close)
        val btnNo = dialogView.findViewById<Button>(R.id.btn_no)
        val btnYes = dialogView.findViewById<Button>(R.id.btn_yes)

        lotteAnimationView.setAnimation(R.raw.lotte_local_backup)
        tvTitle.text = "Data Backup"
        tvDescription.text = "Are you sure want to Backup Your Data to your Mobile?"

        btnYes.setOnClickListener {
            dialog.dismiss()
            callback(true)
        }

        btnNo.setOnClickListener {
            dialog.dismiss()
            callback(false)
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
            callback(false)
        }



        dialog.setCancelable(false)
        dialog.setContentView(dialogView)
        try {
            dialog.show()
        }
        catch (e:Exception){e.printStackTrace()}

    }


    fun showConfirmDriveRestoreDialog(context: Context,callback:(action:Boolean)->Unit){
        val dialog = Dialog(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_common,null,false)
        val lotteAnimationView = dialogView.findViewById<LottieAnimationView>(R.id.lotteAnimationView)
        val tvTitle = dialogView.findViewById<TextView>(R.id.tv_title)
        val tvDescription = dialogView.findViewById<TextView>(R.id.tv_description)
        val btnClose = dialogView.findViewById<CircleImageView>(R.id.iv_close)
        val btnNo = dialogView.findViewById<Button>(R.id.btn_no)
        val btnYes = dialogView.findViewById<Button>(R.id.btn_yes)

        lotteAnimationView.setAnimation(R.raw.lotte_cloud_restore)
        tvTitle.text = "Google Drive Restore"
        tvDescription.text = "Are you sure want to Restore Your Data from Google Drive?\nAll your current data will be replaced by Google Drive backup data."

        btnYes.setOnClickListener {
            dialog.dismiss()
            callback(true)
        }

        btnNo.setOnClickListener {
            dialog.dismiss()
            callback(false)
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
            callback(false)
        }



        dialog.setCancelable(false)
        dialog.setContentView(dialogView)
        try {
            dialog.show()
        }
        catch (e:Exception){e.printStackTrace()}

    }


    fun showConfirmLocalRestoreDialog(context: Context,callback:(action:Boolean)->Unit){
        val dialog = Dialog(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_common,null,false)
        val lotteAnimationView = dialogView.findViewById<LottieAnimationView>(R.id.lotteAnimationView)
        val tvTitle = dialogView.findViewById<TextView>(R.id.tv_title)
        val tvDescription = dialogView.findViewById<TextView>(R.id.tv_description)
        val btnClose = dialogView.findViewById<CircleImageView>(R.id.iv_close)
        val btnNo = dialogView.findViewById<Button>(R.id.btn_no)
        val btnYes = dialogView.findViewById<Button>(R.id.btn_yes)

        lotteAnimationView.setAnimation(R.raw.lotte_local_restore)
        tvTitle.text = "Data Restore"
        tvDescription.text = "Are you sure want to Restore Your Data from a Backup file?\nAll your current data will be replaced by the selected backup file data."

        btnYes.setOnClickListener {
            dialog.dismiss()
            callback(true)
        }

        btnNo.setOnClickListener {
            dialog.dismiss()
            callback(false)
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
            callback(false)
        }



        dialog.setCancelable(false)
        dialog.setContentView(dialogView)
        try {
            dialog.show()
        }
        catch (e:Exception){e.printStackTrace()}

    }

}