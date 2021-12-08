package com.ashlikun.adapter.recyclerview.vlayout.mode

import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.*
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
data class LayoutStyle(
    /////////////////////LayoutHelp开始/////////////////////
    //单位 px
    var padding: Int? = null,
    var paddingLeft: Int? = null,
    var paddingRight: Int? = null,
    var paddingTop: Int? = null,
    var paddingBottom: Int? = null,

    //单位 px
    var margin: Int? = null,
    var marginLeft: Int? = null,
    var marginRight: Int? = null,
    var marginTop: Int? = null,
    var marginBottom: Int? = null,
    //Base
    //纵横比
    var aspectRatio: Float? = null,

    //宫格的几个
    //几列,用于创建GridLayoutHelper
    var spanCount: Int? = null,
    //GridLayoutHelper的spanSizeLookup
    var spanSizeLookup: GridLayoutHelper.SpanSizeLookup? = null,
    var gap: Int? = null,
    var vGap: Int? = null,
    var hGap: Int? = null,
    //自动展开
    var autoExpand: Boolean? = false,


    //分割线高度，用于开启LinearLayoutHelper，默认也是LinearLayoutHelper
    var dividerHeight: Int? = null,

    //ColumnLayoutHelper,栏格布局，和布局在一排，可以配置不同列之间的宽度比值
    var weights: FloatArray? = null,

    //FixLayoutHelper  固定布局，始终在屏幕固定位置显示
    //FloatLayoutHelper: 浮动布局，可以固定显示在屏幕上，但用户可以拖拽其位置
    val float: Boolean? = null,
    val fixX: Int? = null,
    val fixY: Int? = null,
    //对齐方式
    //   public static final int TOP_LEFT = 0;
    //    public static final int TOP_RIGHT = 1;
    //    public static final int BOTTOM_LEFT = 2;
    //    public static final int BOTTOM_RIGHT = 3;
    val alignType: Int? = null,

    //StickyLayoutHelper,stikcy布局， 可以配置吸顶或者吸底
    val sticky: Boolean? = null,
    val offset: Int? = null,
    //SingleLayoutHelper,通栏布局，只会显示一个组件View
    val single: Boolean? = null,


    /////////////////////LayoutHelp结束/////////////////////
    //高度
    var height: Int? = null,

    //宽度
    var width: Int? = null,

    //颜色
    var color: String? = null,

    //字体大小
    var fontSize: Int? = null,

    //字体颜色
    var fontColor: String? = null,

    //圆角,全部
    var corner: Int? = null,

    //圆角 左上右下
    var corners: List<Int>? = null

) : Serializable {

    /**
     * 自动创建对应的类型
     */
    fun createHelper(): LayoutHelper {
        return when {
            spanCount != null -> bindHelperUI(GridLayoutHelper(spanCount!!))
            weights != null -> bindHelperUI(ColumnLayoutHelper())
            fixX != null || fixY != null -> bindHelperUI(
                if (float == true) FloatLayoutHelper() else FixLayoutHelper(
                    fixX
                        ?: 0, fixY ?: 0
                )
            )
            sticky != null -> bindHelperUI(StickyLayoutHelper(sticky!!))
            single == true -> bindHelperUI(SingleLayoutHelper())
            else -> bindHelperUI(LinearLayoutHelper())
        }
    }

    fun bindHelperUI(helper: LayoutHelper): LayoutHelper {
        if (helper is BaseLayoutHelper) {
            if (aspectRatio != null) helper.aspectRatio = aspectRatio!!
        }
        if (helper is ColumnLayoutHelper) {
            if (weights != null) helper.setWeights(weights)
        }
        if (helper is StickyLayoutHelper) {
            if (sticky != null) helper.setStickyStart(sticky)
            if (offset != null) helper.setOffset(offset)
        }
        if (helper is FixLayoutHelper) {
            if (fixX != null) helper.setX(fixX)
            if (fixY != null) helper.setY(fixY)
            if (alignType != null) helper.setAlignType(alignType)
        }
        if (helper is FloatLayoutHelper) {
            if (fixX != null) helper.setX(fixX)
            if (fixY != null) helper.setY(fixY)
            if (alignType != null) helper.setAlignType(alignType)
        }
        if (helper is MarginLayoutHelper) {
            if (marginLeft != null) helper.marginLeft = marginLeft!!
            if (marginTop != null) helper.marginTop = marginTop!!
            if (marginRight != null) helper.marginRight = marginRight!!
            if (marginBottom != null) helper.marginBottom = marginBottom!!
            if (paddingLeft != null) helper.paddingLeft = paddingLeft!!
            if (paddingTop != null) helper.paddingTop = paddingTop!!
            if (paddingRight != null) helper.paddingRight = paddingRight!!
            if (paddingBottom != null) helper.paddingBottom = paddingBottom!!
            if (padding != null) helper.setPadding(padding!!, padding!!, padding!!, padding!!)
            if (margin != null) helper.setMargin(margin!!, margin!!, margin!!, margin!!)
        }
        if (helper is LinearLayoutHelper) {
            if (dividerHeight != null) helper.setDividerHeight(dividerHeight!!)
        }
        if (helper is GridLayoutHelper) {
            if (spanCount != null) helper.spanCount = spanCount!!
            if (spanSizeLookup != null) helper.setSpanSizeLookup(spanSizeLookup!!)
            if (vGap != null) helper.vGap = vGap!!
            if (hGap != null) helper.hGap = hGap!!
            if (gap != null) helper.setGap(gap!!)
            if (autoExpand != null) helper.setAutoExpand(autoExpand!!)

        }
        return helper
    }
}
