package com.digitalsamurai.automaton.sample.presentation.screens.settings

import com.digitalsamurai.automaton.sample.core.navigation.AppNavigator
import com.digitalsamurai.automaton.sample.core.viewmodel.ScreenViewModel
import com.digitalsamurai.automaton.sample.presentation.screens.main.MainScreen
import com.digitalsamurai.automaton.sample.presentation.theme.ThemeController
import com.digitalsamurai.automaton.sample.presentation.theme.ThemeMod
import com.digitalsamurai.automaton.sample.presentation.view.ModConverter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = SettingsScreenViewModel.Factory::class)
class SettingsScreenViewModel @AssistedInject constructor(
    private val themeController: ThemeController,
    @Assisted private val navigator: AppNavigator,
) : ScreenViewModel<SettingsScreenState, SettingsScreenEvent, SettingsScreenActions>(
), SettingsScreenActions {

    @AssistedFactory
    interface Factory {
        fun get(navigator: AppNavigator): SettingsScreenViewModel
    }

    override fun initialState(): SettingsScreenState = SettingsScreenState(
        modConverter = ModConverter.defaultState()
    )

    fun toMainScreen() {
        navigator.navigate(MainScreen)
    }

    override fun onConverterModSelected(mod: ModConverter.State.Mod) {
        updateState { state ->
            themeController.setThemeMod(mod = mod.toThemeMod())
            state.copy(modConverter = state.modConverter.copy(selectedMod = mod))
        }
    }

    private fun ModConverter.State.Mod.toThemeMod(): ThemeMod {
        return when (this) {
            ModConverter.State.Mod.NDK -> ThemeMod.NDK
            ModConverter.State.Mod.KOTLIN -> ThemeMod.KOTLIN
        }
    }
}