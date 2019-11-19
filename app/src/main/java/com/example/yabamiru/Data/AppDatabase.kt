package com.example.yabamiru.data

import androidx.room.*
import android.content.Context
import android.graphics.Color
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.yabamiru.util.DateFormatter
import com.example.yabamiru.data.dao.TaskDao
import com.example.yabamiru.data.dao.TaskTagsDao
import com.example.yabamiru.data.model.Task
import com.example.yabamiru.data.model.TaskTags
import kotlin.concurrent.thread


@Database(
    entities = arrayOf(
        Task::class,
        TaskTags::class
    ), version = 3
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
//                    .addCallback(RoomDatabaseCallback())    //初期データを用いない場合はこの行をコメントアウトする。
                    .addMigrations(MIGRATION1_2,MIGRATION2_3)
                    .build()
                INSTANCE = instance
                return instance
            }
        }

        private val MIGRATION1_2 = object:Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE TaskTags")
                database.execSQL("CREATE TABLE TaskTags(" +
                        "tagName TEXT NOT NULL," +
                        "taskId INTEGER NOT NULL," +
                        "color INTEGER NOT NULL," +
                        "PRIMARY KEY(tagName,taskId)," +
                        "FOREIGN KEY (taskId) REFERENCES Task(taskId) ON DELETE CASCADE)")
            }
        }

        private val MIGRATION2_3 =object :Migration(2,3){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("drop table TaskTags")
                database.execSQL("drop table Task")
                database.execSQL("create table Task(" +
                        "taskId INTEGER not null primary key," +
                        "title TEXT not null," +
                        "weight INTEGER not null," +
                        "timeStump INTEGER not null," +
                        "deadLine INTEGER not null," +
                        "memo TEXT not null," +
                        "isActive INTEGER not null)")
                database.execSQL("CREATE TABLE TaskTags(" +
                        "tagName TEXT NOT NULL," +
                        "taskId INTEGER NOT NULL," +
                        "color INTEGER NOT NULL," +
                        "PRIMARY KEY(tagName,taskId)," +
                        "FOREIGN KEY (taskId) REFERENCES Task(taskId) ON DELETE CASCADE)")
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
                deadLine = DateFormatter.strToTime("2019/10/11  12:23"),
                isActive = true,
                weight = 10,
                memo = "5-101"
            )
            val taskId1 = taskDao.insert(task1)[0]
            //タスク①のタグリスト
            val tag1 = arrayOf(
                TaskTags(
                    taskId = taskId1,
                    tagName = "授業",
                    color = Color.RED
                ),
                TaskTags(
                    taskId = taskId1,
                    tagName = "必修",
                    color = Color.GREEN
                )
            )
            taskTagsDao.insert(*tag1)

            //タスク②の情報
            val task2 = Task(
                title = "パターン認識の課題",
                deadLine = DateFormatter.strToTime("2019/10/11  12:23"),
                isActive = true,
                weight = 5,
                memo = "丸と四角と星と三角の写真"
            )
            val taskId2 = taskDao.insert(task2)[0]
            //タスク①のタグリスト
            val tag2 = arrayOf(
                TaskTags(
                    taskId = taskId2,
                    tagName = "パターン認識",
                    color = Color.RED
                ),
                TaskTags(
                    taskId = taskId2,
                    tagName = "課題",
                    color = Color.YELLOW
                )
            )
            taskTagsDao.insert(*tag2)

            //データを追加する際には上の例に従って追加する。
        }
    }
}