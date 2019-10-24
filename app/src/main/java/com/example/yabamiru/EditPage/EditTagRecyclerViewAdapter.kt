package com.example.yabamiru.EditPage

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.yabamiru.Data.TaskTags
import com.example.yabamiru.R

class EditTagRecyclerViewAdapter(
    val context: Context
    ) : RecyclerView.Adapter<EditTagRecyclerViewAdapter.TagListViewHolder>(){

    var tagsList = emptyList<TaskTags>()
        private set

    private var inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): TagListViewHolder {
        val view = inflater.inflate(R.layout.rowstag,parent,false)
        return TagListViewHolder(view)
    }

    override fun getItemCount() = tagsList.size

    override fun onBindViewHolder(viewHolder: TagListViewHolder, position: Int) {
       viewHolder.tagName.text = tagsList[position].tagName
        viewHolder.tagCard.setCardBackgroundColor( tagsList[position].color)
    }

    fun setTagsList(tags:List<TaskTags>){
        tagsList = tags
        notifyDataSetChanged()
    }

    fun addTagsList(tag:TaskTags){
        tagsList += listOf(tag)
        notifyDataSetChanged()
    }

    class TagListViewHolder(view: View):RecyclerView.ViewHolder(view){
        val tagName = view.findViewById<TextView>(R.id.tag_text)
        val tagCard =view.findViewById<CardView>(R.id.tag_cardView)
    }
}