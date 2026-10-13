package com.digitalsamurai.automaton.sample.presentation.screens.interpolation.bicubic

import com.digitalsamurai.automaton.sample.core.viewmodel.UiActions

interface BicubicScreenActions : UiActions {
    fun onGenerateButtonClicked()
    fun onBitmapRendererClicked(id: String)
}