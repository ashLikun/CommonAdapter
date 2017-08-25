package com.ashlikun.adapter.databind.abslistview;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.util.SparseArray;

import com.ashlikun.adapter.databind.DataBindHolder;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间:2017/8/13 0013　3:05
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：公共的 简单的 RecycleView Adapter;
 */

public class EasyBindAdapter extends CommonBindAdapter<Object, ViewDataBinding> {
    private int variableId;
    private SparseArray<Object> variables;

    public EasyBindAdapter(Context context, int layoutId, List datas, int variableId) {
        super(context, layoutId, datas);
        this.variableId = variableId;
    }


    //添加全局的variable
    public void addVariable(int variableId, Object value) {
        if (variables == null) variables = new SparseArray<>();
        variables.append(variableId, value);
    }

    @Override
    public void convert(DataBindHolder<ViewDataBinding> holder, Object o) {
        holder.dataBind.setVariable(variableId, o);
        if (variables != null) {
            for (int i = 0; i < variables.size(); i++) {
                holder.dataBind.setVariable(variables.keyAt(i), variables.valueAt(i));
            }
        }
    }
}
