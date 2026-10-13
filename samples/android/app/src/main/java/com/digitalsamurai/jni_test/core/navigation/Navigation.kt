package com.digitalsamurai.automaton.sample.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.digitalsamurai.automaton.sample.core.screen.BaseScreen
import com.digitalsamurai.automaton.sample.presentation.screens.auth.AuthScreen
import com.digitalsamurai.automaton.sample.presentation.screens.gallery.GalleryScreen
import com.digitalsamurai.automaton.sample.presentation.screens.interpolation.bicubic.BicubicScreen
import com.digitalsamurai.automaton.sample.presentation.screens.interpolation.linear.LinearScreen
import com.digitalsamurai.automaton.sample.presentation.screens.interpolation.neighbor.NeighborScreen
import com.digitalsamurai.automaton.sample.presentation.screens.main.MainScreen
import com.digitalsamurai.automaton.sample.presentation.screens.settings.SettingsScreen


@Composable
fun Navigation(modifier: Modifier, navController: NavHostController, startScreen: BaseScreen<*, *, *>, onNavigateToScreen: (BaseScreen<*, *, *>) -> Unit) {
    NavHost(modifier = modifier, navController = navController, startDestination = startScreen.screenRoute) {
        appsScreen.forEach { screen ->
            composable(route = screen.screenRoute) {
                onNavigateToScreen(screen)
                screen.NavToScreen(AppNavigator(navController))
            }
        }
    }
}

val appsScreen = setOf<BaseScreen<*, *, *>>(
    AuthScreen,
    SettingsScreen,
    MainScreen,
    LinearScreen,
    NeighborScreen,
    BicubicScreen,
    GalleryScreen
)