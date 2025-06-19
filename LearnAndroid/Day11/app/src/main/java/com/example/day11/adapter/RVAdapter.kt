package com.example.day11

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.day11.activity.DetailActivity
import com.example.day11.bean.Item
import com.example.day11.bean.UpdateLikeEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * RVAdapter 适配器类，用于处理 RecyclerView 的数据绑定
 * @param layoutResId 布局资源 ID
 * @param data 数据列表
 */
class RVAdapter(private var mContext: Context, data: MutableList<Item>? = null) :
    BaseMultiItemQuickAdapter<Item, BaseViewHolder>(data), LoadMoreModule {
    init {
        addItemType(Item.POEM_TEXT, R.layout.rv_poem_text_item)
        addItemType(Item.PERSON_CARD, R.layout.rv_person_card_item)
        EventBus.getDefault().register(this)
    }

    override fun convert(holder: BaseViewHolder, item: Item) {
        var isLike:ImageButton = holder.itemView.findViewById(R.id.ib_like)
        var card:CardView = holder.itemView.findViewById(R.id.card)
        isLike.setOnClickListener {
            it.isSelected = !it.isSelected
        }
        isLike.isSelected = item.like
        card.setOnClickListener {
            var intent = Intent(mContext, DetailActivity::class.java)
            var bundle = Bundle()
            bundle.putBoolean("isPoem", item.itemType == Item.POEM_TEXT)
            bundle.putBoolean("isLike", isLike.isSelected)
            bundle.putString("poem", item.poemContent)
            bundle.putInt("position", getItemPosition(item))
            intent.putExtras(bundle)
            mContext.startActivity(intent)
        }
        when (item.itemType) {
            Item.PERSON_CARD -> {
                // 将数据绑定到视图
                holder.setImageDrawable(R.id.iv_avatar, item.img)
                holder.setText(R.id.tv_name, item.name)
                holder.setText(R.id.tv_id, item.id.toString())
            }
            else -> {
                holder.setText(R.id.tv_poet_name, item.poetName)
                holder.setText(R.id.tv_poem_title, item.poemName)
                holder.setText(R.id.tv_poem_content, item.poemContent)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUpdateLikeEvent(event: UpdateLikeEvent) {
        val item = data[event.position]
        item.like = event.isLike
        notifyItemChanged(event.position)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        EventBus.getDefault().unregister(this)
    }

}
