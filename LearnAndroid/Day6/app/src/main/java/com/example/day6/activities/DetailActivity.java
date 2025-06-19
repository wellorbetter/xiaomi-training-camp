package com.example.day6.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.day6.R;
import com.example.day6.events.UpdateLikeEvent;

import org.greenrobot.eventbus.EventBus;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean isImage, isLike;
    private int position;
    TextView mTextView;
    ImageView mImageView, mIsLikeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        mIsLikeImageView.setOnClickListener(this);
    }

    private void initView() {
        mTextView = findViewById(R.id.tv);
        mImageView = findViewById(R.id.iv);
        mIsLikeImageView = findViewById(R.id.iv_like);
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        isImage = bundle.getBoolean("isImage");
        isLike = bundle.getBoolean("isLike");
        position = bundle.getInt("position");

        if (isImage) {
            Glide.with(this)
                    .load(Uri.parse(bundle.getString("image")))
                    .into(mImageView);
            mTextView.setVisibility(View.INVISIBLE);
            mImageView.setVisibility(View.VISIBLE);
        } else {
            mTextView.setText(bundle.getString("text"));
            mTextView.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.INVISIBLE);
        }
        mIsLikeImageView.setSelected(isLike);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_like) {
            isLike = !isLike;
            mIsLikeImageView.setSelected(isLike);
            EventBus.getDefault().post(new UpdateLikeEvent(position, isLike));
        }
    }
}
