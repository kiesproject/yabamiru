package com.example.yabamiru

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_tag.view.*

class  TagRecyclerAdapter(var items: List<String>): RecyclerView.Adapter<TagRecyclerAdapter.TagRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagRecyclerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.activity_tag, parent, false)

        return TagRecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TagRecyclerViewHolder, position: Int) {
        holder.text.text = items.get(position)
    }


    class TagRecyclerViewHolder(var view: View): RecyclerView.ViewHolder(view){
        val text: TextView = view.tag_text
    }
}