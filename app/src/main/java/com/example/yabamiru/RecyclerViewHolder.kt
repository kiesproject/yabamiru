package com.example.yabamiru

import android.media.Image
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.main_list_row.view.*

class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view){

    interface ItemClickListener{
        fun onItemClick(view: View, position: Int)
    }


    val itemImageView: ImageView = view.main_row_imageView
    val itemTitleView: TextView = view.row_title
    val itemDeadlineView: TextView = view.row_deadline
    val itemPercent: TextView = view.row_percent
    val itemTags: RecyclerView = view.row_tag_listview

    init{
        //layoutの初期設定をする
    }
}