package com.ashlikun.adapter.recyclerview.multiltem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.LayoutHelper;
import com.ashlikun.adapter.ViewHolder;
import com.ashlikun.adapter.recyclerview.BaseAdapter;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/4/10 0010　20:48
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：多表单的内部adapter,得自己穿件ViewHolder
 *
 * @OnLifecycleEvent(Lifecycle.Event.ON_RESUME) public void onResume() {
 * }
 */
public abstract class SingAdapter<T, VH extends ViewHolder> extends BaseAdapter<T, VH> {
    //内部的adapter建议只用一个type
    private Object viewType;

    /**
     * 这2个方法是父Adapter onBindViewHolder回掉的
     */
    protected void onBindViewHolderWithOffset(RecyclerView.ViewHolder holder, int position, int offsetTotal) {

    }


    /**
     * 这2个方法是父Adapter onBindViewHolder回掉的
     */
    protected void onBindViewHolderWithOffset(RecyclerView.ViewHolder holder, int position, int offsetTotal, List<Object> payloads) {
        onBindViewHolderWithOffset(holder, position, offsetTotal);
    }

    public SingAdapter(Context context, int layoutId, List<T> datas) {
        super(context, layoutId, datas);
    }

    public SingAdapter(Context context, int layoutId) {
        this(context, layoutId, null);
    }

    public SingAdapter(Context context, List<T> datas) {
        this(context, -1, datas);
    }

    /**
     * 要使用这个构造器
     * 这里就必须重写 @{@link SingAdapter#getLayoutId}方法
     */
    public SingAdapter(Context context) {
        this(context, -1, null);
    }

    @Override
    public VH onCreateViewHolder(final ViewGroup parent, int viewType) {
        ViewHolder holder = createHolder(parent);
        setListener(parent, holder, viewType);
        return (VH) holder;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.setPosition(position);
        convert(holder, mDatas == null ? null : mDatas.get(position));
    }

    /**
     * @author　　: 李坤
     * 创建时间: 2018/4/16 0016 上午 10:47
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：设置这个adapter的ViewType
     */
    public SingAdapter setViewType(Object viewType) {
        this.viewType = viewType;
        return this;
    }

    @Override
    public int getItemViewType(int position) {
        if (viewType == null) {
            return super.getItemViewType(position);
        }
        return Math.abs(viewType.hashCode());
    }


    public abstract LayoutHelper onCreateLayoutHelper();

    public abstract VH createHolder(final ViewGroup parent);

}