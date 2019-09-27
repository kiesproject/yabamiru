package com.example.yabamiru.AddTask

import android.app.Activity
import android.arch.persistence.room.Room
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.yabamiru.Data.*
import com.example.yabamiru.R
import java.text.ParseException
import java.text.SimpleDateFormat
import kotlin.concurrent.thread

class AddTaskFragment : Fragment() {

    private val DATEPICKER_REQUEST_CODE = 1
    private val TIMEPICKER_REQUEST_CODE = 2

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
        dateEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                val newFragment = DatePick()
                newFragment.setTargetFragment(this, DATEPICKER_REQUEST_CODE)
                newFragment.show(fragmentManager, "datePicker")

            }
        }
        timeEditText = view!!.findViewById(R.id.edit_time)
        timeEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                val newFragment = TimePick()
                newFragment.setTargetFragment(this, TIMEPICKER_REQUEST_CODE)
                newFragment.show(fragmentManager, "timePicker")
            }
        }
        tagEditText = view!!.findViewById(R.id.edit_tag)
        addTagButton = view!!.findViewById(R.id.button_tag)
        addTagButton.setOnClickListener(onClickListener(addTagButton.id))
        tagRecyclerView = view!!.findViewById(R.id.edit_recyclerView)
        tagRecyclerView.layoutManager = LinearLayoutManager(view!!.context)
    }

    private fun onClickListener(viewId: Int): View.OnClickListener {
        when (viewId) {
            R.id.button_add -> {
                return View.OnClickListener {
                    if (validation())
                        addTask()
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

    //タグの追加
    private fun addTag() {
        if (tagEditText.text.isEmpty() || tagEditText.text.isBlank()) {
            Toast.makeText(this.context, "タグ名を入力してください。。", Toast.LENGTH_SHORT).show()
        } else if (tagNameList.contains(tagEditText.text.toString())) {
            Toast.makeText(this.context, "その名前のタグは追加済みです。", Toast.LENGTH_SHORT).show()
        } else {
            tagNameList.add(tagEditText.text.toString())
            if (tagRecyclerView.adapter == null)
                tagRecyclerView.adapter = TagRecyclerViewAdapter(this.context!!, tagNameList)
            else {
                tagRecyclerView.adapter!!.notifyItemInserted(tagNameList.size - 1)
            }
            Toast.makeText(this.context, "タグを追加しました。", Toast.LENGTH_SHORT).show()
            tagEditText.setText("")
        }
    }

    //dbへの追加処理
    private fun addTask() {
        val dateTime = dateEditText.text.toString() + " " + timeEditText.text.toString()
        val pattern = SimpleDateFormat("yyyy/MM/dd HH:mm")
        val time = pattern.parse(dateTime).time
        val task = Task(
            title = titleEditText.text.toString(), weight = weightSeekBar.progress, deadLine = time,
            memo = view!!.findViewById<EditText>(R.id.edit_memo).text.toString(), isActive = true,finishedYabasa = 0f
        )
        thread {
            val taskId = db.taskDao().insert(task)
            val allTagList = db.tagDao().getAll().value ?: mutableListOf<Tag>()
            val taskTags = mutableListOf<TaskTags>()
            tagNameList.forEach { tagName ->
                val filtered: List<Tag>? = allTagList.filter { it.name == tagName }
                if (filtered != null) {
                    val tagId = db.tagDao().insert(Tag(name = tagName, color = Color.RED))
                    taskTags.add(TaskTags(tagId[0], taskId[0]))
                } else {
                    taskTags.add(TaskTags(allTagList.filter { it.name == tagName }[0].tagId, taskId[0]))
                }
            }
            db.taskTagsDao().insert(*taskTags.toTypedArray())
        }
    }

    //入力値チェック
    private fun validation(): Boolean {
        var flg = true
        var str = "タスクを追加できませんでした。"

        //タイトルチェック
        if (titleEditText.text.isEmpty() || titleEditText.text.isBlank()) {
            flg = false
            str += "\nタイトルを入力してください。"
        }

        //date|timeチェック
        if (dateEditText.text.isEmpty() || timeEditText.text.isEmpty() ||
            dateEditText.text.isBlank() || timeEditText.text.isBlank()
        ) {
            flg = false
            str += "\n期限を入力してください。"
        }

        try {
            val pattern = SimpleDateFormat("yyyy/MM/dd")
            pattern.isLenient = false
            val date = pattern.parse(dateEditText.text.toString())
        } catch (e: ParseException) {
            flg = false
            str += "\n正しく日付を入力してください"
        }
        try {
            val pattern = SimpleDateFormat("HH:mm")
            val time = pattern.parse(timeEditText.text.toString())
        } catch (e: ParseException) {
            flg = false
            str += "\n正しく時間をを入力してください"
        }

        //タグチェック
        if (tagNameList.size == 0) {
            flg = false
            str += "\nタグを１つ以上選択してください。"
        }

        if (!flg)
            Toast.makeText(this.context, str, Toast.LENGTH_SHORT).show()
        return flg
    }

    //datePicker,timePickerからの返答後の処理
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == DATEPICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val date = data?.getStringExtra("selectedDate")
            dateEditText.setText(date)
        } else if (requestCode == TIMEPICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val time = data?.getStringExtra("selectedTime")
            timeEditText.setText(time)
        }
    }
}