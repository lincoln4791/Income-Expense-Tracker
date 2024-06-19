package com.lincoln4791.dailyexpensemanager.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.lincoln4791.dailyexpensemanager.base.BaseFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.itmedicus.patientaid.ads.admobAdsUpdated.AdMobUtil
import com.lincoln4791.dailyexpensemanager.common.util.CurrentDate
import com.lincoln4791.dailyexpensemanager.Adapters.Adapter_AddExpense
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.admobAdsUpdated.AdUnitIds
import com.lincoln4791.dailyexpensemanager.admobAdsUpdated.NativeAdUtil
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.common.PrefManager
import com.lincoln4791.dailyexpensemanager.common.util.Util
import com.lincoln4791.dailyexpensemanager.common.util.GlobalVariabls
import com.lincoln4791.dailyexpensemanager.databinding.AddExpenseFragmentBinding
import com.lincoln4791.dailyexpensemanager.model.MC_Cards
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import com.lincoln4791.dailyexpensemanager.roomDB.AppDatabase
import com.lincoln4791.dailyexpensemanager.viewModels.VMAddExpenses
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AddExpenseFragment : BaseFragment<AddExpenseFragmentBinding>(AddExpenseFragmentBinding::inflate), View.OnClickListener {
    @Inject lateinit var repository: Repository
    @Inject lateinit var prefManager: PrefManager

    val vmAddExpenses by viewModels<VMAddExpenses>()
    private lateinit var navCon : NavController

    private var tempHour = 0
    private var tempMinute = 0
    private var tempYear = 0
    private var tempMonth = 0
    private var tempDay = 0
    private var amPm: String? = null
    private var hourInString: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LifeCycle", "AddExpense Fragment create")
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    Log.d("tag","OnBackPressCalled -> Monthly")
                  goBack()

                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }


    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("LifeCycle", "AddExpense Fragment ViewCreated")
        unloadProgressBar()
        showNativeAdd()
        Util.recordScreenEvent("add_expense_fragment","MainActivity")
        navCon = Navigation.findNavController(view)
        initialization()

        binding.cvBackAddExpense.setOnClickListener {
            goBack()
        }

        binding.cvImg.setOnClickListener {
            goBack()
        }

        binding.cvAddMoreAddExpense.setOnClickListener {
            addMoreCard()
        }

        binding.cvCalculatorAddExpense.setOnClickListener {
            Toast.makeText(requireContext(),"Coming Soon",Toast.LENGTH_SHORT).show()
        }

        vmAddExpenses.loadAllCards()

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
        //binding.cvAddMoreAddExpense.setOnClickListener(this)
        binding.cvTransportAddExpense.setOnClickListener(this)
        binding.cvOtherAddExpense.setOnClickListener(this)
        binding.cvSaveAddExpense.setOnClickListener { saveData() }
        binding.tvDateTimeAddExpense.setOnClickListener { changeDate() }

        binding.tvCurrentBalanceValueToolBarAddExpense.text = GlobalVariabls.currentBalance.toString()
        observe()
        vmAddExpenses.setDateTime()

    }

    private fun initialization() {
        val calendar = Calendar.getInstance()
        tempYear = calendar[Calendar.YEAR]
        tempMonth = calendar[Calendar.MONTH]
        tempDay = calendar[Calendar.DAY_OF_MONTH]
        val simpleHourFormat = SimpleDateFormat("hh")
        val simpleMinuteFormat = SimpleDateFormat("mm")
        tempHour = simpleHourFormat.format(System.currentTimeMillis()).toInt()
        tempMinute = simpleMinuteFormat.format(System.currentTimeMillis()).toInt()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateUI(data: List<MC_Cards>, callback: (isUIUpdated : Boolean) -> Unit) {
        val layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        val myAdapter = Adapter_AddExpense(data,requireContext(),this)
        binding.recyclerView.layoutManager=layoutManager
        binding.recyclerView.adapter = myAdapter
        myAdapter.notifyDataSetChanged()
        callback(true)
    }



    private fun saveData() {
        if (TextUtils.isEmpty(binding.etExpenseAmountAddExpense.text)) {
            binding.etExpenseAmountAddExpense.error = getString(R.string.AmontNeeded)
        } else if (TextUtils.isEmpty(vmAddExpenses.category)) {
            Toast.makeText(context, getString(R.string.Pleaseselectacatagory), Toast.LENGTH_SHORT)
                .show()
        } else {
            val amount = binding.etExpenseAmountAddExpense.text.toString()
            var expenseDescription = ""
            if (!TextUtils.isEmpty(binding.etExpenseDescriptionAddExpense.text)) {
                expenseDescription = binding.etExpenseDescriptionAddExpense.text.toString()
            }
            val posts = MC_Posts(expenseDescription,
                vmAddExpenses.category,
                Constants.TYPE_EXPENSE,
                amount.toInt(),
                vmAddExpenses.year!!,
                vmAddExpenses.month!!,
                vmAddExpenses.day!!,
                vmAddExpenses.time!!,
                System.currentTimeMillis().toString(),
                vmAddExpenses.dateTime.value!!)
            vmAddExpenses.insertExpense(posts)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun observe() {
        vmAddExpenses.mutable_category.observe(viewLifecycleOwner) { s: String ->
            when (s) {
                Constants.CATEGORY_FOOD -> {
                    markFood()
                }
                Constants.CATEGORY_TRANSPORT -> {
                    markTransport()
                }
                Constants.CATEGORY_BILLS -> {
                    markBills()
                }
                Constants.CATEGORY_HOUSE_RENT -> {
                    markHouseRent()
                }
                Constants.CATEGORY_BUSINESS -> {
                    markBusiness()
                }
                Constants.CATEGORY_MEDICINE -> {
                    markMedicine()
                }
                Constants.CATEGORY_CLOTHS -> {
                    addMoreCard()
                }
                Constants.CATEGORY_EDUCATION -> {
                    markEducation()
                }
                Constants.CATEGORY_LIFESTYLE -> {
                    markLifeStyle()
                }
                Constants.CATEGORY_OTHER -> {
                    markOther()
                }
            }
        }

        vmAddExpenses.mutable_amount.observe(viewLifecycleOwner) { s ->
            when (s) {
                Constants.AMOUNT_500 -> {
                    binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_500)
                }
                Constants.AMOUNT_1000 -> {
                    binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_1000)
                }
                Constants.AMOUNT_1500 -> {
                    binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_1500)
                }
                Constants.AMOUNT_2000 -> {
                    binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_2000)
                }
                Constants.AMOUNT_2500 -> {
                    binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_2500)
                }
                Constants.AMOUNT_3000 -> {
                    binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_3000)
                }
                Constants.AMOUNT_4000 -> {
                    binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_4000)
                }
                Constants.AMOUNT_5000 -> {
                    binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_5000)
                }
                Constants.AMOUNT_10000 -> {
                    binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_10000)
                }
                Constants.AMOUNT_20000 -> {
                    binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_20000)
                }
                Constants.AMOUNT_30000 -> {
                    binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_30000)
                }
                Constants.AMOUNT_40000 -> {
                    binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_40000)
                }
                Constants.AMOUNT_50000 -> {
                    binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_50000)
                }
                Constants.AMOUNT_100000 -> {
                    binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_100000)
                }
                Constants.AMOUNT_200000 -> {
                    binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_200000)
                }
                Constants.AMOUNT_300000 -> {
                    binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_300000)
                }
                Constants.AMOUNT_400000 -> {
                    binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_400000)
                }
                Constants.AMOUNT_500000 -> {
                    binding.etExpenseAmountAddExpense.setText(Constants.AMOUNT_500000)
                }
            }
        }

        vmAddExpenses.postsList.observe(viewLifecycleOwner){
            Log.d("addExpense", "observed")
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                is Resource.Success<*> ->  updateUI(it.value as List<MC_Cards>){

                }
                is Resource.Failure ->{
                    Toast.makeText(context,"Something is Wrong", Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }

        vmAddExpenses.adExpenseFlag.observe(viewLifecycleOwner){
            if(it==true){
                vmAddExpenses.adjustBalance(binding.etExpenseAmountAddExpense.text.toString().toInt())
                Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                navCon.navigateUp()
            }
            else{
                Toast.makeText(requireContext(), "Something is wrong, Try Again Later", Toast.LENGTH_SHORT).show()
                navCon.navigateUp()
            }
        }

        vmAddExpenses.dateTime.observe(viewLifecycleOwner){
            binding.tvDateTimeAddExpense.text = it
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.cv_amount500_AddExpense -> {
                vmAddExpenses.amount = Constants.AMOUNT_500
                vmAddExpenses.mutable_amount.setValue(vmAddExpenses.amount)
            }
            R.id.cv_amount1000_AddExpense -> {
                vmAddExpenses.amount = Constants.AMOUNT_1000
                vmAddExpenses.mutable_amount.setValue(vmAddExpenses.amount)
            }
            R.id.cv_amount1500_AddExpense -> {
                vmAddExpenses.amount = Constants.AMOUNT_1500
                vmAddExpenses.mutable_amount.setValue(vmAddExpenses.amount)
            }
            R.id.cv_amount2000_AddExpense -> {
                vmAddExpenses.amount = Constants.AMOUNT_2000
                vmAddExpenses.mutable_amount.setValue(vmAddExpenses.amount)
            }
            R.id.cv_amount2500_AddExpense -> {
                vmAddExpenses.amount = Constants.AMOUNT_2500
                vmAddExpenses.mutable_amount.setValue(vmAddExpenses.amount)
            }
            R.id.cv_amount3000_AddExpense -> {
                vmAddExpenses.amount = Constants.AMOUNT_3000
                vmAddExpenses.mutable_amount.setValue(vmAddExpenses.amount)
            }
            R.id.cv_amount3500_AddExpense -> {
                vmAddExpenses.amount = Constants.AMOUNT_3500
                vmAddExpenses.mutable_amount.setValue(vmAddExpenses.amount)
            }
            R.id.cv_amount4000_AddExpense -> {
                vmAddExpenses.amount = Constants.AMOUNT_4000
                vmAddExpenses.mutable_amount.setValue(vmAddExpenses.amount)
            }
            R.id.cv_amount5000_AddExpense -> {
                vmAddExpenses.amount = Constants.AMOUNT_5000
                vmAddExpenses.mutable_amount.setValue(vmAddExpenses.amount)
            }
            R.id.cv_amount10000_AddExpense -> {
                vmAddExpenses.amount = Constants.AMOUNT_10000
                vmAddExpenses.mutable_amount.setValue(vmAddExpenses.amount)
            }
            R.id.cv_amount20000_AddExpense -> {
                vmAddExpenses.amount = Constants.AMOUNT_20000
                vmAddExpenses.mutable_amount.setValue(vmAddExpenses.amount)
            }
            R.id.cv_amount30000_AddExpense -> {
                vmAddExpenses.amount = Constants.AMOUNT_30000
                vmAddExpenses.mutable_amount.setValue(vmAddExpenses.amount)
            }
            R.id.cv_amount40000_AddExpense -> {
                vmAddExpenses.amount = Constants.AMOUNT_40000
                vmAddExpenses.mutable_amount.setValue(vmAddExpenses.amount)
            }
            R.id.cv_amount50000_AddExpense -> {
                vmAddExpenses.amount = Constants.AMOUNT_50000
                vmAddExpenses.mutable_amount.setValue(vmAddExpenses.amount)
            }
            R.id.cv_amount100000_AddExpense -> {
                vmAddExpenses.amount = Constants.AMOUNT_100000
                vmAddExpenses.mutable_amount.setValue(vmAddExpenses.amount)
            }
            R.id.cv_amount200000_AddExpense -> {
                vmAddExpenses.amount = Constants.AMOUNT_200000
                vmAddExpenses.mutable_amount.setValue(vmAddExpenses.amount)
            }
            R.id.cv_amount300000_AddExpense -> {
                Constants.AMOUNT_300000.also { vmAddExpenses.amount = it }
                vmAddExpenses.mutable_amount.setValue(vmAddExpenses.amount)
            }
            R.id.cv_amount400000_AddExpense -> {
                vmAddExpenses.amount = Constants.AMOUNT_400000
                vmAddExpenses.mutable_amount.setValue(vmAddExpenses.amount)
            }
            R.id.cv_amount500000_AddExpense -> {
                vmAddExpenses.amount = Constants.AMOUNT_500000
                vmAddExpenses.mutable_amount.setValue(vmAddExpenses.amount)
            }
            R.id.cv_food_AddExpense -> {
                vmAddExpenses.category = Constants.CATEGORY_FOOD
                vmAddExpenses.mutable_category.setValue(vmAddExpenses.category)
            }
            R.id.cv_business_AddExpense -> {
                vmAddExpenses.category = Constants.CATEGORY_BUSINESS
                vmAddExpenses.mutable_category.setValue(vmAddExpenses.category)
            }
            R.id.cv_houseRent_AddExpense -> {
                vmAddExpenses.category = Constants.CATEGORY_HOUSE_RENT
                vmAddExpenses.mutable_category.setValue(vmAddExpenses.category)
            }
            R.id.cv_transport_AddExpense -> {
                vmAddExpenses.category = Constants.CATEGORY_TRANSPORT
                vmAddExpenses.mutable_category.setValue(vmAddExpenses.category)
            }
            /* else if (v.id == R.id.cv_cloths_AddExpense) {
                 vm_addExpenses!!.category = Constants.CATEGORY_CLOTHS
                 vm_addExpenses!!.mutable_category.setValue(vm_addExpenses!!.category)
             }*/
            R.id.cv_bills_AddExpense -> {
                vmAddExpenses.category = Constants.CATEGORY_BILLS
                vmAddExpenses.mutable_category.setValue(vmAddExpenses.category)
            }
            R.id.cv_education_AddExpense -> {
                vmAddExpenses.category = Constants.CATEGORY_EDUCATION
                vmAddExpenses.mutable_category.setValue(vmAddExpenses.category)
            }
            R.id.cv_lifeStyle_AddExpense -> {
                vmAddExpenses.category = Constants.CATEGORY_LIFESTYLE
                vmAddExpenses.mutable_category.setValue(vmAddExpenses.category)
            }
            R.id.cv_medicine_AddExpense -> {
                vmAddExpenses.category = Constants.CATEGORY_MEDICINE
                vmAddExpenses.mutable_category.setValue(vmAddExpenses.category)
            }
            R.id.cv_other_AddExpense -> {
                vmAddExpenses.category = Constants.CATEGORY_OTHER
                vmAddExpenses.mutable_category.value = vmAddExpenses.category
            }
        }
    }

    private fun markOther() {
        binding.cvFoodAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBusinessAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvHouseRentAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvTransportAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvAddMoreAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBillsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvEducationAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvLifeStyleAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvMedicineAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvOtherAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.pink))
        deSelectedMoreCard()



    }

    private fun markMedicine() {
        deSelectedMoreCard()
        binding.cvFoodAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBusinessAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvHouseRentAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvTransportAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvAddMoreAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBillsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvEducationAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvLifeStyleAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvMedicineAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.pink))
        binding.cvOtherAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
    }

    private fun markLifeStyle() {
        deSelectedMoreCard()
        binding.cvFoodAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBusinessAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvHouseRentAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvTransportAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvAddMoreAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBillsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvEducationAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvLifeStyleAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.pink))
        binding.cvMedicineAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvOtherAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
    }

    private fun markEducation() {
        deSelectedMoreCard()
        binding.cvFoodAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBusinessAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvHouseRentAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvTransportAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvAddMoreAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBillsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvEducationAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.pink))
        binding.cvLifeStyleAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvMedicineAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvOtherAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
    }

    private fun markBills() {
        deSelectedMoreCard()
        binding.cvFoodAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBusinessAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvHouseRentAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvTransportAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvAddMoreAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBillsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.pink))
        binding.cvEducationAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvLifeStyleAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvMedicineAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvOtherAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
    }

    @SuppressLint("InflateParams")
    private fun addMoreCard() {
        deSelectedMoreCard()
        deselectAllCard()
        vmAddExpenses.category=""

        Log.d("tag","Add Card Called")

        val viewAddCard = layoutInflater.inflate(R.layout.dialog_add_more_card,null,false)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(viewAddCard)
        dialog.show()

        val tvCard = viewAddCard.findViewById<TextView>(R.id.tv_cardName)

        viewAddCard.findViewById<Button>(R.id.btn).setOnClickListener {
            if(!tvCard.text.isNullOrEmpty()){
                dialog.dismiss()

                var existingCardList : MutableList<MC_Cards>? = mutableListOf()
                val job1= CoroutineScope(Dispatchers.IO).launch {
                    try {
                        existingCardList = AppDatabase.getInstance(requireContext().applicationContext).dbDao().loadAllExpenseCards(Constants.TYPE_EXPENSE)
                    }
                    catch (e:Exception){
                        e.printStackTrace()
                    }
                }
                runBlocking {
                    var isExists = false
                    job1.join()
                    for(card in existingCardList!!){
                        if(card.cardName == tvCard.text.toString()){
                            isExists = true
                            break
                        }
                    }

                    if(!isExists){
                        val job2 = CoroutineScope(Dispatchers.IO).launch {
                            try {
                                AppDatabase.getInstance(requireContext().applicationContext).dbDao().insertCard(
                                    MC_Cards(tvCard.text.toString(),Constants.TYPE_EXPENSE))


                            }
                            catch (e:Exception){
                                e.printStackTrace()
                            }
                        }

                        runBlocking {
                            job2.join()
                            vmAddExpenses.loadAllCards()
                            selectMoreCard(tvCard.text.toString())

                        }
                    }
                    else{
                        Toast.makeText(requireContext(),"Card Already Exists",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun deselectAllCard() {
        binding.cvFoodAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBusinessAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvHouseRentAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvTransportAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        //binding.cvAddMoreAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.pink))
        binding.cvBillsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvEducationAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvLifeStyleAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvMedicineAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvOtherAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
    }

    private fun markTransport() {
        deSelectedMoreCard()
        binding.cvFoodAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBusinessAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvHouseRentAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvTransportAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.pink))
        binding.cvAddMoreAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBillsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvEducationAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvLifeStyleAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvMedicineAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvOtherAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
    }

    private fun markHouseRent() {
        deSelectedMoreCard()
        binding.cvFoodAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBusinessAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvHouseRentAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.pink))
        binding.cvTransportAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvAddMoreAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBillsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvEducationAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvLifeStyleAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvMedicineAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvOtherAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
    }

    private fun markBusiness() {
        deSelectedMoreCard()
        binding.cvFoodAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBusinessAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.pink))
        binding.cvHouseRentAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvTransportAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvAddMoreAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBillsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvEducationAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvLifeStyleAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvMedicineAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvOtherAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
    }

    private fun markFood() {
        deSelectedMoreCard()
        binding.cvFoodAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.pink))
        binding.cvBusinessAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvHouseRentAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvTransportAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvAddMoreAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBillsAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvEducationAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvLifeStyleAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvMedicineAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvOtherAddExpense.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
    }

    private fun changeDate() {
        val timePickerDialog =
            TimePickerDialog(requireContext(), { _: TimePicker?, hourOfDay: Int, minute: Int ->
                vmAddExpenses.time = "$hourOfDay : $minute"
                Log.d("tag", "hour$hourOfDay")
                if (hourOfDay >= 12) {
                    amPm = "pm"
                    hourInString = (hourOfDay - 12).toString()
                } else {
                    amPm = "am"
                    hourInString = hourOfDay.toString()
                }
                vmAddExpenses.dateTime.value =
                    vmAddExpenses.day + "-" + vmAddExpenses.month + "-" + vmAddExpenses.year + "  " + hourInString + ":" + minute.toString() + " " + amPm
            }, tempHour, tempMinute, true)
        val datePickerDialog = DatePickerDialog(requireContext(), { _, _, month, dayOfMonth ->
            setDay(dayOfMonth)
            setMonth(month + 1)
            timePickerDialog.show()
        }, tempYear, tempMonth, tempDay)
        datePickerDialog.show()
    }

    private fun setDay(day: Int) {
        when (day) {
            1 -> {
                vmAddExpenses.day = getString(R.string.digit01)
            }
            2 -> {
                vmAddExpenses.day = getString(R.string.digit02)
            }
            3 -> {
                vmAddExpenses.day = getString(R.string.digit03)
            }
            4 -> {
                vmAddExpenses.day = getString(R.string.digit04)
            }
            5 -> {
                vmAddExpenses.day = getString(R.string.digit05)
            }
            6 -> {
                vmAddExpenses.day = getString(R.string.digit06)
            }
            7 -> {
                vmAddExpenses.day = getString(R.string.digit07)
            }
            8 -> {
                vmAddExpenses.day = getString(R.string.digit08)
            }
            9 -> {
                vmAddExpenses.day = getString(R.string.digit09)
            }
            else -> {
                vmAddExpenses.day = day.toString()
            }
        }
    }

    private fun setMonth(month: Int) {
        when (month) {
            1 -> {
                vmAddExpenses.month = getString(R.string.digit01)
            }
            2 -> {
                vmAddExpenses.month = getString(R.string.digit02)
            }
            3 -> {
                vmAddExpenses.month = getString(R.string.digit03)
            }
            4 -> {
                vmAddExpenses.month = getString(R.string.digit04)
            }
            5 -> {
                vmAddExpenses.month = getString(R.string.digit05)
            }
            6 -> {
                vmAddExpenses.month = getString(R.string.digit06)
            }
            7 -> {
                vmAddExpenses.month = getString(R.string.digit07)
            }
            8 -> {
                vmAddExpenses.month = getString(R.string.digit08)
            }
            9 -> {
                vmAddExpenses.month = getString(R.string.digit09)
            }
            else -> {
                vmAddExpenses.month = month.toString()
            }
        }
    }

    fun deleteCardByName(cardName : String,callback : (isDeleted : Boolean)-> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                AppDatabase.getInstance(requireContext().applicationContext).dbDao().deleteExpenseCardByName(cardName,Constants.TYPE_EXPENSE)
                callback(true)
            }
            catch (e:Exception){
                e.printStackTrace()
                callback(false)
            }
        }
    }

    fun selectMoreCard(cardName : String){

        deselectAllCard()
        vmAddExpenses.category = cardName
        binding.tvSelectedMoreCard.text = cardName
        binding.cvSelectedMoreCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.pink))
        binding.cvSelectedMoreCard.visibility = View.VISIBLE
    }

    private fun deSelectedMoreCard(){
        binding.tvSelectedMoreCard.text = ""
        binding.cvSelectedMoreCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvSelectedMoreCard.visibility = View.INVISIBLE
    }

    private fun showNativeAdd() {
        val lastAdShowDate = prefManager.lastNativeAdShownAEF
        if (AdMobUtil.canAdShow(requireContext(), lastAdShowDate)) {
            val nativeAd = NativeAdUtil(requireContext().applicationContext)
            nativeAd.loadNativeAd(requireActivity(),
                binding.nativeAd,
                AdUnitIds.NATIVE_ADD_TRANSACTION) {
                if (it) {
                    binding.nativeAd.visibility = View.VISIBLE
                    Log.d("Native", "Native Ad Shown")
                    prefManager.lastNativeAdShownAEF = CurrentDate.currentTime24H
                }
            }
        }
        else{
            binding.nativeAd.visibility = View.GONE
        }
    }

    private fun goBack(){
        navCon.navigateUp()
    }


    override fun onDestroy() {
        Log.d("LifeCycle", "AddExpense Fragment Destroyed")
        super.onDestroy()
    }

    override fun onDestroyView() {
        Log.d("LifeCycle", "AddExpense Fragment DestroyedView")
        super.onDestroyView()
    }

    override fun onStop() {
        Log.d("LifeCycle", "AddExpense Fragment Stop")
        super.onStop()
    }

    override fun onPause() {
        Log.d("LifeCycle", "AddExpense Fragment Paused")
        super.onPause()
    }

    override fun onAttach(context: Context) {
        Log.d("LifeCycle", "AddExpense Fragment Attached")
        super.onAttach(context)
    }

    override fun onDetach() {
        Log.d("LifeCycle", "AddExpense Fragment Detached")
        super.onDetach()
    }

    override fun onStart() {
        Log.d("LifeCycle", "AddExpense Fragment Started")
        super.onStart()
    }

    override fun onResume() {
        Log.d("LifeCycle", "AddExpense Fragment resumed")
        super.onResume()
    }

    private fun loadProgressBar() {
        binding.mainLoadingBar.visibility = View.VISIBLE
        binding.clContainer.visibility=View.GONE
    }

    private fun unloadProgressBar(){
        binding.mainLoadingBar.visibility = View.GONE
        binding.clContainer.visibility=View.VISIBLE
    }

}