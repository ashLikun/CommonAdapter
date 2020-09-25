package com.ashlikun.adapter.recyclerview;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

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


    public CommonAdapter(@NonNull Context context, int layoutId, List<T> datas) {
        super(context, layoutId, datas);
    }

    public CommonAdapter(@NonNull Context context, List<T> datas) {
        super(context, datas);
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(mContext, createLayout(parent, getLayoutId(viewType), viewType), this);
        setListener(parent, holder, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setListener(recyclerView, holder, holder.getItemViewType());
        convert(holder, getItemData(position));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, List<Object> payloads) {
        if (payloads != null && !payloads.isEmpty()) {
            if (!convert(holder, getItemData(position), payloads)) {
                super.onBindViewHolder(holder, position, payloads);
            }
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }
}
