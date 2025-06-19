package com.example.weibo_wudongsheng.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weibo_wudongsheng.R;
import com.example.weibo_wudongsheng.activity.DetailItemActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wellorbetter
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    // uri
    private List<String> images;
    private Context mContext;
    private String nickName;

    public ImageAdapter(Context mContext, List<String> images, String nickName) {
        this.images = images;
        this.mContext = mContext;
        this.nickName = nickName;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imageUrl = images.get(position);
        Glide.with(holder.itemView.getContext())
                .load(Uri.parse(imageUrl))
                .into(holder.imageView);
        holder.imageView.setOnClickListener(v -> {
            // 点击图片跳转详情页面
            Intent intent = new Intent(mContext, DetailItemActivity.class);
            intent.putStringArrayListExtra("images", (ArrayList<String>) images);
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            bundle.putString("nickname", nickName);
            intent.putExtras(bundle);
            // 还要传nickname
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}