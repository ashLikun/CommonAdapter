package com.ashlikun.adapter.recyclerview.vlayout

import android.content.Context
import com.ashlikun.adapter.recyclerview.vlayout.mode.IAdapterBindData

/**
 * 作者　　: 李坤
 * 创建时间: 2021/6/9　15:30
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：全局管理Adapter
 */
/**
 * 创建Adapter的回调
 */
typealias OnAdapterCreate = (OnAdapterCreateParams) -> SingAdapter<*>
/**
 *
 */
typealias OnAdapterEvent = (Map<String, Any>) -> Boolean

/**
 * 创建适配器的参数
 */
data class OnAdapterCreateParams(
        val context: Context,
        //总的适配器
        val adapter: MultipleAdapter,
        //数据
        val data: Any,
        //其他参数，一般用于改变UI
        val params: Map<String, Any>? = null
) {
}

object AdapterManage {
    /**
     * Adapter创建的回调,由外部创建Adapter
     */
    private val adapterCreates = mutableListOf<OnAdapterCreate>()

    /**
     * Adapter的事件回调
     */
    private val adapterEvent = mutableListOf<OnAdapterEvent>()

    fun addCreate(create: OnAdapterCreate) {
        if (!adapterCreates.contains(create)) {
            adapterCreates.add(create)
        }
    }

    fun addCreates(creates: List<OnAdapterCreate>) {
        creates.forEach {
            addCreate(it)
        }
    }

    fun removeCreate(create: OnAdapterCreate) {
        adapterCreates.remove(create)
    }

    fun removeCreates(creates: List<OnAdapterCreate>) {
        adapterCreates.removeAll(creates)
    }

    /**
     * 绑定数据
     * @param otherParams 创建这个Adapter需要的额外参数，一般用于改变UI,优先级没有data里面的getParams 权限高
     */
    fun bindUi(
            context: Context, adapter: MultipleAdapter, data: List<IAdapterBindData>,
            otherParams: Map<String, Map<String, Any>>? = null,
            onEvent: Map<String, OnAdapterEvent>? = null
    ) {
        data.forEach {
            adapterCreates.forEach { ait ->
                val params = OnAdapterCreateParams(
                        context = context,
                        adapter = adapter,
                        data = it.getData(),
                        //其他参数，先用data的，如果为null，再用方法的
                        params = it.getParams() ?: otherParams?.get(it.getType())
                )
                val ada = ait.invoke(params)
                //默认赋值
                if (ada.params == null) {
                    ada.params = params.params
                }
                //设置事件管理
                ada.onEvent = onEvent
                //添加之前
                ada.beforeAdd()
                //添加
                adapter.addAdapter(ada)
                //添加之后
                ada.afterAdd()
            }
        }
    }
}