package com.digitalsamurai.automaton.sample.presentation.screens.interpolation.neighbor

import com.digitalsamurai.automaton.sample.core.viewmodel.UiActions

interface NeighborScreenActions : UiActions {
    fun onBitmapRendererClicked(id: String)
    fun onGenerateButtonClicked()
    fun undoImageSaving(bitmapId: String)
}