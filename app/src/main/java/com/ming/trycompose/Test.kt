package com.ming.trycompose

import android.util.Log

/**
 * Created by zh on 2022/6/30.
 */

fun main() {
    val test: Test = Test("Tom", 23)
}

class Test constructor(var title: String) {

    init {
        println("init:$title")
    }

    constructor(title: String, id: Int) : this(title) {
        println("con:$title")
    }

}