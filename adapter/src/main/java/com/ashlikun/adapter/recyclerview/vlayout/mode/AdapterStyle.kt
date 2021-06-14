package com.ashlikun.adapter.recyclerview.vlayout.mode

import android.content.Context
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.alibaba.android.vlayout.layout.MarginLayoutHelper
import com.ashlikun.adapter.AdapterUtils
import java.io.Serializable

/**
 * 作者　　: 李坤
 * 创建时间: 2021/6/12　23:39
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 */
/**
 * 样式
 */
class AdapterStyle :Serializable{
    //单位DP
    var padding = 0f
    var paddingLeft = 0f
    var paddingRight = 0f
    var paddingTop = 0f
    var paddingBottom = 0f

    //单位DP
    var margin = 0f
    var marginLeft = 0f
    var marginRight = 0f
    var marginTop = 0f
    var marginBottom = 0f

    //宫格的几个
    var gap = 0
    var vGap = 0
    var hGap = 0


    fun bindHelperUI(context: Context, helper: LayoutHelper) {
        if (helper is MarginLayoutHelper) {
            helper.marginLeft = AdapterUtils.dip2px(context, marginLeft)
            helper.marginTop = AdapterUtils.dip2px(context, marginTop)
            helper.marginRight = AdapterUtils.dip2px(context, marginRight)
            helper.marginBottom = AdapterUtils.dip2px(context, marginBottom)
            helper.paddingLeft = AdapterUtils.dip2px(context, paddingLeft)
            helper.paddingTop = AdapterUtils.dip2px(context, paddingTop)
            helper.paddingRight = AdapterUtils.dip2px(context, paddingRight)
            helper.paddingBottom = AdapterUtils.dip2px(context, paddingBottom)
            if (padding != 0f) {
                val paddingPx = AdapterUtils.dip2px(context, padding)
                helper.setPadding(paddingPx, paddingPx, paddingPx, paddingPx)
            }
            if (margin != 0f) {
                val marginPx = AdapterUtils.dip2px(context, margin)
                helper.setMargin(marginPx, marginPx, marginPx, marginPx)
            }
        }
        if (helper is GridLayoutHelper) {
            if (vGap > 0) helper.vGap = vGap
            if (hGap > 0) helper.hGap = hGap
            if (gap > 0) {
                helper.setGap(gap)
            }
        }
    }
}
//文字大小
//文字颜色
//图片大小
//图片圆角
//vGap hGap//一行几个