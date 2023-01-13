/*
 * MIT License
 *
 * Copyright (c) 2016 Alibaba Group
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.ashlikun.adapter.recyclerview.vlayout

import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ashlikun.vlayout.Cantor
import com.ashlikun.vlayout.LayoutHelper
import com.ashlikun.vlayout.VirtualLayoutAdapter
import com.ashlikun.vlayout.VirtualLayoutManager
import com.ashlikun.adapter.AdapterUtils
import com.ashlikun.adapter.ViewHolder
import com.ashlikun.adapter.recyclerview.IHeaderAndFooter
import com.ashlikun.adapter.recyclerview.common.CommonAdapter
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max
import kotlin.math.min

/**
 *
 *
 * 功能介绍：
 * 不能添加头尾
 * alibaba的VLayout库的adapter
 * 要想使用必须使用这个库
 * implementation ('com.github.ashLikun:VLayout:{latest version}')
 */
open class MultipleAdapter(
    layoutManager: VirtualLayoutManager,
    var hasConsistItemType: Boolean = false,
    threadSafe: Boolean = false
) : VirtualLayoutAdapter<ViewHolder>(layoutManager), IHeaderAndFooter {
    override var footerSize = 0
    override var headerSize = 0

    //异步获取
    var recyclerView: RecyclerView? = null
    private var mIndexGen: AtomicInteger? = null
    private var mIndex = 0
    private val mItemTypeAry = SparseArray<CommonAdapter<*>>()
    private val mAdapters = mutableListOf<CommonAdapter<*>>()
    private var mTotal = 0
    private val mIndexAry = SparseArray<CommonAdapter<*>>()

    //防止Cantor(康托)算法溢出int和long最大值
    private val mCantorTemp = ArrayList<Long>()
    private val cantorReverse = LongArray(2)

    init {
        if (threadSafe) mIndexGen = AtomicInteger(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (hasConsistItemType) {
            val adapter = mItemTypeAry[viewType]
            if (adapter != null)
                return adapter!!.onCreateViewHolder(parent, viewType)
        }
        // 反康托尔函数
        Cantor.reverseCantor(viewType.toLong(), cantorReverse)
        val index = cantorReverse.getOrNull(1)?.toInt()
        if (index != null) {
            val subItemType = cantorReverse[0].toInt()
            val adapter = findByIndex<Any>(index)
            if (adapter != null) return adapter.onCreateViewHolder(parent, subItemType)
        }
        throw NullPointerException("onCreateViewHolder null")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val adapter = findAdapter(position) ?: return
        adapter.onBindViewHolder(holder, position - adapter.startPosition)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        val adapter = findAdapter(position) ?: return
        adapter.onBindViewHolder(holder, position - adapter.startPosition, payloads)
    }

    override fun getItemCount() = mTotal

    /**
     * 委托适配器返回的itemType的大整数可能导致失败
     */
    override fun getItemViewType(position: Int): Int {
        val adapter = findAdapter(position) ?: return RecyclerView.INVALID_TYPE
        return getAdapterItemViewType(adapter, position - adapter.startPosition)

    }

    /**
     * 获取adapter转换成对应真实的ViewType
     * 需要在addadapter后才能调用
     */
    fun getAdapterItemViewType(adapter: CommonAdapter<*>, position: Int = 0): Int {
        var subItemType = adapter.getItemViewType(position)
        if (subItemType < 0) {
            // 负整数，无效，仅返回
            return subItemType
        }
        if (hasConsistItemType) {
            if (mAdapters.contains(adapter)) {
                mItemTypeAry.put(subItemType, adapter)
            }
            return subItemType
        }
        val index = adapter.vLayoutIndex
        //防止Cantor(康托)算法溢出int和long最大值
        subItemType = getCantorToViewType(subItemType.toLong())
        return Cantor.getCantor(subItemType.toLong(), index.toLong()).toInt()
    }

    /**
     * 防止Cantor(康托)算法溢出int和long最大值
     * 这里从新从1开始赋值
     */
    private fun getCantorToViewType(cantor: Long): Int {
        if (!mCantorTemp.contains(cantor)) {
            mCantorTemp.add(cantor)
        }
        return mCantorTemp.indexOf(cantor) + 1
    }

    override fun getItemId(position: Int): Long {
        val adapter = findAdapter(position) ?: return RecyclerView.NO_ID
        val itemId = adapter.getItemId(position - adapter.startPosition)
        if (itemId < 0) return RecyclerView.NO_ID
        val index = adapter.vLayoutIndex
        //现在我们有一个配对函数的问题，我们使用cantor配对函数作为itemId。
        return Cantor.getCantor(index.toLong(), itemId)
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        // do nothing
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        val position = holder.layoutPosition
        if (position >= 0) {
            findAdapter(position)?.onViewRecycled(holder)
        }
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val position = holder.layoutPosition
        if (position >= 0) {
            findAdapter(position)?.onViewAttachedToWindow(holder)
        }
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        val position = holder.layoutPosition
        if (position >= 0) {
            findAdapter(position)?.onViewDetachedFromWindow(holder)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        mAdapters.forEach {
            it.onAttachedToRecyclerView(recyclerView)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        mAdapters.forEach {
            it.onDetachedFromRecyclerView(recyclerView)
        }
        this.recyclerView = null
    }


    fun setAdapters(adapters: List<CommonAdapter<*>>?) {
        clear()
        val helpers = mutableListOf<LayoutHelper>()
        var hasStableIds = true
        adapters?.forEach { adapter ->
            // 每个适配器都有一个唯一的索引id
            val observer = AdapterDataObserver(mTotal + headerSize, mIndexGen?.incrementAndGet() ?: mIndex++)
            adapter.registerAdapterDataObserver(observer)
            hasStableIds = hasStableIds && adapter.hasStableIds()
            val helper = adapter.layoutStyle.helper
            helper.itemCount = adapter.itemCount
            //这里一定得获取layouHelper的
            mTotal += helper.itemCount
            //添加的时候构建一下
            adapter.layoutStyle.bindUI()
            helpers.add(helper)
            mIndexAry.put(observer.index, adapter)
            //设置附属到recyclerView
            if (recyclerView != null && adapter.recyclerView == null) {
                adapter.onAttachedToRecyclerView(recyclerView!!)
            }
            mAdapters.add(adapter)
        }
        if (!hasObservers()) {
            super.setHasStableIds(hasStableIds)
        }
        layoutHelpers = helpers
    }

    fun addAdapters(adapters: List<CommonAdapter<*>>?, index: Int = mAdapters.size) {
        if (adapters.isNullOrEmpty()) return
        var index = min(max(index, 0), mAdapters.size)
        val newAdapter = mutableListOf<CommonAdapter<*>>()
        newAdapter.addAll(mAdapters)
        adapters.forEach {
            newAdapter.add(index, it)
            index++
        }
        setAdapters(newAdapter)
    }

    fun addAdapters(vararg adapters: CommonAdapter<*>, index: Int = mAdapters.size) {
        addAdapters(adapters.toList(), index)
    }

    fun addAdapter(adapter: CommonAdapter<*>?, index: Int = mAdapters.size) {
        if (adapter == null) return
        addAdapters(listOf(adapter), index)
    }

    fun removeFirstAdapter() = removeAdapter(mAdapters.firstOrNull())

    fun removeLastAdapter() = removeAdapter(mAdapters.lastOrNull())

    fun removeAdapter(index: Int) = removeAdapter(mAdapters.getOrNull(index))

    fun removeAdapter(targetAdapter: CommonAdapter<*>?) {
        if (targetAdapter == null) return
        removeAdapters(listOf(targetAdapter))
    }

    fun removeAdapters(targetAdapters: List<CommonAdapter<*>>?) {
        if (targetAdapters.isNullOrEmpty()) return
        val helpers = LinkedList(getLayoutHelpers())
        val newAdapter = mutableListOf<CommonAdapter<*>>()
        mAdapters.filterTo(newAdapter) { adapter ->
            val contains = targetAdapters.contains(adapter)
            val obs = adapter.vLayoutObserver
            if (contains && obs != null) {
                adapter.unregisterAdapterDataObserver(obs)
                val position = findPositionByIndex(adapter.vLayoutIndex)
                if (position >= 0 && position < helpers.size) {
                    helpers.removeAt(position)
                }
            }
            !contains
        }
        setAdapters(newAdapter)
    }

    fun clear() {
        mTotal = 0
        mIndex = 0
        mIndexGen?.set(0)
        mLayoutManager.setLayoutHelpers(null)
        mAdapters.forEach {
            it.vLayoutObserver?.let { it1 -> it.unregisterAdapterDataObserver(it1) }
        }
        mItemTypeAry.clear()
        mAdapters.clear()
        mIndexAry.clear()
    }

    /**
     * 当前适配器数量
     */
    val adaptersCount: Int
        get() = mAdapters.size

    /**
     * 根据全局的position 查找Adapter
     */
    private fun findAdapter(position: Int): CommonAdapter<*>? {
        val count = mAdapters.size
        if (count == 0) {
            return null
        }
        var s = 0
        var e = count - 1
        var m: Int
        var rs: CommonAdapter<*>? = null

        // 二进制搜索范围
        while (s <= e) {
            m = (s + e) / 2
            rs = mAdapters[m]
            val endPosition = rs.startPosition + rs.itemCount - 1
            if (rs.startPosition > position) {
                e = m - 1
            } else if (endPosition < position) {
                s = m + 1
            } else if (rs.startPosition <= position && endPosition >= position) {
                break
            }
            rs = null
        }
        return rs
    }

    /**
     * 通过在DelegaterAdapter中的绝对位置来确定子适配器中的相对位置。 如果没有子适配器，返回-1。
     */
    fun findOffsetPosition(absoultePosition: Int): Int {
        val p = findAdapter(absoultePosition) ?: return -1
        return absoultePosition - p.startPosition
    }

    fun findPositionByIndex(index: Int): Int {
        val rs = mIndexAry[index]
        return if (rs == null) -1 else mAdapters.indexOf(rs)
    }

    fun <DATA> findByIndex(index: Int): CommonAdapter<DATA>? = findByIndexX(index)

    /**
     * 查找对应position的adapter
     */
    fun <T : CommonAdapter<*>> findByIndexX(index: Int): T? = mIndexAry[index] as? T

    /**
     * 查找对应的adapter的开始position
     */
    fun findByIndexStartPosition(index: Int) = mIndexAry[index]?.startPosition ?: -1

    /**
     * 获取这种类型的adapter对于的开始position
     */
    fun findByViewTypeStartPosition(viewType: Any) =
        mAdapters.find { it.getItemViewType(0) == AdapterUtils.viewTypeToVLayout(viewType) }?.startPosition
            ?: -1

    /**
     * 查找对应position的adapter
     */
    fun <DATA> findByPosition(position: Int): CommonAdapter<DATA>? = findByPositionX(position)


    /**
     * 查找对应position的adapter
     */
    fun <T : CommonAdapter<*>> findByPositionX(position: Int): T? =
        findAdapter(position) as? T

    /**
     * 获取这种类型的adapter
     */
    fun <DATA> findByViewType(viewType: Any): CommonAdapter<DATA>? = findByViewTypeX(viewType)

    /**
     * 获取这种类型的adapter
     */
    fun <T> findByViewTypeX(viewType: Any): T? =
        mAdapters.find { it.getItemViewType(0) == AdapterUtils.viewTypeToVLayout(viewType) } as? T

    /**
     * 获取这种Id的adapter
     */
    fun <DATA> findById(viewType: Any): CommonAdapter<DATA>? = findByIdX(viewType)

    /**
     * 获取这种id的adapter
     */
    fun <T> findByIdX(id: Any): T? =
        mAdapters.find { it.id == id } as? T

    /**
     * 是否有这种类型的adapter
     */
    fun haveByViewType(viewType: Any) =
        mAdapters.find { it.getItemViewType(0) == AdapterUtils.viewTypeToVLayout(viewType) } != null

    /**
     * 更新全部数据
     */
    fun notifyChanged() {
        mAdapters.forEach {
            it.notifyDataSetChanged()
        }
    }

    /**
     * 内部的Adapter
     */
    inner class AdapterDataObserver(var startPosition: Int, var index: Int) :
        RecyclerView.AdapterDataObserver() {
        private fun updateLayoutHelper(): Boolean {
            if (index < 0) return false
            val idx = findPositionByIndex(index)
            if (idx < 0) return false
            val p = mAdapters[idx]
            val helpers: List<LayoutHelper> = LinkedList(layoutHelpers)
            val helper = helpers[idx]
            if (helper.itemCount != p.itemCount) {
                // if itemCount changed;
                helper.itemCount = p.itemCount
                mTotal = startPosition + (p.itemCount)
                for (i in idx + 1 until mAdapters.size) {
                    val adapter = mAdapters[i]
                    // 更新以下适配器的startPosition
                    adapter.vLayoutIndex
                    adapter.vLayoutObserver?.startPosition = mTotal
                    mTotal += adapter.itemCount
                }
                // 将助手设置为刷新范围
                layoutHelpers = helpers
            }
            return true
        }

        override fun onChanged() {
            if (!updateLayoutHelper()) {
                return
            }
            notifyDataSetChanged()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            if (!updateLayoutHelper()) {
                return
            }
            notifyItemRangeRemoved(startPosition + positionStart, itemCount)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            if (!updateLayoutHelper()) {
                return
            }
            notifyItemRangeInserted(startPosition + positionStart, itemCount)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            if (!updateLayoutHelper()) {
                return
            }
            notifyItemMoved(startPosition + fromPosition, startPosition + toPosition)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            if (!updateLayoutHelper()) {
                return
            }
            notifyItemRangeChanged(startPosition + positionStart, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            if (!updateLayoutHelper()) {
                return
            }
            notifyItemRangeChanged(startPosition + positionStart, itemCount, payload)
        }


    }
}