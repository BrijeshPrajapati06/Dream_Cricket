package com.diyantech.dreamcricket.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabViewPagerAdapter(private val myContext: Context, fm: FragmentManager?, var totalTabs: Int, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm!!,lifecycle) {
    private val fragmentList : MutableList<Fragment> =ArrayList()
    private val titleList : MutableList<String> =ArrayList()


    override fun getItemCount(): Int {
        return fragmentList.size

    }

    override fun createFragment(position: Int): Fragment {
        return  fragmentList[position]
    }

    fun addFragment(fragment: Fragment, title: String){
        fragmentList.add(fragment)
        titleList.add(title)
    }

    fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }
}