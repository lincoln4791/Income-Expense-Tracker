package com.lincoln4791.dailyexpensemanager.view

import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.lincoln4791.dailyexpensemanager.viewModels.VM_EditDataIncome
import android.os.Bundle
import com.lincoln4791.dailyexpensemanager.R
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import com.lincoln4791.dailyexpensemanager.view.MainActivity
import com.lincoln4791.dailyexpensemanager.common.UtilDB
import com.lincoln4791.dailyexpensemanager.common.NodeName
import android.text.TextUtils
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import com.lincoln4791.dailyexpensemanager.common.SQLiteHelper
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.util.Log
import android.view.View
import android.widget.*
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.databinding.ActivityEditDataIncomeBinding
import java.text.SimpleDateFormat
import java.util.*

class EditDataIncome : AppCompatActivity(), View.OnClickListener {

    var incomeDescription: String? = null
    var amount: String? = null
    private var ID: String? = null
    private var hour = 0
    private var minute = 0
    private var year = 0
    private var month = 0
    private var day = 0
    var am_pm: String? = null
    var hourInString: String? = null
    private var vm_EditDataIncome: VM_EditDataIncome? = null

    private lateinit var binding : ActivityEditDataIncomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDataIncomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //*************************************************Initializations*******************************************
        supportActionBar!!.hide()
        vm_EditDataIncome = ViewModelProviders.of(this).get(VM_EditDataIncome::class.java)
        val calendar = Calendar.getInstance()
        year = calendar[Calendar.YEAR]
        month = calendar[Calendar.MONTH]
        day = calendar[Calendar.DAY_OF_MONTH]
        val simpleHourFormat = SimpleDateFormat("hh")
        val simpleMinuteFormat = SimpleDateFormat("mm")
        hour = simpleHourFormat.format(System.currentTimeMillis()).toInt()
        minute = simpleMinuteFormat.format(System.currentTimeMillis()).toInt()


