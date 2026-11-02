package com.digitalsamurai.automaton.sample.presentation.screens.interpolation.linear

import com.digitalsamurai.automaton.sample.core.viewmodel.UiState
import com.digitalsamurai.automaton.sample.presentation.view.BitmapRenderer

data class LinearScreenState(
    val bitmapRendererState: BitmapRenderer.State,
    val isButtonLoading: Boolean,
) : UiState()