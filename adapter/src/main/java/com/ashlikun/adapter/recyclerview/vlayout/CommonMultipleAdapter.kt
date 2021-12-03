package com.ashlikun.adapter.recyclerview.vlayout

import androidx.lifecycle.LifecycleObserver
import androidx.recyclerview.widget.RecyclerView
import com.ashlikun.adapter.recyclerview.vlayout.SingAdapter

/**
 * 作者　　: 李坤
 * 创建时间: 2020/5/21　11:16
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：[MultipleAdapterHelp]的简化版
 */
open class CommonMultipleAdapter(recyclerView: RecyclerView)
    : MultipleAdapterHelp(recyclerView) {
    fun bindUi(vararg adapters: SingAdapter<*>): CommonMultipleAdapter {
        adapter.addAdapters(adapters.toMutableList())
        return this
    }
}