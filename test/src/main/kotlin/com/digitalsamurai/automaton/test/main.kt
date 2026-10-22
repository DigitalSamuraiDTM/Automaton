package com.digitalsamurai.automaton.test

import com.digitalsamurai.automaton.api.Automaton
import com.digitalsamurai.automaton.backend.android.kaspresso.AndroidAppActivity
import com.digitalsamurai.automaton.backend.android.kaspresso.AndroidKaspressoBackend
import com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.ManualTestCaseBackend
import com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.language.Language
import com.digitalsamurai.automaton.compiler.compiler
import com.digitalsamurai.automaton.frontend.api.AutomatonResult
import com.digitalsamurai.automaton.frontend.yaml.YamlFrontend
import com.digitalsamurai.automaton.frontend.yaml.YamlInput
import com.digitalsamurai.automaton.logger.api.AutomatonLogger
import com.digitalsamurai.automaton.logger.terminal.TerminalLogger
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import java.io.File

fun main(): Unit = runBlocking {
    val yamlFrontend = YamlFrontend()
    val resultFlow = MutableSharedFlow<AutomatonResult>(extraBufferCapacity = 10, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val compiler = Automaton.compiler.factory(
        frontend = yamlFrontend,
    ) {
        backends(
            ManualTestCaseBackend(
                language = Language.RU,
                outputDirectory = File("/Users/nugaev.andrey/Desktop/Projects/Automaton/samples/tests/outputs"),
            ),
            AndroidKaspressoBackend(
                outputDirectory = File("/Users/nugaev.andrey/Desktop/Projects/Automaton/samples/android/app/src/androidTest/java/com/digitalsamurai/automaton/sample"),
                appPackage = "com.digitalsamurai.automaton.sample",
                activity = AndroidAppActivity(
                    activityPackage = "com.digitalsamurai.automaton.sample.presentation.main",
                    name = "MainActivity",
                ),
                composeApp = true,
            )
        )
        logger(TerminalLogger(enabledLevels = AutomatonLogger.LogLevel.entries.toSet()))
        resultFlow(resultFlow)
    }
    val start = System.currentTimeMillis()
//    yamlFrontend.compile(YamlInput(File("/Users/nugaev.andrey/Desktop/Projects/Automaton/samples/tests/test_anonymous_auth.yaml")))
    yamlFrontend.compile(YamlInput(File("/Users/nugaev.andrey/Desktop/Projects/Automaton/samples/tests/test_success_auth.yaml")))

    resultFlow.take(2).collect { result ->
        println(result)
    }
    println("FINISHED: ${System.currentTimeMillis() - start}")


}