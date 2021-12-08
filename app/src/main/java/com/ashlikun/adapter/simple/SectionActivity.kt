package com.ashlikun.adapter.simple

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ashlikun.adapter.ViewHolder
import com.ashlikun.adapter.recyclerview.CommonAdapter
import com.ashlikun.adapter.recyclerview.section.SectionAdapter
import com.ashlikun.adapter.simple.data.NeibuData
import com.ashlikun.adapter.simple.databinding.ActivityCommentBinding
import com.ashlikun.adapter.simple.databinding.ItemHeaderBinding
import com.ashlikun.adapter.simple.databinding.ItemViewBinding
import java.util.*

/**
 * 作者　　: 李坤
 * 创建时间: 2018/5/4 0004　下午 2:47
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：SectionAdapter 的测试
 */
class SectionActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityCommentBinding.inflate(layoutInflater)
    }
    var neibuData = ArrayList<NeibuData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        for (i in 0..99) {
            val isHeader = i % 10 == 0
            val headerPosi = i / 10
            neibuData.add(NeibuData(if (isHeader) "我是头：${headerPosi}" else "我是第一种$i", isHeader))
        }
        initView()
    }

    private fun initView() {
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.adapter = SectionAdapter(
            this, neibuData,
            apply = {
                onItemClick = {
                    Toast.makeText(context, it.name, Toast.LENGTH_LONG).show()
                }
            },
            binding = ItemViewBinding::class.java,
            bndingHead = ItemHeaderBinding::class.java,
            convertHeader = { holder, t ->
                holder.binding<ItemHeaderBinding>().run {
                    tvHeader.text = t?.name
                }
            }
        ) { holder, t ->
            holder.binding<ItemViewBinding>().run {
                textView.text = t?.name
            }
        }
        addHeard();
    }

    private fun addHeard() {
        val imageView = ImageView(this)
        imageView.setImageResource(R.mipmap.ic_launcher_round)
        val params = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 600)
        imageView.layoutParams = params
        binding.recyclerView.addHeaderView(imageView)
    }
}

class MySectionAdapter(context: Context) : CommonAdapter<NeibuData>(context) {
    override fun convert(holder: ViewHolder, t: NeibuData?) {
    }
}