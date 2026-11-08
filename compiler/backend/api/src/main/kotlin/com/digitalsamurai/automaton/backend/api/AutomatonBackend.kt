package com.digitalsamurai.automaton.backend.api

import com.digitalsamurai.automaton.semantic.SemanticModel

interface AutomatonBackend {
    suspend fun makeOutput(semanticModel: SemanticModel): BackendResult
}