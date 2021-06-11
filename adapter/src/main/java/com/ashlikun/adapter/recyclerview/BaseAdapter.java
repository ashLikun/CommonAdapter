package com.ashlikun.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.recyclerview.widget.RecyclerView;

import com.ashlikun.adapter.AdapterUtils;
import com.ashlikun.adapter.DataHandle;
import com.ashlikun.adapter.ForegroundEffects;
import com.ashlikun.adapter.ViewHolder;
import com.ashlikun.adapter.animation.AdapterAnimHelp;
import com.ashlikun.adapter.animation.BaseAnimation;
import com.ashlikun.adapter.recyclerview.click.OnItemClickListener;
import com.ashlikun.adapter.recyclerview.click.OnItemLongClickListener;
import com.ashlikun.adapter.recyclerview.click.SingleClickListener;
import com.ashlikun.adapter.recyclerview.vlayout.IStartPosition;
import com.ashlikun.adapter.recyclerview.vlayout.mode.AdapterBus;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/25 9:56
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：基础的RecycleView的adapter
 * <p>
 * 1:一行代码实现item{@link BaseAdapter#convert}
 * 2:添加生命周期
 * 先添加监听 addLifecycle(lifecycle)
 *
 * @OnLifecycleEvent(Lifecycle.Event.ON_RESUME) public void onResume() {
 * }
 * <p>
 * 3：动画{@link  BaseAdapter#getAdapterAnimHelp}{@link  BaseAdapter#setCustomAnimation}  -> {@link AdapterAnimHelp}
 * 4:{@link OnItemClickListener},{@link OnItemLongClickListener}
 * 5:前景点击效果水波纹
 * 6:灵活操作数据{@link DataHandle}
 * 7:布局可用XML，也可实现{@link BaseAdapter#createLayout}代码生成
 */
public abstract class BaseAdapter<T, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V>
        implements IHeaderAndFooter, LifecycleObserver, OnItemClickListener<T>, OnItemLongClickListener<T>, IStartPosition {
    public static int DEFAULT_LAYOUT_ID = -1;
    private int clickDelay = 500;
    protected int mLayoutId = DEFAULT_LAYOUT_ID;
    protected Context mContext;
    protected DataHandle<T> dataHandle;
    private int headerSize;
    private int footerSize;
    OnItemClickListener onItemClickListener;
    OnItemLongClickListener onItemLongClickListener;
    AdapterAnimHelp adapterAnimHelp;
    /**
     * item点击颜色
     */
    private int itemClickColor = 0;
    /**
     * 是否打开点击效果
     */
    private boolean isOpenClickEffects = false;
    protected RecyclerView recyclerView;

    /**
     * 1:创建Adapter回调的其他参数，一般用于改变UI
     * 2:事件的回调
     */
    public AdapterBus bus;

    public BaseAdapter(@NonNull Context context, int layoutId, List<T> datas) {
        dataHandle = new DataHandle<>(datas, this);
        mContext = context;
        mLayoutId = layoutId;
        setHasStableIds(true);
        adapterAnimHelp = new AdapterAnimHelp(this);
    }

    public BaseAdapter(@NonNull Context context, List<T> datas) {
        this(context, DEFAULT_LAYOUT_ID, datas);
    }

    public abstract void convert(@NonNull V holder, T t);

    /**
     * recycleView的局部刷新
     *
     * @param payloads 一定不为null
     * @return true：代表处理完毕，不走另一个刷新的方法，false表示没有处理，继续走另一个
     */
    public boolean convert(@NonNull V holder, T t, List<Object> payloads) {
        return false;
    }

    /**
     * 获取开始的position
     * 可能有头
     * 或者Vlayout内部是多个adapter
     *
     * @return
     */
    @Override
    public int getStartPosition() {
        return headerSize;
    }

    public int getLayoutId() {
        return mLayoutId;
    }

    public int getLayoutId(int viewType) {
        if (mLayoutId == DEFAULT_LAYOUT_ID) {
            return getLayoutId();
        }
        return mLayoutId;
    }

    /**
     * 可以重写这个方法，用java代码写布局,构造方法就不用传layoutID了
     */
    public View createLayout(ViewGroup parent, int layoutId, int viewType) {
        return LayoutInflater.from(mContext).inflate(layoutId, parent, false);
    }

    @Override
    public long getItemId(int position) {
        T d = getItemData(position);
        if (d != null) {
            return d.hashCode();
        }
        return getStartPosition() + position;
    }

    @Override
    public int getItemCount() {
        return dataHandle.getItemCount();
    }

    public boolean isEmpty() {
        return dataHandle.isEmpty();
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
        dataHandle.setDatas(datas);
    }

    /**
     * 设置新的数据源
     *
     * @param isNotify 是否通知适配器刷新
     */
    public void setDatas(List<T> datas, boolean isNotify) {
        dataHandle.setDatas(datas, isNotify);
    }

    /**
     * 添加数据
     */
    public void addDatas(List<T> datas) {
        addDatas(datas, false);
    }

    /**
     * 添加数据
     *
     * @param isNotify 是否通知适配器刷新
     */
    public void addDatas(List<T> datas, boolean isNotify) {
        dataHandle.addDatas(datas, isNotify);
    }

    /**
     * 清空全部数据
     */
    public void clearData() {
        dataHandle.clearData();
    }

    public @NonNull
    DataHandle<T> getDataHandle() {
        return dataHandle;
    }

    /**
     * 清空全部数据
     *
     * @param isNotify 是否通知适配器刷新
     */
    public void clearData(boolean isNotify) {
        dataHandle.clearData(isNotify);
    }

    public List<T> getDatas() {
        return dataHandle.getDatas();
    }

    /**
     * 获取position对应的数据
     */
    public T getItemData(int position) {
        return dataHandle.getItemData(position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    protected void setListener(@NonNull final ViewGroup parent, final ViewHolder viewHolder, final int viewType) {
        viewHolder.setStartPosition(this);
        if (!isEnabled(viewType)
                || !viewHolder.itemView.isEnabled()) {
            return;
        }
        if (isOpenClickEffects) {
            int color = itemClickColor == 0 ? viewHolder.getItemClickColor() : itemClickColor;
            if (!viewHolder.isSetEffects(color)) {
                ForegroundEffects.setForeground(color, getForegroundView(viewHolder, viewType));
                viewHolder.setEffects(color);
            }
        }
        viewHolder.itemView.setOnClickListener(new SingleClickListener(clickDelay) {
            @Override
            public void onSingleClick(View v) {
                int position = getPosition(viewHolder);
                T d = getItemData(position);
                if (d != null) {
                    onItemClick(viewType, parent, v, d, position);
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(viewType, parent, v, d, position);
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
                            } else if (onItemLongClickListener != null) {
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

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull V holder) {
        super.onViewAttachedToWindow(holder);
        onViewAttachedToWindowAnim(holder);
        adapterAnimHelp.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull V holder) {
        super.onViewDetachedFromWindow(holder);
        adapterAnimHelp.onViewDetachedFromWindow(holder);
    }

    /**
     * 附属于window时候动画
     *
     * @param holder
     */
    public void onViewAttachedToWindowAnim(@NonNull V holder) {

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


    public void setCustomAnimation(BaseAnimation mCustomAnimation) {
        adapterAnimHelp.setCustomAnimation(mCustomAnimation);
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
     * 设置生命周期监听
     */
    public void addLifecycle(@NonNull Lifecycle lifecycle) {
        lifecycle.addObserver(this);
    }

    /**
     * 获取动画帮助类
     *
     * @return
     */
    public AdapterAnimHelp getAdapterAnimHelp() {
        return adapterAnimHelp;
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
    public boolean onItemLongClick(int viewType, @NonNull ViewGroup parent, @NonNull View view, @NonNull T data, int position) {
        return false;
    }

    /**
     * 内部封装点击事件
     */
    @Override
    public void onItemClick(int viewType, @NonNull ViewGroup parent, @NonNull View view, @NonNull T data, int position) {

    }


    /**
     * 是否打开点击效果
     * 如果主动设置了点击事件，那么这个参数无效
     */
    public void setOffClickEffects() {
        isOpenClickEffects = false;
    }

    public void setOpenClickEffects() {
        isOpenClickEffects = true;
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
