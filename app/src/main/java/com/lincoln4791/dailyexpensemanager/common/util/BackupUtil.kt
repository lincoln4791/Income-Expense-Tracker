package com.lincoln4791.dailyexpensemanager.common.util

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.lincoln4791.dailyexpensemanager.MyApplication
import com.lincoln4791.dailyexpensemanager.common.Constants.BACKUP_RESTORE_ROLLBACK_FILE_NAME
import com.lincoln4791.dailyexpensemanager.common.Constants.DATABASE_NAME
import com.lincoln4791.dailyexpensemanager.common.Constants.FILE_NAME
import com.lincoln4791.dailyexpensemanager.common.Constants.MAXIMUM_DATABASE_FILE
import com.lincoln4791.dailyexpensemanager.common.Constants.SHAREDPREF
import com.lincoln4791.dailyexpensemanager.roomDB.AppDatabase
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

}