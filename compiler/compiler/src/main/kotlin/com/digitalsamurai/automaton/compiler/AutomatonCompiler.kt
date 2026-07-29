package com.digitalsamurai.automaton.compiler

import com.digitalsamurai.automaton.backend.api.AutomatonBackend
import com.digitalsamurai.automaton.frontend.api.AutomatonFrontend
import com.digitalsamurai.automaton.parser.api.AutomatonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

public class AutomatonCompiler<T: AutomatonFrontend> internal constructor(
    private val backends: List<AutomatonBackend>,
    private val frontend: T,
    private val parser: AutomatonParser,
) {

    private val automatonScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    init {
        listenFrontend()
    }

    private fun listenFrontend() {
        automatonScope.launch {
            frontend.tokensFlow.collect { sequence ->
                println("OUT: " + sequence.toList().toString())
            }
        }
    }
}