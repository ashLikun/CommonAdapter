package com.ashlikun.adapter.recyclerview.callback;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.ashlikun.adapter.R;

/**
 * @author　　: 李坤
 * 创建时间: 2018/10/19 9:16
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：ItemTouchHelper的回调监听
 */

public class ItemDragAndSwipeCallback extends ItemTouchHelper.SimpleCallback {
    private ItemTouchHelperAdapter mAdapter;

    public ItemDragAndSwipeCallback(ItemTouchHelperAdapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.END);
        mAdapter = adapter;
    }

    public ItemDragAndSwipeCallback(boolean isGrid, ItemTouchHelperAdapter adapter) {
        //如果是grid布局，那么就是上下左右都可以移动
        super(isGrid ? ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT : ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0);
        this.mAdapter = adapter;
    }

    /**
     * 开启长按拖拽功能，默认为true
     * 如果设置为false，手动开启，调用startDrag()
     *
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    /**
     * 开始滑动功能，默认为true
     * 如果设置为false，手动开启，调用startSwipe()
     *
     * @return
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return mAdapter.isItemSwipeEnable();
    }

    /**
     * 长按选中Item的时候开始调用
     *
     * @param viewHolder
     * @param actionState
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG
                && !mAdapter.isViewCreateByAdapter(viewHolder)) {
            mAdapter.onItemDragStart(viewHolder);
            viewHolder.itemView.setTag(R.id.adapter_dragging_support, true);
        } else if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE
                && !mAdapter.isViewCreateByAdapter(viewHolder)) {
            mAdapter.onItemSwipeStart(viewHolder);
            viewHolder.itemView.setTag(R.id.adapter_swiping_support, true);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    /**
     * 手指松开还原的时候
     *
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (mAdapter.isViewCreateByAdapter(viewHolder)) {
            return;
        }

        if (viewHolder.itemView.getTag(R.id.adapter_dragging_support) != null
                && (Boolean) viewHolder.itemView.getTag(R.id.adapter_dragging_support)) {
            mAdapter.onItemDragEnd(viewHolder);
            viewHolder.itemView.setTag(R.id.adapter_dragging_support, false);
        }
        if (viewHolder.itemView.getTag(R.id.adapter_swiping_support) != null
                && (Boolean) viewHolder.itemView.getTag(R.id.adapter_swiping_support)) {
            mAdapter.onItemSwipeClear(viewHolder);
            viewHolder.itemView.setTag(R.id.adapter_swiping_support, false);
        }
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (mAdapter.isViewCreateByAdapter(viewHolder)) {
            return makeMovementFlags(0, 0);
        }
        return super.getMovementFlags(recyclerView, viewHolder);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        return  mAdapter.onItemDragMoving(source, target);
    }

    /**
     * 当用户左右滑动Item达到删除条件时，会调用该方法，
     * 一般手指触摸滑动的距离达到RecyclerView宽度的一半时，再松开手指，此时该Item会继续向原先滑动方向滑过去并且调用onSwiped方法进行删除，否则会反向滑回原来的位置。在该方法内部我们可以这样写：
     *
     * @param viewHolder
     * @param direction
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (!mAdapter.isViewCreateByAdapter(viewHolder)) {
            mAdapter.onItemSwiped(viewHolder);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE
                && !mAdapter.isViewCreateByAdapter(viewHolder)) {
            View itemView = viewHolder.itemView;

            c.save();
            if (dX > 0) {
                c.clipRect(itemView.getLeft(), itemView.getTop(),
                        itemView.getLeft() + dX, itemView.getBottom());
                c.translate(itemView.getLeft(), itemView.getTop());
            } else {
                c.clipRect(itemView.getRight() + dX, itemView.getTop(),
                        itemView.getRight(), itemView.getBottom());
                c.translate(itemView.getRight() + dX, itemView.getTop());
            }

            mAdapter.onItemSwiping(c, viewHolder, dX, dY, isCurrentlyActive);
            c.restore();

        }
    }


}
