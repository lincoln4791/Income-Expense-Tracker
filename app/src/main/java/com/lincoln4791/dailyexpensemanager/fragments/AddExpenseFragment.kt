package com.lincoln4791.dailyexpensemanager.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.color.MaterialColors.getColor
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.common.Util
import com.lincoln4791.dailyexpensemanager.common.UtilDB
import com.lincoln4791.dailyexpensemanager.databinding.ActivityAddExpenseBinding
import com.lincoln4791.dailyexpensemanager.databinding.AddExpenseFragmentBinding
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import com.lincoln4791.dailyexpensemanager.roomDB.AppDatabase
import com.lincoln4791.dailyexpensemanager.view.MainActivity
import com.lincoln4791.dailyexpensemanager.viewModels.VM_AddExpenses
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddExpenseFragment : Fragment(), View.OnClickListener {
    private var hour = 0
    private var minute = 0
    private var year = 0
    private var month = 0
    private var day = 0
    var am_pm: String? = null
    var hourInString: String? = null
    var vm_addExpenses: VM_AddExpenses? = null

    private lateinit var viewModel: VM_AddExpenses
    private lateinit var binding : AddExpenseFragmentBinding
    private lateinit var navCon : NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    Log.d("tag","OnBackPressCalled -> Monthly")
                    //navCon.navigateUp()
                    goBack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = AddExpenseFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Util.recordScreenEvent("add_expense_fragment","MainActivity")

        viewModel = ViewModelProvider(this)[VM_AddExpenses::class.java]
        navCon = Navigation.findNavController(view)

        vm_addExpenses = ViewModelProvider(this)[VM_AddExpenses::class.java]
        val calendar = Calendar.getInstance()
        year = calendar[Calendar.YEAR]
        month = calendar[Calendar.MONTH]
        day = calendar[Calendar.DAY_OF_MONTH]
        val simpleHourFormat = SimpleDateFormat("hh")
        val simpleMinuteFormat = SimpleDateFormat("mm")
        hour = simpleHourFormat.format(System.currentTimeMillis()).toInt()
        minute = simpleMinuteFormat.format(System.currentTimeMillis()).toInt()


        binding.cvBackAddExpense.setOnClickListener(View.OnClickListener { v: View? ->
         goBack()
        })

        binding.cvImg.setOnClickListener {
            goBack()
        }

        binding.cvAmount500AddExpense.setOnClickListener(this)
        binding.cvAmount1000AddExpense.setOnClickListener(this)
        binding.cvAmount1500AddExpense.setOnClickListener(this)
        binding.cvAmount2000AddExpense.setOnClickListener(this)
        binding.cvAmount2500AddExpense.setOnClickListener(this)
        binding.cvAmount3000AddExpense.setOnClickListener(this)
        binding.cvAmount3500AddExpense.setOnClickListener(this)
        binding.cvAmount4000AddExpense.setOnClickListener(this)
        binding.cvAmount5000AddExpense.setOnClickListener(this)
        binding.cvAmount10000AddExpense.setOnClickListener(this)
        binding.cvAmount20000AddExpense.setOnClickListener(this)
        binding.cvAmount30000AddExpense.setOnClickListener(this)
        binding.cvAmount40000AddExpense.setOnClickListener(this)
        binding.cvAmount50000AddExpense.setOnClickListener(this)
        binding.cvAmount100000AddExpense.setOnClickListener(this)
        binding.cvAmount200000AddExpense.setOnClickListener(this)
        binding.cvAmount300000AddExpense.setOnClickListener(this)
        binding.cvAmount400000AddExpense.setOnClickListener(this)
        binding.cvAmount500000AddExpense.setOnClickListener(this)
        binding.cvFoodAddExpense.setOnClickListener(this)
        binding.cvBusinessAddExpense.setOnClickListener(this)
        binding.cvHouseRentAddExpense.setOnClickListener(this)
        binding.cvMedicineAddExpense.setOnClickListener(this)
        binding.cvLifeStyleAddExpense.setOnClickListener(this)
        binding.cvEducationAddExpense.setOnClickListener(this)
        binding.cvBillsAddExpense.setOnClickListener(this)
        binding.cvClothsAddExpense.setOnClickListener(this)
        binding.cvTransportAddExpense.setOnClickListener(this)
        binding.cvOtherAddExpense.setOnClickListener(this)
        binding.cvSaveAddExpense.setOnClickListener { saveData() }
        binding.tvDateTimeAddExpense.setOnClickListener { changeDate() }

        binding.tvCurrentBalanceValueToolBarAddExpense.setText(UtilDB.currentBalance.toString())
        observe()
        setDateTime()

    }

    private fun setDateTime() {
        val simpleDateTimeFormat = SimpleDateFormat("dd-MM-yyyy  hh:mm a", Locale.getDefault())
        val simpleDayFormat = SimpleDateFormat("dd", Locale.getDefault())
        val simpleMonthFormat = SimpleDateFormat("MM", Locale.getDefault())
        val simpleYearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        val simpleTimeFormat = SimpleDateFormat("hh-mm a", Locale.getDefault())
        vm_addExpenses!!.day = simpleDayFormat.format(System.currentTimeMillis())
        vm_addExpenses!!.month = simpleMonthFormat.format(System.currentTimeMillis())
        vm_addExpenses!!.year = simpleYearFormat.format(System.currentTimeMillis())
        vm_addExpenses!!.time = simpleTimeFormat.format(System.currentTimeMillis())
        vm_addExpenses!!.dateTime = simpleDateTimeFormat.format(System.currentTimeMillis())
        binding.tvDateTimeAddExpense.text = vm_addExpenses!!.dateTime
    }

    private fun saveData() {
        if (TextUtils.isEmpty(binding.etExpenseAmountAddExpense.text)) {
            binding.etExpenseAmountAddExpense.error = getString(R.string.AmontNeeded)
        } else if (TextUtils.isEmpty(vm_addExpenses!!.category)) {
            Toast.makeText(context, getString(R.string.Pleaseselectacatagory), Toast.LENGTH_SHORT)
                .show()
        } else {
            val amount = binding.etExpenseAmountAddExpense.text.toString()
            var expenseDescription = ""
            if (!TextUtils.isEmpty(binding.etExpenseDescriptionAddExpense.text)) {
                expenseDescription = binding.etExpenseDescriptionAddExpense.text.toString()
            }
            val posts = MC_Posts(expenseDescription,
                vm_addExpenses!!.category,
                Constants.TYPE_EXPENSE,
                amount,
                vm_addExpenses!!.year!!,
                vm_addExpenses!!.month!!,
                vm_addExpenses!!.day!!,
                vm_addExpenses!!.time!!,
                System.currentTimeMillis().toString(),
                vm_addExpenses!!.dateTime)
            /* val helper = SQLiteHelper(this@AddExpense)
             helper.saveData(posts)*/
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    AppDatabase.getInstance(requireContext().applicationContext).dbDao().insertAll(posts)
                    CoroutineScope(Dispatchers.Main).launch {
                        UtilDB.currentBalance = UtilDB.currentBalance - amount.toInt()
                        startActivity(Intent(context, MainActivity::class.java))
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                    }
                }

            }
            catch (e:Exception){
                Toast.makeText(requireContext(), "Failed ${e.message}::: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun observe() {
        vm_addExpenses!!.mutable_category.observe(viewLifecycleOwner, { s: String ->
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
        vm_addExpenses!!.mutable_amount.observe(viewLifecycleOwner, { s ->
            if (s == Constants.AMOUNT_500) {
                binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_500)
            } else if (s == Constants.AMOUNT_1000) {
                binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_1000)
            } else if (s == Constants.AMOUNT_1500) {
                binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_1500)
            } else if (s == Constants.AMOUNT_2000) {
                binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_2000)
            } else if (s == Constants.AMOUNT_2500) {
                binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_2500)
            } else if (s == Constants.AMOUNT_3000) {
                binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_3000)
            } else if (s == Constants.AMOUNT_4000) {
                binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_4000)
            } else if (s == Constants.AMOUNT_5000) {
                binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_5000)
            } else if (s == Constants.AMOUNT_10000) {
                binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_10000)
            } else if (s == Constants.AMOUNT_20000) {
                binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_20000)
            } else if (s == Constants.AMOUNT_30000) {
                binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_30000)
            } else if (s == Constants.AMOUNT_40000) {
                binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_40000)
            } else if (s == Constants.AMOUNT_50000) {
                binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_50000)
            } else if (s == Constants.AMOUNT_100000) {
                binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_100000)
            } else if (s == Constants.AMOUNT_200000) {
                binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_200000)
            } else if (s == Constants.AMOUNT_300000) {
                binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_300000)
            } else if (s == Constants.AMOUNT_400000) {
                binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_400000)
            } else if (s == Constants.AMOUNT_500000) {
                binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_500000)
            }
        })
    }

    override fun onClick(v: View) {
        if (v.id == R.id.cv_amount500_AddExpense) {
            vm_addExpenses!!.amount = Constants.AMOUNT_500
            vm_addExpenses!!.mutable_amount.setValue(vm_addExpenses!!.amount)
        } else if (v.id == R.id.cv_amount1000_AddExpense) {
            vm_addExpenses!!.amount = Constants.AMOUNT_1000
            vm_addExpenses!!.mutable_amount.setValue(vm_addExpenses!!.amount)
        } else if (v.id == R.id.cv_amount1500_AddExpense) {
            vm_addExpenses!!.amount = Constants.AMOUNT_1500
            vm_addExpenses!!.mutable_amount.setValue(vm_addExpenses!!.amount)
        } else if (v.id == R.id.cv_amount2000_AddExpense) {
            vm_addExpenses!!.amount = Constants.AMOUNT_2000
            vm_addExpenses!!.mutable_amount.setValue(vm_addExpenses!!.amount)
        } else if (v.id == R.id.cv_amount2500_AddExpense) {
            vm_addExpenses!!.amount = Constants.AMOUNT_2500
            vm_addExpenses!!.mutable_amount.setValue(vm_addExpenses!!.amount)
        } else if (v.id == R.id.cv_amount3000_AddExpense) {
            vm_addExpenses!!.amount = Constants.AMOUNT_3000
            vm_addExpenses!!.mutable_amount.setValue(vm_addExpenses!!.amount)
        } else if (v.id == R.id.cv_amount3500_AddExpense) {
            vm_addExpenses!!.amount = Constants.AMOUNT_3500
            vm_addExpenses!!.mutable_amount.setValue(vm_addExpenses!!.amount)
        } else if (v.id == R.id.cv_amount4000_AddExpense) {
            vm_addExpenses!!.amount = Constants.AMOUNT_4000
            vm_addExpenses!!.mutable_amount.setValue(vm_addExpenses!!.amount)
        } else if (v.id == R.id.cv_amount5000_AddExpense) {
            vm_addExpenses!!.amount = Constants.AMOUNT_5000
            vm_addExpenses!!.mutable_amount.setValue(vm_addExpenses!!.amount)
        } else if (v.id == R.id.cv_amount10000_AddExpense) {
            vm_addExpenses!!.amount = Constants.AMOUNT_10000
            vm_addExpenses!!.mutable_amount.setValue(vm_addExpenses!!.amount)
        } else if (v.id == R.id.cv_amount20000_AddExpense) {
            vm_addExpenses!!.amount = Constants.AMOUNT_20000
            vm_addExpenses!!.mutable_amount.setValue(vm_addExpenses!!.amount)
        } else if (v.id == R.id.cv_amount30000_AddExpense) {
            vm_addExpenses!!.amount = Constants.AMOUNT_30000
            vm_addExpenses!!.mutable_amount.setValue(vm_addExpenses!!.amount)
        } else if (v.id == R.id.cv_amount40000_AddExpense) {
            vm_addExpenses!!.amount = Constants.AMOUNT_40000
            vm_addExpenses!!.mutable_amount.setValue(vm_addExpenses!!.amount)
        } else if (v.id == R.id.cv_amount50000_AddExpense) {
            vm_addExpenses!!.amount = Constants.AMOUNT_50000
            vm_addExpenses!!.mutable_amount.setValue(vm_addExpenses!!.amount)
        } else if (v.id == R.id.cv_amount100000_AddExpense) {
            vm_addExpenses!!.amount = Constants.AMOUNT_100000
            vm_addExpenses!!.mutable_amount.setValue(vm_addExpenses!!.amount)
        } else if (v.id == R.id.cv_amount200000_AddExpense) {
            vm_addExpenses!!.amount = Constants.AMOUNT_200000
            vm_addExpenses!!.mutable_amount.setValue(vm_addExpenses!!.amount)
        } else if (v.id == R.id.cv_amount300000_AddExpense) {
            vm_addExpenses!!.amount = Constants.AMOUNT_300000
            vm_addExpenses!!.mutable_amount.setValue(vm_addExpenses!!.amount)
        } else if (v.id == R.id.cv_amount400000_AddExpense) {
            vm_addExpenses!!.amount = Constants.AMOUNT_400000
            vm_addExpenses!!.mutable_amount.setValue(vm_addExpenses!!.amount)
        } else if (v.id == R.id.cv_amount500000_AddExpense) {
            vm_addExpenses!!.amount = Constants.AMOUNT_500000
            vm_addExpenses!!.mutable_amount.setValue(vm_addExpenses!!.amount)
        } else if (v.id == R.id.cv_food_AddExpense) {
            vm_addExpenses!!.category = Constants.CATEGORY_FOOD
            vm_addExpenses!!.mutable_category.setValue(vm_addExpenses!!.category)
        } else if (v.id == R.id.cv_business_AddExpense) {
            vm_addExpenses!!.category = Constants.CATEGORY_BUSINESS
            vm_addExpenses!!.mutable_category.setValue(vm_addExpenses!!.category)
        } else if (v.id == R.id.cv_houseRent_AddExpense) {
            vm_addExpenses!!.category = Constants.CATEGORY_HOUSE_RENT
            vm_addExpenses!!.mutable_category.setValue(vm_addExpenses!!.category)
        } else if (v.id == R.id.cv_transport_AddExpense) {
            vm_addExpenses!!.category = Constants.CATEGORY_TRANSPORT
            vm_addExpenses!!.mutable_category.setValue(vm_addExpenses!!.category)
        } else if (v.id == R.id.cv_cloths_AddExpense) {
            vm_addExpenses!!.category = Constants.CATEGORY_CLOTHS
            vm_addExpenses!!.mutable_category.setValue(vm_addExpenses!!.category)
        } else if (v.id == R.id.cv_bills_AddExpense) {
            vm_addExpenses!!.category = Constants.CATEGORY_BILLS
            vm_addExpenses!!.mutable_category.setValue(vm_addExpenses!!.category)
        } else if (v.id == R.id.cv_education_AddExpense) {
            vm_addExpenses!!.category = Constants.CATEGORY_EDUCATION
            vm_addExpenses!!.mutable_category.setValue(vm_addExpenses!!.category)
        } else if (v.id == R.id.cv_lifeStyle_AddExpense) {
            vm_addExpenses!!.category = Constants.CATEGORY_LIFESTYLE
            vm_addExpenses!!.mutable_category.setValue(vm_addExpenses!!.category)
        } else if (v.id == R.id.cv_medicine_AddExpense) {
            vm_addExpenses!!.category = Constants.CATEGORY_MEDICINE
            vm_addExpenses!!.mutable_category.setValue(vm_addExpenses!!.category)
        } else if (v.id == R.id.cv_other_AddExpense) {
            vm_addExpenses!!.category = Constants.CATEGORY_OTHER
            vm_addExpenses!!.mutable_category.value = vm_addExpenses!!.category
        }
    }

    private fun markOther() {
        binding.cvFoodAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBusinessAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvHouseRentAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvTransportAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvClothsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBillsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvEducationAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvLifeStyleAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvMedicineAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvOtherAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.pink))
    }

    private fun markMedicine() {
        binding.cvFoodAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBusinessAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvHouseRentAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvTransportAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvClothsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBillsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvEducationAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvLifeStyleAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvMedicineAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.pink))
        binding.cvOtherAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
    }

    private fun markLifeStyle() {
        binding.cvFoodAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBusinessAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvHouseRentAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvTransportAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvClothsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBillsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvEducationAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvLifeStyleAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.pink))
        binding.cvMedicineAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvOtherAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
    }

    private fun markEducation() {
        binding.cvFoodAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBusinessAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvHouseRentAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvTransportAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvClothsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBillsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvEducationAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.pink))
        binding.cvLifeStyleAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvMedicineAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvOtherAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
    }

    private fun markBills() {
        binding.cvFoodAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBusinessAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvHouseRentAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvTransportAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvClothsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBillsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.pink))
        binding.cvEducationAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvLifeStyleAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvMedicineAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvOtherAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
    }

    private fun markCloths() {
        binding.cvFoodAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBusinessAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvHouseRentAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvTransportAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvClothsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.pink))
        binding.cvBillsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvEducationAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvLifeStyleAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvMedicineAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvOtherAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
    }

    private fun markTransport() {
        binding.cvFoodAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBusinessAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvHouseRentAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvTransportAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.pink))
        binding.cvClothsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBillsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvEducationAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvLifeStyleAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvMedicineAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvOtherAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
    }

    private fun markHouseRent() {
        binding.cvFoodAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBusinessAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvHouseRentAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.pink))
        binding.cvTransportAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvClothsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBillsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvEducationAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvLifeStyleAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvMedicineAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvOtherAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
    }

    private fun markBusiness() {
        binding.cvFoodAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBusinessAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.pink))
        binding.cvHouseRentAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvTransportAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvClothsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBillsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvEducationAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvLifeStyleAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvMedicineAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvOtherAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
    }

    private fun markFood() {
        binding.cvFoodAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.pink))
        binding.cvBusinessAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvHouseRentAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvTransportAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvClothsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBillsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvEducationAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvLifeStyleAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvMedicineAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvOtherAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
    }

    private fun changeDate() {
        val timePickerDialog =
            TimePickerDialog(requireContext(), { view: TimePicker?, hourOfDay: Int, minute: Int ->
                vm_addExpenses!!.time = "$hourOfDay : $minute"
                Log.d("tag", "hour$hourOfDay")
                if (hourOfDay >= 12) {
                    am_pm = "pm"
                    hourInString = (hourOfDay - 12).toString()
                } else {
                    am_pm = "am"
                    hourInString = hourOfDay.toString()
                }
                vm_addExpenses!!.dateTime =
                    vm_addExpenses!!.day + "-" + vm_addExpenses!!.month + "-" + vm_addExpenses!!.year + "  " + hourInString + ":" + minute.toString() + " " + am_pm
                //Log.d("tag","year"+vm_addIncome.year+" month "+vm_addIncome.month+" day "+vm_addIncome.day+" hour "+hourOfDay+"min "+minute+" "+am_pm);
                binding.tvDateTimeAddExpense.text = vm_addExpenses!!.dateTime
            }, hour, minute, true)
        val datePickerDialog = DatePickerDialog(requireContext(), { view, year, month, dayOfMonth ->
            setDay(dayOfMonth)
            setMonth(month + 1)
            timePickerDialog.show()
        }, year, month, day)
        datePickerDialog.show()
    }

    private fun setDay(day: Int) {
        if (day == 1) {
            vm_addExpenses!!.day = getString(R.string.digit01)
        } else if (day == 2) {
            vm_addExpenses!!.day = getString(R.string.digit02)
        } else if (day == 3) {
            vm_addExpenses!!.day = getString(R.string.digit03)
        } else if (day == 4) {
            vm_addExpenses!!.day = getString(R.string.digit04)
        } else if (day == 5) {
            vm_addExpenses!!.day = getString(R.string.digit05)
        } else if (day == 6) {
            vm_addExpenses!!.day = getString(R.string.digit06)
        } else if (day == 7) {
            vm_addExpenses!!.day = getString(R.string.digit07)
        } else if (day == 8) {
            vm_addExpenses!!.day = getString(R.string.digit08)
        } else if (day == 9) {
            vm_addExpenses!!.day = getString(R.string.digit09)
        } else {
            vm_addExpenses!!.day = day.toString()
        }
    }

    private fun setMonth(month: Int) {
        if (month == 1) {
            vm_addExpenses!!.month = getString(R.string.digit01)
        } else if (month == 2) {
            vm_addExpenses!!.month = getString(R.string.digit02)
        } else if (month == 3) {
            vm_addExpenses!!.month = getString(R.string.digit03)
        } else if (month == 4) {
            vm_addExpenses!!.month = getString(R.string.digit04)
        } else if (month == 5) {
            vm_addExpenses!!.month = getString(R.string.digit05)
        } else if (month == 6) {
            vm_addExpenses!!.month = getString(R.string.digit06)
        } else if (month == 7) {
            vm_addExpenses!!.month = getString(R.string.digit07)
        } else if (month == 8) {
            vm_addExpenses!!.month = getString(R.string.digit08)
        } else if (month == 9) {
            vm_addExpenses!!.month = getString(R.string.digit09)
        } else {
            vm_addExpenses!!.month = month.toString()
        }
    }


    private fun goBack(){
        navCon.navigateUp()
    }

}