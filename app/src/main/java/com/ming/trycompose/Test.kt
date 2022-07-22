package com.ming.trycompose

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * Created by zh on 2022/6/30.
 */

suspend fun main() = runBlocking {

    val flow1: Flow<String> = flow {
        println("emit hello")
        emit("hello")
        println("emit hey")
        emit("hey")
        println("emit hi")
        emit("hi")
    }

    val flow2: Flow<Int> = flow {
        emit(1)
        emit(2)
        emit(3)
    }

//    val combine: Flow<Map<String, Int>> = combine(flow1, flow2) { f1, f2 ->
//        mapOf(f1 to f2)
//    }

//    val zip: Flow<Map<String, Int>> = flow1.zip(flow2) { f1, f2 ->
//        mapOf(f1 to f2)
//    }

//    try {
//        zip.collect {
//            for ((key, value) in it) {
//                delay(1000)
//                println("$key to $value")
//            }
//        }
//    } finally {
//        println("done")
//    }

//    combine.collect {
//        for ((string: String, int: Int) in it) {
//            delay(1000)
//            println("$string to $int")
//        }
//    }

    delay(2000)
    flow1.onCompletion { error ->
        if (error != null) println("error:${error}")
    }.catch { error ->
        println("catch:${error}")
    }.collect {
        println(it)
    }
    delay(4000)
    flow1.collect {
        println("then:$it")
    }
}

suspend fun getName(): String {
    delay(500)
    return "Tom"
}

suspend fun getAge(): Int {
    delay(4000)
    return 12
}

data class Goods(val shopName: String, val shopCount: Int)

class Test constructor(var title: String) {

    init {
        println("init:$title")
    }

    constructor(title: String, id: Int) : this(title) {
        println("con:$title")
    }

}