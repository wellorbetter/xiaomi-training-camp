package com.example.day6.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.day6.R;
import com.example.day6.activities.DetailActivity;
import com.example.day6.beans.Item;
import com.example.day6.events.UpdateLikeEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class HomePageAdapter extends BaseMultiItemQuickAdapter<Item, BaseViewHolder> implements LoadMoreModule {

    public HomePageAdapter(@Nullable List<Item> data) {
        super(data);
        Log.d("LISTDATA", String.valueOf(data));
        addItemType(Item.TYPE_IMAGE, R.layout.item_imageview_home);
        addItemType(Item.TYPE_TEXT, R.layout.item_textview_home);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, Item item) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(getContext(), DetailActivity.class);

        ImageView mImageView = baseViewHolder.itemView.findViewById(R.id.iv_like);
        mImageView.setSelected(item.isLike());
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setLike(!item.isLike());
                mImageView.setSelected(item.isLike());
            }
        });

        switch (baseViewHolder.getItemViewType()) {
            case Item.TYPE_IMAGE:
                ImageView imageView = baseViewHolder.itemView.findViewById(R.id.itemImageView);
                Glide.with(getContext())
                        .load(item.getImage())
                        .into(imageView);
                baseViewHolder.itemView.findViewById(R.id.itemImageView).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bundle.putBoolean("isImage", true);
                        bundle.putBoolean("isLike", item.isLike());
                        bundle.putString("image", String.valueOf(item.getImage()));
                        bundle.putInt("position", baseViewHolder.getAdapterPosition());
                        intent.putExtras(bundle);
                        getContext().startActivity(intent);
                    }
                });
                break;
            case Item.TYPE_TEXT:
                baseViewHolder.setText(R.id.itemTextView, item.getText());
                baseViewHolder.itemView.findViewById(R.id.itemTextView).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bundle.putBoolean("isImage", false);
                        bundle.putBoolean("isLike", item.isLike());
                        bundle.putString("text", item.getText());
                        bundle.putInt("position", baseViewHolder.getAdapterPosition());
                        intent.putExtras(bundle);
                        getContext().startActivity(intent);
                    }
                });
                break;
            default:
                Toast.makeText(getContext(), "数据类型出错了", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateLikeEvent(UpdateLikeEvent event) {
        Item item = getData().get(event.getPosition());
        item.setLike(event.isLike());
        notifyItemChanged(event.getPosition());
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        EventBus.getDefault().unregister(this);
    }
}
