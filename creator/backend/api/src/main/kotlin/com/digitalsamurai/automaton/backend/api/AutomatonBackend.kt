package com.digitalsamurai.automaton.backend.api

import com.digitalsamurai.automaton.logger.api.AutomatonLogger
import com.digitalsamurai.automaton.semantic.SemanticModel

interface AutomatonBackend {
    suspend fun makeOutput(semanticModel: SemanticModel, logger: AutomatonLogger?): BackendResult
}