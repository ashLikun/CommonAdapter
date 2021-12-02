package com.ashlikun.adapter

import com.ashlikun.adapter.recyclerview.BaseAdapter
import kotlin.jvm.JvmOverloads
import java.util.ArrayList

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/2 22:20
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：adapter数据处理
 */

class DataHandle<T>(
    var data: MutableList<T>, var adapter: BaseAdapter<*, *>
) {
    val itemCount: Int
        get() = data.size
    val isEmpty: Boolean
        get() = itemCount == 0

    /**
     * 获取position对应的数据
     */
    fun getItemData(position: Int) = data.getOrNull(position)

    /**
     * 设置新的数据源
     *
     * @param isNotify 是否通知适配器刷新
     */
    fun setDatas(datas: MutableList<T>?, isNotify: Boolean = false) {
        data = datas ?: mutableListOf()
        if (isNotify) {
            adapter.notifyDataSetChanged()
        }
    }

    /**
     * 第一个前面添加数据
     *
     * @param isNotify 是否通知适配器刷新
     */
    fun addFirstDatas(datas: List<T>?, isNotify: Boolean = false) {
        addDatas(datas, false, isNotify)
    }

    /**
     * 添加数据
     *
     * @param isNotify 是否通知适配器刷新
     */
    fun addDatas(datas: List<T>?, isNotify: Boolean = false) {
        addDatas(datas, true, isNotify)
    }

    /**
     * 添加数据
     *
     * @param isNotify 是否通知适配器刷新
     * @param isEnd    是否从末尾插入
     */
    fun addDatas(datas: List<T>?, isEnd: Boolean = false, isNotify: Boolean = false) {
        if (datas.isNullOrEmpty()) {
            return
        }
        val size = itemCount
        if (isEnd) data.addAll(datas) else data.addAll(0, datas)
        if (isNotify) {
            if (size > 0) {
                if (isEnd) {
                    adapter.notifyItemRangeInserted(size + adapter.headerSize, datas.size)
                } else {
                    adapter.notifyItemRangeInserted(adapter.headerSize, datas.size)
                }
            } else {
                adapter.notifyDataSetChanged()
            }
        }
    }

    /**
     * 移除数据
     *
     * @param isNotify 是否通知适配器刷新
     */
    fun removeData(deleteData: T?, isNotify: Boolean = false) {
        if (deleteData == null) {
            return
        }
        val indexOf = data.indexOf(deleteData)
        if (data.remove(deleteData)) {
            if (isNotify) {
                adapter.notifyItemRemoved(indexOf)
            }
        }
    }

    /**
     * 清空全部数据
     *
     * @param isNotify 是否通知适配器刷新
     */
    fun clearData(isNotify: Boolean = false) {
        val size = itemCount
        data.clear()
        if (isNotify && size > 0) {
            adapter.notifyItemRangeRemoved(0, size)
        }
    }
}