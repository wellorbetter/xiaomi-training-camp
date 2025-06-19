package com.example.day3.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class VPAdapter extends FragmentStatePagerAdapter {
    List<Fragment> pages;

    public VPAdapter(@NonNull FragmentManager fm, List<Fragment> pages) {
        super(fm);
        this.pages = pages;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return pages.get(position);
    }

    @Override
    public int getCount() {
        return pages.size();
    }
}
