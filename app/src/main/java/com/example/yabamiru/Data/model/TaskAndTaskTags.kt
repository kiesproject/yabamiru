package com.example.yabamiru.data.model

import androidx.room.*

class TaskAndTaskTags {
    @Embedded
    lateinit var task: Task

    @Relation(parentColumn = "taskId",entityColumn = "taskId")
    lateinit var taskTags:List<TaskTags>
}