package com.ashlikun.adapter.recyclerview.multiltem

import android.content.Context
import com.ashlikun.adapter.recyclerview.vlayout.SingAdapter
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.ashlikun.adapter.recyclerview.AdapterConvert
import com.ashlikun.adapter.recyclerview.vlayout.mode.AdapterBus
import com.ashlikun.adapter.recyclerview.vlayout.mode.LayoutStyle

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/5 1:10
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：VLayout的ItemAdapter
 * 多表单的内部adapter,可以有多个不同的type,
 * 第一步在构造方法addItemType关联id
 */

open class MultipleSingAdapter<T>(
    context: Context,
    initDatas: List<T>? = null,
    //事件
    override var bus: AdapterBus? = null,
    //data对应的type
    open var itemType: ((position: Int, data: T) -> Int)? = null,
    //布局，优先
    layoutStyle: LayoutStyle? = null,
    //ViewType
    override var viewType: Any? = null,
    //初始化的apply 便于执行其他代码
    apply: (MultipleSingAdapter<T>.() -> Unit)? = null,
    //转换
    override var convert: AdapterConvert<T>? = null
) : SingAdapter<T>(
    context = context,
    initDatas = initDatas,
    layoutStyle = layoutStyle
) {
    //ItemType对应的binding 多个
    open var bindings: MutableMap<Int, Class<out ViewBinding>> = mutableMapOf()

    //ItemType对应的LayoutId
    open var layouts: MutableMap<Int, Int> = mutableMapOf()

    init {
        apply?.invoke(this)
    }

    override fun getItemViewType(position: Int): Int {
        var data: T = getItemData(position) ?: return super.getItemViewType(position)
        return getItemViewType(position, data)
    }

    /** 关联 viewType与layoutResId
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

    override fun getLayoutId(viewType: Int): Int {
        return layouts[viewType] ?: TYPE_NOT_FOUND
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
        itemType?.invoke(position, data) ?: throw RuntimeException("必须提供itemtype")

    companion object {
        const val TYPE_NOT_FOUND = -404
        const val DEFAULT_VIEW_TYPE = -0xff
    }
}