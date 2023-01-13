package com.ashlikun.adapter.simple

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.ashlikun.adapter.recyclerview.common.CommonAdapter
import com.ashlikun.adapter.recyclerview.section.SectionAdapter
import com.ashlikun.adapter.recyclerview.vlayout.MultipleAdapterHelp
import com.ashlikun.adapter.recyclerview.vlayout.mode.LayoutStyle
import com.ashlikun.adapter.simple.data.Data
import com.ashlikun.adapter.simple.data.NeibuData
import com.ashlikun.adapter.simple.databinding.ActivityComment2Binding
import com.ashlikun.adapter.simple.databinding.ActivityCommentBinding
import com.ashlikun.adapter.simple.databinding.ItemHeaderBinding
import com.ashlikun.adapter.simple.databinding.ItemView1Binding
import com.ashlikun.adapter.simple.databinding.ItemViewBinding
import java.util.*

/**
 * 作者　　: 李坤
 * 创建时间: 2018/5/4 0004　下午 2:47
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：Vlayout 多样式列表
 */
class VLayoutActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityComment2Binding.inflate(layoutInflater)
    }
    val help by lazy {
        MultipleAdapterHelp(binding.recyclerView)
    }
    var data = ArrayList<Data>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        for (i in 1..10) {
            val type = "type${i}"
            val neibu = mutableListOf<NeibuData>()
            when (type) {
                "type1" -> {
                    for (j in 0..10) {
                        neibu.add(NeibuData("我是${type},数据是$j"))
                    }
                }
                "type4" -> {
                    for (j in 0..30) {
                        val isHeader = j % 10 == 0
                        val headerPosi = j / 10
                        neibu.add(
                            NeibuData(
                                if (isHeader) "我是头,我是${type}：${headerPosi}" else "我是${type},数据是$j",
                                isHeader
                            )
                        )
                    }
                }
                "type6" -> {
                    for (j in 0..30) {
                        val isHeader = j % 10 == 0
                        val headerPosi = j / 10
                        neibu.add(
                            NeibuData(
                                if (isHeader) "我是头,我是${type}：${headerPosi}" else "我是${type},数据是$j",
                                isHeader
                            )
                        )
                    }
                }
                else -> {
                    for (j in 0..10) {
                        neibu.add(NeibuData("我是${type},数据是$j"))
                    }
                }
            }
            data.add(Data(type, neibu))
        }
        initView()
    }

    private fun initView() {
        help.adapter.addAdapters(data.map {
            when (it.type) {
                "type1" ->
                    CommonAdapter(this, it.datas, ItemView1Binding::class.java,
                        layoutStyle = LayoutStyle(single = true, bgColor = Color.RED),
                        onItemClick = {
                            Toast.makeText(this, it.name, Toast.LENGTH_LONG).show()
                        }) { t ->
                        binding<ItemView1Binding>().run {
                            textView.setTextColor(0xffff3300.toInt())
                            textView.text = t?.name
                        }
                    }
                "type2" ->
                    CommonAdapter(this, it.datas, ItemView1Binding::class.java,
                        layoutStyle = LayoutStyle(
                            spanCount = 3,
                            bgDrawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(Color.RED, Color.GREEN)).apply {
                                cornerRadius = 30f
                            }
                        ),
                        onItemClick = {
                            Toast.makeText(this, it.name, Toast.LENGTH_LONG).show()
                        }) { t ->
                        binding<ItemView1Binding>().run {
                            textView.setTextColor(0xff0fff00.toInt())
                            textView.text = t?.name
                        }
                    }
                "type4" ->
                    SectionAdapter(this, it.datas, ItemViewBinding::class.java,
                        layoutStyle = LayoutStyle(spanCount = 3),
                        onItemClick = {
                            Toast.makeText(this, it.name, Toast.LENGTH_LONG).show()
                        },
                        bndingHead = ItemHeaderBinding::class.java,
                        convertHeader = { t ->
                            binding<ItemHeaderBinding>().run {
                                tvHeader.text = t?.name
                            }
                        }
                    ) { t ->
                        binding<ItemViewBinding>().run {
                            textView.text = t?.name
                        }
                    }
                else ->
                    CommonAdapter(this, it.datas, ItemViewBinding::class.java,
                        layoutStyle = LayoutStyle(
                            bgDrawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(Color.RED, Color.GREEN)).apply {
                                cornerRadius = 30f
                            }
                        ),
                        onItemClick = {
                            Toast.makeText(this, it.name, Toast.LENGTH_LONG).show()
                        }) { t ->
                        binding<ItemViewBinding>().run {
                            textView.text = t?.name
                        }
                    }
            }
        })

//        addHeard()
    }

    private fun addHeard() {
        val imageView = ImageView(this)
        imageView.setImageResource(R.mipmap.ic_launcher_round)
        val params = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 600)
        imageView.layoutParams = params
//        binding.recyclerView.addHeaderView(imageView)
    }
}