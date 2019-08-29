package com.example.yabamiru.AddTask

import android.app.Activity
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.TimePicker
import java.text.SimpleDateFormat
import java.util.*

class TimePick :DialogFragment(),TimePickerDialog.OnTimeSetListener{

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        return TimePickerDialog(activity,this,c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true)
    }
    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
        val c = Calendar.getInstance()
        c.set(Calendar.HOUR,hour)
        c.set(Calendar.MINUTE,minute)
        val time = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(c.time)
        targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, Intent().putExtra("selectedTime",time))
    }
}