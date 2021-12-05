package com.ashlikun.adapter.recyclerview.group

import android.content.Context
import com.ashlikun.adapter.recyclerview.multiltem.MultiItemCommonAdapter
import java.util.ArrayList
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.ViewGroup
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding
import com.ashlikun.adapter.R
import com.ashlikun.adapter.ViewHolder
import com.ashlikun.adapter.recyclerview.AdapterConvert
import com.ashlikun.adapter.recyclerview.vlayout.mode.AdapterBus
import kotlin.math.abs
import kotlin.math.max

typealias OnHeaderFooterClick = (adapter: GroupedCommonAdapter<*>, groupPosition: Int) -> Unit
typealias OnChildClick = (adapter: GroupedCommonAdapter<*>, groupPosition: Int, childPosition: Int) -> Unit
typealias GroupAdapterConvert<T> = (holder: ViewHolder?, t: T?, groupPosition: Int) -> Unit
typealias GroupChildAdapterConvert<T> = (holder: ViewHolder?, t: T?, groupPosition: Int, childPosition: Int) -> Unit

/**
 * 通用的分组列表Adapter。通过它可以很方便的实现列表的分组效果。
 * 这个类提供了一系列的对列表的更新、删除和插入等操作的方法。
 * 使用者要使用这些方法的列表进行操作，而不要直接使用RecyclerView.Adapter的方法。
 * 因为当分组列表发生变化时，需要及时更新分组列表的组结构[GroupedCommonAdapter.mStructures]
 */
