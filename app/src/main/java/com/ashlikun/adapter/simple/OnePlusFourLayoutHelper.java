/*
 * MIT License
 *
 * Copyright (c) 2016 Alibaba Group
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ashlikun.adapter.simple;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.LayoutManagerHelper;
import com.alibaba.android.vlayout.OrientationHelperEx;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.VirtualLayoutManager.LayoutStateWrapper;
import com.alibaba.android.vlayout.layout.AbstractFullFillLayoutHelper;
import com.alibaba.android.vlayout.layout.LayoutChunkResult;

import java.util.Arrays;

import static com.alibaba.android.vlayout.VirtualLayoutManager.VERTICAL;

/**
 * @author　　: 李坤
 * 创建时间: 2018/7/5 10:18
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 * 1 + 4
 * -------------------------
 * |               |       |
 * |     1         |   2   |
 * |               |       |
 * -------------------------
 * |       |       |       |
 * |   3   |   4   |   5   |
 * |       |       |       |
 * -------------------------
 */

public class OnePlusFourLayoutHelper extends AbstractFullFillLayoutHelper {

    private static final String TAG = "OnePlusNLayoutHelper";

    private Rect mAreaRect = new Rect();

    private View[] mChildrenViews;

    private float[] mColWeights = new float[0];

    private float mRowWeight = Float.NaN;

    public OnePlusFourLayoutHelper() {
        setItemCount(0);
    }

    public OnePlusFourLayoutHelper(int itemCount) {
        this(itemCount, 0, 0, 0, 0);
    }

    public OnePlusFourLayoutHelper(int itemCount, int leftMargin, int topMargin, int rightMargin,
                                   int bottomMargin) {
        setItemCount(itemCount);
    }

    /**
     * 当item范围改变的时候
     */
    @Override
    public void onRangeChange(int start, int end) {
        if (end - start != 4) {
            throw new IllegalArgumentException(
                    "这个helper的item个数一定要==5" + "  现在是 = " + (end - start + 1));
        }
    }

    public void setColWeights(float[] weights) {
        if (weights != null) {
            this.mColWeights = Arrays.copyOf(weights, weights.length);
        } else {
            this.mColWeights = new float[0];
        }
    }

    public void setRowWeight(float weight) {
        this.mRowWeight = weight;
    }

    @Override
    public void layoutViews(RecyclerView.Recycler recycler, RecyclerView.State state,
                            LayoutStateWrapper layoutState, LayoutChunkResult result, LayoutManagerHelper helper) {
        // reach the end of this layout
        final int originCurPos = layoutState.getCurrentPosition();
        if (isOutOfRange(originCurPos)) {
            return;
        }

        if (mChildrenViews == null || mChildrenViews.length != getItemCount()) {
            mChildrenViews = new View[getItemCount()];
        }

        int count = getAllChildren(mChildrenViews, recycler, layoutState, result, helper);

        if (count != getItemCount()) {
            Log.w(TAG, "The real number of children is not match with range of LayoutHelper");
        }

        final boolean layoutInVertical = helper.getOrientation() == VERTICAL;

        final int parentWidth = helper.getContentWidth();
        final int parentHeight = helper.getContentHeight();
        final int parentHPadding = helper.getPaddingLeft() + helper.getPaddingRight()
                + getHorizontalMargin() + getHorizontalPadding();
        final int parentVPadding = helper.getPaddingTop() + helper.getPaddingBottom()
                + getVerticalMargin() + getVerticalPadding();

        int mainConsumed = 0;

        if (count == 5) {
            mainConsumed = handFive(layoutState, result, helper, layoutInVertical, parentWidth, parentHeight,
                    parentHPadding, parentVPadding);
        }
        result.mConsumed = mainConsumed;

        Arrays.fill(mChildrenViews, null);
    }


    private float getViewMainWeight(ViewGroup.MarginLayoutParams params, int index) {
        if (mColWeights.length > index) {
            return mColWeights[index];
        }

        return Float.NaN;
    }

