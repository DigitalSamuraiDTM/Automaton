package com.digitalsamurai.automaton.grammar

/**
 * эпсилон переход фактически указывает на нуллабельность данного нетерминала
 */
internal fun epsilon(): List<GrammarSymbol<*>> = emptyList()

internal typealias Production = List<GrammarSymbol<*>>

internal fun production(vararg values: GrammarSymbol<*>): Production = listOf(*values)