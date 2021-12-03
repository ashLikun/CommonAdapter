package com.ashlikun.adapter.recyclerview.callback

import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.3 15:50
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：ItemTouchHelper 的回调再次封装的接口
 */

interface ItemTouchHelperAdapter {
    var isItemSwipeEnable: Boolean
    var isItemDraggable: Boolean
    fun onItemDragStart(viewHolder: RecyclerView.ViewHolder)
    fun onItemSwipeStart(viewHolder: RecyclerView.ViewHolder)
    fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder)
    fun onItemSwipeClear(viewHolder: RecyclerView.ViewHolder)
    fun onItemDragMoving(source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean
    fun onItemSwiped(viewHolder: RecyclerView.ViewHolder)
    fun onItemSwiping(canvas: Canvas, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, isCurrentlyActive: Boolean)

    /**
     * 这个holder是不是内部自己创建的，
     *
     * @return true 没有任何触摸事件
     */
    fun isViewCreateByAdapter(viewHolder: RecyclerView.ViewHolder): Boolean
}