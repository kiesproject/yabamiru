package com.example.yabamiru.data.model

import android.graphics.Color
import androidx.room.*

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