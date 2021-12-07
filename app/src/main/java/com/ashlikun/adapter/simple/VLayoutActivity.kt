package com.ashlikun.adapter.simple

import androidx.appcompat.app.AppCompatActivity
import com.ashlikun.adapter.simple.data.NeibuData
import java.util.ArrayList
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ashlikun.adapter.recyclerview.vlayout.MultipleAdapterHelp
import com.ashlikun.adapter.recyclerview.vlayout.SingAdapter
import com.ashlikun.adapter.recyclerview.vlayout.mode.AdapterStyle
import com.ashlikun.adapter.simple.data.Data
import com.ashlikun.adapter.simple.databinding.ActivityCommentBinding
import com.ashlikun.adapter.simple.databinding.ItemView1Binding
import com.ashlikun.adapter.simple.databinding.ItemViewBinding

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
        ActivityCommentBinding.inflate(layoutInflater)
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
            for (j in 0..10) {
                neibu.add(NeibuData("我是${type},数据是$j"))
            }
            data.add(Data(type, neibu))
        }
        initView()
    }

    private fun initView() {

        data.forEach {
            if (it.type == "type3") {
                help.adapter.addAdapter(SingAdapter(this, it.datas,
                    layoutStyle = AdapterStyle(spanCount = 3),
                    bindingClass = ItemView1Binding::class.java,
                    apply = {
                        onItemClick = {
                            Toast.makeText(context, it.name, Toast.LENGTH_LONG).show()
                        }
                    }) { holder, t ->
                    holder.binding<ItemView1Binding>().run {
                        textView.setTextColor(0xffffff00.toInt())
                        textView.text = t?.name
                    }
                })
            } else {
                help.adapter.addAdapter(SingAdapter(this, it.datas,
                    layoutStyle = AdapterStyle(),
                    bindingClass = ItemViewBinding::class.java,
                    apply = {
                        onItemClick = {
                            Toast.makeText(context, it.name, Toast.LENGTH_LONG).show()
                        }
                    }) { holder, t ->
                    holder.binding<ItemViewBinding>().run {
                        textView.text = t?.name
                    }
                })
            }
        }
//        addHeard()
    }

    private fun addHeard() {
        val imageView = ImageView(this)
        imageView.setImageResource(R.mipmap.ic_launcher_round)
        val params = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 600)
        imageView.layoutParams = params
        binding.recyclerView.addHeaderView(imageView)
    }
}