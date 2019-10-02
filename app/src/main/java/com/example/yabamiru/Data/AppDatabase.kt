package com.example.yabamiru.Data

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.graphics.Color
import com.example.yabamiru.DateFormatter
import kotlin.concurrent.thread

@Database(
    entities = arrayOf(
        Task::class,
        TaskTags::class
    ), version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun taskTagsDao(): TaskTagsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "task_database"
                )
                    .addCallback(RoomDatabaseCallback())    //初期データを用いない場合はこの行をコメントアウトする。
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

    //初期データのinsert
    private class RoomDatabaseCallback(
    ) : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                thread {
                    populateDatabase(database.taskDao(), database.taskTagsDao())
                }
            }
        }

        fun populateDatabase(taskDao: TaskDao, taskTagsDao: TaskTagsDao) {
            taskDao.deleteAll()
            taskTagsDao.deleteAll()

            //タスク①の情報
            val task1 = Task(
                title = "専門ゼミ",
                deadLine = DateFormatter.strToTime("2019/10/1112:23"),
                isActive = true,
                weight = 5,
                memo = "5-101")
            val taskId1 = taskDao.insert(task1)[0]
            //タスク①のタグリスト
            val tag1 = arrayOf(
                TaskTags(
                    taskId = taskId1,
                    tagName = "授業",
                    color = Color.RED),
                TaskTags(
                    taskId = taskId1,
                    tagName = "必修",
                    color = Color.GREEN)
            )
            taskTagsDao.insert(*tag1)

            //タスク②の情報
            val task2 = Task(
                title = "パターン認識の課題",
                deadLine = DateFormatter.strToTime("2019/10/1112:23"),
                isActive = true,
                weight = 5,
                memo = "丸と四角と星と三角の写真")
            val taskId2 = taskDao.insert(task2)[0]
            //タスク①のタグリスト
            val tag2 = arrayOf(
                TaskTags(
                    taskId = taskId2,
                    tagName = "パターン認識",
                    color = Color.RED),
                TaskTags(
                    taskId = taskId2,
                    tagName = "課題",
                    color = Color.YELLOW)
            )
            taskTagsDao.insert(*tag2)

            //データを追加する際には上の例に従って追加する。
        }
    }
}