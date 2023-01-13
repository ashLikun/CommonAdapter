package com.ashlikun.adapter.recyclerview.vlayout

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.ashlikun.vlayout.VirtualLayoutManager
import com.ashlikun.adapter.AdapterUtils
import com.ashlikun.adapter.recyclerview.SimpleLifecycleObserver
import com.ashlikun.adapter.recyclerview.common.CommonAdapter
import com.ashlikun.adapter.recyclerview.vlayout.mode.AdapterBus
import com.ashlikun.adapter.recyclerview.vlayout.mode.IAdapterBindData
import java.util.*

/**
 * 作者　　: 李坤
 * 创建时间: 2018/5/29 0029　上午 10:26
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：MultipleAdapter 的帮助类
 * @param hasConsistItemType 子适配器项类型是否一致,可以复用相同的viewtype
 * @param bus.onEvent 处理adapter发出的事件,在创建adapter的时候会赋值  key:数据层的type，value：这个type对应的事件（key/value）
 * @param bus.otherParams 创建Adapter回调的其他参数，一般用于改变UI,   key:数据层的type，value：这个type对应的参数（key/value）
 */
open class MultipleAdapterHelp(
    var recyclerView: RecyclerView,
    var hasConsistItemType: Boolean = true,
    //监听事件和ui参数的总线
    bus: List<AdapterBus>? = null,
) : SimpleLifecycleObserver {
    //event 处理adapter发出的事件,在创建adapter的时候会赋值  key:数据层的type，value：这个type对应的事件（key/value）
    //params 创建Adapter回调的其他参数，一般用于改变UI,   key:数据层的type，value：这个type对应的参数（key/value）
    var busMap: MutableMap<String, AdapterBus>? = null
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

        layoutManager = MultipleLayoutManage(context)
        this.adapter = MultipleAdapter(layoutManager, hasConsistItemType)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        bus?.forEach {
            busMap?.put(it.type, it)
        }
    }

    /**
     * 添加监听事件和ui参数的总线
     */
    fun addBus(adapterBus: AdapterBus) {
        if (busMap == null) {
            busMap = mutableMapOf()
        }
        busMap?.put(adapterBus.type, adapterBus)
    }

    /**
     * 添加监听事件和ui参数的总线
     */
    fun addBus(bus: Map<String, AdapterBus>) {
        if (busMap == null) {
            busMap = mutableMapOf()
        }
        busMap?.putAll(bus)
    }

    fun removeBus(type: String) {
        busMap?.remove(type)
    }

    open fun addObserver(observer: LifecycleObserver?) {
        if (observer != null) {
            lifecycle?.addObserver(observer)
        }
    }

    open fun isEmpty() = adapter.itemCount == 0

    /**
     * 绑定数据
     * @param data 要绑定的数据集合
     * @param bus 监听和额外参数
     */
    open fun bindUi(data: List<IAdapterBindData<*>>, bus: Map<String, AdapterBus>? = null) {
        if (bus != null) {
            addBus(bus)
        }
        AdapterBind.bindUi(context, adapter, data, busMap)
    }

    /**
     * 清空数据
     */
    open fun clearData(isScrollTop: Boolean = true) {
        adapter.clear()
        //如果使用动态(个数不一致)addAddpter，那么这里要重新设置adapter，不然RecyclerView的缓存会使Position错乱，点击事件错乱
        recyclerView.adapter = adapter
        if (isScrollTop) {
            recyclerView.scrollToPosition(0)
        }
    }

    fun addAdapters(adapters: List<CommonAdapter<*>>?, index: Int = adapter.adaptersCount) = adapter.addAdapters(adapters, index)
    fun addAdapters(vararg adapters: CommonAdapter<*>, index: Int = adapter.adaptersCount) = adapter.addAdapters(adapters.toList(), index)
    fun addAdapter(adapter: CommonAdapter<*>?, index: Int = this.adapter.adaptersCount) = this.adapter.addAdapter(adapter, index)
    fun removeFirstAdapter() = adapter.removeFirstAdapter()
    fun removeLastAdapter() = adapter.removeLastAdapter()
    fun removeAdapter(index: Int) = adapter.removeAdapter(index)
    fun removeAdapter(targetAdapter: CommonAdapter<*>?) = adapter.removeAdapter(targetAdapter)
    fun removeAdapters(targetAdapters: List<CommonAdapter<*>>?) = adapter.removeAdapters(targetAdapters)
}