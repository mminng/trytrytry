package com.ming.trycompose

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * Created by zh on 2021/8/17.
 */
class GeeksViewModel : ViewModel() {

    private val geeksService: GeeksService = GeeksService.create()

    private val _data: MutableLiveData<GeeksResponse> by lazy {
        MutableLiveData<GeeksResponse>()
    }

    val data: LiveData<GeeksResponse> = _data

    fun load(date: Long, number: Int) {
        viewModelScope.launch {
            val map: Map<String, String> =
                mapOf("date" to date.toString(), "num" to number.toString())
            _data.value = GeeksResponse(true)
            delay(3 * 1000)

            when (val result = enqueue { login(map) }) {
                is Result.Response -> {
                    _data.value = GeeksResponse(geeksList = result.response.geeksList)
                }
                is Result.Failure -> {
                    result.exception.message?.let {
                        _data.value = GeeksResponse(showError = it)
                    }
                }
            }
        }
    }

    private suspend fun login(map: Map<String, String>): Result<GeeksResponse> {
        val response = geeksService.login(map)
        if (response.isSuccessful) {
            response.body()?.let {
                return Result.Response(it)
            }
        }
        return Result.Failure(ServiceException("登录失败"))
    }

}