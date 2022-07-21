package com.ming.trycompose

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by zh on 2021/8/17.
 */
class GeeksViewModel : ViewModel() {

    private val geeksService: GeeksService = GeeksService.create()

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _result: MutableStateFlow<Result<GeeksResponse>> = MutableStateFlow(Result.Loading)
    private val _message: MutableStateFlow<String> = MutableStateFlow("")

    val uiState: StateFlow<UiState> =
        combine(_isLoading, _result, _message) { isLoading, result, message ->
            when (result) {
                is Result.Loading -> {
                    UiState(isLoading = isLoading)
                }
                is Result.Success -> {
                    UiState(data = result.data)
                }
                is Result.Failure -> {
                    UiState(message = message)
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState())

    fun load(date: Long, number: Int) {
        uiState.value.copy(
            isLoading = true
        )
        viewModelScope.launch {
            val map: Map<String, String> =
                mapOf("date" to date.toString(), "num" to number.toString())
            delay(3 * 1000)
            val result = enqueue { login(map) }
            uiState.value.copy(
                isLoading = false,
                data = if (result is Result.Success) result.data else GeeksResponse()
            )
        }
    }

    private suspend fun login(map: Map<String, String>): Result<GeeksResponse> {
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
    val data: GeeksResponse = GeeksResponse(),
    val message: String = ""
)