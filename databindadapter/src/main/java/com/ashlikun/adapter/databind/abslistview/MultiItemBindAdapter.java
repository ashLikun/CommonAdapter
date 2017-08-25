package com.ashlikun.adapter.databind.abslistview;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

import com.ashlikun.adapter.abslistview.MultiItemTypeSupport;
import com.ashlikun.adapter.databind.DataBindHolder;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/25 10:32
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：多种布局的DataBindAdapter
 */

public abstract class MultiItemBindAdapter<T> extends CommonBindAdapter<T, ViewDataBinding> implements MultiItemTypeSupport<T> {


    public MultiItemBindAdapter(Context context, List<T> datas) {
        super(context, -1, datas);
    }


    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position,
                mDatas.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int layoutId = getLayoutId(position, getItem(position));
        DataBindHolder viewHolder = DataBindHolder.get(mContext, convertView, parent,
                layoutId, position);
        convert(viewHolder, getItem(position));
        return viewHolder.getConvertView();
    }

}
