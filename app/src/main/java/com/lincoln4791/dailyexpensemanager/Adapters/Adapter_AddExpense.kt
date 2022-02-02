package com.lincoln4791.dailyexpensemanager.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.model.MC_Cards
import com.lincoln4791.dailyexpensemanager.model.MC_Posts

class Adapter_AddExpense(private val list: List<MC_Cards>, private val context: Context): RecyclerView.Adapter<Adapter_AddExpense.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): Adapter_AddExpense.MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.sample_cardview, parent, false)
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: Adapter_AddExpense.MyViewHolder, position: Int) {
        holder.tv.text = list[position].cardName
        holder.cv.setOnClickListener {
            Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv = itemView.findViewById<TextView>(R.id.tv_sample_cv)
        val cv = itemView.findViewById<CardView>(R.id.cv_mainLayout)
    }
}