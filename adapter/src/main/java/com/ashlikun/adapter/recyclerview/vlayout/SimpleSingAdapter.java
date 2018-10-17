package com.ashlikun.adapter.recyclerview.vlayout;

import android.content.Context;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.ashlikun.adapter.ViewHolder;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/4/10 0010　20:53
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：VLayout的ItemAdapter,默认ViewHolder
 */
public abstract class SimpleSingAdapter<T> extends SingAdapter<T, ViewHolder> {
    LayoutHelper layoutHelper;

    public SimpleSingAdapter(Context context, int layoutId, LayoutHelper layoutHelper, List<T> datas) {
        super(context, layoutId, datas);
        this.layoutHelper = layoutHelper;
    }

    public SimpleSingAdapter(Context context, int layoutId, LayoutHelper layoutHelper) {
        this(context, layoutId, layoutHelper, null);
    }

    public SimpleSingAdapter(Context context, int layoutId, List<T> datas) {
        this(context, layoutId, null, datas);
    }

    public SimpleSingAdapter(Context context, int layoutId) {
        this(context, layoutId, null, null);
    }

    /**
     * 要使用这个构造器
     * 这里就必须重写 @{@link SingAdapter#getLayoutId}方法
     */
    public SimpleSingAdapter(Context context, List<T> datas) {
        this(context, -1, datas);
    }

    /**
     * 要使用这个构造器
     * 这里就必须重写 @{@link SingAdapter#getLayoutId}方法
     */
    public SimpleSingAdapter(Context context) {
        this(context, null);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        if (layoutHelper == null) {
            throw new RuntimeException(getClass().getSimpleName() + " layoutHelper is null");
        }
        return layoutHelper;
    }

    @Override
    public ViewHolder createHolder(final ViewGroup parent, int viewType) {
        return new ViewHolder(mContext, getItemLayout(parent, getLayoutId()), -1);
    }

    public LayoutHelper getLayoutHelper() {
        return layoutHelper;
    }

    public void setLayoutHelper(LayoutHelper layoutHelper) {
        this.layoutHelper = layoutHelper;
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
