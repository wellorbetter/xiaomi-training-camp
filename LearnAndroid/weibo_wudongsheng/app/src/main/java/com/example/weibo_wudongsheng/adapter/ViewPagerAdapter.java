package com.example.weibo_wudongsheng.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * @author wellorbetter
 * ViewPager 实现页面切换的适配器
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    /**
     * 存储各个页面的列表
     */
    private List<Fragment> mPages;

    /**
     * 构造方法，初始化页面列表
     *
     * @param fragmentManager Fragment管理器
     * @param pages           页面列表
     */
    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, List<Fragment> pages) {
        super(fragmentManager);
        this.mPages = pages;
    }

    /**
     * 获取指定位置的页面
     *
     * @param position 页面位置
     * @return 指定位置的页面
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mPages.get(position);
    }

    /**
     * 获取页面数量
     *
     * @return 页面数量
     */
    @Override
    public int getCount() {
        return mPages.size();
    }

    /**
     * 获取页面位置
     * 必须重写此方法，以确保ViewPager可以正确替换页面
     *
     * @param object 页面对象
     * @return 页面位置常量
     */
    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
