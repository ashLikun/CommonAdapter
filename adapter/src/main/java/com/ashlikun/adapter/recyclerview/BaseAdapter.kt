package com.ashlikun.adapter.recyclerview

import com.ashlikun.adapter.ForegroundEffects.setForeground
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import androidx.lifecycle.LifecycleObserver
import com.ashlikun.adapter.recyclerview.vlayout.IStartPosition
import com.ashlikun.adapter.animation.AdapterAnimHelp
import java.lang.Class
import com.ashlikun.adapter.recyclerview.vlayout.mode.AdapterBus
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import com.ashlikun.adapter.recyclerview.click.SingleClickListener
import com.ashlikun.adapter.animation.BaseAnimation
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import com.ashlikun.adapter.*

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/25 9:56
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：基础的RecycleView的adapter
 *
 *
 * 1:一行代码实现item[BaseAdapter.convert]
 * 2:添加生命周期
 * 先添加监听 addLifecycle(lifecycle)
 *
 * @OnLifecycleEvent(Lifecycle.Event.ON_RESUME) public void onResume() {
 * }
 *
 *
 * 3：动画[BaseAdapter.getAdapterAnimHelp][BaseAdapter.setCustomAnimation]  -> [AdapterAnimHelp]
 * 4:[OnItemClickListener],[OnItemLongClickListener]
 * 5:前景点击效果水波纹
 * 6:灵活操作数据[DataHandle]
 * 7:布局可用XML，也可实现[BaseAdapter.createLayout]代码生成
 */
typealias OnItemClickListener<T> = (viewType: Int, parent: ViewGroup, view: View, data: T, position: Int) -> Unit
typealias OnItemLongClickListener<T> = (viewType: Int, parent: ViewGroup, view: View, data: T, position: Int) -> Boolean
typealias OnSimpleItemClickListener<T> = (data: T) -> Unit
typealias OnSimpleItemLongClickListener<T> = (data: T) -> Boolean
typealias AdapterConvert<T> = (holder: ViewHolder, t: T?) -> Unit

