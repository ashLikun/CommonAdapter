package com.ashlikun.adapter.abslistview;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ashlikun.adapter.ViewHolder;

import java.util.List;

public abstract class CommonAdapter<T, DB extends ViewDataBinding> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mDatas;
    private int layoutId;

    public int getLayoutId() {
        return layoutId;
    }

    public CommonAdapter(Context context, int layoutId, List<T> datas) {
        this.mContext = context;
        this.mDatas = datas;
        this.layoutId = layoutId;
    }

    public int getmLayoutId() {
        return layoutId;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder<DB> holder = ViewHolder.get(mContext, convertView, parent,
                layoutId, position);
        convert(holder, getItem(position));
        return holder.getConvertView();
    }

    public abstract void convert(ViewHolder holder, T t);

}
