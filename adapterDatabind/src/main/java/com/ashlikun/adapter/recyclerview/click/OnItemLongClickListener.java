package com.ashlikun.adapter.recyclerview.click;

/**
 * Created by Administrator on 2016/8/27.
 */

import android.view.View;
import android.view.ViewGroup;


public interface OnItemLongClickListener<T> {

    boolean onItemLongClick(ViewGroup parent, View view, T data, int position);
}