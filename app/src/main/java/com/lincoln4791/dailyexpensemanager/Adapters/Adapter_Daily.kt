package com.lincoln4791.dailyexpensemanager.Adapters

import android.content.Context
import android.content.Intent
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
import com.lincoln4791.dailyexpensemanager.common.NodeName
import com.lincoln4791.dailyexpensemanager.fragments.DailyFragment
import com.lincoln4791.dailyexpensemanager.model.MC_Posts


class Adapter_Daily(private val postList: List<MC_Posts>, private val context: Context,private val fragment: DailyFragment) :
    RecyclerView.Adapter<Adapter_Daily.MyViewHolder>() {
    private var cv_Temp: CardView? = null
    private val cv_new: CardView? = null
    private val i = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.sample_transactions, parent, false)
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
        //holder.tv_dateTime.text = postList[position].postDateTime

        val date = postList[position].postDateTime.split(" ")[0]

        holder.tv_category.text = postList[position].postCategory
        holder.tv_amount.text = postList[position].postAmount.toString()
        holder.tv_date.text = date
        holder.tv_time.text = postList[position].postTime
        holder.tv_description.text = postList[position].postDescription
        holder.cv_mainLayout.setOnClickListener { v: View? -> }
        holder.cv_mainLayout.setOnLongClickListener { v: View? ->
            if (cv_Temp != null) {
                cv_Temp!!.visibility = View.GONE
            }
            cv_Temp = holder.cv_editDltLayout
            cv_Temp!!.visibility = View.VISIBLE
            false
        }
        holder.iv_editData.setOnClickListener { v: View? ->
        /*    if (postList[position].postType == Constants.TYPE_INCOME) {
                val editDataIncomeIntent = Intent(context, EditDataIncome::class.java)
                editDataIncomeIntent.putExtra(NodeName.ID, postList[position].id.toString())
                editDataIncomeIntent.putExtra(NodeName.POST_DESCRIPTION,
                    postList[position].postDescription)
                editDataIncomeIntent.putExtra(NodeName.POST_CATEGORY,
                    postList[position].postCategory)
                editDataIncomeIntent.putExtra(NodeName.POST_AMOUNT,
                    postList[position].postAmount)
                editDataIncomeIntent.putExtra(NodeName.POST_YEAR, postList[position].postYear)
                editDataIncomeIntent.putExtra(NodeName.POST_MONTH,
                    postList[position].postMonth)
                editDataIncomeIntent.putExtra(NodeName.POST_DAY, postList[position].postDay)
                editDataIncomeIntent.putExtra(NodeName.POST_TIME, postList[position].postTime)
                editDataIncomeIntent.putExtra(NodeName.POST_DATE_TIME,
                    postList[position].postDateTime)
                context.startActivity(editDataIncomeIntent)
            }*/

      /*      else if (postList[position].postType == Constants.TYPE_EXPENSE) {
                val editDataExpenseIntent = Intent(context, EditDataExpense::class.java)
                editDataExpenseIntent.putExtra(NodeName.ID, postList[position].id.toString())
                editDataExpenseIntent.putExtra(NodeName.POST_DESCRIPTION,
                    postList[position].postDescription)
                editDataExpenseIntent.putExtra(NodeName.POST_CATEGORY,
                    postList[position].postCategory)
                editDataExpenseIntent.putExtra(NodeName.POST_AMOUNT,
                    postList[position].postAmount)
                editDataExpenseIntent.putExtra(NodeName.POST_YEAR, postList[position].postYear)
                editDataExpenseIntent.putExtra(NodeName.POST_MONTH,
                    postList[position].postMonth)
                editDataExpenseIntent.putExtra(NodeName.POST_DAY, postList[position].postDay)
                editDataExpenseIntent.putExtra(NodeName.POST_TIME, postList[position].postTime)
                editDataExpenseIntent.putExtra(NodeName.POST_DATE_TIME,
                    postList[position].postDateTime)
                context.startActivity(editDataExpenseIntent)
            }*/
        }
        holder.iv_deleteData.setOnClickListener {

            fragment.confirmDelete(
                postList[position].id)
        }
        holder.iv_closeEdtDltLayout.setOnClickListener { v: View? ->
            holder.cv_editDltLayout.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //internal val tv_dateTime: TextView = itemView.findViewById(R.id.tv_dateTime_sample_transactions)
        internal val tv_category: TextView = itemView.findViewById(R.id.tv_category_sample_transactions)
        internal val tv_amount: TextView = itemView.findViewById(R.id.tv_amount_sample_transactions)
        internal val tv_description: TextView = itemView.findViewById(R.id.tv_description_sample_transactions)
        internal val tv_date: TextView = itemView.findViewById(R.id.tv_date_sample_transactions)
        internal val tv_time: TextView = itemView.findViewById(R.id.tv_time_sample_transactions)
        internal val cv_typeHolder: LinearLayout = itemView.findViewById(R.id.cv_type_sample_transactions)
        internal val cv_mainLayout: CardView = itemView.findViewById(R.id.cv_MainLayout_sampleTransaction)
        internal val cv_editDltLayout: CardView = itemView.findViewById(R.id.cv_editDltHolder_sampleTransactions)
        internal val iv_editData: ImageView = itemView.findViewById(R.id.iv_edit_sampleTransactions)
        internal val iv_deleteData: ImageView =  itemView.findViewById(R.id.iv_delete_sampleTransactions)
        internal val iv_closeEdtDltLayout: ImageView = itemView.findViewById(R.id.iv_closeEditDlt_sampleTransactions)

    }
}