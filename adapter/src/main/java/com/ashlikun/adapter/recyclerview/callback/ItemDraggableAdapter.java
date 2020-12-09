package com.ashlikun.adapter.recyclerview.callback;

import android.content.Context;
import android.graphics.Canvas;

import androidx.recyclerview.widget.RecyclerView;

import com.ashlikun.adapter.ViewHolder;
import com.ashlikun.adapter.recyclerview.CommonAdapter;

import java.util.Collections;
import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/10/19　9:38
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：实现拖拽功能的adapter
 */
public abstract class ItemDraggableAdapter<T> extends CommonAdapter<T> implements ItemTouchHelperAdapter {
    protected boolean itemDragEnabled = false;
    protected boolean itemSwipeEnabled = false;
    protected OnItemDragListener mOnItemDragListener;
    protected OnItemSwipeListener mOnItemSwipeListener;

    public ItemDraggableAdapter(Context context, List<T> data) {
        super(context, data);
    }

    public ItemDraggableAdapter(Context context, int layoutResId, List<T> data) {
        super(context, layoutResId, data);
    }

    public void disableDragItem() {
        itemDragEnabled = false;
    }

    public boolean isItemDraggable() {
        return itemDragEnabled;
    }


    public void enableSwipeItem() {
        itemSwipeEnabled = true;
    }

    public void disableSwipeItem() {
        itemSwipeEnabled = false;
    }

    @Override
    public boolean isItemSwipeEnable() {
        return itemSwipeEnabled;
    }

    public void setOnItemDragListener(OnItemDragListener onItemDragListener) {
        mOnItemDragListener = onItemDragListener;
    }

    public int getViewHolderPosition(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof ViewHolder) {
            return ((ViewHolder) viewHolder).getPositionInside();
        }
        return viewHolder.getLayoutPosition();
    }

    @Override
    public void onItemDragStart(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemDragListener != null && itemDragEnabled) {
            mOnItemDragListener.onItemDragStart(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    //必须这样，不然会抖动
    @Override
    public long getItemId(int position) {
        T d = getItemData(position);
        if (d != null) {
            return d.hashCode();
        }
        return 0;
    }

    @Override
    public boolean onItemDragMoving(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        int from = getViewHolderPosition(source);
        int to = getViewHolderPosition(target);

        if (inRange(from) && inRange(to)) {
            if (from < to) {
                for (int i = from; i < to; i++) {
                    Collections.swap(getDatas(), i, i + 1);
                }
            } else {
                for (int i = from; i > to; i--) {
                    Collections.swap(getDatas(), i, i - 1);
                }
            }
            notifyItemMoved(source.getAdapterPosition(), target.getAdapterPosition());
        }

        if (mOnItemDragListener != null && itemDragEnabled) {
            mOnItemDragListener.onItemDragMoving(source, from, target, to);
        }
        return true;
    }

    @Override
    public void onItemDragEnd(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemDragListener != null && itemDragEnabled) {
            mOnItemDragListener.onItemDragEnd(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    public void setOnItemSwipeListener(OnItemSwipeListener listener) {
        mOnItemSwipeListener = listener;
    }

    @Override
    public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemSwipeListener != null && itemSwipeEnabled) {
            mOnItemSwipeListener.onItemSwipeStart(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    @Override
    public void onItemSwipeClear(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemSwipeListener != null && itemSwipeEnabled) {
            mOnItemSwipeListener.clearView(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    @Override
    public void onItemSwiped(RecyclerView.ViewHolder viewHolder) {
        int pos = getViewHolderPosition(viewHolder);
        if (inRange(pos)) {
            getDataHandle().removeData(getItemData(pos));
            notifyItemRemoved(viewHolder.getAdapterPosition());
        }
        if (mOnItemSwipeListener != null && itemSwipeEnabled) {
            mOnItemSwipeListener.onItemSwiped(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    @Override
    public void onItemSwiping(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
        if (mOnItemSwipeListener != null && itemSwipeEnabled) {
            mOnItemSwipeListener.onItemSwipeMoving(canvas, viewHolder, dX, dY, isCurrentlyActive);
        }
    }

    private boolean inRange(int position) {
        return position >= 0 && position < getDataHandle().getItemCount();
    }
}
