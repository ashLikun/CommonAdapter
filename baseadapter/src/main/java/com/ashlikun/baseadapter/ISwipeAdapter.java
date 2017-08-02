package com.ashlikun.baseadapter;

/**
 * 作者　　: 李坤
 * 创建时间:2017/4/19 0019　11:32
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：listView侧滑菜单  某个item是否有侧滑功能
 */

public interface ISwipeAdapter {
    public boolean getSwipEnableByPosition(int position);
}
