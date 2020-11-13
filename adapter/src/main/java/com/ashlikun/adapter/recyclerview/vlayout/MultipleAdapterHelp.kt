package com.ashlikun.adapter.recyclerview.vlayout

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.ashlikun.adapter.AdapterUtils
import com.ashlikun.adapter.recyclerview.vlayout.MultipleAdapter

/**
 * 作者　　: 李坤
 * 创建时间: 2018/5/29 0029　上午 10:26
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：MultipleAdapter 的帮助类
 * @param hasConsistItemType 子适配器项类型是否一致,可以复用相同的viewtype
 */
open class MultipleAdapterHelp
@JvmOverloads constructor(var recyclerView: RecyclerView,
                          hasConsistItemType: Boolean = true) : LifecycleObserver {
    var adapter: MultipleAdapter
        protected set
    var context: Context
        protected set
    var lifecycle: Lifecycle? = null
    var layoutManager: VirtualLayoutManager
        protected set

    init {
        this.context = recyclerView.context
        if (context is LifecycleOwner) {
            lifecycle = (context as LifecycleOwner).lifecycle
        } else {
            val activity = AdapterUtils.getActivity(context)
            if (activity is LifecycleOwner) {
                lifecycle = (activity as LifecycleOwner).lifecycle
            }
        }

        layoutManager = VirtualLayoutManager(context)
        this.adapter = MultipleAdapter(layoutManager, hasConsistItemType)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    fun addObserver(observer: LifecycleObserver?) {
        if (observer != null) {
            lifecycle?.addObserver(observer)
        }
    }

    fun isEmpty() = adapter.itemCount == 0

    /**
     * 清空数据
     */
    fun clearData(isScrollTop: Boolean = true) {
        adapter.clear()
        //如果使用动态(个数不一致)addAddpter，那么这里要重新设置adapter，不然RecyclerView的缓存会使Position错乱，点击事件错乱
        recyclerView.adapter = adapter
        if (isScrollTop) {
            recyclerView.scrollToPosition(0)
        }
    }
}