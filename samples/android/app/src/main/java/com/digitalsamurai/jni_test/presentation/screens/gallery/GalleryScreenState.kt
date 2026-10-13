package com.digitalsamurai.automaton.sample.presentation.screens.gallery

import com.digitalsamurai.automaton.sample.core.viewmodel.UiState
import com.digitalsamurai.automaton.sample.presentation.view.BitmapRenderer

data class GalleryScreenState(
    val bitmapItems: List<BitmapRenderer.State>,
) : UiState()