//package com.ashlikun.adapter.recyclerview.common
//
//import android.content.Context
//import androidx.viewbinding.ViewBinding
//import com.ashlikun.adapter.TypeToken
//import com.ashlikun.adapter.ViewHolder
//import com.ashlikun.adapter.recyclerview.*
//import com.ashlikun.adapter.recyclerview.vlayout.mode.AdapterBus
//
///**
// * 作者　　: 李坤
// * 创建时间: 2021/12/8　23:04
// * 邮箱　　：496546144@qq.com
// *
// * 功能介绍：泛型dataBind形式的adapter
// */
//open class CommonBindingAdapter<T, VB : ViewBinding>(
//    context: Context,
//    initDatas: List<T>? = null,
//    //1:创建Adapter回调的其他参数，一般用于改变UI , 2:事件的回调
//    override var bus: AdapterBus = AdapterBus(),
//    //点击事件
//    override var onItemClick: OnItemClick<T>? = null,
//    override var onItemClickX: OnItemClickX<T>? = null,
//    //长按事件
//    override var onItemLongClick: OnItemLongClick<T>? = null,
//    override var onItemLongClickX: OnItemLongClickX<T>? = null,
//    //初始化的apply 便于执行其他代码,子类一定需要自己实现
//    apply: (CommonBindingAdapter<T, VB>.() -> Unit)? = null,
//    //转换,从新实现
//    open val bindConvertP: AdapterBindingPayloadsConvert<T, VB>? = null,
//    open val bindConvert: AdapterBindingConvert<T, VB>? = null
//) : CommonBaseAdapter<T>(context, initDatas) {
//
//    init {
//        apply?.invoke(this)
//    }
//
//    override fun convert(holder: ViewHolder, t: T?) {
//        super.convert(holder, t)
//        convert(holder, holder.binding as VB, t)
//    }
//
//    override fun convert(holder: ViewHolder, t: T?, payloads: MutableList<Any>) =
//        convert(holder, holder.binding as VB, t, payloads)
//
//
//    open fun convert(holder: ViewHolder, binding: VB, t: T?) {
//        bindConvert?.invoke(holder, binding, t)
//    }
//
//    open fun convert(holder: ViewHolder, binding: VB, t: T?, payloads: MutableList<Any>) =
//        bindConvertP?.invoke(holder, binding, t, payloads) ?: super.convert(
//            holder, t, payloads
//        )
//
//    override fun getBindClass(viewType: Int): Class<out ViewBinding>? {
//        return object : TypeToken<VB>() {
//        }.type as Class<out ViewBinding>
//    }
//
//    inline fun <reified T : Any> new(): T {
//        val clz = T::class.java
//
//        val mCreate = clz.getDeclaredConstructor()
//
//        mCreate.isAccessible = true
//
//        return mCreate.newInstance()
//
//    }
//}
//
//
////inline fun <reified T : Any> new(vararg params: Any) =
////    T::class.java.getDeclaredConstructor(*params.map { it::class.java }.toTypedArray())
////        .apply { isAccessible = true }.newInstance(*params)