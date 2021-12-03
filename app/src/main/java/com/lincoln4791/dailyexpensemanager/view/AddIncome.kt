package com.lincoln4791.dailyexpensemanager.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.lincoln4791.dailyexpensemanager.viewModels.VM_AddIncome
import android.os.Bundle
import com.lincoln4791.dailyexpensemanager.R
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import com.lincoln4791.dailyexpensemanager.view.MainActivity
import com.lincoln4791.dailyexpensemanager.common.UtilDB
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
import com.lincoln4791.dailyexpensemanager.databinding.ActivityAddIncomeBinding
import java.text.SimpleDateFormat
import java.util.*

class AddIncome : AppCompatActivity(), View.OnClickListener {

    private var hour = 0
    private var minute = 0
    private var year = 0
    private var month = 0
    private var day = 0
    var am_pm: String? = null
    var hourInString: String? = null
    private var vm_addIncome: VM_AddIncome? = null


    private lateinit var binding : ActivityAddIncomeBinding
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddIncomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //*************************************************Initializations*******************************************
        supportActionBar!!.hide()
        vm_addIncome = ViewModelProviders.of(this).get(VM_AddIncome::class.java)
        val calendar = Calendar.getInstance()
        year = calendar[Calendar.YEAR]
        month = calendar[Calendar.MONTH]
        day = calendar[Calendar.DAY_OF_MONTH]
        val simpleHourFormat = SimpleDateFormat("hh")
        val simpleMinuteFormat = SimpleDateFormat("mm")
        hour = simpleHourFormat.format(System.currentTimeMillis()).toInt()
        minute = simpleMinuteFormat.format(System.currentTimeMillis()).toInt()


