package com.ashlikun.adapter.recyclerview.click;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author　　: 李坤
 * 创建时间: 2018/9/6 10:29
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：adapter的Item点击事件
 */

public interface OnItemClickListener<T> {

    void onItemClick(int viewType, ViewGroup parent, View view, T data, int position);
}
