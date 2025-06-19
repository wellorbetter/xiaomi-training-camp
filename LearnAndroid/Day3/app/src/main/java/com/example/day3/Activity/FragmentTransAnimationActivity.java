package com.example.day3.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.day3.Fragment.Animation.AnimTransBeginFragment;
import com.example.day3.R;
// 共享元素动画 过渡动画
public class FragmentTransAnimationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 一个fragment一个button，button开启过渡动画
        setContentView(R.layout.activity_fragment_animation);
        // 从AnimTransBeginFragment开始的，先加进去
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(AnimTransBeginFragment.class.getName());
        // 防止重复加
        if (fragment == null) {
            fragment = new AnimTransBeginFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl, fragment, AnimTransBeginFragment.class.getName())
                    .commit();
        }

    }
}