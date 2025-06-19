package com.example.day4.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.day4.Adapter.VPAdapter;
import com.example.day4.Bean.Data;
import com.example.day4.Fragment.ImprovedRVFragment;
import com.example.day4.Fragment.RVFragment;
import com.example.day4.Fragment.TableInputFragment;
import com.example.day4.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    BottomNavigationView nav;
    ViewPager vp;
    List<Data> items;
    List<Fragment> pages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
        initEvent();
    }

    private void initData() {
        pages = new ArrayList<>();
        items = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            items.add(new Data(getDrawable(R.drawable.img), "这是内容 " + i));
        }
        TableInputFragment tableInputFragment = new TableInputFragment();
        RVFragment rvFragment = new RVFragment(items);
        ImprovedRVFragment improvedRVFragment = new ImprovedRVFragment(items);

        pages.add(tableInputFragment);
        pages.add(rvFragment);
        pages.add(improvedRVFragment);
    }

    private void initView() {
        nav = findViewById(R.id.nav);
        vp = findViewById(R.id.vp);

        //  ViewPager 适配器
        vp.setAdapter(new VPAdapter(getSupportFragmentManager(), pages));
        vp.setCurrentItem(0);
        clearColor();
        nav.findViewById(R.id.tableInput).setBackgroundColor(getResources().getColor(R.color.colorSelected));
    }

    private void initEvent() {
        nav.setOnNavigationItemSelectedListener(this);
        vp.addOnPageChangeListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.tableInput) {
            vp.setCurrentItem(0);
        } else if (id == R.id.list) {
            vp.setCurrentItem(1);
        } else if (id == R.id.improvedList) {
            vp.setCurrentItem(2);
        }
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                nav.setSelectedItemId(R.id.tableInput);
                clearColor();
                nav.findViewById(R.id.tableInput).setBackgroundColor(getResources().getColor(R.color.colorSelected));
                break;
            case 1:
                nav.setSelectedItemId(R.id.list);
                clearColor();
                nav.findViewById(R.id.list).setBackgroundColor(getResources().getColor(R.color.colorSelected));
                break;
            case 2:
                nav.setSelectedItemId(R.id.improvedList);
                clearColor();
                nav.findViewById(R.id.improvedList).setBackgroundColor(getResources().getColor(R.color.colorSelected));
                // 注意顺序
                break;
        }
    }

    private void clearColor() {
        nav.findViewById(R.id.tableInput).setBackgroundColor(getResources().getColor(R.color.colorUnselected));
        nav.findViewById(R.id.list).setBackgroundColor(getResources().getColor(R.color.colorUnselected));
        nav.findViewById(R.id.improvedList).setBackgroundColor(getResources().getColor(R.color.colorUnselected));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
