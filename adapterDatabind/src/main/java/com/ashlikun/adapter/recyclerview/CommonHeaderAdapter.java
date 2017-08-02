package com.ashlikun.adapter.recyclerview;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.ViewGroup;

import com.ashlikun.adapter.ViewHolder;
import com.ashlikun.baseadapter.IStickyHeadersAdapter;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 9:31 Administrator
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：带悬浮头部的RecyclerView的adapter
 * 利用getHeaderId区分是否是头部
 */

public abstract class CommonHeaderAdapter<T, DB extends ViewDataBinding>
        implements IStickyHeadersAdapter<ViewHolder<DB>> {
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;

    public CommonHeaderAdapter(Context context, int layoutId, List<T> datas) {
        mContext = context;
        mLayoutId = layoutId;
        mDatas = datas;
    }

    @Override
    public ViewHolder<DB> onCreateViewHolder(ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.get(mContext, null, parent, mLayoutId, -1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder<DB> headerViewHolder, int position) {
        headerViewHolder.updatePosition(position);
        convert(headerViewHolder, mDatas.get(position));
    }

    public abstract void convert(ViewHolder<DB> holder, T t);

    @Override
    public long getHeaderId(int position) {
        return mDatas.get(position).hashCode();
    }

}
