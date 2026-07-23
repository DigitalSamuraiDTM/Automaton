package com.digitalsamurai.automaton.frontend.api

import com.digitalsamurai.automaton.grammar.Token

interface AutomatonLexicalAnalyzer<in T> {

    fun analyze(input: T): Sequence<Token<*>>
}