package com.example.yabamiru

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_tag.view.*

class TagRecyclerViewHolder(var view: View): RecyclerView.ViewHolder(view){
    val text: TextView = view.tag_text
}