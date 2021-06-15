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
typealias OnAdapterCreate = (OnAdapterCreateParams) -> SingAdapter<*>


/**
 * 创建适配器的参数
 */
data class OnAdapterCreateParams(
        val context: Context,
        //总的适配器
        val adapter: MultipleAdapter,
        //数据,就是数据层的getData
        val data: Any,
        //Adapter与外界交互的参数集合
        val bus: AdapterBus? = null
)

/**
 * 反射创建Adapter
 * @param cls 必须有双单参数或者双参数构造方法，并且第一个必须是Context,第二个必须是数据
 */
class OnAdapterCreateClass(val cls: Class<out SingAdapter<*>>) : OnAdapterCreate {
    override fun invoke(param: OnAdapterCreateParams): SingAdapter<*> {
        cls.constructors.forEach {
            val typeParameters = it.typeParameters
            if (typeParameters.size == 2 && typeParameters[0] is Context) {
                return it.newInstance(param.context, param.data) as SingAdapter<*>
            } else if (typeParameters.size == 1 && typeParameters[0] is Context) {
                return it.newInstance(param.context) as SingAdapter<*>
            }
        }
        throw NullPointerException("这个Class：${cls},必须有双单参数或者双参数构造方法，并且第一个必须是Context,第二个必须是数据")
    }
}

object AdapterBind {
    /**
     * Adapter创建的回调,由外部创建Adapter
     */
    private val adapterCreates = mutableMapOf<String, OnAdapterCreate>()

    /**
     * 类型对应Type
     */
    private val adapterType = mutableMapOf<KClass<*>, String>()


    fun addCreate(type: String, create: OnAdapterCreate) {
        adapterCreates[type] = create
    }

    fun addCreates(creates: Map<String, OnAdapterCreate>) {
        adapterCreates.putAll(creates)
    }

    fun addCreateClass(type: String, create: Class<out SingAdapter<*>>) {
        adapterCreates[type] = OnAdapterCreateClass(create)
    }

    fun addCreatesClass(creates: Map<String, Class<SingAdapter<*>>>) {
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
            data: List<IAdapterBindData>,
            busMap: Map<String, AdapterBus>? = null
    ) {
        data.forEach {
            var adaCreate = adapterCreates[it.getType()]
            if (adaCreate != null) {
                val dataBus = it.getBus()
                dataBus?.type = it.getType()
                val adaBus = busMap?.get(it.getType())?.plus(dataBus)
                //数据内部的
                val params = OnAdapterCreateParams(
                        context = context,
                        adapter = adapter,
                        data = it.getData(),
                        //其他参数，先用data的，如果为null，再用方法的
                        bus = adaBus
                )
                val ada = adaCreate.invoke(params)
                //赋值Type
                ada.viewType = it.getType()
                //设置事件管理
                ada.bus = adaBus
                //添加之前
                ada.beforeAdd()
                //添加
                adapter.addAdapter(ada)
                //添加之后
                ada.afterAdd()
                //保存类型对应的映射关系
                if (!adapterType.containsKey(ada::class)) {
                    adapterType[ada::class] = it.getType()
                }
            } else {
                //数据丢失
                Log.e(AdapterBind::class.java.name, "数据已经丢失->类型：${it.getType()}")
            }
        }
    }
}