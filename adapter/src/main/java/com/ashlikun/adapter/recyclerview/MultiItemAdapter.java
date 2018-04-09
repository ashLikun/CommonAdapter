package com.ashlikun.adapter.recyclerview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/25 9:56
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：多种列表样式的适配器,复杂版(每个item基本不一样)
 * 添加生命周期
 *
 * @OnLifecycleEvent(Lifecycle.Event.ON_RESUME) public void onResume() {
 * }
 */

public abstract class MultiItemAdapter<T> extends BaseAdapter<T, MultiltemViewHolder> implements MultiItemTypeSupport<T> {

    public MultiItemAdapter(Context context, List<T> datas) {
        super(context, -1, datas);
    }

    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, getItemData(position));
    }

    @Override
    public MultiltemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = getLayoutId(viewType);
        MultiltemViewHolder holder = createViewHolder(mContext, getItemLayout(parent, layoutId), viewType);
        holder.setHeaderSize(getHeaderSize());
        setListener(parent, holder, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(MultiltemViewHolder holder, int position) {
        holder.setPosition(position);
        holder.setHeaderSize(getHeaderSize());
        holder.convert(holder, holder.builderData(mDatas.get(position)));
    }

    /**
     * 根据不同的类型创建不同的ViewHolder
     */
    public abstract MultiltemViewHolder createViewHolder(Context context, View view, int itemType);

    /**
     * 这种adapter就不用这个方法了
     */
    @Deprecated
    @Override
    public void convert(MultiltemViewHolder holder, T t) {

    }


}