abstract class GroupedCommonAdapter<T>(
    override var context: Context,
    datas: MutableList<T>,
    //ItemType对应的binding 多个
    override var bindingClasss: MutableMap<Int, Class<out ViewBinding>> = hashMapOf(),
    //ItemType对应的LayoutId
    override var layouts: MutableMap<Int, Int> = hashMapOf(),
    //事件
    override var bus: AdapterBus? = null,
    open var onHeaderClick: OnHeaderFooterClick? = null,
    open var onFooterClick: OnHeaderFooterClick? = null,
    open var onChildClick: OnChildClick? = null,
    //获取子的大小
    open var getChildrenCount: ((groupPosition: Int, t: T?) -> Int)? = null,
    //转换
    open var convertHeader: GroupAdapterConvert<T>? = null,
    open var convertFoot: GroupAdapterConvert<T>? = null,
    open var convertChild: GroupChildAdapterConvert<T>? = null
) : MultiItemCommonAdapter<T>(
    context = context,
    datas = datas,
    bindingClasss = bindingClasss,
    layouts = layouts,
    bus = bus,
    itemType = { position, data -> 0 }
) {
    //保存分组列表的组结构
    protected var mStructures = ArrayList<GroupStructure>()

    //数据是否发生变化。如果数据发生变化，要及时更新组结构。
    private var isDataChanged = false

    companion object {
        val TYPE_HEADER = abs("GroupedCommonAdapter.TYPE_HEADER".hashCode())
        val TYPE_FOOTER = abs("GroupedCommonAdapter.TYPE_FOOTER".hashCode())
        val TYPE_CHILD = abs("GroupedCommonAdapter.TYPE_CHILD".hashCode())
    }

    init {
        this.convert = { holder, t ->
            val groupPosition = getGroupPositionForPosition(holder.positionInside)
            when (holder.itemViewType) {
                TYPE_HEADER -> convertHeader?.invoke(holder, t, groupPosition)
                TYPE_FOOTER -> convertFoot?.invoke(holder, t, groupPosition)
                TYPE_CHILD ->
                    convertChild?.invoke(
                        holder,
                        t,
                        groupPosition,
                        getChildPositionForPosition(groupPosition, holder.positionInside)
                    )
            }
        }
        registerAdapterDataObserver(GroupDataObserver())
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (recyclerView.layoutManager is GridLayoutManager) {
            val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager
            gridLayoutManager.spanSizeLookup = SpanSizeLookupGroup(gridLayoutManager, this)
        }
        structureChanged()
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        //处理StaggeredGridLayout，保证组头和组尾占满一行。
        if (holder.isStaggeredGridLayout) {
            if (judgePositionToType(holder.positionInside) == TYPE_HEADER || judgePositionToType(
                    holder.positionInside
                ) == TYPE_FOOTER
            ) {
                val p = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
                p.isFullSpan = true
            }
        }
    }

    /**
     * 重写父类，用组的position替代
     */
    override fun getItemData(position: Int): T? {
        val groupPosition = getGroupPositionForPosition(position)
        return super.getItemData(groupPosition)
    }

    /**
     * 获取组数据
     */
    fun getGroupData(groupPosition: Int): T? {
        return dataHandle.getItemData(groupPosition)
    }

    override fun onItemClick(viewType: Int, parent: ViewGroup, view: View, data: T, position: Int) {
        super.onItemClick(viewType, parent, view, data, position)
        val groupPosition = getGroupPositionForPosition(position)
        if (viewType == TYPE_HEADER) {
            onHeaderClick?.apply {
                val gPosition =
                    if (parent is FrameLayout) groupPosition else getGroupPositionForPosition(
                        position
                    )
                if (gPosition >= 0 && gPosition < mStructures.size) {
                    invoke(this@GroupedCommonAdapter, gPosition)
                }
            }
        } else if (viewType == TYPE_FOOTER) {
            onFooterClick?.apply {
                if (onFooterClick != null) {
                    val gPosition = getGroupPositionForPosition(position)
                    if (gPosition >= 0 && gPosition < mStructures.size) {
                        onFooterClick?.invoke(this@GroupedCommonAdapter, gPosition)
                    }
                }
            }

        } else if (viewType == TYPE_CHILD) {
            onChildClick?.apply {
                val gPosition = getGroupPositionForPosition(position)
                val cPosition = getChildPositionForPosition(gPosition, position)
                if (gPosition >= 0 && gPosition < mStructures.size && cPosition >= 0 && cPosition < mStructures[gPosition].childrenCount) {
                    invoke(this@GroupedCommonAdapter, gPosition, cPosition)
                }
            }
        }
    }


    override fun getItemCount(): Int {
        if (isDataChanged) {
            structureChanged()
        }
        return max(count(), 0)
    }


    override fun getItemViewType(position: Int): Int {
        return judgePositionToType(position)
    }

    private fun count(): Int {
        return if (mStructures.isEmpty()) 0 else mStructures[mStructures.size - 1].end + 1
    }


    /**
     * 判断item的type 头部 尾部 和 子项
     *
     * @param position
     * @return
     */
    fun judgePositionToType(position: Int): Int {
        val groupCount = mStructures.size
        for (i in 0 until groupCount) {
            val structure = mStructures[i]
            val type = structure.containsPositionType(position)
            if (type != -1) {
                return type
            }
        }
        return -1
    }


    /**
     * 重置组结构列表
     */
    private fun structureChanged() {
        val structures = ArrayList<GroupStructure>()
        structures.addAll(mStructures)
        mStructures.clear()
        val groupCount: Int = itemCount
        var startPosition = 0
        for (i in 0 until groupCount) {
            var structure: GroupStructure
            val itemData = getGroupData(i)
            if (i < structures.size) {
                structure = structures[i]
                structure.init(
                    hasHeader(i, itemData),
                    hasFooter(i, itemData),
                    startPosition,
                    getChildrenCount(i, itemData)
                )
            } else {
                structure = GroupStructure(
                    hasHeader(i, itemData),
                    hasFooter(i, itemData),
                    startPosition,
                    getChildrenCount(i, itemData)
                )
            }
            mStructures.add(structure)
            startPosition = structure.end + 1
        }
        structures.clear()
        isDataChanged = false
    }

    /**
     * 根据下标计算position所在的组（groupPosition）
     *
     * @param position 下标
     * @return 组下标 groupPosition
     */
    fun getGroupPositionForPosition(position: Int): Int {
        for (i in mStructures.indices) {
            val structure = getGroupItem(i)
            if (structure != null && structure.containsPosition(position)) {
                return i
            }
        }
        return -1
    }

    /**
     * 根据下标计算position在组中位置（childPosition）
     *
     * @param groupPosition 所在的组
     * @param position      下标
     * @return 子项下标 childPosition
     */
    fun getChildPositionForPosition(groupPosition: Int, position: Int): Int {
        val structure = getGroupItem(groupPosition)
        return structure?.getChildPositionForPosition(position) ?: -1
    }

    /**
     * 获取一个组的开始下标，这个下标可能是组头，可能是子项(如果没有组头)或者组尾(如果这个组只有组尾)
     *
     * @param groupPosition 组下标
     * @return
     */
    fun getPositionForGroup(groupPosition: Int): Int {
        val structure = getGroupItem(groupPosition)
        return structure?.start ?: -1
    }

    /**
     * 获取一个组的组头下标 如果该组没有组头 返回-1
     *
     * @param groupPosition 组下标
     * @return 下标
     */
    fun getPositionForGroupHeader(groupPosition: Int): Int {
        val structure = getGroupItem(groupPosition)
        return if (structure != null) {
            if (!structure.hasHeader) {
                -1
            } else structure.start
        } else -1
    }

    /**
     * 获取一个组的组尾下标 如果该组没有组尾 返回-1
     *
     * @param groupPosition 组下标
     * @return 下标
     */
    fun getPositionForGroupFooter(groupPosition: Int): Int {
        val structure = getGroupItem(groupPosition)
        return if (structure != null) {
            if (!structure.hasFooter) {
                -1
            } else structure.end
        } else -1
    }

    /**
     * 获取一个组指定的子项下标 如果没有 返回-1
     *
     * @param groupPosition 组下标
     * @param childPosition 子项的组内下标
     * @return 下标
     */
    fun getPositionForChild(groupPosition: Int, childPosition: Int): Int {
        val structure = mStructures[groupPosition]
        if (structure != null) {
            if (structure.childrenCount > childPosition) {
                return childPosition + structure.childrenStart
            }
        }
        return -1
    }

    /**
     * 计算一个组里有多少个Item（头加尾加子项）
     */
    fun countGroupItem(groupPosition: Int): Int {
        val item = getGroupItem(groupPosition)
        return item?.countGroupItem() ?: 0
    }

    /**
     * 获取一个组
     */
    fun getGroupItem(groupPosition: Int): GroupStructure? {
        return if (groupPosition >= 0 && groupPosition < mStructures.size) {
            mStructures[groupPosition]
        } else null
    }
    //****** 刷新操作 *****//
    /**
     * 通知数据列表刷新
     */
    fun notifyDataChanged() {
        isDataChanged = true
        notifyDataSetChanged()
    }

    /**
     * 通知一组数据刷新，包括组头,组尾和子项
     *
     * @param groupPosition
     */
    fun notifyGroupChanged(groupPosition: Int) {
        val index = getPositionForGroup(groupPosition)
        val itemCount = countGroupItem(groupPosition)
        if (index >= 0 && itemCount > 0) {
            notifyItemRangeChanged(index, itemCount)
        }
    }

    /**
     * 通知多组数据刷新，包括组头,组尾和子项
     *
     * @param groupPosition
     */
    fun notifyGroupRangeChanged(groupPosition: Int, count: Int) {
        val start = getPositionForGroup(groupPosition)
        val endGroup = getGroupItem(Math.min(groupPosition + count - 1, mStructures.size - 1))
        val itemCount = (endGroup?.end ?: 0) - start + 1
        if (start >= 0 && itemCount > 0) {
            notifyItemRangeChanged(start, itemCount)
        }
    }

    /**
     * 通知组头刷新
     *
     * @param groupPosition
     */
    fun notifyHeaderChanged(groupPosition: Int) {
        val start = getPositionForGroupHeader(groupPosition)
        if (start >= 0) {
            notifyItemChanged(start)
        }
    }

    /**
     * 通知组尾刷新
     *
     * @param groupPosition
     */
    fun notifyFooterChanged(groupPosition: Int) {
        val end = getPositionForGroupFooter(groupPosition)
        if (end >= 0) {
            notifyItemChanged(end)
        }
    }

    /**
     * 通知一组里的某个子项刷新
     *
     * @param groupPosition
     * @param childPosition
     */
    fun notifyChildChanged(groupPosition: Int, childPosition: Int) {
        val index = getPositionForChild(groupPosition, childPosition)
        if (index >= 0) {
            notifyItemChanged(index)
        }
    }

    /**
     * 通知一组里的多个子项刷新
     *
     * @param groupPosition
     * @param childPosition
     * @param count
     */
    fun notifyChildRangeChanged(groupPosition: Int, childPosition: Int, count: Int) {
        if (groupPosition < mStructures.size) {
            val index = getPositionForChild(groupPosition, childPosition)
            if (index >= 0) {
                val structure = mStructures[groupPosition]
                if (structure.childrenCount >= childPosition + count) {
                    notifyItemRangeChanged(index, count)
                } else {
                    notifyItemRangeChanged(index, structure.childrenCount - childPosition)
                }
            }
        }
    }

    /**
     * 通知一组里的所有子项刷新
     *
     * @param groupPosition
     */
    fun notifyChildrenChanged(groupPosition: Int) {
        val index = getPositionForChild(groupPosition, 0)
        if (index >= 0) {
            val structure = mStructures[groupPosition]
            notifyItemRangeChanged(index, structure.childrenCount)
        }
    }

    /**
     * 通知所有数据删除
     */
    fun notifyDataRemoved() {
        val count = itemCount
        mStructures.clear()
        notifyItemRangeRemoved(0, count)
    }

    /**
     * 通知一组数据删除，包括组头,组尾和子项
     *
     * @param groupPosition
     */
    fun notifyGroupRemoved(groupPosition: Int) {
        val start = getPositionForGroup(groupPosition)
        val itemCount = countGroupItem(groupPosition)
        if (start >= 0 && itemCount > 0) {
            structureChanged()
            notifyItemRangeRemoved(start, itemCount)
        }
    }

    /**
     * 通知多组数据删除，包括组头,组尾和子项
     *
     * @param groupPosition
     */
    fun notifyGroupRangeRemoved(groupPosition: Int, count: Int) {
        val start = getPositionForGroup(groupPosition)
        val endGroup = getGroupItem(Math.min(groupPosition + count - 1, mStructures.size - 1))
        val itemCount = if (endGroup == null) 0 else endGroup.end + 1
        if (start >= 0 && itemCount > 0) {
            structureChanged()
            notifyItemRangeChanged(start, itemCount)
        }
    }

    /**
     * 通知组头删除
     *
     * @param groupPosition
     */
    fun notifyHeaderRemoved(groupPosition: Int) {
        val index = getPositionForGroupHeader(groupPosition)
        if (index >= 0) {
            structureChanged()
            val structure = mStructures[groupPosition]
            notifyItemRemoved(index)
        }
    }

    /**
     * 通知组尾删除
     *
     * @param groupPosition
     */
    fun notifyFooterRemoved(groupPosition: Int) {
        val index = getPositionForGroupFooter(groupPosition)
        if (index >= 0) {
            structureChanged()
            val structure = mStructures[groupPosition]
            notifyItemRemoved(index)
        }
    }

    /**
     * 通知一组里的某个子项删除
     *
     * @param groupPosition
     * @param childPosition
     */
    fun notifyChildRemoved(groupPosition: Int, childPosition: Int) {
        val index = getPositionForChild(groupPosition, childPosition)
        if (index >= 0) {
            structureChanged()
            notifyItemRemoved(index)
        }
    }

    /**
     * 通知一组里的多个子项删除
     *
     * @param groupPosition
     * @param childPosition
     * @param count
     */
    fun notifyChildRangeRemoved(groupPosition: Int, childPosition: Int, count: Int) {
        val index = getPositionForChild(groupPosition, childPosition)
        if (index >= 0) {
            val structure = mStructures[groupPosition]
            val childCount = structure.childrenCount
            var removeCount = count
            if (childCount < childPosition + count) {
                removeCount = childCount - childPosition
            }
            structureChanged()
            notifyItemRangeRemoved(index, removeCount)
        }
    }

    /**
     * 通知一组里的所有子项删除
     *
     * @param groupPosition
     */
    fun notifyChildrenRemoved(groupPosition: Int) {
        if (groupPosition < mStructures.size) {
            val start = getPositionForChild(groupPosition, 0)
            if (start >= 0) {
                val structure = mStructures[groupPosition]
                val itemCount = structure.childrenCount
                structureChanged()
                notifyItemRangeRemoved(start, itemCount)
            }
        }
    }
    //****** 插入操作 *****//
    /**
     * 通知一组数据插入
     *
     * @param groupPosition
     */
    fun notifyGroupInserted(groupPosition: Int) {
        structureChanged()
        val structure = getGroupItem(groupPosition)
        val start = structure?.start ?: 0
        val itemCount = countGroupItem(groupPosition)
        if (itemCount > 0) {
            notifyItemRangeInserted(start, itemCount)
        }
    }

    /**
     * 通知多组数据插入
     *
     * @param groupPosition
     * @param count
     */
    fun notifyGroupRangeInserted(groupPosition: Int, count: Int) {
        structureChanged()
        val structure = getGroupItem(groupPosition)
        val start = structure?.start ?: 0
        val itemCount = countGroupItem(groupPosition + count)
        if (itemCount > 0) {
            notifyItemRangeInserted(start, itemCount)
        }
    }

    /**
     * 通知组头插入
     *
     * @param groupPosition
     */
    fun notifyHeaderInserted(groupPosition: Int) {
        structureChanged()
        val structure = getGroupItem(groupPosition)
        if (structure != null) {
            notifyItemInserted(structure.start)
        }
    }

    /**
     * 通知组尾插入
     *
     * @param groupPosition
     */
    fun notifyFooterInserted(groupPosition: Int) {
        structureChanged()
        val structure = mStructures[groupPosition]
        if (structure != null) {
            notifyItemInserted(structure.start)
        }
    }

    /**
     * 通知一个子项到组里插入
     *
     * @param groupPosition
     * @param childPosition
     */
    fun notifyChildInserted(groupPosition: Int, childPosition: Int) {
        if (groupPosition < mStructures.size) {
            structureChanged()
            val index = getPositionForChild(groupPosition, childPosition)
            if (index >= 0) {
                notifyItemInserted(index)
            }
        }
    }

    /**
     * 通知一组里的多个子项插入
     *
     * @param groupPosition
     * @param childPosition
     * @param count
     */
    fun notifyChildRangeInserted(groupPosition: Int, childPosition: Int, count: Int) {
        if (groupPosition < mStructures.size) {
            structureChanged()
            val structure = mStructures[groupPosition]
            if (count > 0 && structure != null) {
                notifyItemRangeInserted(structure.start + childPosition, count)
            }
        }
    }

    /**
     * 通知一组里的所有子项插入
     *
     * @param groupPosition
     */
    fun notifyChildrenInserted(groupPosition: Int) {
        if (groupPosition < mStructures.size) {
            structureChanged()
            val structure = mStructures[groupPosition]
            if (structure != null && structure.childrenCount > 0) {
                notifyItemRangeInserted(structure.start, structure.childrenCount)
            }
        }
    }

    open fun hasHeader(groupPosition: Int, itemData: T?) = true

    open fun hasFooter(groupPosition: Int, itemData: T?) = false

    open fun getChildrenCount(groupPosition: Int, t: T?) =
        getChildrenCount?.invoke(groupPosition, t) ?: throw RuntimeException("必须提供子项数量")


    internal inner class GroupDataObserver : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            isDataChanged = true
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            isDataChanged = true
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            onItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            isDataChanged = true
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            isDataChanged = true
        }
    }


}