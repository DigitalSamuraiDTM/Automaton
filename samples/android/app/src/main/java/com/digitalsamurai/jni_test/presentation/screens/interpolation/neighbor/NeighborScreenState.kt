package com.digitalsamurai.automaton.sample.presentation.screens.interpolation.neighbor

import com.digitalsamurai.automaton.sample.core.viewmodel.UiState
import com.digitalsamurai.automaton.sample.presentation.view.BitmapRenderer

data class NeighborScreenState(
    val bitmapRendererState: BitmapRenderer.State,
    val isButtonLoading: Boolean,
) : UiState()