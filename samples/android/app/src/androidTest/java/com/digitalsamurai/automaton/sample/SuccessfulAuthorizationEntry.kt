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
import com.digitalsamurai.automaton.sample.AutomatonEnvironment
import com.digitalsamurai.automaton.sample.AutomatonMockWebServer
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performClick

/**
* WARN! AUTO-GENERATED
* Generate with love by Automaton
* Datetime: 1792968885517
*/
@RunWith(AndroidJUnit4::class)
class SuccessfulAuthorizationEntry : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {
    

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    val mockWebServer = AutomatonEnvironment.mockWebServer
    

    @Test
    fun SuccessfulAuthorizationEntry() = run {
        step("Открываем экран авторизации") {
            flakySafely(timeoutMs = 5000) {
                composeTestRule.onNodeWithTag("AuthScreen").assertIsDisplayed()
            }
        }
        step("Вводим пользовательские данные и жмякаем 'Войти'") {
            composeTestRule.onNodeWithTag("LoginField").performTextInput("Username228")
            composeTestRule.onNodeWithTag("PasswordField").performTextInput("secretPassword1234")
            composeTestRule.onNodeWithTag("LoginButton").performClick()
        }
        step("Попадаем на главный экран. Проверяем наличие фич") {
            flakySafely(timeoutMs = 5000) {
                composeTestRule.onNodeWithTag("MainScreen").assertIsDisplayed()
            }
            composeTestRule.onNodeWithTag("LinearGeneration").assertIsDisplayed()
            composeTestRule.onNodeWithTag("BicubicGeneration").assertIsDisplayed()
            composeTestRule.onNodeWithTag("NeighborGeneration").assertIsDisplayed()
        }
    }
}
