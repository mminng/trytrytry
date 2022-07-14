package com.ming.trycompose

/**
 * Created by zh on 2022/7/14.
 */
sealed class Result<out T : Any> {

    data class Response<out T : Any>(val response: T) : Result<T>()
    data class Failure(val exception: Exception) : Result<Nothing>()
}

suspend fun <T : Any> enqueue(response: suspend () -> Result<T>): Result<T> {
    return try {
        response()
    } catch (e: Exception) {
        Result.Failure(e)
    }
}

class ServiceException(message: String) : Exception(message)