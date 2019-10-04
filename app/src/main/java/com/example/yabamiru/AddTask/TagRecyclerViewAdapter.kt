package com.example.yabamiru.AddTask

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.yabamiru.Data.TaskAndTaskTags
import com.example.yabamiru.Data.TaskTags
import com.example.yabamiru.R

class TagRecyclerViewAdapter( private val context: Context) :
    RecyclerView.Adapter<TagRecyclerViewAdapter.TagViewHolder>() {

    var tagList = emptyList<TaskTags>()
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagRecyclerViewAdapter.TagViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.activity_tag,parent,false)
        return TagViewHolder(view)
    }

    override fun getItemCount() = tagList.size

    override fun onBindViewHolder(viewHolder: TagViewHolder, position: Int) {
        viewHolder.tagName.text = tagList[position].tagName
        viewHolder.cardView.setBackgroundColor(tagList[position].color)
    }

    fun setTags(taskTagsList: List<TaskTags>){
        tagList = taskTagsList
        notifyDataSetChanged()
    }

    fun addTags(taskTags:TaskTags){
        tagList += listOf(taskTags)
        notifyDataSetChanged()
    }

    class TagViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tagName = view.findViewById<TextView>(R.id.tag_text)!!
        val cardView = view.findViewById<CardView>(R.id.tag_cardView)
    }
}