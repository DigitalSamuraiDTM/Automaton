package com.digitalsamurai.automaton.grammar

import kotlin.time.Duration

sealed interface Terminal<T>: GrammarSymbol<T>

data object ElementReference: Terminal<String> {
    override val representation: String = "elementReference"
}

data object InputData: Terminal<String> {
    override val representation: String = "inputData"
}

data object TapType: Terminal<TapType.Type> {

    override val representation: String = "tapType"

    enum class Type {
        SINGLE,
    }
}

data object Timeout: Terminal<Duration> {
    override val representation: String = "Timeout"
}

data object ElementProperty: Terminal<String> {
    override val representation: String = "ElementProperty"
}

data object PropertyValue: Terminal<String> {
    override val representation: String = "PropertyValue"
}