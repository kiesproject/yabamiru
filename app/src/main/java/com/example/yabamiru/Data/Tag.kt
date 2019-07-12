package com.example.yabamiru.Data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Tag constructor(
    @PrimaryKey(autoGenerate = true)
    var tagId:Long=0,
    var name:String="",
    var color:Int=0
)