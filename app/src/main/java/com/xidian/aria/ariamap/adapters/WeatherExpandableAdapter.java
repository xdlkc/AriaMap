package com.xidian.aria.ariamap.adapters;

import android.content.Context;

import java.util.List;

/**
 * Created by lkc on 2018/5/16.
 */

public class WeatherExpandableAdapter extends MyExpandableListAdapter<String ,String >{
    public WeatherExpandableAdapter(Context mContext, List<String> mParentList, List<List<String>> mChildList, int mParentLayout, int mChildLayout) {
        super(mContext, mParentList, mChildList, android.R.layout.simple_list_item_1, android.R.layout.simple_expandable_list_item_1);
    }

    @Override
    public void convertParent(ViewHolder holder, String s, int groupPosition, boolean isExpanded) {

    }

    @Override
    public void convertChild(ViewHolder holder, String s, int groupPosition, int childPosition, boolean isLastChild) {

    }
}
