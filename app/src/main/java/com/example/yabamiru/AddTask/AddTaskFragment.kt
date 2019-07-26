package com.example.yabamiru.AddTask

import android.arch.persistence.room.Room
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Toast
import com.example.yabamiru.Data.*
import com.example.yabamiru.R
import kotlin.concurrent.thread

class AddTaskFragment : Fragment() {

    lateinit var titleEditText: EditText
    lateinit var weightSeekBar: SeekBar
    lateinit var dateEditText: EditText
    lateinit var timeEditText: EditText
    lateinit var tagEditText: EditText
    lateinit var addTagButton: Button
    lateinit var tagRecyclerView: RecyclerView

    private val tagNameList = mutableListOf<String>()
    lateinit var db: AppDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.app_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setViews()
        db = Room.databaseBuilder(view.context, AppDatabase::class.java, "database").build()
    }

    private fun setViews() {
        val addTaskButton = view!!.findViewById<Button>(R.id.button_add)
        addTaskButton.setOnClickListener(onClickListener(addTaskButton.id))
        titleEditText = view!!.findViewById(R.id.edit_title)
        weightSeekBar = view!!.findViewById(R.id.edit_seekBar)
        dateEditText = view!!.findViewById(R.id.edit_date)
        timeEditText = view!!.findViewById(R.id.edit_time)
        tagEditText = view!!.findViewById(R.id.edit_tag)
        addTagButton = view!!.findViewById(R.id.button_tag)
        addTagButton.setOnClickListener(onClickListener(addTagButton.id))
        tagRecyclerView = view!!.findViewById(R.id.edit_recyclerView)
        tagRecyclerView.layoutManager= LinearLayoutManager(view!!.context)

    }

    private fun onClickListener(viewId: Int): View.OnClickListener {
        when (viewId) {
            R.id.button_add -> {
                return View.OnClickListener {
                    //if(validation())
                       // addTask()
                }
            }
            R.id.button_tag -> {
                return View.OnClickListener {
                    addTag()
                }
            }
        }
        return View.OnClickListener { }
    }

    private fun addTag() {
        if (tagEditText.text.isEmpty()||tagEditText.text.isBlank()){
            Toast.makeText(this.context,"タグ名を入力してください。。",Toast.LENGTH_SHORT).show()
        }else if (tagNameList.contains(tagEditText.text.toString())){
            Toast.makeText(this.context,"その名前のタグは追加済みです。",Toast.LENGTH_SHORT).show()
        }else{
            tagNameList.add(tagEditText.text.toString())
            if(tagRecyclerView.adapter==null)
                tagRecyclerView.adapter=TagRecyclerViewAdapter(this.context!!,tagNameList)
            else {
                tagRecyclerView.adapter!!.notifyItemInserted(tagNameList.size-1)
            }
        }
    }

//    private fun addTask(){
//        val task = Task(title=titleEditText.text.toString(),weight = weightSeekBar.progress,deadLine = 0,memo =view!!.findViewById<EditText>(R.id.edit_memo).text.toString() ,isActive = true)
//        thread {
//            val taskId =db.taskDao().insert(task)
//            val allTagList = db.tagDao().getAll()
//            val taskTags = mutableListOf<TaskTags>()
//            tagNameList.forEach{tagName->
//                if(allTagList. { tagName.name == tagName }){
//                    val tagId=db.tagDao().insert(Tag(name=tagName,color= Color.RED))
//                    taskTags.add(TaskTags(tagId[0],taskId[0]))
//                }else{
//                    taskTags.add(TaskTags(allTagList.filter { it.name==tagName }[0].tagId,taskId[0]))
//                }
//            }
//            db.taskTagsDao().insert(*taskTags.toTypedArray())
//        }
//    }

    private fun validation(): Boolean {
        var flg = true
        var str = "タスクを追加できませんでした。"
        if (titleEditText.text.isEmpty() || titleEditText.text.isBlank()) {
            flg = false
            str += "\nタイトルを入力してください。"
        }
        if (dateEditText.text.isEmpty() || timeEditText.text.isEmpty() ||
            dateEditText.text.isBlank() || timeEditText.text.isBlank()
        ) {
            flg = false
            str += "\n期限を正しく入力してください。"
        }
        if(tagNameList.size==0){
            flg=false
            str+="\nタグを１つ以上選択してください。"
        }
        if(!flg)
            Toast.makeText(this.context,str,Toast.LENGTH_SHORT).show()
        return flg
    }
}