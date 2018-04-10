package com.ashlikun.adapter.recyclerview.multiltem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.LayoutHelper;
import com.ashlikun.adapter.ViewHolder;
import com.ashlikun.adapter.recyclerview.BaseAdapter;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/4/10 0010　20:48
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：多表单的内部adapter,得自己穿件ViewHolder
 * @OnLifecycleEvent(Lifecycle.Event.ON_RESUME) public void onResume() {
 * }
 */
public abstract class SingAdapter<T, VH extends ViewHolder> extends BaseAdapter<T, VH> {
    /**
     * 这2个方法是父Adapter onBindViewHolder回掉的
     */
    protected void onBindViewHolderWithOffset(RecyclerView.ViewHolder holder, int position, int offsetTotal) {

    }

    /**
     * 这2个方法是父Adapter onBindViewHolder回掉的
     */
    protected void onBindViewHolderWithOffset(RecyclerView.ViewHolder holder, int position, int offsetTotal, List<Object> payloads) {
        onBindViewHolderWithOffset(holder, position, offsetTotal);
    }

    public SingAdapter(Context context, int layoutId, List<T> datas) {
        super(context, layoutId, datas);
    }

    /**
     * 要使用这个构造器
     * 这里就必须重写 @{@link SingAdapter#getLayoutId}方法
     */
    public SingAdapter(Context context, List<T> datas) {
        super(context, -1, datas);
    }

    @Override
    public VH onCreateViewHolder(final ViewGroup parent, int viewType) {
        ViewHolder holder = createHolder(parent);
        holder.setHeaderSize(getHeaderSize());
        setListener(parent, holder, viewType);
        return (VH) holder;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.setPosition(position);
        holder.setHeaderSize(getHeaderSize());
        convert(holder, mDatas.get(position));
    }

    public abstract LayoutHelper onCreateLayoutHelper();

    public abstract VH createHolder(final ViewGroup parent);

}