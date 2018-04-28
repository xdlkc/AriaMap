package com.xidian.aria.ariamap;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.xidian.aria.ariamap.adapters.MyFragmentPagerAdapter;
import com.xidian.aria.ariamap.fragments.DriveFragment;
import com.xidian.aria.ariamap.fragments.TransitFragment;
import com.xidian.aria.ariamap.fragments.WalkFragment;

import java.util.LinkedList;
import java.util.List;

public class RouteResultActivity extends FragmentActivity {
    TabLayout tabLayout = null;
    ViewPager viewPager = null;
    // 传递过来的数据
    SerializableBaiduMap serializableBaiduMap = null;
    FragmentPagerAdapter fragmentPagerAdapter;
    Fragment walkFragment;
    DriveFragment driveFragment;
    TransitFragment transitFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_result);
        tabLayout = findViewById(R.id.route_tab);
        viewPager = findViewById(R.id.route_view_pager);
        Intent intent = getIntent();
        serializableBaiduMap = intent.getParcelableExtra("map");
        initViewPager();
    }
    public void initViewPager(){
        walkFragment = WalkFragment.newInstance(serializableBaiduMap);
        driveFragment = DriveFragment.newInstance(serializableBaiduMap);
        transitFragment = TransitFragment.newInstance(serializableBaiduMap);
        List<Fragment> fragments = new LinkedList<>();
        fragments.add(walkFragment);
        fragments.add(transitFragment);
        fragments.add(driveFragment);
        fragmentPagerAdapter = new MyFragmentPagerAdapter(fragments,getSupportFragmentManager());
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
