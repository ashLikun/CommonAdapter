package com.ashlikun.adapter;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.lang.reflect.Field;

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
     * 查找屏幕中第一个item位置
     *
     * @param layoutManager
     * @return
     */
    public static int findFirstVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        int firstVisibleItem = -1;
        if (layoutManager != null) {
            if (layoutManager instanceof GridLayoutManager) {
                firstVisibleItem = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
            } else if (layoutManager instanceof LinearLayoutManager) {
                firstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] firstPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(firstPositions);
                firstVisibleItem = findMaxOrMin(firstPositions, false);
            }
        }
        return firstVisibleItem;
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

    public static int viewTypeToVLayout(Object viewType) {
        return Math.abs(viewType.hashCode());
    }

    /**
     * 反射字段
     *
     * @param object    要反射的对象
     * @param fieldName 要反射的字段名称
     */
    public static Field setField(Object object, String fieldName, Object value) {
        if (object == null || fieldName == null || fieldName.isEmpty()) {
            return null;
        }
        try {
            Field field = getAllDeclaredField(object.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                field.set(object, value);
                return field;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定的字段
     */
    public static Field getAllDeclaredField(Class<?> claxx, String fieldName) {
        if (fieldName == null || fieldName.isEmpty()) {
            return null;
        }

        while (claxx != null && claxx != Object.class) {
            try {
                Field f = claxx.getDeclaredField(fieldName);
                if (f != null) {
                    return f;
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            claxx = claxx.getSuperclass();
        }
        return null;
    }
}
