package com.ashlikun.adapter.databind.recycleview;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.ViewGroup;

import com.ashlikun.adapter.databind.DataBindHolder;
import com.ashlikun.adapter.recyclerview.BaseAdapter;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间:2017/8/13 0013　3:05
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：公共的 RecycleView Adapter;
 */
public abstract class CommonBindAdapter<T, DB extends ViewDataBinding> extends BaseAdapter<T, DataBindHolder<DB>> {
    public CommonBindAdapter(Context context, int layoutId, List<T> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public DataBindHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        DataBindHolder viewHolder = DataBindHolder.get(mContext, null, parent, mLayoutId, this);
        setListener(parent, viewHolder, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DataBindHolder holder, int position) {
        setListener(recyclerView, holder, holder.getItemViewType());
        convert(holder, getItemData(position));
    }


    @Override
    public void onBindViewHolder(DataBindHolder<DB> holder, int position, List<Object> payloads) {
        if (payloads != null && !payloads.isEmpty()) {
            if (!convert(holder, getItemData(position), payloads)) {
                super.onBindViewHolder(holder, position, payloads);
            }
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }
}
