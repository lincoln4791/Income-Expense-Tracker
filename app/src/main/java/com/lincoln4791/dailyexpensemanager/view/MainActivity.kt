package com.lincoln4791.dailyexpensemanager.view

import android.Manifest
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.widget.TextView
import android.widget.Spinner
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import android.os.Bundle
import com.lincoln4791.dailyexpensemanager.R
import android.content.Intent
import android.net.Uri
import com.lincoln4791.dailyexpensemanager.view.AddIncome
import com.lincoln4791.dailyexpensemanager.view.AddExpense
import com.lincoln4791.dailyexpensemanager.view.FullReport
import com.lincoln4791.dailyexpensemanager.view.Transactions
import com.lincoln4791.dailyexpensemanager.common.Extras
import com.lincoln4791.dailyexpensemanager.view.Daily
import com.lincoln4791.dailyexpensemanager.view.MonthlyReport
import com.lincoln4791.dailyexpensemanager.common.SQLiteHelper
import com.lincoln4791.dailyexpensemanager.common.UtilDB
import android.view.LayoutInflater
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.PermissionListener
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.PermissionToken
import android.os.Environment
import android.os.AsyncTask
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.karumi.dexter.listener.PermissionRequest
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.databinding.ActivityMainBinding
import java.io.*
import java.lang.Exception
import java.util.ArrayList

class MainActivity() : AppCompatActivity() {

    private var totalIncome = 0
    private var totalExpenses = 0
    private val spinner: Spinner? = null
    private val iv_back: ImageView? = null
    private var postList: MutableList<MC_Posts>? = null

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //**********************************************Initializations****************************************
        postList = ArrayList()
        supportActionBar!!.hide()


