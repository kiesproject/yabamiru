package com.example.yabamiru.Data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface TaskTagsDao{

    @Query("SELECT * FROM TaskTags")
    fun getAll():LiveData<List<TaskTags>>

    @Insert
    fun insert(vararg taskTags: TaskTags):List<Long>

    @Delete
    fun delete(taskTags: TaskTags)

    @Update
    fun update(taskTags: TaskTags)

    @Query("DELETE FROM tasktags")
    fun deleteAll()
}