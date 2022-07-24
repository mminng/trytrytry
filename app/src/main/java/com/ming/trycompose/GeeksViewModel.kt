package com.ming.trycompose

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by zh on 2021/8/17.
 */
class GeeksViewModel : ViewModel() {

    private val geeksService: GeeksService = GeeksService.create()

//    private val _message: Channel<String> = Channel()
//    val message: Flow<String> = _message.receiveAsFlow()
//
//    private val _result: MutableStateFlow<Result<GeeksResponse>> = MutableStateFlow(
//        Result.Success(
//            GeeksResponse()
//        )
//    )
//    val result: StateFlow<Result<GeeksResponse>> = _result

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _toast: MutableStateFlow<String?> = MutableStateFlow(null)
    private val _data: MutableStateFlow<Result<GeeksResponse>> = MutableStateFlow(
        Result.Success(
            GeeksResponse()
        )
    )

    val uiState: SharedFlow<UiState> =
        combine(_isLoading, _toast, _data) { isLoading, toast, data ->
            when (data) {
                is Result.Success -> {
                    UiState(isLoading = isLoading, result = data.data.geeksList, message = toast)
                }
                is Result.Failure -> {
                    UiState(isLoading = isLoading, message = toast)
                }
            }
        }.shareIn(viewModelScope, WhileUiSubscribed, 0)

    fun load(date: Long, number: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            delay(1 * 1000)
            _isLoading.value = false
            when (val result = enqueue { login(buildParams(date, number)) }) {
                is Result.Success -> {
                    _data.value = result
                    _toast.value = "success"
                }
                is Result.Failure -> {
                    _toast.value = result.exception.message
                }
            }
        }
    }

    private fun buildParams(date: Long, number: Int): Map<String, Any> {
        return mapOf(
            "date" to date,
            "num" to number
        )
    }

    private suspend fun login(map: Map<String, Any>): Result<GeeksResponse> {
        val response = geeksService.login(map)
        if (response.isSuccessful) {
            response.body()?.let {
                return Result.Success(it)
            }
        }
        return Result.Failure(Exception("登录失败 ${response.code()} ${response.message()}"))
    }

}

data class UiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val result: List<GeeksModel> = emptyList()
)