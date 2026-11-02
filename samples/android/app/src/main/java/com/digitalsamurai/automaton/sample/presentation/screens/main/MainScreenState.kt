package com.digitalsamurai.automaton.sample.presentation.screens.main

import com.digitalsamurai.automaton.sample.core.viewmodel.UiState
import com.digitalsamurai.automaton.sample.presentation.view.items.FeatureItem

data class MainScreenState(
    val featuresItems: List<FeatureItem.State>,
) : UiState()