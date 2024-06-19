package com.lincoln4791.dailyexpensemanager.common.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*
import java.net.URL


/**
 * Created by Soumik on 30,December,2020
 * ITmedicus,
 * Dhaka, Bangladesh.
 */
object FileUtils {

    private const val TAG = "FILE_UTILS"


    fun saveImageAndroid9(context: Context, mediaUrl: URL?, folderName: String, fName: String?) {
        var imgOpenStream: InputStream? = null

        try {
            imgOpenStream = mediaUrl!!.openStream()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "Open Failed: ${e.localizedMessage}")
        }



        val directory = File(Environment.getExternalStorageDirectory(), folderName)
        var fileName = ""
        if(!fName.isNullOrEmpty()){
             fileName = if (fName.contains(".jpg")) fName else "${fName}.jpg"
        }


        Log.d(TAG, "saveImage: File_Name: $fileName")

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val imgFile = File("${Environment.getExternalStorageDirectory()}/$folderName/${fileName}")

        if (imgFile.exists()) imgFile.delete()

        //image download

        try {
            val imgOutputStream = FileOutputStream(File(directory, fileName))

            imgOutputStream.use { outputStream ->
                val buffer = ByteArray(1024)
                var bytesRead = imgOpenStream!!.read(buffer, 0, buffer.size)
                while (bytesRead >= 0) {
                    outputStream.write(buffer, 0, bytesRead)
                    bytesRead = imgOpenStream.read(buffer, 0, buffer.size)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            imgFile.delete()
            Log.d(TAG, "Download Failed: ${e.localizedMessage}")
        }
    }


    suspend fun saveImageToAppSpecificStorageFromUrl(context: Context, fileName: String?,imageUrl : String?){
        CoroutineScope(Dispatchers.IO).launch {
            val outFileName = "$fileName.png"
            val outFile = File(context.getExternalFilesDir("IncomeExpenseManager"), outFileName)
            try{
                val stringUrl = URL(imageUrl)
                val bitmap =
                    BitmapFactory.decodeStream(
                        stringUrl.openStream())
                val fos = FileOutputStream(outFile)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.close()
                Log.d("tag", "save Done")
            }
            catch (e:IOException){
                if(outFile.exists()){
                    outFile.delete()
                }
            }

        }
    }


/*    private fun loadImage(context: Context,imageName : String) {
        val inFileName = "myImage.png"
        val inFile = File(context.getExternalFilesDir("MyImageFolder"), inFileName)
        val bitmap = BitmapFactory.decodeStream(FileInputStream(inFile))
        binding.iv.setImageBitmap(bitmap)

    }*/
}