package com.ashlikun.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;


public class ViewHolder<DB extends ViewDataBinding> extends RecyclerView.ViewHolder {

    private int mPosition;
    private Context mContext;
    public DB dataBind;

    public <DB extends ViewDataBinding> DB getDataBind(Class<DB> dbClass) {
        return (DB) dataBind;
    }

    private ViewHolder(Context context, DB dataBind, int position) {
        super(dataBind.getRoot());
        mContext = context;
        mPosition = position;
        this.dataBind = dataBind;
        dataBind.getRoot().setTag(dataBind.getRoot().getId(), this);
    }

    public static ViewHolder get(final Context context, View convertView,
                                 ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new ViewHolder(context,
                    DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, parent, false)
                    , position);
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag(convertView.getId());
            holder.mPosition = position;
            return holder;
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2016/12/14 9:06
     * <p>
     * 方法功能：设置view的背景点击效果
     */

    public void setItemBackgound() {
        int pressed = Color.GRAY;
        Drawable content = itemView.getBackground();
        if (content == null) {
            content = new ColorDrawable(Color.TRANSPARENT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ColorStateList colorList = new ColorStateList(new int[][]{{}}, new int[]{pressed});
            RippleDrawable ripple = new RippleDrawable(colorList, content.getAlpha() == 0 ? null : content, content.getAlpha() == 0 ? new ColorDrawable(Color.WHITE) : null);
            setBackgroundCompat(itemView, ripple);
        } else {
            StateListDrawable bg = new StateListDrawable();
            // View.PRESSED_ENABLED_STATE_SET
            bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, new ColorDrawable(pressed));
            // View.EMPTY_STATE_SET
            bg.addState(new int[]{}, content);
        }
    }

    public View getConvertView() {
        return dataBind.getRoot();
    }


    public ViewHolder setText(TextView textView, Number text) {
        textView.setText(String.valueOf(text));
        return this;
    }

    public ViewHolder setImageResource(ImageView view, int resId) {
        view.setImageResource(resId);
        return this;
    }


    public ViewHolder setTextColorRes(TextView view, @ColorRes int textColorRes) {
        view.setTextColor(mContext.getResources().getColor(textColorRes));
        return this;
    }

    @SuppressLint("NewApi")
    public ViewHolder setAlpha(View view, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            view.setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            view.startAnimation(alpha);
        }
        return this;
    }

    public ViewHolder setVisible(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/4/17 0017 15:42
     * <p>
     * 方法功能：支持链接
     */
    public ViewHolder linkify(TextView view) {
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    public ViewHolder setTypeface(Typeface typeface, TextView... views) {
        for (TextView view : views) {
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }


    public void updatePosition(int position) {
        mPosition = position;
    }

    public int getmPosition() {
        return mPosition;
    }


    private void setBackgroundCompat(View view, Drawable drawable) {
        int pL = view.getPaddingLeft();
        int pT = view.getPaddingTop();
        int pR = view.getPaddingRight();
        int pB = view.getPaddingBottom();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
        view.setPadding(pL, pT, pR, pB);
    }


}
