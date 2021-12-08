package com.ashlikun.adapter.recyclerview

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.viewbinding.ViewBinding
import com.ashlikun.adapter.ViewHolder
import com.ashlikun.adapter.recyclerview.vlayout.mode.AdapterBus
import com.ashlikun.adapter.recyclerview.vlayout.mode.OnAdapterEvent

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
 *
 * 简单使用
 *   binding.recyclerView.adapter = CommonAdapter(this, neibuData,
 *          binding = ItemViewBinding::class.java) { holder, t ->
 *      holder.binding<ItemViewBinding>().run {
 *          textView.text = t?.name
 *      }
 *  }.apply {
 *      onItemClickListener = { viewType, parent, view, data, position ->
 *          Log.e("onItemClickListener", data.name)
 *      }
 *  }
 */
open class CommonAdapter<T>(
    context: Context,
    initDatas: List<T>? = null,
    //创建ViewBinding的Class,与layoutId 二选一
    override val binding: Class<out ViewBinding>? = null,
    //布局文件
    override val layoutId: Int? = null,
    //1:创建Adapter回调的其他参数，一般用于改变UI , 2:事件的回调
    open var bus: AdapterBus? = null,

    //点击事件
    override var onItemClick: OnItemClick<T>? = null,
    override var onItemClickX: OnItemClickX<T>? = null,
    //长按事件
    override var onItemLongClick: OnItemLongClick<T>? = null,
    override var onItemLongClickX: OnItemLongClickX<T>? = null,

    //初始化的apply 便于执行其他代码,子类一定需要自己实现
    apply: (CommonAdapter<T>.() -> Unit)? = null,
    //转换
    open val convert: AdapterConvert<T>? = null
) : BaseAdapter<T, ViewHolder>(
    context = context,
    initDatas = initDatas
) {
    /**
     * 获取AdapterBus.STYLE
     */
    open val style
        get() = bus?.style

    init {
        apply?.invoke(this)

    }


    override fun convert(holder: ViewHolder, t: T?) {
        convert?.invoke(holder, t)
    }

    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = ViewHolder(context, createRoot(parent, viewType), this)
        setListener(holder, viewType)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setListener(holder, holder.itemViewType)
        convert(holder, getItemData(position))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads != null && payloads.isNotEmpty()) {
            if (!convert(holder, getItemData(position), payloads)) {
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
        if (bus == null) {
            bus = AdapterBus()
        }
        bus?.putEvent(action, eve)
        return this
    }

    /**
     * 添加参数，由外部处理,一般是由某个界面处理
     * @param key 参数类型
     * @param eve 这个数对应的参数
     */
    open fun putParam(key: String, eve: Any): CommonAdapter<T> {
        if (bus == null) {
            bus = AdapterBus()
        }
        bus?.putParam(key, eve)
        return this
    }

    /**
     * 获取Bus里面的key
     */
    open fun <T : OnAdapterEvent> event(key: String): T? = bus?.event(key)


    /**
     * 获取AdapterBus.STYLE
     */
    open fun params(key: String) = bus?.get<Any>(key)
}