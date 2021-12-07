package com.ashlikun.adapter.recyclerview.section

import android.content.Context
import android.view.View
import androidx.viewbinding.ViewBinding
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.ashlikun.adapter.ViewHolder
import com.ashlikun.adapter.recyclerview.AdapterConvert
import com.ashlikun.adapter.recyclerview.BaseAdapter
import com.ashlikun.adapter.recyclerview.vlayout.SingAdapter
import com.ashlikun.adapter.recyclerview.vlayout.mode.AdapterBus
import com.ashlikun.adapter.recyclerview.vlayout.mode.AdapterStyle
import kotlin.math.abs
import kotlin.reflect.KClass

/**
 * 作者　　: 李坤
 * 创建时间: 9:57 Administrator
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：分组adapter 给Vlayout的
 */
open class SectionSingAdapter<T : SectionEntity>(
        context: Context,
        initDatas: List<T>? = null,
        //创建ViewBinding的Class,与layoutId 二选一
        override val bindingClass: Class<out ViewBinding>? = null,
        //头布局 ViewBinding
        open val headBinding: Class<out ViewBinding>? = null,
        //布局文件
        override val layoutId: Int = DEFAULT_LAYOUT_ID,
        //事件
        override var bus: AdapterBus? = null,
        //头布局id
        open var headLayoutId: Int = View.NO_ID,
        //布局
        layoutHelper: LayoutHelper = LinearLayoutHelper(),
        //布局，优先
        layoutStyle: AdapterStyle? = null,
        //ViewType
        override var viewType: Any = this::class,
        //转换头
        open var convertHeader: AdapterConvert<T>? = null,
        //初始化的apply 便于执行其他代码
        apply: (SectionSingAdapter<T>.() -> Unit)? = null,
        //转换
        override var convert: AdapterConvert<T>? = null
) : SingAdapter<T>(
        context = context,
        initDatas = initDatas,
        layoutHelper = layoutHelper,
        layoutStyle = layoutStyle,
        apply = apply as (SingAdapter<T>.() -> Unit)?
) {

    companion object {
        /**
         * 头部ItemType,这里Vlayout是不允许type为负数的
         */
        val TYPE_SECTION = abs("SectionSingAdapter.TYPE_SECTION".hashCode())
    }

    init {
        apply?.invoke(this)
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItemData(position)?.isHeader == true) SectionAdapter.TYPE_SECTION else super.getItemViewType(
                position
        )
    }

    override fun getLayoutId(viewType: Int): Int? {
        return if (viewType == SectionAdapter.TYPE_SECTION) return headLayoutId else super.getLayoutId(
                viewType
        )
    }

    override fun getBindClass(viewType: Int): Class<out ViewBinding>? {
        return if (viewType == SectionAdapter.TYPE_SECTION) return headBinding else super.getBindClass(
                viewType
        )
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