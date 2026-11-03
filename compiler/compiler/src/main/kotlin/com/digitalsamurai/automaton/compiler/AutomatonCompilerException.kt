package com.digitalsamurai.automaton.compiler

import com.digitalsamurai.automaton.api.AutomatonException

class AutomatonCompilerException(
    override val cause: Exception,
) : AutomatonException() {
    override val message: String = "Unexpected error during Automaton compilation: ${cause.javaClass.name}: ${cause.message}"
}
