package com.lincoln4791.dailyexpensemanager.Adapters

import android.content.Context
import android.util.Log
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.lincoln4791.dailyexpensemanager.R
import android.widget.TextView

class Adapter_MonthlyCategoryWiseReport(
    private val postList: List<MC_Posts>,
    private val context: Context
) : RecyclerView.Adapter<Adapter_MonthlyCategoryWiseReport.MyViewHolder>() {

    init {
        Log.d("tag","List size in adapter is -> ${postList.size}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.sample_monthly_category_wise, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tv_date.text = postList[position].postDateTime
        holder.tv_description.text = postList[position].postDescription
        holder.tv_amount.text = postList[position].postAmount
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val tv_date: TextView = itemView.findViewById(R.id.tv_dateValue_sampleMonthlyCategoryWiseReport)
        internal val tv_description: TextView = itemView.findViewById(R.id.tv_descriptionValue_sampleMonthlyCategoryWiseReport)
        internal val tv_amount: TextView = itemView.findViewById(R.id.tv_amountValue_sampleMonthlyCategoryWiseReport)

    }
}