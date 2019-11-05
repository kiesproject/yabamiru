package com.example.yabamiru.taskList

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yabamiru.R
import com.example.yabamiru.addTask.AddTaskActivity
import com.example.yabamiru.data.AppDatabase
import com.example.yabamiru.data.Constant
import com.example.yabamiru.data.model.TaskAndTaskTags
import kotlinx.android.synthetic.main.activity_task_list.*


class TaskListActivity : AppCompatActivity() {

    lateinit var db: AppDatabase

    private  var taskAndTaskTagsList: List<TaskAndTaskTags>? = null

    private lateinit var recyclerAdapter: TaskListRecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        db = AppDatabase.getDatabase(this)
        setupRecyclerView()
        setupSearchView()
        onTasksChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tasklist_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.taskList_sortByTitle_asc->{
                recyclerAdapter.setList(taskAndTaskTagsList!!.sortedBy { it.task.title },Constant.ALL_ITEMS)
                taskList_searchView.setQuery("",false)
                taskList_searchView.clearFocus()
                return true
            }
            R.id.taskList_sortByTitle_desc->{
                recyclerAdapter.setList(taskAndTaskTagsList!!.sortedByDescending { it.task.title },Constant.ALL_ITEMS)
                taskList_searchView.setQuery("",false)
                taskList_searchView.clearFocus()
                return true
            }
            R.id.taskList_filter_all->{
                db.taskDao().loadTaskAndTaskTags().observe(this, Observer {
                    taskAndTaskTagsList=it
                    recyclerAdapter.setList(taskAndTaskTagsList!!,Constant.ALL_ITEMS)
                })
                taskList_searchView.setQuery("",false)
                taskList_searchView.clearFocus()
                setTitle("すべてのタスク")
                return true
            }
            R.id.taskList_filter_active->{
                db.taskDao().loadTaskAndTaskTagsByIsActive(true).observe(this, Observer {
                    taskAndTaskTagsList=it
                    recyclerAdapter.setList(taskAndTaskTagsList!!,Constant.ACTIVE_ITEMS)
                })
                taskList_searchView.setQuery("",false)
                taskList_searchView.clearFocus()
                setTitle(getString(R.string.app_name))
                return true
            }
            R.id.taskList_filter_completed->{
                db.taskDao().loadTaskAndTaskTagsByIsActive(false).observe(this, Observer {
                    taskAndTaskTagsList=it
                    recyclerAdapter.setList(taskAndTaskTagsList!!,Constant.COMPLETED_ITEMS)
                })
                taskList_searchView.setQuery("",false)
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
                if (text != null && taskAndTaskTagsList!=null){
                    if(text.isBlank()||text.isEmpty()){
                        recyclerAdapter.setList(taskAndTaskTagsList!!.filter { it.task.title.contains(text) },Constant.ALL_ITEMS)
                        return true
                    }
                    recyclerAdapter.setList(taskAndTaskTagsList!!.filter { it.task.title.contains(text) },Constant.ALL_ITEMS)
                }

                return true
            }
        })
    }

    private fun onTasksChanged() {
        db.taskDao().loadTaskAndTaskTagsByIsActive(true).observe(this, Observer {
            taskAndTaskTagsList = it
            recyclerAdapter.setList(it,Constant.ACTIVE_ITEMS)
        })
    }

    fun onFabClicked(view: View) {
        val intent = Intent(this,AddTaskActivity::class.java)
        startActivityForResult(intent,Constant.ADD_TASK_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==Constant.ADD_TASK_REQUEST_CODE && resultCode== Activity.RESULT_OK){
            db.taskDao().loadTaskAndTaskTagsByIsActive(true).observe(this, Observer {
                this.taskAndTaskTagsList = it
                recyclerAdapter.setList(it,Constant.ACTIVE_ITEMS)
            })
        }else if(requestCode ==Constant.DETAIL_TASK_REQUEST_CODE && resultCode== Activity.RESULT_OK ){
            db.taskDao().loadTaskAndTaskTagsByIsActive(true).observe(this, Observer {
                this.taskAndTaskTagsList = it
                recyclerAdapter.setList(it,Constant.ACTIVE_ITEMS)
            })
        }
    }
}