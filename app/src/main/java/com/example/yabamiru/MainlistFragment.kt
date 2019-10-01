package com.example.yabamiru

import android.arch.lifecycle.Observer
import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.yabamiru.Data.AppDatabase
import com.example.yabamiru.Data.Tag
import com.example.yabamiru.Data.Task
import kotlinx.android.synthetic.main.fragment_mainlist.*
import kotlin.concurrent.thread

class MainlistFragment : Fragment(), RecyclerAdapter.RecyclerViewHolder.ItemClickListener {

    lateinit var db: AppDatabase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val taskids: MutableSet<Long> = mutableSetOf()
        val tagids: MutableSet<Long> = mutableSetOf()
        val titles: MutableList<String> = mutableListOf()
        val deadlines: MutableList<Long> = mutableListOf()
        //仮置きではweightをpercentとする
        val percents: MutableList<Int> = mutableListOf()

        lateinit var adapter: TagRecyclerAdapter
        val adapterlist: MutableSet<TagRecyclerAdapter> = mutableSetOf()

        db = Room.databaseBuilder(view.context, AppDatabase::class.java, "database").build()


        //Databaseの仮置き
        val task = Task(
            title = "MyTitle",
            weight = 10,
            deadLine = 10,
            memo = "MyMemo",
            isActive = true,
            finishedYabasa = 0f
        )
        val tag = Tag(
            name = "MyTag",
            color = 0
        )

        temporary_add_task_button.setOnClickListener{
            thread {
                db.taskDao().insert(task)
                db.tagDao().insert(tag)
            }
        }


//        val tags1 = resources.getStringArray(R.array.tags1).toMutableList()
//        val adapter1 = TagRecyclerAdapter(tags1)
//        val tags2 = resources.getStringArray(R.array.tags2).toMutableList()
//        val adapter2 = TagRecyclerAdapter(tags2)
//        val tags3 = resources.getStringArray(R.array.tags3).toMutableList()
//        val adapter3 = TagRecyclerAdapter(tags3)
//        val tags4 = resources.getStringArray(R.array.tags4).toMutableList()
//        val adapter4 = TagRecyclerAdapter(tags4)
//        val tags5 = resources.getStringArray(R.array.tags5).toMutableList()
//        val adapter5 = TagRecyclerAdapter(tags5)
//        val adapterlist = mutableListOf(adapter1, adapter2, adapter3, adapter4, adapter5)

        val tasklist = db.taskDao().getAll().value
        Log.d("TAG", "$tasklist")
//db.taskDao().getAll().value
//        //TaskのDatabase呼び出し
//        db.taskDao().getAll().observe(this, Observer<List<Task>> { tasks ->
//            if (tasks != null) {
//                tasks.forEach{task ->
//                    if(task.taskId in taskids){
//                    }else{
//                        taskids.add(task.taskId)
//                        titles.add(task.title)
//                        deadlines.add(task.deadLine)
//                        percents.add(task.weight)
//                        db.tagDao().getAll().value
////                        val tags = mutableListOf<String>(task.taskId.toString(), (task.taskId + 1).toString())
////                        adapter = TagRecyclerAdapter(tags)
////                        adapterlist.add(adapter)
//                    }
//                }
//            }
//        })
//
//        db.tagDao().getAll().observe(this, Observer<List<Tag>> { tags ->
//            if (tags != null) {
//                tags.forEach{tag ->
//                    if(tag.tagId in tagids){
//                    }else{
//
//                    }
//                }
//            }
//        })
//
//
//
//        main_recyclerView.adapter =
//            RecyclerAdapter(view.context, this, titles, deadlines, percents, adapterlist)
//        main_recyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)

//        Log.d("TAG", "$titles")



//        val titles = resources.getStringArray(R.array.titles).toMutableList()
//        val deadlines = resources.getStringArray(R.array.deadlines).toMutableList()
//        val percents = resources.getStringArray(R.array.percents).toMutableList()


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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mainlist, container, false)
    }

    override fun onItemClick(view: View, position: Int) {
        Toast.makeText(view.context, "position $position was tapped", Toast.LENGTH_SHORT).show()
    }
}