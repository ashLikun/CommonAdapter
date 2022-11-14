package com.ashlikun.adapter.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.ashlikun.adapter.*
import com.ashlikun.adapter.ForegroundEffects.setForeground
import com.ashlikun.adapter.animation.AdapterAnimHelp
import com.ashlikun.adapter.animation.BaseAnimation
import com.ashlikun.adapter.recyclerview.click.SingleClickListener
import com.ashlikun.adapter.recyclerview.vlayout.IStartPosition

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
 * 4:[OnItemClick],[OnItemLongClick]
 * 5:前景点击效果水波纹
 * 6:灵活操作数据[DataHandle]
 * 7:布局可用XML，也可实现[BaseAdapter.createLayout]代码生成
 */
typealias OnItemClickX<T> = (viewType: Int, parent: ViewGroup, view: View, data: T, position: Int) -> Unit
typealias OnItemLongClickX<T> = (viewType: Int, parent: ViewGroup, view: View, data: T, position: Int) -> Boolean
typealias OnItemClick<T> = (data: T) -> Unit
typealias OnGetItemId<T> = (data: T) -> Any
typealias OnItemLongClick<T> = (data: T) -> Boolean
typealias AdapterConvert<T> = ViewHolder.(t: T) -> Unit
typealias AdapterPayloadsConvert<T> = ViewHolder.(t: T, payloads: MutableList<Any>) -> Boolean

//无参数的this回调
typealias NoParamsThis<Th> = Th.() -> Unit

//这里想封装泛型Binding的，由于kotlin泛型类不是匿名内部内的话不能获取泛型类型，name一直是T
typealias AdapterBindingConvert<T, VB> = (holder: ViewHolder, binding: VB, t: T?) -> Unit
typealias AdapterBindingPayloadsConvert<T, VB> = (holder: ViewHolder, binding: VB, t: T?, payloads: MutableList<Any>) -> Boolean

/**
 * 设计一个基类时，应该避免在构造函数、属性初始化器以及 init 块中使用 open 成员。
 */
