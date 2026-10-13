package com.digitalsamurai.automaton.sample.presentation.screens.auth

import com.digitalsamurai.automaton.sample.core.viewmodel.UiEvent

sealed class AuthScreenEvent: UiEvent() {
    data object AuthException: AuthScreenEvent()
}