        //***************************************************Click Listeners**************************************
        binding.cvBackEditDataIncome.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(this@EditDataIncome,
                MainActivity::class.java))
        })

        binding.cvAmount500EditDataIncome.setOnClickListener(this)
        binding.cvAmount1000EditDataIncome.setOnClickListener(this)
        binding.cvAmount1500EditDataIncome.setOnClickListener(this)
        binding.cvAmount2000EditDataIncome.setOnClickListener(this)
        binding.cvAmount2500EditDataIncome.setOnClickListener(this)
        binding.cvAmount3000EditDataIncome.setOnClickListener(this)
        binding.cvAmount3500EditDataIncome.setOnClickListener(this)
        binding.cvAmount4000EditDataIncome.setOnClickListener(this)
        binding.cvAmount5000EditDataIncome.setOnClickListener(this)
        binding.cvAmount10000EditDataIncome.setOnClickListener(this)
        binding.cvAmount20000EditDataIncome.setOnClickListener(this)
        binding.cvAmount30000EditDataIncome.setOnClickListener(this)
        binding.cvAmount40000EditDataIncome.setOnClickListener(this)
        binding.cvAmount50000EditDataIncome.setOnClickListener(this)
        binding.cvAmount100000EditDataIncome.setOnClickListener(this)
        binding.cvAmount200000EditDataIncome.setOnClickListener(this)
        binding.cvAmount300000EditDataIncome.setOnClickListener(this)
        binding.cvAmount400000EditDataIncome.setOnClickListener(this)
        binding.cvAmount500000EditDataIncome.setOnClickListener(this)
        binding.cvSalaryEditDataIncome.setOnClickListener(this)
        binding.cvBusinessEditDataIncome.setOnClickListener(this)
        binding.cvHouseRentEditDataIncome.setOnClickListener(this)
        binding.cvOtherEditDataIncome.setOnClickListener(this)
        binding.cvSaveEditDataIncome.setOnClickListener(View.OnClickListener { v: View? -> updateIncome() })
        binding.tvDateTimeEditDataIncome.setOnClickListener(View.OnClickListener { v: View? -> changeDate() })
        binding.ivHomeToolbarEditDataIncome.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(this@EditDataIncome,
                MainActivity::class.java))
        })


        //************************************************Starting Methods*****************************************
        binding.tvCurrentBalanceValueToolBarEditDataIncome.setText(UtilDB.currentBalance.toString())
        observe()
        intentData
        setViewWIthIntentData()
    }

    private fun setViewWIthIntentData() {
        binding.tvDateTimeEditDataIncome.text = vm_EditDataIncome!!.dateTime
        binding.etAmountEditDataIncome.setText(amount)
        binding.etIncomeDescriptionEditDataIncome.setText(incomeDescription)
        if (vm_EditDataIncome!!.category == Constants.CATEGORY_SALARY) {
            markSalary()
        } else if (vm_EditDataIncome!!.category == Constants.CATEGORY_BUSINESS) {
            markBusiness()
        } else if (vm_EditDataIncome!!.category == Constants.CATEGORY_HOUSE_RENT) {
            markHouseRent()
        } else if (vm_EditDataIncome!!.category == Constants.CATEGORY_OTHER) {
            markOther()
        }
    }

    private val intentData: Unit
        get() {
            ID = intent.getStringExtra(NodeName.ID)
            incomeDescription = intent.getStringExtra(NodeName.POST_DESCRIPTION)
            amount = intent.getStringExtra(NodeName.POST_AMOUNT)
            vm_EditDataIncome!!.dateTime = intent.getStringExtra(NodeName.POST_DATE_TIME)
            vm_EditDataIncome!!.category = intent.getStringExtra(NodeName.POST_CATEGORY)
            vm_EditDataIncome!!.year = intent.getStringExtra(NodeName.POST_YEAR)
            vm_EditDataIncome!!.month = intent.getStringExtra(NodeName.POST_MONTH)
            vm_EditDataIncome!!.day = intent.getStringExtra(NodeName.POST_DAY)
            vm_EditDataIncome!!.time = intent.getStringExtra(NodeName.POST_TIME)
        }

    private fun updateIncome() {
        if (TextUtils.isEmpty(binding.etAmountEditDataIncome.text)) {
            binding.etAmountEditDataIncome.error = getString(R.string.AmontNeeded)
        } else if (TextUtils.isEmpty(vm_EditDataIncome!!.category)) {
            Toast.makeText(this, getString(R.string.Pleaseselectacatagory), Toast.LENGTH_SHORT)
                .show()
        } else {
            amount = binding.etAmountEditDataIncome!!.text.toString()
            incomeDescription = ""
            if (!TextUtils.isEmpty(binding.etIncomeDescriptionEditDataIncome!!.text)) {
                incomeDescription = binding.etIncomeDescriptionEditDataIncome!!.text.toString()
            }
            val posts = MC_Posts(incomeDescription,
                vm_EditDataIncome!!.category,
                Constants.TYPE_INCOME,
                amount,
                vm_EditDataIncome!!.year,
                vm_EditDataIncome!!.month,
                vm_EditDataIncome!!.day,
                vm_EditDataIncome!!.time,
                System.currentTimeMillis().toString(),
                vm_EditDataIncome!!.dateTime)
            val helper = SQLiteHelper(this@EditDataIncome)
            helper.updateData(ID, posts)
            Toast.makeText(this, "Success ", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@EditDataIncome, MainActivity::class.java))
        }
    }

    private fun observe() {
        vm_EditDataIncome!!.mutable_category.observe(this, { s: String ->
            if (s == Constants.CATEGORY_SALARY) {
                markSalary()
            } else if (s == Constants.CATEGORY_BUSINESS) {
                markBusiness()
            } else if (s == Constants.CATEGORY_HOUSE_RENT) {
                markHouseRent()
            } else if (s == Constants.CATEGORY_OTHER) {
                markOther()
            }
        })
        vm_EditDataIncome!!.mutable_amount.observe(this, { s ->
            if (s == Constants.AMOUNT_500) {
                binding.etAmountEditDataIncome.setText(Constants.AMOUNT_500)
            } else if (s == Constants.AMOUNT_1000) {
                binding.etAmountEditDataIncome.setText(Constants.AMOUNT_1000)
            } else if (s == Constants.AMOUNT_1500) {
                binding.etAmountEditDataIncome.setText(Constants.AMOUNT_1500)
            } else if (s == Constants.AMOUNT_2000) {
                binding.etAmountEditDataIncome.setText(Constants.AMOUNT_2000)
            } else if (s == Constants.AMOUNT_2500) {
                binding.etAmountEditDataIncome.setText(Constants.AMOUNT_2500)
            } else if (s == Constants.AMOUNT_3000) {
                binding.etAmountEditDataIncome.setText(Constants.AMOUNT_3000)
            } else if (s == Constants.AMOUNT_4000) {
                binding.etAmountEditDataIncome.setText(Constants.AMOUNT_4000)
            } else if (s == Constants.AMOUNT_5000) {
                binding.etAmountEditDataIncome.setText(Constants.AMOUNT_5000)
            } else if (s == Constants.AMOUNT_10000) {
                binding.etAmountEditDataIncome.setText(Constants.AMOUNT_10000)
            } else if (s == Constants.AMOUNT_20000) {
                binding.etAmountEditDataIncome.setText(Constants.AMOUNT_20000)
            } else if (s == Constants.AMOUNT_30000) {
                binding.etAmountEditDataIncome.setText(Constants.AMOUNT_30000)
            } else if (s == Constants.AMOUNT_40000) {
                binding.etAmountEditDataIncome.setText(Constants.AMOUNT_40000)
            } else if (s == Constants.AMOUNT_50000) {
                binding.etAmountEditDataIncome.setText(Constants.AMOUNT_50000)
            } else if (s == Constants.AMOUNT_100000) {
                binding.etAmountEditDataIncome.setText(Constants.AMOUNT_100000)
            } else if (s == Constants.AMOUNT_200000) {
                binding.etAmountEditDataIncome.setText(Constants.AMOUNT_200000)
            } else if (s == Constants.AMOUNT_300000) {
                binding.etAmountEditDataIncome.setText(Constants.AMOUNT_300000)
            } else if (s == Constants.AMOUNT_400000) {
                binding.etAmountEditDataIncome.setText(Constants.AMOUNT_400000)
            } else if (s == Constants.AMOUNT_500000) {
                binding.etAmountEditDataIncome.setText(Constants.AMOUNT_500000)
            }
        })
    }

    override fun onClick(v: View) {
        if (v.id == R.id.cv_amount500_EditDataIncome) {
            vm_EditDataIncome!!.amount = Constants.AMOUNT_500
            vm_EditDataIncome!!.mutable_amount.setValue(vm_EditDataIncome!!.amount)
        } else if (v.id == R.id.cv_amount1000_EditDataIncome) {
            vm_EditDataIncome!!.amount = Constants.AMOUNT_1000
            vm_EditDataIncome!!.mutable_amount.setValue(vm_EditDataIncome!!.amount)
        } else if (v.id == R.id.cv_amount1500_EditDataIncome) {
            vm_EditDataIncome!!.amount = Constants.AMOUNT_1500
            vm_EditDataIncome!!.mutable_amount.setValue(vm_EditDataIncome!!.amount)
        } else if (v.id == R.id.cv_amount2000_EditDataIncome) {
            vm_EditDataIncome!!.amount = Constants.AMOUNT_2000
            vm_EditDataIncome!!.mutable_amount.setValue(vm_EditDataIncome!!.amount)
        } else if (v.id == R.id.cv_amount2500_EditDataIncome) {
            vm_EditDataIncome!!.amount = Constants.AMOUNT_2500
            vm_EditDataIncome!!.mutable_amount.setValue(vm_EditDataIncome!!.amount)
        } else if (v.id == R.id.cv_amount3000_EditDataIncome) {
            vm_EditDataIncome!!.amount = Constants.AMOUNT_3000
            vm_EditDataIncome!!.mutable_amount.setValue(vm_EditDataIncome!!.amount)
        } else if (v.id == R.id.cv_amount3500_EditDataIncome) {
            vm_EditDataIncome!!.amount = Constants.AMOUNT_3500
            vm_EditDataIncome!!.mutable_amount.setValue(vm_EditDataIncome!!.amount)
        } else if (v.id == R.id.cv_amount4000_EditDataIncome) {
            vm_EditDataIncome!!.amount = Constants.AMOUNT_4000
            vm_EditDataIncome!!.mutable_amount.setValue(vm_EditDataIncome!!.amount)
        } else if (v.id == R.id.cv_amount5000_EditDataIncome) {
            vm_EditDataIncome!!.amount = Constants.AMOUNT_5000
            vm_EditDataIncome!!.mutable_amount.setValue(vm_EditDataIncome!!.amount)
        } else if (v.id == R.id.cv_amount10000_EditDataIncome) {
            vm_EditDataIncome!!.amount = Constants.AMOUNT_10000
            vm_EditDataIncome!!.mutable_amount.setValue(vm_EditDataIncome!!.amount)
        } else if (v.id == R.id.cv_amount20000_EditDataIncome) {
            vm_EditDataIncome!!.amount = Constants.AMOUNT_20000
            vm_EditDataIncome!!.mutable_amount.setValue(vm_EditDataIncome!!.amount)
        } else if (v.id == R.id.cv_amount30000_EditDataIncome) {
            vm_EditDataIncome!!.amount = Constants.AMOUNT_30000
            vm_EditDataIncome!!.mutable_amount.setValue(vm_EditDataIncome!!.amount)
        } else if (v.id == R.id.cv_amount40000_EditDataIncome) {
            vm_EditDataIncome!!.amount = Constants.AMOUNT_40000
            vm_EditDataIncome!!.mutable_amount.setValue(vm_EditDataIncome!!.amount)
        } else if (v.id == R.id.cv_amount50000_EditDataIncome) {
            vm_EditDataIncome!!.amount = Constants.AMOUNT_50000
            vm_EditDataIncome!!.mutable_amount.setValue(vm_EditDataIncome!!.amount)
        } else if (v.id == R.id.cv_amount100000_EditDataIncome) {
            vm_EditDataIncome!!.amount = Constants.AMOUNT_100000
            vm_EditDataIncome!!.mutable_amount.setValue(vm_EditDataIncome!!.amount)
        } else if (v.id == R.id.cv_amount200000_EditDataIncome) {
            vm_EditDataIncome!!.amount = Constants.AMOUNT_200000
            vm_EditDataIncome!!.mutable_amount.setValue(vm_EditDataIncome!!.amount)
        } else if (v.id == R.id.cv_amount300000_EditDataIncome) {
            vm_EditDataIncome!!.amount = Constants.AMOUNT_300000
            vm_EditDataIncome!!.mutable_amount.setValue(vm_EditDataIncome!!.amount)
        } else if (v.id == R.id.cv_amount400000_EditDataIncome) {
            vm_EditDataIncome!!.amount = Constants.AMOUNT_400000
            vm_EditDataIncome!!.mutable_amount.setValue(vm_EditDataIncome!!.amount)
        } else if (v.id == R.id.cv_amount500000_EditDataIncome) {
            vm_EditDataIncome!!.amount = Constants.AMOUNT_500000
            vm_EditDataIncome!!.mutable_amount.setValue(vm_EditDataIncome!!.amount)
        } else if (v.id == R.id.cv_salary_EditDataIncome) {
            vm_EditDataIncome!!.category = Constants.CATEGORY_SALARY
            vm_EditDataIncome!!.mutable_category.setValue(vm_EditDataIncome!!.category)
        } else if (v.id == R.id.cv_business_EditDataIncome) {
            vm_EditDataIncome!!.category = Constants.CATEGORY_BUSINESS
            vm_EditDataIncome!!.mutable_category.setValue(vm_EditDataIncome!!.category)
        } else if (v.id == R.id.cv_houseRent_EditDataIncome) {
            vm_EditDataIncome!!.category = Constants.CATEGORY_HOUSE_RENT
            vm_EditDataIncome!!.mutable_category.setValue(vm_EditDataIncome!!.category)
        } else if (v.id == R.id.cv_other_EditDataIncome) {
            vm_EditDataIncome!!.category = Constants.CATEGORY_OTHER
            vm_EditDataIncome!!.mutable_category.value = vm_EditDataIncome!!.category
        }
    }

    private fun markOther() {
        binding.cvSalaryEditDataIncome.setCardBackgroundColor(getColor(R.color.white))
        binding.cvBusinessEditDataIncome.setCardBackgroundColor(getColor(R.color.white))
        binding.cvHouseRentEditDataIncome.setCardBackgroundColor(getColor(R.color.white))
        binding.cvOtherEditDataIncome.setCardBackgroundColor(getColor(R.color.green))
    }

    private fun markHouseRent() {
        binding.cvSalaryEditDataIncome.setCardBackgroundColor(getColor(R.color.white))
        binding.cvBusinessEditDataIncome.setCardBackgroundColor(getColor(R.color.white))
        binding.cvHouseRentEditDataIncome.setCardBackgroundColor(getColor(R.color.green))
        binding.cvOtherEditDataIncome.setCardBackgroundColor(getColor(R.color.white))
    }

    private fun markBusiness() {
        binding.cvSalaryEditDataIncome.setCardBackgroundColor(getColor(R.color.white))
        binding.cvBusinessEditDataIncome.setCardBackgroundColor(getColor(R.color.green))
        binding.cvHouseRentEditDataIncome.setCardBackgroundColor(getColor(R.color.white))
        binding.cvOtherEditDataIncome.setCardBackgroundColor(getColor(R.color.white))
    }

    private fun markSalary() {
        binding.cvSalaryEditDataIncome.setCardBackgroundColor(getColor(R.color.green))
        binding.cvBusinessEditDataIncome.setCardBackgroundColor(getColor(R.color.white))
        binding.cvHouseRentEditDataIncome.setCardBackgroundColor(getColor(R.color.white))
        binding.cvOtherEditDataIncome.setCardBackgroundColor(getColor(R.color.white))
    }

    private fun changeDate() {
        val timePickerDialog = TimePickerDialog(this@EditDataIncome,
            { view: TimePicker?, hourOfDay: Int, minute: Int ->
                vm_EditDataIncome!!.time = "$hourOfDay : $minute"
                Log.d("tag", "hour$hourOfDay")
                if (hourOfDay >= 12) {
                    am_pm = "pm"
                    hourInString = (hourOfDay - 12).toString()
                } else {
                    am_pm = "am"
                    hourInString = hourOfDay.toString()
                }

                //Log.d("tag","year"+vm_addIncome.year+" month "+vm_addIncome.month+" day "+vm_addIncome.day+" hour "+hourOfDay+"min "+minute+" "+am_pm);
                binding.tvDateTimeEditDataIncome.text =
                    vm_EditDataIncome!!.day + "-" + vm_EditDataIncome!!.month + "-" + vm_EditDataIncome!!.year + "  " + hourInString + ":" + minute.toString() + " " + am_pm
            },
            hour,
            minute,
            true)
        val datePickerDialog =
            DatePickerDialog(this@EditDataIncome, { view, year, month, dayOfMonth ->
                setDay(day)
                setMonth(month)
                if (day == 1) {
                    vm_EditDataIncome!!.day = getString(R.string.digit01)
                }
                vm_EditDataIncome!!.year = year.toString()
                vm_EditDataIncome!!.month = (month + 1).toString()
                vm_EditDataIncome!!.day = dayOfMonth.toString()
                timePickerDialog.show()
            }, year, month, day)
        datePickerDialog.show()
    }

    private fun setDay(day: Int) {
        if (day == 1) {
            vm_EditDataIncome!!.day = getString(R.string.digit01)
        } else if (day == 2) {
            vm_EditDataIncome!!.day = getString(R.string.digit02)
        } else if (day == 3) {
            vm_EditDataIncome!!.day = getString(R.string.digit03)
        } else if (day == 4) {
            vm_EditDataIncome!!.day = getString(R.string.digit04)
        } else if (day == 5) {
            vm_EditDataIncome!!.day = getString(R.string.digit05)
        } else if (day == 6) {
            vm_EditDataIncome!!.day = getString(R.string.digit06)
        } else if (day == 7) {
            vm_EditDataIncome!!.day = getString(R.string.digit07)
        } else if (day == 8) {
            vm_EditDataIncome!!.day = getString(R.string.digit08)
        } else if (day == 9) {
            vm_EditDataIncome!!.day = getString(R.string.digit09)
        } else {
            vm_EditDataIncome!!.day = day.toString()
        }
    }

    private fun setMonth(month: Int) {
        if (month == 1) {
            vm_EditDataIncome!!.month = getString(R.string.digit01)
        } else if (month == 2) {
            vm_EditDataIncome!!.month = getString(R.string.digit02)
        } else if (month == 3) {
            vm_EditDataIncome!!.month = getString(R.string.digit03)
        } else if (month == 4) {
            vm_EditDataIncome!!.month = getString(R.string.digit04)
        } else if (month == 5) {
            vm_EditDataIncome!!.month = getString(R.string.digit05)
        } else if (month == 6) {
            vm_EditDataIncome!!.month = getString(R.string.digit06)
        } else if (month == 7) {
            vm_EditDataIncome!!.month = getString(R.string.digit07)
        } else if (month == 8) {
            vm_EditDataIncome!!.month = getString(R.string.digit08)
        } else if (month == 9) {
            vm_EditDataIncome!!.month = getString(R.string.digit09)
        } else {
            vm_EditDataIncome!!.month = month.toString()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}