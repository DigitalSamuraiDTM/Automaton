package com.digitalsamurai.automaton.sample.presentation.screens.interpolation.linear

import com.digitalsamurai.automaton.sample.core.viewmodel.UiActions

interface LinearScreenActions : UiActions {
    fun onBitmapRendererClicked(id: String)
    fun onGenerateButtonClicked()
    fun undoImageSaving(id: String)
}