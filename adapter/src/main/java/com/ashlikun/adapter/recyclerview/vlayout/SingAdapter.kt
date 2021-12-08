package com.ashlikun.adapter.recyclerview.vlayout

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.alibaba.android.vlayout.layout.MarginLayoutHelper
import com.ashlikun.adapter.recyclerview.AdapterConvert
import com.ashlikun.adapter.recyclerview.CommonAdapter
import com.ashlikun.adapter.recyclerview.vlayout.mode.AdapterBus
import com.ashlikun.adapter.recyclerview.vlayout.mode.LayoutStyle
import kotlin.math.abs

/**
 * 作者　　: 李坤
 * 创建时间: 2018/4/10 0010　20:48
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：VLayout的ItemAdapter
 */
open class SingAdapter<T>(
    context: Context,
    initDatas: List<T>? = null,
    //创建ViewBinding的Class,与layoutId 二选一
    override val binding: Class<out ViewBinding>? = null,
    //布局文件
    override val layoutId: Int? = null,
    //事件
    override var bus: AdapterBus? = null,
    //布局，优先
    var layoutStyle: LayoutStyle? = null,
    //ViewType
    open var viewType: Any? = null,
    //初始化的apply 便于执行其他代码
    apply: (SingAdapter<T>.() -> Unit)? = null,
    //转换
    override var convert: AdapterConvert<T>? = null
) : CommonAdapter<T>(
    context = context,
    initDatas = initDatas
) {
    var layoutHelper: LayoutHelper = LinearLayoutHelper()

    init {
        apply?.invoke(this)
        layoutStyle?.run {
            layoutHelper = createHelper()
        }
        //赋值MarginLayoutHelper
        if (layoutHelper is MarginLayoutHelper) {
            style?.bindHelperUI(layoutHelper!!)
        }
    }


    private var observer: MultipleAdapter.AdapterDataObserver? = null


    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        super.registerAdapterDataObserver(observer)
        if (observer is MultipleAdapter.AdapterDataObserver) {
            this.observer = observer
        }
    }

    override fun unregisterAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        super.unregisterAdapterDataObserver(observer)
        if (observer is MultipleAdapter.AdapterDataObserver) {
            this.observer = null
        }
    }

    /**
     * 获取开始的position
     * 可能有头
     * 或者Vlayout内部是多个adapter
     *
     * @return
     */
    override fun getStartPosition() = observer?.startPositionInside ?: super.getStartPosition()

    /**
     * 在Vlayout里面第几个
     */
    open val index: Int
        get() = observer?.index ?: -1

    /**
     * 这2个方法是父Adapter onBindViewHolder回掉的
     */
    open fun onBindViewHolderWithOffset(
        holder: RecyclerView.ViewHolder?,
        position: Int,
        offsetTotal: Int,
        payloads: List<Any?>?
    ) {
        onBindViewHolderWithOffset(holder, position, offsetTotal)
    }

    /**
     * 这2个方法是父Adapter onBindViewHolder回掉的
     */
    open protected fun onBindViewHolderWithOffset(
        holder: RecyclerView.ViewHolder?,
        position: Int,
        offsetTotal: Int
    ) {
    }


    override fun getItemViewType(position: Int) =
        if (viewType == null) abs(binding?.hashCode() ?: 0) + abs(
            layoutId?.hashCode() ?: 0
        ) + abs(this::class.hashCode()) else abs(viewType.hashCode())


    override fun getItemCount(): Int {
        return if (layoutHelper.itemCount > 0) {
            layoutHelper.itemCount
        } else {
            super.getItemCount()
        }
    }

    /**
     * 添加到MultipleAdapter 之前的回调
     */
    open fun beforeAdd(adapter: MultipleAdapter, params: OnAdapterCreateParams) {}

    /**
     * 添加到MultipleAdapter 之后的回调
     */
    open fun afterAdd(adapter: MultipleAdapter, params: OnAdapterCreateParams) {}
}