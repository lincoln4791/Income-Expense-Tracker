package com.lincoln4791.dailyexpensemanager.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import com.example.mybaseproject2.base.BaseFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.itmedicus.patientaid.ads.admobAdsUpdated.AdMobUtil
import com.lincoln4791.dailyexpensemanager.admobAdsUpdated.AdUnitIds
import com.lincoln4791.dailyexpensemanager.common.util.CurrentDate
import com.lincoln4791.dailyexpensemanager.Adapters.Adapter_AddIncome
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.admobAdsUpdated.NativeAdUtil
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.common.PrefManager
import com.lincoln4791.dailyexpensemanager.common.util.Util
import com.lincoln4791.dailyexpensemanager.common.util.GlobalVariabls
import com.lincoln4791.dailyexpensemanager.databinding.FragmentAddIncomeBinding
import com.lincoln4791.dailyexpensemanager.model.MC_Cards
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import com.lincoln4791.dailyexpensemanager.roomDB.AppDatabase
import com.lincoln4791.dailyexpensemanager.viewModels.VMAddIncome
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AddIncomeFragment : BaseFragment<FragmentAddIncomeBinding>(FragmentAddIncomeBinding::inflate), View.OnClickListener {

    @Inject lateinit var prefManager : PrefManager
    @Inject lateinit var repository : Repository
    val vmAddIncome by viewModels<VMAddIncome>()
    private lateinit var navCon : NavController

    private var hour = 0
    private var minute = 0
    private var year = 0
    private var month = 0
    private var day = 0
    var amPm: String? = null
    var hourInString: String? = null



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



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        unloadProgressBar()
        showNativeAdd()
        Util.recordScreenEvent("add_income_fragment","MainActivity")
        navCon = Navigation.findNavController(view)
        initialization()
        binding.cvImg.setOnClickListener {
            goBack()
        }

        binding.cvAddMore.setOnClickListener {
            addMoreCard()
        }

        binding.cvCalculatorAddIncome.setOnClickListener {
            Toast.makeText(requireContext(),"Coming Soon",Toast.LENGTH_SHORT).show()
        }

        vmAddIncome.loadAllCards()



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
        binding.tvDateTimeAddIncome.setOnClickListener { changeDate() }
        binding.cvSaveAddIncome.setOnClickListener { addIncome() }

        observe()
        vmAddIncome.setDateTime()
        binding.tvCurrentBalanceValueToolBarAddIncome.text = GlobalVariabls.currentBalance.toString()


    }

    @SuppressLint("SimpleDateFormat")
    private fun initialization() {
        val calendar = Calendar.getInstance()
        year = calendar[Calendar.YEAR]
        month = calendar[Calendar.MONTH]
        day = calendar[Calendar.DAY_OF_MONTH]
        val simpleHourFormat = SimpleDateFormat("hh")
        val simpleMinuteFormat = SimpleDateFormat("mm")
        hour = simpleHourFormat.format(System.currentTimeMillis()).toInt()
        minute = simpleMinuteFormat.format(System.currentTimeMillis()).toInt()
    }


    private fun addIncome() {
        if (TextUtils.isEmpty(binding.etAmountAddIncome.text)) {
            binding.etAmountAddIncome.error = getString(R.string.AmontNeeded)
        } else if (TextUtils.isEmpty(vmAddIncome.category)) {
            Toast.makeText(context, getString(R.string.Pleaseselectacatagory), Toast.LENGTH_SHORT)
                .show()
        } else {
            val amount = binding.etAmountAddIncome.text.toString()
            var expenseDescription = ""
            if (!TextUtils.isEmpty(binding.etIncomeDescriptionAddIncome.text)) {
                expenseDescription = binding.etIncomeDescriptionAddIncome.text.toString()
            }
            val posts = MC_Posts(expenseDescription,
                vmAddIncome.category,
                Constants.TYPE_INCOME,
                amount.toInt(),
                vmAddIncome.year!!,
                vmAddIncome.month!!,
                vmAddIncome.day!!,
                vmAddIncome.time!!,
                System.currentTimeMillis().toString(),
                vmAddIncome.dateTime.value!!)
                vmAddIncome.addIncome(posts)

        }
    }

    private fun observe() {
        vmAddIncome.mutableCategory.observe(viewLifecycleOwner) { s: String ->
            if (s == Constants.CATEGORY_SALARY) {
                markSalary()
            } else if (s == Constants.CATEGORY_BUSINESS) {
                markBusiness()
            } else if (s == Constants.CATEGORY_HOUSE_RENT) {
                markHouseRent()
            } else if (s == Constants.CATEGORY_OTHER) {
                markOther()
            }
        }
        vmAddIncome.mutableAmount.observe(viewLifecycleOwner) { s ->
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
        }

        vmAddIncome.postsList.observe(viewLifecycleOwner){
            Log.d("addExpense", "observed")
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                //is Resource.Success -> adapter_transactions = Adapter_Transactions(it.data, this)
                is Resource.Success<*> ->  updateUI(it.value as List<MC_Cards>){

                }
                //is Resource.Failure -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                else -> {

                }
            }
        }

        vmAddIncome.flagInsertIncome.observe(viewLifecycleOwner){
            if(it == true){
                vmAddIncome.adjustCurrentBalance(binding.etAmountAddIncome.text.toString().toInt())
                Toast.makeText(requireContext(),"Success",Toast.LENGTH_SHORT).show()
                goBack()
            }
            else{
                Toast.makeText(requireContext(),"Something is wrong",Toast.LENGTH_SHORT).show()
            }
        }



        vmAddIncome.dateTime.observe(viewLifecycleOwner){
            binding.tvDateTimeAddIncome.text = it
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.cv_amount500_AddIncome) {
            vmAddIncome.amount = Constants.AMOUNT_500
            vmAddIncome.mutableAmount.setValue(vmAddIncome.amount)
        } else if (v.id == R.id.cv_amount1000_AddIncome) {
            vmAddIncome.amount = Constants.AMOUNT_1000
            vmAddIncome.mutableAmount.setValue(vmAddIncome.amount)
        } else if (v.id == R.id.cv_amount1500_AddIncome) {
            vmAddIncome.amount = Constants.AMOUNT_1500
            vmAddIncome.mutableAmount.setValue(vmAddIncome.amount)
        } else if (v.id == R.id.cv_amount2000_AddIncome) {
            vmAddIncome.amount = Constants.AMOUNT_2000
            vmAddIncome.mutableAmount.setValue(vmAddIncome.amount)
        } else if (v.id == R.id.cv_amount2500_AddIncome) {
            vmAddIncome.amount = Constants.AMOUNT_2500
            vmAddIncome.mutableAmount.setValue(vmAddIncome.amount)
        } else if (v.id == R.id.cv_amount3000_AddIncome) {
            vmAddIncome.amount = Constants.AMOUNT_3000
            vmAddIncome.mutableAmount.setValue(vmAddIncome.amount)
        } else if (v.id == R.id.cv_amount3500_AddIncome) {
            vmAddIncome.amount = Constants.AMOUNT_3500
            vmAddIncome.mutableAmount.setValue(vmAddIncome.amount)
        } else if (v.id == R.id.cv_amount4000_AddIncome) {
            vmAddIncome.amount = Constants.AMOUNT_4000
            vmAddIncome.mutableAmount.setValue(vmAddIncome.amount)
        } else if (v.id == R.id.cv_amount5000_AddIncome) {
            vmAddIncome.amount = Constants.AMOUNT_5000
            vmAddIncome.mutableAmount.setValue(vmAddIncome.amount)
        } else if (v.id == R.id.cv_amount10000_AddIncome) {
            vmAddIncome.amount = Constants.AMOUNT_10000
            vmAddIncome.mutableAmount.setValue(vmAddIncome.amount)
        } else if (v.id == R.id.cv_amount20000_AddIncome) {
            vmAddIncome.amount = Constants.AMOUNT_20000
            vmAddIncome.mutableAmount.setValue(vmAddIncome.amount)
        } else if (v.id == R.id.cv_amount30000_AddIncome) {
            vmAddIncome.amount = Constants.AMOUNT_30000
            vmAddIncome.mutableAmount.setValue(vmAddIncome.amount)
        } else if (v.id == R.id.cv_amount40000_AddIncome) {
            vmAddIncome.amount = Constants.AMOUNT_40000
            vmAddIncome.mutableAmount.setValue(vmAddIncome.amount)
        } else if (v.id == R.id.cv_amount50000_AddIncome) {
            vmAddIncome.amount = Constants.AMOUNT_50000
            vmAddIncome.mutableAmount.setValue(vmAddIncome.amount)
        } else if (v.id == R.id.cv_amount100000_AddIncome) {
            vmAddIncome.amount = Constants.AMOUNT_100000
            vmAddIncome.mutableAmount.setValue(vmAddIncome.amount)
        } else if (v.id == R.id.cv_amount200000_AddIncome) {
            vmAddIncome.amount = Constants.AMOUNT_200000
            vmAddIncome.mutableAmount.setValue(vmAddIncome.amount)
        } else if (v.id == R.id.cv_amount300000_AddIncome) {
            vmAddIncome.amount = Constants.AMOUNT_300000
            vmAddIncome.mutableAmount.setValue(vmAddIncome.amount)
        } else if (v.id == R.id.cv_amount400000_AddIncome) {
            vmAddIncome.amount = Constants.AMOUNT_400000
            vmAddIncome.mutableAmount.setValue(vmAddIncome.amount)
        } else if (v.id == R.id.cv_amount500000_AddIncome) {
            vmAddIncome.amount = Constants.AMOUNT_500000
            vmAddIncome.mutableAmount.setValue(vmAddIncome.amount)
        } else if (v.id == R.id.cv_salary_AddIncome) {
            vmAddIncome.category = Constants.CATEGORY_SALARY
            with(vmAddIncome) { mutableCategory.setValue(this.category) }
        } else if (v.id == R.id.cv_business_AddIncome) {
            vmAddIncome.category = Constants.CATEGORY_BUSINESS
            with(vmAddIncome) { mutableCategory.setValue(this.category) }
        } else if (v.id == R.id.cv_houseRent_AddIncome) {
            vmAddIncome.category = Constants.CATEGORY_HOUSE_RENT
            vmAddIncome.mutableCategory.setValue(vmAddIncome.category)
        } else if (v.id == R.id.cv_other_AddIncome) {
            vmAddIncome.category = Constants.CATEGORY_OTHER
            vmAddIncome.mutableCategory.value = vmAddIncome.category
        }
    }

    private fun markOther() {
        deSelectedMoreCard()
        binding.cvSalaryAddIncome.setCardBackgroundColor(requireContext().getColor(R.color.white))
        binding.cvBusinessAddIncome.setCardBackgroundColor(requireContext().getColor(R.color.white))
        binding.cvHouseRentAddIncome.setCardBackgroundColor(requireContext().getColor(R.color.white))
        binding.cvOtherAddIncome.setCardBackgroundColor(requireContext().getColor(R.color.green))
    }

    private fun markHouseRent() {
        deSelectedMoreCard()
        binding.cvSalaryAddIncome.setCardBackgroundColor(requireContext().getColor(R.color.white))
        binding.cvBusinessAddIncome.setCardBackgroundColor(requireContext().getColor(R.color.white))
        binding.cvHouseRentAddIncome.setCardBackgroundColor(requireContext().getColor(R.color.green))
        binding.cvOtherAddIncome.setCardBackgroundColor(requireContext().getColor(R.color.white))
    }

    private fun markBusiness() {
        deSelectedMoreCard()
        binding.cvSalaryAddIncome.setCardBackgroundColor(requireContext().getColor(R.color.white))
        binding.cvBusinessAddIncome.setCardBackgroundColor(requireContext().getColor(R.color.green))
        binding.cvHouseRentAddIncome.setCardBackgroundColor(requireContext().getColor(R.color.white))
        binding.cvOtherAddIncome.setCardBackgroundColor(requireContext().getColor(R.color.white))
    }

    private fun markSalary() {
        deSelectedMoreCard()
        binding.cvSalaryAddIncome.setCardBackgroundColor(requireContext().getColor(R.color.green))
        binding.cvBusinessAddIncome.setCardBackgroundColor(requireContext().getColor(R.color.white))
        binding.cvHouseRentAddIncome.setCardBackgroundColor(requireContext().getColor(R.color.white))
        binding.cvOtherAddIncome.setCardBackgroundColor(requireContext().getColor(R.color.white))
    }

    private fun changeDate() {
        val timePickerDialog =
            TimePickerDialog(requireContext(), { _: TimePicker?, hourOfDay: Int, minute: Int ->
                vmAddIncome.time = "$hourOfDay : $minute"
                Log.d("tag", "hour$hourOfDay")
                if (hourOfDay >= 12) {
                    amPm = "pm"
                    hourInString = (hourOfDay - 12).toString()
                } else {
                    amPm = "am"
                    hourInString = hourOfDay.toString()
                }
                vmAddIncome.dateTime.value =
                    vmAddIncome.day + "-" + vmAddIncome.month + "-" + vmAddIncome.year + "  " + hourInString + ":" + minute.toString() + " " + amPm

            }, hour, minute, true)
        val datePickerDialog = DatePickerDialog(requireContext(), { _, _, month, dayOfMonth ->
            setDay(dayOfMonth)
            setMonth(month + 1)
            timePickerDialog.show()
        }, year, month, day)
        datePickerDialog.show()
    }

    private fun setDay(day: Int) {
        if (day == 1) {
            vmAddIncome.day = getString(R.string.digit01)
        } else if (day == 2) {
            vmAddIncome.day = getString(R.string.digit02)
        } else if (day == 3) {
            vmAddIncome.day = getString(R.string.digit03)
        } else if (day == 4) {
            vmAddIncome.day = getString(R.string.digit04)
        } else if (day == 5) {
            vmAddIncome.day = getString(R.string.digit05)
        } else if (day == 6) {
            vmAddIncome.day = getString(R.string.digit06)
        } else if (day == 7) {
            vmAddIncome.day = getString(R.string.digit07)
        } else if (day == 8) {
            vmAddIncome.day = getString(R.string.digit08)
        } else if (day == 9) {
            vmAddIncome.day = getString(R.string.digit09)
        } else {
            vmAddIncome.day = day.toString()
        }
    }

    private fun setMonth(month: Int) {
        if (month == 1) {
            vmAddIncome.month = getString(R.string.digit01)
        } else if (month == 2) {
            vmAddIncome.month = getString(R.string.digit02)
        } else if (month == 3) {
            vmAddIncome.month = getString(R.string.digit03)
        } else if (month == 4) {
            vmAddIncome.month = getString(R.string.digit04)
        } else if (month == 5) {
            vmAddIncome.month = getString(R.string.digit05)
        } else if (month == 6) {
            vmAddIncome.month = getString(R.string.digit06)
        } else if (month == 7) {
            vmAddIncome.month = getString(R.string.digit07)
        } else if (month == 8) {
            vmAddIncome.month = getString(R.string.digit08)
        } else if (month == 9) {
            vmAddIncome.month = getString(R.string.digit09)
        } else {
            vmAddIncome.month = month.toString()
        }
    }

    fun deleteCardByName(cardName : String,callback : (isDeleted : Boolean)-> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                AppDatabase.getInstance(requireContext().applicationContext).dbDao().deleteIncomeCardByName(cardName,Constants.TYPE_INCOME)
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
        vmAddIncome.category=cardName
        binding.tvSelectedMoreCard.text = cardName
        binding.cvSelectedMoreCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.pink))
        binding.cvSelectedMoreCard.visibility = View.VISIBLE
    }

    fun deSelectedMoreCard(){
        binding.tvSelectedMoreCard.text = ""
        binding.cvSelectedMoreCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvSelectedMoreCard.visibility = View.INVISIBLE
    }

    private fun goBack() {
       /* val action = AddIncomeFragmentDirections.actionAddIncomeFragmentToHomeFragment()
        navCon.navigate(action)*/
        navCon.navigateUp()
    }

    private fun deselectAllCard() {
        binding.cvSalaryAddIncome.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvBusinessAddIncome.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvHouseRentAddIncome.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.cvOtherAddIncome.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateUI(data: List<MC_Cards>, callback: (isUIUpdated : Boolean) -> Unit) {
        val layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL, false)
        val myAdapter = Adapter_AddIncome(data, requireContext(), this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = myAdapter
        myAdapter.notifyDataSetChanged()
        callback(true)
    }


    private fun addMoreCard() {
        deSelectedMoreCard()
        deselectAllCard()
        vmAddIncome.category=""

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
                        existingCardList = AppDatabase.getInstance(requireContext().applicationContext).dbDao().loadAllIncomeCards(Constants.TYPE_INCOME)
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
                                    MC_Cards(tvCard.text.toString(),Constants.TYPE_INCOME))

                            }
                            catch (e:Exception){
                                e.printStackTrace()
                            }
                        }

                        runBlocking {
                            job2.join()
                            vmAddIncome.loadAllCards()
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

    private fun showNativeAdd() {
        val lastAdShowDate = prefManager.lastNativeAdShownAIF

        if (AdMobUtil.canAdShow(requireContext(), lastAdShowDate)) {
            val nativeAd = NativeAdUtil(requireContext().applicationContext)
            nativeAd.loadNativeAd(requireActivity(),
                binding.nativeAd,
                AdUnitIds.NATIVE_ADD_INCOME) {
                if (it) {
                    binding.nativeAd.visibility = View.VISIBLE
                    Log.d("Native", "Native Ad Shown")
                    prefManager.lastNativeAdShownAIF = CurrentDate.currentTime24H
                }
            }
        }
        else{
            binding.nativeAd.visibility = View.GONE
        }
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