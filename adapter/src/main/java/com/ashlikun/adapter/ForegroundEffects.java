package com.ashlikun.adapter;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/5/21 0021　上午 11:05
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：设置前景色效果的兼容
 */
public class ForegroundEffects {
    /**
     * 作者　　: 李坤
     * 创建时间: 2016/12/14 9:06
     * <p>
     * 方法功能：设置view的背景点击效果
     */

    public static void setForeground(int color, View view) {
        Drawable drawable;
        Drawable background = view.getBackground();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            color = color & 0xee000000;
            ColorStateList colorList = new ColorStateList(new int[][]{{}}, new int[]{color});
            drawable = new RippleDrawable(colorList, !isCanForeground(view) ? background : null,
                    background == null ? new ColorDrawable(Color.WHITE) : background);
        } else {
            if (isCanForeground(view)) {
                color = color & 0x33000000;
            }
            StateListDrawable bg = new StateListDrawable();
            Drawable colorDrawable;
            if (background instanceof GradientDrawable) {
                background.mutate();
                colorDrawable = background.getConstantState().newDrawable();
                ((GradientDrawable) colorDrawable).setColor(color);
            } else {
                colorDrawable = new ColorDrawable(color);
            }
            bg.addState(new int[]{android.R.attr.state_pressed}, colorDrawable);
            // View.EMPTY_STATE_SET
            bg.addState(new int[]{}, !isCanForeground(view) ? background : null);
            drawable = bg;
        }
        setForeground(view, drawable);
    }

    @SuppressLint("NewApi")
    public static void setForeground(View view, Drawable drawable) {
        if (isCanForeground(view)) {
            if (view instanceof FrameLayout) {
                ((FrameLayout) view).setForeground(drawable);
            } else {
                view.setForeground(drawable);
            }
        } else {
            //不可设置前景色，就只能设置背景色
            ViewCompat.setBackground(view, drawable);
        }
    }

    /**
     * 是否可以设置前景色
     *
     * @param view
     * @return
     */
    public static boolean isCanForeground(View view) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M || view instanceof FrameLayout;
    }
}
