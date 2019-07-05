package com.example.yabamiru

import android.media.Image
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView

class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view){

    interface ItemClickListener{
        fun onItemClick(view: View, position: Int)
    }

    val itemTextView: TextView = view.findViewById(R.id.main_row_title)
    val itemImageView: ImageView = view.findViewById(R.id.main_row_imageView)

    init{
        //layoutの初期設定をする
    }
}