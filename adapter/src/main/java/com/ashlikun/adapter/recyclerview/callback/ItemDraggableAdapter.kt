package com.ashlikun.adapter.recyclerview.callback

import android.content.Context
import android.graphics.Canvas
import android.view.View
import com.ashlikun.adapter.recyclerview.CommonAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.ashlikun.adapter.ViewHolder
import com.ashlikun.adapter.recyclerview.AdapterConvert
import java.util.*
import kotlin.reflect.KClass

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.3 15:42
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：实现拖拽功能的adapter
 */

open class ItemDraggableAdapter<T>(
    context: Context,
    initDatas: List<T>? = null,
    //创建ViewBinding的Class,与layoutId 二选一
    override var bindingClass: Class<out ViewBinding>? = null,
    override var layoutId: Int? = null,
    //初始化的apply 便于执行其他代码
    apply: (ItemDraggableAdapter<T>.() -> Unit)? = null,
    /**
     * 这个holder是不是内部自己创建的，
     * @return true 没有任何触摸事件
     */
    open var isViewCreateByAdapter: Boolean = false,
    //转换
    override var convert: AdapterConvert<T>? = null
) : CommonAdapter<T>(
    context = context,
    initDatas = initDatas
), ItemTouchHelperAdapter {

    var onItemDragListener: OnItemDragListener? = null
    var onItemSwipeListener: OnItemSwipeListener? = null
    override var isItemSwipeEnable = false;
    override var isItemDraggable = false;

    init {
        apply?.invoke(this)
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

    override fun onItemDragMoving(
        source: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
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

    override fun onItemSwiping(
        canvas: Canvas,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        isCurrentlyActive: Boolean
    ) {
        if (isItemSwipeEnable) {
            onItemSwipeListener?.onItemSwipeMoving(canvas, viewHolder, dX, dY, isCurrentlyActive)
        }
    }

    override fun isViewCreateByAdapter(viewHolder: RecyclerView.ViewHolder) = isViewCreateByAdapter

    private fun inRange(position: Int): Boolean {
        return position >= 0 && position < dataHandle.itemCount
    }
}