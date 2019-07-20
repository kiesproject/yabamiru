package com.example.yabamiru.AddTask

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.yabamiru.R

class TagRecyclerViewAdapter(private val context: Context, private val tagList: List<String>) :
    RecyclerView.Adapter<TagRecyclerViewAdapter.TagViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagRecyclerViewAdapter.TagViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.activity_tag,parent,false)
        return TagViewHolder(view)
    }

    override fun getItemCount() = tagList.size

    override fun onBindViewHolder(viewHolder: TagRecyclerViewAdapter.TagViewHolder, position: Int) {
        viewHolder.tagName.text = tagList[position]
    }

    class TagViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tagName = view.findViewById<TextView>(R.id.tag_text)
    }
}