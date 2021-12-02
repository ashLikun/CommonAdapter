package com.ashlikun.adapter.recyclerview.click

import android.view.View

/**
 * 作者　　: 李坤
 * 创建时间: 2020/12/24　13:44
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：防止多次点击
 */
open class SingleClickListener(
    var lastClickTime: Long = 0,
    var clickDelay: Int = 500,
    var onSingleClick: ((View) -> Unit)? = null
) : View.OnClickListener {
    override fun onClick(v: View) {
        if (System.currentTimeMillis() - lastClickTime > clickDelay) {
            lastClickTime = System.currentTimeMillis()
            onSingleClick?.invoke(v)
        }
    }

}