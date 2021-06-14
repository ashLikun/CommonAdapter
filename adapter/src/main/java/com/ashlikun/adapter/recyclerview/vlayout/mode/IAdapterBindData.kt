package com.ashlikun.adapter.recyclerview.vlayout.mode

/**
 * 作者　　: 李坤
 * 创建时间: 2021/6/9　17:54
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：数据层实现的接口
 */
interface IAdapterBindData {
    /**
     * 获取适配类型
     */
    fun getType(): String

    /**
     * 获取对于类型的data
     */
    fun getData(): Any

    /**
     * Adapter与外界交互的参数集合,这里的AdapterBus 不需要type,内部自动赋值
     */
    open fun getBus(): AdapterBus? {
        return null
    }



}
