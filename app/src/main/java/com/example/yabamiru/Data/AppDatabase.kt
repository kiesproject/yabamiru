package com.example.yabamiru.Data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = arrayOf(
    Tag::class,
    Task::class,
    TaskTags::class
),version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tagDao():TagDao
    abstract fun taskDao():TaskDao
}