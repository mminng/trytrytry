package com.ming.trycompose

/**
 * Created by zh on 2022/7/14.
 */
sealed class Async<out T : Any> {

    data class Response<out T : Any>(val response: T) : Async<T>()
    data class Failure(val exception: Exception) : Async<Nothing>()
}

suspend fun <T : Any> enqueue(response: suspend () -> Async<T>): Async<T> {
    return try {
        response()
    } catch (e: Exception) {
        Async.Failure(e)
    }
}