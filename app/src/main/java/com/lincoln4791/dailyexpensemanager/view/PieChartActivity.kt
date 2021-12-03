package com.lincoln4791.dailyexpensemanager.view

import androidx.appcompat.app.AppCompatActivity
import org.eazegraph.lib.charts.PieChart
import android.widget.TextView
import android.os.Bundle
import com.lincoln4791.dailyexpensemanager.R
import android.content.Intent
import android.view.View
import android.widget.ImageView
import com.lincoln4791.dailyexpensemanager.view.MainActivity
import com.lincoln4791.dailyexpensemanager.common.UtilDB
import com.lincoln4791.dailyexpensemanager.common.Extras
import com.lincoln4791.dailyexpensemanager.databinding.ActivityPieChartBinding
import org.eazegraph.lib.models.PieModel

class PieChartActivity : AppCompatActivity() {
    private var totalIncome = 0.0
    private var totalExpense = 0.0
    private val balance = 0.0
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

    private lateinit var binding : ActivityPieChartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPieChartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //**********************************************************Initializations*****************************************
        supportActionBar!!.hide()
        intentData
        setPieChart()


        //****************************************************** Click Listeners ******************************************
        binding.ivHomeToolbarPieChart.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(this@PieChartActivity,
                MainActivity::class.java))
        })


        //********************************************************Starting Methods**********************************************
        binding.tvCurrentBalanceValueToolBarPieChart.text = UtilDB.currentBalance.toString()
    }

    private val intentData: Unit
        get() {
            totalIncome = intent.getDoubleExtra(Extras.TOTAL_INCOME, 0.0)
            salaryIncome = intent.getDoubleExtra(Extras.SALARY_INCOME, 0.0)
            businessIncome = intent.getDoubleExtra(Extras.BUSINESS_INCOME, 0.0)
            houseRentIncome = intent.getDoubleExtra(Extras.HOUSE_RENT_INCOME, 0.0)
            otherIncome = intent.getDoubleExtra(Extras.OTHER_INCOME, 0.0)
            totalExpense = intent.getDoubleExtra(Extras.TOTAL_EXPENSE, 0.0)
            foodExpense = intent.getDoubleExtra(Extras.FOOD_EXPENSE, 0.0)
            businessExpense = intent.getDoubleExtra(Extras.BUSINESS_EXPENSE, 0.0)
            houseRentExpense = intent.getDoubleExtra(Extras.HOUSE_RENT_EXPENSE, 0.0)
            billsExpense = intent.getDoubleExtra(Extras.BILLS_EXPENSE, 0.0)
            transportExpense = intent.getDoubleExtra(Extras.TRANSPORT_EXPENSE, 0.0)
            clothsExpense = intent.getDoubleExtra(Extras.CLOTHS_EXPENSE, 0.0)
            medicineExpense = intent.getDoubleExtra(Extras.MEDICINE_EXPENSE, 0.0)
            educationExpense = intent.getDoubleExtra(Extras.EDUCATION_EXPENSE, 0.0)
            lifeStyleExpense = intent.getDoubleExtra(Extras.LIFESTYLE_EXPENSE, 0.0)
            otherExpense = intent.getDoubleExtra(Extras.OTHER_EXPENSE, 0.0)
            salaryIncomePercent = intent.getDoubleExtra(Extras.SALARY_INCOME_PERCENT, 0.0)
            businessIncomePercent = intent.getDoubleExtra(Extras.BUSINESS_INCOME_PERCENT, 0.0)
            houseRentIncomePercent = intent.getDoubleExtra(Extras.HOUSE_RENT_INCOME_PERCENT, 0.0)
            otherIncomePercent = intent.getDoubleExtra(Extras.OTHER_INCOME_PERCENT, 0.0)
            foodExpensePercent = intent.getDoubleExtra(Extras.FOOD_EXPENSE_PERCENT, 0.0)
            businessExpensePercent = intent.getDoubleExtra(Extras.BUSINESS_EXPENSE_PERCENT, 0.0)
            houseRentExpensePercent = intent.getDoubleExtra(Extras.HOUSE_RENT_EXPENSE_PERCENT, 0.0)
            billsExpensePercent = intent.getDoubleExtra(Extras.BILLS_EXPENSE_PERCENT, 0.0)
            transportExpensePercent = intent.getDoubleExtra(Extras.TRANSPORT_EXPENSE_PERCENT, 0.0)
            clothsExpensePercent = intent.getDoubleExtra(Extras.CLOTHS_EXPENSE_PERCENT, 0.0)
            medicineExpensePercent = intent.getDoubleExtra(Extras.MEDICINE_EXPENSE_PERCENT, 0.0)
            educationExpensePercent = intent.getDoubleExtra(Extras.EDUCATION_EXPENSE_PERCENT, 0.0)
            lifeStyleExpensePercent = intent.getDoubleExtra(Extras.LIFESTYLE_EXPENSE_PERCENT, 0.0)
            otherExpensePercent = intent.getDoubleExtra(Extras.OTHER_EXPENSE_PERCENT, 0.0)
        }

    private fun setPieChart() {
        binding.pieChartIncome.addPieSlice(PieModel(getString(R.string.Salary),
            salaryIncome.toFloat(),
            getColor(R.color.salary)))
        binding.pieChartIncome.addPieSlice(PieModel(getString(R.string.Business),
            businessIncome.toFloat(),
            getColor(R.color.business)))
        binding.pieChartIncome.addPieSlice(PieModel(getString(R.string.HouseRent),
            houseRentIncome.toFloat(),
            getColor(R.color.houseRent)))
        binding.pieChartIncome.addPieSlice(PieModel(getString(R.string.Other),
            otherIncome.toFloat(),
            getColor(R.color.other)))
        binding.pieChartExpense.addPieSlice(PieModel(getString(R.string.Food),
            foodExpense.toFloat(),
            getColor(R.color.food)))
        binding.pieChartExpense.addPieSlice(PieModel(getString(R.string.Business),
            businessExpense.toFloat(),
            getColor(R.color.business)))
        binding.pieChartExpense.addPieSlice(PieModel(getString(R.string.Transport),
            transportExpense.toFloat(),
            getColor(R.color.transport)))
        binding.pieChartExpense.addPieSlice(PieModel(getString(R.string.HouseRent),
            houseRentExpense.toFloat(),
            getColor(R.color.houseRent)))
        binding.pieChartExpense.addPieSlice(PieModel(getString(R.string.Bills),
            billsExpense.toFloat(),
            getColor(R.color.bills)))
        binding.pieChartExpense.addPieSlice(PieModel(getString(R.string.Cloths),
            clothsExpense.toFloat(),
            getColor(R.color.cloths)))
        binding.pieChartExpense.addPieSlice(PieModel(getString(R.string.Education),
            educationExpense.toFloat(),
            getColor(R.color.education)))
        binding.pieChartExpense.addPieSlice(PieModel(getString(R.string.Medicine),
            medicineExpense.toFloat(),
            getColor(R.color.medicine)))
        binding.pieChartExpense.addPieSlice(PieModel(getString(R.string.LifeStyle),
            lifeStyleExpense.toFloat(),
            getColor(R.color.lifestyle)))
        binding.pieChartExpense.addPieSlice(PieModel(getString(R.string.Other),
            otherExpense.toFloat(),
            getColor(R.color.other)))
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

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}