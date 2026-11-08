package com.digitalsamurai.automaton.integrator.api

import com.digitalsamurai.automaton.backend.api.GeneratorOutput

public interface AutomatonIntegrator {
    suspend fun integrate(generatorOutput: GeneratorOutput)
}