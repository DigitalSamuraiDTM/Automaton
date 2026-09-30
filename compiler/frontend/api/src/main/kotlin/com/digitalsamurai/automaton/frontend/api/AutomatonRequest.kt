package com.digitalsamurai.automaton.frontend.api

import com.digitalsamurai.automaton.api.AutomatonException
import com.digitalsamurai.automaton.grammar.Token

data class AutomatonRequest(
    val requestId: String,
    val tokens: List<Token<*>>,
)

sealed class AutomatonResult {
    abstract val requestId: String

    data class Failure(override val requestId: String, val exception: AutomatonException) : AutomatonResult()
    data class Success(override val requestId: String, val backendMetadata: String) : AutomatonResult()
}