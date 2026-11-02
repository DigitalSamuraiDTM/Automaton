package com.digitalsamurai.automaton.sample.presentation.screens.interpolation.linear

import com.digitalsamurai.automaton.sample.core.viewmodel.UiEvent

sealed class LinearScreenEvent : UiEvent() {

    data object NetworkException: LinearScreenEvent()

    sealed class BitmapSaving : LinearScreenEvent() {
        class Success(val fileName: String) : LinearScreenEvent()
        data object Failed : LinearScreenEvent()
    }
}
