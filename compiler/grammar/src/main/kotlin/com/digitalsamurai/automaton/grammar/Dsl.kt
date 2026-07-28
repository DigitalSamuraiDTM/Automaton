package com.digitalsamurai.automaton.grammar

import kotlin.reflect.KClass

internal fun epsilon(): List<KClass<GrammarSymbol<*>>> = emptyList()

internal typealias Production = List<KClass<out GrammarSymbol<*>>>

internal fun production(vararg values: KClass<out GrammarSymbol<*>>): Production = listOf(*values)