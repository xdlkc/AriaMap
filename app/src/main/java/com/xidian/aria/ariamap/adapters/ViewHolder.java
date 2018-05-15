package com.xidian.aria.ariamap.adapters;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by lkc on 2018/5/16.
 */

public class ViewHolder {
    private View convertView;
    private SparseArray<View> views; // SparseArray效率比HashMap更高

    private ViewHolder(View convertView) {
        this.views = new SparseArray<>();
        this.convertView = convertView;
        convertView.setTag(this);
    }

    public static ViewHolder getInstance(Context context, View convertView, int layout) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layout, null);
            return new ViewHolder(convertView);
        }
        return (ViewHolder) convertView.getTag(); // 重用convertView的逻辑
    }

    public <T extends View> T findViewById(int id) {
        View view = views.get(id);
        if (view == null) {
            view = convertView.findViewById(id);
            views.append(id, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return convertView;
    }

}
