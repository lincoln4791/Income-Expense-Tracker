package com.lincoln4791.dailyexpensemanager.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.fragments.TransactionsFragment
import com.lincoln4791.dailyexpensemanager.model.MC_Posts

class Adapter_Transactions(private val postList: List<MC_Posts>, context: Context, val tFragment : TransactionsFragment) :
    RecyclerView.Adapter<Adapter_Transactions.MyViewHolder>() {
    private var cv_Temp: CardView? = null
    private var ctx = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.sample_transactions, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (postList[position].postType == Constants.TYPE_INCOME) {
            Log.d("tag", "color green" + postList[position].postType)
            //holder.cv_mainLayout.setCardBackgroundColor(context.getColor(R.color.green))
            holder.tv_date.setTextColor(ContextCompat.getColor(ctx,R.color.greenIncome))
            holder.tv_time.setTextColor(ContextCompat.getColor(ctx,R.color.greenIncome))
            holder.tv_category.setTextColor(ContextCompat.getColor(ctx,R.color.greenIncome))
            holder.tv_description.setTextColor(ContextCompat.getColor(ctx,R.color.greenIncome))
            holder.tv_amount.setTextColor(ContextCompat.getColor(ctx,R.color.greenIncome))
        }

        val date = postList[position].postDateTime.split(" ")[0]
        holder.tv_date.text = date
        holder.tv_time.text = postList[position].postTime
        holder.tv_category.text = postList[position].postCategory
        holder.tv_amount.text = postList[position].postAmount.toString()
        holder.tv_description.text = postList[position].postDescription
        //holder.cv_mainLayout.setOnClickListener { v: View? -> }

        holder.cv_mainLayout.setOnClickListener {
            tFragment.navigateToDetails(
                postList[position].postYear,
                postList[position].postMonth,
                postList[position].postType,
                postList[position].postCategory
            )
        }

        holder.cv_mainLayout.setOnLongClickListener {
            if (cv_Temp != null) {
                cv_Temp!!.visibility = View.GONE
            }
            cv_Temp = holder.cv_editDltLayout
            cv_Temp!!.visibility = View.VISIBLE
            true
        }
        holder.iv_editData.setOnClickListener {

        }
        holder.iv_deleteData.setOnClickListener {
            tFragment.confirmDelete(
                postList[position].id, postList[position].postAmount.toInt(),
                postList[position].postType)
        }
        holder.iv_closeEdtDltLayout.setOnClickListener {
            holder.cv_editDltLayout.visibility = View.GONE
        }


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
        internal val cv_typeHolder: LinearLayout
        internal val cv_mainLayout: CardView
        internal val cv_editDltLayout: CardView
        internal val iv_editData: ImageView
        internal val iv_deleteData: ImageView
        internal val iv_closeEdtDltLayout: ImageView

        init {
            iv_editData = itemView.findViewById(R.id.iv_edit_sampleTransactions)
            iv_deleteData = itemView.findViewById(R.id.iv_delete_sampleTransactions)
            iv_closeEdtDltLayout = itemView.findViewById(R.id.iv_closeEditDlt_sampleTransactions)
            cv_editDltLayout = itemView.findViewById(R.id.cv_editDltHolder_sampleTransactions)
            cv_typeHolder = itemView.findViewById(R.id.cv_type_sample_transactions)
            cv_mainLayout = itemView.findViewById(R.id.cv_MainLayout_sampleTransaction)
            tv_description = itemView.findViewById(R.id.tv_description_sample_transactions)
            tv_date = itemView.findViewById(R.id.tv_date_sample_transactions)
            tv_time = itemView.findViewById(R.id.tv_time_sample_transactions)
            tv_category = itemView.findViewById(R.id.tv_category_sample_transactions)
            tv_amount = itemView.findViewById(R.id.tv_amount_sample_transactions)
        }
    }
}