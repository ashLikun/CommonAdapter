package com.ashlikun.adapter.recyclerview.group

import kotlin.math.max


/**
 * @author　　: 李坤
 * 创建时间: 2021/12/5 1:12
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：这个类是用来记录分组列表中组的结构的。
 * 通过GroupStructure记录每个组是否有头部，是否有尾部和子项的数量。从而能方便的计算
 * 列表的长度和每个组的组头、组尾和子项在列表中的位置。
 */

class GroupStructure(
    hasHeader: Boolean,
    hasFooter: Boolean,
    childrenStart: Int,
    childrenCount: Int
) {
    var hasHeader = false
        set(value) {
            if (field && !value) childrenStart -= 1
            else if (value) childrenStart += 1
            field = value
        }
    var hasFooter = false
        set(value) {
            childrenEnd = max(0, childrenStart + childrenCount - 1)
            field = value
        }
    var childrenCount = 0
        set(value) {
            field = value
            childrenEnd = max(0, childrenStart + value - 1)

        }
    var childrenStart = 0
        set(value) {
            field = value
            childrenEnd = max(0, value + childrenCount - 1)
        }
    var childrenEnd = 0
        private set

    init {
        this.hasHeader = hasHeader
        this.hasFooter = hasFooter
        if (hasHeader) {
            this.childrenStart = childrenStart + 1
        } else {
            this.childrenStart = childrenStart
        }
        this.childrenCount = childrenCount
        childrenEnd = max(0, this.childrenStart + this.childrenCount - 1)
    }


    val start: Int
        get() = if (hasHeader) childrenStart - 1 else childrenStart
    val end: Int
        get() = if (hasFooter) childrenEnd + 1 else childrenEnd

    /**
     * 计算一个组里有多少个Item（头加尾加子项）
     *
     * @return
     */
    fun countGroupItem(): Int {
        var itemCount = 0
        if (hasHeader) {
            itemCount += 1
        }
        itemCount += childrenCount
        if (hasFooter) {
            itemCount += 1
        }
        return itemCount
    }

    fun containsPosition(position: Int): Boolean {
        return position in start..end
    }

    /**
     * 判断item的type 头部 尾部 和 子项
     *
     * @param position
     * @return
     */
    fun containsPositionType(position: Int): Int {
        if (hasHeader) {
            if (position <= start) {
                return GroupedCommonAdapter.TYPE_HEADER
            }
        }
        if (position <= childrenEnd) {
            return GroupedCommonAdapter.TYPE_CHILD
        }
        if (hasFooter) {
            if (position <= end) {
                return GroupedCommonAdapter.TYPE_FOOTER
            }
        }
        return -1
    }

    /**
     * 根据下标计算position在组中位置（childPosition）
     *
     * @param position 下标
     * @return 子项下标 childPosition
     */
    fun getChildPositionForPosition(position: Int): Int {
        return if (containsPosition(position)) {
            position - childrenStart
        } else -1
    }


}