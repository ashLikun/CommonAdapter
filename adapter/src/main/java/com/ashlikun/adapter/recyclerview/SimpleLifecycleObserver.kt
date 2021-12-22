package com.ashlikun.adapter.recyclerview

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * 作者　　: 李坤
 * 创建时间: 2021.12.22　15:18
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：简化DefaultLifecycleObserver
 */
internal interface SimpleLifecycleObserver : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        onCreate()
    }

    fun onCreate() {}
    override fun onStart(owner: LifecycleOwner) {
        onStart()
    }

    fun onStart() {}
    override fun onResume(owner: LifecycleOwner) {
        onResume()
    }

    fun onResume() {}
    override fun onPause(owner: LifecycleOwner) {
        onPause()
    }

    fun onPause() {}
    override fun onStop(owner: LifecycleOwner) {
        onStop()
    }

    fun onStop() {}
    override fun onDestroy(owner: LifecycleOwner) {
        onDestroy()
    }

    fun onDestroy() {}
}