package com.digitalsamurai.automaton.sample.core.navigation

import androidx.navigation.NavHostController
import com.digitalsamurai.automaton.sample.core.screen.BaseScreen

class AppNavigator(
    private val navHostController: NavHostController
) {

    fun navigate(screen: BaseScreen<*, *, *>) {
        navHostController.navigate(screen.screenRoute)
    }

    fun newRoot(screen: BaseScreen<*, *, *>) {
        navHostController.navigate(screen.screenRoute) {
            popUpTo(0) { inclusive = true }
        }
    }
}