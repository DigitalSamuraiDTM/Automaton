package com.digitalsamurai.automaton.parser.api

import com.digitalsamurai.automaton.ast.AstNode
import com.digitalsamurai.automaton.grammar.Token


interface AutomatonParser {
    val isInitialized: Boolean
    fun initialize()

    fun parse(tokens: Sequence<Token<*>>): AstNode
}