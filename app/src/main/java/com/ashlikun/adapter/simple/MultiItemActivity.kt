package com.ashlikun.adapter.simple

import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ashlikun.adapter.recyclerview.multiltem.MultiItemCommonAdapter
import com.ashlikun.adapter.simple.data.MultiItemData
import com.ashlikun.adapter.simple.data.NeibuData
import com.ashlikun.adapter.simple.databinding.ActivityCommentBinding
import com.ashlikun.adapter.simple.databinding.ItemView1Binding
import com.ashlikun.adapter.simple.databinding.ItemViewBinding

/**
 * 作者　　: 李坤
 * 创建时间: 2018/5/4 0004　下午 2:47
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：原生的多样式列表
 */
class MultiItemActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityCommentBinding.inflate(layoutInflater)
    }
    val adapter by lazy {
        MultiItemCommonAdapter<MultiItemData>(this,
            bindings = hashMapOf(
                "type1".hashCode() to ItemViewBinding::class.java,
                "type2".hashCode() to ItemView1Binding::class.java
            ),
            onItemClick = {
                Toast.makeText(this, it?.type + "  " + it?.data?.name, Toast.LENGTH_LONG).show()
            },
            itemType = { data -> data.type.hashCode() }) {  t ->
            when (itemViewType) {
                "type2".hashCode() -> binding<ItemView1Binding>().run {
                    textView.text = t?.type + "  " + t?.data?.name
                }
                else -> binding<ItemViewBinding>().run {
                    textView.text = t?.type + "  " + t?.data?.name
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        for (i in 1..100) {
            val type = if (i > 10) "type2" else "type1"
            adapter.dataHandle.addDatas(MultiItemData(type, NeibuData("我是第$i")))
        }
        initView()
    }

    private fun initView() {
        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerView.adapter = adapter
        addHeard()
    }

    private fun addHeard() {
        val imageView = ImageView(this)
        imageView.setImageResource(R.mipmap.ic_launcher_round)
        val params = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 600)
        imageView.layoutParams = params
        binding.recyclerView.addHeaderView(imageView)
    }
}