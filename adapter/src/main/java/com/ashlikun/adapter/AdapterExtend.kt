package com.ashlikun.adapter

/**
 * 作者　　: 李坤
 * 创建时间: 2021/6/18　18:52
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 */
/**
 * 从ViewHolder里面获取viewBinding
 */
inline fun <T> ViewHolder.binding(block: T.() -> Unit) {
    block(getViewBinding())
}
