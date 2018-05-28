package com.xidian.aria.ariamap.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.List;


public abstract class MyExpandableListAdapter<P, C> extends BaseExpandableListAdapter {
    protected Context mContext;
    protected List<P> mParentList;
    protected List<List<C>> mChildList;
    protected int mParentLayout;
    protected int mChildLayout;

    public MyExpandableListAdapter(Context mContext, List<P> mParentList, List<List<C>> mChildList, int mParentLayout, int mChildLayout) {
        this.mContext = mContext;
        this.mParentList = mParentList;
        this.mChildList = mChildList;
        this.mParentLayout = mParentLayout;
        this.mChildLayout = mChildLayout;
    }
    public abstract void convertParent(ViewHolder holder, P p, int groupPosition, boolean isExpanded);
    public abstract void convertChild(ViewHolder holder, C c, int groupPosition, int childPosition, boolean isLastChild);
    @Override
    public int getGroupCount() {
        return mParentList == null ? 0 : mParentList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (mChildList == null || mChildList.size() <= groupPosition || mChildList.get(groupPosition) == null) ?
                0 : mChildList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mParentList == null ? null : mParentList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return (mChildList == null || mChildList.size() <= groupPosition || mChildList.get(groupPosition) == null) ?
                null : mChildList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getInstance(mContext, convertView, mParentLayout);
        convertParent(holder, mParentList.get(groupPosition), groupPosition, isExpanded);
        return holder.getConvertView();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getInstance(mContext, convertView, mChildLayout);
        convertChild(holder, mChildList.get(groupPosition).get(childPosition), groupPosition, childPosition, isLastChild);
        return holder.getConvertView();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}

