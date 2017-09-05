package com.ashlikun.adapter.abslistview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.ashlikun.adapter.ViewHolder;

import java.util.List;

public abstract class CommonAdapter<T> extends com.ashlikun.adapter.abslistview.BaseAdapter<T> {


    public CommonAdapter(Context context, int layoutId, List<T> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder(mContext, getItemLayout(parent, getLayoutId()), position);
            holder.itemView.setTag(10086, this);
        } else {
            holder = (ViewHolder) convertView.getTag(10086);
            holder.setPosition(position);
        }
        convert(holder, getItem(position));
        return holder.itemView;
    }

    public abstract void convert(ViewHolder holder, T t);

}
