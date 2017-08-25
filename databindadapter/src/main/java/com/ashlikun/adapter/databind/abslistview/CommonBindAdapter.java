package com.ashlikun.adapter.databind.abslistview;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

import com.ashlikun.adapter.databind.DataBindHolder;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间:2017/8/13 0013　3:05
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：公共的 ListView Adapter;
 */
public abstract class CommonBindAdapter<T, DB extends ViewDataBinding> extends com.ashlikun.adapter.abslistview.BaseAdapter<T> {
    public CommonBindAdapter(Context context, int layoutId, List<T> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DataBindHolder holder = DataBindHolder.get(mContext, convertView, parent,
                layoutId, position);
        convert(holder, getItem(position));
        return holder.getConvertView();
    }

    public abstract void convert(DataBindHolder<DB> holder, T t);
}
