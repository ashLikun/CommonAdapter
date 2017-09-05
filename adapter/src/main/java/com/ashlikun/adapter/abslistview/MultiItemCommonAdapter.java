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

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder(mContext, getItemLayout(parent, getLayoutId(position, getItem(position))), position);
            holder.itemView.setTag(10086, this);
        } else {
            holder = (ViewHolder) convertView.getTag(10086);
            holder.setPosition(position);
        }
        convert(holder, getItem(position));
        return holder.itemView;
    }

}
