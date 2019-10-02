package com.example.yabamiru.EditPage

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.arch.lifecycle.Observer
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import com.example.yabamiru.Data.AppDatabase
import com.example.yabamiru.Data.TaskAndTaskTags
import com.example.yabamiru.Data.TaskTags
import com.example.yabamiru.DateFormatter
import com.example.yabamiru.R
import kotlin.concurrent.thread

class EditActivity() : AppCompatActivity(),DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener{

    private val titleEditText: EditText by lazy { findViewById<EditText>(R.id.task_edit_title) }
    private val memoEditText: EditText by lazy { findViewById<EditText>(R.id.task_edit_memo) }
    private val dateEditText: EditText by lazy { findViewById<EditText>(R.id.task_edit_date) }
    private val timeEditText: EditText by lazy { findViewById<EditText>(R.id.task_edit_time) }
    private val tagEditText: EditText by lazy { findViewById<EditText>(R.id.task_edit_tag) }
    private val weightSeekBar: SeekBar by lazy { findViewById<SeekBar>(R.id.task_edit_seekBar) }
    private val submitButton: Button by lazy { findViewById<Button>(R.id.task_button_add) }
    private val addTagButton: Button by lazy { findViewById<Button>(R.id.task_button_tag) }
    private val tagList: RecyclerView by lazy { findViewById<RecyclerView>(R.id.task_edit_recyclerView) }

    private val db: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    private var taskAndTaskTags: TaskAndTaskTags? = null
    private var adapter: EditTagRecyclerViewAdapter? = null

    private val DATEPICKER_REQUEST_CODE = 101
    private val TIMEPICKER_REQUEST_CODE = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_edit_layout)

        initRecyclerView()
        submitButton.setOnClickListener(onSubmitButtonClicked())
        addTagButton.setOnClickListener(onAddTagButtonClicked())
        dateEditText.onFocusChangeListener = onDateEditTextClicked()
        timeEditText.onFocusChangeListener = onTimeEditTextClicked()

        val taskId = intent.getLongExtra("taskId", 0)
        if (taskId != 0L) {
            db.taskDao().loadTaskAndTaskTagsByTaskId(taskId).observe(this, Observer {
                setValue(it)
            })
        }
    }

    private fun setValue(_taskAndTaskTags: TaskAndTaskTags?) {
        taskAndTaskTags = _taskAndTaskTags
        _taskAndTaskTags?.task?.let {
            titleEditText.setText(it.title)
            dateEditText.setText(DateFormatter.timeToDateStr(it.deadLine))
            timeEditText.setText(DateFormatter.timeToTimeStr(it.deadLine))
            memoEditText.setText(it.memo)
            weightSeekBar.progress = it.weight
        }
        _taskAndTaskTags?.taskTags?.let {
            adapter?.setTagsList(it)
        }
    }

    private fun initRecyclerView() {
        adapter = EditTagRecyclerViewAdapter(this)
        tagList.adapter = adapter
        tagList.layoutManager = LinearLayoutManager(this)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        dateEditText.setText("${year}/${month}/${dayOfMonth}")
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        timeEditText.setText("${hourOfDay}:${minute}")
    }

    private fun onDateEditTextClicked() = View.OnFocusChangeListener {view,isFocused->
        if(isFocused) {
            val datePickerFragment = DatePick()
            datePickerFragment.show(supportFragmentManager, "date_picker")
        }
    }

    private fun onTimeEditTextClicked() = View.OnFocusChangeListener {view,isFocused->
        if(isFocused) {
            val timePickerFragment = TimePick()
            timePickerFragment.show(supportFragmentManager, "time_picker")
        }
    }

    private fun onAddTagButtonClicked(): View.OnClickListener = View.OnClickListener {
        if (tagEditText.text.isNotEmpty() && tagEditText.text.isNotBlank() && taskAndTaskTags != null && adapter != null) {
            val addedTaskTags = TaskTags(
                tagName = tagEditText.text.toString(),
                taskId = taskAndTaskTags!!.task.taskId,
                color = Color.RED
            )

            if (addedTaskTags !in adapter!!.tagsList) {
                adapter!!.addTagsList(addedTaskTags)
                Toast.makeText(this, "タグを追加しました。", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
        }
        Toast.makeText(this, "タグを追加できませんでした。", Toast.LENGTH_SHORT).show()
    }

    private fun onSubmitButtonClicked(): View.OnClickListener = View.OnClickListener { view ->
        taskAndTaskTags?.let { before ->
            val updated = TaskAndTaskTags().apply {
                task = before.task.copy()
                taskTags = before.taskTags
            }
            updated.apply {
                this.task.title = titleEditText.text.toString()
                this.task.memo = titleEditText.text.toString()
                this.task.weight = weightSeekBar.progress
                this.task.memo = memoEditText.text.toString()
                this.task.deadLine = DateFormatter.strToTime(
                    dateEditText.text.toString() + timeEditText.text.toString()
                )
                adapter?.let { this.taskTags = adapter!!.tagsList }
            }
            if (before.equals(updated)) {
                Toast.makeText(this, "変更点はありません", Toast.LENGTH_SHORT).show()
            } else {
                thread {
                    db.taskDao().update(updated.task)
                    db.taskTagsDao().insert(*updated.taskTags.toTypedArray())
                }
                Toast.makeText(this, "更新しました。", Toast.LENGTH_SHORT).show()
            }
        }
        //finish()
    }
}
















