package com.example.yabamiru

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.yabamiru.Data.TaskAndTaskTags
import com.example.yabamiru.Data.TaskTags
import java.text.FieldPosition

class RecycleA(context:Context):RecyclerView.Adapter<RecycleA.Viewholder>(){
    private var taskTags = emptyList<TaskTags>()
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): Viewholder {
        var inflater = LayoutInflater.from(parent.context)
        var view = inflater.inflate(R.layout.activity_tag,parent,false)
        return Viewholder(view)
    }

    override fun getItemCount(): Int {
        return taskTags.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.card.setBackgroundColor(taskTags[position].color)
        holder.tagName.text = taskTags[position].tagName
    }

    class Viewholder(view:View):RecyclerView.ViewHolder(view) {
        val card = view.findViewById<CardView>(R.id.tag_cardview)
        val tagName = view.findViewById<TextView>(R.id.tag_text)
    }

}
