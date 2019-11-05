package com.example.yabamiru.data.dao


import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.yabamiru.data.model.TaskTags


@Dao
interface TaskTagsDao{

    @Query("SELECT * FROM TaskTags")
    fun getAll(): LiveData<List<TaskTags>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg taskTags: TaskTags):List<Long>

    @Delete
    fun delete(taskTags: TaskTags)

    @Query("DELETE FROM TaskTags WHERE taskId=(:taskId)")
    fun deleteByTaskId(taskId:Long)

    @Update
    fun update(taskTags: TaskTags)

    @Query("DELETE FROM tasktags")
    fun deleteAll()
}