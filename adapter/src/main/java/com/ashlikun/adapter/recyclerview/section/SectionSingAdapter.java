package com.ashlikun.adapter.recyclerview.section;

import android.content.Context;
import android.view.ViewGroup;

import com.ashlikun.adapter.ViewHolder;
import com.ashlikun.adapter.recyclerview.vlayout.SimpleSingAdapter;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 9:57 Administrator
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：分组adapter 给Vlayout的
 */

public abstract class SectionSingAdapter<T extends SectionEntity> extends SimpleSingAdapter<T> {
    /**
     * 头部ItemType
     */
    public static final int TYPE_SECTION = -0x00000444;
    /**
     * 头布局id
     */
    protected int mSectionHeadResId = -2;

    public SectionSingAdapter(Context context, List<T> datas) {
        this(context, -1, -2, datas);
    }

    public SectionSingAdapter(Context context, int mSectionHeadResId, List<T> datas) {
        this(context, -1, mSectionHeadResId, datas);
    }

    public SectionSingAdapter(Context context, int layoutId, int mSectionHeadResId, List<T> datas) {
        super(context, layoutId, datas);
        this.mSectionHeadResId = mSectionHeadResId;
    }

    public abstract int getItemViewType(int position, T data);

    @Override
    public int getItemViewType(int position) {
        return getItemData(position).isHeader() ?
                TYPE_SECTION :
                getItemViewType(position, getItemData(position));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SECTION) {
            ViewHolder holder = new ViewHolder(mContext, getItemLayout(parent, mSectionHeadResId), -1);
            setListener(parent, holder, viewType);
            return holder;
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }


    protected boolean isViewTypeHeader(int viewType) {
        return viewType == TYPE_SECTION;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_SECTION) {
            holder.setPosition(position);
            setListener(recyclerView, holder, holder.getItemViewType());
            bindHeader(holder, getItemData(position));
            return;
        }
        super.onBindViewHolder(holder, position);
    }

    /**
     * 为header设置数据
     *
     * @param holder
     */
    public abstract void bindHeader(ViewHolder holder, T data);
}
