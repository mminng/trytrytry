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
            val uiState = GeeksResponse(true)
            _data.value = uiState
            delay(3 * 1000)
            when (val result: Result<GeeksResponse> = enqueue { login(map) }) {
                is Result.Response -> {
                    uiState.showProgress = false
                    uiState.geeksList = result.response.geeksList
                    _data.value = uiState
                }
                is Result.Failure -> {
                    uiState.showProgress = false
                    uiState.showError = result.exception.message
                    _data.value = uiState
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