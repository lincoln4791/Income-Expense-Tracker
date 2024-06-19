package com.lincoln4791.dailyexpensemanager.Adapters

import android.content.Context
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.fragments.Feed
import com.lincoln4791.dailyexpensemanager.modelClass.QuoteModelClass.QuotesResponseModel
import com.lincoln4791.dailyexpensemanager.modelClass.QuoteModelClass.Result

class Adapter_Quotes : PagingDataAdapter<Result, Adapter_Quotes.ViewHolder>(DataDifferntiator) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    init {
        //Log.d("tag","Called -> ${getItem(0)!!.firstName}")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Log.d("tag","in adapter")
        holder.itemView.findViewById<TextView>(R.id.tvQuote).text = "${getItem(position)?.content}"
        //holder.itemView.findViewById<TextView>(R.id.tvA).text = getItem(position)?.author

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_quote, parent, false)
        )
    }

    object DataDifferntiator : DiffUtil.ItemCallback<Result>() {

        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

}