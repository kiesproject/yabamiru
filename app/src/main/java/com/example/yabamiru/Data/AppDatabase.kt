package com.example.yabamiru.Data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = arrayOf(
    Task::class,
    TaskTags::class
),version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao():TaskDao
    abstract fun taskTagsDao():TaskTagsDao
}