abstract class BaseAdapter<T, V : RecyclerView.ViewHolder>(
    context: Context,
    //如果是MutableList可变的就直接引用
    initDatas: List<T>? = null,
    open val binding: Class<out ViewBinding>? = null,
    //布局文件
    open val layoutId: Int? = null,
    //布局
    open val layouView: View? = null
) : RecyclerView.Adapter<V>(), IHeaderAndFooter, SimpleLifecycleObserver, IStartPosition {
    var context: Context = context

    override var footerSize = 0
    override var headerSize = 0

    //暴力点击的延迟
    open var clickDelay = 500

    //数据处理
    open var dataHandle = DataHandle(
        //如果是MutableList可变的就直接引用
        initDatas?.toAutoMutableList() ?: mutableListOf(),
        this
    )

    //获取id
    open var itemId: OnGetItemId<T>? = null
    open var itemIdIsPosition: Boolean? = null

    //点击事件
    open var onItemClick: OnItemClick<T>? = null
    open var onItemClickX: OnItemClickX<T>? = null

    //长按事件
    open var onItemLongClick: OnItemLongClick<T>? = null
    open var onItemLongClickX: OnItemLongClickX<T>? = null

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
        /**
         * 方法的名称意思是设置是否有稳定的id，设置了该值为true后，ViewHolder中的mHasStableIds就为true。
         * StableId有三种模式：NO_STABLE_IDS、ISOLATED_STABLE_IDS、SHARED_STABLE_IDS
         * RecyclerView在进行Item的Remove，Insert，Change的时候会调用到。
         * 如果设置了这个属性，那么需要在Adapter中重写getItemId(int position)方法。
         * 这样子在进行列表的更新时候，Adapter会根据getItemId方法返回的long类型的id进行判断，决定当前的item是否需要刷新。因此取代以往的全部刷新的情况，从而提高效率。
         */
        setHasStableIds(true)
    }

    /**
     * 设置新的数据源
     * 设置的时候可以用  setData方法
     */
    open var datas: MutableList<T>
        get() = dataHandle.data
        set(value) {
            dataHandle.setDatas(value)
        }

    /**
     * 是否空的
     */
    open val isEmpty: Boolean
        get() = dataHandle.isEmpty

    /**
     * 绑定数据
     */
    abstract fun convert(holder: V, t: T)

    /**
     * recycleView的局部刷新
     *
     * @param payloads 一定不为null
     * @return true：代表处理完毕，不走另一个刷新的方法，false表示没有处理，继续走另一个
     */
    open fun convert(holder: V, t: T, payloads: MutableList<Any>): Boolean {
        return false
    }

    /**
     * 获取布局
     */
    open fun getLayoutId(viewType: Int): Int? = layoutId


    /**
     * 获取布局 ViewBindClass
     */
    open fun getBindClass(viewType: Int) = binding


    /**
     * 获取开始的position
     * 可能有头
     * 或者Vlayout内部是多个adapter
     */
    override val startPosition: Int
        get() = headerSize


    /**
     * 只有都无法创建view的时候才会用这个方法
     *
     * @return
     */
    open fun createViewBinding(
        parent: ViewGroup,
        binding: Class<out ViewBinding>?,
        viewType: Int
    ): Any? {
        return if (binding != null) {
            //反射获取
            AdapterUtils.getViewBindingToClass(
                binding,
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
        } else layouView
    }

    /**
     *
     * 创建跟布局
     * 不可从写
     */
    fun createRoot(parent: ViewGroup, viewType: Int): CreateView {
        val view = createLayout(parent, getLayoutId(viewType) ?: View.NO_ID, viewType)
        var viewBinding: Any? = null
        if (view == null) {
            //调用创建ViewBinding
            viewBinding = createViewBinding(parent, getBindClass(viewType), viewType)
        }
        return CreateView.create(view, viewBinding)
    }


    /**
     * Adapter会根据getItemId方法返回的long类型的id进行判断，决定当前的item是否需要刷新。因此取代以往的全部刷新的情况，从而提高效率。
     */
    override fun getItemId(position: Int): Long {
        if (itemIdIsPosition == true) return (startPosition + position).toLong()
        return getItemData(position)?.let { itemId?.invoke(it)?.toString()?.toLongOrNull() ?: hashCode().toLong() }
            ?: (startPosition + position).toLong()
    }

    override fun getItemCount(): Int {
        return dataHandle.itemCount
    }


    /**
     * 是否可以点击
     */
    open protected fun isEnabled(viewType: Int): Boolean {
        return true
    }

    /**
     * 设置新的数据源
     * @param datas 如果是MutableList 就是同一个对象，否则会转换成MutableList 就是个新对象
     * @param isNotify 是否通知适配器刷新
     */
    open fun setDatas(datas: List<T>?, isNotify: Boolean = false) {
        dataHandle.setDatas(datas, isNotify)
    }

    /**
     * 添加数据
     * @param isNotify 是否通知适配器刷新
     */
    open fun addDatas(datas: List<T>?, isNotify: Boolean = false) {
        dataHandle.addDatas(datas, isNotify)
    }

    /**
     * 清空全部数据
     * @param isNotify 是否通知适配器刷新
     */
    open fun clearData(isNotify: Boolean = false) {
        dataHandle.clearData(isNotify)
    }

    /**
     * 获取position对应的数据
     */
    open fun getItemData(position: Int) = dataHandle.getItemData(position)

    open protected fun setListener(viewHolder: ViewHolder, viewType: Int) {
        //更新监听
        viewHolder.iStartPosition = this
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
    open protected fun dispatchClick(
        v: View, viewHolder: ViewHolder, viewType: Int
    ) {
        val position = viewHolder.positionInside
        val d = getItemData(position)
        if (d != null && recyclerView != null) {
            //事件之前判断是否有事件总线
            onItemClick(viewType, recyclerView!!, v, d, position)
            onItemClick?.invoke(d)
            onItemClickX?.invoke(viewType, recyclerView!!, v, d, position)
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
    open protected fun dispatchLongClick(
        v: View, viewHolder: ViewHolder, viewType: Int
    ): Boolean {
        val position = viewHolder.positionInside
        val d = getItemData(position)
        if (d != null && recyclerView != null) {
            return if (onItemLongClick(viewType, recyclerView!!, v, d, position)) true
            else {
                onItemLongClick?.invoke(d)
                    ?: (onItemLongClickX?.invoke(viewType, recyclerView!!, v, d, position)
                        ?: false)
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
    open protected fun getForegroundView(viewHolder: ViewHolder, viewType: Int): View {
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
    open fun onViewAttachedToWindowAnim(holder: V) {}
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
    open val lastPosition: Int
        get() = if (recyclerView?.layoutManager == null) RecyclerView.NO_POSITION else AdapterUtils.findLastVisiblePosition(
            recyclerView?.layoutManager!!
        )

    /**
     * 屏幕中的第一个Item位置
     */
    open val firstPosition: Int
        get() = if (recyclerView?.layoutManager == null) RecyclerView.NO_POSITION else AdapterUtils.findFirstVisibleItemPosition(
            recyclerView!!.layoutManager
        )

    /**
     * 设置生命周期监听
     */
    open fun addLifecycle(lifecycle: Lifecycle) {
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