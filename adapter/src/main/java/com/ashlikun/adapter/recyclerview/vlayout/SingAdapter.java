package com.ashlikun.adapter.recyclerview.vlayout;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.ashlikun.adapter.recyclerview.CommonAdapter;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/4/10 0010　20:48
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：VLayout的ItemAdapter
 */
public abstract class SingAdapter<T> extends CommonAdapter<T> {
    /**
     * 内部的adapter建议只用一个type
     */
    protected Object viewType = this.getClass();
    protected LayoutHelper layoutHelper;

    protected MultipleAdapter.AdapterDataObserver observer;

    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    public LayoutHelper getLayoutHelper() {
        return layoutHelper;
    }

    public void setLayoutHelper(LayoutHelper layoutHelper) {
        this.layoutHelper = layoutHelper;
    }

    /**
     * 这2个方法是父Adapter onBindViewHolder回掉的
     */
    protected void onBindViewHolderWithOffset(RecyclerView.ViewHolder holder, int position, int offsetTotal) {

    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
        if (observer instanceof MultipleAdapter.AdapterDataObserver) {
            this.observer = (MultipleAdapter.AdapterDataObserver) observer;
        }
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
        if (observer instanceof MultipleAdapter.AdapterDataObserver) {
            this.observer = null;
        }
    }

    /**
     * 获取开始的position
     * 可能有头
     * 或者Vlayout内部是多个adapter
     *
     * @return
     */
    @Override
    public int getStartPosition() {
        if (observer != null) {
            return observer.mStartPosition;
        }
        return super.getStartPosition();
    }

    /**
     * 在Vlayout里面第几个
     *
     * @return
     */
    public int getIndex() {
        if (observer != null) {
            return observer.mIndex;
        }
        return -1;
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

    public SingAdapter(Context context, int layoutId) {
        this(context, layoutId, null);
    }

    public SingAdapter(Context context, List<T> datas) {
        this(context, DEFAULT_LAYOUT_ID, datas);
    }

    /**
     * 要使用这个构造器
     * 这里就必须重写 @{@link SingAdapter#getLayoutId}方法
     */
    public SingAdapter(Context context) {
        this(context, -1, null);
    }

    public SingAdapter(Context context, int layoutId, LayoutHelper layoutHelper, List<T> datas) {
        this(context, layoutId, datas);
        this.layoutHelper = layoutHelper;
    }

    /**
     * @author　　: 李坤
     * 创建时间: 2018/4/16 0016 上午 10:47
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：设置这个adapter的ViewType
     */
    public SingAdapter setViewType(Object viewType) {
        this.viewType = viewType;
        return this;
    }

    @Override
    public int getItemViewType(int position) {
        if (viewType == null) {
            return super.getItemViewType(position);
        }
        return Math.abs(viewType.hashCode());
    }


    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public int getItemCount() {
        if (layoutHelper != null && layoutHelper instanceof SingleLayoutHelper) {
            return layoutHelper.getItemCount();
        } else {
            return super.getItemCount();
        }
    }
}