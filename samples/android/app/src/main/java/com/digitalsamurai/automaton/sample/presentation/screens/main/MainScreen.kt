package com.digitalsamurai.automaton.sample.presentation.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.digitalsamurai.automaton.sample.core.navigation.AppNavigator
import com.digitalsamurai.automaton.sample.core.screen.BaseScreen
import com.digitalsamurai.automaton.sample.presentation.view.items.FeatureItem

object MainScreen : BaseScreen<MainScreenState, MainScreenEvent, MainScreenActions>() {

    override val routeName: String = "main"
    override val screenName: String = "MainScreen"
    override val isNavigationBarEnabled: Boolean = true

    @Composable
    override fun MakeViewModel(navigator: AppNavigator): MainScreenViewModel {
        return hiltViewModel<MainScreenViewModel, MainScreenViewModel.Factory> { f ->
            f.get(navigator)
        }
    }

    override suspend fun onEvent(
        event: MainScreenEvent,
        actions: MainScreenActions,
        snackbar: SnackbarHostState
    ) {
        when (event) {
            is MainScreenEvent.UnknownFeature -> snackbar.showSnackbar(
                message = "Unknown feature!",
                actionLabel = "Obama",
                duration = SnackbarDuration.Short
            )
        }
    }

    @Composable
    override fun Screen(
        state: MainScreenState,
        actions: MainScreenActions,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.featuresItems) { feature ->
                    FeatureItem(
                        modifier = Modifier
                            .height(110.dp)
                            .fillMaxWidth()
                            .testTag(feature.id),
                        state = feature,
                        onClick = { actions.onFeatureItemClicked(feature.id) }
                    )
                }
            }
        }
    }
}