package com.ashlikun.adapter.recyclerview.support;

import com.ashlikun.adapter.ViewHolder;

/**
 * Created by zhy on 16/4/9.
 */
public interface SectionSupport<T> {
    public int sectionHeaderLayoutId();

    public String getTitle(T t);

    public void setTitle(ViewHolder holder, T t);
}
