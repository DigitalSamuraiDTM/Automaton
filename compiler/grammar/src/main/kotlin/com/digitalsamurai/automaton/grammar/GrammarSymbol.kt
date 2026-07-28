package com.digitalsamurai.automaton.grammar

import kotlin.reflect.KClass

public interface GrammarSymbol<T> {
    val representation: String
}