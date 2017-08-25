package com.ashlikun.adapter.abslistview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.ashlikun.adapter.ViewHolder;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/25 10:29
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：多种布局的ListView  adapter
 */

public abstract class MultiItemCommonAdapter<T> extends CommonAdapter<T> implements MultiItemTypeSupport<T> {


    public MultiItemCommonAdapter(Context context, List<T> datas) {
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
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
                layoutId, position);
        convert(viewHolder, getItem(position));
        return viewHolder.getConvertView();
    }

}
