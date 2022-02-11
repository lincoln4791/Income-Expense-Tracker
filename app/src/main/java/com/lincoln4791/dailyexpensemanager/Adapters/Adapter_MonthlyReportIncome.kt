package com.lincoln4791.dailyexpensemanager.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.fragments.MonthlyFragment
import com.lincoln4791.dailyexpensemanager.model.MC_MonthlyReport


class Adapter_MonthlyReportIncome(private val postList: List<MC_MonthlyReport>, private val fragment: MonthlyFragment) :
    RecyclerView.Adapter<Adapter_MonthlyReportIncome.MyViewHolder>() {
    private var cv_Temp: CardView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(fragment.requireContext()).inflate(R.layout.sample_transactions2, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      /*  if (type ==  Constants.TYPE_INCOME) {
            holder.cvHolder.setCardBackgroundColor(ContextCompat.getColor(context,R.color.green))
        }*/

        holder.cvHolder.setCardBackgroundColor(ContextCompat.getColor(fragment.requireContext(),R.color.green))
        holder.tv_amountPercent.text = MonthlyFragment.df2.format(postList[position].amountPercent!!.toDouble())
        holder.tv_name.text = postList[position].postCategory
        holder.tv_amount.text = postList[position].postAmount.toString()

        holder.cvHolder.setOnClickListener {
            fragment.navigateToDetails(
                Constants.TYPE_INCOME,
                postList[position].postCategory
            )
        }

    }

    override fun getItemCount(): Int {
        return postList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val tv_name: TextView = itemView.findViewById(R.id.name)
        internal val tv_amountPercent: TextView = itemView.findViewById(R.id.amountPercent)
        internal  val tv_amount: TextView = itemView.findViewById(R.id.tv_amount)
        internal  val cvHolder: CardView = itemView.findViewById(R.id.cv_type_houseRentExpense_MonthlyReport)

    }
}