package com.digitalsamurai.automaton.backend.android.kaspresso

import com.digitalsamurai.automaton.backend.api.AutomatonBackend
import com.digitalsamurai.automaton.backend.api.BackendOutput
import com.digitalsamurai.automaton.semantic.SemanticModel

class AndroidKaspressoBackend(
    private val authKey: String
): AutomatonBackend {

    override suspend fun buildOutput(model: SemanticModel): BackendOutput {
        TODO("Not yet implemented")
    }
}