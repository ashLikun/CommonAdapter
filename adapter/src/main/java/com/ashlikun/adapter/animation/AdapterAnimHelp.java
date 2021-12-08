package com.ashlikun.adapter.animation;

import android.animation.Animator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import androidx.recyclerview.widget.RecyclerView;

import com.ashlikun.adapter.recyclerview.BaseAdapter;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/6/21 0021　下午 5:08
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：adapter动画逻辑
 */
public class AdapterAnimHelp {
    private int mDuration = 300;
    BaseAdapter adapter;
    private boolean mOpenAnimationEnable = false;//动画是否开启
    private boolean mFirstOnlyEnable = true;//仅仅第一次底部出现才动画
    private int lastPosition = RecyclerView.NO_POSITION;//最后一个显示的item(只要显示过的就不会再显示),LayouPosition
    private boolean lastPositionEnable = false;//内部计算最后一个position使能
    private BaseAnimation mCustomAnimation;//动画
    private Interpolator mInterpolator = new LinearInterpolator();

    public AdapterAnimHelp(BaseAdapter adapter) {
        this.adapter = adapter;
    }

    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        if (lastPositionEnable) {
            if (holder.getLayoutPosition() >= lastPosition - 2) {
                lastPosition = adapter.getLastPosition();
                if (lastPosition == 0) {
                    lastPosition = RecyclerView.NO_POSITION;
                }
            }
        }
    }

    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        addAnimation(holder);
        if (lastPositionEnable || mOpenAnimationEnable) {
            if (holder.getLayoutPosition() > lastPosition) {
                lastPosition = holder.getLayoutPosition();
                if (lastPosition == 0) {
                    lastPosition = RecyclerView.NO_POSITION;
                }
            }
        }
    }

    /**
     * 开始item动画一个动画
     *
     * @param holder
     */
    private void addAnimation(RecyclerView.ViewHolder holder) {
        if (mOpenAnimationEnable) {
            if (mFirstOnlyEnable && holder.getLayoutPosition() > lastPosition) {
                dispathAnim(holder);
            } else if (!mFirstOnlyEnable) {
                dispathAnim(holder);
            }
        }
    }

    private void dispathAnim(RecyclerView.ViewHolder holder) {
        if (mCustomAnimation != null) {
            Animator[] animators = mCustomAnimation.getAnimators(holder.itemView);
            for (Animator anim : animators) {
                startAnim(anim, holder.getLayoutPosition());
            }
        }
    }

    /**
     * 开始动画
     */
    protected void startAnim(Animator anim, int index) {
        anim.setDuration(mDuration).start();
        anim.setInterpolator(mInterpolator);
    }

    public void setCustomAnimation(BaseAnimation mCustomAnimation) {
        this.mCustomAnimation = mCustomAnimation;
        mOpenAnimationEnable = true;
    }

    /**
     * 内部计算的最后一个显示的item(滑动后的底部),LayouPosition
     * 用于动画
     */
    public int getOnlyLastPosition() {
        return lastPosition;
    }

    /**
     * 主动设置最后一个显示的position
     *
     * @param lastPosition
     */
    public void setOnlyLastPosition(int lastPosition) {
        if (lastPositionEnable) {
            this.lastPosition = lastPosition;
        }
    }

    /**
     * 仅仅第一次底部出现才动画
     *
     * @param mFirstOnlyEnable
     */
    public void setFirstOnlyEnable(boolean mFirstOnlyEnable) {
        this.mFirstOnlyEnable = mFirstOnlyEnable;
    }

    /**
     * 是否内部计算lastPosition
     *
     * @param
     */
    public void setLastPositionOn() {
        this.lastPositionEnable = true;
    }

    public void setDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    public void setInterpolator(Interpolator mInterpolator) {
        this.mInterpolator = mInterpolator;
    }
}
