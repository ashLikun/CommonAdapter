package com.ashlikun.adapter.recyclerview.vlayout

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.SingleLayoutHelper
import com.ashlikun.adapter.recyclerview.CommonAdapter
import kotlin.math.abs

/**
 * 作者　　: 李坤
 * 创建时间: 2018/4/10 0010　20:48
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：VLayout的ItemAdapter
 */
abstract class SingAdapter<T>
/**
 * 要使用这个构造器
 * 这里就必须重写 @[SingAdapter.getLayoutId]方法
 */
@JvmOverloads constructor(context: Context, layoutId: Int = -1, datas: List<T>? = null,
                          var layoutHelper: LayoutHelper? = null)
    : CommonAdapter<T>(context, layoutId, datas) {

    /**
     * 内部的adapter建议只用一个type
     */
    var viewType: Any = this.javaClass
    private var observer: MultipleAdapter.AdapterDataObserver? = null

    open fun onCreateLayoutHelper() = layoutHelper


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
    override fun getStartPosition() = observer?.mStartPosition ?: super.getStartPosition()

    /**
     * 在Vlayout里面第几个
     */
    open val index: Int
        get() = observer?.index ?: -1

    /**
     * 这2个方法是父Adapter onBindViewHolder回掉的
     */
    open fun onBindViewHolderWithOffset(holder: RecyclerView.ViewHolder?, position: Int, offsetTotal: Int, payloads: List<Any?>?) {
        onBindViewHolderWithOffset(holder, position, offsetTotal)
    }

    /**
     * 这2个方法是父Adapter onBindViewHolder回掉的
     */
    open protected fun onBindViewHolderWithOffset(holder: RecyclerView.ViewHolder?, position: Int, offsetTotal: Int) {}


    override fun getItemViewType(position: Int) = abs(viewType.hashCode())


    override fun getItemCount(): Int {
        return if (layoutHelper != null && layoutHelper is SingleLayoutHelper) {
            layoutHelper!!.itemCount
        } else {
            super.getItemCount()
        }
    }

    /**
     * 添加到MultipleAdapter 之前的回调
     */
    open fun beforeAdd() {}

    /**
     * 添加到MultipleAdapter 之后的回调
     */
    open fun afterAdd() {}

    /**
     * 发送事件，由外部处理,一般是由某个界面处理
     * @param action 事件的类型
     * @param params 这个事件对应的参数
     * @return 是否处理，true：外部已经处理，内部就不处理了  ，，， false:未处理
     */
    open fun sendEvent(action: String, params: Map<String, Any> = emptyMap()): Boolean {
        return bus?.event?.get(action)?.invoke(params) ?: false
    }
}