package com.ashlikun.adapter.recyclerview.callback;

import android.graphics.Canvas;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/10/19　9:12
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：ItemTouchHelper 的回调再次封装的接口
 */
public interface ItemTouchHelperAdapter {
    public void onItemDragStart(RecyclerView.ViewHolder viewHolder);

    public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder);

    public void onItemDragEnd(RecyclerView.ViewHolder viewHolder);

    public void onItemSwipeClear(RecyclerView.ViewHolder viewHolder);

    public boolean onItemDragMoving(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target);

    public void onItemSwiped(RecyclerView.ViewHolder viewHolder);

    public void onItemSwiping(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive);

    public boolean isItemSwipeEnable();

    /**
     * 这个holder是不是内部自己创建的，
     *
     * @return true 没有任何触摸事件
     */
    public boolean isViewCreateByAdapter(RecyclerView.ViewHolder viewHolder);
}
