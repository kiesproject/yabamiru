package com.example.yabamiru

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.yabamiru.Data.AppDatabase
import kotlinx.android.synthetic.main.fragment_mainlist.*

class MainlistFragment : Fragment(), RecyclerAdapter.RecyclerViewHolder.ItemClickListener {

    lateinit var db: AppDatabase

    val taskIds = mutableSetOf<Long>()
    val titles = mutableListOf<String>()
    val deadLines = mutableListOf<Long>()
    val percents = mutableListOf<Int>()
    lateinit var tagsadapter: TagRecyclerAdapter
    val tagsadapterlist = mutableListOf<TagRecyclerAdapter>()
    private val recycleradapter by lazy {
        RecyclerAdapter(
            view!!.context,
            this,
            titles,
            deadLines,
            percents,
            tagsadapterlist
        )
    }
    val cardList = mutableListOf<Card>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = AppDatabase.getDatabase(this.requireContext())

        main_recyclerView.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        main_recyclerView.adapter = recycleradapter


        db.taskDao().loadTaskAndTaskTags().observe(this, Observer { taskandtagslist ->
            if (taskandtagslist != null) {
                taskandtagslist.forEach { taskandtags ->
                    if (taskandtags.task.taskId in taskIds) {
                    } else {


                        val tags: MutableList<String> = mutableListOf()
                        taskandtags.taskTags.forEach { tasktag ->
                            tags.add(tasktag.tagName)
                        }
                        tagsadapter = TagRecyclerAdapter(tags)
                        setTask(
                            taskandtags.task.taskId,
                            taskandtags.task.title,
                            taskandtags.task.deadLine,
                            taskandtags.task.weight,
                            tagsadapter
                        )
                        val card = Card(taskandtags.task.taskId, taskandtags.task.title,taskandtags.task.deadLine, taskandtags.task.weight, tagsadapter)
                        cardList.add(card)
                    }
                }
            }
            recycleradapter.notifyDataSetChanged()
        })

        val spinnerItems = arrayOf(
            "昇順",
            "降順"
        )

        val spinneradapter = ArrayAdapter(
            getActivity()!!.getApplicationContext(),
            android.R.layout.simple_spinner_item,
            spinnerItems
        )
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        main_spinner_button.adapter = spinneradapter


        main_spinner_button.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?, position: Int, id: Long
            ) {
                val spinnerParent = parent as Spinner
                val item = spinnerParent.selectedItem as String

                sortCard(item)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }

//        temporary_add_task_button.setOnClickListener {
//            sortCard("")
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mainlist, container, false)
    }

    override fun onItemClick(view: View, position: Int) {
        Toast.makeText(view.context, "position $position was tapped", Toast.LENGTH_SHORT).show()
    }

    private fun sortCard(status: String) {
        when (status) {
            "降順" -> {
                val sortValues = percents.zip(cardList) as MutableList
                sortValues.sortBy { it.first }
                val sortedCards = sortValues.unzip().second as MutableList<Card>
                Log.d("mainlistfragment", "${sortedCards}")

//                sortedtaskids.forEach{taskid ->
//
//                }
//                recycleradapter.notifyDataSetChanged()
            }
        }
    }

    data class Card(val taskId: Long,
                    val title: String,
                    val deadLine: Long,
                    val weight: Int,
                    val tagsadapter: TagRecyclerAdapter)


    private fun setTask(
        taskId: Long,
        title: String,
        deadLine: Long,
        weight: Int,
        tagsadapter: TagRecyclerAdapter
    ) {
        taskIds.add(taskId)
        titles.add(title)
        deadLines.add(deadLine)
        percents.add(weight)
        tagsadapterlist.add(tagsadapter)
    }
}