package com.lincoln4791.dailyexpensemanager.Adapters

import android.content.Context
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.lincoln4791.dailyexpensemanager.R
import android.widget.TextView

class Adapter_FullReport(private val context: Context, private val postsList: List<MC_Posts>) :
    RecyclerView.Adapter<Adapter_FullReport.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.sample_full_report, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tv_dateTime.text = postsList[position].postDateTime
        holder.tv_category.text = postsList[position].postCategory
        holder.tv_type.text = postsList[position].postType
        holder.tv_amount.text = postsList[position].postAmount
    }

    override fun getItemCount(): Int {
        return postsList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val tv_dateTime: TextView = itemView.findViewById(R.id.tv_dateTime_SampleFullReport)
        internal val tv_type: TextView = itemView.findViewById(R.id.tv_type_SampleFullReport)
        internal val tv_category: TextView = itemView.findViewById(R.id.tv_category_SampleFullReport)
        internal val tv_amount: TextView = itemView.findViewById(R.id.tv_amount_SampleFullReport)

    }
}