package com.digitalsamurai.automaton.backend.api

import com.digitalsamurai.automaton.api.Automaton
import com.digitalsamurai.automaton.semantic.SemanticModel
import com.digitalsamurai.automaton.utils.Fingerprinted

public fun Automaton.generators(): AutomatonGenerators {
    return AutomatonGenerators
}

public object AutomatonGenerators {

}

public interface AutomatonGenerator: Fingerprinted {

    suspend fun generate(model: SemanticModel): GeneratorOutput

    /**
     * version of generator.
     * **Fingerprint will be changed after bump version**
     */
    val version: String
}