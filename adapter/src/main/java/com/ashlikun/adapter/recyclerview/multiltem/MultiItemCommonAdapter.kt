package com.ashlikun.adapter.recyclerview.multiltem

import android.content.Context
import androidx.viewbinding.ViewBinding
import com.ashlikun.adapter.recyclerview.*
import com.ashlikun.adapter.recyclerview.common.CommonAdapter
import com.ashlikun.adapter.recyclerview.vlayout.mode.AdapterBus
import com.ashlikun.adapter.recyclerview.vlayout.mode.LayoutStyle

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
    //ItemType对应的binding 多个
    open var bindings: MutableMap<Int, Class<out ViewBinding>> = mutableMapOf(),
    //data对应的type
    open var itemType: ((data: T) -> Int)? = null,
    open var itemType2: ((position: Int, data: T) -> Int)? = null,
    //ItemType对应的LayoutId
    open var layouts: MutableMap<Int, Int> = mutableMapOf(),
    //事件
    bus: AdapterBus = AdapterBus(),
    //布局，优先
    layoutStyle: LayoutStyle = LayoutStyle(),
    //ViewType
    override var viewType: Any? = null,

    //点击事件
    override var onItemClick: OnItemClick<T>? = null,
    override var onItemClickX: OnItemClickX<T>? = null,
    //长按事件
    override var onItemLongClick: OnItemLongClick<T>? = null,
    override var onItemLongClickX: OnItemLongClickX<T>? = null,
    //初始化的apply 便于执行其他代码
    apply: (MultiItemCommonAdapter<T>.() -> Unit)? = null,
    //转换
    override val convertP: AdapterPayloadsConvert<T>? = null,
    override val convert: AdapterConvert<T>? = null
) : CommonAdapter<T>(
    context = context,
    initDatas = initDatas,
    layoutStyle = layoutStyle,
    bus = bus
) {


    init {
        apply?.invoke(this)
    }

    override fun getItemViewType(position: Int): Int {
        var data = getItemData(position) ?: return super.getItemViewType(position)
        return getItemViewType(position, data)
    }


    override fun getLayoutId(viewType: Int): Int? {
        return layouts.getOrDefault(viewType, null)
    }

    override fun getBindClass(viewType: Int): Class<out ViewBinding>? {
        return bindings.getOrDefault(viewType, null)
    }

    open fun getItemViewType(position: Int, data: T) =
        itemType2?.invoke(position, data) ?: itemType?.invoke(data)
        ?: throw RuntimeException("必须提供itemtype")

}