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

public class Neibu1ViewHolder extends MultiltemViewHolder<NeibuData, Data> {

    public Neibu1ViewHolder(Context context, View itemView) {
        super(context, itemView);
    }

    @Override
    public NeibuData builderData(Data data) {
        return data.data;
    }


    @Override
    public void convert(ViewHolder holder, NeibuData neibuData) {
        holder.setText(R.id.textView, neibuData.name);
    }
}
