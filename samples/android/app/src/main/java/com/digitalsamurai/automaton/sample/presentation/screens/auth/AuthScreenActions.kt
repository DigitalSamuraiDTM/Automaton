package com.digitalsamurai.automaton.sample.presentation.screens.auth

import com.digitalsamurai.automaton.sample.core.viewmodel.UiActions

interface AuthScreenActions: UiActions {

    fun onLoginButtonClicked()

    fun onAnonymButtonClicked()

    fun onLoginInput(text: String)

    fun onPasswordInput(text: String)
}