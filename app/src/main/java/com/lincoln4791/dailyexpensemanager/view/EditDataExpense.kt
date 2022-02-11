/*
package com.lincoln4791.dailyexpensemanager.view

import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.lincoln4791.dailyexpensemanager.viewModels.VM_EditDataExpense
import android.os.Bundle
import com.lincoln4791.dailyexpensemanager.R
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import com.lincoln4791.dailyexpensemanager.view.MainActivity
import com.lincoln4791.dailyexpensemanager.common.util.UtilDB
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
import com.lincoln4791.dailyexpensemanager.databinding.ActivityEditDataExpenseBinding
import java.text.SimpleDateFormat
import java.util.*

class EditDataExpense : AppCompatActivity(), View.OnClickListener {

    lateinit var expenseDescription: String
    lateinit var amount: String
    private lateinit var ID: String
    private var hour = 0
    private var minute = 0
    private var year = 0
    private var month = 0
    private var day = 0
    var am_pm: String? = null
    var hourInString: String? = null
    private var vm_EditDataExpense: VM_EditDataExpense? = null

    private lateinit var binding : ActivityEditDataExpenseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDataExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()
        vm_EditDataExpense = ViewModelProviders.of(this).get(VM_EditDataExpense::class.java)
        val calendar = Calendar.getInstance()
        year = calendar[Calendar.YEAR]
        month = calendar[Calendar.MONTH]
        day = calendar[Calendar.DAY_OF_MONTH]
        val simpleHourFormat = SimpleDateFormat("hh")
        val simpleMinuteFormat = SimpleDateFormat("mm")
        hour = simpleHourFormat.format(System.currentTimeMillis()).toInt()
        minute = simpleMinuteFormat.format(System.currentTimeMillis()).toInt()


        binding.cvBackEditDataExpense.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(this@EditDataExpense,
                MainActivity::class.java))
        })
        binding.cvAmount500EditDataExpense.setOnClickListener(this)
        binding.cvAmount1000EditDataExpense.setOnClickListener(this)
        binding.cvAmount1500EditDataExpense.setOnClickListener(this)
        binding.cvAmount2000EditDataExpense.setOnClickListener(this)
        binding.cvAmount2500EditDataExpense.setOnClickListener(this)
        binding.cvAmount3000EditDataExpense.setOnClickListener(this)
        binding.cvAmount3500EditDataExpense.setOnClickListener(this)
        binding.cvAmount4000EditDataExpense.setOnClickListener(this)
        binding.cvAmount5000EditDataExpense.setOnClickListener(this)
        binding.cvAmount10000EditDataExpense.setOnClickListener(this)
        binding.cvAmount20000EditDataExpense.setOnClickListener(this)
        binding.cvAmount30000EditDataExpense.setOnClickListener(this)
        binding.cvAmount40000EditDataExpense.setOnClickListener(this)
        binding.cvAmount50000EditDataExpense.setOnClickListener(this)
        binding.cvAmount100000EditDataExpense.setOnClickListener(this)
        binding.cvAmount200000EditDataExpense.setOnClickListener(this)
        binding.cvAmount300000EditDataExpense.setOnClickListener(this)
        binding.cvAmount400000EditDataExpense.setOnClickListener(this)
        binding.cvAmount500000EditDataExpense.setOnClickListener(this)
        binding.cvFoodEditDataExpense.setOnClickListener(this)
        binding.cvBusinessEditDataExpense.setOnClickListener(this)
        binding.cvHouseRentEditDataExpense.setOnClickListener(this)
        binding.cvMedicineEditDataExpense.setOnClickListener(this)
        binding.cvLifeStyleEditDataExpense.setOnClickListener(this)
        binding.cvEducationEditDataExpense.setOnClickListener(this)
        binding.cvBillsEditDataExpense.setOnClickListener(this)
        binding.cvClothsEditDataExpense.setOnClickListener(this)
        binding.cvTransportEditDataExpense.setOnClickListener(this)
        binding.cvOtherEditDataExpense.setOnClickListener(this)
        binding.cvSaveEditDataExpense.setOnClickListener(View.OnClickListener { v: View? -> updateExpense() })
        binding.tvDateTimeEditDataExpense.setOnClickListener(View.OnClickListener { v: View? -> changeDate() })
        binding.cvBackEditDataExpense.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(this@EditDataExpense,
                MainActivity::class.java))
        })

        binding.tvCurrentBalanceValueToolBarEditDataExpense.setText(UtilDB.currentBalance.toString())
        observe()
        intentData
        setViewWIthIntentData()
    }

    private fun setViewWIthIntentData() {
        binding.tvDateTimeEditDataExpense.text = vm_EditDataExpense!!.dateTime
        binding.etExpenseAmountEditDataExpense.setText(vm_EditDataExpense!!.amount)
        binding.etExpenseDescriptionEditDataExpense.setText(expenseDescription)
        if (vm_EditDataExpense!!.category == Constants.CATEGORY_FOOD) {
            markFood()
        } else if (vm_EditDataExpense!!.category == Constants.CATEGORY_TRANSPORT) {
            markTransport()
        } else if (vm_EditDataExpense!!.category == Constants.CATEGORY_BILLS) {
            markBills()
        } else if (vm_EditDataExpense!!.category == Constants.CATEGORY_HOUSE_RENT) {
            markHouseRent()
        } else if (vm_EditDataExpense!!.category == Constants.CATEGORY_BUSINESS) {
            markBusiness()
        } else if (vm_EditDataExpense!!.category == Constants.CATEGORY_MEDICINE) {
            markMedicine()
        } else if (vm_EditDataExpense!!.category == Constants.CATEGORY_CLOTHS) {
            markCloths()
        } else if (vm_EditDataExpense!!.category == Constants.CATEGORY_EDUCATION) {
            markEducation()
        } else if (vm_EditDataExpense!!.category == Constants.CATEGORY_LIFESTYLE) {
            markLifeStyle()
        } else if (vm_EditDataExpense!!.category == Constants.CATEGORY_OTHER) {
            markOther()
        }
    }

    private val intentData: Unit
        private get() {
            ID = intent.getStringExtra(NodeName.ID)!!
            expenseDescription = intent.getStringExtra(NodeName.POST_DESCRIPTION)!!
            amount = intent.getStringExtra(NodeName.POST_AMOUNT)!!
            vm_EditDataExpense!!.dateTime = intent.getStringExtra(NodeName.POST_DATE_TIME)!!
            vm_EditDataExpense!!.category = intent.getStringExtra(NodeName.POST_CATEGORY)!!
            vm_EditDataExpense!!.year = intent.getStringExtra(NodeName.POST_YEAR)
            vm_EditDataExpense!!.month = intent.getStringExtra(NodeName.POST_MONTH)
            vm_EditDataExpense!!.day = intent.getStringExtra(NodeName.POST_DAY)
            vm_EditDataExpense!!.time = intent.getStringExtra(NodeName.POST_TIME)
        }

    private fun updateExpense() {
        if (TextUtils.isEmpty(binding.etExpenseAmountEditDataExpense.text)) {
            binding.etExpenseAmountEditDataExpense.error = getString(R.string.AmontNeeded)
        } else if (TextUtils.isEmpty(vm_EditDataExpense!!.category)) {
            Toast.makeText(this, getString(R.string.Pleaseselectacatagory), Toast.LENGTH_SHORT)
                .show()
        } else {
            amount = binding.etExpenseAmountEditDataExpense.text.toString()
            expenseDescription = ""
            if (!TextUtils.isEmpty(binding.etExpenseDescriptionEditDataExpense!!.text)) {
                expenseDescription = binding.etExpenseDescriptionEditDataExpense!!.text.toString()
            }
            val posts = MC_Posts(expenseDescription,
                vm_EditDataExpense!!.category,
                Constants.TYPE_EXPENSE,
                amount,
                vm_EditDataExpense!!.year!!,
                vm_EditDataExpense!!.month!!,
                vm_EditDataExpense!!.day!!,
                vm_EditDataExpense!!.time!!,
                System.currentTimeMillis().toString(),
                vm_EditDataExpense!!.dateTime)
            val helper = SQLiteHelper(this@EditDataExpense)
            helper.updateData(ID, posts)
            Toast.makeText(this, "Success $ID", Toast.LENGTH_LONG).show()
            startActivity(Intent(this@EditDataExpense, MainActivity::class.java))
        }
    }

    private fun observe() {
        vm_EditDataExpense!!.mutable_category.observe(this, { s: String ->
            if (s == Constants.CATEGORY_FOOD) {
                markFood()
            } else if (s == Constants.CATEGORY_TRANSPORT) {
                markTransport()
            } else if (s == Constants.CATEGORY_BILLS) {
                markBills()
            } else if (s == Constants.CATEGORY_HOUSE_RENT) {
                markHouseRent()
            } else if (s == Constants.CATEGORY_BUSINESS) {
                markBusiness()
            } else if (s == Constants.CATEGORY_MEDICINE) {
                markMedicine()
            } else if (s == Constants.CATEGORY_CLOTHS) {
                markCloths()
            } else if (s == Constants.CATEGORY_EDUCATION) {
                markEducation()
            } else if (s == Constants.CATEGORY_LIFESTYLE) {
                markLifeStyle()
            } else if (s == Constants.CATEGORY_OTHER) {
                markOther()
            }
        })
        vm_EditDataExpense!!.mutable_amount.observe(this, { s ->
            if (s == Constants.AMOUNT_500) {
                binding.etExpenseAmountEditDataExpense.setText(Constants.AMOUNT_500)
            } else if (s == Constants.AMOUNT_1000) {
                binding.etExpenseAmountEditDataExpense.setText(Constants.AMOUNT_1000)
            } else if (s == Constants.AMOUNT_1500) {
                binding.etExpenseAmountEditDataExpense.setText(Constants.AMOUNT_1500)
            } else if (s == Constants.AMOUNT_2000) {
                binding.etExpenseAmountEditDataExpense.setText(Constants.AMOUNT_2000)
            } else if (s == Constants.AMOUNT_2500) {
                binding.etExpenseAmountEditDataExpense.setText(Constants.AMOUNT_2500)
            } else if (s == Constants.AMOUNT_3000) {
                binding.etExpenseAmountEditDataExpense.setText(Constants.AMOUNT_3000)
            } else if (s == Constants.AMOUNT_4000) {
                binding.etExpenseAmountEditDataExpense.setText(Constants.AMOUNT_4000)
            } else if (s == Constants.AMOUNT_5000) {
                binding.etExpenseAmountEditDataExpense.setText(Constants.AMOUNT_5000)
            } else if (s == Constants.AMOUNT_10000) {
                binding.etExpenseAmountEditDataExpense.setText(Constants.AMOUNT_10000)
            } else if (s == Constants.AMOUNT_20000) {
                binding.etExpenseAmountEditDataExpense.setText(Constants.AMOUNT_20000)
            } else if (s == Constants.AMOUNT_30000) {
                binding.etExpenseAmountEditDataExpense.setText(Constants.AMOUNT_30000)
            } else if (s == Constants.AMOUNT_40000) {
                binding.etExpenseAmountEditDataExpense.setText(Constants.AMOUNT_40000)
            } else if (s == Constants.AMOUNT_50000) {
                binding.etExpenseAmountEditDataExpense.setText(Constants.AMOUNT_50000)
            } else if (s == Constants.AMOUNT_100000) {
                binding.etExpenseAmountEditDataExpense.setText(Constants.AMOUNT_100000)
            } else if (s == Constants.AMOUNT_200000) {
                binding.etExpenseAmountEditDataExpense.setText(Constants.AMOUNT_200000)
            } else if (s == Constants.AMOUNT_300000) {
                binding.etExpenseAmountEditDataExpense.setText(Constants.AMOUNT_300000)
            } else if (s == Constants.AMOUNT_400000) {
                binding.etExpenseAmountEditDataExpense.setText(Constants.AMOUNT_400000)
            } else if (s == Constants.AMOUNT_500000) {
                binding.etExpenseAmountEditDataExpense.setText(Constants.AMOUNT_500000)
            }
        })
    }

    override fun onClick(v: View) {
        if (v.id == R.id.cv_amount500_EditDataExpense) {
            vm_EditDataExpense!!.amount = Constants.AMOUNT_500
            vm_EditDataExpense!!.mutable_amount.setValue(vm_EditDataExpense!!.amount)
        } else if (v.id == R.id.cv_amount1000_EditDataExpense) {
            vm_EditDataExpense!!.amount = Constants.AMOUNT_1000
            vm_EditDataExpense!!.mutable_amount.setValue(vm_EditDataExpense!!.amount)
        } else if (v.id == R.id.cv_amount1500_EditDataExpense) {
            vm_EditDataExpense!!.amount = Constants.AMOUNT_1500
            vm_EditDataExpense!!.mutable_amount.setValue(vm_EditDataExpense!!.amount)
        } else if (v.id == R.id.cv_amount2000_EditDataExpense) {
            vm_EditDataExpense!!.amount = Constants.AMOUNT_2000
            vm_EditDataExpense!!.mutable_amount.setValue(vm_EditDataExpense!!.amount)
        } else if (v.id == R.id.cv_amount2500_EditDataExpense) {
            vm_EditDataExpense!!.amount = Constants.AMOUNT_2500
            vm_EditDataExpense!!.mutable_amount.setValue(vm_EditDataExpense!!.amount)
        } else if (v.id == R.id.cv_amount3000_EditDataExpense) {
            vm_EditDataExpense!!.amount = Constants.AMOUNT_3000
            vm_EditDataExpense!!.mutable_amount.setValue(vm_EditDataExpense!!.amount)
        } else if (v.id == R.id.cv_amount3500_EditDataExpense) {
            vm_EditDataExpense!!.amount = Constants.AMOUNT_3500
            vm_EditDataExpense!!.mutable_amount.setValue(vm_EditDataExpense!!.amount)
        } else if (v.id == R.id.cv_amount4000_EditDataExpense) {
            vm_EditDataExpense!!.amount = Constants.AMOUNT_4000
            vm_EditDataExpense!!.mutable_amount.setValue(vm_EditDataExpense!!.amount)
        } else if (v.id == R.id.cv_amount5000_EditDataExpense) {
            vm_EditDataExpense!!.amount = Constants.AMOUNT_5000
            vm_EditDataExpense!!.mutable_amount.setValue(vm_EditDataExpense!!.amount)
        } else if (v.id == R.id.cv_amount10000_EditDataExpense) {
            vm_EditDataExpense!!.amount = Constants.AMOUNT_10000
            vm_EditDataExpense!!.mutable_amount.setValue(vm_EditDataExpense!!.amount)
        } else if (v.id == R.id.cv_amount20000_EditDataExpense) {
            vm_EditDataExpense!!.amount = Constants.AMOUNT_20000
            vm_EditDataExpense!!.mutable_amount.setValue(vm_EditDataExpense!!.amount)
        } else if (v.id == R.id.cv_amount30000_EditDataExpense) {
            vm_EditDataExpense!!.amount = Constants.AMOUNT_30000
            vm_EditDataExpense!!.mutable_amount.setValue(vm_EditDataExpense!!.amount)
        } else if (v.id == R.id.cv_amount40000_EditDataExpense) {
            vm_EditDataExpense!!.amount = Constants.AMOUNT_40000
            vm_EditDataExpense!!.mutable_amount.setValue(vm_EditDataExpense!!.amount)
        } else if (v.id == R.id.cv_amount50000_EditDataExpense) {
            vm_EditDataExpense!!.amount = Constants.AMOUNT_50000
            vm_EditDataExpense!!.mutable_amount.setValue(vm_EditDataExpense!!.amount)
        } else if (v.id == R.id.cv_amount100000_EditDataExpense) {
            vm_EditDataExpense!!.amount = Constants.AMOUNT_100000
            vm_EditDataExpense!!.mutable_amount.setValue(vm_EditDataExpense!!.amount)
        } else if (v.id == R.id.cv_amount200000_EditDataExpense) {
            vm_EditDataExpense!!.amount = Constants.AMOUNT_200000
            vm_EditDataExpense!!.mutable_amount.setValue(vm_EditDataExpense!!.amount)
        } else if (v.id == R.id.cv_amount300000_EditDataExpense) {
            vm_EditDataExpense!!.amount = Constants.AMOUNT_300000
            vm_EditDataExpense!!.mutable_amount.setValue(vm_EditDataExpense!!.amount)
        } else if (v.id == R.id.cv_amount400000_EditDataExpense) {
            vm_EditDataExpense!!.amount = Constants.AMOUNT_400000
            vm_EditDataExpense!!.mutable_amount.setValue(vm_EditDataExpense!!.amount)
        } else if (v.id == R.id.cv_amount500000_EditDataExpense) {
            vm_EditDataExpense!!.amount = Constants.AMOUNT_500000
            vm_EditDataExpense!!.mutable_amount.setValue(vm_EditDataExpense!!.amount)
        } else if (v.id == R.id.cv_food_EditDataExpense) {
            vm_EditDataExpense!!.category = Constants.CATEGORY_FOOD
            vm_EditDataExpense!!.mutable_category.setValue(vm_EditDataExpense!!.category)
        } else if (v.id == R.id.cv_business_EditDataExpense) {
            vm_EditDataExpense!!.category = Constants.CATEGORY_BUSINESS
            vm_EditDataExpense!!.mutable_category.setValue(vm_EditDataExpense!!.category)
        } else if (v.id == R.id.cv_houseRent_EditDataExpense) {
            vm_EditDataExpense!!.category = Constants.CATEGORY_HOUSE_RENT
            vm_EditDataExpense!!.mutable_category.setValue(vm_EditDataExpense!!.category)
        } else if (v.id == R.id.cv_transport_EditDataExpense) {
            vm_EditDataExpense!!.category = Constants.CATEGORY_TRANSPORT
            vm_EditDataExpense!!.mutable_category.setValue(vm_EditDataExpense!!.category)
        } else if (v.id == R.id.cv_cloths_EditDataExpense) {
            vm_EditDataExpense!!.category = Constants.CATEGORY_CLOTHS
            vm_EditDataExpense!!.mutable_category.setValue(vm_EditDataExpense!!.category)
        } else if (v.id == R.id.cv_bills_EditDataExpense) {
            vm_EditDataExpense!!.category = Constants.CATEGORY_BILLS
            vm_EditDataExpense!!.mutable_category.setValue(vm_EditDataExpense!!.category)
        } else if (v.id == R.id.cv_education_EditDataExpense) {
            vm_EditDataExpense!!.category = Constants.CATEGORY_EDUCATION
            vm_EditDataExpense!!.mutable_category.setValue(vm_EditDataExpense!!.category)
        } else if (v.id == R.id.cv_lifeStyle_EditDataExpense) {
            vm_EditDataExpense!!.category = Constants.CATEGORY_LIFESTYLE
            vm_EditDataExpense!!.mutable_category.setValue(vm_EditDataExpense!!.category)
        } else if (v.id == R.id.cv_medicine_EditDataExpense) {
            vm_EditDataExpense!!.category = Constants.CATEGORY_MEDICINE
            vm_EditDataExpense!!.mutable_category.setValue(vm_EditDataExpense!!.category)
        } else if (v.id == R.id.cv_other_EditDataExpense) {
            vm_EditDataExpense!!.category = Constants.CATEGORY_OTHER
            vm_EditDataExpense!!.mutable_category.value = vm_EditDataExpense!!.category
        }
    }

    private fun markOther() {
        binding.cvFoodEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvBusinessEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvHouseRentEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvTransportEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvClothsEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvBillsEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvEducationEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvLifeStyleEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvMedicineEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvOtherEditDataExpense.setCardBackgroundColor(getColor(R.color.pink))
    }

    private fun markMedicine() {
        binding.cvFoodEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvBusinessEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvHouseRentEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvTransportEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvClothsEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvBillsEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvEducationEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvLifeStyleEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvMedicineEditDataExpense.setCardBackgroundColor(getColor(R.color.pink))
        binding.cvOtherEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
    }

    private fun markLifeStyle() {
        binding.cvFoodEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvBusinessEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvHouseRentEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvTransportEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvClothsEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvBillsEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvEducationEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvLifeStyleEditDataExpense.setCardBackgroundColor(getColor(R.color.pink))
        binding.cvMedicineEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvOtherEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
    }

    private fun markEducation() {
        binding.cvFoodEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvBusinessEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvHouseRentEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvTransportEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvClothsEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvBillsEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvEducationEditDataExpense.setCardBackgroundColor(getColor(R.color.pink))
        binding.cvLifeStyleEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvMedicineEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvOtherEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
    }

    private fun markBills() {
        binding.cvFoodEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvBusinessEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvHouseRentEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvTransportEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvClothsEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvBillsEditDataExpense.setCardBackgroundColor(getColor(R.color.pink))
        binding.cvEducationEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvLifeStyleEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvMedicineEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvOtherEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
    }

    private fun markCloths() {
        binding.cvFoodEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvBusinessEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvHouseRentEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvTransportEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvClothsEditDataExpense.setCardBackgroundColor(getColor(R.color.pink))
        binding.cvBillsEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvEducationEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvLifeStyleEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvMedicineEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvOtherEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
    }

    private fun markTransport() {
        binding.cvFoodEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvBusinessEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvHouseRentEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvTransportEditDataExpense.setCardBackgroundColor(getColor(R.color.pink))
        binding.cvClothsEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvBillsEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvEducationEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvLifeStyleEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvMedicineEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvOtherEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
    }

    private fun markHouseRent() {
        binding.cvFoodEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvBusinessEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvHouseRentEditDataExpense.setCardBackgroundColor(getColor(R.color.pink))
        binding.cvTransportEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvClothsEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvBillsEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvEducationEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvLifeStyleEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvMedicineEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvOtherEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
    }

    private fun markBusiness() {
        binding.cvFoodEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvBusinessEditDataExpense.setCardBackgroundColor(getColor(R.color.pink))
        binding.cvHouseRentEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvTransportEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvClothsEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvBillsEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvEducationEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvLifeStyleEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvMedicineEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvOtherEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
    }

    private fun markFood() {
        binding.cvFoodEditDataExpense.setCardBackgroundColor(getColor(R.color.pink))
        binding.cvBusinessEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvHouseRentEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvTransportEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvClothsEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvBillsEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvEducationEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvLifeStyleEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvMedicineEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
        binding.cvOtherEditDataExpense.setCardBackgroundColor(getColor(R.color.white))
    }

    private fun changeDate() {
        val timePickerDialog = TimePickerDialog(this@EditDataExpense,
            { view: TimePicker?, hourOfDay: Int, minute: Int ->
                vm_EditDataExpense!!.time = "$hourOfDay : $minute"
                Log.d("tag", "hour$hourOfDay")
                if (hourOfDay >= 12) {
                    am_pm = "pm"
                    hourInString = (hourOfDay - 12).toString()
                } else {
                    am_pm = "am"
                    hourInString = hourOfDay.toString()
                }

                //Log.d("tag","year"+vm_addIncome.year+" month "+vm_addIncome.month+" day "+vm_addIncome.day+" hour "+hourOfDay+"min "+minute+" "+am_pm);
                binding.tvDateTimeEditDataExpense.text =
                    vm_EditDataExpense!!.day + "-" + vm_EditDataExpense!!.month + "-" + vm_EditDataExpense!!.year + "  " + hourInString + ":" + minute.toString() + " " + am_pm
            },
            hour,
            minute,
            true)
        val datePickerDialog =
            DatePickerDialog(this@EditDataExpense, { view, year, month, dayOfMonth ->
                setDay(day)
                setMonth(month)
                if (day == 1) {
                    vm_EditDataExpense!!.day = getString(R.string.digit01)
                }
                vm_EditDataExpense!!.year = year.toString()
                vm_EditDataExpense!!.month = (month + 1).toString()
                vm_EditDataExpense!!.day = dayOfMonth.toString()
                timePickerDialog.show()
            }, year, month, day)
        datePickerDialog.show()
    }

    private fun setDay(day: Int) {
        if (day == 1) {
            vm_EditDataExpense!!.day = getString(R.string.digit01)
        } else if (day == 2) {
            vm_EditDataExpense!!.day = getString(R.string.digit02)
        } else if (day == 3) {
            vm_EditDataExpense!!.day = getString(R.string.digit03)
        } else if (day == 4) {
            vm_EditDataExpense!!.day = getString(R.string.digit04)
        } else if (day == 5) {
            vm_EditDataExpense!!.day = getString(R.string.digit05)
        } else if (day == 6) {
            vm_EditDataExpense!!.day = getString(R.string.digit06)
        } else if (day == 7) {
            vm_EditDataExpense!!.day = getString(R.string.digit07)
        } else if (day == 8) {
            vm_EditDataExpense!!.day = getString(R.string.digit08)
        } else if (day == 9) {
            vm_EditDataExpense!!.day = getString(R.string.digit09)
        } else {
            vm_EditDataExpense!!.day = day.toString()
        }
    }

    private fun setMonth(month: Int) {
        if (month == 1) {
            vm_EditDataExpense!!.month = getString(R.string.digit01)
        } else if (month == 2) {
            vm_EditDataExpense!!.month = getString(R.string.digit02)
        } else if (month == 3) {
            vm_EditDataExpense!!.month = getString(R.string.digit03)
        } else if (month == 4) {
            vm_EditDataExpense!!.month = getString(R.string.digit04)
        } else if (month == 5) {
            vm_EditDataExpense!!.month = getString(R.string.digit05)
        } else if (month == 6) {
            vm_EditDataExpense!!.month = getString(R.string.digit06)
        } else if (month == 7) {
            vm_EditDataExpense!!.month = getString(R.string.digit07)
        } else if (month == 8) {
            vm_EditDataExpense!!.month = getString(R.string.digit08)
        } else if (month == 9) {
            vm_EditDataExpense!!.month = getString(R.string.digit09)
        } else {
            vm_EditDataExpense!!.month = month.toString()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}*/
