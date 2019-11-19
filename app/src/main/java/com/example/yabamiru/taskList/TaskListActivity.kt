package com.example.yabamiru.taskList

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yabamiru.R
import com.example.yabamiru.addTask.AddTaskActivity
import com.example.yabamiru.data.AppDatabase
import com.example.yabamiru.data.Constant
import com.example.yabamiru.data.ConstantColor
import com.example.yabamiru.data.model.TaskAndTaskTags
import kotlinx.android.synthetic.main.activity_task_list.*
import java.util.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.text.Html


class TaskListActivity : AppCompatActivity() {

    lateinit var db: AppDatabase

    private var taskAndTaskTagsList: List<TaskAndTaskTags>? = null

    private lateinit var recyclerAdapter: TaskListRecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        setColor()

        db = AppDatabase.getDatabase(this)
        setupRecyclerView()
        setupSearchView()
        onTasksChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tasklist_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.taskList_sortByTitle_asc -> {
                recyclerAdapter.setList(
                    taskAndTaskTagsList!!.sortedBy { it.task.title },
                    Constant.ALL_ITEMS
                )
                taskList_searchView.setQuery("", false)
                taskList_searchView.clearFocus()
                return true
            }
            R.id.taskList_sortByTitle_desc -> {
                recyclerAdapter.setList(
                    taskAndTaskTagsList!!.sortedByDescending { it.task.title },
                    Constant.ALL_ITEMS
                )
                taskList_searchView.setQuery("", false)
                taskList_searchView.clearFocus()
                return true
            }
            R.id.taskList_filter_all -> {
                db.taskDao().loadTaskAndTaskTags().observe(this, Observer {
                    taskAndTaskTagsList = it
                    recyclerAdapter.setList(taskAndTaskTagsList!!, Constant.ALL_ITEMS)
                })
                taskList_searchView.setQuery("", false)
                taskList_searchView.clearFocus()
                setTitle("すべてのタスク")
                return true
            }
            R.id.taskList_filter_active -> {
                db.taskDao().loadTaskAndTaskTagsByIsActive(true).observe(this, Observer {
                    taskAndTaskTagsList = it
                    recyclerAdapter.setList(taskAndTaskTagsList!!, Constant.ACTIVE_ITEMS)
                })
                taskList_searchView.setQuery("", false)
                taskList_searchView.clearFocus()
                setTitle(getString(R.string.app_name))
                return true
            }
            R.id.taskList_filter_completed -> {
                db.taskDao().loadTaskAndTaskTagsByIsActive(false).observe(this, Observer {
                    taskAndTaskTagsList = it
                    recyclerAdapter.setList(taskAndTaskTagsList!!, Constant.COMPLETED_ITEMS)
                })
                taskList_searchView.setQuery("", false)
                taskList_searchView.clearFocus()
                setTitle("完了したタスク")
                return true
            }
        }
        return false
    }

    private fun setupRecyclerView() {
        recyclerAdapter = TaskListRecyclerViewAdapter(this)
        taskList_recyclerView.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(this@TaskListActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this@TaskListActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun setupSearchView() {
        taskList_searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                if (text != null && taskAndTaskTagsList != null) {
                    if (text.isBlank() || text.isEmpty()) {
                        recyclerAdapter.setList(taskAndTaskTagsList!!.filter {
                            it.task.title.contains(
                                text
                            )
                        }, Constant.ALL_ITEMS)
                        return true
                    }
                    recyclerAdapter.setList(taskAndTaskTagsList!!.filter {
                        it.task.title.contains(
                            text
                        )
                    }, Constant.ALL_ITEMS)
                }

                return true
            }
        })
    }

    private fun onTasksChanged() {
        db.taskDao().loadTaskAndTaskTagsByIsActive(true).observe(this, Observer {
            taskAndTaskTagsList = it
            recyclerAdapter.setList(it, Constant.ACTIVE_ITEMS)
        })
    }

    fun onFabClicked(view: View) {
        val intent = Intent(this, AddTaskActivity::class.java)
        startActivityForResult(intent, Constant.ADD_TASK_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.ADD_TASK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            db.taskDao().loadTaskAndTaskTagsByIsActive(true).observe(this, Observer {
                this.taskAndTaskTagsList = it
                recyclerAdapter.setList(it, Constant.ACTIVE_ITEMS)
            })
        } else if (requestCode == Constant.DETAIL_TASK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            db.taskDao().loadTaskAndTaskTagsByIsActive(true).observe(this, Observer {
                this.taskAndTaskTagsList = it
                recyclerAdapter.setList(it, Constant.ACTIVE_ITEMS)
            })
        } else {
            recyclerAdapter.setList(this.taskAndTaskTagsList!!, Constant.ACTIVE_ITEMS)
        }
    }

    fun setColor() {
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
        val titleHtml = "<font color=\"$htmlColor\">${getString(R.string.app_name)}</font>"
        supportActionBar?.setTitle(Html.fromHtml(titleHtml))




        taskList_constraintLayout.setBackgroundResource(ConstantColor.nowColorSet.colorWindowBackground)
        taskList_fab.backgroundTintList =
            getColorStateList(ConstantColor.nowColorSet.colorFabBackground)
        val searchView = findViewById(com.example.yabamiru.R.id.taskList_searchView) as SearchView
        searchView.setBackgroundColor(getColor(ConstantColor.nowColorSet.colorCardBackground))
        val id = searchView.context
            .resources
            .getIdentifier("android:id/search_src_text", null, null)
        val textView = searchView.findViewById<View>(id) as TextView
        textView.setTextColor(getColor(ConstantColor.nowColorSet.colorText))
        textView.setHintTextColor(getColor(ConstantColor.nowColorSet.colorText))

    }
}