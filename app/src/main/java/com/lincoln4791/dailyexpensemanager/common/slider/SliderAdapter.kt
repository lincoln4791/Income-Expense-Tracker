package com.lincoln4791.dailyexpensemanager.common.slider

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.lincoln4791.dailyexpensemanager.common.util.Util
import com.lincoln4791.dailyexpensemanager.fragments.HomeFragment
import com.lincoln4791.dailyexpensemanager.modelClass.Banner
import com.makeramen.roundedimageview.RoundedImageView
import java.io.File
import java.io.FileInputStream


class SliderAdapter internal constructor(
    private val sliderItems: MutableList<Banner>,
    private val viewPager2: ViewPager2,
    private val fragment: HomeFragment,
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
        //holder.setImage(sliderItems[position])
        if(sliderItems[position].is_active=="1") {
            try {
                val inFileName = "${sliderItems[position].name}.png"
                val inFile =
                    File(fragment.requireContext().getExternalFilesDir("IncomeExpenseManager"),
                        inFileName)
                val bitmap = BitmapFactory.decodeStream(FileInputStream(inFile))
                holder.setImage(bitmap)

                if (position == sliderItems.size - 2) {
                    viewPager2.post(runnable)
                }

                holder.itemView.setOnClickListener {
                    if(sliderItems[position].link=="www.iem.com"){
                        try {
                            Util.goToFacebookPage(fragment.requireContext())
                        }
                        catch (e:Exception){
                            e.printStackTrace()
                        }

                    }
                    else{
                       try {
                           val i = Intent(Intent.ACTION_VIEW)
                           i.data = Uri.parse(sliderItems[position].link)
                           fragment.requireContext().startActivity(i)
                       }
                       catch (e:java.lang.Exception){
                           e.printStackTrace()
                       }
                    }
                }

            } catch (e: Exception) {
                holder.setDefaultImage()
                holder.itemView.setOnClickListener {
                    Util.goToFacebookPage(fragment.requireContext())
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return sliderItems.size
    }

    inner class SliderViewHolder(@NonNull itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val imageView: RoundedImageView = itemView.findViewById(com.lincoln4791.dailyexpensemanager.R.id.imageSlide)
        fun setImage(bitmap: Bitmap) {
//use glide or picasso in case you get image from internet
            //imageView.setImageResource(sliderItems.image)
            try {
                imageView.setImageBitmap(bitmap)
            }
            catch (e:Exception){
                imageView.setImageResource(com.lincoln4791.dailyexpensemanager.R.drawable.cover_1)
            }

        }

        fun setDefaultImage() {
//use glide or picasso in case you get image from internet
            //imageView.setImageResource(sliderItems.image)
            try {
                imageView.setImageResource(com.lincoln4791.dailyexpensemanager.R.drawable.cover_1)
            }
            catch (e:Exception){
                e.printStackTrace()
            }

        }

    }

     @SuppressLint("NotifyDataSetChanged")
     private val runnable = Runnable {
         sliderItems.addAll(sliderItems)
         notifyDataSetChanged()
     }
}