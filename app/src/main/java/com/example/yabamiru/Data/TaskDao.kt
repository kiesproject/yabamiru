package com.example.yabamiru.Data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface TaskDao{

    @Query("SELECT * FROM Task")
    fun getAll(): LiveData<List<Task>>

    @Query("SELECT * FROM Task WHERE taskId=(:taskId)")
    fun getById(taskId:Long): LiveData<Task>

    //タスクとそれに関連付けられたタグIdを取得
    @Transaction
    @Query("SELECT * FROM Task")
    fun loadTaskAndTaskTags():LiveData<List<TaskAndTaskTags>>

    @Transaction
    @Query("SELECT * FROM Task WHERE taskId=(:taskId)")
    fun loadTaskAndTaskTagsByTaskId(taskId:Long):LiveData<TaskAndTaskTags>

    @Update
    fun update(task:Task)

    @Insert
    fun insert(vararg task:Task):List<Long>

    @Delete
    fun delete(task: Task)
}