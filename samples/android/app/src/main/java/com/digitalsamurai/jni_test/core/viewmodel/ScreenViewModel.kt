package com.digitalsamurai.automaton.sample.core.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class ScreenViewModel<STATE : UiState, EVENT : UiEvent, ACTIONS : UiActions>(
) : ViewModel() {

    // UI STATE MANAGEMENT

    abstract fun initialState(): STATE

    @Suppress("UNCHECKED_CAST")
    open fun getActions(): ACTIONS = this as ACTIONS

    private val _state: MutableStateFlow<STATE> = MutableStateFlow(initialState())
    val state: StateFlow<STATE> = _state

    private var _events = MutableSharedFlow<EVENT>(extraBufferCapacity = 1)
    val events = _events

    protected fun updateState(update: (STATE) -> STATE) {
        _state.update { state ->
            update(state)
        }
    }

    protected fun event(event: EVENT) {
        _events.tryEmit(event)
    }

    private companion object {
        private const val SPAN_PREFIX_NAME = "VM."
    }
}