package com.ashlikun.adapter.simple

import androidx.appcompat.app.AppCompatActivity
import com.ashlikun.adapter.simple.data.NeibuData
import java.util.ArrayList
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.ashlikun.adapter.recyclerview.CommonAdapter
import com.ashlikun.adapter.recyclerview.vlayout.MultipleAdapterHelp
import com.ashlikun.adapter.recyclerview.vlayout.SingAdapter
import com.ashlikun.adapter.simple.data.Data
import com.ashlikun.adapter.simple.databinding.ActivityCommentBinding
import com.ashlikun.adapter.simple.databinding.ItemViewBinding

/**
 * 作者　　: 李坤
 * 创建时间: 2018/5/4 0004　下午 2:47
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：commonAdapterTest
 */
class CommonActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityCommentBinding.inflate(layoutInflater)
    }
    var neibuData = ArrayList<NeibuData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        for (i in 0..99) {
            neibuData.add(NeibuData("我是第一种$i"))
        }
        initView()
    }

    private fun initView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = CommonAdapter(this, neibuData,
                apply = {
                    onItemClick = {
                        Toast.makeText(context, it.name, Toast.LENGTH_LONG).show()
                    }
                },
                bindingClass = ItemViewBinding::class.java) { holder, t ->
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