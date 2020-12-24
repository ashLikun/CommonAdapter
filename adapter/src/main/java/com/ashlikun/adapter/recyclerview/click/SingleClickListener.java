package com.ashlikun.adapter.recyclerview.click;

import android.view.View;

/**
 * 作者　　: 李坤
 * 创建时间: 2020/12/24　13:44
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：防止多次点击
 */
public abstract class SingleClickListener implements View.OnClickListener {
    long lastClickTime = 0;
    int clickDelay = 500;

    public SingleClickListener(int clickDelay) {
        this.clickDelay = clickDelay;
    }

    public SingleClickListener() {
    }

    @Override
    public void onClick(View v) {
        if (System.currentTimeMillis() - lastClickTime > clickDelay) {
            lastClickTime = System.currentTimeMillis();
            onSingleClick(v);
        }
    }

    public abstract void onSingleClick(View v);
}
