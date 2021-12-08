package com.ashlikun.adapter.recyclerview

import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.vlayout.layout.GridLayoutHelper

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/5 1:25
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：头部沾满一行
 */

open class SpanSizeLookupGroup(
    var manager: GridLayoutManager,
    var isOne: (position: Int) -> Boolean
) :
    GridLayoutManager.SpanSizeLookup() {
    var old: GridLayoutManager.SpanSizeLookup? = manager.spanSizeLookup

    override fun getSpanSize(position: Int): Int {
        return if (isOne(position)) {
            //占满全行
            manager.spanCount
        } else {
            //使用之前设置过的 并且positions是去除头部后的位置
            old?.getSpanSize(position) ?: 1
        }
    }
}

open class SpanSizeLookupGroupLayoutHelper(
    var manager: GridLayoutHelper,
    var isOne: (position: Int) -> Boolean
) : GridLayoutHelper.SpanSizeLookup() {
    var old: GridLayoutHelper.SpanSizeLookup? = null

    init {
        try {
            val mSpanSizeLookup = GridLayoutHelper::class.java.getDeclaredField("mSpanSizeLookup")
            mSpanSizeLookup.isAccessible = true
            old = mSpanSizeLookup.get(manager) as GridLayoutHelper.SpanSizeLookup
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getSpanSize(position: Int): Int {
        Log.e("111111111", "ddddddddd")
        return if (isOne(position)) {
            //占满全行
            manager.spanCount
        } else {
            //使用之前设置过的 并且positions是去除头部后的位置
            old?.getSpanSize(position) ?: 1
        }
    }
}