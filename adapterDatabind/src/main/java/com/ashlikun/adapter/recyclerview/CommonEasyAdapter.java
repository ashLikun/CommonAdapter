package com.ashlikun.adapter.recyclerview;

import android.content.Context;
import android.databinding.ViewDataBinding;

import com.ashlikun.adapter.ViewHolder;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间:2017/8/13 0013　3:05
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class CommonEasyAdapter extends CommonAdapter<Object, ViewDataBinding> {
    private int variableId;

    public CommonEasyAdapter(Context context, int layoutId, List<Object> datas, int variableId) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, Object o) {
        holder.dataBind.setVariable(variableId, o);
    }
}
