package com.example.day9.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.day9.R;
import com.example.day9.activities.DetailActivity;
import com.example.day9.activities.MainActivity;
import com.example.day9.beans.SearchResponseData;

import java.io.Serializable;
import java.util.List;

/**
 * @author wellorbetter
 */
public class GameInfoPageAdapter extends BaseQuickAdapter<SearchResponseData.GameInfoPage.GameInfo, BaseViewHolder> implements LoadMoreModule {


    public GameInfoPageAdapter(int layoutResId, @Nullable List<SearchResponseData.GameInfoPage.GameInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, SearchResponseData.GameInfoPage.GameInfo gameInfo) {
        baseViewHolder.findView(R.id.cv_game_info).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // 点击时，隐藏历史记录列表，并取消SearchView的焦点
                    ((MainActivity)getContext()).findViewById(R.id.lv_search_game_info).setVisibility(View.GONE);
                    ((MainActivity)getContext()).findViewById(R.id.sv_game_info).clearFocus();
                }
                return false;
            }
        });
        baseViewHolder.findView(R.id.cv_game_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("gameinfo", (Parcelable) gameInfo);
                intent.putExtras(bundle);
                getContext().startActivity(intent);
            }
        });
        ImageView ivGameIcon = baseViewHolder.findView(R.id.iv_game_icon);
        Glide.with(getContext())
                .load(gameInfo.getIcon())
                .into(ivGameIcon);
        ((TextView) baseViewHolder.findView(R.id.tv_game_name)).setText(gameInfo.getGameName());
        ((TextView) baseViewHolder.findView(R.id.tv_game_brief)).setText(gameInfo.getBrief());
    }
}
