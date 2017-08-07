package com.ashlikun.adapter.recyclerview.support;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ashlikun.adapter.ViewHolder;
import com.ashlikun.adapter.recyclerview.CommonAdapter;
import com.ashlikun.adapter.recyclerview.MultiItemTypeSupport;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 作者　　: 李坤
 * 创建时间: 9:57 Administrator
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：有头部的adapter
 */

public abstract class SectionAdapter<T> extends CommonAdapter<T> implements MultiItemTypeSupport<T>, SectionSupport<T> {
    private static final int TYPE_SECTION = 0;
    private LinkedHashMap<String, Integer> mSections;


    final RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            findSections();
        }
    };

    @Override
    public int getLayoutId(int itemType) {
        return mLayoutId;
    }

    @Override
    public int getItemViewType(int position, T t) {
        return -1;
    }

    @Override
    public int getItemViewType(int position) {
        return mSections.values().contains(position) ?
                TYPE_SECTION :
                getItemViewType(position, getItemData(position));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;
        if (viewType == TYPE_SECTION)
            layoutId = sectionHeaderLayoutId();
        else {
            layoutId = getLayoutId(viewType);
        }
        if (layoutId <= 0) {
            throw new RuntimeException("layoutId 没有找到");
        }
        ViewHolder holder = ViewHolder.get(mContext, null, parent, layoutId, -1);
        return holder;
    }


    public SectionAdapter(Context context, List<T> datas) {
        this(context, -1, datas);
    }

    public SectionAdapter(Context context, int layoutId, List<T> datas) {
        super(context, layoutId, datas);
        mSections = new LinkedHashMap<>();
        findSections();
        registerAdapterDataObserver(observer);
    }

    @Override
    protected boolean isEnabled(int viewType) {
        if (viewType == TYPE_SECTION)
            return false;
        return super.isEnabled(viewType);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        unregisterAdapterDataObserver(observer);
    }

    public void findSections() {
        int n = mDatas.size();
        int nSections = 0;
        mSections.clear();

        for (int i = 0; i < n; i++) {
            String sectionName = getTitle(i, mDatas.get(i));

            if (!mSections.containsKey(sectionName)) {
                mSections.put(sectionName, i + nSections);
                nSections++;
            }
        }

    }


    @Override
    public int getItemCount() {
        return super.getItemCount() + mSections.size();
    }

    public int getIndexForPosition(int position) {
        int nSections = 0;

        Set<Map.Entry<String, Integer>> entrySet = mSections.entrySet();
        for (Map.Entry<String, Integer> entry : entrySet) {
            if (entry.getValue() < position) {
                nSections++;
            }
        }
        return position - nSections;
    }

    @Override
    protected int getPosition(RecyclerView.ViewHolder viewHolder) {
        return getIndexForPosition(viewHolder.getAdapterPosition());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        position = getIndexForPosition(position);
        if (holder.getItemViewType() == TYPE_SECTION) {
            setTitle(holder, getTitle(position, mDatas.get(position)), mDatas.get(position));
            return;
        }
        super.onBindViewHolder(holder, position);
    }


}
