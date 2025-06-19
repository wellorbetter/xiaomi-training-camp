package com.example.day6.activities;

import static com.example.day6.utils.NewsService.API_PREFIX;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.day6.R;
import com.example.day6.adapters.ViewPagerAdapter;
import com.example.day6.beans.Item;
import com.example.day6.beans.NewsItem;
import com.example.day6.fragments.HomePage;
import com.example.day6.fragments.PersonalPage;
import com.example.day6.utils.ConvertUtil;
import com.example.day6.utils.NewsService;
import com.example.day6.utils.PermissionService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ViewPager mHomeViewPager;
    BottomNavigationView mHomeBottomNavigationView;
    List<Item> mItems;
    NewsItem mNewsItems;
    ViewPagerAdapter adapter;
    List<Fragment> mPages;
    String mNowDate;
    boolean isDataLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initEvent();
    }

    private void initData() {
        mItems = new ArrayList<>();
        mPages = new ArrayList<>();

        // 异步加载最新新闻数据
        NewsService.getInstance().getTargetNews(API_PREFIX + "20240515").thenAccept(newsItem -> {
            mNewsItems = newsItem;
            mNowDate = newsItem.getDate();
            ConvertUtil.getInstance().convertData(this, mItems, mNewsItems, true);
            isDataLoading = true;
            // 数据加载完成后，添加首页Fragment并设置ViewPager适配器
            mPages.add(new HomePage(this, mItems, mNowDate));
            mPages.add(new PersonalPage());
            adapter = new ViewPagerAdapter(getSupportFragmentManager(), mPages);
            runOnUiThread(() -> mHomeViewPager.setAdapter(adapter));
        }).exceptionally(throwable -> {
            runOnUiThread(() -> Toast.makeText(this, "获取新闻失败: " + throwable.getMessage(), Toast.LENGTH_SHORT).show());
            return null;
        });
    }

    private void initEvent() {
        mHomeBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.homePage) {
                mHomeViewPager.setCurrentItem(0);
                return true;
            } else if (id == R.id.personalPage) {
                mHomeViewPager.setCurrentItem(1);
                return true;
            }
            return false;
        });

        mHomeViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mHomeBottomNavigationView.setSelectedItemId(R.id.homePage);
                        break;
                    default:
                        mHomeBottomNavigationView.setSelectedItemId(R.id.personalPage);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void initView() {
        mHomeViewPager = findViewById(R.id.homeViewPager);
        mHomeBottomNavigationView = findViewById(R.id.homeBottomNavigationView);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionService.PERMISSIONS_RESULT_CODE && grantResults.length > 0) {
            // 判断是否获得权限
            for (int i = 0; i < grantResults.length; i++) {
                // 未得到授权的权限
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                        Toast.makeText(this, permissions[i] + " 未授权且不再询问", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, permissions[i] + " 未授权", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, permissions[i] + " 已授权", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
