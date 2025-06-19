package com.example.day6.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;
import java.util.Optional;

/**
 * @author wellorbetter
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    List<Fragment> mPages;
    public ViewPagerAdapter(@NonNull FragmentManager fm, List<Fragment> mPages) {
        super(fm);
        this.mPages = mPages;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mPages.get(position);
    }

    @Override
    public int getCount() {
        return mPages.size();
    }
}
