package com.ashlikun.adapter.abslistview;

import android.content.Context;
import android.databinding.ViewDataBinding;

import com.ashlikun.baseadapter.ISwipeAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/5/22.
 */
public abstract class SwipeAdapter<T, DB extends ViewDataBinding> extends CommonAdapter<T, DB>
implements ISwipeAdapter{
    public SwipeAdapter(Context context, int layoutId, List<T> datas) {
        super(context, layoutId, datas);
    }

    public boolean getSwipEnableByPosition(int position) {
        return true;
    }

}
