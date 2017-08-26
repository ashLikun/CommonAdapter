package com.ashlikun.adapter.recyclerview;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.ashlikun.adapter.ViewHolder;
import com.ashlikun.adapter.animation.BaseAnimation;
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
    private boolean mOpenAnimationEnable = false;//动画是否开启
    private boolean mFirstOnlyEnable = true;//仅仅第一次底部出现才动画
    private int mLastPosition = -1;//最后一个显示的item
    private BaseAnimation mCustomAnimation;//动画
    private Interpolator mInterpolator = new LinearInterpolator();
    private int mDuration = 300;

    public BaseAdapter(Context context, int layoutId, List<T> datas) {
        mContext = context;
        mDatas = datas;
        mLayoutId = layoutId;
        setHasStableIds(true);
    }

    public abstract void convert(V holder, T t);

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
        if (!isEnabled(viewType) || !viewHolder.getConvertView().isClickable()
                || !viewHolder.getConvertView().isEnabled()) return;
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

    @Override
    public void onViewAttachedToWindow(V holder) {
        super.onViewAttachedToWindow(holder);
        int type = holder.getItemViewType();
        addAnimation(holder);
    }

    /**
     * add animation when you want to show time
     *
     * @param holder
     */
    private void addAnimation(RecyclerView.ViewHolder holder) {
        if (mOpenAnimationEnable) {
            if (!mFirstOnlyEnable || holder.getLayoutPosition() > mLastPosition) {
                if (mCustomAnimation != null) {
                    for (Animator anim : mCustomAnimation.getAnimators(holder.itemView)) {
                        startAnim(anim, holder.getLayoutPosition());
                    }
                }
                mLastPosition = holder.getLayoutPosition();
            }
        }
    }

    /**
     * set anim to start when loading
     *
     * @param anim
     * @param index
     */
    protected void startAnim(Animator anim, int index) {
        anim.setDuration(mDuration).start();
        anim.setInterpolator(mInterpolator);
    }
}
