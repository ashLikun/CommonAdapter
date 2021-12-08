package com.ashlikun.adapter.recyclerview.multiltem

import android.content.Context
import android.view.View
import com.ashlikun.adapter.recyclerview.CommonAdapter
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding
import com.ashlikun.adapter.recyclerview.AdapterConvert
import com.ashlikun.adapter.recyclerview.vlayout.mode.AdapterBus

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/4 23:51
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：功能介绍：多种列表样式的适配器,简单版
 * 基本功能看 [BaseAdapter]类注释
 * 第一步在构造方法addItemType关联id
 */

open class MultiItemCommonAdapter<T>(
    context: Context,
    initDatas: List<T>? = null,
    //事件
    override var bus: AdapterBus? = null,
    //data对应的type
    open var itemType2: ((position: Int, data: T) -> Int)? = null,
    open var itemType: ((data: T) -> Int)? = null,
    //初始化的apply 便于执行其他代码
    apply: (MultiItemCommonAdapter<T>.() -> Unit)? = null,
    //转换
    override var convert: AdapterConvert<T>? = null
) : CommonAdapter<T>(
    context = context,
    initDatas = initDatas
) {
    //ItemType对应的binding 多个
    open var bindings: MutableMap<Int, Class<out ViewBinding>> = mutableMapOf()

    //ItemType对应的LayoutId
    open var layouts: MutableMap<Int, Int> = mutableMapOf()

    init {
        apply?.invoke(this)
    }

    override fun getItemViewType(position: Int): Int {
        var data = getItemData(position) ?: return super.getItemViewType(position)
        return getItemViewType(position, data)
    }

    /**
     * 关联 viewType与layoutResId
     *
     * @param viewType
     * @param layoutResId
     */
    fun addItemType(viewType: Int, @LayoutRes layoutResId: Int) {
        layouts[viewType] = layoutResId
    }

    /**
     * 关联 viewType与ViewBinding
     *
     * @param viewType
     * @param layoutResId
     */
    fun addItemType(viewType: Int, viewBindClass: Class<out ViewBinding>) {
        bindings[viewType] = viewBindClass
    }

    override fun getLayoutId(viewType: Int): Int? {
        return layouts[viewType]
    }

    override fun getBindClass(viewType: Int): Class<out ViewBinding>? {
        return bindings[viewType]
    }

    /**
     * 添加默认的view
     *
     * @param layoutResId
     */
    protected fun setDefaultViewTypeLayout(@LayoutRes layoutResId: Int) {
        addItemType(DEFAULT_VIEW_TYPE, layoutResId)
    }

    /**
     * 添加默认的view
     *
     * @param layoutResId
     */
    protected fun setDefaultViewTypeLayout(viewBindClass: Class<out ViewBinding>) {
        addItemType(DEFAULT_VIEW_TYPE, viewBindClass)
    }

    open fun getItemViewType(position: Int, data: T) =
        itemType2?.invoke(position, data) ?: itemType?.invoke(data)
        ?: throw RuntimeException("必须提供itemtype")

    companion object {
        const val DEFAULT_VIEW_TYPE = -0xff
    }
}