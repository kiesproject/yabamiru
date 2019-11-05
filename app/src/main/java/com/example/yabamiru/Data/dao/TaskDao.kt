package com.example.yabamiru.data.dao


import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.yabamiru.data.model.Task
import com.example.yabamiru.data.model.TaskAndTaskTags


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

    @Transaction
    @Query("SELECT * FROM Task WHERE isActive=(:isActive)")
    fun loadTaskAndTaskTagsByIsActive(isActive:Boolean):LiveData<List<TaskAndTaskTags>>

    @Update
    fun update(task: Task)

    @Insert
    fun insert(vararg task: Task):List<Long>

    @Query("DELETE FROM Task WHERE taskId=(:taskId)")
    fun delete(taskId: Long)

    @Query("DELETE FROM task")
    fun deleteAll()

    @Query("UPDATE task SET isActive='1' WHERE taskId =(:taskId)")
    fun reopenTask(taskId:Long)

    @Query("UPDATE task SET isActive='0' WHERE taskId =(:taskId)")
    fun completeTask(taskId:Long)
}