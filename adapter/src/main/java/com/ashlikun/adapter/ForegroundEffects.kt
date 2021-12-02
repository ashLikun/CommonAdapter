package com.ashlikun.adapter

import android.R
import android.view.View
import android.os.Build
import android.content.res.ColorStateList
import com.ashlikun.adapter.ForegroundEffects
import android.graphics.Color
import android.annotation.SuppressLint
import android.graphics.drawable.*
import android.widget.FrameLayout
import androidx.core.view.ViewCompat

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/2 22:20
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：设置前景色效果的兼容
 */

object ForegroundEffects {
    /**
     * 设置view的背景点击效果
     */
    fun setForeground(color: Int, view: View) {
        var color = color
        val drawable: Drawable
        val background = view.background
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            color = color and -0x11000001
            val colorList = ColorStateList(arrayOf(intArrayOf()), intArrayOf(color))
            drawable = RippleDrawable(
                colorList, if (!isCanForeground(view)) background else null,
                background ?: ColorDrawable(Color.WHITE)
            )
        } else {
            if (isCanForeground(view)) {
                color = color and 0x33FFFFFF
            } else if (color == Color.GRAY) {
                color = Color.LTGRAY
            }
            val bg = StateListDrawable()
            val colorDrawable: Drawable
            if (background is GradientDrawable) {
                background.mutate()
                colorDrawable = background.getConstantState().newDrawable()
                (colorDrawable as GradientDrawable).setColor(color)
            } else {
                colorDrawable = ColorDrawable(color)
            }
            bg.addState(intArrayOf(R.attr.state_pressed), colorDrawable)
            // View.EMPTY_STATE_SET
            bg.addState(intArrayOf(), if (!isCanForeground(view)) background else null)
            drawable = bg
        }
        setForeground(view, drawable)
    }

    @SuppressLint("NewApi")
    fun setForeground(view: View, drawable: Drawable?) {
        if (isCanForeground(view)) {
            if (view is FrameLayout) {
                view.setForeground(drawable)
            } else {
                view.foreground = drawable
            }
        } else {
            //不可设置前景色，就只能设置背景色
            ViewCompat.setBackground(view, drawable)
        }
    }

    /**
     * 是否可以设置前景色
     *
     * @param view
     * @return
     */
    fun isCanForeground(view: View?): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M || view is FrameLayout
    }
}