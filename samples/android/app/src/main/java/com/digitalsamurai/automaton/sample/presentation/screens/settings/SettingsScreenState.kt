package com.digitalsamurai.automaton.sample.presentation.screens.settings

import com.digitalsamurai.automaton.sample.core.viewmodel.UiState
import com.digitalsamurai.automaton.sample.presentation.view.ModConverter

public data class SettingsScreenState(
    public val modConverter: ModConverter.State
) : UiState()