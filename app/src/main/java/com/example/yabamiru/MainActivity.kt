package com.example.yabamiru

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val contacts = listOf(
            Contact(R.drawable.dokuro_img2, "数学", "課題"),
            Contact(R.drawable.dokuro_img2, "国語", "テスト")
        )

        val arrayAdapter = ContactArrayAdapter(this, contacts)


        main_listView.adapter = arrayAdapter

    }
}
