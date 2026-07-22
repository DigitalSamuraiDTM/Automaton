package com.digitalsamurai.automaton.lexis

data class Token<T>(
    val nonTerminal: GrammarSymbol<T>,
    val value: T,
    val sourceLocation: String
)


