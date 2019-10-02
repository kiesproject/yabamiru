package com.example.yabamiru

import java.text.SimpleDateFormat

object DateFormatter {

    const val DATE ="yyyy/MM/dd"
    const val TIME ="hh:mm"

    private val combinedPattern = SimpleDateFormat(DATE+TIME)
    private val datePattern =  SimpleDateFormat(DATE)
    private val timePattern = SimpleDateFormat(TIME)

    fun timeToTimeStr(time:Long)= timePattern.format(time).toString()
    fun timeToDateStr(time:Long) = datePattern.format(time).toString()
    fun strToTime(str:String) = combinedPattern.parse(str).time
}