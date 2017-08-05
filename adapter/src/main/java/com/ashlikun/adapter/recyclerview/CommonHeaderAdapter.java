package com.ashlikun.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ashlikun.adapter.ViewHolder;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 9:31 Administrator
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：带头部的RecyclerView的adapter
 */

public abstract class CommonHeaderAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;

    public CommonHeaderAdapter(Context context, int layoutId, List<T> datas) {
        mContext = context;
        mLayoutId = layoutId;
        mDatas = datas;
    }

    //viewType无效
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = ViewHolder.get(mContext, null, parent, mLayoutId, -1);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder headerViewHolder, int position) {
        headerViewHolder.updatePosition(position);
        convert(headerViewHolder, getItemData(position));
    }

    public abstract void convert(ViewHolder holder, T t);

    @Override
    public int getItemCount() {
        return 0;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/8/6 0006 1:54
     * <p>
     * 方法功能：保证数组不会溢出
     */

    private T getItemData(int pos) {
        return pos >= mDatas.size() ? mDatas.get(mDatas.size() - 1) : mDatas.get(pos);
    }

    //获取头部id
    @Override
    public long getItemId(int position) {
        return getItemData(position).hashCode();
    }
}
