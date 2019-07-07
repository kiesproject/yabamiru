package com.example.yabamiru

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.ViewParent
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_tab_layout.*

class MainActivity : AppCompatActivity(), RecyclerViewHolder.ItemClickListener  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager = findViewById<ViewPager>(R.id.pager)
        viewPager.adapter=TabAdapter(supportFragmentManager,this)
        val tab_Layout = findViewById<TabLayout>(R.id.tab_layout)
        tab_Layout.setupWithViewPager(viewPager)

        val hoges = resources.getStringArray(R.array.hoges).toMutableList()

        main_recyclerView.adapter = RecyclerAdapter(this, this, hoges)
        main_recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun onItemClick(view: View, position: Int) {
        Toast.makeText(applicationContext, "position $position was tapped", Toast.LENGTH_SHORT).show()
    }
}
