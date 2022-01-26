package com.lincoln4791.dailyexpensemanager.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.Resource
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.common.Extras
import com.lincoln4791.dailyexpensemanager.common.Util
import com.lincoln4791.dailyexpensemanager.databinding.FragmentPieChartBinding
import com.lincoln4791.dailyexpensemanager.databinding.FragmentTransactionsBinding
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import com.lincoln4791.dailyexpensemanager.viewModels.VM_MonthlyReport
import com.lincoln4791.dailyexpensemanager.viewModels.VM_PieChart
import com.lincoln4791.dailyexpensemanager.viewModels.VM_Transactions
import org.eazegraph.lib.models.PieModel

class PieChartFragment : Fragment() {
    val args: PieChartFragmentArgs by navArgs()
    private lateinit var year: String
    private lateinit var month: String
    private var totalIncome = 0.0
    private var totalExpense = 0.0
    private var balance = 0.0
    private var transportExpense = 0.0
    private var foodExpense = 0.0
    private var billsExpense = 0.0
    private var houseRentExpense = 0.0
    private var businessExpense = 0.0
    private var medicineExpense = 0.0
    private var clothsExpense = 0.0
    private var educationExpense = 0.0
    private var lifeStyleExpense = 0.0
    private var otherExpense = 0.0
    private var salaryIncome = 0.0
    private var businessIncome = 0.0
    private var houseRentIncome = 0.0
    private var otherIncome = 0.0
    private var transportExpensePercent = 0.0
    private var foodExpensePercent = 0.0
    private var billsExpensePercent = 0.0
    private var houseRentExpensePercent = 0.0
    private var businessExpensePercent = 0.0
    private var medicineExpensePercent = 0.0
    private var clothsExpensePercent = 0.0
    private var educationExpensePercent = 0.0
    private var lifeStyleExpensePercent = 0.0
    private var otherExpensePercent = 0.0
    private var salaryIncomePercent = 0.0
    private var businessIncomePercent = 0.0
    private var houseRentIncomePercent = 0.0
    private var otherIncomePercent = 0.0

    private lateinit var binding : FragmentPieChartBinding
    private lateinit var navCon : NavController
    private lateinit var viewModel: VM_PieChart


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
        // Inflate the layout for this fragment
        binding = FragmentPieChartBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navCon = Navigation.findNavController(view)
        viewModel = ViewModelProvider(this)[VM_PieChart::class.java]

        year = args.year!!
        month = args.month!!

