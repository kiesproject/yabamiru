package com.example.yabamiru.Data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey

@Entity(
    primaryKeys = ["tagId", "taskId"],
    foreignKeys = [ForeignKey(
        entity = Task::class,
        parentColumns = ["taskId"],
        childColumns = ["taskId"],
        onDelete = ForeignKey.CASCADE
    ),
        ForeignKey(
            entity = Tag::class,
            parentColumns = ["tagId"],
            childColumns = ["tagId"],
            onDelete = ForeignKey.RESTRICT
        )]
)

data class TaskTags constructor(
    var tagId: Long = 0,
    var taskId: Long = 0
)