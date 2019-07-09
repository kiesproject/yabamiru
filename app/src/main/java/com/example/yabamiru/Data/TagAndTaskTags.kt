package com.example.yabamiru.Data

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

class TagAndTaskTags {
    @Embedded
    lateinit var tag:Tag

    @Relation(parentColumn = "tagId",entityColumn = "tagId")
    lateinit var taskTags:List<TaskTags>
}