        //*************************************************** Click Listeners****************************************
        binding.cvAddIncomeMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(this@MainActivity,
                AddIncome::class.java))
        })
        binding.cvAddExpensesMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(this@MainActivity,
                AddExpense::class.java))
        })
        binding.cvFullReportMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(this@MainActivity,
                FullReport::class.java))
        })
        binding.cvTransactionsMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            val transactionsIntent: Intent = Intent(this@MainActivity, Transactions::class.java)
            transactionsIntent.putExtra(Extras.TYPE, Constants.TYPE_ALL)
            startActivity(transactionsIntent)
        })
        binding.cvIncomeMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            val incomeIntent: Intent = Intent(this@MainActivity, Transactions::class.java)
            incomeIntent.putExtra(Extras.TYPE, Constants.TYPE_INCOME)
            startActivity(incomeIntent)
        })
        binding.cvExpensesMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            val incomeIntent: Intent = Intent(this@MainActivity, Transactions::class.java)
            incomeIntent.putExtra(Extras.TYPE, Constants.TYPE_EXPENSE)
            startActivity(incomeIntent)
        })
        binding.cvDailyMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(this@MainActivity,
                Daily::class.java))
        })
        binding.cvMonthlyMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(this@MainActivity,
                MonthlyReport::class.java))
        })
        binding.cvTotalIncomesTopBarMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            val incomeIntent: Intent = Intent(this@MainActivity, Transactions::class.java)
            incomeIntent.putExtra(Extras.TYPE, Constants.TYPE_INCOME)
            startActivity(incomeIntent)
        })
        binding.cvTotalExpensesTopBarMainActivity.setOnClickListener(View.OnClickListener { v: View? ->
            val expenseIntent: Intent = Intent(this@MainActivity, Transactions::class.java)
            expenseIntent.putExtra(Extras.TYPE, Constants.TYPE_EXPENSE)
            startActivity(expenseIntent)
        })
        binding.cvAboutMainActivity.setOnClickListener { openAbout() }
        binding.cvBackupDataMainActivity.setOnClickListener { backupData() }
        binding.cvRestoreDataMainActivity.setOnClickListener { restoreData() }


        //**********************************************Starting Methods***************************************
        setIncomeExpenseValues()
    }

    private fun setIncomeExpenseValues() {
        val sqLiteHelper = SQLiteHelper(this@MainActivity)
        val cursor = sqLiteHelper.loadAllTransactions()
        while (cursor.moveToNext()) {
            val postType = cursor.getString(3)
            val amount = cursor.getString(4).toInt()
            if ((postType == Constants.TYPE_INCOME)) totalIncome += amount else if ((postType == Constants.TYPE_EXPENSE)) totalExpenses += amount
        }
        UtilDB.currentBalance = totalIncome - totalExpenses
        binding.tvTotalIncomeValueTopBarMainActivity.text = totalIncome.toString()
        binding.tvTotalExpenseValueTopBarMainActivity.text = totalExpenses.toString()
        binding.tvBalanceValueTopBarMainActivity.text = UtilDB.currentBalance.toString()
        binding.tvCurrentBalanceValueToolBarMainActivity.text = UtilDB.currentBalance.toString()
        cursor.close()
    }

    override fun onBackPressed() {
        confirmLogout()
    }

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

    private fun restoreData() {
        Toast.makeText(this@MainActivity, "permission Checking", Toast.LENGTH_SHORT).show()
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                    Toast.makeText(this@MainActivity, "permission Granted", Toast.LENGTH_SHORT)
                        .show()
                    importCSV()
                }

                override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.parse("package:$packageName")
                    startActivity(intent)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissionRequest: PermissionRequest,
                    permissionToken: PermissionToken
                ) {
                    permissionToken.continuePermissionRequest()
                }
            }).onSameThread().check()
    }

    private fun backupData() {
        Toast.makeText(this, "checking ", Toast.LENGTH_SHORT).show()
        Dexter.withContext(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                    Toast.makeText(this@MainActivity, "Granted ", Toast.LENGTH_SHORT).show()
                    AllTransactionsTask().execute()
                }

                override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.parse("package:$packageName")
                    startActivity(intent)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissionRequest: PermissionRequest,
                    permissionToken: PermissionToken
                ) {
                    permissionToken.continuePermissionRequest()
                }
            }).onSameThread().check()
    }

    private fun exportCSV() {
        val inFilePath = applicationContext.getDatabasePath("MyDatabase").toString()
        val inFile = File(inFilePath)
        var fis: InputStream? = null
        var bf: BufferedReader? = null
        val outFile =
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "DEMDB.txt")
        var fos: FileOutputStream? = null
        try {
            fis = FileInputStream(inFile)
            fos = FileOutputStream(outFile)
            bf = BufferedReader(InputStreamReader(fis))
            val output = FileOutputStream(outFile)
            var line = ""
            while ((bf.readLine().also { line = it }) != null) {
                fos.write(line.toByteArray())
            }

            /*  // Transfer bytes from the input file to the output file
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }*/fos.close()
            Toast.makeText(this@MainActivity, "Backup Completed", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this@MainActivity,
                "Unable to backup database. Retry",
                Toast.LENGTH_SHORT).show()
            Log.d("tag", "Database Craet failed : " + e.message)
            e.printStackTrace()
        }
    }

    private fun importCSV() {
        val inFolder =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString()
        val inFileName = "DEMDB.txt"
        val inFile = File(inFolder, inFileName)
        val outFileName = getDatabasePath("MyDatabase").toString()
        val outFile = File(outFileName)
        //File outFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(),"DEMDB2.txt");
        var fis: FileInputStream? = null
        val bf: BufferedReader? = null
        var fos: OutputStream? = null
        if (inFile.exists()) {
            try {
                fis = FileInputStream(inFile)
                fos = FileOutputStream(outFile)
                /*                bf = new BufferedReader(new InputStreamReader(fis));

                String line;

                while((line = bf.readLine()) != null){
                    fos.write(line.getBytes());
                    Log.d("tag","inImport "+line);
                }*/
                val buffer = ByteArray(1024)
                var length: Int
                while ((fis.read(buffer).also { length = it }) > 0) {
                    fos.write(buffer, 0, length)
                    Log.d("tag", buffer.toString())
                }
                fos.flush()
                fos.close()
                fis.close()
                Toast.makeText(this, "Restore Complete", Toast.LENGTH_SHORT).show()


                /* CSVReader csvReader = new CSVReader(new FileReader(inFile.getAbsolutePath()));
                String[] nextLine;
                while((nextLine = csvReader.readNext()) != null){
                    int ID = Integer.parseInt(nextLine[0]);
                    String postDescription = nextLine[1];
                    String postCategory = nextLine[2];
                    String postType = nextLine[3];
                    String postAmount = nextLine[4];
                    String postYear = nextLine[5];
                    String postMonth = nextLine[6];
                    String postDay = nextLine[7];
                    String time = nextLine[8];
                    String timeStamp = nextLine[9];
                    String dateTime = nextLine[10];

                    MC_Posts mc_posts = new MC_Posts(ID,postDescription,postCategory,postType,postAmount,postYear,postMonth,postDay,time,timeStamp,dateTime);
                    SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
                    sqLiteHelper.saveData(mc_posts);
                }*/
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            Toast.makeText(this, "No Backup Found", Toast.LENGTH_SHORT).show()
        }
    }

    internal inner class AllTransactionsTask() : AsyncTask<Any?, Any?, Any?>() {
        override fun doInBackground(objects: Array<Any?>): Any? {
            fetchAllTransactions()
            return null
        }

        override fun onPostExecute(o: Any?) {
            super.onPostExecute(o)
            exportCSV()
        }
    }

    private fun fetchAllTransactions() {
        val sqLiteHelper = SQLiteHelper(this)
        val cursor = sqLiteHelper.loadAllTransactions()
        while (cursor.moveToNext()) {
            val ID = cursor.getInt(0)
            val postDescription = cursor.getString(1)
            val postCategory = cursor.getString(2)
            val postType = cursor.getString(3)
            val postAmount = cursor.getString(4)
            val postTime = cursor.getString(5)
            val postDay = cursor.getString(6)
            val postMonth = cursor.getString(7)
            val postYear = cursor.getString(8)
            val postDateTime = cursor.getString(9)
            val timeStamp = cursor.getString(10)
            val post = MC_Posts(ID,
                postDescription,
                postCategory,
                postType,
                postAmount,
                postYear,
                postMonth,
                postDay,
                postTime,
                timeStamp,
                postDateTime)
            postList!!.add(post)
        }
        cursor.close()
    }
}