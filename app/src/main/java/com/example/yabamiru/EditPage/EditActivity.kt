//package com.example.yabamiru.EditPage
//
//import android.arch.lifecycle.Observer
//import android.arch.persistence.room.Room
//import android.os.Bundle
//import android.support.design.widget.TabLayout
//import android.support.v4.view.ViewPager
//import android.support.v7.app.AppCompatActivity
//import android.support.v7.widget.RecyclerView
//import android.view.View
//import android.widget.Button
//import android.widget.EditText
//import android.widget.SeekBar
//import com.example.yabamiru.Data.AppDatabase
//import com.example.yabamiru.Data.Task
//import com.example.yabamiru.R
//import com.example.yabamiru.TabAdapter
//
//class EditActivity : AppCompatActivity() {
//
//    private val titleEditText : EditText by lazy { findViewById(R.id.edit_title) }
//    private val memoEditText :EditText by lazy{findViewById(R.id.edit_memo)}
//    private val weightSeekBar : SeekBar by lazy{findViewById(R.id.edit_seekBar)}
//    private val submitButton : Button by lazy { findViewById(R.id.button_add)}
//    private val dateEditText :EditText by lazy{findViewById(R.id.edit_date)}
//    private val timeEditText: EditText by lazy{findViewById(R.id.edit_time)}
//    private val tagEditText:EditText by lazy{findViewById(R.id.edit_tag)}
//    private val addTagButton :Button by lazy { findViewById(R.id.button_tag)}
//    private val tagList :RecyclerView by lazy{findViewById(R.id.edit_recyclerView) }
//
//    private val db : AppDatabase by lazy{ Room.databaseBuilder(this, AppDatabase::class.java, "database").build()}
//
//    var task : Task? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.app_edit)
//
//        val taskId = intent.getLongExtra("taskId",0)
//
//
//        if(taskId != 0L){
//            db.taskDao().getById(taskId).observe(this, Observer {
//                task=it
//            })
//        }
//
//        submitButton.setOnClickListener(onSubmitButtonClicked())
//
//    }
//
//    private fun initView(task:Task){
//        titleEditText.setText(task.title)
//        dateEditText.setText(task.deadLine.toString())
//        timeEditText.setText(task.deadLine.toString())
//
//    }
//
//    private fun onSubmitButtonClicked() : View.OnClickListener =View.OnClickListener {
//        if(task != null) {
//            val updated = task!!.also {
//                it.title = titleEditText.text.toString()
//            }
//            db.taskDao().update(updated)
//        }
//    }
//
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
