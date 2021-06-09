package com.ashlikun.adapter.recyclerview.vlayout

import androidx.recyclerview.widget.RecyclerView
import com.ashlikun.adapter.recyclerview.vlayout.mode.IAdapterBindData

/**
 * 作者　　: 李坤
 * 创建时间: 2021/6/9　15:30
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：全局管理Adapter
 */
object AdapterManage {
    /**
     * 管理的Adapter
     */
    val adapterMapper = mutableMapOf<String, Class<RecyclerView.Adapter<*>>>()

    fun add(type: String, cls: Class<RecyclerView.Adapter<*>>) {
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
            adapter.addAdapter()
            val cls = adapterMapper[it.getType()]
            var adapter :
            if(cls != null){
                //实例化适配器

            }
        }
    }
}