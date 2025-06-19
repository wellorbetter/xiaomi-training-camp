package com.example.day6.beans;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author wellorbetter
 */

public class Item implements MultiItemEntity {
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_TEXT = 2;

    private final int mItemType;
    private String mText;
    private Uri mImage;
    private boolean isLike;

    public Item(int itemType, String text) {
        this.mItemType = itemType;
        this.mText = text;
    }

    public Item(int itemType, Uri image) {
        this.mItemType = itemType;
        this.mImage = image;
        this.isLike = false;
    }

    @Override
    public int getItemType() {
        return mItemType;
    }

    public String getText() {
        return mText;
    }

    public Uri getImage() {
        return mImage;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }
}

