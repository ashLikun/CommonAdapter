package com.ashlikun.adapter.recyclerview.section

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.ashlikun.vlayout.layout.GridLayoutHelper
import com.ashlikun.adapter.ViewHolder
import com.ashlikun.adapter.recyclerview.*
import com.ashlikun.adapter.recyclerview.common.CommonAdapter
import com.ashlikun.adapter.recyclerview.vlayout.mode.AdapterBus
import com.ashlikun.adapter.recyclerview.vlayout.mode.LayoutStyle
import kotlin.math.abs

/**
 * 作者　　: 李坤
 * 创建时间: 9:57 Administrator
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：分组adapter
 */
open class SectionAdapter<T : SectionEntity>(
    context: Context,
    initDatas: List<T>? = null,
    //创建ViewBinding的Class,与layoutId 二选一
    override var binding: Class<out ViewBinding>? = null,
    //头布局 ViewBinding
    open var bndingHead: Class<out ViewBinding>? = null,
    //布局文件
    override var layoutId: Int? = null,
    //布局
    override val layouView: View? = null,
    //头布局id
    open var headLayoutId: Int? = null,
    //事件
    bus: AdapterBus = AdapterBus(),
    //布局，优先
    layoutStyle: LayoutStyle = LayoutStyle(),
    //获取id
    override var itemId: OnGetItemId<T>? = null,
    override var itemIdIsPosition: Boolean? = null,
    //ViewType
    override var viewType: Any? = null,
    //转换头
    open var convertHeader: AdapterConvert<T>? = null,
    //点击事件
    override var onItemClick: OnItemClick<T>? = null,
    override var onItemClickX: OnItemClickX<T>? = null,
    //长按事件
    override var onItemLongClick: OnItemLongClick<T>? = null,
    override var onItemLongClickX: OnItemLongClickX<T>? = null,
    //初始化的apply 便于执行其他代码,子类一定需要自己实现,切换this 到Adapter
    apply: NoParamsThis<SectionAdapter<T>>? = null,
    //转换
    override var convertP: AdapterPayloadsConvert<T>? = null,
    override var convert: AdapterConvert<T>? = null
) : CommonAdapter<T>(
    context = context,
    initDatas = initDatas,
    layoutStyle = layoutStyle,
    bus = bus
) {


    companion object {
        /**
         * 头部ItemType
         */
        val TYPE_SECTION = abs("SectionAdapter.TYPE_SECTION".hashCode())
    }

    init {
        apply?.invoke(this)
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItemData(position)?.isHeader == true) TYPE_SECTION else super.getItemViewType(
            position
        )
    }

    override fun getLayoutId(viewType: Int): Int? {
        return if (viewType == TYPE_SECTION) return headLayoutId else super.getLayoutId(viewType)
    }

    override fun getBindClass(viewType: Int): Class<out ViewBinding>? {
        return if (viewType == TYPE_SECTION) return bndingHead else super.getBindClass(viewType)
    }

    protected fun isPositionHeader(position: Int): Boolean {
        return getItemViewType(position) == TYPE_SECTION
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (recyclerView.layoutManager is GridLayoutManager) {
            val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager
            gridLayoutManager.spanSizeLookup = SpanSizeLookupGroup(gridLayoutManager) {
                if (vLayoutObserver != null) {
                    //这里得减去这个Adapter、开始的位置
                    isPositionHeader(it - startPosition)
                } else {
                    //这里得减去这个Adapter、开始的位置
                    isPositionHeader(it)
                }

            }
        }
        if (vLayoutObserver != null && layoutStyle.helper is GridLayoutHelper) {
            (layoutStyle.helper as GridLayoutHelper).run {
                setSpanSizeLookup(SpanSizeLookupGroupLayoutHelper(this) {
                    //这里得减去这个Adapter、开始的位置
                    isPositionHeader(it - startPosition)
                })
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder.itemViewType == TYPE_SECTION) {
            setListener(holder, holder.itemViewType)
            val data = getItemData(position)
            if (data != null) {
                convertHeader?.invoke(holder, data)
            }
            return
        }
        super.onBindViewHolder(holder, position)
    }


}