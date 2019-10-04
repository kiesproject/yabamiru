package com.example.yabamiru.AddTask

import android.app.Activity
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
import com.example.yabamiru.DateFormatter
import com.example.yabamiru.R
import java.text.ParseException
import java.text.SimpleDateFormat
import kotlin.concurrent.thread

class AddTaskFragment : Fragment() {

    private val DATEPICKER_REQUEST_CODE = 101
    private val TIMEPICKER_REQUEST_CODE = 102

    private val titleEditText: EditText by lazy {view!!.findViewById<EditText>(R.id.edit_title)}
    private val weightSeekBar: SeekBar by lazy {view!!.findViewById<SeekBar>(R.id.edit_seekBar)}
    private val dateEditText: EditText by lazy {view!!.findViewById<EditText>(R.id.edit_date)}
    private val timeEditText: EditText by lazy {view!!.findViewById<EditText>(R.id.edit_time)}
    private val tagEditText: EditText by lazy {view!!.findViewById<EditText>(R.id.edit_tag)}
    private val memoEditText:EditText by lazy { view!!.findViewById<EditText>(R.id.edit_memo)}
    private val addTagButton: Button by lazy {view!!.findViewById<Button>(R.id.button_tag)}
    private val tagRecyclerView: RecyclerView by lazy {view!!.findViewById<RecyclerView>(R.id.edit_recyclerView)}

    private lateinit var adapter : TagRecyclerViewAdapter
    lateinit var db: AppDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.app_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setViews(view)
        db = AppDatabase.getDatabase(this.requireContext())
    }

    private fun setViews(view:View) {
        view.findViewById<Button>(R.id.button_add).setOnClickListener(onAddTaskButtonClicked())
        addTagButton.setOnClickListener(onAddTagButtonClicked())
        dateEditText.onFocusChangeListener = onDateEditTextFocusChanged()
        timeEditText.onFocusChangeListener = onTimeEditTextFocusChanged()

        adapter = TagRecyclerViewAdapter(view.context)
        tagRecyclerView.layoutManager = LinearLayoutManager(view.context)
        tagRecyclerView.adapter = adapter
    }

    private fun onAddTagButtonClicked() = View.OnClickListener {
        if(tagEditText.text.toString().isNotBlank() && tagEditText.text.toString().isNotEmpty()){
           val tagName = tagEditText.text.toString()
            if(adapter.tagList.count{it.tagName==tagName}==0){
                val addedTaskTags = TaskTags(
                    taskId = -1,
                    tagName = tagName,
                    color = Color.RED
                )
                adapter.addTags(addedTaskTags)
                tagEditText.text.clear()
                Toast.makeText(this.context, "タグを追加しました。", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onAddTaskButtonClicked() = View.OnClickListener {
        if(validation()){
            val task = Task(
                title = titleEditText.text.toString(),
                weight = weightSeekBar.progress,
                deadLine = DateFormatter.strToTime(dateEditText.text.toString()+timeEditText.text.toString()),
                memo = memoEditText.text.toString(),
                isActive = true)
            thread{
                val taskId = db.taskDao().insert(task)
                val taskTagsList = adapter.tagList
                taskTagsList.map { it.taskId=taskId[0] }
                db.taskTagsDao().insert(*taskTagsList.toTypedArray())
            }
            Toast.makeText(this.context,"タスクを追加しました。",Toast.LENGTH_SHORT).show()
        }
    }

    private fun onDateEditTextFocusChanged() = View.OnFocusChangeListener { view, hasFocus ->
        if (hasFocus) {
            val newFragment = DatePick()
            newFragment.setTargetFragment(this, DATEPICKER_REQUEST_CODE)
            newFragment.show(fragmentManager, "datePicker")

        }
    }

    private fun onTimeEditTextFocusChanged() = View.OnFocusChangeListener { view, hasFocus ->
        if (hasFocus) {
            val newFragment = TimePick()
            newFragment.setTargetFragment(this, TIMEPICKER_REQUEST_CODE)
            newFragment.show(fragmentManager, "timePicker")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == DATEPICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val date = data?.getStringExtra("selectedDate")
            dateEditText.setText(date)
        } else if (requestCode == TIMEPICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val time = data?.getStringExtra("selectedTime")
            timeEditText.setText(time)
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
            str += "\n正しく時間を入力してください"
        }

        //タグチェック
        if (adapter.tagList.isEmpty()) {
            flg = false
            str += "\nタグを１つ以上選択してください。"
        }

        if (!flg)
            Toast.makeText(this.context, str, Toast.LENGTH_SHORT).show()
        return flg
    }

    //datePicker,timePickerからの返答後の処理

}