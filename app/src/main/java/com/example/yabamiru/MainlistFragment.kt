package com.example.yabamiru

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.yabamiru.Data.AppDatabase
import com.example.yabamiru.Data.TaskAndTaskTags
import kotlinx.android.synthetic.main.fragment_mainlist.*

class MainlistFragment : Fragment() {

    lateinit var db: AppDatabase

    lateinit var taskAndTaskTagsList: List<TaskAndTaskTags>

    private val recycleradapter by lazy { RecyclerAdapter(view!!.context) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = AppDatabase.getDatabase(this.requireContext())

        main_recyclerView.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        main_recyclerView.adapter = recycleradapter


        db.taskDao().loadTaskAndTaskTags().observe(this, Observer { taskAndTaskTagsList ->
            if (taskAndTaskTagsList != null) {
                this.taskAndTaskTagsList = taskAndTaskTagsList
                recycleradapter.setList(taskAndTaskTagsList)
            }
        })

        val spinnerItems = arrayOf(
            "昇順",
            "降順"
        )

        val spinneradapter = ArrayAdapter(
            getActivity()!!.getApplicationContext(),
            android.R.layout.simple_spinner_item,
            spinnerItems
        )
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        main_spinner_button.adapter = spinneradapter


        main_spinner_button.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?, position: Int, id: Long
            ) {
                val spinnerParent = parent as Spinner
                val item = spinnerParent.selectedItem as String

                sortCard(item)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mainlist, container, false)
    }

    private fun sortCard(status: String) {
        when (status) {
            "昇順" -> {
                recycleradapter.setList(taskAndTaskTagsList.sortedBy { it.task.weight })
            }
            "降順" -> {

                recycleradapter.setList(taskAndTaskTagsList.sortedByDescending { it.task.weight })
            }

        }
    }
}