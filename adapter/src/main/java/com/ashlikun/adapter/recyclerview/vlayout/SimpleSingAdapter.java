package com.ashlikun.adapter.recyclerview.vlayout;

import android.content.Context;
import android.view.View;

import com.alibaba.android.vlayout.LayoutHelper;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/4/10 0010　20:53
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：VLayout的ItemAdapter,默认ViewHolder
 *
 * @Deprecated 请直接使用{@link SingAdapter}
 */
@Deprecated
public abstract class SimpleSingAdapter<T> extends SingAdapter<T> {


    public SimpleSingAdapter(Context context, int layoutId, LayoutHelper layoutHelper, List<T> datas) {
        super(context, layoutId, datas);
        this.setLayoutHelper(layoutHelper);
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
        super(context, View.NO_ID, datas);
    }

    /**
     * 要使用这个构造器
     * 这里就必须重写 @{@link SingAdapter#getLayoutId}方法
     */
    public SimpleSingAdapter(Context context) {
        this(context, null);
    }

}
