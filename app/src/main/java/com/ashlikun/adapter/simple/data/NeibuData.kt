package com.ashlikun.adapter.simple.data

import com.ashlikun.adapter.recyclerview.section.SectionEntity

/**
 * 作者　　: 李坤
 * 创建时间: 2018/4/9 0009　下午 5:19
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：
 */
data class NeibuData(var name: String, override val isHeader: Boolean = false) : SectionEntity