package com.digitalsamurai.automaton.sample.presentation.screens.main

import com.digitalsamurai.automaton.sample.core.viewmodel.UiEvent

sealed class MainScreenEvent : UiEvent() {

    data class UnknownFeature(val name: String) : MainScreenEvent()
}