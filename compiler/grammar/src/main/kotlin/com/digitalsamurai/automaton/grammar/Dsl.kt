package com.digitalsamurai.automaton.grammar

internal fun epsilon(): List<GrammarSymbol<*>> = emptyList()

internal typealias Production = List<GrammarSymbol<*>>

internal fun production(vararg values: GrammarSymbol<*>): Production = listOf(*values)