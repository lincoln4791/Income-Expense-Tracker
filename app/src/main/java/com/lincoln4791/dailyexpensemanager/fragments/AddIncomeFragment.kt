package com.lincoln4791.dailyexpensemanager.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.itmedicus.patientaid.ads.admobAdsUpdated.AdMobUtil
import com.lincoln4791.dailyexpensemanager.admobAdsUpdated.AdUnitIds
import com.lincoln4791.dailyexpensemanager.common.util.CurrentDate
import com.lincoln4791.dailyexpensemanager.Adapters.Adapter_AddIncome
import com.lincoln4791.dailyexpensemanager.R
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
import com.lincoln4791.dailyexpensemanager.viewModels.VM_AddIncome
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class AddIncomeFragment : Fragment(), View.OnClickListener {
    private var hour = 0
    private var minute = 0
    private var year = 0
    private var month = 0
    private var day = 0
    var am_pm: String? = null
    var hourInString: String? = null

    lateinit var vm_addIncome : VM_AddIncome
    private lateinit var binding : FragmentAddIncomeBinding
    private lateinit var navCon : NavController
    private lateinit var prefManager : PrefManager

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
    ): View {
        prefManager = PrefManager(requireContext())
        // Inflate the layout for this fragment
        binding = FragmentAddIncomeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showNativeAdd()
        Util.recordScreenEvent("add_income_fragment","MainActivity")

        vm_addIncome = ViewModelProvider(this)[VM_AddIncome::class.java]
        navCon = Navigation.findNavController(view)

        val calendar = Calendar.getInstance()
        year = calendar[Calendar.YEAR]
        month = calendar[Calendar.MONTH]
        day = calendar[Calendar.DAY_OF_MONTH]
        val simpleHourFormat = SimpleDateFormat("hh")
        val simpleMinuteFormat = SimpleDateFormat("mm")
        hour = simpleHourFormat.format(System.currentTimeMillis()).toInt()
        minute = simpleMinuteFormat.format(System.currentTimeMillis()).toInt()

        vm_addIncome.postsList.observe(viewLifecycleOwner){
            Log.d("addExpense", "observed")
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                //is Resource.Success -> adapter_transactions = Adapter_Transactions(it.data, this)
                is Resource.Success ->  updateUI(it.data){

                }
                is Resource.Error -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        }

/*        binding.cvBackAddIncome.setOnClickListener(View.OnClickListener { v: View? ->
           *//* startActivity(Intent(this@AddIncome,
                MainActivity::class.java))*//*
            val action = AddIncomeFragmentDirections.actionAddIncomeFragmentToHomeFragment()
            navCon.navigate(action)
        })*/

        binding.cvImg.setOnClickListener {
            goBack()
        }

        binding.cvAddMore.setOnClickListener {
            addMoreCard()
        }

        binding.cvCalculatorAddIncome.setOnClickListener {
            Toast.makeText(requireContext(),"Coming Soon",Toast.LENGTH_SHORT).show()
        }

        vm_addIncome.loadAllCards(){

        }

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
        setDateTime()
        binding.tvCurrentBalanceValueToolBarAddIncome.setText(GlobalVariabls.currentBalance.toString())



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
        if (TextUtils.isEmpty(binding.etAmountAddIncome.text)) {
            binding.etAmountAddIncome.error = getString(R.string.AmontNeeded)
        } else if (TextUtils.isEmpty(vm_addIncome!!.category)) {
            Toast.makeText(context, getString(R.string.Pleaseselectacatagory), Toast.LENGTH_SHORT)
                .show()
        } else {
            val amount = binding.etAmountAddIncome.text.toString()
            var expenseDescription = ""
            if (!TextUtils.isEmpty(binding.etIncomeDescriptionAddIncome.text)) {
                expenseDescription = binding.etIncomeDescriptionAddIncome.text.toString()
            }
            val posts = MC_Posts(expenseDescription,
                vm_addIncome.category,
                Constants.TYPE_INCOME,
                amount.toInt(),
                vm_addIncome.year!!,
                vm_addIncome.month!!,
                vm_addIncome.day!!,
                vm_addIncome.time!!,
                System.currentTimeMillis().toString(),
                vm_addIncome.dateTime)
            /*val helper = SQLiteHelper(this@AddIncome)
            helper.saveData(posts)*/
            val db = AppDatabase.getInstance(requireContext().applicationContext)
            val dao = db.dbDao()
            CoroutineScope(Dispatchers.IO).launch {
                dao.insertAll(posts)
                CoroutineScope(Dispatchers.Main).launch {
                    GlobalVariabls.currentBalance = GlobalVariabls.currentBalance + amount.toInt()
                    Log.d("tag","Income added , Current Balance is ${GlobalVariabls.currentBalance}")

                   /* startActivity(Intent(this@AddIncome, MainActivity::class.java))
                    finish()*/
                    Toast.makeText(context, "Success ", Toast.LENGTH_SHORT).show()
                    val action = AddIncomeFragmentDirections.actionAddIncomeFragmentToHomeFragment()
                    navCon.navigate(action)
                }
            }


        }
    }

    private fun observe() {
        vm_addIncome!!.mutable_category.observe(viewLifecycleOwner, { s: String ->
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
        vm_addIncome.mutable_amount.observe(viewLifecycleOwner, { s ->
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
            TimePickerDialog(requireContext(), { view: TimePicker?, hourOfDay: Int, minute: Int ->
                vm_addIncome.time = "$hourOfDay : $minute"
                Log.d("tag", "hour$hourOfDay")
                if (hourOfDay >= 12) {
                    am_pm = "pm"
                    hourInString = (hourOfDay - 12).toString()
                } else {
                    am_pm = "am"
                    hourInString = hourOfDay.toString()
                }
                vm_addIncome.dateTime =
                    vm_addIncome.day + "-" + vm_addIncome!!.month + "-" + vm_addIncome!!.year + "  " + hourInString + ":" + minute.toString() + " " + am_pm
                //Log.d("tag","year"+vm_addIncome.year+" month "+vm_addIncome.month+" day "+vm_addIncome.day+" hour "+hourOfDay+"min "+minute+" "+am_pm);
                binding.tvDateTimeAddIncome.text = vm_addIncome.dateTime
            }, hour, minute, true)
        val datePickerDialog = DatePickerDialog(requireContext(), { view, year, month, dayOfMonth ->
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
        vm_addIncome.category=cardName
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
        vm_addIncome.category=""

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
                            vm_addIncome.loadAllCards(){
                               selectMoreCard(tvCard.text.toString())
                            }
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

    override fun onPause() {
        Log.d("tag", " Add Income Fragment Paused")
        super.onPause()
    }

    override fun onStop() {
        Log.d("tag","Add Income Fragment Stopped")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d("tag","Add Income Fragment Destroyed")
        super.onDestroy()
    }



}