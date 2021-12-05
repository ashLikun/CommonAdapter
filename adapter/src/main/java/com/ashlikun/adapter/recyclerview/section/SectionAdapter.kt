package com.ashlikun.adapter.recyclerview.section

import android.content.Context
import android.view.View
import androidx.viewbinding.ViewBinding
import com.ashlikun.adapter.recyclerview.CommonAdapter
import com.ashlikun.adapter.ViewHolder
import com.ashlikun.adapter.recyclerview.AdapterConvert
import com.ashlikun.adapter.recyclerview.vlayout.mode.AdapterBus
import kotlin.math.abs

/**
 * 作者　　: 李坤
 * 创建时间: 9:57 Administrator
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：分组adapter
 */
abstract class SectionAdapter<T : SectionEntity>(
    override var context: Context,
    datas: MutableList<T>,
    //创建ViewBinding的Class,与layoutId 二选一
    override var bindingClass: Class<out ViewBinding>? = null,
    //头布局 ViewBinding
    open var headBinding: Class<out ViewBinding>? = null,
    //布局文件
    override var layoutId: Int = DEFAULT_LAYOUT_ID,
    //事件
    override var bus: AdapterBus? = null,
    //头布局id
    open var headLayoutId: Int = View.NO_ID,
    //转换头
    open var convertHeader: AdapterConvert<T>? = null,
    //转换
    override var convert: AdapterConvert<T>? = null
) : CommonAdapter<T>(context, datas, bindingClass, layoutId, bus, convert) {


    companion object {
        /**
         * 头部ItemType
         */
        val TYPE_SECTION = abs("SectionAdapter.TYPE_SECTION".hashCode())
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItemData(position)?.isHeader == true) TYPE_SECTION else super.getItemViewType(
            position
        )
    }

    override fun getLayoutId(viewType: Int): Int {
        return if (viewType == TYPE_SECTION) return headLayoutId else super.getLayoutId(viewType)
    }

    override fun getBindClass(viewType: Int): Class<out ViewBinding>? {
        return if (viewType == TYPE_SECTION) return headBinding else super.getBindClass(viewType)
    }

    protected fun isViewTypeHeader(viewType: Int): Boolean {
        return viewType == TYPE_SECTION
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder.itemViewType == TYPE_SECTION) {
            setListener(holder, holder.itemViewType)
            convertHeader?.invoke(holder, getItemData(position))
            return
        }
        super.onBindViewHolder(holder, position)
    }


}