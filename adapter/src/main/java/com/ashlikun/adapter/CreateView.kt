package com.ashlikun.adapter

import android.view.View
import androidx.viewbinding.ViewBinding

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/2 22:18
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：ViewHolder创建view的对象
 */

class CreateView(var view: View, var viewBinding: ViewBinding?) {

    companion object {
        fun create(view: View?, viewBinding: Any?): CreateView {
            if (viewBinding != null) {
                try {
                    if (viewBinding is ViewBinding) {
                        return CreateView(viewBinding.root, viewBinding)
                    }
                } catch (e: NoClassDefFoundError) {
                    //这里允许容错
                }
            }
            return CreateView(view!!, null)
        }
    }
}