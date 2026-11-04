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
import com.digitalsamurai.automaton.sample.AutomatonTheme
import com.digitalsamurai.automaton.sample.AutomatonMockRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performClick

/**
* WARN! AUTO-GENERATED
* Generate with love by Automaton
* Datetime: 04.11.2026 03:19:33
*/
@RunWith(AndroidJUnit4::class)
class LinearImageGeneration : TestCase(
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
    fun LinearImageGeneration() = before {
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
        mockWebServer.setStepRules(emptySet())
        AutomatonEnvironment.themeManager.restoreTheme()
    }.run {
        step("Открываем экран авторизации") {
            flakySafely(timeoutMs = 5000) {
                composeTestRule.onNodeWithTag("AuthScreen").assertIsDisplayed()
            }
        }
        step("Вводим пользовательские данные и авторизуемся") {
            composeTestRule.onNodeWithTag("LoginField").performTextInput("Username228")
            composeTestRule.onNodeWithTag("PasswordField").performTextInput("secretPassword1234")
            composeTestRule.onNodeWithTag("LoginButton").performClick()
        }
        step("Открываем генерацию линейной интерполяции") {
            flakySafely(timeoutMs = 5000) {
                composeTestRule.onNodeWithTag("MainScreen").assertIsDisplayed()
            }
            composeTestRule.onNodeWithTag("LinearGeneration").performClick()
        }
        step("Запускаем генерацию картинки") {
            mockWebServer.setStepRules(
                setOf(
                    AutomatonMockRule(
                        path = "/api/v1/diagram/bilinear/configuration",
                        method = "GET",
                        code = 200,
                            headers = mapOf(
                                "Content-Type" to "application/json",
                            ),
                        body = "{\"width\":32,\"resolution\":4,\"height\":32}",
                    ),
                )
            )
            flakySafely(timeoutMs = 5000) {
                composeTestRule.onNodeWithTag("LinearScreen").assertIsDisplayed()
            }
            composeTestRule.onNodeWithTag("LinearGenerate").performClick()
        }
        step("Проверяем, что картинка сгенерировалась") {
            flakySafely(timeoutMs = 10000) {
                composeTestRule.onNodeWithTag("GeneratedImage").assertIsDisplayed()
            }
        }
    }
}
