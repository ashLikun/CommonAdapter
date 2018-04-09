package com.ashlikun.adapter.recyclerview;

import android.content.Context;
import android.view.View;

import com.ashlikun.adapter.ViewHolder;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/4/9 0009　下午 4:33
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：多种布局的基类ViewHolder
 * 第一个泛型为本ViewHolder用的数据类型，
 * 第二个泛型为外部adapter的item数据类型，就是setData的参数类型
 */

public abstract class MultiltemViewHolder<T, ADAPTER_DATA> extends ViewHolder {

    public MultiltemViewHolder(Context context, View itemView) {
        super(context, itemView, -1);
    }

    /**
     * 当前item的数据
     */
    public abstract T builderData(ADAPTER_DATA data);


    /**
     * 为了给其他的地方公用，所以有一个holder参数
     */
    public abstract void convert(ViewHolder holder, T t);
}
