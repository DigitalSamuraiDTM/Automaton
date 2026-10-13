package com.digitalsamurai.automaton.sample.presentation.screens.main

import com.digitalsamurai.automaton.sample.core.viewmodel.UiActions

interface MainScreenActions : UiActions {
    fun generateNoises()
    fun onFeatureItemClicked(itemId: String)
}