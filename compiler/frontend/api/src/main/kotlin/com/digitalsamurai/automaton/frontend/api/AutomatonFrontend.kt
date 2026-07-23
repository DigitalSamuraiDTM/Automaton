package com.digitalsamurai.automaton.frontend.api

import com.digitalsamurai.automaton.api.Automaton
import com.digitalsamurai.automaton.grammar.Token
import kotlinx.coroutines.flow.Flow


fun Automaton.frontends(): AutomatonFrontends = AutomatonFrontends

public object AutomatonFrontends {}


public interface AutomatonFrontend {

    val tokensFlow: Flow<Sequence<Token<*>>>

    val lexicalAnalyzer: AutomatonLexicalAnalyzer<*>
}