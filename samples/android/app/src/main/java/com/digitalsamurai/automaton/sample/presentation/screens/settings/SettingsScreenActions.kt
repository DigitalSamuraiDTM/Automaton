package com.digitalsamurai.automaton.sample.presentation.screens.settings

import com.digitalsamurai.automaton.sample.core.viewmodel.UiActions
import com.digitalsamurai.automaton.sample.presentation.view.ModConverter

interface SettingsScreenActions : UiActions {
    fun onConverterModSelected(mod: ModConverter.State.Mod)
}