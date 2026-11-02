package com.digitalsamurai.automaton.sample.presentation.screens.interpolation.neighbor

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.digitalsamurai.automaton.sample.core.navigation.AppNavigator
import com.digitalsamurai.automaton.sample.core.viewmodel.ScreenViewModel
import com.digitalsamurai.automaton.sample.data.network.repository.AuthRepository
import com.digitalsamurai.automaton.sample.data.network.requests.frontback.GetNeighborConfigRequest
import com.digitalsamurai.automaton.sample.data.repositories.BitmapRepository
import com.digitalsamurai.automaton.sample.presentation.view.BitmapRenderer
import com.digitalsamurai.opentelemetry.example.core.network.NetworkHttpClient
import com.digitsamurai.algos.BitmapGenerator
import com.digitsamurai.utils.extensions.generateName
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = NeighborScreenViewModel.Factory::class)
class NeighborScreenViewModel @AssistedInject constructor(
    private val bitmapGenerator: BitmapGenerator,
    private val bitmapRepository: BitmapRepository,
    private val networkHttpClient: NetworkHttpClient,
    private val authRepository: AuthRepository,
    @Assisted private val navigator: AppNavigator,
) : ScreenViewModel<NeighborScreenState, NeighborScreenEvent, NeighborScreenActions>(
), NeighborScreenActions {

    @AssistedFactory
    interface Factory {
        fun get(navigator: AppNavigator): NeighborScreenViewModel
    }

    private val isAutosaveEnabled: Boolean = true

    override fun initialState(): NeighborScreenState {
        return NeighborScreenState(
            bitmapRendererState = BitmapRenderer.State.Empty,
            isButtonLoading = false
        )
    }

    override fun undoImageSaving(bitmapId: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val isDeleted = bitmapRepository.delete(bitmapId)
            Log.d("OBAMA", "Image delete ${isDeleted}: ${bitmapId}")
        }
    }

    override fun onBitmapRendererClicked(id: String) {
        TODO("Not yet implemented")
    }

    override fun onGenerateButtonClicked() {
        viewModelScope.launch(Dispatchers.Default) {
            updateState { it.copy(isButtonLoading = true) }
            val token = authRepository.get() ?: error("No auth token")
            val config = networkHttpClient.makeNetworkRequest(GetNeighborConfigRequest(token), Unit)
                .getOrElse { t ->
                    updateState { it.copy(isButtonLoading = false) }
                    return@launch
                }
            val bitmap = bitmapGenerator.neighborBitmap(
                size = BitmapGenerator.Size(config.width, config.height),
                neighborConfig = BitmapGenerator.NeighborConfig.Random(config.points)
            )
            val bitmapId = bitmap.generateName()
            updateState {
                it.copy(
                    bitmapRendererState = BitmapRenderer.State.ContentBitmap(
                        bitmap = bitmap,
                        id = bitmapId,
                    ),
                    isButtonLoading = false
                )
            }
            if (isAutosaveEnabled) {
                val isSaved = autosaveBitmap(bitmap, bitmapId)
            }
        }
    }

    private suspend fun autosaveBitmap(bitmap: Bitmap, id: String): Boolean {
        val isSaved = bitmapRepository.set(bitmap, BitmapRepository.Name.Value(id))
        val event = if (isSaved) {
            NeighborScreenEvent.BitmapSaving.Success(id)
        } else {
            NeighborScreenEvent.BitmapSaving.Failed
        }
        event(event)
        return isSaved
    }
}