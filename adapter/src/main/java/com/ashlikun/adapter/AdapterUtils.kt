package com.ashlikun.adapter

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewbinding.ViewBinding
import java.lang.reflect.AccessibleObject
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier

/**
 * 作者　　: 李坤
 * 创建时间: 2018/6/21 0021　上午 11:16
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：适配器的一些工具
 */
/**
 * 如果是MutableList 就是同一个对象，否则会转换成MutableList 就是个新对象
 */
inline fun <T> Collection<T>.toAutoMutableList(): MutableList<T> {
    return if (this is MutableList) this else this.toMutableList()
}

object AdapterUtils {
    private var viewBindingGetMap = mutableMapOf<Class<*>, AccessibleObject>()

    /**
     * 查找屏幕中最后一个item位置
     */
    fun findLastVisiblePosition(layoutManager: LayoutManager): Int {
        val lastVisibleItemPosition: Int
        lastVisibleItemPosition = if (layoutManager is GridLayoutManager) {
            layoutManager.findLastVisibleItemPosition()
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val into = layoutManager.findLastVisibleItemPositions(null)
            findMaxOrMin(into, true)
        } else {
            (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        }
        return lastVisibleItemPosition
    }

    /**
     * 查找屏幕中第一个item位置
     *
     * @param layoutManager
     * @return
     */
    fun findFirstVisibleItemPosition(layoutManager: LayoutManager?): Int {
        var firstVisibleItem = -1
        if (layoutManager != null) {
            if (layoutManager is GridLayoutManager) {
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
            } else if (layoutManager is LinearLayoutManager) {
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
            } else if (layoutManager is StaggeredGridLayoutManager) {
                val firstPositions = IntArray(layoutManager.spanCount)
                layoutManager.findFirstVisibleItemPositions(firstPositions)
                firstVisibleItem = findMaxOrMin(firstPositions, false)
            }
        }
        return firstVisibleItem
    }

    private fun findMaxOrMin(lastPositions: IntArray, isMax: Boolean): Int {
        var max = lastPositions[0]
        for (value in lastPositions) {
            if (isMax) {
                if (value > max) {
                    max = value
                }
            } else {
                if (value < max) {
                    max = value
                }
            }
        }
        return max
    }

    fun setFullSpan(holder: RecyclerView.ViewHolder) {
        if (holder.itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            val params = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
            params.isFullSpan = true
        }
    }

    fun viewTypeToVLayout(viewType: Any): Int {
        return Math.abs(viewType.hashCode())
    }

    /**
     * 反射字段
     *
     * @param object    要反射的对象
     * @param fieldName 要反射的字段名称
     */
    fun setField(`object`: Any?, fieldName: String?, value: Any?): Field? {
        if (`object` == null || fieldName == null || fieldName.isEmpty()) {
            return null
        }
        try {
            val field = getAllDeclaredField(`object`.javaClass, fieldName)
            if (field != null) {
                field.isAccessible = true
                field[`object`] = value
                return field
            }
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 获取指定的字段
     */
    fun getAllDeclaredField(claxx: Class<*>?, fieldName: String?): Field? {
        var claxx = claxx
        if (fieldName == null || fieldName.isEmpty()) {
            return null
        }
        while (claxx != null && claxx != Any::class.java) {
            try {
                val f = claxx.getDeclaredField(fieldName)
                if (f != null) {
                    return f
                }
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            }
            claxx = claxx.superclass
        }
        return null
    }

    /**
     * 方法功能：从context中获取activity，如果context不是activity那么久返回null
     */
    fun getActivity(context: Context?): Activity? {
        if (context == null) {
            return null
        }
        if (context is Activity) {
            return context
        } else if (context is ContextWrapper) {
            return getActivity(context.baseContext)
        } else if (context is ContextThemeWrapper) {
            return getActivity(context.baseContext)
        }
        return null
    }

    fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    /**
     * 反射查找ViewBinding的view
     *
     * @return 实例是ViewBinding
     */
    fun getViewBinding(`object`: Any?): ViewBinding? {
        if (`object` == null) {
            return null
        }
        try {
            //检测是否有ViewBinding 库
            val viewBindingCls: Class<*> = ViewBinding::class.java
            val objCls: Class<*> = `object`.javaClass
            //查找缓存
            var accessibleObject = viewBindingGetMap[objCls]
            if (accessibleObject != null) {
                accessibleObject.isAccessible = true
                if (accessibleObject is Method) {
                    return accessibleObject.invoke(`object`) as ViewBinding
                } else if (accessibleObject is Field) {
                    return accessibleObject[`object`] as ViewBinding
                }
            }
            var cls: Class<*>? = objCls
            while (cls != null && cls != Any::class.java) {
                //获取方法->返回值是
                val declaredMethods = cls.declaredMethods
                for (m in declaredMethods) {
                    if (viewBindingCls.isAssignableFrom(m.returnType)) {
                        m.isAccessible = true
                        val view = m.invoke(`object`) as ViewBinding
                        viewBindingGetMap[objCls] = m
                        return view
                    }
                }
                //获取父类的
                cls = cls.superclass
            }
        } catch (e: Exception) {
            return null
        } catch (e: NoClassDefFoundError) {
            return null
        }
        return null
    }


    /**
     * 获取3个参数的静态方法
     * @return ViewBinding
     */
    fun getViewBindingToClass(
        cls: Class<*>?,
        layoutInflater: LayoutInflater?,
        parent: ViewGroup?,
        attachToParent: Boolean
    ): Any? {
        if (cls == null || layoutInflater == null) {
            return null
        }
        //从缓存获取
        try {
            var inflate: Method? = null
            val existinflate = viewBindingGetMap[cls]
            if (existinflate is Method) {
                inflate = existinflate
            }
            if (inflate == null) {
                //直接取方法
                inflate = cls.getDeclaredMethod(
                    "inflate",
                    LayoutInflater::class.java,
                    ViewGroup::class.java,
                    Boolean::class.javaPrimitiveType
                )
                //这里循环全部方法是为了混淆的时候无影响
                if (inflate == null) {
                    val declaredMethods = cls.declaredMethods
                    for (declaredMethod in declaredMethods) {
                        val modifiers = declaredMethod.modifiers
                        if (Modifier.isStatic(modifiers)) {
                            val parameterTypes = declaredMethod.parameterTypes
                            if (parameterTypes != null && parameterTypes.size == 3) {
                                if (LayoutInflater::class.java.isAssignableFrom(parameterTypes[0]) &&
                                    ViewGroup::class.java.isAssignableFrom(parameterTypes[1]) &&
                                    Boolean::class.javaPrimitiveType!!.isAssignableFrom(
                                        parameterTypes[2]
                                    )
                                ) {
                                    inflate = declaredMethod
                                    break
                                }
                            }
                        }
                    }
                }
                //添加到缓存
                if (inflate != null) {
                    viewBindingGetMap[cls] = inflate
                }
            }
            if (inflate != null) {
                return inflate.invoke(null, layoutInflater, parent, attachToParent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}