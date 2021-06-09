package com.ashlikun.adapter.recyclerview.vlayout

import androidx.recyclerview.widget.RecyclerView
import com.ashlikun.adapter.AdapterUtils
import com.ashlikun.adapter.recyclerview.vlayout.mode.IAdapterBindData
import kotlin.reflect.KClass

/**
 * 作者　　: 李坤
 * 创建时间: 2021/6/9　15:30
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：全局管理Adapter
 */
typealias OnAdapterCreate = () -> Unit

object AdapterManage {
    /**
     * 管理的Adapter
     */
    val adapterMapper = mutableMapOf<String, OnAdapterCreate>()

    fun add(type: String, cls: KClass<SingAdapter<*>>) {
        adapterMapper[type] = cls
    }

    fun remove(type: String) {
        adapterMapper.remove(type)
    }

    /**
     * 绑定数据
     */
    fun bindUi(adapter: MultipleAdapter, data: List<IAdapterBindData>) {
        data.forEach {
            val cls = adapterMapper[it.getType()]
            var ada: SingAdapter<*>
            if (cls != null) {
                //实例化适配器
                cls.constructors.forEach {
                    it.call()
                }
                adapter.addAdapter(ada)
            } else {
                //数据丢失
            }

        }
    }
}