        //***************************************************Click Listeners**************************************
        binding.cvBackAddIncome.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(this@AddIncome,
                MainActivity::class.java))
        })

        binding.cvAmount500AddIncome.setOnClickListener(this)
        binding.cvAmount1000AddIncome.setOnClickListener(this)
        binding.cvAmount1500AddIncome.setOnClickListener(this)
        binding.cvAmount2000AddIncome.setOnClickListener(this)
        binding.cvAmount2500AddIncome.setOnClickListener(this)
        binding.cvAmount3000AddIncome.setOnClickListener(this)
        binding.cvAmount3500AddIncome.setOnClickListener(this)
        binding.cvAmount4000AddIncome.setOnClickListener(this)
        binding.cvAmount5000AddIncome.setOnClickListener(this)
        binding.cvAmount10000AddIncome.setOnClickListener(this)
        binding.cvAmount20000AddIncome.setOnClickListener(this)
        binding.cvAmount30000AddIncome.setOnClickListener(this)
        binding.cvAmount40000AddIncome.setOnClickListener(this)
        binding.cvAmount50000AddIncome.setOnClickListener(this)
        binding.cvAmount100000AddIncome.setOnClickListener(this)
        binding.cvAmount200000AddIncome.setOnClickListener(this)
        binding.cvAmount300000AddIncome.setOnClickListener(this)
        binding.cvAmount400000AddIncome.setOnClickListener(this)
        binding.cvAmount500000AddIncome.setOnClickListener(this)
        binding.cvSalaryAddIncome.setOnClickListener(this)
        binding.cvBusinessAddIncome.setOnClickListener(this)
        binding.cvHouseRentAddIncome.setOnClickListener(this)
        binding.cvOtherAddIncome.setOnClickListener(this)
        binding.cvSaveAddIncome.setOnClickListener(View.OnClickListener { addIncome() })
        binding.tvDateTimeAddIncome.setOnClickListener(View.OnClickListener { changeDate() })
        binding.ivHomeToolbarAddIncome.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(this@AddIncome,
                MainActivity::class.java))
        })


        //************************************************Starting Methods*****************************************
        observe()
        setDateTime()
        binding.tvCurrentBalanceValueToolBarAddIncome.setText(UtilDB.currentBalance.toString())
    }

    private fun setDateTime() {
        val simpleDateTimeFormat = SimpleDateFormat("dd-MM-yyyy  hh:mm a", Locale.getDefault())
        val simpleDayFormat = SimpleDateFormat("dd", Locale.getDefault())
        val simpleMonthFormat = SimpleDateFormat("MM", Locale.getDefault())
        val simpleYearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        val simpleTimeFormat = SimpleDateFormat("hh-mm a", Locale.getDefault())
        vm_addIncome!!.day = simpleDayFormat.format(System.currentTimeMillis())
        vm_addIncome!!.month = simpleMonthFormat.format(System.currentTimeMillis())
        vm_addIncome!!.year = simpleYearFormat.format(System.currentTimeMillis())
        vm_addIncome!!.time = simpleTimeFormat.format(System.currentTimeMillis())
        vm_addIncome!!.dateTime = simpleDateTimeFormat.format(System.currentTimeMillis())
        binding.tvDateTimeAddIncome!!.text = vm_addIncome!!.dateTime
    }

    private fun addIncome() {
        if (TextUtils.isEmpty(binding.etAmountAddIncome!!.text)) {
            binding.etAmountAddIncome!!.error = getString(R.string.AmontNeeded)
        } else if (TextUtils.isEmpty(vm_addIncome!!.category)) {
            Toast.makeText(this, getString(R.string.Pleaseselectacatagory), Toast.LENGTH_SHORT)
                .show()
        } else {
            val amount = binding.etAmountAddIncome!!.text.toString()
            var expenseDescription = ""
            if (!TextUtils.isEmpty(binding.etIncomeDescriptionAddIncome!!.text)) {
                expenseDescription = binding.etIncomeDescriptionAddIncome!!.text.toString()
            }
            val posts = MC_Posts(expenseDescription,
                vm_addIncome!!.category,
                Constants.TYPE_INCOME,
                amount,
                vm_addIncome!!.year,
                vm_addIncome!!.month,
                vm_addIncome!!.day,
                vm_addIncome!!.time,
                System.currentTimeMillis().toString(),
                vm_addIncome!!.dateTime)
            val helper = SQLiteHelper(this@AddIncome)
            helper.saveData(posts)
            UtilDB.currentBalance = UtilDB.currentBalance + amount.toInt()
            startActivity(Intent(this@AddIncome, MainActivity::class.java))
            Toast.makeText(this, "Success ", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observe() {
        vm_addIncome!!.mutable_category.observe(this, { s: String ->
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
        vm_addIncome!!.mutable_amount.observe(this, { s ->
            if (s == Constants.AMOUNT_500) {
                binding.etAmountAddIncome.setText(Constants.AMOUNT_500)
            } else if (s == Constants.AMOUNT_1000) {
                binding.etAmountAddIncome.setText(Constants.AMOUNT_1000)
            } else if (s == Constants.AMOUNT_1500) {
                binding.etAmountAddIncome.setText(Constants.AMOUNT_1500)
            } else if (s == Constants.AMOUNT_2000) {
                binding.etAmountAddIncome.setText(Constants.AMOUNT_2000)
            } else if (s == Constants.AMOUNT_2500) {
                binding.etAmountAddIncome.setText(Constants.AMOUNT_2500)
            } else if (s == Constants.AMOUNT_3000) {
                binding.etAmountAddIncome.setText(Constants.AMOUNT_3000)
            } else if (s == Constants.AMOUNT_4000) {
                binding.etAmountAddIncome.setText(Constants.AMOUNT_4000)
            } else if (s == Constants.AMOUNT_5000) {
                binding.etAmountAddIncome.setText(Constants.AMOUNT_5000)
            } else if (s == Constants.AMOUNT_10000) {
                binding.etAmountAddIncome.setText(Constants.AMOUNT_10000)
            } else if (s == Constants.AMOUNT_20000) {
                binding.etAmountAddIncome.setText(Constants.AMOUNT_20000)
            } else if (s == Constants.AMOUNT_30000) {
                binding.etAmountAddIncome.setText(Constants.AMOUNT_30000)
            } else if (s == Constants.AMOUNT_40000) {
                binding.etAmountAddIncome.setText(Constants.AMOUNT_40000)
            } else if (s == Constants.AMOUNT_50000) {
                binding.etAmountAddIncome.setText(Constants.AMOUNT_50000)
            } else if (s == Constants.AMOUNT_100000) {
                binding.etAmountAddIncome.setText(Constants.AMOUNT_100000)
            } else if (s == Constants.AMOUNT_200000) {
                binding.etAmountAddIncome.setText(Constants.AMOUNT_200000)
            } else if (s == Constants.AMOUNT_300000) {
                binding.etAmountAddIncome.setText(Constants.AMOUNT_300000)
            } else if (s == Constants.AMOUNT_400000) {
                binding.etAmountAddIncome.setText(Constants.AMOUNT_400000)
            } else if (s == Constants.AMOUNT_500000) {
                binding.etAmountAddIncome.setText(Constants.AMOUNT_500000)
            }
        })
    }

    override fun onClick(v: View) {
        if (v.id == R.id.cv_amount500_AddIncome) {
            vm_addIncome!!.amount = Constants.AMOUNT_500
            vm_addIncome!!.mutable_amount.setValue(vm_addIncome!!.amount)
        } else if (v.id == R.id.cv_amount1000_AddIncome) {
            vm_addIncome!!.amount = Constants.AMOUNT_1000
            vm_addIncome!!.mutable_amount.setValue(vm_addIncome!!.amount)
        } else if (v.id == R.id.cv_amount1500_AddIncome) {
            vm_addIncome!!.amount = Constants.AMOUNT_1500
            vm_addIncome!!.mutable_amount.setValue(vm_addIncome!!.amount)
        } else if (v.id == R.id.cv_amount2000_AddIncome) {
            vm_addIncome!!.amount = Constants.AMOUNT_2000
            vm_addIncome!!.mutable_amount.setValue(vm_addIncome!!.amount)
        } else if (v.id == R.id.cv_amount2500_AddIncome) {
            vm_addIncome!!.amount = Constants.AMOUNT_2500
            vm_addIncome!!.mutable_amount.setValue(vm_addIncome!!.amount)
        } else if (v.id == R.id.cv_amount3000_AddIncome) {
            vm_addIncome!!.amount = Constants.AMOUNT_3000
            vm_addIncome!!.mutable_amount.setValue(vm_addIncome!!.amount)
        } else if (v.id == R.id.cv_amount3500_AddIncome) {
            vm_addIncome!!.amount = Constants.AMOUNT_3500
            vm_addIncome!!.mutable_amount.setValue(vm_addIncome!!.amount)
        } else if (v.id == R.id.cv_amount4000_AddIncome) {
            vm_addIncome!!.amount = Constants.AMOUNT_4000
            vm_addIncome!!.mutable_amount.setValue(vm_addIncome!!.amount)
        } else if (v.id == R.id.cv_amount5000_AddIncome) {
            vm_addIncome!!.amount = Constants.AMOUNT_5000
            vm_addIncome!!.mutable_amount.setValue(vm_addIncome!!.amount)
        } else if (v.id == R.id.cv_amount10000_AddIncome) {
            vm_addIncome!!.amount = Constants.AMOUNT_10000
            vm_addIncome!!.mutable_amount.setValue(vm_addIncome!!.amount)
        } else if (v.id == R.id.cv_amount20000_AddIncome) {
            vm_addIncome!!.amount = Constants.AMOUNT_20000
            vm_addIncome!!.mutable_amount.setValue(vm_addIncome!!.amount)
        } else if (v.id == R.id.cv_amount30000_AddIncome) {
            vm_addIncome!!.amount = Constants.AMOUNT_30000
            vm_addIncome!!.mutable_amount.setValue(vm_addIncome!!.amount)
        } else if (v.id == R.id.cv_amount40000_AddIncome) {
            vm_addIncome!!.amount = Constants.AMOUNT_40000
            vm_addIncome!!.mutable_amount.setValue(vm_addIncome!!.amount)
        } else if (v.id == R.id.cv_amount50000_AddIncome) {
            vm_addIncome!!.amount = Constants.AMOUNT_50000
            vm_addIncome!!.mutable_amount.setValue(vm_addIncome!!.amount)
        } else if (v.id == R.id.cv_amount100000_AddIncome) {
            vm_addIncome!!.amount = Constants.AMOUNT_100000
            vm_addIncome!!.mutable_amount.setValue(vm_addIncome!!.amount)
        } else if (v.id == R.id.cv_amount200000_AddIncome) {
            vm_addIncome!!.amount = Constants.AMOUNT_200000
            vm_addIncome!!.mutable_amount.setValue(vm_addIncome!!.amount)
        } else if (v.id == R.id.cv_amount300000_AddIncome) {
            vm_addIncome!!.amount = Constants.AMOUNT_300000
            vm_addIncome!!.mutable_amount.setValue(vm_addIncome!!.amount)
        } else if (v.id == R.id.cv_amount400000_AddIncome) {
            vm_addIncome!!.amount = Constants.AMOUNT_400000
            vm_addIncome!!.mutable_amount.setValue(vm_addIncome!!.amount)
        } else if (v.id == R.id.cv_amount500000_AddIncome) {
            vm_addIncome!!.amount = Constants.AMOUNT_500000
            vm_addIncome!!.mutable_amount.setValue(vm_addIncome!!.amount)
        } else if (v.id == R.id.cv_salary_AddIncome) {
            vm_addIncome!!.category = Constants.CATEGORY_SALARY
            vm_addIncome!!.mutable_category.setValue(vm_addIncome!!.category)
        } else if (v.id == R.id.cv_business_AddIncome) {
            vm_addIncome!!.category = Constants.CATEGORY_BUSINESS
            vm_addIncome!!.mutable_category.setValue(vm_addIncome!!.category)
        } else if (v.id == R.id.cv_houseRent_AddIncome) {
            vm_addIncome!!.category = Constants.CATEGORY_HOUSE_RENT
            vm_addIncome!!.mutable_category.setValue(vm_addIncome!!.category)
        } else if (v.id == R.id.cv_other_AddIncome) {
            vm_addIncome!!.category = Constants.CATEGORY_OTHER
            vm_addIncome!!.mutable_category.value = vm_addIncome!!.category
        }
    }

    private fun markOther() {
        binding.cvSalaryAddIncome.setCardBackgroundColor(getColor(R.color.white))
        binding.cvBusinessAddIncome.setCardBackgroundColor(getColor(R.color.white))
        binding.cvHouseRentAddIncome.setCardBackgroundColor(getColor(R.color.white))
        binding.cvOtherAddIncome.setCardBackgroundColor(getColor(R.color.green))
    }

    private fun markHouseRent() {
        binding.cvSalaryAddIncome.setCardBackgroundColor(getColor(R.color.white))
        binding.cvBusinessAddIncome.setCardBackgroundColor(getColor(R.color.white))
        binding.cvHouseRentAddIncome.setCardBackgroundColor(getColor(R.color.green))
        binding.cvOtherAddIncome.setCardBackgroundColor(getColor(R.color.white))
    }

    private fun markBusiness() {
        binding.cvSalaryAddIncome.setCardBackgroundColor(getColor(R.color.white))
        binding.cvBusinessAddIncome.setCardBackgroundColor(getColor(R.color.green))
        binding.cvHouseRentAddIncome.setCardBackgroundColor(getColor(R.color.white))
        binding.cvOtherAddIncome.setCardBackgroundColor(getColor(R.color.white))
    }

    private fun markSalary() {
        binding.cvSalaryAddIncome.setCardBackgroundColor(getColor(R.color.green))
        binding.cvBusinessAddIncome.setCardBackgroundColor(getColor(R.color.white))
        binding.cvHouseRentAddIncome.setCardBackgroundColor(getColor(R.color.white))
        binding.cvOtherAddIncome.setCardBackgroundColor(getColor(R.color.white))
    }

    private fun changeDate() {
        val timePickerDialog =
            TimePickerDialog(this@AddIncome, { view: TimePicker?, hourOfDay: Int, minute: Int ->
                vm_addIncome!!.time = "$hourOfDay : $minute"
                Log.d("tag", "hour$hourOfDay")
                if (hourOfDay >= 12) {
                    am_pm = "pm"
                    hourInString = (hourOfDay - 12).toString()
                } else {
                    am_pm = "am"
                    hourInString = hourOfDay.toString()
                }
                vm_addIncome!!.dateTime =
                    vm_addIncome!!.day + "-" + vm_addIncome!!.month + "-" + vm_addIncome!!.year + "  " + hourInString + ":" + minute.toString() + " " + am_pm
                //Log.d("tag","year"+vm_addIncome.year+" month "+vm_addIncome.month+" day "+vm_addIncome.day+" hour "+hourOfDay+"min "+minute+" "+am_pm);
                binding.tvDateTimeAddIncome.text = vm_addIncome!!.dateTime
            }, hour, minute, true)
        val datePickerDialog = DatePickerDialog(this@AddIncome, { view, year, month, dayOfMonth ->
            setDay(dayOfMonth)
            setMonth(month + 1)
            /*vm_addIncome.year = String.valueOf(year);
                      vm_addIncome.month = String.valueOf(month+1);
                      vm_addIncome.day = String.valueOf(dayOfMonth);*/timePickerDialog.show()
        }, year, month, day)
        datePickerDialog.show()
    }

    private fun setDay(day: Int) {
        if (day == 1) {
            vm_addIncome!!.day = getString(R.string.digit01)
        } else if (day == 2) {
            vm_addIncome!!.day = getString(R.string.digit02)
        } else if (day == 3) {
            vm_addIncome!!.day = getString(R.string.digit03)
        } else if (day == 4) {
            vm_addIncome!!.day = getString(R.string.digit04)
        } else if (day == 5) {
            vm_addIncome!!.day = getString(R.string.digit05)
        } else if (day == 6) {
            vm_addIncome!!.day = getString(R.string.digit06)
        } else if (day == 7) {
            vm_addIncome!!.day = getString(R.string.digit07)
        } else if (day == 8) {
            vm_addIncome!!.day = getString(R.string.digit08)
        } else if (day == 9) {
            vm_addIncome!!.day = getString(R.string.digit09)
        } else {
            vm_addIncome!!.day = day.toString()
        }
    }

    private fun setMonth(month: Int) {
        if (month == 1) {
            vm_addIncome!!.month = getString(R.string.digit01)
        } else if (month == 2) {
            vm_addIncome!!.month = getString(R.string.digit02)
        } else if (month == 3) {
            vm_addIncome!!.month = getString(R.string.digit03)
        } else if (month == 4) {
            vm_addIncome!!.month = getString(R.string.digit04)
        } else if (month == 5) {
            vm_addIncome!!.month = getString(R.string.digit05)
        } else if (month == 6) {
            vm_addIncome!!.month = getString(R.string.digit06)
        } else if (month == 7) {
            vm_addIncome!!.month = getString(R.string.digit07)
        } else if (month == 8) {
            vm_addIncome!!.month = getString(R.string.digit08)
        } else if (month == 9) {
            vm_addIncome!!.month = getString(R.string.digit09)
        } else {
            vm_addIncome!!.month = month.toString()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}