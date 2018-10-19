package com.ashlikun.adapter.databind;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashlikun.adapter.ViewHolder;
import com.ashlikun.adapter.recyclerview.vlayout.IStartPosition;


public class DataBindHolder<DB extends ViewDataBinding> extends ViewHolder {

    public DB dataBind;

    public <DB extends ViewDataBinding> DB getDataBind(Class<DB> dbClass) {
        return (DB) dataBind;
    }

    private DataBindHolder(Context context, DB dataBind, IStartPosition startPosition) {
        super(context, dataBind.getRoot(), startPosition);
        this.dataBind = dataBind;
        dataBind.getRoot().setTag(dataBind.getRoot().getId(), this);
    }

    public static DataBindHolder get(final Context context, View convertView,
                                     ViewGroup parent, int layoutId, IStartPosition startPosition) {
        if (convertView == null) {
            return new DataBindHolder(context,
                    DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, parent, false)
                    , startPosition);
        } else {
            DataBindHolder holder = (DataBindHolder) convertView.getTag(convertView.getId());
            return holder;
        }
    }



    public View getConvertView() {
        return dataBind.getRoot();
    }


    public DataBindHolder setText(TextView textView, Number text) {
        textView.setText(String.valueOf(text));
        return this;
    }

    public DataBindHolder setImageResource(ImageView view, int resId) {
        view.setImageResource(resId);
        return this;
    }


    public DataBindHolder setTextColorRes(TextView view, @ColorRes int textColorRes) {
        view.setTextColor(mContext.getResources().getColor(textColorRes));
        return this;
    }

    @SuppressLint("NewApi")
    public DataBindHolder setAlpha(View view, float value) {
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

    public DataBindHolder setVisible(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/4/17 0017 15:42
     * <p>
     * 方法功能：支持链接
     */
    public DataBindHolder linkify(TextView view) {
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    public DataBindHolder setTypeface(Typeface typeface, TextView... views) {
        for (TextView view : views) {
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }




}
