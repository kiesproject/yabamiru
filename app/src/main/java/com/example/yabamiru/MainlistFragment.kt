package com.example.yabamiru

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.yabamiru.Data.AppDatabase
import kotlinx.android.synthetic.main.fragment_mainlist.*

class MainlistFragment : Fragment(), RecyclerAdapter.RecyclerViewHolder.ItemClickListener {

    lateinit var db: AppDatabase

    val titles: MutableList<String> = mutableListOf()
    val deadlines: MutableList<Long> = mutableListOf()
    val percents: MutableList<Int> = mutableListOf()
    lateinit var tagsadapter:TagRecyclerAdapter
    val tagsadapterlist: MutableList<TagRecyclerAdapter> = mutableListOf()
    private val recycleradapter: RecyclerAdapter by lazy {RecyclerAdapter(view!!.context, this, titles, deadlines, percents, tagsadapterlist)}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = AppDatabase.getDatabase(this.requireContext())
        main_recyclerView.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        main_recyclerView.adapter = recycleradapter


        db.taskDao().loadTaskAndTaskTags().observe(this, Observer { taskandtagslist ->
            if (taskandtagslist != null) {
                taskandtagslist.forEach { taskandtags ->
                    titles.add(taskandtags.task.title)
                    deadlines.add(taskandtags.task.deadLine)
                    percents.add(taskandtags.task.weight)
                    val tags: MutableList<String> = mutableListOf()
                    taskandtags.taskTags.forEach { tasktag ->
                        tags.add(tasktag.tagName)
                    }
                    tagsadapter = TagRecyclerAdapter(tags)
                    tagsadapterlist.add(tagsadapter)
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
}