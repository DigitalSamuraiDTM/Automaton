package com.digitalsamurai.automaton.frontend.api

import com.digitalsamurai.automaton.api.Automaton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow


fun Automaton.frontends(): AutomatonFrontends = AutomatonFrontends

public object AutomatonFrontends {}


public interface AutomatonFrontend {

    val tokensFlow: SharedFlow<AutomatonRequest>

    val lexicalAnalyzer: AutomatonLexicalAnalyzer<*>
}