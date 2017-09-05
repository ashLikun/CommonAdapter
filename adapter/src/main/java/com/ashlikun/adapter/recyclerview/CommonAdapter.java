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
 */
public abstract class CommonAdapter<T> extends BaseAdapter<T, ViewHolder> {


    public CommonAdapter(Context context, int layoutId, List<T> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(mContext, getItemLayout(parent, getLayoutId()), -1);
        setListener(parent, holder, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.updatePosition(position);
        convert(holder, mDatas.get(position));
    }

    public abstract void convert(ViewHolder holder, T t);

}
