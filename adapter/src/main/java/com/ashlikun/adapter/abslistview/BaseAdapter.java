package com.ashlikun.adapter.abslistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/25 9:56
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：基础的RecycleView的adapter
 */
public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {
    protected Context mContext;
    protected List<T> mDatas;
    protected int layoutId;


    public BaseAdapter(Context context, int layoutId, List<T> datas) {
        this.mContext = context;
        this.mDatas = datas;
        this.layoutId = layoutId;
    }

    public BaseAdapter(Context context, List<T> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    public int getLayoutId() {
        return layoutId;
    }

    //可以重写这个方法，用java代码写布局,构造方法就不用传layoutID了
    public View getItemLayout(ViewGroup parent, int layoutId) {
        return LayoutInflater.from(mContext).inflate(layoutId, parent, false);
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
}
