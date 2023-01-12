package com.ashlikun.adapter.recyclerview.common

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.ashlikun.adapter.ViewHolder
import com.ashlikun.adapter.recyclerview.*
import com.ashlikun.adapter.recyclerview.vlayout.MultipleAdapter
import com.ashlikun.adapter.recyclerview.vlayout.OnAdapterCreateParams
import com.ashlikun.adapter.recyclerview.vlayout.mode.AdapterBus
import com.ashlikun.adapter.recyclerview.vlayout.mode.LayoutStyle
import com.ashlikun.adapter.recyclerview.vlayout.mode.OnAdapterEvent
import kotlin.math.abs

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/25 9:56
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：
 * 设计一个基类时，应该避免在构造函数、属性初始化器以及 init 块中使用 open 成员。
 * 公共的RecycleView的adapter
 * 在BaseAdapter基础上封装了 onCreateViewHolder,onBindViewHolder
 *          binding.recyclerView.adapter = CommonAdapter(
 *            this, neibuData, ItemViewBinding::class.java,
 *            onItemClick = {
 *                Toast.makeText(this, it.name, Toast.LENGTH_LONG).show()
 *            },
 *        ) { holder, t ->
 *            holder.binding<ItemViewBinding>().apply {
 *                textView.text = t?.name
 *            }
 *        }
 */
open class CommonAdapter<T>(
    context: Context,
    initDatas: List<T>? = null,
    override val binding: Class<out ViewBinding>? = null,
    //布局文件
    override val layoutId: Int? = null,
    //布局
    override val layouView: View? = null,
    //1:创建Adapter回调的其他参数，一般用于改变UI , 2:事件的回调,不能被覆盖
    var bus: AdapterBus = AdapterBus(),
    //VLayout布局，优先
    var layoutStyle: LayoutStyle = LayoutStyle(),
    //ViewType
    open var viewType: Any? = null,
    //获取id
    override var itemId: OnGetItemId<T>? = null,
    override var itemIdIsPosition: Boolean? = null,
    //点击事件
    override var onItemClick: OnItemClick<T>? = null,
    override var onItemClickX: OnItemClickX<T>? = null,
    //长按事件
    override var onItemLongClick: OnItemLongClick<T>? = null,
    override var onItemLongClickX: OnItemLongClickX<T>? = null,
    //初始化的apply 便于执行其他代码,子类一定需要自己实现,切换this 到Adapter
    apply: NoParamsThis<CommonAdapter<T>>? = null,
    //转换
    open var convertP: AdapterPayloadsConvert<T>? = null,
    open var convert: AdapterConvert<T>? = null
) : BaseAdapter<T, ViewHolder>(
    context = context,
    initDatas = initDatas
) {

    //VLayout的,可以用于区分是否是用于MultipleAdapter的
    var vLayoutObserver: MultipleAdapter.AdapterDataObserver? = null

    /**
     * 获取AdapterBus.STYLE
     */
    open val style
        get() = bus.style

    init {
        apply?.invoke(this)
    }

    override fun convert(holder: ViewHolder, t: T) {
        convert?.invoke(holder, t)
    }

    override fun convert(holder: ViewHolder, t: T, payloads: MutableList<Any>): Boolean {
        return convertP?.invoke(holder, t, payloads) ?: super.convert(holder, t, payloads)
    }

    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = ViewHolder(createRoot(parent, viewType), this, this)
        setListener(holder, viewType)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setListener(holder, holder.itemViewType)

        val data = getItemData(position)
        if (data != null || (vLayoutObserver != null && layoutStyle.isSingleLayout())) {
            convert(holder, data!!)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads != null && payloads.isNotEmpty()) {
            val data = getItemData(position)
            if (data != null && !convert(holder, data, payloads)) {
                super.onBindViewHolder(holder, position, payloads)
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun dispatchClick(v: View, viewHolder: ViewHolder, viewType: Int) {
        if (!sendEvent(AdapterBus.ITEM_CLICK)) {
            super.dispatchClick(v, viewHolder, viewType)
        }
    }

    override fun dispatchLongClick(v: View, viewHolder: ViewHolder, viewType: Int): Boolean {
        if (!sendEvent(AdapterBus.ITEM_LONG_CLICK)) {
            return super.dispatchLongClick(v, viewHolder, viewType)
        }
        return true
    }

    /**
     * 发送事件，由外部处理,一般是由某个界面处理
     * @param action 事件的类型
     * @param params 这个事件对应的参数
     * @return 是否处理，true：外部已经处理，内部就不处理了  ，，， false:未处理
     */
    open fun sendEvent(action: String, params: Map<String, Any> = emptyMap()): Boolean {
        return event<OnAdapterEvent>(action)?.invoke(params) ?: false
    }

    /**
     * 注册事件，由外部处理,一般是由某个界面处理
     * @param action 事件的类型
     * @param eve 这个事件对应回调
     */
    open fun putEvent(action: String, eve: OnAdapterEvent): CommonAdapter<T> {
        bus.putEvent(action, eve)
        return this
    }

    /**
     * 添加参数，由外部处理,一般是由某个界面处理
     * @param key 参数类型
     * @param eve 这个数对应的参数
     */
    open fun putParam(key: String, eve: Any): CommonAdapter<T> {
        bus.putParam(key, eve)
        return this
    }

    /**
     * 获取Bus里面的key
     */
    open fun <T : OnAdapterEvent> event(key: String): T? = bus.event(key)


    /**
     * 获取AdapterBus.STYLE
     */
    open fun params(key: String) = bus.get<Any>(key)


    /**
     * 获取开始的position
     * 可能有头
     * 或者Vlayout内部是多个adapter
     *
     * @return
     */
    override val startPosition: Int
        get() = vLayoutObserver?.startPosition ?: super.startPosition

    override fun getItemViewType(position: Int): Int {
        return if (viewType == null)
            if (vLayoutObserver == null) {
                super.getItemViewType(position);
            } else {
                //vLayoput的itemViewType必须是正数
                abs(
                    abs(binding?.hashCode() ?: 0) +
                            abs(layoutId?.hashCode() ?: 0) +
                            abs(this::class.hashCode())
                )
            }
        else abs(viewType.hashCode())
    }

    override fun getItemCount(): Int {
        return if (vLayoutObserver != null && layoutStyle.isSingleLayout()) layoutStyle.helper.itemCount
        else super.getItemCount()
    }

    /**
     * 添加到MultipleAdapter 之前的回调
     */
    open fun beforeAdd(adapter: MultipleAdapter, params: OnAdapterCreateParams) {}

    /**
     * 添加到MultipleAdapter 之后的回调
     */
    open fun afterAdd(adapter: MultipleAdapter, params: OnAdapterCreateParams) {}


    /**
     * 在Vlayout里面第几个
     */
    open val vLayoutIndex: Int
        get() = vLayoutObserver?.index ?: -1

    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        super.registerAdapterDataObserver(observer)
        if (observer is MultipleAdapter.AdapterDataObserver) {
            this.vLayoutObserver = observer
        }
    }

    override fun unregisterAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        super.unregisterAdapterDataObserver(observer)
        if (observer is MultipleAdapter.AdapterDataObserver) {
            this.vLayoutObserver = null
        }
    }
}