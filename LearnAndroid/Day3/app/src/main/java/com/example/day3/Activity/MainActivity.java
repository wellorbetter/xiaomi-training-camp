package com.example.day3.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.day3.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button bt_nav, bt_vp, bt_anim, bt_anim_replace;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();
    }

    private void initView() {
        bt_nav = findViewById(R.id.bt_nav);
        bt_vp = findViewById(R.id.bt_vp);
        bt_anim_replace = findViewById(R.id.bt_anim_replace);
        bt_anim = findViewById(R.id.bt_anim);
        fragmentManager = getSupportFragmentManager();
    }

    private void initEvent() {
        bt_anim_replace.setOnClickListener(this);
        bt_nav.setOnClickListener(this);
        bt_vp.setOnClickListener(this);
        bt_anim.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        // 跳转到底部Button控制fragment切换
        if (id == R.id.bt_nav) {
            Intent intent = new Intent(this, BottomNavigationActivity.class);
            startActivity(intent);
            // 跳转到ViewPager滑动切换Fragment
        } else if (id == R.id.bt_vp) {
            Intent intent = new Intent(this, ViewPagerActivity.class);
            startActivity(intent);
            // 跳转到展示Fragment动画
        } else if (id == R.id.bt_anim) {
            Intent intent = new Intent(this, FragmentTransAnimationActivity.class);
            startActivity(intent);
        } else if (id == R.id.bt_anim_replace) {
            Intent intent = new Intent(this, FragmentCustomAnimationActivity.class);
            startActivity(intent);
        }
    }
}