package com.digitalsamurai.automaton.sample.presentation.screens.gallery

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.digitalsamurai.automaton.sample.core.navigation.AppNavigator
import com.digitalsamurai.automaton.sample.core.viewmodel.ScreenViewModel
import com.digitalsamurai.automaton.sample.data.repositories.BitmapRepository
import com.digitalsamurai.automaton.sample.presentation.view.BitmapRenderer
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = GalleryScreenViewModel.Factory::class)
class GalleryScreenViewModel @AssistedInject constructor(
    private val bitmapRepository: BitmapRepository,
    @Assisted private val navigator: AppNavigator,
) : ScreenViewModel<GalleryScreenState, GalleryScreenEvent, GalleryScreenActions>(
), GalleryScreenActions {

    @AssistedFactory
    interface Factory {
        fun get(navigator: AppNavigator): GalleryScreenViewModel
    }

    override fun initialState(): GalleryScreenState {
        return GalleryScreenState(bitmapItems = emptyList())
    }

    init {
        loadBitmaps()
    }

    private fun loadBitmaps() {
        viewModelScope.launch(Dispatchers.IO) {
            val paths = bitmapRepository.getMetaInfo()
            updateState { state ->
                state.copy(bitmapItems = paths.map {
                    BitmapRenderer.State.ContentBitmapPath(
                        id = it.second
                    )
                }
                )
            }
        }
    }

    override fun onGalleryItemClicked(itemId: String?) {

    }

    override fun onGalleryItemLoad(itemId: String): Deferred<Bitmap> {
        return viewModelScope.async {
            bitmapRepository.get(itemId)
        }
    }
}