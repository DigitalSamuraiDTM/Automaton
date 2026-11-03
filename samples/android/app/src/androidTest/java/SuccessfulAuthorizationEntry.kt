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
import org.junit.BeforeClass
import org.junit.AfterClass
import android.Manifest
import com.digitalsamurai.automaton.sample.AutomatonTheme
import com.digitalsamurai.automaton.sample.AutomatonMockRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performClick

/**
* WARN! AUTO-GENERATED
* Generate with love by Automaton
* Datetime: 04.11.2026 02:38:16
*/
@RunWith(AndroidJUnit4::class)
class SuccessfulAuthorizationEntry : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {
    

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    
    companion object {
        val mockWebServer = AutomatonEnvironment.mockWebServer
    
        @JvmStatic
        @BeforeClass
        fun beforeClass() {
            mockWebServer.start()
        }
    
        @JvmStatic
        @AfterClass
        fun afterClass() {
            mockWebServer.stop()
        }
    }
    
    @Test
    fun SuccessfulAuthorizationEntry() = before {
        AutomatonEnvironment.permissionManager.applyPermission(
            testContext = this,
            permission = Manifest.permission.CAMERA,
            isGranted = true,
        )
        AutomatonEnvironment.permissionManager.applyPermission(
            testContext = this,
            permission = Manifest.permission.RECORD_AUDIO,
            isGranted = true,
        )
        AutomatonEnvironment.themeManager.applyTheme(
            theme = AutomatonTheme.LIGHT,
        )
        mockWebServer.setGeneralRules(
            setOf(
                AutomatonMockRule(
                    path = "/auth",
                    method = "POST",
                    code = 200,
                        headers = mapOf(
                            "Content-Type" to "application/json",
                        ),
                    body = "{\"token\":\"exampleOfToken1234\"}",
                ),
            )
        )
    }.after {
        mockWebServer.setGeneralRules(emptySet())
        AutomatonEnvironment.themeManager.restoreTheme()
    }.run {
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
