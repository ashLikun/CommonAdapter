package com.ashlikun.adapter.simple

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ashlikun.adapter.ViewHolder
import com.ashlikun.adapter.recyclerview.common.CommonAdapter
import com.ashlikun.adapter.simple.data.NeibuData
import com.ashlikun.adapter.simple.databinding.ActivityCommentBinding
import com.ashlikun.adapter.simple.databinding.ItemViewBinding
import java.util.*

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
        neibuData.sortBy { it.name }
        val dd = NeibuData("aaaaaa")

        val aa = dd::name
        aa.get()
        initView()
    }

    private fun initView() {
        apply { }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = CommonAdapter(
            this, neibuData, ItemViewBinding::class.java,
            itemId = { it.name },
            onItemClick = {
                Toast.makeText(this, it.name, Toast.LENGTH_LONG).show()
            }, convert = { t ->
                binding<ItemViewBinding>().apply {
                    textView.text = t?.name
                    isLostPosition
                }
            },
            apply = {

            }
        )
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

class MyCommonAdapter(context: Context) :
    CommonAdapter<NeibuData>(context) {
    override fun convert(holder: ViewHolder, t: NeibuData) {
        super.convert(holder, t)
    }
}