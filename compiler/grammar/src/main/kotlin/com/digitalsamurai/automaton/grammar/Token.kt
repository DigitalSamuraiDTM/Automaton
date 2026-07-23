package com.digitalsamurai.automaton.grammar

data class Token<T>(
    val symbol: GrammarSymbol<T>,
    val value: T,
    val sourceLocation: String
) {
    override fun toString(): String {
        return "$symbol:$value"
    }
}


