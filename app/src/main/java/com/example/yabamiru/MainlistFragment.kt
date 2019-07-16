package com.example.yabamiru

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_mainlist.*

class MainlistFragment : AppCompatActivity(), RecyclerAdapter.RecyclerViewHolder.ItemClickListener  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_mainlist)

        val titles = resources.getStringArray(R.array.titles).toMutableList()
        val deadlines = resources.getStringArray(R.array.deadlines).toMutableList()
        val percents = resources.getStringArray(R.array.percents).toMutableList()

        val tags1 = resources.getStringArray(R.array.tags1).toMutableList()
        val adapter1 = TagRecyclerAdapter(tags1)
        val tags2 = resources.getStringArray(R.array.tags2).toMutableList()
        val adapter2 = TagRecyclerAdapter(tags2)
        val tags3 = resources.getStringArray(R.array.tags3).toMutableList()
        val adapter3 = TagRecyclerAdapter(tags3)
        val tags4 = resources.getStringArray(R.array.tags4).toMutableList()
        val adapter4 = TagRecyclerAdapter(tags4)
        val tags5 = resources.getStringArray(R.array.tags5).toMutableList()
        val adapter5 = TagRecyclerAdapter(tags5)
        val adapterlist = mutableListOf(adapter1, adapter2, adapter3, adapter4, adapter5)

        main_recyclerView.adapter = RecyclerAdapter(this, this, titles, deadlines, percents, adapterlist)
        main_recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun onItemClick(view: View, position: Int) {
        Toast.makeText(applicationContext, "position $position was tapped", Toast.LENGTH_SHORT).show()
    }
}