abstract class BaseAdapter<T, V : RecyclerView.ViewHolder>(
    open var context: Context,
    initDatas: MutableList<T>,
    //创建ViewBinding的Class,与layoutId 二选一
    open var bindingClass: Class<out ViewBinding>? = null,
    //布局文件
    open var layoutId: Int = DEFAULT_LAYOUT_ID

) : RecyclerView.Adapter<V>(), IHeaderAndFooter, LifecycleObserver, IStartPosition {

    companion object {
        @JvmField
        var DEFAULT_LAYOUT_ID = View.NO_ID
    }

    override var footerSize = 0
    override var headerSize = 0

    //暴力点击的延迟
    open var clickDelay = 500

    //数据处理
    open var dataHandle = DataHandle(initDatas, this)

    //点击事件
    open var onItemClickListener: OnItemClickListener<T>? = null
    open var onSimpleItemClickListener: OnSimpleItemClickListener<T>? = null

    //长按事件
    open var onItemLongClickListener: OnItemLongClickListener<T>? = null
    open var onSimpleItemLongClickListener: OnSimpleItemLongClickListener<T>? = null

    //获取动画帮助类
    open var adapterAnimHelp = AdapterAnimHelp(this)


    //item点击颜色
    private var itemClickColor: Int? = null

    //是否打开点击效果
    open var isOpenClickEffects = false

    //当前附属的RecyclerView
    open var recyclerView: RecyclerView? = null
        protected set


    init {
        setHasStableIds(true)
    }


    /**
     * 设置新的数据源
     */
    var datas: MutableList<T>
        get() = dataHandle.data
        set(value) {
            dataHandle.setDatas(value)
        }
    val isEmpty: Boolean
        get() = dataHandle.isEmpty

    /**
     * 绑定数据
     */
    abstract fun convert(holder: V, t: T?)

    /**
     * 获取布局
     */
    open fun getLayoutId(viewType: Int) = layoutId


    /**
     * 获取布局 ViewBindClass
     */
    open fun getBindClass(viewType: Int) = bindingClass

    /**
     * recycleView的局部刷新
     *
     * @param payloads 一定不为null
     * @return true：代表处理完毕，不走另一个刷新的方法，false表示没有处理，继续走另一个
     */
    fun convert(holder: V, t: T?, payloads: List<Any?>?): Boolean {
        return false
    }

    /**
     * 获取开始的position
     * 可能有头
     * 或者Vlayout内部是多个adapter
     *
     * @return
     */
    override fun getStartPosition(): Int {
        return headerSize
    }


    /**
     * 只有都无法创建view的时候才会用这个方法
     *
     * @return
     */
    open fun createViewBinding(parent: ViewGroup, bindingClass: Class<*>?, viewType: Int): Any? {
        return if (bindingClass != null) {
            //反射获取
            AdapterUtils.getViewBindingToClass(
                bindingClass,
                LayoutInflater.from(context),
                parent,
                false
            )
        } else null
    }

    /**
     * 可以重写这个方法，用java代码写布局,构造方法就不用传layoutID了
     */
    open fun createLayout(parent: ViewGroup, layoutId: Int, viewType: Int): View? {
        return if (layoutId != View.NO_ID) {
            LayoutInflater.from(context).inflate(layoutId, parent, false)
        } else null
    }

    /**
     *
     * 创建跟布局
     * 不可从写
     */
    fun createRoot(parent: ViewGroup, viewType: Int): CreateView {
        val view = createLayout(parent, getLayoutId(viewType), viewType)
        var viewBinding: Any? = null
        if (view == null) {
            //调用创建ViewBinding
            viewBinding = createViewBinding(parent, getBindClass(viewType), viewType)
        }
        return CreateView.create(view, viewBinding)
    }

    override fun getItemId(position: Int): Long {
        val d = getItemData(position)
        return d?.hashCode()?.toLong() ?: (getStartPosition() + position).toLong()
    }

    override fun getItemCount(): Int {
        return dataHandle.itemCount
    }


    protected fun getPosition(viewHolder: ViewHolder): Int {
        return viewHolder.positionInside
    }

    /**
     * 是否可以点击
     */
    protected fun isEnabled(viewType: Int): Boolean {
        return true
    }

    /**
     * 设置新的数据源
     * @param isNotify 是否通知适配器刷新
     */
    fun setDatas(datas: MutableList<T>?, isNotify: Boolean = false) {
        dataHandle.setDatas(datas, isNotify)
    }

    /**
     * 添加数据
     * @param isNotify 是否通知适配器刷新
     */
    fun addDatas(datas: List<T>?, isNotify: Boolean = false) {
        dataHandle.addDatas(datas, isNotify)
    }

    /**
     * 清空全部数据
     * @param isNotify 是否通知适配器刷新
     */
    fun clearData(isNotify: Boolean = false) {
        dataHandle.clearData(isNotify)
    }

    /**
     * 获取position对应的数据
     */
    open fun getItemData(position: Int) = dataHandle.getItemData(position)

    protected fun setListener(viewHolder: ViewHolder, viewType: Int) {
        if (!isEnabled(viewType)
            || !viewHolder.itemView.isEnabled
        ) {
            return
        }
        if (isOpenClickEffects) {
            val color = itemClickColor ?: viewHolder.itemClickColor
            if (!viewHolder.isSetEffects(color)) {
                setForeground(color, getForegroundView(viewHolder, viewType))
                viewHolder.setEffects(color)
            }
        }
        viewHolder.itemView.setOnClickListener(SingleClickListener(clickDelay = clickDelay) {
            dispatchClick(it, viewHolder, viewType)
        })
        viewHolder.itemView.setOnLongClickListener { v ->
            dispatchLongClick(v, viewHolder, viewType)
        }
    }

    /**
     * 分发点击事件
     */
    protected open fun dispatchClick(
        v: View, viewHolder: ViewHolder, viewType: Int
    ) {
        val position = getPosition(viewHolder)
        val d = getItemData(position)
        if (d != null && recyclerView != null) {
            //事件之前判断是否有事件总线
            onItemClick(viewType, recyclerView!!, v, d, position)
            onItemClickListener?.invoke(viewType, recyclerView!!, v, d, position)
            onSimpleItemClickListener?.invoke(d)
        }
    }

    /**
     * 分发长按事件
     *
     * @param parent
     * @param v
     * @param viewHolder
     * @param viewType
     */
    protected open fun dispatchLongClick(
        v: View, viewHolder: ViewHolder, viewType: Int
    ): Boolean {
        val position = getPosition(viewHolder)
        val d = getItemData(position)
        if (d != null && recyclerView != null) {
            return if (onItemLongClick(viewType, recyclerView!!, v, d, position)) true
            else {
                onItemLongClickListener?.invoke(viewType, recyclerView!!, v, d, position)
                    ?: (onSimpleItemLongClickListener?.invoke(d) ?: false)
            }
        }
        return false
    }

    /**
     * 获取要设置前景色效果的view，23,FrameLayout
     *
     * @param viewHolder
     * @param viewType   因为这个方法是在设置事件里面调用的，此时ViewHolder还没有viewType
     * @return
     */
    protected fun getForegroundView(viewHolder: ViewHolder, viewType: Int): View {
        return viewHolder.itemView
    }


    override fun onViewAttachedToWindow(holder: V) {
        super.onViewAttachedToWindow(holder)
        onViewAttachedToWindowAnim(holder)
        adapterAnimHelp.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: V) {
        super.onViewDetachedFromWindow(holder)
        adapterAnimHelp.onViewDetachedFromWindow(holder)
    }

    /**
     * 附属于window时候动画
     *
     * @param holder
     */
    fun onViewAttachedToWindowAnim(holder: V) {}
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    fun setCustomAnimation(mCustomAnimation: BaseAnimation?) {
        adapterAnimHelp.setCustomAnimation(mCustomAnimation)
    }

    /**
     * 屏幕中的最后一个Item位置
     */
    val lastPosition: Int
        get() = if (recyclerView?.layoutManager == null) RecyclerView.NO_POSITION else AdapterUtils.findLastVisiblePosition(
            recyclerView!!.layoutManager
        )

    /**
     * 屏幕中的第一个Item位置
     */
    val firstPosition: Int
        get() = if (recyclerView?.layoutManager == null) RecyclerView.NO_POSITION else AdapterUtils.findFirstVisibleItemPosition(
            recyclerView!!.layoutManager
        )

    /**
     * 设置生命周期监听
     */
    fun addLifecycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }


    open fun onItemLongClick(
        viewType: Int,
        parent: ViewGroup,
        view: View,
        data: T,
        position: Int
    ): Boolean {
        return onItemLongClick(data)
    }

    open fun onItemLongClick(
        data: T
    ): Boolean {
        return false
    }

    open fun onItemClick(
        viewType: Int,
        parent: ViewGroup,
        view: View,
        data: T,
        position: Int
    ) {
        onItemClick(data)
    }


    open fun onItemClick(
        data: T
    ) {
    }
}