package com.digitalsamurai.automaton.lexis

import kotlin.time.Duration

sealed interface Terminal<T>: GrammarSymbol<T>

data object ElementReference: Terminal<String> {
    override val representation: String = "ElementReference"
}

data object InputData: Terminal<String> {
    override val representation: String = "InputData"
}

data object TapType: Terminal<TapType.Type> {

    override val representation: String = "TapType"

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