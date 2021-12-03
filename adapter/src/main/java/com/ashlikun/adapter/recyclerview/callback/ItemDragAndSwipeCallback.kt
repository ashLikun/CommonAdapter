package com.ashlikun.adapter.recyclerview.callback

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import com.ashlikun.adapter.recyclerview.callback.ItemTouchHelperAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ashlikun.adapter.R

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.3 15:50
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：ItemTouchHelper的回调监听
 */

open class ItemDragAndSwipeCallback(
        var adapter: ItemTouchHelperAdapter,
        //如果是grid布局，那么就是上下左右都可以移动
        var isGrid: Boolean = false) : ItemTouchHelper.SimpleCallback(if (isGrid) ItemTouchHelper.UP or ItemTouchHelper.DOWN or
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT else ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {

    /**
     * 开启长按拖拽功能，默认为true
     * 如果设置为false，手动开启，调用startDrag()
     *
     * @return
     */
    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    /**
     * 开始滑动功能，默认为true
     * 如果设置为false，手动开启，调用startSwipe()
     *
     * @return
     */
    override fun isItemViewSwipeEnabled(): Boolean {
        return adapter.isItemSwipeEnable
    }

    /**
     * 长按选中Item的时候开始调用
     *
     * @param viewHolder
     * @param actionState
     */
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder != null) {
            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG
                    && !adapter.isViewCreateByAdapter(viewHolder)) {
                adapter.onItemDragStart(viewHolder)
                viewHolder.itemView.setTag(R.id.adapter_dragging_support, true)
            } else if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE
                    && !adapter.isViewCreateByAdapter(viewHolder)) {
                adapter.onItemSwipeStart(viewHolder)
                viewHolder.itemView.setTag(R.id.adapter_swiping_support, true)
            }
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    /**
     * 手指松开还原的时候
     *
     * @param recyclerView
     * @param viewHolder
     */
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        if (adapter.isViewCreateByAdapter(viewHolder)) {
            return
        }
        if (viewHolder.itemView.getTag(R.id.adapter_dragging_support) != null
                && viewHolder.itemView.getTag(R.id.adapter_dragging_support) as Boolean) {
            adapter.onItemDragEnd(viewHolder)
            viewHolder.itemView.setTag(R.id.adapter_dragging_support, false)
        }
        if (viewHolder.itemView.getTag(R.id.adapter_swiping_support) != null
                && viewHolder.itemView.getTag(R.id.adapter_swiping_support) as Boolean) {
            adapter.onItemSwipeClear(viewHolder)
            viewHolder.itemView.setTag(R.id.adapter_swiping_support, false)
        }
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return if (adapter.isViewCreateByAdapter(viewHolder)) {
            makeMovementFlags(0, 0)
        } else super.getMovementFlags(recyclerView, viewHolder)
    }

    override fun onMove(recyclerView: RecyclerView, source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return adapter.onItemDragMoving(source, target)
    }

    /**
     * 当用户左右滑动Item达到删除条件时，会调用该方法，
     * 一般手指触摸滑动的距离达到RecyclerView宽度的一半时，再松开手指，此时该Item会继续向原先滑动方向滑过去并且调用onSwiped方法进行删除，否则会反向滑回原来的位置。在该方法内部我们可以这样写：
     *
     * @param viewHolder
     * @param direction
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (!adapter.isViewCreateByAdapter(viewHolder)) {
            adapter.onItemSwiped(viewHolder)
        }
    }

    override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                 dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE
                && !adapter.isViewCreateByAdapter(viewHolder)) {
            val itemView = viewHolder.itemView
            c.save()
            if (dX > 0) {
                c.clipRect(itemView.left.toFloat(), itemView.top.toFloat(),
                        itemView.left + dX, itemView.bottom.toFloat())
                c.translate(itemView.left.toFloat(), itemView.top.toFloat())
            } else {
                c.clipRect(itemView.right + dX, itemView.top.toFloat(),
                        itemView.right.toFloat(), itemView.bottom.toFloat())
                c.translate(itemView.right + dX, itemView.top.toFloat())
            }
            adapter.onItemSwiping(c, viewHolder, dX, dY, isCurrentlyActive)
            c.restore()
        }
    }
}