package com.digitalsamurai.automaton.creator

import com.digitalsamurai.automaton.api.AutomatonException

class AutomatonCreatorException(
    override val cause: Exception,
) : AutomatonException(cause) {
    override val message: String = "Unexpected error during Automaton creation: ${cause.javaClass.name}: ${cause.message}"
}
