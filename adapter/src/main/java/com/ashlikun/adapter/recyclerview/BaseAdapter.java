package com.ashlikun.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.ashlikun.adapter.ViewHolder;
import com.ashlikun.adapter.recyclerview.click.OnItemClickListener;
import com.ashlikun.adapter.recyclerview.click.OnItemLongClickListener;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/25 9:56
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：基础的RecycleView的adapter
 */
public abstract class BaseAdapter<T, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V>
        implements IHeaderAndFooter {
    protected int mLayoutId;
    protected Context mContext;
    protected List<T> mDatas;
    private int headerSize;
    private int footerSize;
    OnItemClickListener onItemClickListener;
    OnItemLongClickListener onItemLongClickListener;

    public BaseAdapter(Context context, int layoutId, List<T> datas) {
        mContext = context;
        mDatas = datas;
        mLayoutId = layoutId;
        setHasStableIds(true);
    }

    public int getLayoutId() {
        return mLayoutId;
    }

    @Override
    public long getItemId(int position) {
        if (position < mDatas.size()) {
            return mDatas.get(position).hashCode();
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    protected int getPosition(RecyclerView.ViewHolder viewHolder) {
        return viewHolder.getAdapterPosition();
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }

    public void setDatas(List<T> mDatas) {
        this.mDatas = mDatas;
    }


    public List<T> getDatas() {
        return mDatas;
    }

    public T getItemData(int position) {
        if (mDatas != null && mDatas.size() <= position) {
            return null;
        }
        return mDatas.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder, int viewType) {
        if (!isEnabled(viewType)) return;
        if (onItemLongClickListener != null || onItemClickListener != null) {
            viewHolder.setItemBackgound();
        }
        if (onItemClickListener != null) {
            viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getPosition(viewHolder);
                    onItemClickListener.onItemClick(parent, v, mDatas.get(position - getHeaderSize()), position - getHeaderSize());
                }
            });
        }

        if (onItemLongClickListener != null) {
            viewHolder.getConvertView().setOnLongClickListener(
                    new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            int position = getPosition(viewHolder);
                            return onItemLongClickListener.onItemLongClick(parent, v, mDatas.get(position - getHeaderSize()), position - getHeaderSize());
                        }
                    }
            );
        }
    }

    public int getFooterSize() {
        return footerSize;
    }

    public void setFooterSize(int footerSize) {
        this.footerSize = footerSize;
    }

    public int getHeaderSize() {
        return headerSize;
    }

    public void setHeaderSize(int headerSize) {
        this.headerSize = headerSize;
    }

}
