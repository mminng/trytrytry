package com.ming.trycompose

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by zh on 2021/8/17.
 */
class GeeksViewModel : ViewModel() {

    private val geeksService: GeeksService = GeeksService.create()

    private val _data: MutableLiveData<Result<GeeksResponse>> by lazy {
        MutableLiveData<Result<GeeksResponse>>()
    }

    val data: LiveData<Result<GeeksResponse>> = _data

    fun load(date: Long, number: Int) {
        viewModelScope.launch {
            val map: Map<String, String> =
                mapOf("date" to date.toString(), "num" to number.toString())
            _data.value = Result.Loading
            delay(3 * 1000)
            when (val result = enqueue { login(map) }) {
                is Async.Response -> {
                    _data.value =
                        Result.Success(GeeksResponse(geeksList = result.response.geeksList))
                }
                is Async.Failure -> {
                    result.exception.message?.let {
                        _data.value = Result.Failure(Exception(it))
                    }
                }
            }
        }
    }

    private suspend fun login(map: Map<String, String>): Async<GeeksResponse> {
        val response = geeksService.login(map)
        if (response.isSuccessful) {
            response.body()?.let {
                return Async.Response(it)
            }
        }
        return Async.Failure(Exception("登录失败"))
    }

}