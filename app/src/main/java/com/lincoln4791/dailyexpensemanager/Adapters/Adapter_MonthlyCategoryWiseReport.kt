package com.lincoln4791.dailyexpensemanager.Adapters

import android.content.Context
import android.util.Log
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.lincoln4791.dailyexpensemanager.R
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.lincoln4791.dailyexpensemanager.common.Constants

class Adapter_MonthlyCategoryWiseReport(
    private val postList: List<MC_Posts>,
    private val context: Context
) : RecyclerView.Adapter<Adapter_MonthlyCategoryWiseReport.MyViewHolder>() {

    init {
        Log.d("tag","List size in adapter is -> ${postList.size}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.sample_transactions, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (postList[position].postType == Constants.TYPE_INCOME) {
            Log.d("tag", "color green" + postList[position].postType)
            //holder.cv_mainLayout.setCardBackgroundColor(context.getColor(R.color.green))
            holder.tv_date.setTextColor(ContextCompat.getColor(context,R.color.green))
            holder.tv_time.setTextColor(ContextCompat.getColor(context,R.color.green))
            holder.tv_category.setTextColor(ContextCompat.getColor(context,R.color.green))
            holder.tv_description.setTextColor(ContextCompat.getColor(context,R.color.green))
            holder.tv_amount.setTextColor(ContextCompat.getColor(context,R.color.green))
        }

        val date = postList[position].postDateTime.split(" ")[0]


        holder.tv_date.text = date
        holder.tv_time.text = postList[position].postTime
        holder.tv_category.text = postList[position].postCategory
        holder.tv_amount.text = postList[position].postAmount.toString()
        holder.tv_description.text = postList[position].postDescription
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal val tv_time: TextView
        internal val tv_date: TextView
        internal  val tv_category: TextView
        internal  val tv_amount: TextView
        internal  val tv_description: TextView
        internal val cv_mainLayout: CardView

        init {
            cv_mainLayout = itemView.findViewById(R.id.cv_MainLayout_sampleTransaction)
            tv_description = itemView.findViewById(R.id.tv_description_sample_transactions)
            tv_date = itemView.findViewById(R.id.tv_date_sample_transactions)
            tv_time = itemView.findViewById(R.id.tv_time_sample_transactions)
            tv_category = itemView.findViewById(R.id.tv_category_sample_transactions)
            tv_amount = itemView.findViewById(R.id.tv_amount_sample_transactions)
        }



    }
}