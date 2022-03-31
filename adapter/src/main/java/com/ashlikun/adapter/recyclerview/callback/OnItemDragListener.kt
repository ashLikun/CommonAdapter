package com.ashlikun.adapter.recyclerview.callback

import androidx.recyclerview.widget.RecyclerView

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.3 15:50
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：item拖拽回调
 */

interface OnItemDragListener {
    fun onItemDragStart(viewHolder: RecyclerView.ViewHolder, pos: Int)
    fun onItemDragMoving(source: RecyclerView.ViewHolder, from: Int, target: RecyclerView.ViewHolder, to: Int): Boolean
    fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder, pos: Int)
}