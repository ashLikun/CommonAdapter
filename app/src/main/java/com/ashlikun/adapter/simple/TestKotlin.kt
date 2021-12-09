package com.ashlikun.adapter.simple

/**
 * 作者　　: 李坤
 * 创建时间: 2021.12.6　23:37
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 */

fun main(args: Array<String>) {
    BCls().print()
}

open class ACls(
    open var a: Int = 1,
    open var b: String = "ad",
    open var c: Int = 2
) {
    val ss = a + c

    open var maxHeight = 1f

    init {
        print()
        println(222222222)
    }

    open fun print(): Unit {
        println("a=${a}")
        println("b=${b}")
        println("c=${c}")
        println("ss=${ss}")
    }
}

class BCls(
    override var a: Int = 10,
    override var b: String = "StringStringString",
    override var c: Int = 100
) : ACls(a = a, b = b, c = c) {

    var maxRatio: Float = 0f
        set(value) {
            field = value
        }

    init {
        print()
        println(11111111111111)
    }

    override fun print() {
        maxRatio = 20f
        println("aa=${a}")
        println("bb=${b}")
        println("cc=${c}")
        println("sss=${ss}")
        println("maxRatio=${maxRatio}")
        if (this is ACls) {
            println("dddddddddddddd")
        }
        var position = 11
        if (position in -1..10) {
            println("ssssssssssss")
        }
        val aaad = null
        if (aaad is ACls) {
            println("aaad")
        }
    }
}