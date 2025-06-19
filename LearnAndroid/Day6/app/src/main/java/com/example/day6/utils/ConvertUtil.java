package com.example.day6.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.day6.beans.Item;
import com.example.day6.beans.NewsItem;

import java.util.List;

/**
 * 转换工具类，用于将NewsItem数据转为Item数据，并使用Glide加载网络图片到item数组中
 */
public class ConvertUtil {
    private ConvertUtil() {
    }

    private static ConvertUtil instance;

    public static synchronized ConvertUtil getInstance() {
        if (instance == null) {
            instance = new ConvertUtil();
        }
        return instance;
    }

    /**
     * 将NewsItem数据转为Item数据
     * 使用Glide加载网络图片到item数组里面
     *
     * @param mContext     上下文
     * @param mItems       Item列表
     * @param mNewsItems   新闻数据
     */
    public void convertData(Context mContext, List<Item> mItems, NewsItem mNewsItems, boolean isTop) {
        for (int i = 0; i < mNewsItems.getStories().size(); i++) {
            String imageUrl = mNewsItems.getStories().get(i).getImages().get(0);
            if (isTop) {
                mItems.add(0, new Item(Item.TYPE_IMAGE, Uri.parse(imageUrl)));
                mItems.add(0, new Item(Item.TYPE_TEXT, mNewsItems.getStories().get(i).getTitle()));
            } else {
                mItems.add(new Item(Item.TYPE_IMAGE, Uri.parse(imageUrl)));
                mItems.add(new Item(Item.TYPE_TEXT, mNewsItems.getStories().get(i).getTitle()));
            }
//            int finalI = i;
            // 这里加载出一堆bug，不知道问题在哪里，头痛
//            Glide.with(mContext)
//                    .load(Uri.parse(imageUrl))
//                    .into(new CustomTarget<Drawable>() {
//                        @Override
//                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                            synchronized (mItems) {
//                                // 创建 Item 对象并放入 mItems 列表中
//                                Item item = new Item(Item.TYPE_IMAGE, resource);
//                                mItems.add(finalI * 2, item); // 图片类型的 Item
//                                mItems.add(finalI * 2 + 1, new Item(Item.TYPE_TEXT, mNewsItems.getStories().get(finalI).getTitle()));
//                            }
//                        }
//
//                        @Override
//                        public void onLoadCleared(@Nullable Drawable placeholder) {
//                            // Placeholder 清除时的处理
//                        }
//                    });
        }
    }
}
