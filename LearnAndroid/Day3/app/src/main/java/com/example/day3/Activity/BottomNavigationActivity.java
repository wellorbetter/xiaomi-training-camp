package com.example.day3.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.day3.Fragment.PageFragment;
import com.example.day3.R;

// 使用点击事件切换Fragment
public class BottomNavigationActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_shop, tv_home, tv_profile;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        initView();
        initData();
        initEvent();

        // 初始化界面就是Home
        replaceFragment("home界面");
    }


    private void initView() {
        tv_home = findViewById(R.id.tv_home);
        tv_shop = findViewById(R.id.tv_shop);
        tv_profile = findViewById(R.id.tv_profile);
    }

    private void initData() {
        fragmentManager = getSupportFragmentManager();
    }

    private void initEvent() {
        tv_home.setOnClickListener(this);
        tv_shop.setOnClickListener(this);
        tv_profile.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        // 点击切换到对应的Fragment，使用replace
        if (id == R.id.tv_home) {
            // 如果已经是当前页面了，就不需要替换
            if (isNeedReplace("home界面"))
                return;
            replaceFragment("home界面");
        } else if (id == R.id.tv_shop) {
            if (isNeedReplace("shop界面"))
                return;
            replaceFragment("shop界面");
        } else if (id == R.id.tv_profile) {
            if (isNeedReplace("profile界面"))
                return;
            replaceFragment("profile界面");
        }
    }

    // 点击的就是当前页面就不用加载
    private boolean isNeedReplace(String params) {
        return ((PageFragment) fragmentManager.findFragmentById(R.id.fl)).getParams().equals(params);
    }

    // 替换fragment
    private void replaceFragment(String params) {
        Bundle bundle = new Bundle();
        bundle.putString("params", params);
        fragmentManager
                .beginTransaction()
                .replace(R.id.fl, PageFragment.class, bundle)  // fl是FrameLayout的id
                .commit();
    }
}