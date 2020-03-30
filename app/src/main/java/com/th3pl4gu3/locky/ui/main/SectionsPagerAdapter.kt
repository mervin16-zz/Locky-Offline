package com.th3pl4gu3.locky.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var fragmentList = ArrayList<Fragment>()

    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getPageTitle(position: Int): CharSequence? = null

    override fun getCount(): Int = fragmentList.size

    internal fun addFragment(fragment: Fragment) = fragmentList.add(fragment)

}