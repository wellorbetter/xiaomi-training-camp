package com.example.day11.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import com.example.day11.R
import com.example.day11.bean.UpdateLikeEvent
import org.greenrobot.eventbus.EventBus

class DetailActivity : AppCompatActivity(), View.OnClickListener {
    private var position: Int? = 0
    private lateinit var ib_like: ImageView
    private lateinit var card_person: CardView
    private lateinit var card_poem: CardView
    private var isPoem: Boolean = false
    private var isLike: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        initData()
        initView()
        initEvent()
    }

    private fun initEvent() {
        ib_like.setOnClickListener(this)
    }

    private fun initData() {
        isPoem = intent.extras?.getBoolean("isPoem") == true
        isLike = intent.extras?.getBoolean("isLike") == true
        position = intent.extras?.getInt("position")
    }

    fun initView() {
        ib_like = findViewById(R.id.ib_like)
        card_person = findViewById(R.id.card_person)
        card_poem = findViewById(R.id.card_poem)
        when (isPoem) {
            true -> {
                card_person.visibility = View.INVISIBLE
            }
            else -> {
                card_poem.visibility = View.INVISIBLE
            }
        }
        Log.d("visible", card_person.isVisible.toString())
        ib_like.isSelected = isLike
    }

    override fun onClick(v: View?) {
        val id = v?.id ?:0
        when (id) {
            R.id.ib_like -> {
                ib_like.isSelected = !ib_like.isSelected
                EventBus.getDefault().post(position?.let { UpdateLikeEvent(it, ib_like.isSelected) })
            }
        }
    }
}