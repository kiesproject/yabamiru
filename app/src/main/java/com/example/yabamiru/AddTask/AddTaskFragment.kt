package com.example.yabamiru.AddTask

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import com.example.yabamiru.R

class AddTaskFragment : Fragment() {

    lateinit var titleEditText: EditText
    lateinit var weightSeekBar: SeekBar
    lateinit var dateEditText:EditText
    lateinit var timeEditText:EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.app_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setViews()
        val addTaskButton = view.findViewById<Button>(R.id.add_task_button)


    }

    private fun setViews() {

        titleEditText = view!!.findViewById(R.id.edit_title)
        weightSeekBar = view!!.findViewById(R.id.edit_seekBar)
        dateEditText=view!!.findViewById(R.id.edit_date)
        timeEditText=view!!.findViewById(R.id.edit_time)
    }

    private fun onClickListener(viewId: Int): View.OnClickListener {
        when (viewId) {
            R.id.add_task_button -> {
                return View.OnClickListener { }
            }
            R.id.button_tag -> {
                return View.OnClickListener {

                }
            }
        }
        return View.OnClickListener { }
    }

    private fun validation(): Boolean {
        var flg = true
        var str = "タスクを追加できませんでした。"
        if (titleEditText.text.isEmpty()) {
            flg = false
            str += "\nタイトルを入力してください。"
        }
        if (dateEditText.text.isEmpty()||timeEditText.text.isEmpty()){
            flg=false
            str+="\n期限を正しく入力してください。"
        }

            return flg
    }
}