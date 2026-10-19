package com.digitalsamurai.automaton.sample

import org.junit.runner.RunWith
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.kaspersky.components.composesupport.config.withComposeSupport
import org.junit.Rule
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import com.digitalsamurai.automaton.sample.presentation.main.MainActivity
import org.junit.Test
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertIsNotDisplayed

/**
* WARN! AUTO-GENERATED
* Generate with love by Automaton
* Datetime: 1792413310226
*/
@RunWith(AndroidJUnit4::class)
class SuccessfulAnonymousEntry : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {
    

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    

    @Test
    fun SuccessfulAnonymousEntry() = run {
        step("Открываем экран авторизации") {
            flakySafely(timeoutMs = 5000) {
                composeTestRule.onNodeWithTag("AuthScreen").assertIsDisplayed()
            }
        }
        step("Нажимаем на вход анонимным пользователем") {
            composeTestRule.onNodeWithTag("AnonymButton").performClick()
        }
        step("Попадаем на главный экран. Проверяем наличие генераций для анонимов") {
            flakySafely(timeoutMs = 5000) {
                composeTestRule.onNodeWithTag("MainScreen").assertIsDisplayed()
            }
            composeTestRule.onNodeWithTag("LinearGeneration").assertIsDisplayed()
            composeTestRule.onNodeWithTag("BicubicGeneration").assertIsNotDisplayed()
            composeTestRule.onNodeWithTag("NeighborGeneration").assertIsNotDisplayed()
        }
    }
}
