package com.example.yabamiru.addTask

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.yabamiru.R
import com.example.yabamiru.data.AppDatabase
import com.example.yabamiru.data.Constant
import com.example.yabamiru.data.model.Task
import com.example.yabamiru.data.model.TaskTags
import com.example.yabamiru.editTask.EditTaskActivity
import com.example.yabamiru.util.DateFormatter
import com.example.yabamiru.util.PxDpConverter
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.android.synthetic.main.activity_edit_task.*
import java.util.*
import kotlin.concurrent.thread


class AddTaskActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var db: AppDatabase
    private var tags: List<TaskTags> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        setTitle(getString(R.string.add_task))

        db = AppDatabase.getDatabase(this)

        addTask_time_editText.setOnFocusChangeListener { view, b ->
            if (b)
                TimePick().show(supportFragmentManager, Constant.TIMEPICKER_SERVICE_CODE)
        }
        addTask_date_editText.setOnFocusChangeListener { view, b ->
            if (b)
                DatePick().show(supportFragmentManager, Constant.DATEPICKER_SERVICE_CODE)
        }
        addTask_addTag_button.setOnClickListener {
            if (!addTask_tagName_editText.text.toString().isNullOrEmpty() && !addTask_tagName_editText.text.toString().isNullOrBlank()) {
                if (tags.size <= 4) {
                    if (tags.none { it.tagName == addTask_tagName_editText.text.toString() }) {
                        tags = tags + listOf(
                            TaskTags(
                                tagName = addTask_tagName_editText.text.toString(),
                                taskId = -1L,
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
                            text = addTask_tagName_editText.text.toString()
                            isCheckable = false
                            isCloseIconEnabled = true
                            isClickable = false
                            isCheckedIconEnabled = false
                            setOnCloseIconClickListener {
                                tags =
                                    tags.toMutableList().filter { oldList: TaskTags ->
                                        oldList.tagName != this.text
                                    }
                                addTask_tag_flexboxLayout.removeView(this)
                            }
                        }
                        addTask_tag_flexboxLayout.addView(chip)
                        addTask_tagName_editText.setText("")
                    } else {
                        Toast.makeText(this, getString(R.string.the_tag_already_added), Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this, getString(R.string.max_number_of_tags_is_five), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onTimeSet(p0: TimePicker?, hour: Int, minutes: Int) {
        addTask_time_editText.setText(String.format("%02d:%02d", hour, minutes))
        addTask_time_editText.clearFocus()
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        addTask_date_editText.setText(String.format("%04d/%02d/%02d", year, month + 1, day))
        addTask_date_editText.clearFocus()
    }

    fun onFabClicked(view: View) {
        if (validation()) {
            view.isClickable=false
            saveTask()
            Toast.makeText(this, getString(R.string.saved_task), Toast.LENGTH_SHORT).show()
        }
    }

    private fun validation(): Boolean {
        var flg = true
        var message = getString(R.string.cannot_save_task)

        if (addTask_title_editText.text.isNullOrBlank() || addTask_title_editText.text.isNullOrEmpty()) {
            flg = false
            message += getString(R.string.input_correct_title)
        }
        if (tags.isEmpty()) {
            flg = false
            message += getString(R.string.add_at_least_1_tag)
        }
        if (!addTask_date_editText.text.isNullOrEmpty() && !addTask_date_editText.text.isNullOrBlank()
            && !addTask_time_editText.text.isNullOrEmpty() && !addTask_time_editText.text.isNullOrBlank()
        ) {
            if (DateFormatter.isCorrectDate(addTask_date_editText.text.toString()) && DateFormatter.isCorrectTime(
                    addTask_time_editText.text.toString()
                )
            ) {
                if (System.currentTimeMillis() >= DateFormatter.strToTime(addTask_date_editText.text.toString() + "  " + addTask_time_editText.text.toString())) {
                    flg = false
                    message += getString(R.string.input_future_date)
                }
            }
        } else {
            if (addTask_date_editText.text.isNullOrEmpty() || addTask_date_editText.text.isNullOrBlank()) {
                flg = false
                message += getString(R.string.input_date)
            } else if (!DateFormatter.isCorrectDate(addTask_date_editText.text.toString())) {
                flg = false
                message += getString(R.string.input_correct_date)
            }

            if (addTask_time_editText.text.isNullOrEmpty() || addTask_time_editText.text.isNullOrBlank()) {
                flg = false
                message += getString(R.string.input_time)
            } else if (!DateFormatter.isCorrectTime(addTask_time_editText.text.toString())) {
                flg = false
                message += getString(R.string.input_correct_time)
            }
        }

        if (!flg)
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        return flg
    }

    private fun saveTask() {
        val newTask = Task(
            title = addTask_title_editText.text.toString(),
            deadLine = DateFormatter.strToTime(addTask_date_editText.text.toString() + "  " + addTask_time_editText.text.toString()),
            isActive = true,
            memo = addTask_memo_editText.text.toString(),
            weight = addTask_weight_seekBar.progress
        )

        thread {
            val taskId=db.taskDao().insert(newTask)
            val newTaskTags = mutableListOf<TaskTags>()
            tags.forEach {
                newTaskTags.add(
                    TaskTags(
                        taskId = taskId[0],
                        tagName = it.tagName,
                        color = Color.RED
                    )
                )
            }
            db.taskTagsDao().insert(*newTaskTags.toTypedArray())
        }
        finish()

    }
}

class DatePick : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            activity as AddTaskActivity,
            activity as AddTaskActivity,
            year,
            month,
            day
        )
    }

    override fun onDateSet(
        view: DatePicker, year: Int,
        monthOfYear: Int, dayOfMonth: Int
    ) {
    }
}

class TimePick : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    // Bundle sould be nullable, Bundle?
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // to initialize a Calender instance
        val c = Calendar.getInstance()

        // at the first, to get the system current hour and minute
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(
            activity as AddTaskActivity,
            activity as AddTaskActivity,
            hour,
            minute,
            true
        )
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        //
    }
}