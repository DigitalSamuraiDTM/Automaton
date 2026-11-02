package com.digitalsamurai.automaton.sample.presentation.screens.interpolation.bicubic

import com.digitalsamurai.automaton.sample.core.viewmodel.UiState
import com.digitalsamurai.automaton.sample.presentation.view.BitmapRenderer

data class BicubicScreenState(
    val bitmapRendererState: BitmapRenderer.State,
) : UiState()