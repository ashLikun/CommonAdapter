package com.ashlikun.adapter.abslistview;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

import com.ashlikun.adapter.ViewHolder;

import java.util.List;

public abstract class MultiItemCommonAdapter<T> extends CommonAdapter<T, ViewDataBinding> implements MultiItemTypeSupport<T> {


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

        int layoutId = getLayoutId(position,getItem(position));
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
                layoutId, position);
        convert(viewHolder, getItem(position));
        return viewHolder.getConvertView();
    }

}
