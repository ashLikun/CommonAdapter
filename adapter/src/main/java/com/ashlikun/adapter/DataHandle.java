package com.ashlikun.adapter;

import com.ashlikun.adapter.recyclerview.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/6/22 0022　上午 11:01
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：adapter数据处理
 */
public class DataHandle<T> {
    protected List<T> mDatas;
    BaseAdapter adapter;


    public DataHandle(List<T> mDatas, BaseAdapter adapter) {
        this.mDatas = mDatas;
        this.adapter = adapter;
    }

    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
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

    /**
     * 设置新的数据源
     */
    public void setDatas(List<T> datas) {
        setDatas(datas, false);
    }

    /**
     * 设置新的数据源
     *
     * @param isNotify 是否通知适配器刷新
     */
    public void setDatas(List<T> datas, boolean isNotify) {
        this.mDatas = datas;
        if (isNotify) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 第一个前面添加数据
     */
    public void addFirstDatas(List<T> datas) {
        addDatas(datas, true, false);
    }

    /**
     * 第一个前面添加数据
     *
     * @param isNotify 是否通知适配器刷新
     */
    public void addFirstDatas(List<T> datas, boolean isNotify) {
        addDatas(datas, true, isNotify);
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
        addDatas(datas, true, isNotify);
    }

    /**
     * 添加数据
     *
     * @param isNotify 是否通知适配器刷新
     * @param isEnd    是否从末尾插入
     */
    public void addDatas(List<T> datas, boolean isEnd, boolean isNotify) {
        if (datas == null || datas.isEmpty()) {
            return;
        }
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        int size = getItemCount();
        if (isEnd) {
            mDatas.addAll(datas);
        } else {
            mDatas.addAll(0, datas);
        }
        if (isNotify) {
            if (size > 0) {
                if (isEnd) {
                    adapter.notifyItemRangeInserted(size, datas.size());
                } else {
                    adapter.notifyItemRangeInserted(0, datas.size());
                }
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 移除数据
     */
    public void removeData(T deleteData) {
        removeData(deleteData, false);
    }

    /**
     * 移除数据
     *
     * @param isNotify 是否通知适配器刷新
     */
    public void removeData(T deleteData, boolean isNotify) {
        if (deleteData == null) {
            return;
        }
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        int size = mDatas.indexOf(deleteData);
        if (mDatas.remove(deleteData)) {
            if (isNotify) {
                adapter.notifyItemRemoved(size);
            }
        }

    }

    /**
     * 清空全部数据
     */
    public void clearData() {
        clearData(false);
    }

    /**
     * 清空全部数据
     *
     * @param isNotify 是否通知适配器刷新
     */
    public void clearData(boolean isNotify) {
        if (mDatas != null) {
            int size = getItemCount();
            mDatas.clear();
            if (isNotify && size > 0) {
                adapter.notifyItemRangeRemoved(0, size);
            }
        }
    }
}
