package com.example.yabamiru.util

import java.lang.Exception
import java.text.SimpleDateFormat

object DateFormatter {

    const val DATE ="yyyy/MM/dd"
    const val TIME ="HH:mm"


    private val combinedPattern = SimpleDateFormat(DATE +"  "+ TIME)
    private val datePattern =  SimpleDateFormat(DATE)
    private val timePattern = SimpleDateFormat(TIME)

    fun timeToTimeStr(time:Long)= timePattern.format(time).toString()
    fun timeToDateStr(time:Long) = datePattern.format(time).toString()
    fun strToTime(str:String) = combinedPattern.parse(str).time
    fun isCorrectDate(str:String):Boolean{
        try{
            val str2 = datePattern.format(datePattern.parse(str))
            return str.equals(str2)
        }catch (e:Exception){
            return false
        }
    }

    fun isCorrectTime(str:String):Boolean{
        try{
            val str2 = timePattern.format(timePattern.parse(str))
            return str.equals(str2)
        }catch (e:Exception){
            return false
        }
    }


    fun timeToStrForTaskList(time:Long) = combinedPattern.format(time).toString()


}