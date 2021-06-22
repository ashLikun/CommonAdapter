package com.ashlikun.adapter.recyclerview.vlayout.mode

import android.view.View

/**
 * 作者　　: 李坤
 * 创建时间: 2021/6/11　15:33
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 */
/**
 * Adapter与外界交互的参数集合
 */
data class AdapterBus(
        //对应数据层的type
        var type: String = "",
        //处理adapter发出的事件,在创建adapter的时候会赋值
        var event: Map<String, OnAdapterEvent>? = null,
        //创建Adapter回调的其他参数，一般用于改变UI,如果data里面也有设置那么就是合并
        var params: Map<String, Any>? = null
) {
    /**
     * 不要设置type
     */
    constructor(
            event: Map<String, OnAdapterEvent>? = null,
            params: Map<String, Any>? = null) : this("", event, params)

    /**
     * 获取key对应的event或者params,优先event
     */
    fun <T> get(key: String): T? = (event?.get(key) ?: params?.get(key)) as T?

    /**
     * 获取AdapterBus.STYLE
     */
    open val style
        get() = get<AdapterStyle>(STYLE)

    /**
     * 获取AdapterBus.STYLE
     */
    open fun params(key: String) = get<Any>(key)

    /**
     * 添加事件
     */
    open fun putEvent(key: String, eve: OnAdapterEvent) {
        if (event == null) {
            event = hashMapOf()
        }
        event = event!! + mapOf(key to eve)
    }

    /**
     * 添加事件
     */
    open fun putParam(key: String, par: Any) {
        if (params == null) {
            params = hashMapOf()
        }
        params = params!! + mapOf(key to par)
    }

    /**
     * 获取Bus里面的Event
     */
    open fun <T : OnAdapterEvent> event(key: String) = get<OnAdapterEvent>(key) as T?

    /**
     * 处理adapter发出的事件,在创建adapter的时候会赋值
     * @param action 动作
     * @param event 事件回调
     */
    fun add(action: String, event: OnAdapterEvent) {
        this.event = if (this.event == null) mapOf(action to event) else this.event!! + mapOf(action to event)
    }

    /**
     * 加号运算符
     */
    operator fun plus(data: AdapterBus?): AdapterBus {
        return if (type == data?.type) {
            AdapterBus(type, (event ?: emptyMap()) + (data.event ?: emptyMap()),
                    (params ?: emptyMap()) + (data.params ?: emptyMap()))
        } else {
            val a: View
            this
        }
    }

    companion object {
        //常用的事件
        //item 点击事件
        const val ITEM_CLICK = "ITEM_CLICK"

        //普通的 点击事件
        const val CLICK = "CLICK"

        //item 长按事件
        const val ITEM_LONG_CLICK = "ITEM_LONG_CLICK"

        //普通的 长按事件
        const val LONG_CLICK = "LONG_CLICK"

        //常用的UI属性
        const val STYLE = "STYLE"
    }
}

