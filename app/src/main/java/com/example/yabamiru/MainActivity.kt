package com.example.yabamiru

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), RecyclerViewHolder.ItemClickListener  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val hoges = resources.getStringArray(R.array.hoges).toMutableList()

        main_recyclerView.adapter = RecyclerAdapter(this, this, hoges)
        main_recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    }

    override fun onItemClick(view: View, position: Int) {
        Toast.makeText(applicationContext, "position $position was tapped", Toast.LENGTH_SHORT).show()
    }
}
