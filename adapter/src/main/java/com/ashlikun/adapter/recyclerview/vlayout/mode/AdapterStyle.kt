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
data class AdapterStyle(
        //单位DP
        var padding: Float = 0f,
        var paddingLeft: Float = 0f,
        var paddingRight: Float = 0f,
        var paddingTop: Float = 0f,
        var paddingBottom: Float = 0f,

        //单位DP
        var margin: Float = 0f,
        var marginLeft: Float = 0f,
        var marginRight: Float = 0f,
        var marginTop: Float = 0f,
        var marginBottom: Float = 0f,

        //宫格的几个
        var gap: Int = 0,
        var vGap: Int = 0,
        var hGap: Int = 0,

        //高度
        var height: Int = 0,

        //宽度
        var width: Int = 0,

        //颜色
        var color: String = "",

        //字体大小
        var fontSize: Float = 0f,

        //字体颜色
        var fontColor: String = "",

        //圆角,全部
        var corner: Float = 0f,

        //圆角 左上右下
        var corners: List<Float>? = null

) : Serializable {


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
