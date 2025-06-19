package com.example.day11.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.day11.R
import com.example.day11.RVAdapter
import com.example.day11.bean.Item

class HomeFragment() : Fragment() {
    private lateinit var rv: RecyclerView
    private lateinit var mContext: Context
    private lateinit var sw: SwipeRefreshLayout
    private var data = mutableListOf<Item>()
    constructor(mContext: Context):this() {
        this.mContext = mContext
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView(view)
        initEvent()
    }

    private fun initData() {
        data = getData()
    }

    private fun initEvent() {
        if (rv.adapter is RVAdapter) {
            (rv.adapter as RVAdapter).loadMoreModule.isEnableLoadMoreIfNotFullPage = true
            (rv.adapter as RVAdapter).loadMoreModule.setOnLoadMoreListener {
                if (data.size >= 100) {
                    Toast.makeText(mContext, "已经加载完毕了！（数据太多了）", Toast.LENGTH_SHORT).show()
                    (rv.adapter as RVAdapter).loadMoreModule.loadMoreComplete()
                    return@setOnLoadMoreListener
                }
                data.add(generatePoemDate(i = (Math.random() * 3).toInt()))
                rv.adapter?.notifyItemInserted(data.size - 1)
                (rv.adapter as RVAdapter).loadMoreModule.loadMoreComplete()
            }
        }
        sw.setOnRefreshListener {
            data.add(0, generatePoemDate(i = (Math.random() * 3).toInt()))
            rv.adapter?.notifyItemInserted(0)
            sw.isRefreshing = false
        }
    }

    private fun initView(view: View) {
        rv = view.findViewById(R.id.rv)
        sw = view.findViewById(R.id.sw)
        rv.adapter = RVAdapter(mContext, data)
        rv.layoutManager = LinearLayoutManager(mContext)
    }
    // 模拟获取数据的方法
    private fun getData(): MutableList<Item> {
        val data = mutableListOf<Item>()
        for (i in 1..20) {
            data.add(generatePersonalData(i))
            data.add(generatePoemDate(i))
        }
        return data
    }
    private fun generatePersonalData(i: Int) = Item(mContext.getDrawable(R.drawable.img), i.toString(), i, Item.PERSON_CARD)
    private fun generatePoemDate(i:Int): Item  = when (i % 3) {
        0 -> Item(getString(R.string.poem_0_poet_name), getString(R.string.poem_0_poem_name), getString(
            R.string.poem_0_context
        ), Item.POEM_TEXT)
        1 -> Item(getString(R.string.poem_1_poet_name), getString(R.string.poem_1_poem_name), getString(
            R.string.poem_1_context
        ), Item.POEM_TEXT)
        else -> Item(getString(R.string.poem_2_poet_name), getString(R.string.poem_2_poem_name), getString(
            R.string.poem_2_context
        ), Item.POEM_TEXT)
    }
}