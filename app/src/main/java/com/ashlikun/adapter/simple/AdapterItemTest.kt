package com.ashlikun.adapter.simple

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.ashlikun.adapter.ViewHolder
import com.ashlikun.adapter.recyclerview.CommonAdapter
import com.ashlikun.adapter.recyclerview.vlayout.SingAdapter
import com.ashlikun.adapter.simple.databinding.ItemView1Binding
import com.ashlikun.adapter.viewBinding
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * 作者　　: 李坤
 * 创建时间: 2021/6/18　17:24
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 */
class AdapterItemTest(context: Context, datas: List<Neibu2Data>) : SingAdapter<Neibu2Data>(context, datas) {
    override fun createViewBinding(parent: ViewGroup, viewType: Int): Any {
        return ItemView1Binding.inflate(layoutInflater, parent, false)
    }

    init {
        layoutHelper = LinearLayoutHelper(20)
        (layoutHelper as LinearLayoutHelper).setDividerHeight(30)
    }

    override fun convert(holder: ViewHolder, t: Neibu2Data) {
        holder.viewBinding<ItemView1Binding> {
            textView.text = t.name
        }
    }
}

