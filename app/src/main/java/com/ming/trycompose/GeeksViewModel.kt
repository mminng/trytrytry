package com.ming.trycompose

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

    private val _message: Channel<String> = Channel()
    val message: Flow<String> = _message.receiveAsFlow()

    private val _result: MutableStateFlow<Result<GeeksResponse>> = MutableStateFlow(Result.Loading)
    val result: StateFlow<Result<GeeksResponse>> = _result

    fun load(date: Long, number: Int) {
        _result.value = Result.Loading
        viewModelScope.launch {
            delay(3 * 1000)
            val result = enqueue { login(buildParams(date, number)) }
            _result.value = result
            if (result is Result.Failure) {
                result.exception.message?.let {
                    _message.send(it)
                }
            }
            if (result is Result.Success) {
                _message.send(result.data.geeksList[0].bannerList[3].data.cover.feed)
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