package com.digitalsamurai.automaton.test

import com.digitalsamurai.automaton.api.Automaton
import com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.ManualTestCaseBackend
import com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.language.Language
import com.digitalsamurai.automaton.compiler.compiler
import com.digitalsamurai.automaton.frontend.api.AutomatonResult
import com.digitalsamurai.automaton.frontend.yaml.YamlFrontend
import com.digitalsamurai.automaton.frontend.yaml.YamlInput
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.File

fun main(): Unit = runBlocking {
    val yamlFrontend = YamlFrontend()
    val resultFlow = MutableSharedFlow<AutomatonResult>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val compiler = Automaton.compiler.factory(
        frontend = yamlFrontend,
    ) {
        backends(
            ManualTestCaseBackend(
                language = Language.RU,
                outputDirectory = File("/Users/nugaev.andrey/Desktop/Projects/Automaton"),
            )
        )
        resultFlow(resultFlow)
    }
    val start = System.currentTimeMillis()
    yamlFrontend.compile(YamlInput(File("/Users/nugaev.andrey/Desktop/Projects/Automaton/samples/tests/tree_example.yaml")))

    val finishResult = resultFlow.first()
    println(finishResult)
    println("FINISHED: ${System.currentTimeMillis() - start}")


}