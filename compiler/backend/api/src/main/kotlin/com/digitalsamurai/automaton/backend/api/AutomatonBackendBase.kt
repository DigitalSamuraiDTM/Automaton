package com.digitalsamurai.automaton.backend.api

import com.digitalsamurai.automaton.integrator.api.AutomatonIntegrator
import com.digitalsamurai.automaton.semantic.SemanticModel

public class AutomatonBackendBase(
    private val generator: AutomatonGenerator,
    private val integrator: AutomatonIntegrator,
): AutomatonBackend {

    override suspend fun makeOutput(semanticModel: SemanticModel): BackendResult {
        val generatedData = generator.generate(semanticModel)
        val integratedResult = integrator.integrate(generatedData)

        return BackendResult(
            requestId = semanticModel.requestId,
            metadata = semanticModel.test.metadata.id,
        )
    }
}