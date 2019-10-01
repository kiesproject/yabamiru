package com.example.yabamiru.Data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.graphics.Color

@Entity(
    primaryKeys = ["tagName", "taskId"],
    foreignKeys = [ForeignKey(
        entity = Task::class,
        parentColumns = ["taskId"],
        childColumns = ["taskId"],
        onDelete = ForeignKey.CASCADE
    )]
)

data class TaskTags constructor(
    var tagName: String ="",
    var taskId: Long = 0,
    var color:Int= Color.GREEN
)