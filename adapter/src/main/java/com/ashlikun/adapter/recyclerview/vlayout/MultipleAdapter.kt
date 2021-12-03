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

import android.util.Pair
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.VirtualLayoutAdapter
import androidx.recyclerview.widget.RecyclerView
import android.util.SparseArray
import android.view.ViewGroup
import com.alibaba.android.vlayout.Cantor
import com.alibaba.android.vlayout.LayoutHelper
import com.ashlikun.adapter.AdapterUtils
import com.ashlikun.adapter.ViewHolder
import java.lang.UnsupportedOperationException
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max
import kotlin.math.min

/**
 * 作者　　: 李坤
 * 创建时间: 2018/4/10 0010 20:33
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：alibaba的VLayout库的adapter
 * 要想使用必须使用这个库
 * implementation ('com.alibaba.android:vlayout:1.2.16@aar') {
 * transitive = true
 * }
 */
class MultipleAdapter constructor(layoutManager: VirtualLayoutManager, hasConsistItemType: Boolean = false, threadSafe: Boolean = false)
    : VirtualLayoutAdapter<RecyclerView.ViewHolder?>(layoutManager) {
    var recyclerView: RecyclerView? = null
    private var mIndexGen: AtomicInteger? = null
    private var mIndex = 0
    private val mHasConsistItemType: Boolean
    private val mItemTypeAry = SparseArray<SingAdapter<Any>>()
    private val mAdapters: MutableList<Pair<AdapterDataObserver, SingAdapter<Any>>> = ArrayList()
    private var mTotal = 0
    private val mIndexAry = SparseArray<Pair<AdapterDataObserver, SingAdapter<Any>>>()

    /**
     * 防止Cantor(康托)算法溢出int和long最大值
     */
    private val mCantorTemp = ArrayList<Long>()
    private val cantorReverse = LongArray(2)

    init {
        if (threadSafe) {
            mIndexGen = AtomicInteger(0)
        }
        mHasConsistItemType = hasConsistItemType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (mHasConsistItemType) {
            return mItemTypeAry[viewType]!!.onCreateViewHolder(parent, viewType)
        }
        // reverse Cantor Function
        Cantor.reverseCantor(viewType.toLong(), cantorReverse)
        val index = cantorReverse[1].toInt()
        val subItemType = cantorReverse[0].toInt()
        return findAdapterByIndex(index)!!.onCreateViewHolder(parent, subItemType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        val pair = findAdapter(position) ?: return
        pair.second?.onBindViewHolder((holder as ViewHolder), position - pair.first.startPosition, payloads)
        pair.second?.onBindViewHolderWithOffset(holder, position - pair.first.startPosition, position, payloads)
    }

    override fun getItemCount() = mTotal

    /**
     * Big integer of itemType returned by delegated adapter may lead to failed
     *
     * @param position item position
     * @return integer represent item view type
     */
    override fun getItemViewType(position: Int): Int {
        val p = findAdapter(position) ?: return RecyclerView.INVALID_TYPE
        var subItemType = p.second?.getItemViewType(position - p.first.startPosition) ?: -1
        if (subItemType < 0) {
            // negative integer, invalid, just return
            return subItemType
        }
        if (mHasConsistItemType) {
            mItemTypeAry.put(subItemType, p.second!!)
            return subItemType
        }
        val index = p.first.index
        //防止Cantor(康托)算法溢出int和long最大值
        subItemType = getCantorToViewType(subItemType.toLong())
        return Cantor.getCantor(subItemType.toLong(), index.toLong()).toInt()
    }

    /**
     * 获取adapter转换成对应真实的ViewType
     * 需要在addadapter后才能调用
     *
     * @param adapter
     * @return
     */
    fun getAdapterItemViewType(adapter: SingAdapter<*>?): Int {
        if (adapter == null) {
            return RecyclerView.INVALID_TYPE
        }
        var subItemType = adapter.getItemViewType(0)
        if (subItemType < 0) {
            // negative integer, invalid, just return
            return subItemType
        }
        if (mHasConsistItemType) {
            return subItemType
        }
        val index = adapter.index
        //防止Cantor(康托)算法溢出int和long最大值
        subItemType = getCantorToViewType(subItemType.toLong())
        return Cantor.getCantor(subItemType.toLong(), index.toLong()).toInt()
    }

    /**
     * 防止Cantor(康托)算法溢出int和long最大值
     * 这里从新从1开始赋值
     *
     * @param cantor
     * @return
     */
    private fun getCantorToViewType(cantor: Long): Int {
        if (mCantorTemp.contains(cantor)) {
            //存在值,取出key
        } else {
            //新增一个key
            mCantorTemp.add(cantor)
        }
        return mCantorTemp.indexOf(cantor) + 1
    }

    override fun getItemId(position: Int): Long {
        val p = findAdapter(position) ?: return RecyclerView.NO_ID
        val itemId = p.second!!.getItemId(position - p.first.startPosition)
        if (itemId < 0) {
            return RecyclerView.NO_ID
        }
        val index = p.first.index
        /*
         * Now we have a pairing function problem, we use cantor pairing function for itemId.
         */return Cantor.getCantor(index.toLong(), itemId)
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        // do nothing
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        val position = holder.adapterPosition
        if (position >= 0 && holder is ViewHolder) {
            findAdapter(position)?.second?.onViewRecycled(holder)
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val position = holder.adapterPosition
        if (position >= 0 && holder is ViewHolder) {
            findAdapter(position)?.second?.onViewAttachedToWindow(holder)
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        val position = holder.adapterPosition
        if (position >= 0 && holder is ViewHolder) {
            findAdapter(position)?.second?.onViewDetachedFromWindow(holder)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        for (p in mAdapters) {
            p.second?.onAttachedToRecyclerView(recyclerView)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        for (p in mAdapters) {
            p.second?.onDetachedFromRecyclerView(recyclerView)
        }
        this.recyclerView = null
    }

    /**
     * You can not set layoutHelpers to delegate adapter
     */
    @Deprecated("")
    override fun setLayoutHelpers(helpers: List<LayoutHelper>) {
        throw UnsupportedOperationException("DelegateAdapter doesn't support setLayoutHelpers directly")
    }

    fun setAdapters(adapters: List<SingAdapter<Any>>) {
        clear()
        val helpers: MutableList<LayoutHelper> = LinkedList()
        var hasStableIds = true
        mTotal = 0
        var pair: Pair<AdapterDataObserver, SingAdapter<Any>>
        for (adapter in adapters) {
            // every adapter has an unique index id
            val observer = AdapterDataObserver(mTotal, if (mIndexGen == null) mIndex++ else mIndexGen!!.incrementAndGet())
            adapter.registerAdapterDataObserver(observer)
            hasStableIds = hasStableIds && adapter.hasStableIds()
            val helper = adapter.layoutHelper
            helper.itemCount = adapter.itemCount
            mTotal += helper.itemCount
            helpers.add(helper)
            pair = Pair.create(observer, adapter)
            mIndexAry.put(observer.index, pair)
            //设置附属到recycle
            if (recyclerView != null && adapter.recyclerView == null) {
                adapter.onAttachedToRecyclerView(recyclerView!!)
            }
            mAdapters.add(pair)
        }
        if (!hasObservers()) {
            super.setHasStableIds(hasStableIds)
        }
        super.setLayoutHelpers(helpers)
    }

    /**
     * Add adapters in `position`
     *
     * @param position the index where adapters added
     * @param adapters adapters
     */
    fun addAdapters(adapters: List<SingAdapter<Any>>, position: Int = mAdapters.size) {
        if (adapters.isEmpty()) {
            return
        }
        var position = min(max(position, 0), mAdapters.size)
        val newAdapter: MutableList<SingAdapter<Any>> = ArrayList()
        mAdapters.forEach {
            newAdapter.add(it.second)
        }
        adapters.forEach {
            newAdapter.add(position, it)
            position++
        }
        setAdapters(newAdapter)
    }


    fun addAdapter(adapter: SingAdapter<Any>, position: Int = mAdapters.size) {
        addAdapters(listOf(adapter), position)
    }

    fun removeFirstAdapter() = removeAdapter(mAdapters.getOrNull(0)?.second)

    fun removeLastAdapter() = removeAdapter(mAdapters.getOrNull(mAdapters.size - 1)?.second)

    fun removeAdapter(adapterIndex: Int) = removeAdapter(mAdapters.getOrNull(adapterIndex)?.second)

    fun removeAdapter(targetAdapter: SingAdapter<Any>?) {
        if (targetAdapter == null) {
            return
        }
        removeAdapters(listOf(targetAdapter))
    }

    fun removeAdapters(targetAdapters: List<SingAdapter<Any>>) {
        if (targetAdapters.isEmpty()) {
            return
        }
        val helpers = LinkedList(super.getLayoutHelpers())
        val newAdapter = mutableListOf<Pair<AdapterDataObserver, SingAdapter<Any>>>()
        mAdapters.filterTo(newAdapter) { pair ->
            val theOther = pair.second
            val contains = targetAdapters.contains(theOther)
            if (contains) {
                theOther.unregisterAdapterDataObserver(pair.first)
                val position = findAdapterPositionByIndex(pair.first.index)
                if (position >= 0 && position < helpers.size) {
                    helpers.removeAt(position)
                }
            }
            contains
        }
        mAdapters.clear()
        mAdapters += newAdapter
        setAdapters(mAdapters.map {
            it.second
        })
    }

    fun clear() {
        mTotal = 0
        mIndex = 0
        mIndexGen?.set(0)
        mLayoutManager.setLayoutHelpers(null)
        mAdapters.forEach {
            it.second?.unregisterAdapterDataObserver(it.first)
        }
        mItemTypeAry.clear()
        mAdapters.clear()
        mIndexAry.clear()
    }

    val adaptersCount: Int
        get() = mAdapters.size

    /**
     * @param absoultePosition
     * @return the relative position in sub adapter by the absoulte position in DelegaterAdapter. Return -1 if no sub adapter founded.
     */
    fun findOffsetPosition(absoultePosition: Int): Int {
        val p = findAdapter(absoultePosition) ?: return -1
        return absoultePosition - p.first.startPosition
    }

    private fun findAdapter(position: Int): Pair<AdapterDataObserver, SingAdapter<Any>>? {
        val count = mAdapters.size
        if (count == 0) {
            return null
        }
        var s = 0
        var e = count - 1
        var m: Int
        var rs: Pair<AdapterDataObserver, SingAdapter<Any>>? = null

        // binary search range
        while (s <= e) {
            m = (s + e) / 2
            rs = mAdapters[m]
            val endPosition = rs.first.startPosition + rs.second!!.itemCount - 1
            if (rs.first.startPosition > position) {
                e = m - 1
            } else if (endPosition < position) {
                s = m + 1
            } else if (rs.first.startPosition <= position && endPosition >= position) {
                break
            }
            rs = null
        }
        return rs
    }

    fun findAdapterPositionByIndex(index: Int): Int {
        val rs = mIndexAry[index]
        return if (rs == null) -1 else mAdapters.indexOf(rs)
    }

    fun findAdapterByIndex(index: Int): SingAdapter<Any>? {
        return mIndexAry[index]?.second
    }

    /**
     * 查找对应的adapter的开始position
     *
     * @param index
     * @return
     */
    fun findAdapterByIndexStartPosition(index: Int): Int {
        return mIndexAry[index]?.first?.startPosition ?: -1
    }

    /**
     * 获取这种类型的adapter对于的开始position
     */
    fun findAdapterByViewTypeStartPosition(viewType: Any?): Int {
        if (viewType == null) {
            return -1
        }
        for (adapter in mAdapters) {
            if (adapter.second != null && adapter.second?.getItemViewType(0) == AdapterUtils.viewTypeToVLayout(viewType)) {
                return adapter.first.startPosition
            }
        }
        return -1
    }

    /**
     * 查找对应position的adapter
     *
     * @param position
     * @return
     */
    fun findAdapterByPosition(position: Int): SingAdapter<Any>? {
        val a = findAdapter(position)
        return a?.second
    }

    /**
     * 获取这种类型的adapter
     */
    fun findAdapterByViewType(viewType: Any?): SingAdapter<Any>? {
        if (viewType == null) {
            return null
        }
        for (adapter in mAdapters) {
            if (adapter.second != null && adapter.second?.getItemViewType(0) == AdapterUtils.viewTypeToVLayout(viewType)) {
                return adapter.second
            }
        }
        return null
    }

    /**
     * 是否有这种类型的adapter
     */
    fun haveAdapterByViewType(viewType: Any?): Boolean {
        if (viewType == null) {
            return false
        }
        for (adapter in mAdapters) {
            if (adapter.second != null && adapter.second?.getItemViewType(0) == AdapterUtils.viewTypeToVLayout(viewType)) {
                return true
            }
        }
        return false
    }

    //更新全部数据
    fun notifyChanged() {
        mAdapters?.forEach {
            it?.second?.notifyDataSetChanged()
        }
    }

    inner class AdapterDataObserver(var startPosition: Int, index: Int) : RecyclerView.AdapterDataObserver() {
        var index = -1
        fun updateStartPositionAndIndex(startPosition: Int, index: Int) {
            this.startPosition = startPosition
            this.index = index
        }

        private fun updateLayoutHelper(): Boolean {
            if (index < 0) {
                return false
            }
            val idx = findAdapterPositionByIndex(index)
            if (idx < 0) {
                return false
            }
            val p = mAdapters[idx]
            val helpers: List<LayoutHelper> = LinkedList(layoutHelpers)
            val helper = helpers[idx]
            if (helper.itemCount != p.second?.itemCount) {
                // if itemCount changed;
                helper.itemCount = p.second?.itemCount ?: 0
                mTotal = startPosition + (p.second?.itemCount ?: 0)
                for (i in idx + 1 until mAdapters.size) {
                    val pair = mAdapters[i]
                    // update startPosition for adapters in following
                    pair.first.startPosition = mTotal
                    mTotal += pair.second?.itemCount ?: 0
                }

                // set helpers to refresh range
                super@MultipleAdapter.setLayoutHelpers(helpers)
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

        init {
            this.index = index
        }
    }


}