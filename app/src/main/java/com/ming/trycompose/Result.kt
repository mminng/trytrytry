package com.ming.trycompose

import java.lang.Exception

/**
 * Created by zh on 2022/6/28.
 */
sealed class Result<out R> {
    object Loading : Result<Nothing>()
    data class Success<out R>(val data: R) : Result<R>()
    data class Failure(val exception: Exception) : Result<Nothing>()
}