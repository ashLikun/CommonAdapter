package com.ashlikun.adapter.simple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.ashlikun.adapter.simple.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.commonAdapterTest.setOnClickListener {
            startActivity(Intent(this, CommonActivity::class.java))
        }
        binding.VLayoutTest.setOnClickListener {
            startActivity(Intent(this, VLayoutActivity::class.java))
        }
        binding.multiItem.setOnClickListener {
            startActivity(Intent(this, MultiItemActivity::class.java))
        }
        binding.sectionAdapter.setOnClickListener {
            startActivity(Intent(this, SectionActivity::class.java))
        }
    }


}