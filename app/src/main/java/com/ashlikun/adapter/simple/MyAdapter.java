package com.ashlikun.adapter.simple;

import android.content.Context;
import android.view.View;

import com.ashlikun.adapter.recyclerview.MultiItemAdapter;
import com.ashlikun.adapter.recyclerview.MultiltemViewHolder;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/4/9 0009　下午 5:17
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class MyAdapter extends MultiItemAdapter<Data> {

    public MyAdapter(Context context, List<Data> datas) {
        super(context, datas);
    }

    @Override
    public int getLayoutId(int itemType) {
        if (itemType == 1) {
            return R.layout.item_view;
        } else if (itemType == 2) {
            return R.layout.item_view1;
        } else if (itemType == 3) {
            return R.layout.item_view2;
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position, Data data) {
        return data.type;
    }

    @Override
    public MultiltemViewHolder createViewHolder(Context context, View view, int itemType) {
        if (itemType == 1) {
            return new Neibu1ViewHolder(context, view);
        } else if (itemType == 2) {
            return new Neibu2ViewHolder(context, view);
        } else if (itemType == 3) {
            return new Neibu3ViewHolder(context, view);
        }
        return null;
    }
}
