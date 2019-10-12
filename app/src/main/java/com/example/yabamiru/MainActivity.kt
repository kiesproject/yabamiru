package com.example.yabamiru

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.example.yabamiru.Data.AppDatabase


class MainActivity : AppCompatActivity() {

    lateinit var db: AppDatabase
    private val tagNameList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(this, AppDatabase::class.java, "database").build()

        val viewPager = findViewById<ViewPager>(R.id.pager)
        viewPager.adapter = TabAdapter(supportFragmentManager,this)
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager)
    }
}
