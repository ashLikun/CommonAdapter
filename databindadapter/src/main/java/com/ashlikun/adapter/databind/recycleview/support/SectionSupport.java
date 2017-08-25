package com.ashlikun.adapter.databind.recycleview.support;

import com.ashlikun.adapter.databind.DataBindHolder;

/**
 * Created by zhy on 16/4/9.
 */
public interface SectionSupport<T> {
    public int sectionHeaderLayoutId();

    public String getTitle(int position, T t);

    public void setTitle(DataBindHolder holder, String title, T t);
}
