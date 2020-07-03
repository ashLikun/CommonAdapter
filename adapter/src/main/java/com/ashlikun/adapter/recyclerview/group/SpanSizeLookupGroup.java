package com.ashlikun.adapter.recyclerview.group;

import androidx.recyclerview.widget.GridLayoutManager;

/**
 * 作者　　: 李坤
 * 创建时间: 2020/7/1　14:28
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */
class SpanSizeLookupGroup extends GridLayoutManager.SpanSizeLookup {
    GridLayoutManager manager;
    GroupedCommonAdapter adapter;
    GridLayoutManager.SpanSizeLookup old;

    public SpanSizeLookupGroup(GridLayoutManager manager, GroupedCommonAdapter adapter) {
        this.old = manager.getSpanSizeLookup();
        this.manager = manager;
        this.adapter = adapter;
    }

    @Override
    public int getSpanSize(int position) {
        if (adapter != null) {
            int type = adapter.judgePositionToType(position);
            if (type == GroupedCommonAdapter.TYPE_HEADER || type == GroupedCommonAdapter.TYPE_FOOTER) {
                //占满全行
                return manager.getSpanCount();
            } else {
                if (old != null) {
                    //使用之前设置过的 并且positions是去除头部后的位置
                    return old.getSpanSize(position);
                } else {
                    return 1;
                }
            }
        }
        return 1;
    }
}
