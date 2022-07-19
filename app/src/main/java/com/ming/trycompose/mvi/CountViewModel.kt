package com.ming.trycompose.mvi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by zh on 2022/7/19.
 */
class CountViewModel : ViewModel() {

    data class UiState(val addCount: Int = 0, val removeCount: Int = 0)

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private var _count: Int = 0

    sealed class UiAction {
        class Add(val count: Int) : UiAction()
        class Remove(val count: Int) : UiAction()
    }

    fun executeAction(action: UiAction) {
        when (action) {
            is UiAction.Add -> {
                _count += action.count
                _uiState.value = uiState.value.copy(
                    addCount = _count
                )
            }
            is UiAction.Remove -> {
                _count -= action.count
                _uiState.value = uiState.value.copy(
                    removeCount = _count
                )
            }
        }
    }

}