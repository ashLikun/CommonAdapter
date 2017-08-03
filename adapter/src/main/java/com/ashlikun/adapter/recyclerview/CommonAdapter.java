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
 * Created by zhy on 16/4/9.
 */
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder>
        implements IHeaderAndFooter {
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    OnItemClickListener onItemClickListener;
    OnItemLongClickListener onItemLongClickListener;
    private int headerSize;
    private int footerSize;

    public CommonAdapter(Context context, int layoutId, List<T> datas) {
        mContext = context;
        mLayoutId = layoutId;
        mDatas = datas;
        setHasStableIds(true);
    }

    public void setmDatas(List<T> mDatas) {
        this.mDatas = mDatas;
    }

    public int getmLayoutId() {
        return mLayoutId;
    }

    @Override
    public long getItemId(int position) {
        return mDatas.get(position).hashCode();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        ViewHolder viewHolder = ViewHolder.get(mContext, null, parent, mLayoutId, -1);
        setListener(parent, viewHolder, viewType);
        return viewHolder;
    }

    protected int getPosition(RecyclerView.ViewHolder viewHolder) {
        return viewHolder.getAdapterPosition();
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.updatePosition(position);
        convert(holder, mDatas.get(position));
    }

    public abstract void convert(ViewHolder holder, T t);

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
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


    public T getItemData(int position) {
        if (mDatas != null && mDatas.size() <= position) {
            return null;
        }
        return mDatas.get(position);
    }

    public List<T> getDatas() {
        return mDatas;
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
