package com.ashlikun.adapter.recyclerview;

import android.animation.Animator;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.ashlikun.adapter.AdapterUtils;
import com.ashlikun.adapter.ForegroundEffects;
import com.ashlikun.adapter.ViewHolder;
import com.ashlikun.adapter.animation.BaseAnimation;
import com.ashlikun.adapter.recyclerview.click.OnItemClickListener;
import com.ashlikun.adapter.recyclerview.click.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/25 9:56
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：基础的RecycleView的adapter
 * <p>
 * 添加生命周期
 *
 * @OnLifecycleEvent(Lifecycle.Event.ON_RESUME) public void onResume() {
 * }
 */
public abstract class BaseAdapter<T, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V>
        implements IHeaderAndFooter, LifecycleObserver, OnItemClickListener<T>, OnItemLongClickListener<T> {
    private int clickDelay = 200;
    protected int mLayoutId;
    protected Context mContext;
    protected List<T> mDatas;
    private int headerSize;
    private int footerSize;
    OnItemClickListener onItemClickListener;
    OnItemLongClickListener onItemLongClickListener;
    private boolean mOpenAnimationEnable = false;//动画是否开启
    private boolean mFirstOnlyEnable = true;//仅仅第一次底部出现才动画
    private int lastPosition = -1;//最后一个显示的item(只要显示过的就不会再显示),LayouPosition
    private boolean lastPositionEnable = false;//内部计算最后一个position使能
    private BaseAnimation mCustomAnimation;//动画
    private Interpolator mInterpolator = new LinearInterpolator();
    private int mDuration = 300;
    private long lastClickTime = 0;
    //item点击颜色
    private int itemClickColor = 0;
    /**
     * 是否打开点击效果
     */
    private boolean isOpenClickEffects = true;
    protected RecyclerView recyclerView;

    public BaseAdapter(Context context, int layoutId, List<T> datas) {
        mContext = context;
        mDatas = datas;
        mLayoutId = layoutId;
        setHasStableIds(true);
    }

    public BaseAdapter(Context context, List<T> datas) {
        mContext = context;
        mDatas = datas;
        setHasStableIds(true);
    }

    public abstract void convert(V holder, T t);

    /**
     * recycleView的局部刷新
     *
     * @param payloads 一定不为null
     * @return true：代表处理完毕，不走另一个刷新的方法，false表示没有处理，继续走另一个
     */
    public boolean convert(V holder, T t, List<Object> payloads) {
        return false;
    }

    public int getLayoutId() {
        return mLayoutId;
    }

    /**
     * 可以重写这个方法，用java代码写布局,构造方法就不用传layoutID了
     */
    public View getItemLayout(ViewGroup parent, int layoutId) {
        return LayoutInflater.from(mContext).inflate(layoutId, parent, false);
    }

    @Override
    public long getItemId(int position) {
        T d = getItemData(position);
        if (d != null) {
            return d.hashCode();
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    protected int getPosition(ViewHolder viewHolder) {
        return viewHolder.getPositionInside();
    }

    /**
     * 是否可以点击
     */
    protected boolean isEnabled(int viewType) {
        return true;
    }

    /**
     * 设置新的数据源
     */
    public void setDatas(List<T> datas) {
        this.mDatas = datas;
    }

    /**
     * 添加数据
     */
    public void addDatas(List<T> datas) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.addAll(datas);
    }

    /**
     * 清空全部数据
     */
    public void clearData() {
        if (mDatas != null) {
            mDatas.clear();
        }
    }

    public List<T> getDatas() {
        return mDatas;
    }

    /**
     * 获取position对应的数据
     */
    public T getItemData(int position) {
        if (mDatas == null || mDatas.size() <= position) {
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

    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder, final int viewType) {
        if (!isEnabled(viewType)
                || !viewHolder.itemView.isEnabled()) {
            return;
        }
        if (isOpenClickEffects || onItemLongClickListener != null || onItemClickListener != null) {
            int color = itemClickColor == 0 ? viewHolder.getItemClickColor() : itemClickColor;
            ForegroundEffects.setForeground(color, getForegroundView(viewHolder, viewType));
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //暴力点击
                if (System.currentTimeMillis() - lastClickTime > clickDelay) {
                    lastClickTime = System.currentTimeMillis();
                    int position = getPosition(viewHolder);
                    T d = getItemData(position);
                    if (d != null) {
                        onItemClick(viewType, parent, v, d, position);
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(viewType, parent, v, d, position);
                        }
                    }
                }
            }
        });

        viewHolder.itemView.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int position = getPosition(viewHolder);
                        T d = getItemData(position);
                        if (d != null) {
                            if (onItemLongClick(viewType, parent, v, d, position)) {
                                return true;
                            } else if (onItemClickListener != null) {
                                return onItemLongClickListener.onItemLongClick(viewType, parent, v, d, position);
                            }
                        }
                        return false;
                    }
                }
        );
    }

    /**
     * 获取要设置前景色效果的view，23,FrameLayout
     *
     * @param viewHolder
     * @param viewType   因为这个方法是在设置事件里面调用的，此时ViewHolder还没有viewType
     * @return
     */
    protected View getForegroundView(ViewHolder viewHolder, int viewType) {
        return viewHolder.itemView;
    }

    @Override
    public int getFooterSize() {
        return footerSize;
    }

    @Override
    public void setFooterSize(int footerSize) {
        this.footerSize = footerSize;
    }

    @Override
    public int getHeaderSize() {
        return headerSize;
    }

    @Override
    public void setHeaderSize(int headerSize) {
        this.headerSize = headerSize;
    }

    @Override
    public void onViewAttachedToWindow(V holder) {
        super.onViewAttachedToWindow(holder);
        onViewAttachedToWindowAnim(holder);
        if (lastPositionEnable || mOpenAnimationEnable) {
            if (holder.getLayoutPosition() > lastPosition) {
                lastPosition = holder.getLayoutPosition();
            }
        }
    }

    @Override
    public void onViewDetachedFromWindow(V holder) {
        super.onViewDetachedFromWindow(holder);
        if (lastPositionEnable) {
            if (holder.getLayoutPosition() >= lastPosition - 2) {
                lastPosition = getLastPosition();
            }
        }
    }

    /**
     * 附属于window时候动画
     *
     * @param holder
     */
    public void onViewAttachedToWindowAnim(V holder) {
        addAnimation(holder);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }

    /**
     * 开始item动画一个动画
     *
     * @param holder
     */
    private void addAnimation(RecyclerView.ViewHolder holder) {
        if (mOpenAnimationEnable) {
            if (mFirstOnlyEnable && holder.getLayoutPosition() > lastPosition) {
                dispathAnim(holder);
            } else if (!mFirstOnlyEnable) {
                dispathAnim(holder);
            }
        }
    }

    public void setCustomAnimation(BaseAnimation mCustomAnimation) {
        this.mCustomAnimation = mCustomAnimation;
        mOpenAnimationEnable = true;
    }

    private void dispathAnim(RecyclerView.ViewHolder holder) {
        if (mCustomAnimation != null) {
            Animator[] animators = mCustomAnimation.getAnimators(holder.itemView);
            for (Animator anim : animators) {
                startAnim(anim, holder.getLayoutPosition());
            }
        }
    }

    /**
     * 开始动画
     */
    protected void startAnim(Animator anim, int index) {
        anim.setDuration(mDuration).start();
        anim.setInterpolator(mInterpolator);
    }

    /**
     * 内部计算的最后一个显示的item(滑动后的底部),LayouPosition
     * 用于动画
     */
    public int getOnlyLastPosition() {
        return lastPosition;
    }

    /**
     * 主动设置最后一个显示的position
     *
     * @param lastPosition
     */
    public void setOnlyLastPosition(int lastPosition) {
        if (lastPositionEnable) {
            this.lastPosition = lastPosition;
        }
    }

    /**
     * 屏幕中的最后一个Item位置
     *
     * @return
     */
    public int getLastPosition() {
        if (recyclerView == null || recyclerView.getLayoutManager() == null) {
            return RecyclerView.NO_POSITION;
        }
        return AdapterUtils.findLastVisiblePosition(recyclerView.getLayoutManager());
    }

    /**
     * 屏幕中的第一个Item位置
     *
     * @return
     */
    public int getFirstPosition() {
        if (recyclerView == null || recyclerView.getLayoutManager() == null) {
            return RecyclerView.NO_POSITION;
        }
        return AdapterUtils.findFirstVisibleItemPosition(recyclerView.getLayoutManager());
    }

    /**
     * 仅仅第一次底部出现才动画
     *
     * @param mFirstOnlyEnable
     */
    public void setFirstOnlyEnable(boolean mFirstOnlyEnable) {
        this.mFirstOnlyEnable = mFirstOnlyEnable;
    }

    /**
     * 是否内部计算lastPosition
     *
     * @param
     */
    public void setLastPositionOn() {
        this.lastPositionEnable = true;
    }

    /**
     * 设置生命周期监听
     */
    public void addLifecycle(Lifecycle lifecycle) {
        lifecycle.addObserver(this);
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * 暴力点击的延时
     */
    public void setClickDelay(int clickDelay) {
        this.clickDelay = clickDelay;
    }

    /**
     * 内部封装点击事件
     */
    @Override
    public boolean onItemLongClick(int viewType, ViewGroup parent, View view, T data, int position) {
        return false;
    }

    /**
     * 内部封装点击事件
     */
    @Override
    public void onItemClick(int viewType, ViewGroup parent, View view, T data, int position) {

    }

    /**
     * 是否打开点击效果
     * 如果主动设置了点击事件，那么这个参数无效
     */
    public void setOffClickEffects() {
        isOpenClickEffects = false;
    }

    /**
     * 设置item点击颜色,全局的item
     *
     * @param itemClickColor
     */
    public void setItemClickColor(int itemClickColor) {
        this.itemClickColor = itemClickColor;
    }
}
