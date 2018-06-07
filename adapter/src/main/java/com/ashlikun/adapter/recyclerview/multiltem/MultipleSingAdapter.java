package com.ashlikun.adapter.recyclerview.multiltem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.LayoutHelper;
import com.ashlikun.adapter.ViewHolder;
import com.ashlikun.adapter.recyclerview.MultiItemTypeSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/4/10 0010　20:53
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：多表单的内部adapter,可以有多个不同的type
 *
 * @OnLifecycleEvent(Lifecycle.Event.ON_RESUME) public void onResume() {
 * }
 */
public abstract class MultipleSingAdapter<T> extends SingAdapter<T, ViewHolder> implements MultiItemTypeSupport<T> {
    private LayoutHelper layoutHelper;
    private List<Pair<Integer, Integer>> positionIndex = new ArrayList<>();

    public MultipleSingAdapter(Context context, LayoutHelper layoutHelper, List<T> datas) {
        super(context, -1, datas);
        this.layoutHelper = layoutHelper;
        setPositionIndex(positionIndex);
        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                setPositionIndex(positionIndex);
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                setPositionIndex(positionIndex);
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                setPositionIndex(positionIndex);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                setPositionIndex(positionIndex);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                setPositionIndex(positionIndex);
            }
        });
    }

    public MultipleSingAdapter(Context context, LayoutHelper layoutHelper) {
        this(context, layoutHelper, null);
    }

    public MultipleSingAdapter(Context context, List<T> datas) {
        this(context, null, datas);
    }

    public MultipleSingAdapter(Context context) {
        this(context, null, null);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        if (layoutHelper == null) {
            throw new RuntimeException(getClass().getSimpleName() + " layoutHelper is null");
        }
        return layoutHelper;
    }


    @Override
    public ViewHolder createHolder(final ViewGroup parent, int viewType) {
        int layoutId = getLayoutId(viewType);
        ViewHolder holder = new ViewHolder(mContext, getItemLayout(parent, layoutId), -1);
        return holder;
    }

    public LayoutHelper getLayoutHelper() {
        return layoutHelper;
    }

    public void setLayoutHelper(LayoutHelper layoutHelper) {
        this.layoutHelper = layoutHelper;
    }

    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, getItemData(position));
    }

    @Override
    public int getItemCount() {
        int size = super.getItemCount();
        if (size <= 0 && layoutHelper != null) {
            size = layoutHelper.getItemCount();
        }
        return size;
    }

    /**
     * 折半查找
     *
     * @return Pair 第一个参数 这条数据的外围Position
     * pair 第二个参数，这条item在什么范围(开始---结束position)
     */
    private Pair<Integer, Pair<Integer, Integer>> halfFind(int position) {
        int low = 0, height = positionIndex.size() - 1, mid;
        Pair<Integer, Integer> pair;
        while (low <= height) {
            //中间
            mid = (low + height) / 2;
            pair = positionIndex.get(mid);
            if (pair == null) {
                continue;
            }
            //在前面
            if (pair.first > position) {
                height = mid - 1;
            }
            //在后面
            else if (pair.second < position) {
                low = mid + 1;
            }
            //就是这个数据
            else if (pair.first <= position && pair.second >= position) {
                return new Pair<>(mid, pair);
            } else {
                break;
            }
        }
        return null;
    }

    public List<Pair<Integer, Integer>> getPositionIndex() {
        return positionIndex;
    }

    public void setPositionIndex(List<Pair<Integer, Integer>> positionIndex) {
        positionIndex.clear();
    }
}
