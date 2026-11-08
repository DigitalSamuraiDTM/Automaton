package com.digitalsamurai.automaton.backend.api

import com.digitalsamurai.automaton.api.Automaton
import com.digitalsamurai.automaton.semantic.SemanticModel

public fun Automaton.generators(): AutomatonGenerators {
    return AutomatonGenerators
}

public object AutomatonGenerators {

}

public interface AutomatonGenerator {
    suspend fun generate(model: SemanticModel): GeneratorOutput
}