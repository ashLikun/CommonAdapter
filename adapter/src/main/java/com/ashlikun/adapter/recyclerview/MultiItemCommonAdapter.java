package com.ashlikun.adapter.recyclerview;

import android.content.Context;
import android.view.ViewGroup;

import com.ashlikun.adapter.ViewHolder;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/25 9:56
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：多种列表样式的适配器,简单版
 * 添加生命周期
 *
 * @OnLifecycleEvent(Lifecycle.Event.ON_RESUME) public void onResume() {
 * }
 */

public abstract class MultiItemCommonAdapter<T> extends CommonAdapter<T> implements MultiItemTypeSupport<T> {

    public MultiItemCommonAdapter(Context context, List<T> datas) {
        super(context, -1, datas);
    }

    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, getItemData(position));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = getLayoutId(viewType);
        ViewHolder holder = new ViewHolder(mContext, getItemLayout(parent, layoutId), -1);
        holder.setHeaderSize(getHeaderSize());
        setListener(parent, holder, viewType);
        return holder;
    }

}
