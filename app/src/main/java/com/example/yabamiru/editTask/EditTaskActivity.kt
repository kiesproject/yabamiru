package com.example.yabamiru.editTask

import android.app.*
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.yabamiru.R
import com.example.yabamiru.data.AppDatabase
import com.example.yabamiru.util.DateFormatter
import com.example.yabamiru.util.PxDpConverter
import com.google.android.material.chip.Chip
import androidx.lifecycle.Observer
import com.example.yabamiru.data.Constant
import com.example.yabamiru.data.ConstantColor
import com.example.yabamiru.data.model.Task
import com.example.yabamiru.data.model.TaskTags
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.android.synthetic.main.activity_edit_task.*
import java.util.*
import kotlin.concurrent.thread

class EditTaskActivity() : AppCompatActivity(), TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener {


    lateinit var db: AppDatabase
    private var taskId: Long = -1
    private var isActive:Boolean =false

    lateinit var tags: List<TaskTags>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)
        setTitle("タスクの編集")

        db = AppDatabase.getDatabase(this)

        setColor()

        taskId = intent.getLongExtra("taskId", -1)
        if (taskId != -1L) {
            db.taskDao().loadTaskAndTaskTagsByTaskId(taskId).observe(this, Observer {
                if (it != null) {
                    val task = it.task
                    isActive = task.isActive
                    tags = it.taskTags
                    editTask_title_editText.setText(task.title)
                    editTask_weight_seekBar.progress = task.weight
                    editTask_date_editText.apply {
                        setText(DateFormatter.timeToDateStr(task.deadLine))
                        setOnFocusChangeListener { view, isFocused ->
                            if (isFocused)
                                DatePick().show(
                                    supportFragmentManager,
                                    Constant.DATEPICKER_SERVICE_CODE
                                )
                        }
                    }
                    editTask_time_editText.apply {
                        setText(DateFormatter.timeToTimeStr(task.deadLine))
                        setOnFocusChangeListener { view, isFocused ->
                            if (isFocused) {
                                TimePick().show(
                                    supportFragmentManager,
                                    Constant.TIMEPICKER_SERVICE_CODE
                                )
                            }
                        }
                    }
                    editTask_memo_editText.setText(task.memo)
                    editTask_tag_flexboxLayout.removeAllViews()
                    it.taskTags.forEach { taskTags: TaskTags ->
                        val chip = Chip(this).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                setMargins(
                                    0,
                                    0,
                                    PxDpConverter.dp2Px(4f, context).toInt(),
                                    PxDpConverter.dp2Px(4f, context).toInt()
                                )
                            }
                            setChipBackgroundColorResource(ConstantColor.nowColorSet.colorTagBackground)
                            text = taskTags.tagName
                            isCheckable = false
                            isCloseIconEnabled = true
                            isClickable = false
                            isCheckedIconEnabled = false
                            setOnCloseIconClickListener {
                                tags = tags.toMutableList().filter { oldList: TaskTags ->
                                    oldList.tagName != taskTags.tagName
                                }
                                editTask_tag_flexboxLayout.removeView(this)
                            }
                        }
                        editTask_tag_flexboxLayout.addView(chip)
                    }
                    editTask_addTag_button.setOnClickListener {
                        if (!editTask_tagName_editText.text.toString().isNullOrEmpty() && !editTask_tagName_editText.text.toString().isNullOrBlank()) {
                            if (tags.size <= 4) {
                                if (tags.filter { it.tagName == editTask_tagName_editText.text.toString() }.isEmpty()) {
                                    tags = tags + listOf(
                                        TaskTags(
                                            tagName = editTask_tagName_editText.text.toString(),
                                            taskId = taskId,
                                            color = Color.RED
                                        )
                                    )
                                    val chip = Chip(this).apply {
                                        layoutParams = LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                        ).apply {
                                            setMargins(
                                                0,
                                                0,
                                                PxDpConverter.dp2Px(4f, context).toInt(),
                                                PxDpConverter.dp2Px(4f, context).toInt()
                                            )
                                        }
                                        setChipBackgroundColorResource(ConstantColor.nowColorSet.colorTagBackground)
                                        text = editTask_tagName_editText.text.toString()
                                        isCheckable = false
                                        isCloseIconEnabled = true
                                        isClickable = false
                                        isCheckedIconEnabled = false
                                        setOnCloseIconClickListener {
                                            tags =
                                                tags.toMutableList().filter { oldList: TaskTags ->
                                                    oldList.tagName != this.text
                                                }
                                            editTask_tag_flexboxLayout.removeView(this)
                                        }
                                    }
                                    editTask_tag_flexboxLayout.addView(chip)
                                    editTask_tagName_editText.setText("")
                                } else {
                                    Toast.makeText(this, "そのタグはすでに登録されています。", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            } else {
                                Toast.makeText(this, "一度に登録できるタグは5つまでです。", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }

            })
        }
    }

    fun onFabClicked(view: View) {
        if (validation()) {
            saveTask()
            Toast.makeText(this, "タスクを保存しました。", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validation(): Boolean {
        var flg = true
        var message = "タスクを登録できませんでした。"

        if (editTask_title_editText.text.isNullOrBlank() || editTask_title_editText.text.isNullOrEmpty()) {
            flg = false
            message += "\nタイトルを正しく入力してください"
        }
        if (tags.isEmpty()) {
            flg = false
            message += "\nタグは１つ以上登録してください"
        }
        if (!editTask_date_editText.text.isNullOrEmpty() && !editTask_date_editText.text.isNullOrBlank()
            && !editTask_time_editText.text.isNullOrEmpty() && !editTask_time_editText.text.isNullOrBlank()
        ) {
            if (DateFormatter.isCorrectDate(editTask_date_editText.text.toString()) && DateFormatter.isCorrectTime(
                    editTask_time_editText.text.toString()
                )
            ) {
                if (System.currentTimeMillis() >= DateFormatter.strToTime(editTask_date_editText.text.toString() + "  " + editTask_time_editText.text.toString())) {
                    flg = false
                    message += "\n未来の日付を入力してください"
                }
            } else {
                if (editTask_date_editText.text.isNullOrEmpty() || editTask_date_editText.text.isNullOrBlank()) {
                    flg = false
                    message += "\n日付を入力してください"
                } else if (!DateFormatter.isCorrectDate(editTask_date_editText.text.toString())) {
                    flg = false
                    message += "\n正しい日付を入力してください"
                }

                if (editTask_time_editText.text.isNullOrEmpty() || editTask_time_editText.text.isNullOrBlank()) {
                    flg = false
                    message += "\n日付を時間してください"
                } else if (!DateFormatter.isCorrectTime(editTask_time_editText.text.toString())) {
                    flg = false
                    message += "\n正しい時間を入力してください"
                }
            }
        }

        if (!flg)
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        return flg
    }

    private fun saveTask() {
        val newTask = Task(
            taskId = taskId,
            title = editTask_title_editText.text.toString(),
            deadLine = DateFormatter.strToTime(editTask_date_editText.text.toString() + "  " + editTask_time_editText.text.toString()),
            isActive = isActive,
            memo = editTask_memo_editText.text.toString(),
            weight = editTask_weight_seekBar.progress
        )
        val newTaskTags = mutableListOf<TaskTags>()
        tags.forEach {
            newTaskTags.add(TaskTags(taskId = taskId,tagName = it.tagName,color = Color.RED))
        }
        thread{
            db.taskTagsDao().deleteByTaskId(taskId)
            db.taskTagsDao().insert(*newTaskTags.toTypedArray())
            db.taskDao().update(newTask)
        }
    }

    override fun onTimeSet(p0: TimePicker?, hour: Int, minutes: Int) {
        editTask_time_editText.setText(String.format("%02d:%02d", hour, minutes))
        editTask_time_editText.clearFocus()
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        editTask_date_editText.setText(String.format("%04d/%02d/%02d", year, month + 1, day))
        editTask_date_editText.clearFocus()
    }

    private fun setColor() {
        val dummyDrawable = getDrawable(R.drawable.dummy)
        dummyDrawable?.let {
            it.setColorFilter(getColor(ConstantColor.nowColorSet.colorPrimary), PorterDuff.Mode.SRC)
        }
        supportActionBar?.setBackgroundDrawable(dummyDrawable)
        val titleColor = getColor(ConstantColor.nowColorSet.colorTitleText)
        val htmlColor = String.format(
            Locale.US, "#%06X",
            0xFFFFFF and Color.argb(
                0,
                Color.red(titleColor),
                Color.green(titleColor),
                Color.blue(titleColor)
            )
        )
        val titleHtml = "<font color=\"$htmlColor\">${"タスクを追加"}</font>"
        supportActionBar?.setTitle(Html.fromHtml(titleHtml))


        editTask_layout.setBackgroundResource(ConstantColor.nowColorSet.colorWindowBackground)
        editTask_fab.backgroundTintList =
            getColorStateList(ConstantColor.nowColorSet.colorFabBackground)



        editTask_weight_layout.apply {
            setBackgroundResource(ConstantColor.nowColorSet.colorCardBackground)
        }
        editTask_title_editText.apply {
            setBackgroundResource(ConstantColor.nowColorSet.colorCardBackground)
            setHintTextColor(getColor(ConstantColor.nowColorSet.colorText))
            setTextColor(getColor(ConstantColor.nowColorSet.colorText))
        }
        editTask_date_editText.apply {
            setBackgroundResource(ConstantColor.nowColorSet.colorCardBackground)
            setHintTextColor(getColor(ConstantColor.nowColorSet.colorText))
            setTextColor(getColor(ConstantColor.nowColorSet.colorText))
        }
        editTask_time_editText.apply {
            setBackgroundResource(ConstantColor.nowColorSet.colorCardBackground)
            setHintTextColor(getColor(ConstantColor.nowColorSet.colorText))
            setTextColor(getColor(ConstantColor.nowColorSet.colorText))
        }
        editTask_tagName_editText.apply {
            setBackgroundResource(ConstantColor.nowColorSet.colorCardBackground)
            setHintTextColor(getColor(ConstantColor.nowColorSet.colorText))
            setTextColor(getColor(ConstantColor.nowColorSet.colorText))
        }
        editTask_memo_editText.apply {
            setBackgroundResource(ConstantColor.nowColorSet.colorCardBackground)
            setHintTextColor(getColor(ConstantColor.nowColorSet.colorText))
            setTextColor(getColor(ConstantColor.nowColorSet.colorText))
        }

        findViewById<TextView>(R.id.addTask_weight_textView).setTextColor(getColor(ConstantColor.nowColorSet.colorText))
        findViewById<TextView>(R.id.addTask_deadline_textView).setTextColor(getColor(ConstantColor.nowColorSet.colorText))
        findViewById<TextView>(R.id.addTask_tags_textView).setTextColor(getColor(ConstantColor.nowColorSet.colorText))
        findViewById<TextView>(R.id.raku_textView).setTextColor(getColor(ConstantColor.nowColorSet.colorText))
        findViewById<TextView>(R.id.shi_textView).setTextColor(getColor(ConstantColor.nowColorSet.colorText))

        editTask_addTag_button.setBackgroundResource(ConstantColor.nowColorSet.colorFabBackground)

    }
}

class DatePick() : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            activity as EditTaskActivity,
            activity as EditTaskActivity,
            year,
            month,
            day
        )
    }

    override fun onDateSet(
        view: android.widget.DatePicker, year: Int,
        monthOfYear: Int, dayOfMonth: Int
    ) {
    }
}

class TimePick() : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    // Bundle sould be nullable, Bundle?
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // to initialize a Calender instance
        val c = Calendar.getInstance()

        // at the first, to get the system current hour and minute
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(
            activity as EditTaskActivity,
            activity as EditTaskActivity,
            hour,
            minute,
            true
        )
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        //
    }
}