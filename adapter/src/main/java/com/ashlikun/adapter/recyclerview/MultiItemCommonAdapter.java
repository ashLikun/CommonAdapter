package com.ashlikun.adapter.recyclerview;

import android.content.Context;
import android.view.ViewGroup;

import com.ashlikun.adapter.ViewHolder;

import java.util.List;

public abstract class MultiItemCommonAdapter<T> extends CommonAdapter<T> implements MultiItemTypeSupport<T> {

    public MultiItemCommonAdapter(Context context, List<T> datas) {
        super(context, -1, datas);
    }

    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, getItemData(position));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = getLayoutId(viewType);
        ViewHolder holder = ViewHolder.get(mContext, null, parent, layoutId, -1);
        return holder;
    }
}
