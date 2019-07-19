package com.example.yabamiru.Data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Task constructor(
    @PrimaryKey(autoGenerate = true)
    var taskId:Long=0,
    var title:String="",
    var weight:Int,
    var deadLine:Long,
    var finishedYabasa :Float,
    var memo:String,
    var isActive:Boolean
)