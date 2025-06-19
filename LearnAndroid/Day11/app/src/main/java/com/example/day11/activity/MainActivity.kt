package com.example.day11.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.day11.R
import com.example.day11.RVAdapter
import com.example.day11.adapter.VPAdapter
import com.example.day11.bean.Item
import com.example.day11.fragment.HomeFragment
import com.example.day11.fragment.PersonalFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), View.OnClickListener,
    BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    private lateinit var bottom_nav:BottomNavigationView
    private lateinit var vp:ViewPager
    private lateinit var adapter: VPAdapter
    private lateinit var pages:MutableList<Fragment>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initEvent()
    }
    private fun initView() {
        pages = ArrayList()
        pages.add(HomeFragment(this))
        pages.add(PersonalFragment())
        bottom_nav = findViewById(R.id.bottom_nav)
        vp = findViewById(R.id.vp)
        adapter = VPAdapter(pages, supportFragmentManager)
        vp.adapter = adapter
    }
    private fun initEvent() {
        bottom_nav.setOnNavigationItemSelectedListener(this)
        vp.setOnPageChangeListener(this)
    }

    override fun onClick(v: View?) {

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                vp.currentItem = 0
                true
            }

            R.id.personal -> {
                vp.currentItem = 1
                true
            }
            // 添加其他menu项的case
            else -> false
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        bottom_nav.selectedItemId = when (position) {
            0 -> R.id.home
            1 -> R.id.personal
            else -> 0 // 默认返回第一个
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

}
