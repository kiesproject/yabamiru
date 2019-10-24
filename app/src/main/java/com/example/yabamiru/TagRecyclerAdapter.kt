package com.example.yabamiru

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.yabamiru.Data.TaskTags
import kotlinx.android.synthetic.main.rowstag.view.*

class  TagRecyclerAdapter(val context: Context,private val taskTagsList:List<TaskTags>): RecyclerView.Adapter<TagRecyclerAdapter.TagRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagRecyclerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.rowstag, parent, false)
        return TagRecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return taskTagsList.size
    }

    override fun onBindViewHolder(holder: TagRecyclerViewHolder, position: Int) {
        with(holder){
            text.text = taskTagsList.get(position).tagName
        }
    }

    class TagRecyclerViewHolder(var view: View): RecyclerView.ViewHolder(view){
        val text: TextView = view.rowstag_text_view
    }
}