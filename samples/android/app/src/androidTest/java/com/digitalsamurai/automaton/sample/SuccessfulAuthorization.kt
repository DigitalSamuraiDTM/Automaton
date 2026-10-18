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
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performTextInput

/**
* WARN! AUTO-GENERATED
* Generate with love by Automaton
* Datetime: 1792360327976
*/
@RunWith(AndroidJUnit4::class)
class SuccessfulAuthorization : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {
    

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    

    @Test
    fun SuccessfulAuthorization() = run {
        step("Открываем экран авторизации") {
            composeTestRule.onNodeWithTag("loginButton").performClick()
            flakySafely(timeoutMs = 60000) {
                composeTestRule.onNodeWithTag("loginScreen").assertIsDisplayed()
            }
        }
        step("Вводим учетные данные") {
            composeTestRule.onNodeWithTag("loginField").performTextInput("admin")
            composeTestRule.onNodeWithTag("passwordField").performTextInput("password")
        }
        step("Авторизуемся и попадаем на главный экран") {
            composeTestRule.onNodeWithTag("submitButton").performClick()
            flakySafely(timeoutMs = 5000) {
                composeTestRule.onNodeWithTag("homeTitle").assertIsDisplayed()
            }
            composeTestRule.onNodeWithTag("homeTitle").assertIsDisplayed()
        }
    }
}