        viewModel.postsList.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> Log.d("Transaction", "Loading...")
                is Resource.Success ->  calculateAll(it.data)
                is Resource.Error -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        })


        setReportTime()

        Log.d("tag","year -> $year::: month -> $month")
        viewModel.loadYearMonth(year,month)

        binding.cvImg.setOnClickListener {
            goBack()
        }



    }

    private fun setReportTime() {
        val monthName = Util.getMonthNameFromMonthNumber(month)
        binding.tvReportTime.text = "Report Date : $monthName-$year"

    }

    private fun goBack() {
        val action = PieChartFragmentDirections.actionPieChartFragmentToMonthlyFragment(year,month)
        navCon.navigate(action)
    }


    private fun calculateAll(postsList:List<MC_Posts>) {
        //resetPreviousCalculatedValues()
        Log.d("tag", "list size : " + postsList!!.size)
        for (i in postsList!!.indices) {
            if (postsList!![i].postType == Constants.TYPE_INCOME) {
                if (postsList!![i].postCategory == Constants.CATEGORY_SALARY) {
                    salaryIncome = salaryIncome + postsList!![i].postAmount.toInt()
                } else if (postsList!![i].postCategory == Constants.CATEGORY_BUSINESS) {
                    businessIncome = businessIncome + postsList!![i].postAmount.toInt()
                } else if (postsList!![i].postCategory == Constants.CATEGORY_HOUSE_RENT) {
                    houseRentIncome = houseRentIncome + postsList!![i].postAmount.toInt()
                } else if (postsList!![i].postCategory == Constants.CATEGORY_OTHER) {
                    otherIncome = otherIncome + postsList!![i].postAmount.toInt()
                }
                totalIncome = totalIncome + postsList!![i].postAmount.toInt()
            } else {
                if (postsList!![i].postCategory == Constants.CATEGORY_FOOD) {
                    foodExpense = foodExpense + postsList!![i].postAmount.toInt()
                } else if (postsList!![i].postCategory == Constants.CATEGORY_TRANSPORT) {
                    transportExpense = transportExpense + postsList!![i].postAmount.toInt()
                } else if (postsList!![i].postCategory == Constants.CATEGORY_BILLS) {
                    billsExpense = billsExpense + postsList!![i].postAmount.toInt()
                } else if (postsList!![i].postCategory == Constants.CATEGORY_HOUSE_RENT) {
                    houseRentExpense = houseRentExpense + postsList!![i].postAmount.toInt()
                } else if (postsList!![i].postCategory == Constants.CATEGORY_BUSINESS) {
                    businessExpense = businessExpense + postsList!![i].postAmount.toInt()
                } else if (postsList!![i].postCategory == Constants.CATEGORY_MEDICINE) {
                    medicineExpense = medicineExpense + postsList!![i].postAmount.toInt()
                } else if (postsList!![i].postCategory == Constants.CATEGORY_CLOTHS) {
                    clothsExpense = clothsExpense + postsList!![i].postAmount.toInt()
                } else if (postsList!![i].postCategory == Constants.CATEGORY_EDUCATION) {
                    educationExpense = educationExpense + postsList!![i].postAmount.toInt()
                } else if (postsList!![i].postCategory == Constants.CATEGORY_LIFESTYLE) {
                    lifeStyleExpense = lifeStyleExpense + postsList!![i].postAmount.toInt()
                } else if (postsList!![i].postCategory == Constants.CATEGORY_OTHER) {
                    otherExpense = otherExpense + postsList!![i].postAmount.toInt()
                }
                totalExpense = totalExpense + postsList!![i].postAmount.toInt()
            }
        }
        balance = totalIncome - totalExpense
        calculatePercent()
    }

    private fun calculatePercent() {
        foodExpensePercent = MonthlyFragment.df2.format(foodExpense / totalExpense * 100.0).toDouble()
        businessExpensePercent = MonthlyFragment.df2.format(businessExpense / totalExpense * 100).toDouble()
        transportExpensePercent = MonthlyFragment.df2.format(transportExpense / totalExpense * 100).toDouble()
        billsExpensePercent = MonthlyFragment.df2.format(billsExpense / totalExpense * 100).toDouble()
        houseRentExpensePercent = MonthlyFragment.df2.format(houseRentExpense / totalExpense * 100).toDouble()
        medicineExpensePercent = MonthlyFragment.df2.format(medicineExpense / totalExpense * 100).toDouble()
        clothsExpensePercent = MonthlyFragment.df2.format(clothsExpense / totalExpense * 100).toDouble()
        educationExpensePercent = MonthlyFragment.df2.format(educationExpense / totalExpense * 100).toDouble()
        lifeStyleExpensePercent = MonthlyFragment.df2.format(lifeStyleExpense / totalExpense * 100).toDouble()
        otherExpensePercent = MonthlyFragment.df2.format(otherExpense / totalExpense * 100).toDouble()
        salaryIncomePercent = MonthlyFragment.df2.format(salaryIncome / totalIncome * 100).toDouble()
        businessIncomePercent = MonthlyFragment.df2.format(businessIncome / totalIncome * 100).toDouble()
        houseRentIncomePercent = MonthlyFragment.df2.format(houseRentIncome / totalIncome * 100).toDouble()
        otherIncomePercent = MonthlyFragment.df2.format(otherIncome / totalIncome * 100).toDouble()
        Log.d("tag",
            "food : " + foodExpense + " totat : " + totalExpense + "percent: " + foodExpensePercent)

        setPieChart()
    }


    private fun setPieChart() {
        binding.pieChartIncome.addPieSlice(PieModel(getString(R.string.Salary),
            salaryIncome.toFloat(),
            getColor(requireContext(),R.color.salary)))
        binding.pieChartIncome.addPieSlice(PieModel(getString(R.string.Business),
            businessIncome.toFloat(),
            getColor(requireContext(),R.color.business)))
        binding.pieChartIncome.addPieSlice(PieModel(getString(R.string.HouseRent),
            houseRentIncome.toFloat(),
            getColor(requireContext(),R.color.houseRent)))
        binding.pieChartIncome.addPieSlice(PieModel(getString(R.string.Other),
            otherIncome.toFloat(),
            getColor(requireContext(),R.color.other)))
        binding.pieChartExpense.addPieSlice(PieModel(getString(R.string.Food),
            foodExpense.toFloat(),
            getColor(requireContext(),R.color.food)))
        binding.pieChartExpense.addPieSlice(PieModel(getString(R.string.Business),
            businessExpense.toFloat(),
            getColor(requireContext(),R.color.business)))
        binding.pieChartExpense.addPieSlice(PieModel(getString(R.string.Transport),
            transportExpense.toFloat(),
            getColor(requireContext(),R.color.transport)))
        binding.pieChartExpense.addPieSlice(PieModel(getString(R.string.HouseRent),
            houseRentExpense.toFloat(),
            getColor(requireContext(),R.color.houseRent)))
        binding.pieChartExpense.addPieSlice(PieModel(getString(R.string.Bills),
            billsExpense.toFloat(),
            getColor(requireContext(),R.color.bills)))
        binding.pieChartExpense.addPieSlice(PieModel(getString(R.string.Cloths),
            clothsExpense.toFloat(),
            getColor(requireContext(),R.color.cloths)))
        binding.pieChartExpense.addPieSlice(PieModel(getString(R.string.Education),
            educationExpense.toFloat(),
            getColor(requireContext(),R.color.education)))
        binding.pieChartExpense.addPieSlice(PieModel(getString(R.string.Medicine),
            medicineExpense.toFloat(),
            getColor(requireContext(),R.color.medicine)))
        binding.pieChartExpense.addPieSlice(PieModel(getString(R.string.LifeStyle),
            lifeStyleExpense.toFloat(),
            getColor(requireContext(),R.color.lifestyle)))
        binding.pieChartExpense.addPieSlice(PieModel(getString(R.string.Other),
            otherExpense.toFloat(),
            getColor(requireContext(),R.color.other)))
        binding.tvSalaryIncomePercentValuePieChart.text = " Salary $salaryIncomePercent%"
        binding.tvBusinessIncomePercentValuePieChart.text = " Business $businessIncomePercent%"
        binding.tvHouseRentIncomePercentValuePieChart.text = " House Rent $houseRentIncomePercent%"
        binding.tvOtherIncomePercentValuePieChart.text = " Other $otherIncomePercent%"
        binding.tvFoodExpensePercentValuePieChart.text = " Food $foodExpensePercent%"
        binding.tvBusinessExpensePercentValuePieChart.text = " Business $businessExpensePercent%"
        binding.tvHouseRentExpensePercentValuePieChart.text = " House Rent` $houseRentExpensePercent%"
        binding.tvBillsExpensePercentValuePieChart.text = " Bills $billsExpensePercent%"
        binding.tvTransportExpensePercentValuePieChart.text = " Transport $transportExpensePercent%"
        binding.tvClothsExpensePercentValuePieChart.text = " Cloths $clothsExpensePercent%"
        binding.tvMedicineExpensePercentValuePieChart.text = " Medicine $medicineExpensePercent%"
        binding.tvEducationExpensePercentValuePieChart.text = " Education $educationExpensePercent%"
        binding.tvLifeStyleExpensePercentValuePieChart.text = " Life Style $lifeStyleExpensePercent%"
        binding.tvOtherExpensePercentValuePieChart.text = " Other $otherExpensePercent%"
        binding.pieChartIncome.startAnimation()
        binding.pieChartExpense.startAnimation()
    }

}