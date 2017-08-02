package com.ashlikun.adapter.recyclerview;

import android.content.Context;
import android.view.ViewGroup;

import com.ashlikun.adapter.ViewHolder;
import com.ashlikun.baseadapter.IStickyHeadersAdapter;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 9:31 Administrator
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：带头部的RecyclerView的adapter
 */

public abstract class CommonHeaderAdapter<T>
        implements IStickyHeadersAdapter<ViewHolder> {
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;

    public CommonHeaderAdapter(Context context, int layoutId, List<T> datas) {
        mContext = context;
        mLayoutId = layoutId;
        mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.get(mContext, null, parent, mLayoutId, -1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder headerViewHolder, int position) {
        headerViewHolder.updatePosition(position);
        convert(headerViewHolder, mDatas.get(position));
    }

    public abstract void convert(ViewHolder holder, T t);

    @Override
    public long getHeaderId(int position) {
        return mDatas.get(position).hashCode();
    }

}
