package com.digitalsamurai.automaton.compiler

import com.digitalsamurai.automaton.backend.api.AutomatonBackend
import com.digitalsamurai.automaton.frontend.api.AutomatonFrontend

public class AutomatonCompiler<T: AutomatonFrontend> internal constructor(
    private val backends: List<AutomatonBackend>,
    private val frontend: T,
) {

    init {

    }
}