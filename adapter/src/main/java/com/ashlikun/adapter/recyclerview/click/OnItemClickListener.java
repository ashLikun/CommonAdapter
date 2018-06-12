package com.ashlikun.adapter.recyclerview.click;

import android.view.View;
import android.view.ViewGroup;


public interface OnItemClickListener<T> {

    void onItemClick(int viewType,ViewGroup parent, View view, T data, int position);
}
