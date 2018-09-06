package com.ashlikun.adapter.recyclerview;

import android.content.Context;
import android.view.ViewGroup;

import com.ashlikun.adapter.ViewHolder;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/25 9:56
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：公共的RecycleView的adapter
 * 在BaseAdapter基础上封装了 onCreateViewHolder,onBindViewHolder
 */
public abstract class CommonAdapter<T> extends BaseAdapter<T, ViewHolder> {


    public CommonAdapter(Context context, int layoutId, List<T> datas) {
        super(context, layoutId, datas);
    }

    public CommonAdapter(Context context, List<T> datas) {
        super(context, -1, datas);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(mContext, getItemLayout(parent, getLayoutId()), -1);
        setListener(parent, holder, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setPosition(position);
        convert(holder, getItemData(position));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        if (payloads != null && !payloads.isEmpty()) {
            holder.setPosition(position);
            if (!convert(holder, getItemData(position), payloads)) {
                super.onBindViewHolder(holder, position, payloads);
            }
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }
}
