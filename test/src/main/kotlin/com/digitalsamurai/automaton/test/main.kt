package com.digitalsamurai.automaton.test

import com.digitalsamurai.automaton.api.Automaton
import com.digitalsamurai.automaton.backend.android.kaspresso.AndroidAppActivity
import com.digitalsamurai.automaton.backend.android.kaspresso.AndroidKaspressoGenerator
import com.digitalsamurai.automaton.backend.api.AutomatonBackendBase
import com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.ManualTestCaseGenerator
import com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.language.Language
import com.digitalsamurai.automaton.creator.creator
import com.digitalsamurai.automaton.frontend.api.AutomatonResult
import com.digitalsamurai.automaton.frontend.yaml.YamlFrontend
import com.digitalsamurai.automaton.frontend.yaml.YamlInput
import com.digitalsamurai.automaton.grabber.frontend.openapi.file.OpenApiFileFrontend
import com.digitalsamurai.automaton.grabber.frontend.openapi.file.OpenApiFileInput
import com.digitalsamurai.automaton.grabber.grabber
import com.digitalsamurai.automaton.integrator.files.FileIntegrator
import com.digitalsamurai.automaton.logger.api.AutomatonLogger
import com.digitalsamurai.automaton.logger.terminal.TerminalLogger
import io.swagger.v3.core.util.Json
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import java.io.File

/**
 * Runs the selected manual check.
 *
 * @return Unit after the selected check finishes.
 */
fun main(): Unit = runBlocking {
//    checkCreator()
    checkGrabber()
}

/**
 * Runs the existing creator checks.
 *
 * @return Unit after all configured creator results are received.
 */
private suspend fun checkCreator() {
    val yamlFrontend = YamlFrontend()
    val resultFlow = MutableSharedFlow<AutomatonResult>(extraBufferCapacity = 10, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val creator = Automaton.creator.factory(
        frontend = yamlFrontend,
    ) {
        // TODO если взять за факт то, что
        backends(
            AutomatonBackendBase(
                generator = ManualTestCaseGenerator(Language.RU),
                integrator = FileIntegrator(File("/Users/nugaev.andrey/Desktop/Projects/Automaton/samples/tests/creator/outputs")),
            ),
            AutomatonBackendBase(
                generator = AndroidKaspressoGenerator(
                    appPackage = "com.digitalsamurai.automaton.sample",
                    activity = AndroidAppActivity(
                        activityPackage = "com.digitalsamurai.automaton.sample.presentation.main",
                        name = "MainActivity",
                    ),
                    composeApp = true,
                    mockWebserverHost = "127.0.0.1",
                    mockWebServerPort = 8080,
                ),
                integrator = FileIntegrator(File("/Users/nugaev.andrey/Desktop/Projects/Automaton/samples/android/app/src/androidTest/java/com/digitalsamurai/automaton/sample"))
            )
        )
        logger(TerminalLogger(enabledLevels = AutomatonLogger.LogLevel.entries.toSet()))
        resultFlow(resultFlow)
    }
    val start = System.currentTimeMillis()
//    yamlFrontend.compile(YamlInput(File("/Users/nugaev.andrey/Desktop/Projects/Automaton/samples/tests/creator/test_anonymous_auth.yaml")))
    yamlFrontend.compile(YamlInput(File("/Users/nugaev.andrey/Desktop/Projects/Automaton/samples/tests/creator/test_success_auth.yaml")))
    yamlFrontend.compile(YamlInput(File("/Users/nugaev.andrey/Desktop/Projects/Automaton/samples/tests/creator/test_linear_image_generation.yaml")))
    yamlFrontend.compile(YamlInput(File("/Users/nugaev.andrey/Desktop/Projects/Automaton/samples/tests/creator/test_neighbor_image_generation.yaml")))

    resultFlow.take(3).collect { result ->
        println(result)
    }
    println("FINISHED: ${System.currentTimeMillis() - start}")
}

/**
 * Builds the grabber and prints the parsed frontback OpenAPI specification.
 *
 * @return Unit after the specification is parsed and printed.
 */
private suspend fun checkGrabber() {
    val frontend = OpenApiFileFrontend()
    Automaton.grabber.factory(frontend) {}
    val openApi = frontend.grab(
        OpenApiFileInput(
            File("/Users/nugaev.andrey/Desktop/Projects/Automaton/samples/tests/grabber/frontback-openapi.yaml")
        )
    )
    println(Json.pretty(openApi))
}