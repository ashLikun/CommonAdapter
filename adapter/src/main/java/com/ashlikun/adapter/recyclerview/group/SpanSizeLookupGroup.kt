package com.ashlikun.adapter.recyclerview.group

import androidx.recyclerview.widget.GridLayoutManager
import com.ashlikun.adapter.recyclerview.group.GroupedCommonAdapter

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/5 1:25
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：头部沾满一行
 */

class SpanSizeLookupGroup(var manager: GridLayoutManager, var adapter: GroupedCommonAdapter<*>) :
    GridLayoutManager.SpanSizeLookup() {
    var old: GridLayoutManager.SpanSizeLookup = manager.spanSizeLookup

    override fun getSpanSize(position: Int): Int {
        val type = adapter.judgePositionToType(position)
        return if (type == GroupedCommonAdapter.TYPE_HEADER || type == GroupedCommonAdapter.TYPE_FOOTER) {
            //占满全行
            manager.spanCount
        } else {
            if (old != null) {
                //使用之前设置过的 并且positions是去除头部后的位置
                old.getSpanSize(position)
            } else {
                1
            }
        }
    }


}