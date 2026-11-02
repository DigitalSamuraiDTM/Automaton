package com.digitalsamurai.automaton.sample.presentation.screens.gallery

import android.graphics.Bitmap
import com.digitalsamurai.automaton.sample.core.viewmodel.UiActions
import kotlinx.coroutines.Deferred

interface GalleryScreenActions : UiActions {

    fun onGalleryItemClicked(itemId: String?)

    fun onGalleryItemLoad(itemId: String): Deferred<Bitmap>
}