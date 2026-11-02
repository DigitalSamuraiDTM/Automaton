package com.digitalsamurai.automaton.sample.presentation.screens.interpolation.bicubic

import androidx.lifecycle.viewModelScope
import com.digitalsamurai.automaton.sample.core.navigation.AppNavigator
import com.digitalsamurai.automaton.sample.core.viewmodel.ScreenViewModel
import com.digitalsamurai.automaton.sample.presentation.view.BitmapRenderer
import com.digitsamurai.algos.BitmapGenerator
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = BicubicScreenViewModel.Factory::class)
class BicubicScreenViewModel @AssistedInject constructor(
    private val bitmapGenerator: BitmapGenerator,
    @Assisted private val navigator: AppNavigator,
) : ScreenViewModel<BicubicScreenState, BicubicScreenEvent, BicubicScreenActions>(), BicubicScreenActions {

    @AssistedFactory
    interface Factory {
        fun get(navigator: AppNavigator): BicubicScreenViewModel
    }

    override fun initialState(): BicubicScreenState {
        return BicubicScreenState(BitmapRenderer.default())
    }

    override fun onGenerateButtonClicked() {
        viewModelScope.launch(Dispatchers.Default) {

            val bitmap = bitmapGenerator.bicubicBitmap(
                size = BitmapGenerator.Size(1000, 1000)
            )

            updateState { state ->
                state.copy(
                    bitmapRendererState = BitmapRenderer.State.ContentBitmap(
                        bitmap, "example"
                    )
                )
            }
        }
    }

    override fun onBitmapRendererClicked(id: String) {
        TODO("Not yet implemented")
    }
}