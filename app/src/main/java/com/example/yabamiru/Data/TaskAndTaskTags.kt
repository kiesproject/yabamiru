package com.example.yabamiru.Data

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

class TaskAndTaskTags {
    @Embedded
    lateinit var task:Task

    @Relation(parentColumn = "taskId",entityColumn = "taskId")
    lateinit var taskTags:List<TaskTags>
}