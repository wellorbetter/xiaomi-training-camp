package com.example.day5.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.day5.Adapter.VPAdapter;
import com.example.day5.CustomNav;
import com.example.day5.Fragment.ClassFragment;
import com.example.day5.Fragment.ClassFragment1;
import com.example.day5.Fragment.ClassFragment2;
import com.example.day5.Fragment.ClassFragment3;
import com.example.day5.Fragment.ClassFragment4;
import com.example.day5.Fragment.HelpCenterFragment;
import com.example.day5.Fragment.HomeFragment;
import com.example.day5.Fragment.LoginFragment;
import com.example.day5.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, BottomNavigationView.OnNavigationItemSelectedListener {
    ViewPager vp;
    CustomNav nav;
    List<Fragment> pages;
    VPAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        vp = findViewById(R.id.vp);
        nav = findViewById(R.id.nav);
    }

    private void initData() {
        pages = new ArrayList<>();
        pages.add(new HomeFragment());
        pages.add(new LoginFragment());
        pages.add(new ClassFragment());
        pages.add(new ClassFragment1());
        pages.add(new ClassFragment2());
        pages.add(new ClassFragment3());
        pages.add(new ClassFragment4());
        pages.add(new HelpCenterFragment());
        adapter = new VPAdapter(getSupportFragmentManager(), pages);
    }

    private void initEvent() {
        vp.setAdapter(adapter);
        vp.addOnPageChangeListener(this);
        nav.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        nav.getMenu().getItem(position).setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // set getorder 没用
        int id = item.getItemId();
        if (id == R.id.home) {
            vp.setCurrentItem(0);
        } else if (id == R.id.person) {
            vp.setCurrentItem(1);
        } else if (id == R.id.be_on_class) {
            vp.setCurrentItem(2);
        } else if (id == R.id.be_on_class1) {
            vp.setCurrentItem(3);
        } else if (id == R.id.be_on_class2) {
            vp.setCurrentItem(4);
        } else if (id == R.id.be_on_class3) {
            vp.setCurrentItem(5);
        } else if (id == R.id.be_on_class4) {
            vp.setCurrentItem(6);
        } else if (id == R.id.help_center) {
            vp.setCurrentItem(7);
        }
        return false;
    }

}
