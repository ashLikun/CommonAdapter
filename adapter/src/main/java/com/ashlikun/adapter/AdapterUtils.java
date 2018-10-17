package com.ashlikun.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/6/21 0021　上午 11:16
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：适配器的一些工具
 */
public class AdapterUtils {
    /**
     * 查找屏幕中最后一个item位置
     *
     * @param layoutManager
     * @return
     */
    public static int findLastVisiblePosition(RecyclerView.LayoutManager layoutManager) {
        int lastVisibleItemPosition;
        if (layoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] into = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
            lastVisibleItemPosition = findMaxOrMin(into, true);
        } else {
            lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }
        return lastVisibleItemPosition;
    }

    /**
     * 查找屏幕中最后一个item位置
     *
     * @param layoutManager
     * @return
     */
    public static int findFirstVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        int FirstVisibleItemPosition;
        if (layoutManager instanceof GridLayoutManager) {
            FirstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] into = ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(null);
            FirstVisibleItemPosition = findMaxOrMin(into, false);
        } else {
            FirstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }
        return FirstVisibleItemPosition;
    }

    private static int findMaxOrMin(int[] lastPositions, boolean isMax) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (isMax) {
                if (value > max) {
                    max = value;
                }
            } else {
                if (value < max) {
                    max = value;
                }
            }
        }
        return max;
    }

    public static void setFullSpan(RecyclerView.ViewHolder holder) {
        if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder
                    .itemView.getLayoutParams();
            params.setFullSpan(true);
        }
    }

}
