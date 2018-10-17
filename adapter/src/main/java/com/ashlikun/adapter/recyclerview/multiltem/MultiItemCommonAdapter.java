package com.ashlikun.adapter.recyclerview.multiltem;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.SparseIntArray;
import android.view.ViewGroup;

import com.ashlikun.adapter.ViewHolder;
import com.ashlikun.adapter.recyclerview.BaseAdapter;
import com.ashlikun.adapter.recyclerview.CommonAdapter;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/25 9:56
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：多种列表样式的适配器,简单版
 * 基本功能看 {@link BaseAdapter}类注释
 * 第一步在构造方法addItemType关联id
 */

public abstract class MultiItemCommonAdapter<T> extends CommonAdapter<T> {
    /**
     * ItemType对应的LayoutId
     */
    private SparseIntArray layouts;
    public static final int TYPE_NOT_FOUND = -404;

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
        ViewHolder holder = new ViewHolder(mContext, getItemLayout(parent, layoutId), -1);
        setListener(parent, holder, viewType);
        return holder;
    }

    /**
     * 关联 viewType与layoutResId
     *
     * @param viewType
     * @param layoutResId
     */
    public void addItemType(int viewType, @LayoutRes int layoutResId) {
        if (layouts == null) {
            layouts = new SparseIntArray();
        }
        layouts.put(viewType, layoutResId);
    }

    private int getLayoutId(int viewType) {
        return layouts.get(viewType, TYPE_NOT_FOUND);
    }

    public abstract int getItemViewType(int position, T data);
}
