package com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.language

import com.digitalsamurai.automaton.ast.AstNode
import com.digitalsamurai.automaton.grammar.Action
import com.digitalsamurai.automaton.grammar.Actions
import com.digitalsamurai.automaton.grammar.Assert
import com.digitalsamurai.automaton.grammar.CompilationUnit
import com.digitalsamurai.automaton.grammar.Condition
import com.digitalsamurai.automaton.grammar.Context
import com.digitalsamurai.automaton.grammar.Delay
import com.digitalsamurai.automaton.grammar.GrammarSymbol
import com.digitalsamurai.automaton.grammar.Input
import com.digitalsamurai.automaton.grammar.Metadata
import com.digitalsamurai.automaton.grammar.NonTerminal
import com.digitalsamurai.automaton.grammar.Step
import com.digitalsamurai.automaton.grammar.Steps
import com.digitalsamurai.automaton.grammar.Tap
import com.digitalsamurai.automaton.grammar.Terminal
import com.digitalsamurai.automaton.grammar.Test
import com.digitalsamurai.automaton.grammar.Wait

interface LanguageExtractor {
    fun extractText(node: AstNode): String
}

private val GrammarSymbol<*>.lineFeedAfter: Boolean
    get() {
    when(this) {
        is NonTerminal -> {
//            nonTerminalLineFeed()
            return false
        }

        is Terminal -> {
            return true
        }

    }
    return true
}

private val NonTerminal.nonTerminalLineFeed: Boolean
    get() = when (this) {
        Action -> TODO()
        Actions -> TODO()
        Assert -> TODO()
        CompilationUnit -> TODO()
        Condition -> TODO()
        Context -> TODO()
        Delay -> TODO()
        Input -> TODO()
        Metadata -> TODO()
        Step -> true
        Steps -> TODO()
        Tap -> TODO()
        Test -> TODO()
        Wait -> TODO()
    }