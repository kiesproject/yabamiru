package com.example.yabamiru.data.model

import androidx.room.*
import java.util.*

@Entity
data class Task constructor(
    @PrimaryKey(autoGenerate = true)
    var taskId:Long=0,
    var title:String="",
    var weight:Int,
    val timeStump :Long = Date().time,
    var deadLine:Long,
    var memo:String,
    var isActive:Boolean
)