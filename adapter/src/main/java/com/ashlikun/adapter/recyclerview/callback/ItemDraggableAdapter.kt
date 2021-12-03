package com.ashlikun.adapter.recyclerview.callback

import android.content.Context
import android.graphics.Canvas
import android.view.View
import com.ashlikun.adapter.recyclerview.CommonAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ashlikun.adapter.ViewHolder
import java.util.*

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.3 15:42
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：实现拖拽功能的adapter
 */

abstract class ItemDraggableAdapter<T>(
        override var context: Context,
        override var layoutId: Int = View.NO_ID,
        //创建ViewBinding的Class,与layoutId 二选一
        override var bindingClass: Class<*>? = null, data: MutableList<T>)
    : CommonAdapter<T>(context, layoutId, bindingClass, data), ItemTouchHelperAdapter {

    var onItemDragListener: OnItemDragListener? = null
    var onItemSwipeListener: OnItemSwipeListener? = null

    init {
        isItemDraggable = false
        isItemDraggable = false
    }

    fun getViewHolderPosition(viewHolder: RecyclerView.ViewHolder): Int {
        return if (viewHolder is ViewHolder) {
            viewHolder.positionInside
        } else viewHolder.layoutPosition
    }

    override fun onItemDragStart(viewHolder: RecyclerView.ViewHolder) {
        if (isItemDraggable) {
            onItemDragListener?.onItemDragStart(viewHolder, getViewHolderPosition(viewHolder))
        }
    }

    //必须这样，不然会抖动
    override fun getItemId(position: Int): Long {
        val d = getItemData(position)
        return d?.hashCode()?.toLong() ?: 0
    }

    override fun onItemDragMoving(source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        val from = getViewHolderPosition(source)
        val to = getViewHolderPosition(target)
        if (inRange(from) && inRange(to)) {
            if (from < to) {
                for (i in from until to) {
                    Collections.swap(datas, i, i + 1)
                }
            } else {
                for (i in from downTo to + 1) {
                    Collections.swap(datas, i, i - 1)
                }
            }
            notifyItemMoved(source.adapterPosition, target.adapterPosition)
        }
        if (isItemDraggable) {
            onItemDragListener?.onItemDragMoving(source, from, target, to)
        }
        return true
    }

    override fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder) {
        if (isItemDraggable) {
            onItemDragListener?.onItemDragEnd(viewHolder, getViewHolderPosition(viewHolder))
        }
    }

    override fun onItemSwipeStart(viewHolder: RecyclerView.ViewHolder) {
        if (isItemSwipeEnable) {
            onItemSwipeListener?.onItemSwipeStart(viewHolder, getViewHolderPosition(viewHolder))
        }
    }

    override fun onItemSwipeClear(viewHolder: RecyclerView.ViewHolder) {
        if (isItemSwipeEnable) {
            onItemSwipeListener?.clearView(viewHolder, getViewHolderPosition(viewHolder))
        }
    }

    override fun onItemSwiped(viewHolder: RecyclerView.ViewHolder) {
        val pos = getViewHolderPosition(viewHolder)
        if (inRange(pos)) {
            dataHandle.removeData(getItemData(pos))
            notifyItemRemoved(viewHolder.adapterPosition)
        }
        if (isItemSwipeEnable) {
            onItemSwipeListener?.onItemSwiped(viewHolder, getViewHolderPosition(viewHolder))
        }
    }

    override fun onItemSwiping(canvas: Canvas, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, isCurrentlyActive: Boolean) {
        if (isItemSwipeEnable) {
            onItemSwipeListener?.onItemSwipeMoving(canvas, viewHolder, dX, dY, isCurrentlyActive)
        }
    }

    private fun inRange(position: Int): Boolean {
        return position >= 0 && position < dataHandle.itemCount
    }
}