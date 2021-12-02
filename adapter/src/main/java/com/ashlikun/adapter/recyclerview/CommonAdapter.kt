package com.ashlikun.adapter.recyclerview

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import com.ashlikun.adapter.ViewHolder
import com.ashlikun.adapter.recyclerview.vlayout.mode.AdapterBus
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
abstract class CommonAdapter<T>(
    override var context: Context,
    //布局文件
    override var layoutId: Int = DEFAULT_LAYOUT_ID,
    //创建ViewBinding的Class,与layoutId 二选一
    override var viewBindingClass: Class<*>? = null,
    datas: MutableList<T>
) : BaseAdapter<T, ViewHolder>(context, layoutId, viewBindingClass, datas) {

    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = ViewHolder(context, createRoot(parent, getLayoutId(viewType), viewType), this)
        setListener(holder, viewType)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setListener(holder, holder.itemViewType)
        convert(holder, getItemData(position))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads != null && payloads.isNotEmpty()) {
            if (!convert(holder, getItemData(position), payloads)) {
                super.onBindViewHolder(holder, position, payloads)
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun dispatchClick(v: View, viewHolder: ViewHolder, viewType: Int) {
        if (!sendEvent(AdapterBus.ITEM_CLICK)) {
            super.dispatchClick(v, viewHolder, viewType)
        }
    }

    override fun dispatchLongClick(v: View, viewHolder: ViewHolder, viewType: Int): Boolean {
        if (!sendEvent(AdapterBus.ITEM_LONG_CLICK)) {
            return super.dispatchLongClick(v, viewHolder, viewType)
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
        return event<OnAdapterEvent>(action)?.invoke(params) ?: false
    }

    /**
     * 注册事件，由外部处理,一般是由某个界面处理
     * @param action 事件的类型
     * @param eve 这个事件对应回调
     */
    open fun putEvent(action: String, eve: OnAdapterEvent): CommonAdapter<T> {
        if (bus == null) {
            bus = AdapterBus()
        }
        bus?.putEvent(action, eve)
        return this
    }

    /**
     * 添加参数，由外部处理,一般是由某个界面处理
     * @param key 参数类型
     * @param eve 这个数对应的参数
     */
    open fun putParam(key: String, eve: Any): CommonAdapter<T> {
        if (bus == null) {
            bus = AdapterBus()
        }
        bus?.putParam(key, eve)
        return this
    }

    /**
     * 获取Bus里面的key
     */
    open fun <T : OnAdapterEvent> event(key: String): T? = bus?.event(key)

    /**
     * 获取AdapterBus.STYLE
     */
    open val style
        get() = bus?.style

    /**
     * 获取AdapterBus.STYLE
     */
    open fun params(key: String) = bus?.get<Any>(key)
}