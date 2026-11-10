package com.digitalsamurai.automaton.frontend.api

import com.digitalsamurai.automaton.api.Automaton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow


fun Automaton.frontends(): AutomatonFrontends = AutomatonFrontends

public object AutomatonFrontends {}


public interface AutomatonFrontend {

    // TODO отказаться от Flow в API и перейти к синхронному методу обработки входных данных.
    // Frontend должен сам управлять исполнением: последовательно в одном потоке или параллельно по своему усмотрению.
    val tokensFlow: SharedFlow<AutomatonRequest>

    val lexicalAnalyzer: AutomatonLexicalAnalyzer<*>
}