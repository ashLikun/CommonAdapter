package com.ashlikun.adapter.recyclerview.vlayout

import android.content.Context
import android.util.Log
import com.ashlikun.adapter.recyclerview.vlayout.mode.AdapterBus
import com.ashlikun.adapter.recyclerview.vlayout.mode.IAdapterBindData
import kotlin.reflect.KClass

/**
 * 作者　　: 李坤
 * 创建时间: 2021/6/9　15:30
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：全局管理Adapter
 */
/**
 * 创建Adapter的回调
 */
typealias OnAdapterCreate = (OnAdapterCreateParams) -> SingAdapter<Any>


/**
 * 创建适配器的参数
 */
data class OnAdapterCreateParams(
    val context: Context,
    //总的适配器
    val adapter: MultipleAdapter,
    //数据,就是数据层
    val data: IAdapterBindData<*>?,
    //当前是第几个,对于外侧数据
    val index: Int = 0,
    //总共几个,对于外侧数据
    val count: Int = 0,
    //Adapter与外界交互的参数集合
    val bus: AdapterBus? = null
) {
    fun <T> getData(): T = data as T
    fun isLast() = index == count - 1
    fun isFast() = index == 0
}

/**
 * 反射创建Adapter
 * @param cls 构造函数必须是 (Context) 或者（Context,Data）或者（Context,AdapterBus），或者（Context,OnAdapterCreateParams）
 *
 */
class OnAdapterCreateClass(private val cls: KClass<out SingAdapter<Any>>) : OnAdapterCreate {
    override fun invoke(param: OnAdapterCreateParams): SingAdapter<Any> {
//        cls.constructors.new
//        cls.constructors.forEach {
//            val parameterTypes = it.typeParameters
//            if (parameterTypes.isNotEmpty() && parameterTypes[0].isAssignableFrom(android.content.Context::class.java)) {
//                if (parameterTypes.size == 2) {
//                    return when {
//                        parameterTypes[1].isAssignableFrom(AdapterBus::class.java) -> {
//                            it.newInstance(param.context, param.bus) as SingAdapter<*>
//                        }
//                        parameterTypes[1].isAssignableFrom(OnAdapterCreateParams::class.java) -> {
//                            it.newInstance(param.context, param) as SingAdapter<*>
//                        }
//                        else -> it.newInstance(param.context, param.data?.data) as SingAdapter<*>
//                    }
//                } else if (parameterTypes.size == 1) {
//                    return it.newInstance(param.context) as SingAdapter<*>
//                }
//            }
//        }
        throw NullPointerException("这个Class：${cls},必须有双单参数或者双参数构造方法，并且第一个必须是Context,第二个必须是数据")
    }
}

object AdapterBind {
    /**
     * Adapter创建的回调,由外部创建Adapter
     */
    private val adapterCreates = mutableMapOf<String, OnAdapterCreate>()


    fun addCreate(type: String, create: OnAdapterCreate) {
        adapterCreates[type] = create
    }

    fun addCreates(creates: Map<String, OnAdapterCreate>) {
        adapterCreates.putAll(creates)
    }

    fun addCreateClass(type: String, create: KClass<out SingAdapter<Any>>) {
        adapterCreates[type] = OnAdapterCreateClass(create)
    }

    fun addCreatesClass(creates: Map<String, KClass<SingAdapter<Any>>>) {
        creates.forEach {
            addCreateClass(it.key, it.value)
        }
    }

    fun removeCreate(type: String? = null) {
        if (type != null) {
            adapterCreates.remove(type)
        }
    }

    fun removeCreates(types: List<String>) {
        types.forEach {
            adapterCreates.remove(it)
        }
    }

    fun removeCreates(vararg types: String) {
        types.forEach {
            adapterCreates.remove(it)
        }
    }

    /**
     * 绑定数据
     */
    fun bindUi(
        context: Context,
        adapter: MultipleAdapter,
        data: List<IAdapterBindData<*>>,
        busMap: Map<String, AdapterBus>? = null
    ) {
        data.forEachIndexed { index, it ->
            var adaCreate = adapterCreates[it.type]
            if (adaCreate != null) {
                val dataBus = it.getBus()
                dataBus?.type = it.type
                val adaBus = busMap?.get(it.type)?.plus(dataBus)
                //数据内部的
                val params = OnAdapterCreateParams(
                    context = context,
                    adapter = adapter,
                    data = it,
                    index = index,
                    count = data.size,
                    //其他参数，先用data的，如果为null，再用方法的
                    bus = adaBus
                )
                val ada = adaCreate.invoke(params)
                //赋值Type
                ada.viewType = it.type
                //设置事件管理
                ada.bus = adaBus
                //添加之前
                ada.beforeAdd(adapter, params)
                //添加
                adapter.addAdapter(ada)
                //添加之后
                ada.afterAdd(adapter, params)
            } else {
                //数据丢失
                Log.e(AdapterBind::class.java.name, "数据已经丢失->类型：${it.type}")
            }
        }
    }
}