    @Override
    public int computeAlignOffset(int offset, boolean isLayoutEnd, boolean useAnchor,
                                  LayoutManagerHelper helper) {
        if (helper.getOrientation() == VERTICAL) {
            if (isLayoutEnd) {
                return mMarginBottom + mPaddingBottom;
            } else {
                return -mMarginTop - mPaddingTop;
            }
        } else {
            if (isLayoutEnd) {
                return mMarginRight + mPaddingRight;
            } else {
                return -mMarginLeft - mPaddingLeft;
            }
        }
    }

    /**
     * 构建1拖4
     *
     * @return
     */
    private int handFive(LayoutStateWrapper layoutState, LayoutChunkResult result, LayoutManagerHelper helper,
                         boolean layoutInVertical, int parentWidth, int parentHeight, int parentHPadding, int parentVPadding) {

        int mainConsumed = 0;
        OrientationHelperEx orientationHelper = helper.getMainOrientationHelper();

        final View child1 = mChildrenViews[0];
        final VirtualLayoutManager.LayoutParams lp1 = new VirtualLayoutManager.LayoutParams(
                (VirtualLayoutManager.LayoutParams) child1.getLayoutParams());


        final View child2 = helper.getReverseLayout() ? mChildrenViews[4] : mChildrenViews[1];
        final VirtualLayoutManager.LayoutParams lp2 = new VirtualLayoutManager.LayoutParams(
                (VirtualLayoutManager.LayoutParams) child2.getLayoutParams());


        final View child3 = helper.getReverseLayout() ? mChildrenViews[3] : mChildrenViews[2];
        final VirtualLayoutManager.LayoutParams lp3 = new VirtualLayoutManager.LayoutParams(
                (VirtualLayoutManager.LayoutParams) child3.getLayoutParams());


        final View child4 = helper.getReverseLayout() ? mChildrenViews[2] : mChildrenViews[3];
        final VirtualLayoutManager.LayoutParams lp4 = new VirtualLayoutManager.LayoutParams(
                (VirtualLayoutManager.LayoutParams) child4.getLayoutParams());


        final View child5 = helper.getReverseLayout() ? mChildrenViews[1] : mChildrenViews[4];
        final VirtualLayoutManager.LayoutParams lp5 = new VirtualLayoutManager.LayoutParams(
                (VirtualLayoutManager.LayoutParams) child5.getLayoutParams());


        final float weight1 = getViewMainWeight(lp1, 0);
        final float weight2 = getViewMainWeight(lp1, 1);
        final float weight3 = getViewMainWeight(lp1, 2);
        final float weight4 = getViewMainWeight(lp1, 3);
        final float weight5 = getViewMainWeight(lp1, 4);

        if (layoutInVertical) {

            lp2.topMargin = lp1.topMargin;
            lp3.bottomMargin = lp4.bottomMargin = lp1.bottomMargin;
            lp3.leftMargin = lp2.leftMargin;
            lp4.rightMargin = lp2.rightMargin;
            lp5.rightMargin = lp2.rightMargin;

            if (!Float.isNaN(getAspectRatio())) {
                lp1.height = (int) ((parentWidth - parentHPadding) / getAspectRatio());
            }
            //可用宽度
            int availableSpace = parentWidth - parentHPadding - lp1.leftMargin - lp1.rightMargin
                    - lp2.leftMargin
                    - lp2.rightMargin;

            int width1 = Float.isNaN(weight1) ?
                    (int) (availableSpace / (10 / 6f) + 0.5f)
                    : (int) (availableSpace * weight1 / 100 + 0.5f);
            int width2 = Float.isNaN(weight2) ? availableSpace - width1 :
                    (int) (availableSpace * weight2 / 100 + 0.5f);
            //下面3个的宽度
            int bottomavailableSpace = parentWidth - parentHPadding - lp3.leftMargin - lp3.rightMargin
                    - lp4.leftMargin - lp4.rightMargin
                    - lp5.leftMargin - lp5.rightMargin;

            int width3 = Float.isNaN(weight4) ? (int) (bottomavailableSpace / 3.0f + 0.5f)
                    : (int) (availableSpace * weight3 / 100 + 0.5f);
            int width4 = Float.isNaN(weight4) ? (int) (bottomavailableSpace / 3.0f + 0.5f)
                    : (int) (availableSpace * weight4 / 100 + 0.5f);
            int width5 = Float.isNaN(weight4) ? (int) (bottomavailableSpace / 3.0f + 0.5f)
                    : (int) (availableSpace * weight5 / 100 + 0.5f);


            helper.measureChildWithMargins(child2,
                    MeasureSpec.makeMeasureSpec(width2 + lp2.leftMargin + lp2.rightMargin,
                            MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(width2 + lp2.topMargin + lp2.bottomMargin,
                            MeasureSpec.EXACTLY));

            //高度
            int height2 = child2.getMeasuredHeight();
            int height1 = height2;

            helper.measureChildWithMargins(child1,
                    MeasureSpec.makeMeasureSpec(width1 + lp1.leftMargin + lp1.rightMargin,
                            MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(height1 + lp1.topMargin + lp1.bottomMargin,
                            MeasureSpec.EXACTLY));

            helper.measureChildWithMargins(child3,
                    MeasureSpec.makeMeasureSpec(width3 + lp3.leftMargin + lp3.rightMargin,
                            MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(width3 + lp3.topMargin + lp3.bottomMargin,
                            MeasureSpec.EXACTLY));

            helper.measureChildWithMargins(child4,
                    MeasureSpec.makeMeasureSpec(width4 + lp4.leftMargin + lp4.rightMargin,
                            MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(width4 + lp4.topMargin + lp4.bottomMargin,
                            MeasureSpec.EXACTLY));

            helper.measureChildWithMargins(child5,
                    MeasureSpec.makeMeasureSpec(width5 + lp5.leftMargin + lp5.rightMargin,
                            MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(width5 + lp5.topMargin + lp5.bottomMargin,
                            MeasureSpec.EXACTLY));
            int height3 = child3.getMeasuredHeight();

            int maxTopHeight = Math.max(height1 + lp1.topMargin + lp1.bottomMargin,
                    height2 + lp2.topMargin + lp2.bottomMargin);

            int maxBottomHeight = Math.max(height3 + lp4.topMargin + lp4.bottomMargin,
                    Math.max(height3 + lp5.topMargin + lp5.bottomMargin,
                            height3 + lp5.topMargin + lp5.bottomMargin));

            mainConsumed = maxTopHeight + maxBottomHeight
                    + getVerticalMargin() + getVerticalPadding();

            calculateRect(mainConsumed - getVerticalMargin() - getVerticalPadding(), mAreaRect, layoutState, helper);

            int right1 = mAreaRect.left + orientationHelper
                    .getDecoratedMeasurementInOther(child1);
            layoutChildWithMargin(child1, mAreaRect.left, mAreaRect.top,
                    right1, mAreaRect.bottom - orientationHelper.getDecoratedMeasurement(child3), helper);

            int right2 = right1 + orientationHelper.getDecoratedMeasurementInOther(child2);
            layoutChildWithMargin(child2, right1, mAreaRect.top, right2,
                    mAreaRect.bottom - orientationHelper.getDecoratedMeasurement(child3),
                    helper);

            int right3 = mAreaRect.left + orientationHelper.getDecoratedMeasurementInOther(child3);
            layoutChildWithMargin(child3, mAreaRect.left,
                    mAreaRect.bottom - orientationHelper.getDecoratedMeasurement(child3),
                    right3, mAreaRect.bottom, helper);

            int right4 = right3 + orientationHelper.getDecoratedMeasurementInOther(child4);
            layoutChildWithMargin(child4, right3,
                    mAreaRect.bottom - orientationHelper.getDecoratedMeasurement(child4),
                    right4,
                    mAreaRect.bottom, helper);

            int right5 = right4 + orientationHelper.getDecoratedMeasurementInOther(child5);
            layoutChildWithMargin(child5, right4,
                    mAreaRect.bottom - orientationHelper.getDecoratedMeasurement(child5),
                    right5,
                    mAreaRect.bottom, helper);
        } else {
            // TODO: horizontal support
        }

        handleStateOnResult(result, mChildrenViews);
        return mainConsumed;
    }

}
