package com.example.yabamiru

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class TabAdapter(fm:FragmentManager, private val context: Context): FragmentPagerAdapter(fm){

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> { return MainlistFragment() }
            else ->  { return Tab02Fragment() }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0 -> { return "tab_01" }
            else ->  { return "tab_02" }
        }
    }

    override fun getCount(): Int {
        return 2
    }
}