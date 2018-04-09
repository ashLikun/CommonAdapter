package com.ashlikun.adapter.simple;

import android.content.Context;
import android.view.View;

import com.ashlikun.adapter.ViewHolder;
import com.ashlikun.adapter.recyclerview.MultiltemViewHolder;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/4/9 0009　下午 5:30
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class Neibu2ViewHolder extends MultiltemViewHolder<Neibu2Data, Data> {

    public Neibu2ViewHolder(Context context, View itemView) {
        super(context, itemView);
    }

    @Override
    public Neibu2Data builderData(Data data) {
        return data.data2;
    }


    @Override
    public void convert(ViewHolder holder, Neibu2Data neibuData) {
        holder.setText(R.id.textView, neibuData.name);
    }
}
