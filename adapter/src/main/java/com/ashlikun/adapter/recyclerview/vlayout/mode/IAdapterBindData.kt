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
     * 适配器对应的其他数据
     */
    open fun getParams(): Map<String, Any>? {
        return null
    }
}