package com.ashlikun.adapter.recyclerview

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.ashlikun.adapter.ViewHolder
import com.ashlikun.adapter.recyclerview.vlayout.mode.AdapterBus
import com.ashlikun.adapter.recyclerview.vlayout.mode.AdapterStyle
import com.ashlikun.adapter.recyclerview.vlayout.mode.OnAdapterEvent

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/25 9:56
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：公共的RecycleView的adapter
 * 在BaseAdapter基础上封装了 onCreateViewHolder,onBindViewHolder
 */
abstract class CommonAdapter<T>
@JvmOverloads constructor(context: Context, layoutId: Int = -1, datas: List<T>? = null)
    : BaseAdapter<T, ViewHolder?>(context, layoutId, datas) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = ViewHolder(mContext, createLayout(parent, getLayoutId(viewType), viewType), this)
        setListener(parent, holder, viewType)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setListener(recyclerView, holder, holder.itemViewType)
        convert(holder, getItemData(position))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: List<Any>) {
        if (payloads != null && !payloads.isEmpty()) {
            if (!convert(holder, getItemData(position), payloads)) {
                super.onBindViewHolder(holder, position, payloads)
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun dispatchClick(parent: ViewGroup, v: View, viewHolder: ViewHolder, viewType: Int) {
        if (!sendEvent(AdapterBus.CLICK)) {
            super.dispatchClick(parent, v, viewHolder, viewType)
        }
    }

    override fun dispatchLongClick(parent: ViewGroup, v: View, viewHolder: ViewHolder, viewType: Int): Boolean {
        if (!sendEvent(AdapterBus.LONG_CLICK)) {
            return super.dispatchLongClick(parent, v, viewHolder, viewType)
        }
        return true
    }

    /**
     * 发送事件，由外部处理,一般是由某个界面处理
     * @param action 事件的类型
     * @param params 这个事件对应的参数
     * @return 是否处理，true：外部已经处理，内部就不处理了  ，，， false:未处理
     */
    open fun sendEvent(action: String, params: Map<String, Any> = emptyMap()): Boolean {
        return busEvent<OnAdapterEvent>(action)?.invoke(params) ?: false
    }

    /**
     * 获取Bus里面的key
     */
    open fun <T : OnAdapterEvent> busEvent(key: String): T? = bus?.busEvent(key)

    /**
     * 获取AdapterBus.STYLE
     */
    open val busStyle
        get() = bus?.busStyle

    /**
     * 获取AdapterBus.STYLE
     */
    open fun busParams(key: String) = bus?.get<Any>(key)
}