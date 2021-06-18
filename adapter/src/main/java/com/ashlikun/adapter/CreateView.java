package com.ashlikun.adapter;

import android.view.View;

import androidx.viewbinding.ViewBinding;

/**
 * 作者　　: 李坤
 * 创建时间: 2021/6/18　17:11
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */
public class CreateView {
    View view;
    ViewBinding viewBinding;

    public CreateView(View view, Object viewBinding) {
        this.view = view;
        if (viewBinding != null) {
            try {
                if (viewBinding instanceof ViewBinding) {
                    this.viewBinding = (ViewBinding) viewBinding;
                    this.view = ((ViewBinding) viewBinding).getRoot();
                }
            } catch (NoClassDefFoundError e) {
                //这里允许容错
            }
        }
    }
}
