package com.example.yabamiru.taskList

import android.content.Context
import android.content.Intent
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yabamiru.util.DateFormatter
import com.example.yabamiru.R
import com.example.yabamiru.data.Constant
import com.example.yabamiru.data.model.TaskAndTaskTags
import com.example.yabamiru.data.model.TaskTags
import com.example.yabamiru.detailTask.DetailTaskActivity
import com.example.yabamiru.util.PxDpConverter
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip
import java.lang.Math.pow
import java.util.*


class TaskListRecyclerViewAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var taskAndTaskTagsList: List<TaskAndTaskTags> = emptyList()
    private val inflater = LayoutInflater.from(context)

    var itemType: Int = Constant.ACTIVE_ITEMS

    enum class ViewType(val id: Int) {
        TOP(0) {
            override fun bindViewHolder(
                holder: RecyclerView.ViewHolder,
                item: TaskAndTaskTags,
                context: Context,
                taskAndTaskTagsList: List<TaskAndTaskTags>
            ) {
                holder as TopItemViewHolder
                val now = Date().time
                var maxTime = 0L
                var limitTime = 0L
                taskAndTaskTagsList.forEach {
                    if (it.task.isActive) {
                        maxTime += pow(
                            (it.task.deadLine - it.task.timeStump).toDouble(),
                            (1 + it.task.weight / 20).toDouble()
                        ).toLong()
                        limitTime += if (it.task.deadLine - now < 0) 0L else pow(
                            (it.task.deadLine - now).toDouble(),
                            (1 + it.task.weight / 20).toDouble()
                        ).toLong()
                    }
                }
                val yabasa = if (maxTime != 0L) 100 - (limitTime * 100 / maxTime).toInt() else 0
                holder.yabasaTextView.text = yabasa.toString()+"%"
            }

            override fun createViewHolder(
                inflater: LayoutInflater,
                viewGroup: ViewGroup?
            ): RecyclerView.ViewHolder {
                return TopItemViewHolder(
                    inflater.inflate(
                        R.layout.item_tasklist_top,
                        viewGroup,
                        false
                    )
                )
            }
        },
        TASK(1) {
            override fun bindViewHolder(
                holder: RecyclerView.ViewHolder,
                item: TaskAndTaskTags,
                context: Context,
                taskAndTaskTagsList: List<TaskAndTaskTags>
            ) {
                holder as TaskItemViewHolder
                with(holder) {
                    val taskAndTaskTags = item
                    titleTextView.text = taskAndTaskTags.task.title
                    deadlineTextView.text =
                        DateFormatter.timeToStrForTaskList(taskAndTaskTags.task.deadLine)
                    if(taskAndTaskTags.task.isActive){
                        val now = Date().time
                        val maxTime = pow(
                            (item.task.deadLine - item.task.timeStump).toDouble(),
                            (1 + item.task.weight / 20).toDouble()
                        ).toLong()
                        val limitTime = if (item.task.deadLine - now < 0) 0L else pow(
                            (item.task.deadLine - now).toDouble(),
                            (1 + item.task.weight / 20).toDouble()
                        ).toLong()
                        yabasaPercentTextView.text = (100 - (limitTime * 100 / maxTime).toInt()).toString()+"%"
                        yabasaText.visibility = View.VISIBLE
                        yabasaPercentTextView.visibility =View.VISIBLE
                    }else{
                        yabasaText.visibility = View.GONE
                        yabasaPercentTextView.visibility =View.GONE
                    }
                    tagsFlexboxLayout.removeAllViews()
                    taskAndTaskTags.taskTags.forEach { tags: TaskTags ->
                        val chip = Chip(context).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                setMargins(0, 0, PxDpConverter.dp2Px(4f, context).toInt(), 0)
                            }
                            text = tags.tagName
                            isCheckable = false
                            isCloseIconEnabled = false
                            isClickable = false
                            isCheckedIconEnabled = false
                        }
                        tagsFlexboxLayout.addView(chip)
                    }
                    layout.setOnClickListener {
                        val intent = Intent(context, DetailTaskActivity::class.java)
                        intent.putExtra("taskId", taskAndTaskTags.task.taskId)
                        (context as TaskListActivity).startActivityForResult(
                            intent,
                            Constant.DETAIL_TASK_REQUEST_CODE
                        )
                    }

                    isActiveChip.text = if (taskAndTaskTags.task.isActive) "Active" else "Completed"
                    isActiveChip.setChipBackgroundColorResource(if (taskAndTaskTags.task.isActive) R.color.colorPrimary else R.color.colorPrimaryShadow)
                }
            }

            override fun createViewHolder(
                inflater: LayoutInflater,
                viewGroup: ViewGroup?
            ): RecyclerView.ViewHolder {
                return TaskItemViewHolder(
                    inflater.inflate(
                        R.layout.item_tasklist,
                        viewGroup,
                        false
                    )
                )
            }
        };

        companion object {
            fun forId(id: Int): ViewType {
                for (viewType: ViewType in values()) {
                    if (viewType.id == id) {
                        return viewType
                    }
                }
                throw AssertionError("no enum found for the id. you forgot to implement?")
            }
        }

        abstract fun createViewHolder(
            inflater: LayoutInflater,
            viewGroup: ViewGroup?
        ): RecyclerView.ViewHolder

        abstract fun bindViewHolder(
            holder: RecyclerView.ViewHolder,
            item: TaskAndTaskTags,
            context: Context,
            taskAndTaskTagsList: List<TaskAndTaskTags>
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder ?: return
//        val item = if (position==0) taskAndTaskTagsList[0] else taskAndTaskTagsList[position-1]
        if (taskAndTaskTagsList.isNotEmpty()) {
            if (itemType == Constant.ACTIVE_ITEMS) {
                val item =
                    if (position == 0) taskAndTaskTagsList[0] else taskAndTaskTagsList[position - 1]
//            val item = taskAndTaskTagsList[position]
                ViewType.forId(holder.itemViewType)
                    .bindViewHolder(holder, item, context, taskAndTaskTagsList)
            }else{
                val item = taskAndTaskTagsList[position]
                ViewType.forId(holder.itemViewType)
                    .bindViewHolder(holder, item, context, taskAndTaskTagsList)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (itemType == Constant.ACTIVE_ITEMS)
            taskAndTaskTagsList.size + 1
        else
            taskAndTaskTagsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewType.forId(viewType).createViewHolder(inflater, parent)
    }

    fun setList(taskAndTaskTagsList: List<TaskAndTaskTags>, itemType: Int) {
        this.taskAndTaskTagsList = taskAndTaskTagsList
        this.itemType = itemType
        Log.d("TaskListAdapter", "Set Items.")

        notifyDataSetChanged()
    }

    companion object {
        private class TaskItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val layout = view.findViewById<ConstraintLayout>(R.id.taskListItem_constraintLayout)
            val titleTextView = view.findViewById<TextView>(R.id.taskListItem_title_textView)
            val deadlineTextView = view.findViewById<TextView>(R.id.taskListItem_deadline_textView)
            val yabasaPercentTextView = view.findViewById<TextView>(R.id.taskListItem_yabasaPercent_textView)
            val yabasaText=view.findViewById<TextView>(R.id.taskListItem_yabasa_text)
            val tagsFlexboxLayout =
                view.findViewById<FlexboxLayout>(R.id.tasklListItem_tags_felxboxLayout)
            val isActiveChip = view.findViewById<Chip>(R.id.talkListItem_isActive_chip)
        }

        class TopItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val yabasaTextView =
                view.findViewById<TextView>(R.id.taskListTopIten_abasaPercent_textView)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemType == Constant.ACTIVE_ITEMS) {
            when (position) {
                0 -> {
                    ViewType.TOP.id
                }
                else -> {
                    ViewType.TASK.id
                }
            }
        } else {
            ViewType.TASK.id
        }
    }
}