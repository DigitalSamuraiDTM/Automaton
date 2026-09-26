package com.digitalsamurai.automaton.parser.api

import com.digitalsamurai.automaton.ast.Ast
import com.digitalsamurai.automaton.grammar.Token


interface AutomatonParser {
    val isInitialized: Boolean
    fun initialize()

    fun parse(tokens: List<Token<*>>): Ast
}