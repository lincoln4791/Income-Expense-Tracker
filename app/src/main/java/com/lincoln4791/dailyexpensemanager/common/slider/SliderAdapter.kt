package com.lincoln4791.dailyexpensemanager.common.slider

import android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.lincoln4791.dailyexpensemanager.common.util.Util
import com.lincoln4791.dailyexpensemanager.fragments.HomeFragment
import com.makeramen.roundedimageview.RoundedImageView


 class SliderAdapter internal constructor(
    private val sliderItems: MutableList<SliderItems>,
    private val viewPager2: ViewPager2,
    private val fragment: HomeFragment
) :
    RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {
    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                com.lincoln4791.dailyexpensemanager.R.layout.slide_item_container, parent, false
            ))
    }

    override fun onBindViewHolder(@NonNull holder: SliderViewHolder, position: Int) {
        holder.setImage(sliderItems[position])
        if (position == sliderItems.size - 2) {
            viewPager2.post(runnable)
        }

        holder.itemView.setOnClickListener {
            Util.goToFacebookPage(fragment.requireContext())
        }

    }

    override fun getItemCount(): Int {
        return sliderItems.size
    }

    inner class SliderViewHolder(@NonNull itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val imageView: RoundedImageView = itemView.findViewById(com.lincoln4791.dailyexpensemanager.R.id.imageSlide)
        fun setImage(sliderItems: SliderItems) {
//use glide or picasso in case you get image from internet
            imageView.setImageResource(sliderItems.image)
        }

    }

     private val runnable = Runnable {
         sliderItems.addAll(sliderItems)
         notifyDataSetChanged()
     }
}