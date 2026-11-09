package com.digitalsamurai.automaton.backend.api

import com.digitalsamurai.automaton.integrator.api.AutomatonIntegrator
import com.digitalsamurai.automaton.logger.api.AutomatonLogger
import com.digitalsamurai.automaton.semantic.CanonicalSemanticModel
import com.digitalsamurai.automaton.semantic.SemanticModel
import com.digitalsamurai.automaton.utils.fingerprint

public class AutomatonBackendBase(
    private val generator: AutomatonGenerator,
    private val integrator: AutomatonIntegrator,
): AutomatonBackend {

    override suspend fun makeOutput(semanticModel: SemanticModel, logger: AutomatonLogger?): BackendResult {
        val modelFingerprint = CanonicalSemanticModel.fromSemanticModel(semanticModel).fingerprint()
        logger?.info("Semantic model fingerprint: $modelFingerprint")
        val generatedData = generator.generate(semanticModel)

        val integratedResult = integrator.integrate(generatedData)

        return BackendResult(
            requestId = semanticModel.requestId,
            metadata = semanticModel.test.metadata.id,
        )
    }
}