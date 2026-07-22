package com.digitalsamurai.automaton.compiler

import com.digitalsamurai.automaton.backend.api.AutomatonBackend
import com.digitalsamurai.automaton.frontend.api.AutomatonInput

public class AutomatonCompiler<T: AutomatonInput> internal constructor(
    private val backends: List<AutomatonBackend>
) {


    suspend fun compile(input: T): Result<Unit> {
        return Result.success(Unit)
    }
}