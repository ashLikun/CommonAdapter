package com.ashlikun.adapter.recyclerview.multiltem;

import android.content.Context;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.LayoutHelper;
import com.ashlikun.adapter.ViewHolder;
import com.ashlikun.adapter.recyclerview.MultiItemTypeSupport;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/4/10 0010　20:53
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：多表单的内部adapter,可以有多个不同的type
 *
 * @OnLifecycleEvent(Lifecycle.Event.ON_RESUME) public void onResume() {
 * }
 */
public abstract class MultipleSingAdapter<T> extends SingAdapter<T, ViewHolder> implements MultiItemTypeSupport<T> {
    LayoutHelper layoutHelper;

    public MultipleSingAdapter(Context context, LayoutHelper layoutHelper, List<T> datas) {
        super(context, -1, datas);
        this.layoutHelper = layoutHelper;
    }

    public MultipleSingAdapter(Context context, LayoutHelper layoutHelper) {
        this(context, layoutHelper, null);
    }

    public MultipleSingAdapter(Context context, List<T> datas) {
        this(context, null, datas);
    }

    public MultipleSingAdapter(Context context) {
        this(context, null, null);
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
        int layoutId = getLayoutId(viewType);
        ViewHolder holder = new ViewHolder(mContext, getItemLayout(parent, layoutId), -1);
        return holder;
    }

    public LayoutHelper getLayoutHelper() {
        return layoutHelper;
    }

    public void setLayoutHelper(LayoutHelper layoutHelper) {
        this.layoutHelper = layoutHelper;
    }

    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, getItemData(position));
    }

    @Override
    public int getItemCount() {
        int size = super.getItemCount();
        if (size <= 0 && layoutHelper != null) {
            size = layoutHelper.getItemCount();
        }
        return size;
    }
}
