package com.ashlikun.adapter.recyclerview.vlayout.mode

import android.view.View
import com.ashlikun.adapter.recyclerview.vlayout.OnAdapterEvent

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
        val type: String = "",
        //处理adapter发出的事件,在创建adapter的时候会赋值
        var event: Map<String, OnAdapterEvent>? = null,
        //创建Adapter回调的其他参数，一般用于改变UI,如果data里面也有设置那么就是合并
        var params: Map<String, Any>? = null
) {
    /**
     * 处理adapter发出的事件,在创建adapter的时候会赋值
     * @param action 动作
     * @param event 事件回调
     */
    fun addOnEvent(action: String, event: OnAdapterEvent) {
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
        //间距,一般用MarginStyle
        const val manage = "manage"
    }
}

/**
 * 边距的样式
 */
class MarginStyle {
    var paddingLeft = 0f
    var paddingRight = 0f
    var paddingTop = 0f
    var paddingBottom = 0f

    var marginLeft = 0f
    var marginRight = 0f
    var marginTop = 0f
    var marginBottom = 0f


    fun margin(margin: Float) {
        marginLeft = margin
        marginRight = margin
        marginTop = margin
        marginBottom = margin
    }

    fun padding(padding: Float) {
        paddingLeft = padding
        paddingRight = padding
        paddingTop = padding
        paddingBottom = padding
    }
}
//文字大小
//文字颜色
//图片大小
//图片圆角
//