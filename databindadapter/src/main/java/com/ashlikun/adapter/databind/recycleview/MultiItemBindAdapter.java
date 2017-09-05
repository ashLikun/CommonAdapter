package com.ashlikun.adapter.databind.recycleview;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.ViewGroup;

import com.ashlikun.adapter.databind.DataBindHolder;
import com.ashlikun.adapter.recyclerview.MultiItemTypeSupport;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/25 10:26
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：多种布局的DatabindAdapter
 */


public abstract class MultiItemBindAdapter<T> extends CommonBindAdapter<T, ViewDataBinding> implements MultiItemTypeSupport<T> {


    public MultiItemBindAdapter(Context context, List<T> datas) {
        super(context, -1, datas);
    }

    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, getItemData(position));
    }

    @Override
    public DataBindHolder<ViewDataBinding> onCreateViewHolder(ViewGroup parent, int viewType) {
        DataBindHolder holder = DataBindHolder.get(mContext, null, parent, getLayoutId(viewType), -1);
        setListener(parent, holder, viewType);
        return holder;
    }
}
