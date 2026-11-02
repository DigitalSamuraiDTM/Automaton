package com.digitalsamurai.automaton.sample.presentation.screens.interpolation.neighbor

import com.digitalsamurai.automaton.sample.core.viewmodel.UiEvent

sealed class NeighborScreenEvent : UiEvent() {
    sealed class BitmapSaving : NeighborScreenEvent() {
        class Success(val fileName: String) : NeighborScreenEvent()
        data object Failed : NeighborScreenEvent()
    }
}