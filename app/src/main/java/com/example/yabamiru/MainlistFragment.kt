package com.example.yabamiru

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import com.example.yabamiru.Data.AppDatabase
import com.example.yabamiru.Data.TaskAndTaskTags
import kotlinx.android.synthetic.main.fragment_mainlist.*

class MainlistFragment : Fragment() {

    lateinit var db: AppDatabase

    lateinit var taskAndTaskTagsList: List<TaskAndTaskTags>

    private val recyclerAdapter by lazy { RecyclerAdapter(view!!.context) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = AppDatabase.getDatabase(this.requireContext())

        main_recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        main_recyclerView.adapter = recyclerAdapter


        db.taskDao().loadTaskAndTaskTags().observe(this, Observer { taskAndTaskTagsList ->
            if (taskAndTaskTagsList != null) {
                this.taskAndTaskTagsList = taskAndTaskTagsList
                recyclerAdapter.setList(taskAndTaskTagsList)
            }
        })

        ArrayAdapter.createFromResource(
            context,
            R.array.fragment_mainlist_spinner_values,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            main_spinner_button.adapter = adapter
        }

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
                return
            }
        }
        main_layout_searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null && newText.isNotEmpty()) {
                    recyclerAdapter.setList(taskAndTaskTagsList.filter {
                        it.task.title.contains(
                            newText.toLowerCase()
                        )
                    })
                } else {
                    recyclerAdapter.setList(taskAndTaskTagsList)
                }

                return false
            }
        })
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
                recyclerAdapter.setList(taskAndTaskTagsList.sortedBy { it.task.weight })
            }
            "降順" -> {

                recyclerAdapter.setList(taskAndTaskTagsList.sortedByDescending { it.task.weight })
            }

        }
    }
}