package com.ashlikun.adapter.recyclerview.section;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.ashlikun.adapter.ViewHolder;
import com.ashlikun.adapter.recyclerview.CommonAdapter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 作者　　: 李坤
 * 创建时间: 9:57 Administrator
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：分组adapter
 */

public abstract class SectionAdapter<T> extends CommonAdapter<T> implements SectionSupport<T> {
    /**
     * 头部ItemType
     */
    private static final int TYPE_SECTION = 0;
    private LinkedHashMap<String, Integer> mSections;

    public SectionAdapter(Context context, List<T> datas) {
        this(context, -1, datas);
    }

    public SectionAdapter(Context context, int layoutId, List<T> datas) {
        super(context, layoutId, datas);
        mSections = new LinkedHashMap<>();
        findSections();
        registerAdapterDataObserver(observer);
    }

    final RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            findSections();
        }
    };

    public int getLayoutId(int itemType) {
        return mLayoutId;
    }

    public abstract int getItemViewType(int position, T data);

    @Override
    public int getItemViewType(int position) {
        return mSections.values().contains(position) ?
                TYPE_SECTION :
                getItemViewType(position, getItemData(position));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;
        if (viewType == TYPE_SECTION) {
            layoutId = sectionHeaderLayoutId();
        } else {
            layoutId = getLayoutId(viewType);
        }
        if (layoutId <= 0) {
            throw new RuntimeException("layoutId 没有找到");
        }
        ViewHolder holder = new ViewHolder(mContext, getItemLayout(parent, layoutId), -1);
        setListener(parent, holder, viewType);
        return holder;
    }


    @Override
    protected boolean isEnabled(int viewType) {
        return viewType != TYPE_SECTION && super.isEnabled(viewType);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        unregisterAdapterDataObserver(observer);
    }

    /**
     * 设置分组数据
     */
    public void findSections() {
        int n = getItemCount();
        int nSections = 0;
        mSections.clear();

        for (int i = 0; i < n; i++) {
            String sectionName = getTitle(i, getItemData(i));
            if (!TextUtils.isEmpty(sectionName)) {
                if (!mSections.containsKey(sectionName)) {
                    mSections.put(sectionName, i + nSections);
                    nSections++;
                }
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
    protected int getPosition(ViewHolder viewHolder) {
        return getIndexForPosition(viewHolder.getAdapterPosition());
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(getIndexForPosition(position));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        position = getIndexForPosition(position);
        if (holder.getItemViewType() == TYPE_SECTION) {
            setTitle(holder, getTitle(position, getItemData(position)), getItemData(position));
            return;
        }
        super.onBindViewHolder(holder, position);
    }


}