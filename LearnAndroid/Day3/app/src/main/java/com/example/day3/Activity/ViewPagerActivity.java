package com.example.day3.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.day3.Adapter.VPAdapter;
import com.example.day3.Fragment.PageFragment;
import com.example.day3.R;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends AppCompatActivity {
    ViewPager vp;
    List<Fragment> pages;
    VPAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        initView();
        intiData();
        initEvent();
    }

    private void intiData() {
        pages = new ArrayList<>();
        for (int i = 0; i < 5; i ++ ) {
            pages.add(new PageFragment());
        }
        adapter = new VPAdapter(getSupportFragmentManager(), pages);
    }

    private void initEvent() {
        vp.setAdapter(adapter);
    }

    private void initView() {
        vp = findViewById(R.id.vp);
    }
}