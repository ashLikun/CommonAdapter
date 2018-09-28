package com.ashlikun.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/4/20 0020 11:48a
 * <p>
 * 方法功能：
 */


public class ViewHolder extends RecyclerView.ViewHolder {
    protected SparseArray<View> mViews;
    /**
     * position 的偏移量，相对于整个列表的真实的位置(RecycleView)
     */
    protected int mPositionOffset = RecyclerView.NO_POSITION;
    protected int mLayoutPosition;
    protected Context mContext;
    protected int mLayoutId;
    /**
     * item点击颜色
     */
    private int itemClickColor = Color.GRAY;

    /**
     * 当前布局设置的点击颜色，标记是否设置过
     */
    private int isSetClickColor = -1;

    public ViewHolder(Context context, View itemView, int position) {
        super(itemView);
        mContext = context;
        setPosition(position);
        mViews = new SparseArray();
    }

    public void setPosition(int mPosition) {
        if (getLayoutPosition() == RecyclerView.NO_POSITION) {
            this.mPositionOffset = mPosition;
        } else {
            mLayoutPosition = getLayoutPosition();
            this.mPositionOffset = getLayoutPosition() - mPosition;
        }
    }

    /**
     * 是否设置过item点击效果
     *
     * @param color
     * @return
     */
    public boolean isSetEffects(int color) {
        return isSetClickColor == color;
    }

    public void setEffects(int color) {
        isSetClickColor = color;
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public ImageView getImageView(int viewId) {
        return getView(viewId);
    }


    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public ViewHolder setText(int viewId, Number text) {
        TextView tv = getView(viewId);
        tv.setText(String.valueOf(text));
        return this;
    }

    public ViewHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }


    public ViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public ViewHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public ViewHolder setBackgroundRes(int viewId, int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    public ViewHolder setBackgroundDrawable(int viewId, Drawable drawable) {
        View view = getView(viewId);
        ViewCompat.setBackground(view, drawable);
        return this;
    }

    public ViewHolder setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public ViewHolder setTextColorRes(int viewId, int textColorRes) {
        TextView view = getView(viewId);
        view.setTextColor(mContext.getResources().getColor(textColorRes));
        return this;
    }

    public ViewHolder setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    public ViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public ViewHolder linkify(int viewId) {
        TextView view = getView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    public ViewHolder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    public ViewHolder setProgress(int viewId, int progress) {
        ProgressBar view = getView(viewId);
        view.setProgress(progress);
        return this;
    }

    public ViewHolder setProgress(int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    public ViewHolder setMax(int viewId, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        return this;
    }

    public ViewHolder setRating(int viewId, float rating) {
        RatingBar view = getView(viewId);
        view.setRating(rating);
        return this;
    }

    public ViewHolder setRating(int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    public ViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public ViewHolder setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public ViewHolder setChecked(int viewId, boolean checked) {
        Checkable view = (Checkable) getView(viewId);
        view.setChecked(checked);
        return this;
    }

    public void setViewSize(View view, int size) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        params.height = size;
        params.width = size;
        view.setLayoutParams(params);
        view.setMinimumWidth(size);
        view.setMinimumHeight(size);
    }

    /**
     * 关于事件的
     */
    public ViewHolder setOnClickListener(int viewId,
                                         View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public ViewHolder setOnTouchListener(int viewId,
                                         View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    public ViewHolder setOnLongClickListener(int viewId,
                                             View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }


    public int getLayoutId() {
        return mLayoutId;
    }

    /**
     * 获取内部的位置
     * 下面这些是相对于整个列表的位置
     * 也可以使用这些：{@link #getLayoutPosition}
     * 也可以使用这些：{@link #getAdapterPosition()}
     *
     * @return
     */
    public int getPositionInside() {
        if (getLayoutPosition() != RecyclerView.NO_POSITION) {
            if (mLayoutPosition != RecyclerView.NO_POSITION && mLayoutPosition != getLayoutPosition()) {
                mPositionOffset -= mLayoutPosition - getLayoutPosition();
                mLayoutPosition = getLayoutPosition();
            }
            if (mPositionOffset < 0) {
                mPositionOffset = 0;
            }
            return getLayoutPosition() - mPositionOffset;
        } else {
            return mPositionOffset;
        }
    }


    /**
     * 设置item点击颜色,就当前Holder   item
     *
     * @param itemClickColor
     */
    public void setItemClickColor(int itemClickColor) {
        this.itemClickColor = itemClickColor;
    }

    public int getItemClickColor() {
        return itemClickColor;
    }
}
