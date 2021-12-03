package com.ashlikun.adapter.simple

import android.content.Context
import com.ashlikun.adapter.recyclerview.vlayout.SingAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import android.view.ViewGroup
import android.view.View
import android.util.Log
import com.ashlikun.adapter.animation.SlideInBottomAnimation
import com.ashlikun.adapter.ViewHolder
import com.ashlikun.adapter.simple.databinding.ItemView1Binding

/**
 * 作者　　: 李坤
 * 创建时间: 2018/4/9 0009　下午 5:17
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：
 */
class MyAdapter {
    class AdapterItem1(context: Context, datas: List<NeibuData?>?) : SingAdapter<NeibuData?>(
        context, datas
    ) {
        override fun getItemViewType(position: Int): Int {
            return super.getItemViewType(position)
        }

        override fun getLayoutId(): Int {
            return R.layout.item_view
        }

        override fun onCreateLayoutHelper(): LayoutHelper? {
            val helper = GridLayoutHelper(3)
            helper.setAutoExpand(false)
            return helper
        }

        override fun convert(holder: ViewHolder, neibuData: NeibuData?) {
            holder.setText(R.id.textView, neibuData?.name)
        }
    }

    class AdapterItem2(context: Context, datas: List<Neibu2Data?>?) : SingAdapter<Neibu2Data?>(
        context, datas
    ) {
        override fun onCreateLayoutHelper(): LayoutHelper? {
            val helper = LinearLayoutHelper(20)
            helper.setDividerHeight(30)
            return helper
        }

        override fun convert(holder: ViewHolder, neibuData: Neibu2Data?) {
            val bb: ItemView1Binding = holder.viewBinding()
            holder.viewBinding<ItemView1Binding>().textView.text =
                neibuData?.name
        }

        override fun onItemClick(
            viewType: Int,
            parent: ViewGroup,
            view: View,
            data: Neibu2Data,
            position: Int
        ) {
            super.onItemClick(viewType, parent, view, data, position)
            Log.e("aaaa", position.toString() + "")
        }

        init {
            bindingClass = ItemView1Binding::class.java
        }
    }

    class AdapterItem3(context: Context?, datas: List<Neibu3Data?>?) : SingAdapter<Neibu3Data?>(
        context!!, datas
    ) {
        override fun getLayoutId(): Int {
            return R.layout.item_view2
        }

        override fun onCreateLayoutHelper(): LayoutHelper? {
            return LinearLayoutHelper(20)
        }

        override fun convert(holder: ViewHolder, neibuData: Neibu3Data?) {
            holder.setText(R.id.textView, neibuData?.name)
        }

        init {
            setCustomAnimation(SlideInBottomAnimation())
        }
    }

    class AdapterItem4(context: Context?, datas: List<Neibu3Data?>?) : SingAdapter<Neibu3Data?>(
        context!!, datas
    ) {
        override fun getLayoutId(): Int {
            return R.layout.item_view4
        }

        override fun onCreateLayoutHelper(): LayoutHelper? {
            val helper = OnePlusFourLayoutHelper(5)
            helper.setMargin(10, 10, 10, 10)
            return helper
        }

        override fun convert(holder: ViewHolder, neibuData: Neibu3Data?) {
            holder.setText(R.id.textView, neibuData?.name)
        }

        init {
            setCustomAnimation(SlideInBottomAnimation())
            viewType = AdapterItem4::class.java
        }
    }
}