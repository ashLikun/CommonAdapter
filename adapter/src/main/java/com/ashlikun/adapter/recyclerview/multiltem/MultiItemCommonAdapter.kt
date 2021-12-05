package com.ashlikun.adapter.recyclerview.multiltem

import android.content.Context
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
    override var context: Context,
    datas: MutableList<T>,
    //ItemType对应的binding 多个
    open var bindingClasss: MutableMap<Int, Class<out ViewBinding>> = hashMapOf(),
    //ItemType对应的LayoutId
    open var layouts: MutableMap<Int, Int> = hashMapOf(),
    //事件
    override var bus: AdapterBus? = null,
    //data对应的type
    open var itemType: ((position: Int, data: T) -> Int)? = null,
    //转换
    override var convert: AdapterConvert<T>? = null
) : CommonAdapter<T>(
    context = context, datas = datas, bus = bus, convert = convert
) {

    override fun getItemViewType(position: Int): Int {
        var data: T = getItemData(position) ?: return super.getItemViewType(position)
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
        bindingClasss[viewType] = viewBindClass
    }

    override fun getLayoutId(viewType: Int): Int {
        return layouts[viewType] ?: TYPE_NOT_FOUND
    }

    override fun getBindClass(viewType: Int): Class<out ViewBinding>? {
        return bindingClasss[viewType]
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
        itemType?.invoke(position, data) ?: throw RuntimeException("必须提供itemtype")

    companion object {
        const val TYPE_NOT_FOUND = -404
        const val DEFAULT_VIEW_TYPE = -0xff
    }
}