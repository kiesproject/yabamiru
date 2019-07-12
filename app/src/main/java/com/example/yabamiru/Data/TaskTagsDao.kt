package com.example.yabamiru.Data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface TaskTagsDao{

    @Query("SELECT * FROM TaskTags")
    fun getAll():LiveData<List<TaskTags>>

    @Query("SELECT * FROM TaskTags WHERE taskId=(:taskId)")
    fun getByTaskId(taskId:Long):LiveData<TaskTags>

    @Insert
    fun insert(vararg taskTags: TaskTags):Long

    @Delete
    fun delete(taskTags: TaskTags)

    @Update
    fun update(taskTags: TaskTags)
}