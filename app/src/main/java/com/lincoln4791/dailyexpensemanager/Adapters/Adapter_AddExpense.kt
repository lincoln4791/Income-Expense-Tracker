package com.lincoln4791.dailyexpensemanager.Adapters

import android.content.Context
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.fragments.AddExpenseFragment
import com.lincoln4791.dailyexpensemanager.model.MC_Cards

class Adapter_AddExpense(private val list: List<MC_Cards>,
                         private val context: Context,
                         private val fragment : AddExpenseFragment):
    RecyclerView.Adapter<Adapter_AddExpense.MyViewHolder>() {

    var selectedCardPosition = -1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.sample_cardview, parent, false)
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tv.text = list[position].cardName


        holder.cv.setOnClickListener {
            fragment.selectMoreCard(list[position].cardName.toString())
            fragment.vm_addExpenses!!.category = list[position].cardName.toString()
        }

        holder.ivDelete.setOnClickListener {
            fragment.deleteCardByName(list[position].cardName!!){
                if(it){
                    android.os.Handler(Looper.getMainLooper()).postDelayed({
                        fragment.viewModel.loadAllCards(){

                        }
                    },0)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv = itemView.findViewById<TextView>(R.id.tv_sample_cv)
        val cv = itemView.findViewById<CardView>(R.id.cv_mainLayout)
        val ivDelete = itemView.findViewById<ImageView>(R.id.iv_deleteCard)
    }
}