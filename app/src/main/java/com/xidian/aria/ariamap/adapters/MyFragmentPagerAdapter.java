package com.xidian.aria.ariamap.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.xidian.aria.ariamap.fragments.*;

import java.util.LinkedList;
import java.util.List;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter{
    private String [] titles = {"步行","公交","驾车"};
    private List<Fragment> fragments;
    public MyFragmentPagerAdapter(List<Fragment> fragments,FragmentManager fm){
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return titles.length;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
