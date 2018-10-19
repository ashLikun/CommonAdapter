package com.ashlikun.adapter.recyclerview.callback;

import android.support.v7.widget.RecyclerView;

/**
 * @author　　: 李坤
 * 创建时间: 2018/10/19 9:44
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：item拖拽回调
 */

public interface OnItemDragListener {
    void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos);

    void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to);

    void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos);
}
