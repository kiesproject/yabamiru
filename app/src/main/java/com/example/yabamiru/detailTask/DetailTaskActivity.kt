package com.example.yabamiru.detailTask

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.yabamiru.R
import com.example.yabamiru.data.AppDatabase
import com.example.yabamiru.editTask.EditTaskActivity
import com.example.yabamiru.util.DateFormatter
import com.example.yabamiru.util.PxDpConverter
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_detail.*
import kotlin.concurrent.thread

class DetailTaskActivity : AppCompatActivity() {
    lateinit var db: AppDatabase
    private var taskId: Long = -1

    private var isActive = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        db = AppDatabase.getDatabase(this)
        taskId = intent.getLongExtra("taskId", -1)
        if (taskId != -1L) {
            db.taskDao().loadTaskAndTaskTagsByTaskId(taskId).observe(this, Observer {
                if (it != null) {
                    val task = it.task
                    setTitle(task.title)
                    isActive = task.isActive
                    detail_title_textView.text = task.title
                    detail_weight_seekBar.progress = task.weight
                    detail_weight_seekBar.setOnSeekBarChangeListener(object :
                        SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(seekBar: SeekBar?, p1: Int, p2: Boolean) {
                            if (seekBar != null)
                                seekBar.progress = task.weight
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar?) {
                            if (seekBar != null)
                                seekBar.progress = task.weight
                        }

                        override fun onStopTrackingTouch(seekBar: SeekBar?) {
                            if (seekBar != null)
                                seekBar.progress = task.weight
                        }
                    })
                    detail_weight_textView.text = task.weight.toString()
                    detail_deadline_textView.text =
                        DateFormatter.timeToStrForTaskList(task.deadLine)
                    detail_memo_editText.text = task.memo
                    detail_tag_linearLayout.removeAllViews()
                    it.taskTags.forEach {
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
                            text = it.tagName
                            isCheckable = false
                            isCloseIconEnabled = false
                            isClickable = false
                            isCheckedIconEnabled = false
                        }
                        detail_tag_linearLayout.addView(chip)
                    }
                    detail_complete_or_reopen_button.apply {
                        if (isActive) {
                            text = "Complete"
                            setOnClickListener {
                                completeTask()
                            }

                        } else {
                            text = "Reopen"
                            setOnClickListener {
                                reopenTask()
                            }
                        }
                    }
                    detail_isActive_button.text = if (isActive) "Active" else "Completed"
                    detail_isActive_button.setBackgroundResource(if(isActive) R.color.colorPrimary else R.color.colorPrimaryShadow)
                }
            })
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detailtask_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.detailTask_delete -> {
                showDialog()
                return true
            }
            R.id.detailTask_edit -> {
                if(isActive) {
                    val intent = Intent(this, EditTaskActivity::class.java)
                    intent.putExtra("taskId", taskId)
                    startActivity(intent)
                }else{
                    Toast.makeText(this,"終了したタスクは編集することはできません。\n編集するにはタスクをReopenしてください。",Toast.LENGTH_LONG).show()
                }
            }
        }
        return false
    }

    private fun showDialog() {

        val alertDialog = AlertDialog.Builder(this, R.style.MyAlertDialogStyle)
            .setTitle("注意!")
            .setMessage("本当にタスクを削除しますか?")
            .setPositiveButton("OK", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    deleteTask()
                }
            })
            .setNegativeButton("Cancel", null)
        alertDialog.show()
    }

    private fun deleteTask() {
        if (taskId != -1L) {
            thread {
                db.taskDao().delete(taskId)
            }
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent);
            finish()
        }
    }

    private fun completeTask() {
        thread {
            db.taskDao().completeTask(taskId)
        }
        isActive=false
        detail_isActive_button.text="Completed"
    }

    private fun reopenTask() {
        thread {
            db.taskDao().reopenTask(taskId)
        }
        isActive=true
        detail_isActive_button.text="Active"
    }

}







