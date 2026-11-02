package com.digitalsamurai.automaton.sample.presentation.screens.interpolation.linear

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.digitalsamurai.automaton.sample.core.navigation.AppNavigator
import com.digitalsamurai.automaton.sample.core.viewmodel.ScreenViewModel
import com.digitalsamurai.automaton.sample.data.network.requests.frontback.GetLinearConfigRequest
import com.digitalsamurai.automaton.sample.data.repositories.BitmapRepository
import com.digitalsamurai.automaton.sample.domain.GenerateBilinearImageUseCase
import com.digitalsamurai.automaton.sample.presentation.view.BitmapRenderer
import com.digitalsamurai.opentelemetry.example.core.network.NetworkHttpClient
import com.digitsamurai.algos.BitmapGenerator
import com.digitsamurai.utils.extensions.generateName
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = LinearScreenScreenViewModel.Factory::class)
class LinearScreenScreenViewModel @AssistedInject constructor(
    private val generateBilinearImageUseCase: GenerateBilinearImageUseCase,
    private val bitmapRepository: BitmapRepository,
    private val networkHttpClient: NetworkHttpClient,
    @Assisted private val navigator: AppNavigator,
) : ScreenViewModel<LinearScreenState, LinearScreenEvent, LinearScreenActions>(
), LinearScreenActions {

    @AssistedFactory
    interface Factory {
        fun get(navigator: AppNavigator): LinearScreenScreenViewModel
    }

    private var generatorJob: Job? = null
    private val isAutosaveEnabled = true

    override fun initialState(): LinearScreenState {
        return LinearScreenState(
            bitmapRendererState =
                BitmapRenderer.default(),
            isButtonLoading = false,
        )
    }

    override fun onBitmapRendererClicked(id: String) {
        // TODO
    }

    override fun undoImageSaving(id: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val isDeleted = bitmapRepository.delete(id)
            Log.d("OBAMA", "Image delete ${isDeleted}: ${id}")
        }
    }

    override fun onGenerateButtonClicked() {
        generatorJob?.cancel()
        generatorJob = viewModelScope.launch(Dispatchers.Default) {
            updateState {
                it.copy(isButtonLoading = true)
            }
            val configuration = networkHttpClient.makeNetworkRequest(
                networkHttpRequest = GetLinearConfigRequest(),
                data = Unit,
            ).getOrElse { t ->
                event(LinearScreenEvent.NetworkException)
                updateState {
                    it.copy(isButtonLoading = false)
                }
                return@launch
            }

            val bitmap = generateBilinearImageUseCase(
                size = BitmapGenerator.Size(configuration.width, configuration.height),
                bilinearConfig = BitmapGenerator.BilinearConfig.Matrix(configuration.resolution)
            )
            val bitmapName = bitmap.generateName()
            updateState {
                it.copy(
                    bitmapRendererState = BitmapRenderer.State.ContentBitmap(
                        bitmap = bitmap,
                        id = bitmapName,
                    ),
                    isButtonLoading = false
                )
            }
            if (isAutosaveEnabled) {
                val isSaved = autosaveBitmap(bitmap, bitmapName)
            }
        }
    }

    private suspend fun autosaveBitmap(bitmap: Bitmap, name: String): Boolean {
        val isSaved = bitmapRepository.set(bitmap, BitmapRepository.Name.Value(name))
        val event = if (isSaved) LinearScreenEvent.BitmapSaving.Success(name) else LinearScreenEvent.BitmapSaving.Failed
        event(event)
        return isSaved
    }

}