package com.ashlikun.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.util.Linkify
import android.util.SparseArray
import android.view.View
import android.view.View.OnLongClickListener
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.*
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ashlikun.adapter.recyclerview.BaseAdapter
import com.ashlikun.adapter.recyclerview.vlayout.IStartPosition

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/2 21:57
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 * 如果Adapter是由ViewBinding创建的View，那么可以这里获取ViewBinding，
 * Kotlin 方法是[AdapterExtendKt.binding]
 * Java: getViewBinding
 */

open class ViewHolder(
    var createView: CreateView,
    var iStartPosition: IStartPosition? = null
) : RecyclerView.ViewHolder(createView.view) {
    protected var mViews = SparseArray<View>()
    protected val context: Context
        get() = createView.view.context

    //实例是ViewBinding
    var binding: Any? = null
        protected set
        get() = createView.viewBinding

    fun <T : Any> binding(): T {
        return binding as T
    }

    inline fun <T : Any> binding(block: T.() -> Unit) {
        block(binding())
    }

    /**
     * 设置item点击颜色,就当前Holder   item
     *
     * @param itemClickColor
     */
    /**
     * item点击颜色
     */
    var itemClickColor = Color.GRAY

    /**
     * 当前布局设置的点击颜色，标记是否设置过
     */
    private var isSetClickColor = -1


    /**
     * 是否设置过item点击效果
     *
     * @param color
     * @return
     */
    fun isSetEffects(color: Int): Boolean {
        return isSetClickColor == color
    }

    fun setEffects(color: Int) {
        isSetClickColor = color
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    fun <T : View> getView(viewId: Int): T? {
        var view = mViews[viewId]
        if (view == null) {
            view = itemView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as T?
    }

    /**
     * 强制获取
     */
    fun <T : View> requireView(viewId: Int): T {
        return getView(viewId) ?: throw RuntimeException("ViewHolder 没有发现 viewId")
    }

    /**
     * 强制获取
     */
    fun getImageView(viewId: Int): ImageView {
        return getView(viewId) ?: throw RuntimeException("ViewHolder 没有发现 viewId")
    }

    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param text
     * @return
     */
    fun setText(viewId: Int, text: CharSequence?): ViewHolder {
        val tv = getView<TextView>(viewId)
        tv?.text = text
        return this
    }

    fun setText(viewId: Int, text: Number): ViewHolder {
        val tv = getView<TextView>(viewId)
        tv?.text = text.toString()
        return this
    }

    fun setImageResource(viewId: Int, resId: Int): ViewHolder {
        val view = getView<ImageView>(viewId)
        view?.setImageResource(resId)
        return this
    }

    fun setImageBitmap(viewId: Int, bitmap: Bitmap?): ViewHolder {
        val view = getView<ImageView>(viewId)
        view?.setImageBitmap(bitmap)
        return this
    }

    fun setImageDrawable(viewId: Int, drawable: Drawable?): ViewHolder {
        val view = getView<ImageView>(viewId)
        view?.setImageDrawable(drawable)
        return this
    }

    fun setBackgroundColor(viewId: Int, color: Int): ViewHolder {
        val view = getView<View>(viewId)
        view?.setBackgroundColor(color)
        return this
    }

    fun setBackgroundRes(viewId: Int, backgroundRes: Int): ViewHolder {
        val view = getView<View>(viewId)
        view?.setBackgroundResource(backgroundRes)
        return this
    }

    fun setBackgroundDrawable(viewId: Int, drawable: Drawable?): ViewHolder {
        val view = getView<View>(viewId)?.apply {
            ViewCompat.setBackground(this, drawable)
        }
        return this
    }

    fun setTextColor(viewId: Int, textColor: Int): ViewHolder {
        val view = getView<TextView>(viewId)
        view?.setTextColor(textColor)
        return this
    }

    fun setTextColorRes(viewId: Int, textColorRes: Int): ViewHolder {
        val view = getView<TextView>(viewId)
        view?.setTextColor(context.resources.getColor(textColorRes))
        return this
    }

    fun setAlpha(viewId: Int, value: Float): ViewHolder {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView<View>(viewId)?.alpha = value
        } else {
            // Pre-honeycomb hack to set Alpha value
            val alpha = AlphaAnimation(value, value)
            alpha.duration = 0
            alpha.fillAfter = true
            getView<View>(viewId)?.startAnimation(alpha)
        }
        return this
    }

    fun setVisible(viewId: Int, visible: Boolean): ViewHolder {
        val view = getView<View>(viewId)
        view?.visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }

    fun linkify(viewId: Int): ViewHolder {
        getView<TextView>(viewId)?.apply {
            Linkify.addLinks(this, Linkify.ALL)
        }
        return this
    }

    fun setTypeface(typeface: Typeface?, vararg viewIds: Int): ViewHolder {
        for (viewId in viewIds) {
            getView<TextView>(viewId)?.also {
                it.typeface = typeface
                it.paintFlags = it.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
            }
        }
        return this
    }

    fun setProgress(viewId: Int, progress: Int): ViewHolder {
        val view = getView<ProgressBar>(viewId)
        view?.progress = progress
        return this
    }

    fun setProgress(viewId: Int, progress: Int, max: Int): ViewHolder {
        val view = getView<ProgressBar>(viewId)
        view?.max = max
        view?.progress = progress
        return this
    }

    fun setMax(viewId: Int, max: Int): ViewHolder {
        val view = getView<ProgressBar>(viewId)
        view?.max = max
        return this
    }

    fun setRating(viewId: Int, rating: Float): ViewHolder {
        val view = getView<RatingBar>(viewId)
        view?.rating = rating
        return this
    }

    fun setRating(viewId: Int, rating: Float, max: Int): ViewHolder {
        val view = getView<RatingBar>(viewId)
        view?.max = max
        view?.rating = rating
        return this
    }

    fun setTag(viewId: Int, tag: Any?): ViewHolder {
        val view = getView<View>(viewId)
        view?.tag = tag
        return this
    }

    fun setTag(viewId: Int, key: Int, tag: Any?): ViewHolder {
        val view = getView<View>(viewId)
        view?.setTag(key, tag)
        return this
    }

    fun setChecked(viewId: Int, checked: Boolean): ViewHolder {
        val view = getView<View>(viewId) as Checkable
        view.isChecked = checked
        return this
    }

    fun setViewSize(view: View, size: Int) {
        var params = view.layoutParams
        if (params == null) {
            params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        params.height = size
        params.width = size
        view.layoutParams = params
        view.minimumWidth = size
        view.minimumHeight = size
    }

    /**
     * 关于事件的
     */
    fun setOnClickListener(
        viewId: Int,
        listener: View.OnClickListener?
    ): ViewHolder {
        val view = getView<View>(viewId)
        view?.setOnClickListener(listener)
        return this
    }

    fun setOnTouchListener(
        viewId: Int,
        listener: OnTouchListener?
    ): ViewHolder {
        val view = getView<View>(viewId)
        view?.setOnTouchListener(listener)
        return this
    }

    fun setOnLongClickListener(
        viewId: Int,
        listener: OnLongClickListener?
    ): ViewHolder {
        val view = getView<View>(viewId)
        view?.setOnLongClickListener(listener)
        return this
    }

    /**
     * 获取内部的位置
     * 下面这些是相对于整个列表的位置
     * 也可以使用这些：[.getLayoutPosition]
     * 也可以使用这些：[.getAdapterPosition]
     *
     * @return
     */
    val positionInside: Int
        get() = layoutPosition - startPosition
    val startPosition: Int
        get() = iStartPosition?.startPosition ?: 0

    /**
     * 是否是瀑布流
     *
     * @return
     */
    val isStaggeredGridLayout: Boolean
        get() {
            val layoutParams = itemView.layoutParams
            return layoutParams is StaggeredGridLayoutManager.LayoutParams
        }

    /**
     * 是否是最后一个
     */
    fun isLostPosition(adapter: BaseAdapter<*, *>): Boolean {
        return positionInside == adapter.itemCount - 1
    }

    /**
     * 是否是第一个
     */
    val isFirstPosition: Boolean
        get() = positionInside == 0


}