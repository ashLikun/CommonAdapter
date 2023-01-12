package com.ashlikun.adapter.recyclerview.vlayout

import android.content.Context
import android.view.View
import com.ashlikun.vlayout.VirtualLayoutManager

/**
 * 作者　　: 李坤
 * 创建时间: 2021/12/7　23:21
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：Vlayout的管理器
 */
open class MultipleLayoutManage(
    context: Context,
    orientation: Int = VERTICAL,
    reverseLayout: Boolean = false
) :
    VirtualLayoutManager(context, orientation, reverseLayout) {

    override fun getPosition(view: View): Int {

        return super.getPosition(view)
    }
}