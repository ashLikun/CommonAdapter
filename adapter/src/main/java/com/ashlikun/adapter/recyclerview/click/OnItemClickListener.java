package com.ashlikun.adapter.recyclerview.click;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

/**
 * @author　　: 李坤
 * 创建时间: 2018/9/6 10:29
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：adapter的Item点击事件
 */

public interface OnItemClickListener<T> {

    void onItemClick(int viewType, @NonNull ViewGroup parent, @NonNull View view, @NonNull T data, int position);
}
