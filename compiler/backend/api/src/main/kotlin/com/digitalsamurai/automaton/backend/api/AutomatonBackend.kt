package com.digitalsamurai.automaton.backend.api

import com.digitalsamurai.automaton.api.Automaton
import com.digitalsamurai.automaton.semantic.SemanticModel

public fun Automaton.backends(): AutomatonBackends {
    return AutomatonBackends
}

public object AutomatonBackends {

}

public interface AutomatonBackend {
    fun buildOutput(model: SemanticModel): Result<BackendOutput>
}