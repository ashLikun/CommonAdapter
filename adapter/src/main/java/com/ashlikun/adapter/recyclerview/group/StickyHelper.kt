package com.ashlikun.adapter.recyclerview.group

import androidx.recyclerview.widget.RecyclerView
import android.widget.FrameLayout
import android.content.Context
import android.util.SparseArray
import com.ashlikun.adapter.AdapterUtils
import java.lang.Exception
import android.view.View
import com.ashlikun.adapter.ViewHolder

/**
 * Depiction:头部吸顶布局。只要用StickyHeaderLayout包裹[RecyclerView],
 * 并且使用[GroupedCommonAdapter],就可以实现列表头部吸顶功能。
 * StickyHeaderLayout只能包裹RecyclerView，而且只能包裹一个RecyclerView。
 */
class StickyHelper(
    val recyclerView: RecyclerView,
    //吸顶容器，用于承载吸顶布局。
    val stickyLayout: FrameLayout
) {
    val context: Context = recyclerView.context

    //保存吸顶布局的缓存池。它以列表组头的viewType为key,ViewHolder为value对吸顶布局进行保存和回收复用。
    val stickyViews = SparseArray<ViewHolder>()

    //用于在吸顶布局中保存viewType的key。
    private val VIEW_TAG_TYPE = -20001

    //用于在吸顶布局中保存ViewHolder的key。
    private val VIEW_TAG_HOLDER = -20002

    //记录当前吸顶的组。
    var currentStickyGroup = -1
        private set

    //记录当前吸顶Position
    var currentStickyPosition = -1
        private set

    //是否吸顶。
    var isSticky = true
        set(value) {
            if (field != value) {
                field = value
                if (stickyLayout != null) {
                    if (field) {
                        stickyLayout.visibility = View.VISIBLE
                        updateStickyView(false)
                    } else {
                        recycle()
                        stickyLayout.visibility = View.GONE
                    }
                }
            }
        }

    //是否已经注册了adapter刷新监听
    private var isRegisterDataObserver = false

    init {
        addOnScrollListener()
    }

    /**
     * 添加滚动监听
     */
    private fun addOnScrollListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // 在滚动的时候，需要不断的更新吸顶布局。
                if (isSticky) {
                    updateStickyView(false)
                }
            }
        })
    }

    /**
     * 更新吸顶布局。
     *
     * @param imperative 是否强制更新。
     */
    private fun updateStickyView(imperative: Boolean = true) {
        val adapter = recyclerView.adapter
        //只有RecyclerView的adapter是GroupedCommonAdapter的时候，才会添加吸顶布局。
        if (adapter is GroupedCommonAdapter<*>) {
            val gAdapter = adapter
            registerAdapterDataObserver(gAdapter)
            //获取列表显示的第一个项。
            val firstVisibleItem =
                AdapterUtils.findFirstVisibleItemPosition(recyclerView.layoutManager)
            //通过显示的第一个项的position获取它所在的组。
            val groupPosition = gAdapter.getGroupPositionForPosition(firstVisibleItem)

            //如果当前吸顶的组头不是我们要吸顶的组头，就更新吸顶布局。这样做可以避免频繁的更新吸顶布局。
            if (imperative || currentStickyGroup != groupPosition) {
                currentStickyGroup = groupPosition
                //通过groupPosition获取当前组的组头position。这个组头就是我们需要吸顶的布局。
                currentStickyPosition = gAdapter.getPositionForGroupHeader(groupPosition)
                if (currentStickyPosition != -1) {
                    //获取吸顶布局的viewType。
                    val viewType = gAdapter.getItemViewType(currentStickyPosition)

                    //如果当前的吸顶布局的类型和我们需要的一样，就直接获取它的ViewHolder，否则就回收。
                    var holder = recycleStickyView(viewType)

                    //标志holder是否是从当前吸顶布局取出来的。
                    val flag = holder != null
                    if (holder == null) {
                        //从缓存池中获取吸顶布局。
                        holder = getStickyViewByType(viewType)
                    }
                    if (holder == null) {
                        //如果没有从缓存池中获取到吸顶布局，则通过GroupedCommonAdapter创建。
                        holder = gAdapter.createViewHolder(stickyLayout, viewType)
                        holder.itemView.setTag(VIEW_TAG_TYPE, viewType)
                        holder.itemView.setTag(VIEW_TAG_HOLDER, holder)
                    }
                    try {
                        //设置Position
                        AdapterUtils.setField(holder, "mPreLayoutPosition", currentStickyPosition)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    //通过GroupedCommonAdapter更新吸顶布局的数据。
                    //这样可以保证吸顶布局的显示效果跟列表中的组头保持一致。
                    gAdapter.onBindViewHolder(holder, currentStickyPosition)


                    //如果holder不是从当前吸顶布局取出来的，就需要把吸顶布局添加到容器里。
                    if (!flag) {
                        stickyLayout.addView(holder.itemView)
                    }
                } else {
                    //如果当前组没有组头，则不显示吸顶布局。
                    //回收旧的吸顶布局。
                    recycle()
                }
            }

            //这是是处理第一次打开时，吸顶布局已经添加到StickyLayout，但StickyLayout的高依然为0的情况。
            if (stickyLayout.childCount > 0 && stickyLayout.height == 0) {
                stickyLayout.requestLayout()
            }

            //设置stickyLayout的Y偏移量。
            for (i in 0 until stickyLayout.childCount) {
                stickyLayout.getChildAt(i).translationY =
                    calculateOffset(gAdapter, firstVisibleItem, groupPosition + 1)
            }
        }
    }

    /**
     * 注册adapter刷新监听
     */
    private fun registerAdapterDataObserver(adapter: GroupedCommonAdapter<*>) {
        if (!isRegisterDataObserver) {
            isRegisterDataObserver = true
            adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onChanged() {
                    updateStickyViewDelayed()
                }

                override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                    updateStickyViewDelayed()
                }

                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    updateStickyViewDelayed()
                }

                override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                    updateStickyViewDelayed()
                }
            })
        }
    }

    private fun updateStickyViewDelayed() {
        recyclerView.postDelayed({ updateStickyView(true) }, 64)
    }

    /**
     * 判断是否需要先回收吸顶布局，如果要回收，则回收吸顶布局并返回null。
     * 如果不回收，则返回吸顶布局的ViewHolder。
     * 这样做可以避免频繁的添加和移除吸顶布局。
     *
     * @param viewType
     * @return
     */
    private fun recycleStickyView(viewType: Int): ViewHolder? {
        if (stickyLayout.childCount > 0) {
            val view = stickyLayout.getChildAt(0)
            val type = view.getTag(VIEW_TAG_TYPE) as Int
            if (type == viewType) {
                return view.getTag(VIEW_TAG_HOLDER) as ViewHolder
            } else {
                recycle()
            }
        }
        return null
    }

    /**
     * 回收并移除吸顶布局
     */
    fun recycle() {
        if (stickyLayout.childCount > 0) {
            val view = stickyLayout.getChildAt(0)
            stickyViews.put(
                view.getTag(VIEW_TAG_TYPE) as Int,
                view.getTag(VIEW_TAG_HOLDER) as ViewHolder
            )
            stickyLayout.removeAllViews()
        }
    }

    /**
     * 从缓存池中获取吸顶布局
     *
     * @param viewType 吸顶布局的viewType
     * @return
     */
    fun getStickyViewByType(viewType: Int): ViewHolder {
        return stickyViews[viewType]
    }

    /**
     * 计算StickyLayout的偏移量。因为如果下一个组的组头顶到了StickyLayout，
     * 就要把StickyLayout顶上去，直到下一个组的组头变成吸顶布局。否则会发生两个组头重叠的情况。
     *
     * @param gAdapter
     * @param firstVisibleItem 当前列表显示的第一个项。
     * @param groupPosition    下一个组的组下标。
     * @return 返回偏移量。
     */
    private fun calculateOffset(
        gAdapter: GroupedCommonAdapter<*>,
        firstVisibleItem: Int,
        groupPosition: Int
    ): Float {
        val groupHeaderPosition = gAdapter.getPositionForGroupHeader(groupPosition)
        if (groupHeaderPosition != -1) {
            val index = groupHeaderPosition - firstVisibleItem
            if (recyclerView.childCount > index) {
                //获取下一个组的组头的itemView。
                val view = recyclerView.getChildAt(index)
                val off = view.y - stickyLayout.height
                if (off < 0) {
                    return off
                }
            }
        }
        return 0f
    }


}