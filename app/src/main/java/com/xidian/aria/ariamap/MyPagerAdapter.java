package com.xidian.aria.ariamap;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by lkc on 18-3-23.
 */

public class MyPagerAdapter extends PagerAdapter {
    private List<View> viewList;

    public MyPagerAdapter(List<View> viewList) {
        this.viewList = viewList;
    }

    @Override
    public int getCount() {//返回页卡数量
        return viewList.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {//判断View是否来自Object
        return view==object;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {//初始化一个页卡
        container.addView(viewList.get(position));
        return viewList.get(position);
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {//销毁一个页卡
        container.removeView(viewList.get(position));
    }
}
