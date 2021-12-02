package com.ashlikun.adapter.recyclerview.multiltem;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.ashlikun.adapter.ViewHolder;
import com.ashlikun.adapter.recyclerview.vlayout.SingAdapter;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/4/10 0010　20:53
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：VLayout的ItemAdapter
 * <p>
 * 多表单的内部adapter,可以有多个不同的type,
 * 第一步在构造方法addItemType关联id
 */
public abstract class MultipleSingAdapter<T> extends SingAdapter<T> {
    /**
     * ItemType对应的LayoutId
     */
    private SparseIntArray layouts;
    public static final int TYPE_NOT_FOUND = -404;
    public static final int DEFAULT_VIEW_TYPE = -0xff;

    public MultipleSingAdapter(Context context, List<T> datas) {
        super(context, datas);
    }

    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, getItemData(position));
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = getLayoutId(viewType);
        ViewHolder holder = new ViewHolder(context, createRoot(parent, layoutId, viewType), this);
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

    /**
     * 添加默认的view
     *
     * @param layoutResId
     */
    protected void setDefaultViewTypeLayout(@LayoutRes int layoutResId) {
        addItemType(DEFAULT_VIEW_TYPE, layoutResId);
    }

    @Override
    public int getLayoutId(int viewType) {
        return layouts.get(viewType, TYPE_NOT_FOUND);
    }

    public abstract int getItemViewType(int position, T data);
}
