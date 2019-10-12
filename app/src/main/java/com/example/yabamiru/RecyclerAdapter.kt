package com.example.yabamiru

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.yabamiru.Data.TaskAndTaskTags
import kotlinx.android.synthetic.main.main_list_row.view.*

class RecyclerAdapter(val context: Context) :
    RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>() {

    private var taskAndTaskTagsList: List<TaskAndTaskTags> = emptyList()

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        with(holder) {
            itemCard.setOnClickListener {
                Toast.makeText(context, "position $position was tapped", Toast.LENGTH_SHORT).show()
            }
            itemImageView.setImageResource(R.drawable.dokuro_blue)
            itemTitleView.text = taskAndTaskTagsList.get(position).task.title
            itemDeadlineView.text = taskAndTaskTagsList.get(position).task.deadLine.toString()
            itemPercent.text = taskAndTaskTagsList.get(position).task.weight.toString()
            taskAndTaskTagsList.get(position).taskTags
            itemTags.adapter = TagRecyclerAdapter(context, taskAndTaskTagsList.get(position).taskTags)
            itemTags.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun getItemCount(): Int {
        return taskAndTaskTagsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val mView = layoutInflater.inflate(R.layout.main_list_row, parent, false)
        return RecyclerViewHolder(mView)
    }

    fun setList(taskAndTaskTagsList: List<TaskAndTaskTags>) {
        this.taskAndTaskTagsList = taskAndTaskTagsList
        this.notifyDataSetChanged()
    }

    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val itemCard: CardView = view.row_cardview
        val itemImageView: ImageView = view.main_row_imageView
        val itemTitleView: TextView = view.row_title
        val itemDeadlineView: TextView = view.row_deadline
        val itemPercent: TextView = view.row_percent
        val itemTags: RecyclerView = view.row_tag_listview
    }

}