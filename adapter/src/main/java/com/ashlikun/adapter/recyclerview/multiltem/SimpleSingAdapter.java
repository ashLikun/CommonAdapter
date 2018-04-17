package com.ashlikun.adapter.recyclerview.multiltem;

import android.content.Context;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.LayoutHelper;
import com.ashlikun.adapter.ViewHolder;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/4/10 0010　20:53
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：多表单的内部adapter,默认ViewHolder
 *
 * @OnLifecycleEvent(Lifecycle.Event.ON_RESUME) public void onResume() {
 * }
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

    /**
     * 要使用这个构造器
     * 这里就必须重写 @{@link SingAdapter#getLayoutId}方法
     */
    public SimpleSingAdapter(Context context, List<T> datas) {
        this(context, -1, null,datas);
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
    public ViewHolder createHolder(final ViewGroup parent) {
        return new ViewHolder(mContext, getItemLayout(parent, getLayoutId()), -1);
    }

}
