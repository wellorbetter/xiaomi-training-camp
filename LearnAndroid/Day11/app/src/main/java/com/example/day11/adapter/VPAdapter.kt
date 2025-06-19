package com.example.day11.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * @author wellorbetter
 */
class VPAdapter(private var pages:List<Fragment>, fm: FragmentManager): FragmentStatePagerAdapter(fm) {
    override fun getCount() = pages.size

    override fun getItem(position: Int) = pages[position]
}