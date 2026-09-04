package com.digitalsamurai.automaton.backend.api

import com.digitalsamurai.automaton.api.Automaton
import com.digitalsamurai.automaton.ast.AstNode

public fun Automaton.backends(): AutomatonBackends {
    return AutomatonBackends
}

public object AutomatonBackends {

}

public interface AutomatonBackend {
    fun buildOutput(tree: AstNode<*>): Result<BackendOutput>
}