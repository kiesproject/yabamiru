package com.example.yabamiru.Data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface TagDao{

    @Query("SELECT * FROM Tag")
    fun getAll():LiveData<List<Tag>>

    @Query("SELECT * FROM Tag WHERE tagId=(:tagId)")
    fun getById(tagId:Long):LiveData<Tag>

    //指定したタグを使っているタスクを選択（タグ検索はこれで一発）
    @Query("SELECT * FROM Tag WHERE tagId=(:tagId)")
    fun loadTagAndTaskTags(tagId: Long):LiveData<List<TagAndTaskTags>>

    @Update
    fun update(tag:Tag)

    @Insert
    fun insert(vararg tag:Tag):List<Long>

    @Delete
    fun delete(tag:Tag)

}