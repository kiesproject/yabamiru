package com.example.yabamiru

import android.view.View
import android.widget.TextView

class RecyclerViewHolder(view: View) : RecyclerViewHolder.ViewHolder(view){

    interface ItemClickListener{
        fun onItemClick(view: View, position: Int)
    }

    val itemTextView: TextView = view.findViewById(R.id.)
}